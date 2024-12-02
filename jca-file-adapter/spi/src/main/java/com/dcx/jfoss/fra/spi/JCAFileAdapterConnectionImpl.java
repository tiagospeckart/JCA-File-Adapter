package com.dcx.jfoss.fra.spi;

import com.dcx.jfoss.fra.api.FileFilter;
import com.dcx.jfoss.fra.api.FileName;
import com.dcx.jfoss.fra.api.JCAFileAdapterConnection;
import com.dcx.jfoss.fra.spi.outbound.JCAFileAdapterParams;
import jakarta.resource.spi.ConfigProperty;

import java.io.*;
import java.util.logging.Logger;

public class JCAFileAdapterConnectionImpl implements JCAFileAdapterConnection {
    private static final long serialVersionUID = 7868658418351939365L;
    private static final Logger LOGGER = LoggingManager.getInstance().getLogger(JCAFileAdapterConnectionImpl.class);

    private JCAFileAdapterParams managedConnectionSettings;
    private boolean managed = true;
    private boolean closed = false;

    @ConfigProperty(defaultValue = "", supportsDynamicUpdates = true)
    private String rootPath = null;

    private boolean lockedFileAccess = false;

    public JCAFileAdapterConnectionImpl(JCAFileAdapterParams managedConn) {
        this.managedConnectionSettings = managedConn;
        this.rootPath = managedConn.getRootPath();
        this.lockedFileAccess = managedConn.isFileAccessLocked();
    }

    public boolean isFileAccessLocked() {
        return this.lockedFileAccess;
    }

    public String getPathSeparator() {
        return File.pathSeparator;
    }

    public String getSeparator() {
        return File.separator;
    }

    public boolean canRead(String relativePath) {
        LOGGER.fine("JCA_0010;called method canRead with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.canRead();
    }

    public boolean canWrite(String relativePath) {
        LOGGER.finest("JCA_0011;called method canWrite with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.canWrite();
    }

    public boolean createNewFile(String relativePath) throws IOException {
        LOGGER.finest("JCA_0012;called method createNewFile with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.createNewFile();
    }

    public boolean delete(String relativePath) {
        LOGGER.finest("JCA_0013;called method delete with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.delete();
    }

    public void deleteOnExit(String relativePath) {
        LOGGER.finest("JCA_0014;called method deleteOnExit with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        file.deleteOnExit();
    }

    public String getParent(String relativePath) {
        LOGGER.finest("JCA_0015;called method getParent with param: [" + relativePath + "]");
        String result = null;
        File file = createFileObject(relativePath);
        result = file.getParent();
        if (result != null &&
                result.startsWith(this.managedConnectionSettings.getRootPath())) {
            result = result.substring(this.managedConnectionSettings.getRootPath().length());
        }

        return result;
    }

    public boolean isDirectory(String relativePath) {
        LOGGER.finest("JCA_0016;called method isDirectory with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.isDirectory();
    }

    public boolean isFile(String relativePath) {
        LOGGER.finest("JCA_0017;called method isFile with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.isFile();
    }

    public boolean isHidden(String relativePath) {
        LOGGER.finest("JCA_0018;called method isHidden with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.isHidden();
    }

    public long lastModified(String relativePath) {
        LOGGER.finest("JCA_0019;called method lastModified with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.lastModified();
    }

    public long length(String relativePath) {
        LOGGER.finest("JCA_0020;called method length with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.length();
    }

    public String[] list(String relativePath, final FileFilter filter) {
        LOGGER.finest("JCA_0021;called method list with param: [" + relativePath + "] and Filter");
        File file = createFileObject(relativePath);
        FilenameFilter fnf = null;
        if (filter != null) {
            fnf = new FilenameFilter() {
                public boolean accept(File file, String dir) {
                    if (file != null && file.exists()) {
                        LOGGER.finest("JCA_0032;called filename filter: [" + file.getAbsolutePath() + "]");
                        final FileName paramFileName = new FileName(file.getAbsolutePath());
                        return filter.accept(paramFileName, dir);
                    } else {
                        return false;
                    }
                }
            };
        }

        return file.list(fnf);
    }

    public String[] list(String subpath) {
        LOGGER.finest("JCA_0022;called method list with param: [" + subpath + "]");
        String[] result = null;
        File dir = createFileObject(subpath);
        result = dir.list();
        return result;
    }

    public FileName[] listFiles(String relativePath, FileFilter filter) {
        LOGGER.finest("JCA_0023;called method listFiles with param: [" + relativePath + "] and filter");
        FileName[] result = null;
        String[] fileNameList = list(relativePath, filter);
        if (fileNameList != null && fileNameList.length > 0) {
            result = new FileName[fileNameList.length];
            for (int i = 0; i < fileNameList.length; i++) {
                result[i] = new FileName(fileNameList[i]);
            }
        }
        return result;
    }

    public FileName[] listFiles(String relativePath) {
        LOGGER.finest("JCA_0024;called method listFiles with param: [" + relativePath + "]");
        return listFiles(relativePath, null);
    }

    public boolean mkdir(String relativePath) {
        LOGGER.finest("JCA_0025;called method mkdir with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.mkdir();
    }

    public boolean mkdirs(String relativePath) {
        LOGGER.finest("JCA_0026;called method mkdirs with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.mkdirs();
    }

    public boolean rename(String from, String to) {
        LOGGER.finest("JCA_0027;called method rename with params: [" + from + "] and [" + to + "]");
        File src = createFileObject(from);
        File dest = createFileObject(to);
        return src.renameTo(dest);
    }

    public boolean setLastModified(String relativePath, long time) {
        if (time < 0L) {
            throw new IllegalArgumentException("Last Modified timestamp of file must not be negative");
        }
        LOGGER.finest("JCA_0028;called method setLastModified with params: [" + relativePath + "] and [" + time + "]");
        File file = createFileObject(relativePath);
        return file.setLastModified(time);
    }

    public boolean setReadOnly(String relativePath) {
        LOGGER.finest("JCA_0029;called method setReadOnly with param: [" + relativePath + "]");
        File file = createFileObject(relativePath);
        return file.setReadOnly();
    }

    public InputStream getInputStream(String relativePath) throws IOException {
        LOGGER.finest("JCA_0030;called method getInputStream with param: [" + relativePath + "]");
        return new FileInputStream(createFileObject(relativePath));
    }

    public OutputStream getOutputStream(String relativePath, boolean append) throws IOException {
        LOGGER.finest("JCA_0031;called method getOutputStream with param: [" + relativePath + "]. Access locked: [" + Boolean.valueOf(this.lockedFileAccess) + "]");
        FileOutputStream result = new FileOutputStream(createFileObject(relativePath), append);
        if (this.lockedFileAccess) {
            result.getChannel().lock();
        }
        return result;
    }

    private File createFileObject(String path) {
        if (path.startsWith("/") || path.indexOf(":") > -1 || path.indexOf("..") > -1) {
            LOGGER.warning("JCA_0003;Absolute path names and '..' are not allowed!");
            throw new IllegalArgumentException("Absolute path names and '..' are not allowed!");
        }
        return new File(this.rootPath + path);
    }

    public void closePhysical() {
        if (this.managed && !isClosed()) {
            this.managedConnectionSettings.close();
            closed = true;
            this.managed = false;
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws IOException {
        this.closePhysical();
    }
}


