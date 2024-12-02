/*
 * Copyright © 2023 T-Systems International GmbH. All Rights Reserved.
 * 
 * Reproduction or transmission in whole or in part, in any form or by any
 * means, is prohibited without the prior written consent of the copyright
 * owner.
 */
package com.dcx.epep.common.delegate;

import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NotContextException;

import com.dcx.epep.logging.EpepApplicationLogger;
import com.tsystems.rse4j.commons.server.factory.BasicExportedService;

/**
 */
public class EjbUtil {

    // define a logger for this class
    private static final EpepApplicationLogger LOGGER =
        EpepApplicationLogger.getEpepApplicationLogger(AbstractEjbDelegate.class);

    /**
     * 
     */
    public Map<String, BasicExportedService> mapOfBeans;

    /**
     * 
     */
    public EjbUtil(Map<String, BasicExportedService> mapOfBeans) {
        this.mapOfBeans = mapOfBeans;
    }

    /**
     * Get the remote interface of the service implementing the given interface.
     * 
     * @param serviceInt The interface of the service.
     * @return The remote interface of the service.
     */
    public static BasicExportedService getEJBServiceByName(String serviceName) {
        BasicExportedService result;
        BasicExportedService exportedServiceCandidate = null, exportedService = null;
        try {
            final InitialContext context = new InitialContext();
            if (context != null) {
                NamingEnumeration<NameClassPair> enumerationApps = context.list("java:app");
                while (enumerationApps.hasMoreElements() && exportedService == null) {
                    NameClassPair next = enumerationApps.next();
                    try {
                        final NamingEnumeration<NameClassPair> enumeration = context.list("java:app/" + next.getName());
                        while (enumeration.hasMoreElements() && exportedService == null) {
                            final NameClassPair obj = enumeration.next();
                            final String beanInterface =
                                obj.getName().indexOf("!") > 0 ? obj.getName().substring(obj.getName().indexOf("!") + 1) : null;

                            final Class<?> beanClass = Class.forName(obj.getClassName());
                            final Class<?> beanInterfaceClass = beanInterface != null ? Class.forName(beanInterface) : null;

                            final String beanObjName = beanClass.getSimpleName();
                            final String beanInterfaceName =
                                beanInterfaceClass != null ? beanInterfaceClass.getSimpleName() : null;

                            final boolean interfaceWithSameName =
                                beanInterfaceName != null && beanInterfaceName.equals(serviceName);
                            boolean beanContainsInterfaceWithSameName = containsInterfaceWithSameName(serviceName, beanClass);

                            // interface based
                            if ((beanInterfaceName != null && interfaceWithSameName)) {
                                try {
                                    final Object matched =
                                        InitialContext.doLookup("java:app/" + next.getName() + "/" + obj.getName());
                                    if (matched != null) {
                                        exportedService =
                                            new BasicExportedService(beanObjName, beanInterface, beanClass, matched);
                                        break;
                                    }
                                } catch (Exception e) {
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.warning("AbstractEjbDelegate.DEBUG.001", e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            } else
                            // bean name based
                            if (beanInterfaceName == null && beanContainsInterfaceWithSameName) {
                                try {
                                    final Object matched =
                                        InitialContext.doLookup("java:app/" + next.getName() + "/" + obj.getName());
                                    if (matched != null) {
                                        exportedServiceCandidate = new BasicExportedService(beanObjName,
                                            getInterfaceWithSameName(serviceName, beanClass).getCanonicalName(), beanClass,
                                            matched);
                                    }
                                } catch (Exception e) {
                                    if (LOGGER.isDebugEnabled()) {
                                        LOGGER.warning("AbstractEjbDelegate.DEBUG.002", e.getMessage());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        if (LOGGER.isDebugEnabled()) {
                            String message = e.getMessage();
                            boolean printStack = true;
                            if (e instanceof NotContextException) {
                                message = e.getLocalizedMessage();
                                printStack = false;
                            }
                            LOGGER.warning("AbstractEjbDelegate.DEBUG.003", message);
                            if (printStack) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warning("AbstractEjbDelegate.DEBUG.004", e.getMessage());
                e.printStackTrace();
            }
        }

        result = exportedService;
        if (result == null) {
            result = exportedServiceCandidate;
        }
        return result;
    }

    protected static Class getInterfaceWithSameName(String serviceName, Class<?> aClass) {
        Class result = null;
        if (aClass.getSimpleName().equals(serviceName)) {
            result = aClass;
        } else if (aClass != null && (aClass.getInterfaces().length > 0 || aClass.getSuperclass() != null)) {
            if (aClass.getInterfaces().length > 0) {
                for (Class i : aClass.getInterfaces()) {
                    if (i.getSimpleName().equals(serviceName)) {
                        result = i;
                        break;
                    }
                }
                if (result == null) {
                    for (Class i : aClass.getInterfaces()) {
                        if (i.getSuperclass() != null) {
                            result = getInterfaceWithSameName(serviceName, i);
                            if (result != null) {
                                break;
                            }
                        }
                    }
                }
            }
            if (result == null) {
                if (aClass.getSuperclass() != null) {
                    result = getInterfaceWithSameName(serviceName, aClass.getSuperclass());
                }
            }
        }

        return result;
    }

    protected static boolean containsInterfaceWithSameName(String serviceName, Class<?> aClass) {
        boolean result = false;
        if (aClass.getSimpleName().equals(serviceName)) {
            result = true;
        } else if (aClass != null && (aClass.getInterfaces().length > 0 || aClass.getSuperclass() != null)) {
            if (aClass.getInterfaces().length > 0) {
                for (Class i : aClass.getInterfaces()) {
                    if (i.getSimpleName().equals(serviceName)) {
                        result = true;
                        break;
                    }
                }
                if (!result) {
                    for (Class i : aClass.getInterfaces()) {
                        if (i.getSuperclass() != null) {
                            result = containsInterfaceWithSameName(serviceName, i);
                            if (result) {
                                break;
                            }
                        }
                    }
                }
            }
            if (!result) {
                if (aClass.getSuperclass() != null) {
                    result = containsInterfaceWithSameName(serviceName, aClass.getSuperclass());
                }
            }
        }

        return result;
    }
}