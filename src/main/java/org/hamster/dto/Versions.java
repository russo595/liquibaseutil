package org.hamster.dto;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Versions", propOrder = {

})
public class Versions {
    @XmlElement
    protected List<String> version;

    public List<String> getVersion() {
        if (version == null) {
            return new ArrayList<>();
        }
        return version;
    }

    public void setVersion(List<String> version) {
        this.version = version;
    }
}
