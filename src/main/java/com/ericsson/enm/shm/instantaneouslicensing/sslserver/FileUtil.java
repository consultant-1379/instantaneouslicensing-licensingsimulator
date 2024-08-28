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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static int sequenceCounter = 4000;

    private FileUtil() {
    }

    public static void deleteFileRecursively(final String path) {
        try {
            FileSystemUtils.deleteRecursively(Paths.get(path));
        } catch (final IOException e1) {
            logger.error("Cannot delete the file", e1);
        }
    }

    public static void zip(final String zipFileName, final String[] zipEntries) {
        try (final ZipOutputStream zos =
                new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)))) {
            zos.setLevel(Deflater.BEST_COMPRESSION);
            for (int i = 0; i < zipEntries.length; i++) {
                final File entryFile = new File(zipEntries[i]);
                if (!entryFile.exists()) {
                    logger.error("The entry file {} does not exist", entryFile.getAbsolutePath());
                    logger.error("Aborted processing.");
                    return;
                }
                if (entryFile.isDirectory()) {
                    for (File tmp : entryFile.listFiles()) {
                        zos.putNextEntry(new ZipEntry(entryFile.getName() + File.separator + tmp.getName()));
                        addEntryContent(zos, tmp.getAbsolutePath());
                    }

                } else {
                    zos.putNextEntry(new ZipEntry(entryFile.getName()));
                    addEntryContent(zos, entryFile.getAbsolutePath());
                }
            }
            logger.info(" Zip of all the files completed with zip file name :  {}", zipFileName);
        } catch (final IOException e) {
            logger.error("Cannot zip the entry", e);
        }
    }

    private static void addEntryContent(final ZipOutputStream zos, final String entryFileName)
            throws IOException {
        try (final BufferedInputStream bis =
                new BufferedInputStream(new FileInputStream(entryFileName))) {
            final byte[] buffer = new byte[1024];
            int count = -1;
            while ((count = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, count);
            }
        }
    }

    public static synchronized int generateLicenseSequence() {
        return sequenceCounter++;
    }
}