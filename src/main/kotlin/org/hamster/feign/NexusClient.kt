package org.hamster.feign

import org.hamster.config.FeignClientConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    value = "nexusClient",
    url = "\${NEXUS_MAVEN_CENTRAL_URL}",
    configuration = [FeignClientConfiguration::class]
)
interface NexusClient {
    @GetMapping(value = ["/\${nexusUrl}/{artifactId}/maven-metadata.xml"])
    fun getListVersionsArtifact(@PathVariable("artifactId") artifactId: String?): String

    @GetMapping(value = ["/\${nexusUrl}/{artifactId}/{version}/{artifactId}-{version}.jar"])
    fun downloadArtifact(
        @PathVariable("artifactId") artifactId: String,
        @PathVariable("version") version: String
    ): ByteArray?
}