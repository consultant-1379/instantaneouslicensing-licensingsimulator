/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.cleanup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.ConstantValue;

/**
 * Service class for cleaning up files not required
 * The duration of the clean up is configurable.
 * This will look at all the license files and clean up
 * the files which have been present more than the duration
 * threshold
 */
@Service
public class FileCleanUpImpl implements FileCleanUp {

    private static final Logger logger = LoggerFactory.getLogger(FileCleanUpImpl.class);

    @Value(("${fileduration}"))
    private Long duration;

    /**
     * List all eligible files to be deleted
     */
    @Override
    @Scheduled(fixedRateString = "${fileinterval}")
    public void removeFiles() throws IOException {
        final String path = ConstantValue.LICENSE_FILE_ROOT_DIRECTORY;
        try {
            final File files = new File(path);
            for (final File file : files.listFiles()) {
                final BasicFileAttributes attrbs = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class);
                final LocalDateTime fileCreated = LocalDateTime.ofInstant(attrbs.creationTime().toInstant(), ZoneId.systemDefault());
                if (Duration.between(fileCreated, LocalDateTime.now()).toMinutes() > duration) {
                    logger.debug("Deleting File : {}", file.getName());
                    Files.delete(Paths.get(file.toURI()));
                }
            }
        } catch (final Exception ex) {
            logger.error("FileCleanUpImpl is not able to clean the files in current iteration due to {} ", ex.getMessage());
        }

    }
}