package org.hamster.service

import org.hamster.dto.MetaData

interface NexusArtifactService {
    fun getVersionsArtifact(artifactName: String): MetaData
    fun downloadArtifact(artifactId: String, version: String): ByteArray?
}