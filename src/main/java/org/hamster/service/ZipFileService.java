package org.hamster.service;

import liquibase.exception.LiquibaseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hamster.config.ApplicationsNameConfig;

import javax.servlet.ServletOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class ZipFileService {

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private ApplicationsNameConfig applicationsNameConfig;

    public void zip(AbstractDiff diff, ServletOutputStream outputStream) throws IOException {
        List<CompletableFuture<FileEntry>> futureList = new ArrayList<>();
        try (ZipOutputStream zipOut = new ZipOutputStream(outputStream)) {
            for (Map.Entry<String, String> service : applicationsNameConfig.getServices().entrySet()) {
                futureList.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        byte[] bytes = diff.generateDiff(service.getKey(), service.getValue());
                        ZipEntry zipEntry = new ZipEntry(service.getKey() + diff.getExtension());
                        return new FileEntry(bytes, zipEntry);
                    } catch (SQLException | LiquibaseException | IOException | ParserConfigurationException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                }, executorService));
            }
            futureList.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .forEach(fileEntry -> {
                        try {
                            zipOut.putNextEntry(fileEntry.getZipEntry());
                            zipOut.write(fileEntry.getBytes());
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
        }
    }

    @Data
    @AllArgsConstructor
    private static class FileEntry {
        private byte[] bytes;
        private ZipEntry zipEntry;
    }
}
