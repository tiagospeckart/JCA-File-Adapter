# JCA-File-Adapter

As the PAI library is no longer available with JFOSS, and it had an interface for accessing the file system through a JCA component. This library was created in order to maintain the same functionality in our applications. The artifact generated in `./deployable-spi/jcafileadapter.rar` can be installed on Wildfly. Here as example a configuration of a JCA resource:

```xml
<subsystem xmlns="urn:jboss:domain:resource-adapters:7.1">
  <resource-adapters>
    <resource-adapter id="wspdoku_codeimport_jca">
      <archive>
        jcaFileAdapterExample.rar
      </archive>
      <transaction-support>NoTransaction</transaction-support>
      <connection-definitions>
        <connection-definition class-name="com.dcx.jfoss.fra.spi.outbound.JCAFileAdapterManagedConnectionFactory" jndi-name="java:/jca/daimler/tdoc/tdoc_jca" enabled="true" use-java-context="true" pool-name="wspdoku_codeimport_jca" use-ccm="true" sharable="true" enlistment="true">
          <config-property name="lockedFileAccess">false</config-property>
          <config-property name="rootPath">i:\example\</config-property>
          <pool>
            <min-pool-size>1</min-pool-size>
            <initial-pool-size>1</initial-pool-size>
            <max-pool-size>10</max-pool-size>
            <prefill>true</prefill>
            <flush-strategy>EntirePool</flush-strategy>
          </pool>
        </connection-definition>
      </connection-definitions>
    </resource-adapter>
  </resource-adapters>
</subsystem>
```
### Module

1. Take the `.jar` output of `jca-file-adapter-api`.
2. Rename it to `jca-file-adapter-common.jar`
3. Install it as a module in WildFly at `./modules`.

The module configuration file:

```xml
<?xml version='1.0' encoding='UTF-8'?>

<module xmlns="urn:jboss:module:1.1" name="jfoss-jca-file-adapter" slot="main">

	<properties>
        <property name="jboss.api" value="public"/>
    </properties>
	
    <resources>
        <resource-root path="jca-file-adapter-common.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="javaee.api"/>
        <module name="jakarta.enterprise.api"/>
        <module name="jakarta.json.api" optional="true"/>
        <module name="jakarta.persistence.api"/>
        <module name="jakarta.transaction.api"/>
        <module name="jakarta.xml.bind.api"/>
        <module name="jakarta.servlet.api"/>
    </dependencies>
</module>
```
### Build

````
mvn clean install -DskipTests=true
````

---
## Documentation

- [Jakarta Connectors 2.1 Specifications](https://jakarta.ee/specifications/connectors/2.1/)
- [Jakarta Connectors 2.1 - Documentation](https://jakarta.ee/specifications/connectors/2.1/jakarta-connectors-spec-2.1)
- [Wildfly 30 Resource Adapters configuration](https://docs.wildfly.org/30/wildscribe/subsystem/resource-adapters/resource-adapter/connection-definitions/index.html)

---

# Goals

## Resource Adapter

### Source Code

- [ ] Update the Source Code of `jca-file-adapter` from **Java EE** to **Jakarta EE**
  - [x] Change the namespace imports from `javax.` to `jakarta.`
  - [ ] Update dependencies in `pom.xml` files (in incremental fashion, so it doesn't break dependencies)
    - [ ] Update [parent pom.xml](./pom.xml)
    - [ ] Update [api pom.xml](./api/pom.xml)
    - [x] Update [deployable rar pom.xml](./deployable-rar/pom.xml)
    - [ ] Update [spi pom.xml](./spi/pom.xml)

### Installation

- [ ] Deploy `jca-file-adapter` as a resource adapter to `Wildfly`
  - [x] Build `jca-file-adapter-deployable-1.1.0.4-SNAPSHOT.rar`
  - [x] Build `jca-file-adapter-api-1.1.0.4-SNAPSHOT.jar`
  - [x] Deploy `jca-file-adapter-api-1.1.0.4-SNAPSHOT.jar` as a module with name `jca-file-adapter-common.jar` using the configuration from above
    - [x] Configure **Wildfly** `standalone.xml` _resource_adapter_ subsystem according to above configuration
      

## Web-app

- [ ] Create a simple **Web Application** to interact with the deployed resource adapter
  - [x] Use the [JCAFileAdapter](./war/src/main/java/com/dcx/jfoss/fra/war/JCAFileAdapter.java) class provided
  - [x] Create a [TestServlet](./war/src/main/java/com/dcx/jfoss/fra/war/TestServlet.java)
    - [ ] Create a [web.xml](./war/src/main/webapp/WEB-INF/web.xml) configuration file
      - Double check the following line: 
      ```xml
      <resource-ref>
          <description>JCA resource adapter</description>
          <res-ref-name>jca/daimler/tdoc/tdoc_jca</res-ref-name>
          <res-type>com.dcx.jfoss.fra.spi.outbound.JCAFileAdapterManagedConnectionFactory</res-type>
          <res-auth>Container</res-auth>
      </resource-ref>
      ```