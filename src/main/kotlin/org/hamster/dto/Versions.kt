package org.hamster.dto

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlType

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Versions", propOrder = [])
data class Versions(
    @XmlElement
    val version: List<String>? = arrayListOf(),
)