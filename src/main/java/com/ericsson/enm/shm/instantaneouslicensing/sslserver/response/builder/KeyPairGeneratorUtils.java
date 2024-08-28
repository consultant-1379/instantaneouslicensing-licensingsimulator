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

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.NodeTypeUtil;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.*;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.CapacityKey;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.ComplementaryData;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.lkf.FeatureKey;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;

class KeyPairGeneratorUtils {
  private static final Logger logger = LoggerFactory.getLogger(KeyPairGeneratorUtils.class);
  private static KeyPair instance;

  /**
   * Don't let anyone else instantiate this class
   */
  private KeyPairGeneratorUtils() {
  }

  /**
   * Lazily create the instance when it is accessed for the first time
   * 
   * @param targetNodeType
   *          TODO
   */
  static synchronized KeyPair getInstance() {
    if (instance == null) {
      try {
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        instance = kpg.generateKeyPair();
      } catch (final NoSuchAlgorithmException e) {
        logger.error("Cannot generate KeyPair", e);
      }
    }
    return instance;
  }

  static List<FeatureKey> getLkfFeatureKeys(final Node node) {
    if (NodeTypeUtil.getTargetNodeType(node) == TargetNodeType.MINI_LINK) {
      return Arrays.asList();
    } else {
      final FeatureKey featureKey1 = FeatureKey.featureKeyBuilder().withId("CXC4040010")
          .withDescription("RealTimeSecurity").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey2 = FeatureKey.featureKeyBuilder().withId("CXC4040014")
          .withDescription("EthernetOamService").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey3 = FeatureKey.featureKeyBuilder().withId("CXC4011823")
          .withDescription("VirtualRouters").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey4 = FeatureKey.featureKeyBuilder().withId("CXC4012334")
          .withDescription("EmfPowerLockMidband").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey5 = FeatureKey.featureKeyBuilder().withId("CXC4040005")
          .withDescription("Egress IP Traffic Shaping").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey6 = FeatureKey.featureKeyBuilder().withId("CXC4012052")
          .withDescription("CpriCompressionWithBasebandR").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey7 = FeatureKey.featureKeyBuilder().withId("CXC4040013")
          .withDescription("Ip Flow Monitoring").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey8 = FeatureKey.featureKeyBuilder().withId("CXC4011809")
          .withDescription("MulticabinetControl").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey9 = FeatureKey.featureKeyBuilder().withId("CXC4040018")
          .withDescription("IEEE1588 Boundary Clock").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey10 = FeatureKey.featureKeyBuilder().withId("CXC4012051")
          .withDescription("CpriCompression").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey11 = FeatureKey.featureKeyBuilder().withId("CXC4012104")
          .withDescription("AssistedTimeHoldOver").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey12 = FeatureKey.featureKeyBuilder().withId("CXC4040006")
          .withDescription("IPv6 Backhaul Support").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey13 = FeatureKey.featureKeyBuilder().withId("CXC4011838")
          .withDescription("10GEPortCapability").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey14 = FeatureKey.featureKeyBuilder().withId("CXC4012022")
          .withDescription("TransportPathCharMonitoring").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey15 = FeatureKey.featureKeyBuilder().withId("CXC4040008")
          .withDescription("IEEE1588 Time and Phase Synchronization")
          .withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("").build();
      final FeatureKey featureKey16 = FeatureKey.featureKeyBuilder().withId("CXC4040019")
          .withDescription("MultipleGnssSupport").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey17 = FeatureKey.featureKeyBuilder().withId("CXC4040007")
          .withDescription("IEEE1588 Frequency Synchronization").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey18 = FeatureKey.featureKeyBuilder().withId("CXC4040016")
          .withDescription("TwampInitiator").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey19 = FeatureKey.featureKeyBuilder().withId("CXC4040004").withDescription("IPsec")
          .withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("").build();
      final FeatureKey featureKey20 = FeatureKey.featureKeyBuilder().withId("CXC4012327")
          .withDescription("ControlChannelBeamforming").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey21 = FeatureKey.featureKeyBuilder().withId("CXC4012325")
          .withDescription("StreamingOfPmEvents").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey22 = FeatureKey.featureKeyBuilder().withId("CXC4011710")
          .withDescription("RANGrandMaster").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey23 = FeatureKey.featureKeyBuilder().withId("CXC4011915")
          .withDescription("MultipleEthernetPorts").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey24 = FeatureKey.featureKeyBuilder().withId("CXC4011707")
          .withDescription("BidirectionalFWDetection").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey25 = FeatureKey.featureKeyBuilder().withId("CXC4011649").withDescription("OSPFv2")
          .withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("").build();
      final FeatureKey featureKey26 = FeatureKey.featureKeyBuilder().withId("CXC4040002")
          .withDescription("Ethernet Link Aggregation").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").build();
      final FeatureKey featureKey27 = FeatureKey.featureKeyBuilder().withId("CXC4040009")
          .withDescription("Two-Way Active Measurement Protocol Responder")
          .withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("").build();
      final FeatureKey featureKey28 = FeatureKey.featureKeyBuilder().withId("CXC4040011")
          .withDescription("Synchronous Ethernet").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey29 = FeatureKey.featureKeyBuilder().withId("CXC4012272")
          .withDescription("MassiveMIMOMidBand").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey30 = FeatureKey.featureKeyBuilder().withId("CXC4012273")
          .withDescription("LTE-NRDLAggregation").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .build();
      final FeatureKey featureKey31 = FeatureKey.featureKeyBuilder().withId("CXC4012131")
          .withDescription("PacketCapture").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("").build();
      return Arrays.asList(featureKey1, featureKey2, featureKey3, featureKey4, featureKey5, featureKey6, featureKey7,
          featureKey8, featureKey9, featureKey10, featureKey11, featureKey12, featureKey13, featureKey14, featureKey15,
          featureKey16, featureKey17, featureKey18, featureKey19, featureKey20, featureKey21, featureKey22,
          featureKey23, featureKey24, featureKey25, featureKey26, featureKey27, featureKey28, featureKey29,
          featureKey30, featureKey31);
    }
  }

