package org.hamster.feign;

import org.hamster.config.FeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "nexusClient", url = "${NEXUS_MAVEN_CENTRAL_URL}", configuration = FeignClientConfiguration.class)
public interface NexusClient {

    @GetMapping(value = "/${nexusUrl}/{artifactId}/maven-metadata.xml")
    String getListVersionsArtifact(@PathVariable("artifactId") String artifactId);

    @GetMapping(value = "/${nexusUrl}/{artifactId}/{version}/{artifactId}-{version}.jar")
    byte[] downloadArtifact(@PathVariable("artifactId") String artifactId, @PathVariable("version") String version);
}