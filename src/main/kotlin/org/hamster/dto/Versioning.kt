package org.hamster.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Versioning", propOrder = [])
data class Versioning(
    @XmlElement(required = true)
    val latest: String? = null,

    @XmlElement(required = true)
    val release: String? = null,

    @XmlElement(required = true)
    val versions: Versions? = null,

    @XmlElement
    val lastUpdated: String? = null
)