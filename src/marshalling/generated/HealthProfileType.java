//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.7 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2015.10.17 alle 08:03:04 PM CEST 
//


package marshalling.generated;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per healthProfileType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="healthProfileType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lastupdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="weight" type="{}positiveDecimal"/>
 *         &lt;element name="height" type="{}positiveDecimal"/>
 *         &lt;element name="bmi" type="{}positiveDecimal"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "healthProfileType", propOrder = {
    "lastupdate",
    "weight",
    "height",
    "bmi"
})
public class HealthProfileType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastupdate;
    @XmlElement(required = true)
    protected BigDecimal weight;
    @XmlElement(required = true)
    protected BigDecimal height;
    @XmlElement(required = true)
    protected BigDecimal bmi;

    /**
     * Recupera il valore della proprietà lastupdate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastupdate() {
        return lastupdate;
    }

    /**
     * Imposta il valore della proprietà lastupdate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastupdate(XMLGregorianCalendar value) {
        this.lastupdate = value;
    }

    /**
     * Recupera il valore della proprietà weight.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     * Imposta il valore della proprietà weight.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeight(BigDecimal value) {
        this.weight = value;
    }

    /**
     * Recupera il valore della proprietà height.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHeight() {
        return height;
    }

    /**
     * Imposta il valore della proprietà height.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHeight(BigDecimal value) {
        this.height = value;
    }

    /**
     * Recupera il valore della proprietà bmi.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBmi() {
        return bmi;
    }

    /**
     * Imposta il valore della proprietà bmi.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBmi(BigDecimal value) {
        this.bmi = value;
    }

}
