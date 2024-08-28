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

package com.ericsson.enm.shm.instantaneouslicensing.sslserver.request;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElisRequest {

    private String type;
    private String euft;
    private String swdxId;
    private String globalCustomerId;
    private String destination;
    private Collection<Node> nodes = new ArrayList<>();
    private String batchLkfRequestResultsVersion;
    private String basebandType;

    /**
     * @return the nodes
     */
    public Collection<Node> getNodes() {
        return nodes;
    }

    /**
     * @return the value of request type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type to set the value of request type
     */

    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(final Collection<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(final String destination) {
        this.destination = destination;
    }

    /**
     * @return the version
     */
    public String getSwdxId() {
        return swdxId;
    }

    /**
     * @param version the version to set
     */
    public void setSwdxId(final String version) {
        this.swdxId = version;
    }

    /**
     * @return the euft
     */
    public String getEuft() {
        return euft;
    }

    /**
     * @param euft the euft to set
     */
    public void setEuft(final String euft) {
        this.euft = euft;
    }

    /**
     * @return the globalCustomerId
     */
    public String getGlobalCustomerId() {
        return globalCustomerId;
    }

    /**
     * @param globalCustomerId the globalCustomerId to set
     */
    public void setGlobalCustomerId(final String globalCustomerId) {
        this.globalCustomerId = globalCustomerId;
    }

    /**
     * @return the batchLkfRequestResultsVersion which identifies the batchLkfRequestResult XML version to request ELIS to include in the LKF package
     */
    public String getBatchLkfRequestResultsVersion() {
        return batchLkfRequestResultsVersion;
    }

    /**
     * @param batchLkfRequestResultsVersion the batchLkfRequestResultsVersion to set
     */
    public void setBatchLkfRequestResultsVersion(final String batchLkfRequestResultsVersion) {
        this.batchLkfRequestResultsVersion = batchLkfRequestResultsVersion;
    }

    /**
     * @return the basebandType
     */
    public String getBasebandType() {
        return basebandType;
    }

    /**
     * @param basebandType the basebandType to set
     */
    public void setBasebandType(final String basebandType) {
        this.basebandType = basebandType;
    }

    @Override
    public String toString() {
        return "ElisRequest{" +
               "type='" + type + '\'' +
               ", euft='" + euft + '\'' +
               ", swdxId='" + swdxId + '\'' +
               ", globalCustomerId='" + globalCustomerId + '\'' +
               ", destination='" + destination + '\'' +
               ", nodes=" + nodes +
               ", batchLkfRequestResultsVersion='" + batchLkfRequestResultsVersion + '\'' +
               ", basebandType='" + basebandType + '\'' +
               '}';
    }
}