  static List<CapacityKey> getLkfCapacityKeys(final Node node) {
    if (NodeTypeUtil.getTargetNodeType(node) == TargetNodeType.MINI_LINK) {
      final CapacityKey capacityKey1 = CapacityKey.capacityKeyBuilder().withId("FAL1242346")
          .withDescription("M-L 6352 RL Bonding").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey2 = CapacityKey.capacityKeyBuilder().withId("FAL1242361")
          .withDescription("M-L 6352 UG 3to3,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey3 = CapacityKey.capacityKeyBuilder().withId("FAL1242360")
          .withDescription("M-L 6352 UG 3to3,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey4 = CapacityKey.capacityKeyBuilder().withId("FAL1242347")
          .withDescription("M-L 6352 Node GUI").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey5 = CapacityKey.capacityKeyBuilder().withId("FAL1242345")
          .withDescription("M-L 6352 Header Compr").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey6 = CapacityKey.capacityKeyBuilder().withId("FAL1242355")
          .withDescription("M-L 6352 UG 1,25to1.5Gbps").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey7 = CapacityKey.capacityKeyBuilder().withId("FAL1242358")
          .withDescription("M-L 6352 UG 2to2,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(1).withNoHardLimit("").build();
      final CapacityKey capacityKey8 = CapacityKey.capacityKeyBuilder().withId("FAL1242357")
          .withDescription("M-L 6352 UG 1,75to2Gbps").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(1).withNoHardLimit("").build();
      return Arrays.asList(capacityKey1, capacityKey2, capacityKey3, capacityKey4, capacityKey5, capacityKey6,
          capacityKey7, capacityKey8);
    } else {
      final CapacityKey capacityKey1 = CapacityKey.capacityKeyBuilder().withId("CXC4012290")
          .withDescription("NR5+5MHzSectorCarrier").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withNoHardLimit("").withNotContractuallyLimited("").build();
      final CapacityKey capacityKey2 = CapacityKey.capacityKeyBuilder().withId("CXC4012084")
          .withDescription("InitialHwacBb6630UtilityModule").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(1).withHardLimit("1")
          .withComplementaryData(ComplementaryData.complementaryDataBuilder().withHwac("L:;W:;G:;N:").build()).build();
      final CapacityKey capacityKey3 = CapacityKey.capacityKeyBuilder().withId("CXC4010625")
          .withDescription("HWAC for Output power 20W to 40W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey4 = CapacityKey.capacityKeyBuilder().withId("CXC4010626")
          .withDescription("HWAC for Output power 40W to 60W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey5 = CapacityKey.capacityKeyBuilder().withId("CXC4011161")
          .withDescription("HWAC for Output Power 60W to 80W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey6 = CapacityKey.capacityKeyBuilder().withId("CXC4011162")
          .withDescription("HWAC for Output Power 80W to 100W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey7 = CapacityKey.capacityKeyBuilder().withId("CXC4011182")
          .withDescription("HWAC for Output Power 100W to 120W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey8 = CapacityKey.capacityKeyBuilder().withId("CXC4011620")
          .withDescription("HWAC for Output Power 120W to 140W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey9 = CapacityKey.capacityKeyBuilder().withId("CXC4011621")
          .withDescription("HWAC for Output Power 140W to 160W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey10 = CapacityKey.capacityKeyBuilder().withId("CXC4012085")
          .withDescription("ExpansionHwacBb6630UtilityMod").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey11 = CapacityKey.capacityKeyBuilder().withId("CXC4012134")
          .withDescription("HWACOutputPower160WTo180W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey12 = CapacityKey.capacityKeyBuilder().withId("CXC4012135")
          .withDescription("HWACOutputPower180WTo200W").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey13 = CapacityKey.capacityKeyBuilder().withId("CXC4012204")
          .withDescription("BeamformingFor64TAdvancedAir").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey14 = CapacityKey.capacityKeyBuilder().withId("CXC4012283")
          .withDescription("NRAASTDDChannelBandwidth10MHz").withStart(LocalDate.now().minusDays(20L).toString())
          .withNoStop("").withCapacity(0).withHardLimit("0").build();
      final CapacityKey capacityKey15 = CapacityKey.capacityKeyBuilder().withId("CXC4012338")
          .withDescription("OutputPower20WStep").withStart(LocalDate.now().minusDays(20L).toString()).withNoStop("")
          .withCapacity(0).withHardLimit("0").build();

      return Arrays.asList(capacityKey1, capacityKey2, capacityKey3, capacityKey4, capacityKey5, capacityKey6,
          capacityKey7, capacityKey8, capacityKey9, capacityKey10, capacityKey11, capacityKey12, capacityKey13,
          capacityKey14, capacityKey15);
    }
  }

  static List<LicenseKey> getLdfLicenseKeys(final Node node) {
    if (NodeTypeUtil.getTargetNodeType(node) == TargetNodeType.MINI_LINK) {
      final LicenseKey licenseKey1 = LicenseKey.licenseKeyBuilder().withId("FAL1242346")
          .withDescription("M-L 6352 RL Bonding").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey2 = LicenseKey.licenseKeyBuilder().withId("FAL1242361")
          .withDescription("M-L 6352 UG 3to3,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey3 = LicenseKey.licenseKeyBuilder().withId("FAL1242360")
          .withDescription("M-L 6352 UG 3to3,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey4 = LicenseKey.licenseKeyBuilder().withId("FAL1242347")
          .withDescription("M-L 6352 Node GUI").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey5 = LicenseKey.licenseKeyBuilder().withId("FAL1242345")
          .withDescription("M-L 6352 Header Compr").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey6 = LicenseKey.licenseKeyBuilder().withId("FAL1242355")
          .withDescription("M-L 6352 UG 1,25to1.5Gbps").withStart(LocalDate.now().minusDays(20L).toString())
          .withStop("").build();
      final LicenseKey licenseKey7 = LicenseKey.licenseKeyBuilder().withId("FAL1242358")
          .withDescription("M-L 6352 UG 2to2,5Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      final LicenseKey licenseKey8 = LicenseKey.licenseKeyBuilder().withId("FAL1242357")
          .withDescription("M-L 6352 UG 1,75to2Gbps").withStart(LocalDate.now().minusDays(20L).toString()).withStop("")
          .build();
      return Arrays.asList(licenseKey1, licenseKey2, licenseKey3, licenseKey4, licenseKey5, licenseKey6, licenseKey7,
          licenseKey8);
    } else {

      final LicenseKey licenseKey1 = LicenseKey.licenseKeyBuilder().withId("CXC4040014")
          .withType(LicenseKeyType.FEATURE).withDescription("EthernetOamService").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey2 = LicenseKey.licenseKeyBuilder().withId("CXC4011823")
          .withType(LicenseKeyType.FEATURE).withDescription("VirtualRouters").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey3 = LicenseKey.licenseKeyBuilder().withId("CXC4012334")
          .withType(LicenseKeyType.FEATURE).withDescription("EmfPowerLockMidband").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey4 = LicenseKey.licenseKeyBuilder().withId("CXC4040005")
          .withType(LicenseKeyType.FEATURE).withDescription("Egress IP Traffic Shaping").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey5 = LicenseKey.licenseKeyBuilder().withId("CXC4012052")
          .withType(LicenseKeyType.FEATURE).withDescription("CpriCompressionWithBasebandR ").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey6 = LicenseKey.licenseKeyBuilder().withId("CXC4040013")
          .withType(LicenseKeyType.FEATURE).withDescription("Ip Flow Monitoring").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey7 = LicenseKey.licenseKeyBuilder().withId("CXC4011809")
          .withType(LicenseKeyType.FEATURE).withDescription("MulticabinetControl").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey8 = LicenseKey.licenseKeyBuilder().withId("CXC4040018")
          .withType(LicenseKeyType.FEATURE).withDescription("IEEE1588 Boundary Clock").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey9 = LicenseKey.licenseKeyBuilder().withId("CXC4012051")
          .withType(LicenseKeyType.FEATURE).withDescription("CpriCompression").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey10 = LicenseKey.licenseKeyBuilder().withId("CXC4012104")
          .withType(LicenseKeyType.FEATURE).withDescription("AssistedTimeHoldOver").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey11 = LicenseKey.licenseKeyBuilder().withId("CXC4040006")
          .withType(LicenseKeyType.FEATURE).withDescription("IPv6 Backhaul Support").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey12 = LicenseKey.licenseKeyBuilder().withId("CXC4011838")
          .withType(LicenseKeyType.FEATURE).withDescription("10GEPortCapability").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey13 = LicenseKey.licenseKeyBuilder().withId("CXC4012022")
          .withType(LicenseKeyType.FEATURE).withDescription("TransportPathCharMonitoring").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey14 = LicenseKey.licenseKeyBuilder().withId("CXC4040008")
          .withType(LicenseKeyType.FEATURE).withDescription("IEEE1588 Time and Phase Synchronization").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey15 = LicenseKey.licenseKeyBuilder().withId("CXC4040019")
          .withType(LicenseKeyType.FEATURE).withDescription("MultipleGnssSupport").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey16 = LicenseKey.licenseKeyBuilder().withId("CXC4040007")
          .withType(LicenseKeyType.FEATURE).withDescription("IEEE1588 Frequency Synchronization").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey17 = LicenseKey.licenseKeyBuilder().withId("CXC4040016")
          .withType(LicenseKeyType.FEATURE).withDescription("TwampInitiator").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey18 = LicenseKey.licenseKeyBuilder().withId("CXC4040004")
          .withType(LicenseKeyType.FEATURE).withDescription("IPsec").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey19 = LicenseKey.licenseKeyBuilder().withId("CXC4012327")
          .withType(LicenseKeyType.FEATURE).withDescription("ControlChannelBeamforming").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey20 = LicenseKey.licenseKeyBuilder().withId("CXC4012325")
          .withType(LicenseKeyType.FEATURE).withDescription("StreamingOfPmEvents").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey21 = LicenseKey.licenseKeyBuilder().withId("CXC4011710")
          .withType(LicenseKeyType.FEATURE).withDescription("RANGrandMaster").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey22 = LicenseKey.licenseKeyBuilder().withId("CXC4011915")
          .withType(LicenseKeyType.FEATURE).withDescription("MultipleEthernetPorts").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey23 = LicenseKey.licenseKeyBuilder().withId("CXC4011707")
          .withType(LicenseKeyType.FEATURE).withDescription("BidirectionalFWDetection").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey24 = LicenseKey.licenseKeyBuilder().withId("CXC4011649")
          .withType(LicenseKeyType.FEATURE).withDescription("OSPFv2").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey25 = LicenseKey.licenseKeyBuilder().withId("CXC4040002")
          .withType(LicenseKeyType.FEATURE).withDescription("Ethernet Link Aggregation").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey26 = LicenseKey.licenseKeyBuilder().withId("CXC4040009")
          .withType(LicenseKeyType.FEATURE).withDescription("Two-Way Active Measurement Protocol Responder")
          .withValue(1).withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey27 = LicenseKey.licenseKeyBuilder().withId("CXC4040011")
          .withType(LicenseKeyType.FEATURE).withDescription("Synchronous Ethernet").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey28 = LicenseKey.licenseKeyBuilder().withId("CXC4012272")
          .withType(LicenseKeyType.FEATURE).withDescription("MassiveMIMOMidBand").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey29 = LicenseKey.licenseKeyBuilder().withId("CXC4012273")
          .withType(LicenseKeyType.FEATURE).withDescription("LTE-NRDLAggregation").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey30 = LicenseKey.licenseKeyBuilder().withId("CXC4040010")
          .withType(LicenseKeyType.FEATURE).withDescription("RealTimeSecurity").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey31 = LicenseKey.licenseKeyBuilder().withId("CXC4012131")
          .withType(LicenseKeyType.FEATURE).withDescription("PacketCapture").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey32 = LicenseKey.licenseKeyBuilder().withId("CXC4012290")
          .withType(LicenseKeyType.CAPACITY).withDescription("NR5+5MHzSectorCarrier").withValue(-1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey33 = LicenseKey.licenseKeyBuilder().withId("CXC4012084")
          .withType(LicenseKeyType.CAPACITY).withDescription("InitialHwacBb6630UtilityModule").withValue(1)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey34 = LicenseKey.licenseKeyBuilder().withId("CXC4010625")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output power 20W to 40W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey35 = LicenseKey.licenseKeyBuilder().withId("CXC4010626")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output power 40W to 60W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey36 = LicenseKey.licenseKeyBuilder().withId("CXC4011161")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output Power 60W to 80W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey37 = LicenseKey.licenseKeyBuilder().withId("CXC4011162")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output Power 80W to 100W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey38 = LicenseKey.licenseKeyBuilder().withId("CXC4011182")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output Power 100W to 120W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey39 = LicenseKey.licenseKeyBuilder().withId("CXC4011620")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC for Output Power 120W to 140W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey40 = LicenseKey.licenseKeyBuilder().withId("CXC4011621")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWAC  for Output Power 140W to 160W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey41 = LicenseKey.licenseKeyBuilder().withId("CXC4012085")
          .withType(LicenseKeyType.CAPACITY).withDescription("ExpansionHwacBb6630UtilityMod").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey42 = LicenseKey.licenseKeyBuilder().withId("CXC4012134")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWACOutputPower160WTo180W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey43 = LicenseKey.licenseKeyBuilder().withId("CXC4012135")
          .withType(LicenseKeyType.CAPACITY).withDescription("HWACOutputPower180WTo200W").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey44 = LicenseKey.licenseKeyBuilder().withId("CXC4012204")
          .withType(LicenseKeyType.CAPACITY).withDescription("BeamformingFor64TAdvancedAir").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey45 = LicenseKey.licenseKeyBuilder().withId("CXC4012283")
          .withType(LicenseKeyType.CAPACITY).withDescription("NRAASTDDChannelBandwidth10MHz").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      final LicenseKey licenseKey46 = LicenseKey.licenseKeyBuilder().withId("CXC4012338")
          .withType(LicenseKeyType.CAPACITY).withDescription("OutputPower20WStep").withValue(0)
          .withStart(LocalDate.now().minusDays(20L).toString()).withStop("").build();
      return Arrays.asList(licenseKey1, licenseKey2, licenseKey3, licenseKey4, licenseKey5, licenseKey6, licenseKey7,
          licenseKey8, licenseKey9, licenseKey10, licenseKey11, licenseKey12, licenseKey13, licenseKey14, licenseKey15,
          licenseKey16, licenseKey17, licenseKey18, licenseKey19, licenseKey20, licenseKey21, licenseKey22,
          licenseKey23, licenseKey24, licenseKey25, licenseKey26, licenseKey27, licenseKey28, licenseKey29,
          licenseKey30, licenseKey31, licenseKey32, licenseKey33, licenseKey34, licenseKey35, licenseKey36,
          licenseKey37, licenseKey38, licenseKey39, licenseKey40, licenseKey41, licenseKey42, licenseKey43,
          licenseKey44, licenseKey45, licenseKey46);
    }
  }
}