package org.hamster.dto

import javax.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = ["artifactId", "groupId", "versioning"])
@XmlRootElement(name = "metadata")
data class MetaData(
    @XmlAttribute
    val modelVersion: String? = null,

    @XmlElement(required = true)
    val groupId: String? = null,

    @XmlElement(required = true)
    val artifactId: String? = null,

    @XmlElement(required = true)
    val versioning: Versioning? = null,
)