package org.hamster.controller

import org.hamster.config.ApplicationsNameConfig
import org.hamster.dto.MetaData
import org.hamster.factory.DiffFactory
import org.hamster.service.NexusArtifactService
import org.hamster.service.ZipFileService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@RestController
class TestController {
    @Autowired
    private lateinit var nexusArtifactService: NexusArtifactService

    @Autowired
    private lateinit var applicationsNameConfig: ApplicationsNameConfig

    @Autowired
    private lateinit var zipFileService: ZipFileService

    @Autowired
    private lateinit var diffFactory: DiffFactory
    private val mv = ModelAndView("diff")

    @GetMapping("/test/{artifactId}")
    @ResponseBody
    fun getDictionaryByName(@PathVariable("artifactId") artifactId: String): MetaData {
        return nexusArtifactService.getVersionsArtifact(artifactId)
    }

    /**
     * @param extension Формат файлов в архиве, xml или txt
     */
    @GetMapping(value = ["/download/{extension}"])
    @Throws(IOException::class)
    fun file(@PathVariable extension: String?, response: HttpServletResponse) {
        val l1 = System.nanoTime()
        response.contentType = MediaType.APPLICATION_OCTET_STREAM.type
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=diff.zip")
        response.status = HttpStatus.OK.value()
        when (extension) {
            "xml" -> zipFileService.zip(diffFactory.changeLogDiff(), response.outputStream)
            "txt" -> zipFileService.zip(diffFactory.reportDiff(), response.outputStream)
            else -> {}
        }
        val l2 = System.nanoTime()
        log.debug("Result time: {}", (l2 - l1) / 1000000)
    }

    @GetMapping(value = ["diffPage"])
    fun selectArtifact(): ModelAndView {
        mv.addObject("apps", applicationsNameConfig.services)
        mv.addObject("metaData", MetaData())
        log.debug(mv.toString())
        return mv
    }

    @PostMapping(value = ["diffPage"])
    fun diffPage(data: MetaData): ModelAndView {
        val metaData = nexusArtifactService.getVersionsArtifact(data.artifactId!!)
        val versionList: List<String> = metaData.versioning?.versions?.version ?: listOf()
        mv.addObject("versionList", versionList)
        mv.addObject("metaData", data)
        log.debug(mv.toString())
        return mv
    }

    companion object {
        private val log = LoggerFactory.getLogger(TestController::class.java)
    }
}