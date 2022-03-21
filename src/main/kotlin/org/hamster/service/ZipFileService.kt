package org.hamster.service

import liquibase.exception.LiquibaseException
import org.hamster.config.ApplicationsNameConfig
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.sql.SQLException
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.servlet.ServletOutputStream
import javax.xml.parsers.ParserConfigurationException

@Service
class ZipFileService {
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    @Autowired
    private lateinit var applicationsNameConfig: ApplicationsNameConfig

    fun zip(diff: AbstractDiff, outputStream: ServletOutputStream) {
        val futureList: MutableList<CompletableFuture<FileEntry>> = ArrayList<CompletableFuture<FileEntry>>()
        ZipOutputStream(outputStream).use { zipOut ->
            for ((key, value) in applicationsNameConfig.services.entries) {
                futureList.add(CompletableFuture.supplyAsync({
                    try {
                        val bytes = diff.generateDiff(key, value)
                        val zipEntry = ZipEntry(key + diff.extension)
                        FileEntry(bytes, zipEntry)
                    } catch (e: SQLException) {
                        log.error(e.message, e)
                    } catch (e: LiquibaseException) {
                        log.error(e.message, e)
                    } catch (e: IOException) {
                        log.error(e.message, e)
                    } catch (e: ParserConfigurationException) {
                        log.error(e.message, e)
                    }
                    null
                }, executorService))
            }
            futureList.asSequence()
                .map(CompletableFuture<FileEntry>::join)
                .filter(Objects::nonNull)
                .forEach { fileEntry: FileEntry ->
                    try {
                        zipOut.putNextEntry(fileEntry.zipEntry)
                        zipOut.write(fileEntry.bytes)
                    } catch (e: IOException) {
                        log.error(e.message, e)
                    }
                }
        }
    }

    private data class FileEntry(
        val bytes: ByteArray,
        val zipEntry: ZipEntry,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FileEntry

            if (!bytes.contentEquals(other.bytes)) return false
            if (zipEntry != other.zipEntry) return false

            return true
        }

        override fun hashCode(): Int {
            var result = bytes.contentHashCode()
            result = 31 * result + zipEntry.hashCode()
            return result
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ZipFileService::class.java)
    }
}