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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder;

import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.KeyPairGeneratorUtils.getLdfLicenseKeys;

import java.time.LocalDateTime;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.ConstantValue;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.DateFormatUtil;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.Body;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.LicenseDataFile;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.NodeFingerprint;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.ObjectFactory;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.TargetNodeType;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;

public class LicenseDataFileBuilder {

  private static final Logger logger = LoggerFactory.getLogger(LicenseDataFileBuilder.class);

  private LicenseDataFileBuilder() {
  }

  public static JAXBElement<LicenseDataFile> build(final Node node, final int sequenceNumber,
      final String fileGenerationTimeStamp) throws DatatypeConfigurationException {
    logger.debug("Building the license data file for the node : {} started", node.getFingerprint());
    final NodeFingerprint nodeFingerprint = NodeFingerprint.nodeFingerprintBuilder().withPrint(node.getFingerprint())
        .build();
    final TargetNodeType targetNodeType;
    if (node.getNodeType().contains("MINI-LINK")) {
      targetNodeType = TargetNodeType.MINI_LINK;
    } else {
      targetNodeType = TargetNodeType.BASEBAND;
    }
    final Body body = Body.bodyBuilder().withSequenceNumber(sequenceNumber)
        .withLicenseKeyFileName(
            node.getFingerprint() + ConstantValue.UNDER_SCORE + fileGenerationTimeStamp + ConstantValue.XML_EXTENSION)
        .withGenerated(DatatypeFactory.newInstance().newXMLGregorianCalendar(LocalDateTime.now().toString()))
        .withTargetNodeType(targetNodeType).withLicenseKey(getLdfLicenseKeys(node)).withFingerprint(nodeFingerprint)
        .build();
    final ObjectFactory objectFactory = new ObjectFactory();
    final LicenseDataFile licenseDataFile = LicenseDataFile.licenseDataFileBuilder().withBody(body).build();
    logger.debug("Building the license data file for the node : {} completed ", node.getFingerprint());
    return objectFactory.createLicenseDataFile(licenseDataFile);
  }

  /**
   * return updated License Data File with Signature
   *
   * @param licenseDataFile
   * @return JAXBElement of type license data file
   */
  public static JAXBElement<LicenseDataFile> getLicenseDataElement(final LicenseDataFile licenseDataFile) {
    final ObjectFactory objectFactory = new ObjectFactory();
    final LicenseDataFile updatedLicenseDataFile = LicenseDataFile.licenseDataFileBuilder()
        .withBody(licenseDataFile.getBody()).withSignature(licenseDataFile.getSignature()).build();
    return objectFactory.createLicenseDataFile(updatedLicenseDataFile);
  }

}