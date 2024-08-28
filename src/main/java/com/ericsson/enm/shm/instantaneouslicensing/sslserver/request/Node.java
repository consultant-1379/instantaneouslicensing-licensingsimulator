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
public class Node {

    private String nodeType;
    private String swRelease;
    private String swltId;
    private String fingerprint;
    private Collection<NodeCapacity> capacities = new ArrayList<>();

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    public String getSwltId() {
        return swltId;
    }

    public void setSwltId(final String swltId) {
        this.swltId = swltId;
    }

    /**
     * @return the fingerprint
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * @param fingerprint
     *            the fingerprint to set
     */
    public void setFingerprint(final String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * @return the swRelease
     */
    public String getSwRelease() {
        return swRelease;
    }

    /**
     * @param swRelease
     *            the swRelease to set
     */
    public void setSwRelease(final String swRelease) {
        this.swRelease = swRelease;
    }

    /**
     * @return the capacities
     */
    public Collection<NodeCapacity> getCapacities() {
        return capacities;
    }

    /**
     * @param capacities
     *            the capacities to set
     */
    public void setCapacities(final Collection<NodeCapacity> capacities) {
        this.capacities = capacities;
    }

}
