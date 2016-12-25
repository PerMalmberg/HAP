//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.25 at 05:09:17 PM CET 
//


package hap.ruleengine.parts.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Components">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ComponentDef" type="{}ComponentDef" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Imports">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Import" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="src" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="instanceId" use="required" type="{}UUID" />
 *                           &lt;attribute name="name" type="{}ComponentName" default="" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Wires">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="WireDef" type="{}WireDef" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "components",
    "imports",
    "wires"
})
@XmlRootElement(name = "CompositeDef")
public class CompositeDef {

    @XmlElement(name = "Components", required = true)
    protected CompositeDef.Components components;
    @XmlElement(name = "Imports", required = true)
    protected CompositeDef.Imports imports;
    @XmlElement(name = "Wires", required = true)
    protected CompositeDef.Wires wires;

    /**
     * Gets the value of the components property.
     * 
     * @return
     *     possible object is
     *     {@link CompositeDef.Components }
     *     
     */
    public CompositeDef.Components getComponents() {
        return components;
    }

    /**
     * Sets the value of the components property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompositeDef.Components }
     *     
     */
    public void setComponents(CompositeDef.Components value) {
        this.components = value;
    }

    /**
     * Gets the value of the imports property.
     * 
     * @return
     *     possible object is
     *     {@link CompositeDef.Imports }
     *     
     */
    public CompositeDef.Imports getImports() {
        return imports;
    }

    /**
     * Sets the value of the imports property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompositeDef.Imports }
     *     
     */
    public void setImports(CompositeDef.Imports value) {
        this.imports = value;
    }

    /**
     * Gets the value of the wires property.
     * 
     * @return
     *     possible object is
     *     {@link CompositeDef.Wires }
     *     
     */
    public CompositeDef.Wires getWires() {
        return wires;
    }

    /**
     * Sets the value of the wires property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompositeDef.Wires }
     *     
     */
    public void setWires(CompositeDef.Wires value) {
        this.wires = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ComponentDef" type="{}ComponentDef" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "componentDef"
    })
    public static class Components {

        @XmlElement(name = "ComponentDef")
        protected List<ComponentDef> componentDef;

        /**
         * Gets the value of the componentDef property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the componentDef property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getComponentDef().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ComponentDef }
         * 
         * 
         */
        public List<ComponentDef> getComponentDef() {
            if (componentDef == null) {
                componentDef = new ArrayList<ComponentDef>();
            }
            return this.componentDef;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Import" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="src" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="instanceId" use="required" type="{}UUID" />
     *                 &lt;attribute name="name" type="{}ComponentName" default="" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "_import"
    })
    public static class Imports {

        @XmlElement(name = "Import")
        protected List<CompositeDef.Imports.Import> _import;

        /**
         * Gets the value of the import property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the import property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getImport().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CompositeDef.Imports.Import }
         * 
         * 
         */
        public List<CompositeDef.Imports.Import> getImport() {
            if (_import == null) {
                _import = new ArrayList<CompositeDef.Imports.Import>();
            }
            return this._import;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="src" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="instanceId" use="required" type="{}UUID" />
         *       &lt;attribute name="name" type="{}ComponentName" default="" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Import {

            @XmlAttribute(name = "src")
            protected String src;
            @XmlAttribute(name = "instanceId", required = true)
            protected String instanceId;
            @XmlAttribute(name = "name")
            protected String name;

            /**
             * Gets the value of the src property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSrc() {
                return src;
            }

            /**
             * Sets the value of the src property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSrc(String value) {
                this.src = value;
            }

            /**
             * Gets the value of the instanceId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInstanceId() {
                return instanceId;
            }

            /**
             * Sets the value of the instanceId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInstanceId(String value) {
                this.instanceId = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                if (name == null) {
                    return "";
                } else {
                    return name;
                }
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="WireDef" type="{}WireDef" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "wireDef"
    })
    public static class Wires {

        @XmlElement(name = "WireDef")
        protected List<WireDef> wireDef;

        /**
         * Gets the value of the wireDef property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the wireDef property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getWireDef().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WireDef }
         * 
         * 
         */
        public List<WireDef> getWireDef() {
            if (wireDef == null) {
                wireDef = new ArrayList<WireDef>();
            }
            return this.wireDef;
        }

    }

}
