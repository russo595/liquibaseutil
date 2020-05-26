package org.hamster.service;


import org.hamster.dto.MetaData;

import javax.validation.constraints.NotNull;

public interface NexusArtifactService {

    MetaData getVersionsArtifact(String artifactName);

    byte[] downloadArtifact(@NotNull String artifactId, @NotNull String version);
}
