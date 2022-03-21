package org.hamster.service.impl

import org.hamster.dto.MetaData
import org.hamster.feign.NexusClient
import org.hamster.service.NexusArtifactService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.StringReader
import javax.xml.bind.JAXB

@Service
class NexusArtifactServiceImpl : NexusArtifactService {
    @Autowired
    private lateinit var nexusClient: NexusClient

    override fun getVersionsArtifact(artifactName: String): MetaData {
        val metaData = nexusClient.getListVersionsArtifact(artifactName)
        return JAXB.unmarshal(StringReader(metaData), MetaData::class.java)
    }

    override fun downloadArtifact(artifactId: String, version: String): ByteArray? {
        return nexusClient.downloadArtifact(artifactId, version)
    }
}