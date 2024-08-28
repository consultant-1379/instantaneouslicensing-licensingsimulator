/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.enm.shm.instantaneouslicensing.sslserver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    public static String formatDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy_HHmmss");
        Date date = new Date();
        return formatter.format(date);
    }
}
