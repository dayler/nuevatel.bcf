
package com.nuevatel.bcf.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "unlock", namespace = "http://bcf.nuevatel.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unlock", namespace = "http://bcf.nuevatel.com/", propOrder = {
    "name",
    "regexId"
})
public class Unlock {

    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "regexId", namespace = "")
    private String regexId;

    /**
     * 
     * @return
     *     returns String
     */
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param name
     *     the value for the name property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getRegexId() {
        return this.regexId;
    }

    /**
     * 
     * @param regexId
     *     the value for the regexId property
     */
    public void setRegexId(String regexId) {
        this.regexId = regexId;
    }

}
