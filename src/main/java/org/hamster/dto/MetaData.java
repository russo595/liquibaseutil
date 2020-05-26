package org.hamster.dto;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "artifactId", "groupId", "versioning"
})
@XmlRootElement(name = "metadata")
public class MetaData {

    @XmlAttribute
    protected String modelVersion;
    @XmlElement(required = true)
    protected String groupId;
    @XmlElement(required = true)
    protected String artifactId;
    @XmlElement(required = true)
    protected Versioning versioning;

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Versioning getVersioning() {
        return versioning;
    }

    public void setVersioning(Versioning versioning) {
        this.versioning = versioning;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "modelVersion='" + modelVersion + '\'' +
                ", groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", versioning=" + versioning +
                '}';
    }
}
