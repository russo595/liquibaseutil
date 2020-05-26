package org.hamster.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hamster.dto.MetaData;
import org.hamster.feign.NexusClient;
import org.hamster.service.NexusArtifactService;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXB;
import java.io.StringReader;

@Slf4j
@Service
public class NexusArtifactServiceImpl implements NexusArtifactService {

    @Autowired
    private NexusClient nexusClient;

    @Override
    public MetaData getVersionsArtifact(String artifactName) {
        String metaData = nexusClient.getListVersionsArtifact(artifactName);
        return JAXB.unmarshal(new StringReader(metaData), MetaData.class);
    }

    @Override
    public byte[] downloadArtifact(@NotNull String artifactId, @NotNull String version) {
        return nexusClient.downloadArtifact(artifactId, version);
    }
}
