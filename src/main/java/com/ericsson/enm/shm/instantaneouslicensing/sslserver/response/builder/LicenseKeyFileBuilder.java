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

import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.KeyPairGeneratorUtils.getLkfCapacityKeys;
import static com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.builder.KeyPairGeneratorUtils.getLkfFeatureKeys;

import java.security.Key;
import java.security.KeyPair;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.NodeTypeUtil;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.TargetNodeType;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.Body;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.Certificatechain;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.FeatureKey;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.Fingerprint;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.GeneralInfo;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.LicFile;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.ObjectFactory;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.PKIsignature;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.SWLT;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.NodeCapacity;

public class LicenseKeyFileBuilder {

    private static final Logger logger = LoggerFactory.getLogger(LicenseKeyFileBuilder.class);

    private LicenseKeyFileBuilder() {
    }

    public static LicFile buildObject(final Node node, final int sequenceNumber, final String customerId) {
        logger.debug("Building the licenseKey file for the node : {} started", node.getFingerprint());
        List<Object> allFeaturesAndCapacities = new ArrayList<>();
        allFeaturesAndCapacities.addAll(getLkfFeatureKeys(node));
        allFeaturesAndCapacities.addAll(getLkfCapacityKeys(node));
        final Fingerprint fingerprint = Fingerprint.fingerprintBuilder()
                .withPrint(node.getFingerprint())
                .withMethod("11")
                .withCapacityKeyOrFeatureKeyOrEmergencyResetKey(allFeaturesAndCapacities)
                .build();
        XMLGregorianCalendar generatedDate = null;
        try {
            generatedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            generatedDate.setDay(LocalDate.now().getDayOfMonth());
            generatedDate.setMonth(LocalDate.now().getMonthValue());
            generatedDate.setYear(LocalDate.now().getYear());
            generatedDate.setHour(LocalTime.now().getHour());
            generatedDate.setMinute(LocalTime.now().getHour());
            generatedDate.setSecond(LocalTime.now().getSecond());
        } catch (DatatypeConfigurationException e) {
            logger.error("Unable to create XMLGregorianCalendar instance", e);
        }
        final GeneralInfo generalInfo = GeneralInfo.generalInfoBuilder().withGenerated(generatedDate).withIssuer("Ericsson AB")
                .build();
        final SWLT swlt = SWLT.SWLTBuilder()
                .withFingerprint(fingerprint)
                .withCustomerId(customerId)
                .withProductType(node.getNodeType().contains("MINI-LINK")?"MINI-LINK":NodeTypeUtil.getTargetNodeType(node).toString())
                .withSwltId(node.getSwltId())
                .withGeneralInfo(generalInfo)
                .build();
        final Body body = Body.bodyBuilder().withSequenceNumber(sequenceNumber).withSwlt(swlt).withFormatVersion("2.0").withSignatureType("5")
                .build();
        final List<FeatureKey> featureKeyList = new ArrayList<>();
        final Collection<NodeCapacity> nodeCapacities = node.getCapacities();
        for (final NodeCapacity nodeCapacity : nodeCapacities) {
            final FeatureKey featureKey = FeatureKey.featureKeyBuilder()
                    .withId(nodeCapacity.getKeyId())
                    .withCapacity(Integer.parseInt(nodeCapacity.getRequiredLevel()))
                    .build();
            featureKeyList.add(featureKey);
        }
        final KeyPair pair = KeyPairGeneratorUtils.getInstance();
        final Key pvt = pair.getPrivate();
        final Key pub = pair.getPublic();
        final Base64.Encoder encoder = Base64.getEncoder();

        final String privateKey = String.format("-----BEGIN RSA PRIVATE KEY-----%n%s%n-----END RSA PRIVATE KEY-----%n",
                encoder.encodeToString(pvt.getEncoded()));
        final String publicKey = encoder.encodeToString(pub.getEncoded());
        final PKIsignature pkiSignature = node.getNodeType().contains("MINI-LINK")?PKIsignature.PKIsignatureBuilder()
                .withValue(publicKey)
                .withIssuer("Ericsson AB")
                .build() : PKIsignature.PKIsignatureBuilder()
                .withValue(publicKey)
                .build();
        final Certificatechain certificateChain = Certificatechain.certificatechainBuilder()
                .withProdcert(privateKey)
                .addCacert(privateKey)
                .build();

        final LicFile licFile = LicFile.licFileBuilder()
                .withCertificatechain(certificateChain)
                .withPkIsignature(pkiSignature)
                .withBody(body)
                .build();
        logger.debug("Building the licenseKey file for the node : {} completed ", node.getFingerprint());
        return licFile;
    }

    public static JAXBElement<LicFile> build(final Node node, final int sequenceNumber, final String customerId) {
        final ObjectFactory objectFactory = new ObjectFactory();
        return objectFactory.createLicFile(buildObject(node, sequenceNumber, customerId));
    }

}