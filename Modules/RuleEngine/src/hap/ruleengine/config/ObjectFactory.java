//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.07 at 09:28:04 PM CET 
//


package hap.ruleengine.config;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the hap.ruleengine.config package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: hap.ruleengine.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HAP }
     * 
     */
    public HAP createHAP() {
        return new HAP();
    }

    /**
     * Create an instance of {@link HAP.Composites }
     * 
     */
    public HAP.Composites createHAPComposites() {
        return new HAP.Composites();
    }

    /**
     * Create an instance of {@link ModuleConfig }
     * 
     */
    public ModuleConfig createModuleConfig() {
        return new ModuleConfig();
    }

    /**
     * Create an instance of {@link HAP.Composites.Composite }
     * 
     */
    public HAP.Composites.Composite createHAPCompositesComposite() {
        return new HAP.Composites.Composite();
    }

    /**
     * Create an instance of {@link ModuleConfig.Logging }
     * 
     */
    public ModuleConfig.Logging createModuleConfigLogging() {
        return new ModuleConfig.Logging();
    }

}
