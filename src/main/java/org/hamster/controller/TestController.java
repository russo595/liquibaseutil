package org.hamster.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.hamster.config.ApplicationsNameConfig;
import org.hamster.dto.MetaData;
import org.hamster.dto.Versioning;
import org.hamster.dto.Versions;
import org.hamster.factory.DiffFactory;
import org.hamster.service.NexusArtifactService;
import org.hamster.service.ZipFileService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
public class TestController {

    @Autowired
    private NexusArtifactService nexusArtifactService;
    @Autowired
    private ApplicationsNameConfig applicationsNameConfig;
    @Autowired
    private ZipFileService zipFileService;
    @Autowired
    private DiffFactory diffFactory;

    private ModelAndView mv;

    public TestController() {
        this.mv = new ModelAndView("diff");
    }

    @GetMapping("/test/{artifactId}")
    @ResponseBody
    public MetaData getDictionaryByName(@PathVariable("artifactId") String artifactId) {
        MetaData versionsArtifact = nexusArtifactService.getVersionsArtifact(artifactId);
        return versionsArtifact;
    }

    /**
     * @param extension Формат файлов в архиве, xml или txt
     */
    @GetMapping(value = "/download/{extension}")
    public void file(@PathVariable String extension, HttpServletResponse response) throws IOException {
        long l1 = System.nanoTime();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=diff.zip");
        response.setStatus(HttpStatus.OK.value());

        switch (extension) {
            case "xml":
                zipFileService.zip(diffFactory.changeLogDiff(), response.getOutputStream());
                break;
            case "txt":
                zipFileService.zip(diffFactory.reportDiff(), response.getOutputStream());
                break;
            default:
                break;
        }
        long l2 = System.nanoTime();
        log.debug("Result time: {}", (l2 - l1) / 1_000_000);
    }

    @GetMapping(value = "diffPage")
    public ModelAndView selectArtifact() {
        mv.addObject("apps", applicationsNameConfig.getServices());
        mv.addObject("metaData", new MetaData());
        log.debug(mv.toString());
        return mv;
    }

    @PostMapping(value = "diffPage")
    public ModelAndView diffPage(MetaData data) {

        MetaData metaData = nexusArtifactService.getVersionsArtifact(data.getArtifactId());
        List<String> versionList = Optional.ofNullable(metaData)
                .map(MetaData::getVersioning)
                .map(Versioning::getVersions)
                .map(Versions::getVersion)
                .orElse(Collections.emptyList());

        mv.addObject("versionList", versionList);
        mv.addObject("metaData", data);
        log.debug(mv.toString());
        return mv;
    }
}
