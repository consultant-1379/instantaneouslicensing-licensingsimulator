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

import java.time.LocalDateTime;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.response.LicenseKeyPackageBuilder;

/**
 * Event Generation Wrapper helper for LicenseKeyPackageBuilder
 */
public class LicenseFileGenEvent {

    private LicenseKeyPackageBuilder licenseKeyPackageBuilder;

    private LocalDateTime localDateTime;

    public LicenseKeyPackageBuilder getLicenseKeyPackageBuilder() {
        return licenseKeyPackageBuilder;
    }

    public void setLicenseKeyPackageBuilder(final LicenseKeyPackageBuilder licenseKeyPackageBuilder) {
        this.licenseKeyPackageBuilder = licenseKeyPackageBuilder;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(final LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}

