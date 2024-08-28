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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.response;

import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.FileUtil.generateLicenseSequence;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_21_INVALID_MD5;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_5_INVALID_ZIP;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_6_MISSING_MD5;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_7_MISSING_ZIP;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_9_PARTIAL_BATCH_FAIL;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_20_ELIS_ERROR_18;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.FP_FOR_PARTIAL_FAIL;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants.EUFT_13_BATCH_VERSION_V1;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.ConstantValue;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.DateFormatUtil;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.FileUtil;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.BatchStatusInformation;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v2.ErrorList;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.LicenseDataFile;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.TargetNodeType;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.LicFile;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.ElisRequest;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.BatchResultsBuilderV1;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.BatchResultsBuilderV2;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.LicenseDataFileBuilder;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.LicenseKeyFileBuilder;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.rest.controller.SecuredServerControllerDataConstants;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class LicenseKeyPackageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(LicenseKeyPackageBuilder.class);
    private final ElisRequest elisRequest;
    private final String requestId;
    private final Map<String, List<String>> licenseFiles = new HashMap<>();
    private String requestZipFileName;
    private final boolean requestWithErrorNodes;

    public LicenseKeyPackageBuilder(final ElisRequest elisRequest, final String requestId, final boolean isRequestWithErrorNodes) {
        this.elisRequest = elisRequest;
        this.requestId = requestId;
        requestWithErrorNodes = isRequestWithErrorNodes;
    }

    public void build() {
        logger.info("Building the License file for the request Id : {} started ", getRequestId());
        createRequestDirectory();
        boolean isPartialFail = false;
        if (!isRequestWithErrorNodes()) {
            for (final Node node : elisRequest.getNodes()) {
                if (!((elisRequest.getEuft().equals(EUFT_9_PARTIAL_BATCH_FAIL)
                        && node.getFingerprint().equals(FP_FOR_PARTIAL_FAIL))||elisRequest.getEuft().equals(EUFT_20_ELIS_ERROR_18))) {
                    final int sequenceNumber = generateLicenseSequence();
                    final String fileGenerationTimeStamp = DateFormatUtil.formatDate();
                    if(!node.getNodeType().contains("MINI-LINK")) {
                        buildMetaInfo(sequenceNumber, node, fileGenerationTimeStamp);
                    }
                    buildLkf(sequenceNumber, node, fileGenerationTimeStamp);
                } else {
                    isPartialFail = true;
                }
            }
        }
        String version = "2.0.0";
        if (ConstantValue.VERSION.equals(elisRequest.getBatchLkfRequestResultsVersion()) && !(elisRequest.getEuft().equals(EUFT_13_BATCH_VERSION_V1))) {
            buildBatchResultsV2(isPartialFail);
        } else {
            version = "1.0.0";
            buildBatchResultsV1();
        }
        zipLkf(version);
        generateMD5ForZipFile();
        logger.info("Building the License file for the request Id : {} completed successfully ", getRequestId());
    }

    private void buildLkf(final int sequenceNumber, final Node node, final String fileGenerationTimeStamp) {
        final String path =
                this.getRequestDirectory()
                + File.separator
                + node.getFingerprint()
                + File.separator
                + node.getFingerprint()
                + ConstantValue.UNDER_SCORE
                + fileGenerationTimeStamp
                + ConstantValue.XML_EXTENSION;
        final JAXBElement<LicFile> licFileElement = LicenseKeyFileBuilder.build(node, sequenceNumber, elisRequest.getGlobalCustomerId());
        createXmlFile(licFileElement, path);
        licenseFiles.computeIfAbsent(node.getFingerprint(), k -> new ArrayList<>()).add(path);
        logger.debug("Created Lkf file {} for the node {} successfully", path, node.getFingerprint());
        logger.info("Created Lkf file for all the nodes successfully");
    }

    private void buildBatchResultsV1() {
        final JAXBElement<com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.batchresult.v1.BatchStatusInformation> batchBuilder =
                BatchResultsBuilderV1.build(
                        String.valueOf(this.requestId), new ArrayList<>(elisRequest.getNodes()));
        final String path =
                this.getRequestDirectory()
                + File.separator
                + ConstantValue.BATCH_RESULTS_XML
                + this.requestId
                + ConstantValue.UNDER_SCORE
                + "1.0.0"
                + ConstantValue.XML_EXTENSION;
        createXmlFile(batchBuilder, path);
        logger.info("Created batch result file : {} successfully", path);
    }

    private void buildBatchResultsV2(final boolean isPartialFail) {
        JAXBElement<BatchStatusInformation> batchBuilder;
        List<ErrorList> errorLists = null;
        if (isRequestWithErrorNodes() || elisRequest.getEuft().equalsIgnoreCase(SecuredServerControllerDataConstants.EUFT_9_PARTIAL_BATCH_FAIL)) {
            errorLists = new ArrayList<>();
            final ErrorList errorList = ErrorList.errorListBuilder()
                    .withCode(ConstantValue.ELIS_NODE_FAILURE_ERROR_CODE)
                    .withCategory(ConstantValue.ELIS_NODE_FAILURE_ERROR_CATEGORY)
                    .withDescription(ConstantValue.ELIS_NODE_FAILURE_ERROR_MSG)
                    .withCxcList("CXC1").build();
            errorLists.add(errorList);
        }else if(elisRequest.getEuft().equalsIgnoreCase(SecuredServerControllerDataConstants.EUFT_20_ELIS_ERROR_18)){
            errorLists = new ArrayList<>();
            final ErrorList errorList = ErrorList.errorListBuilder()
                    .withCode(ConstantValue.ELIS_ERROR_CODE_18)
                    .withCategory(ConstantValue.ELIS_NODE_FAILURE_ERROR_CATEGORY)
                    .withDescription(ConstantValue.LKF_GENERATION_FAILED)
                    .withCxcList("CXC2").build();
            errorLists.add(errorList);
        }else if(elisRequest.getEuft().equalsIgnoreCase(SecuredServerControllerDataConstants.EUFT_22_ELIS_WARNING_1)){
            errorLists = new ArrayList<>();
            final ErrorList errorList = ErrorList.errorListBuilder()
                    .withCode(ConstantValue.ELIS_WARNING_1)
                    .withCategory(ConstantValue.WARNING)
                    .withDescription(ConstantValue.ELIS_WARNING_1_MSG)
                    .withCxcList("CXC4012324, CXC4012265").build();
            errorLists.add(errorList);
        }
        final List<Node> successList = new ArrayList<>(elisRequest.getNodes());
        final List<Node> errorList = new ArrayList<>();
        Node failNode = null;
        if (isPartialFail) {
            for (Node node : successList) {
                if (node.getFingerprint().equals(FP_FOR_PARTIAL_FAIL)) {
                    failNode = node;
                    errorList.add(failNode);
                }
            }
            successList.remove(failNode);
        }

        batchBuilder = BatchResultsBuilderV2.build(String.valueOf(this.requestId), successList, errorList, errorLists);
        final String path =
                this.getRequestDirectory()
                + File.separator
                + ConstantValue.BATCH_RESULTS_XML
                + this.requestId + "_"
                + ConstantValue.VERSION
                + ConstantValue.XML_EXTENSION;
        createXmlFile(batchBuilder, path);
        logger.info("Created batch result file : {} successfully", path);
    }

    private void buildMetaInfo(final int sequenceNumber, final Node node, final String fileGenerationTimeStamp) {
        try {
            final String path =
                    this.getRequestDirectory()
                    + File.separator
                    + node.getFingerprint()
                    + File.separator
                    + node.getFingerprint()
                    + ConstantValue.UNDER_SCORE
                    + fileGenerationTimeStamp
                    + ConstantValue.INFO
                    + ConstantValue.XML_EXTENSION;
            final JAXBElement<LicenseDataFile> licenseDataFileBuilderElement =
                    LicenseDataFileBuilder.build(node, sequenceNumber, fileGenerationTimeStamp);
            createXmlFile(licenseDataFileBuilderElement, path);
            final String signature = getSignature(path);
            logger.debug("Generated the Signature successfully for the node: {} ", node.getFingerprint());
            final LicenseDataFile licenseDataFile = readLdfFile(path, signature);
            final JAXBElement<LicenseDataFile> updatedlicenseDataFileBuilderElement = LicenseDataFileBuilder.getLicenseDataElement(
                    licenseDataFile);
            createXmlFile(updatedlicenseDataFileBuilderElement, path);
            licenseFiles.computeIfAbsent(node.getFingerprint(), k -> new ArrayList<>()).add(path);
            logger.debug("Created MetaInfo file {}  for the node {} successfully", path, node.getFingerprint());
        } catch (final Exception ex) {
            logger.error("Exception occured while building the MetaInfo file", ex);
        }
        logger.info("Created MetaInfo for all the nodes successfully");
    }

    private void createXmlFile(final JAXBElement jaxbElement, final String path) {
        try {
            final File file = new File(path);
            final JAXBContext jaxbContext = JAXBContext.newInstance(jaxbElement.getValue().getClass());
            final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final StringWriter tempXml = new StringWriter();
            jaxbMarshaller.marshal(jaxbElement, tempXml);
            String result = tempXml.toString();
            result = result
                    .replace("<noStop></noStop>", "<noStop/>")
                    .replace("<stop></stop>", "<stop/>")
                    .replace("<notContractuallyLimited></notContractuallyLimited>", "<notContractuallyLimited/>")
                    .replace("<noHardLimit></noHardLimit>", "<noHardLimit/>");
            Files.write(result.getBytes(), file);
            final String readFiles = Files.asCharSource(file, Charset.defaultCharset()).read();
            logger.info("LKF: Files read : {}", readFiles);
        } catch (final JAXBException | IOException e) {
            logger.error("jaxbMarshaller Cannot create xml: {}", path, e);
        }
    }

    private void createRequestDirectory() {
        for (final Node node : elisRequest.getNodes()) {
            final String fingerPrint = node.getFingerprint();
            new File(this.getRequestDirectory() + File.separator + fingerPrint).mkdirs();
        }
        logger.info("Created the request directory for all the nodes successfully");
    }

    private String getRequestDirectory() {
        return ConstantValue.LICENSE_FILE_ROOT_DIRECTORY + File.separator + this.requestId;
    }

    private void zipLkf(String version) {
        final List<String> zipFiles = zipLicenseFilesForEachNode();
        logger.info("Zip license files for all the node is completed: {}", zipFiles);
        zipFiles.add(
                this.getRequestDirectory()
                + File.separator
                + ConstantValue.BATCH_RESULTS_XML
                + this.requestId + "_"
                + version
                + ".xml");
        requestZipFileName =
                ConstantValue.LICENSE_FILE_ROOT_DIRECTORY
                + File.separator
                + this.requestId
                + ConstantValue.ZIP_EXTENSION;
        logger.info("partationedZipFiles: {}", zipFiles);
        if (!zipFiles.isEmpty()) {
            FileUtil.zip(requestZipFileName, zipFiles.stream().toArray(String[]::new));
        }
        FileUtil.deleteFileRecursively(
                ConstantValue.LICENSE_FILE_ROOT_DIRECTORY + File.separator + this.requestId);
    }

    private void generateMD5ForZipFile() {
        String checksum;
        final String mdfFileName = requestZipFileName + ConstantValue.MDF_EXTENSION;
        try {
            checksum = DigestUtils.md5DigestAsHex(new FileInputStream(requestZipFileName));
            if (EUFT_21_INVALID_MD5.equals(elisRequest.getEuft())) {
                //use case where md5 file has invalid checksum
                logger.warn("For {}, the generated MD5 checksum will be invalid", EUFT_21_INVALID_MD5);
                Files.write("c752d7e28bf93b0137782948733f5e6f".getBytes(), new File(mdfFileName));
            } else {
                Files.write(checksum.getBytes(), new File(mdfFileName));
            }
            if (EUFT_5_INVALID_ZIP.equals(elisRequest.getEuft())) {
                //use case where zip file has invalid content
                logger.warn("For {}, the generated ZIP file will be invalid", EUFT_5_INVALID_ZIP);
                java.nio.file.Files.delete(Paths.get(requestZipFileName));
                Files.write("INVALID_ZIP_FILE_CONTENT".getBytes(), new File(requestZipFileName));
                checksum = DigestUtils.md5DigestAsHex(new FileInputStream(requestZipFileName));
                java.nio.file.Files.delete(Paths.get(mdfFileName));
                Files.write(checksum.getBytes(), new File(mdfFileName));
            }
            logger.info("Md5 File created successfully");
            if (EUFT_6_MISSING_MD5.equals(elisRequest.getEuft())) {
                //use case where md5 file is missing
                logger.warn("For {}, there will be no MD5 file", EUFT_6_MISSING_MD5);
                java.nio.file.Files.delete(Paths.get(mdfFileName));
            }
            if (EUFT_7_MISSING_ZIP.equals(elisRequest.getEuft())) {
                //use case where zip file is missing
                logger.warn("For {}, there will be no ZIP file", EUFT_7_MISSING_ZIP);
                java.nio.file.Files.delete(Paths.get(requestZipFileName));
            }
        } catch (final IOException ex) {
            logger.error("Md5 File cannot created");
        }
    }

    private List<String> zipLicenseFilesForEachNode() {
        return this.licenseFiles.entrySet().stream()
                .map(e -> this.getRequestDirectory() + File.separator + e.getKey())
                .collect(Collectors.toList());
    }

    private String getSignature(final String path) throws NoSuchAlgorithmException, IOException {
        try (FileInputStream fis = new FileInputStream(path);
                final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            final byte[] data = new byte[1024];
            int noOfBytesRead;
            while ((noOfBytesRead = fis.read(data, 0, 1024)) != -1) {
                baos.write(data, 0, noOfBytesRead);
            }
            final String ldfBody = this.getLdfBody(baos.toString());
            return this.hashLdfBody(ldfBody);
        } catch (final FileNotFoundException e) {
            logger.error("File not found {}", path);
            throw new FileNotFoundException("File Not Found");
        }
    }

    private String getLdfBody(final String ldfContent) {
        String ldfBody = null;
        final int startBodyTag = ldfContent.indexOf("<Body>");
        final int endBodyTag = ldfContent.indexOf("</Body>");
        if (startBodyTag > -1 && endBodyTag > -1) {
            ldfBody = ldfContent.substring(startBodyTag, endBodyTag + 7);
            ldfBody = ldfBody.replaceAll("\\s", "");
        }

        return ldfBody;
    }

    private String hashLdfBody(String ldfBody) throws NoSuchAlgorithmException {
        final StringBuilder result;
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        ldfBody = ConstantValue.SIGNATURE_PREFIX + ldfBody;
        md5.update(ldfBody.getBytes());
        final BigInteger hash = new BigInteger(1, md5.digest());
        result = new StringBuilder(hash.toString(16));

        while (result.length() < 32) {
            result.insert(0, "0");
        }

        return result.toString();
    }

    private LicenseDataFile readLdfFile(final String path, final String signature) throws JAXBException, IOException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(LicenseDataFile.class);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final InputStream inputStream = new BufferedInputStream(new FileInputStream(path));
        final byte[] bytes = ByteStreams.toByteArray(inputStream);
        final ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
        final Source source = new StreamSource(byteInput);
        final JAXBElement<LicenseDataFile> licenseDataFile =
                unmarshaller.unmarshal(source, LicenseDataFile.class);
        inputStream.close();
        return LicenseDataFile.licenseDataFileBuilder()
                .withBody(licenseDataFile.getValue().getBody())
                .withSignature(signature)
                .build();

    }

    public String getRequestId() {
        return requestId;
    }

    private boolean isRequestWithErrorNodes() {
        return requestWithErrorNodes;
    }
}
