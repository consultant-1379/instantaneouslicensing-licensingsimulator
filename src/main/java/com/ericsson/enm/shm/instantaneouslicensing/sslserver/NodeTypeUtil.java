/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2022
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.enm.shm.instantaneouslicensing.sslserver;

import com.ericsson.enm.shm.instantaneouslicensing.sslserver.model.ldf.TargetNodeType;
import com.ericsson.enm.shm.instantaneouslicensing.sslserver.request.Node;

public class NodeTypeUtil {

  public static TargetNodeType getTargetNodeType(final Node node) {
    if (node.getNodeType().contains("MINI-LINK")) {
      return TargetNodeType.MINI_LINK;
    } else {
      return TargetNodeType.BASEBAND;
    }
  }
}
