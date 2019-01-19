/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2019, Northrop Grumman Systems Corporation
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Northrop Grumman Systems Corporation. The intellectual and technical concepts
 * contained herein are proprietary to Northrop Grumman Systems Corporation and
 * may be covered by U.S. and Foreign Patents or patents in process, and are
 * protected by trade secret or copyright law. Dissemination of this information
 * or reproduction of this material is strictly forbidden unless prior written
 * permission is obtained from Northrop Grumman.
 */
package com.ngc.seaside.jellyfish.service.config.api.dto.zeromq;

/**
 * Defines the different modes that a ZeroMQ connection can use.
 */
public enum ConnectionType {

   /**
    * Indicates that the source of the link binds as a server and the target connects as a client. This means the
    * target (the client) connects to the source (the server).
    */
   SOURCE_BINDS_TARGET_CONNECTS,

   /**
    * Indicates that the source of the link connects as a client and the target binds as a server. This means the
    * source (the client) connects to the target (the server).
    */
   SOURCE_CONNECTS_TARGET_BINDS

}
