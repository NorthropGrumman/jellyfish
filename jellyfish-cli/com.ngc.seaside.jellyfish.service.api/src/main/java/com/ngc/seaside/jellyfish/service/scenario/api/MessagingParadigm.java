/**
 * UNCLASSIFIED
 * Northrop Grumman Proprietary
 * ____________________________
 *
 * Copyright (C) 2018, Northrop Grumman Systems Corporation
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
package com.ngc.seaside.jellyfish.service.scenario.api;

/**
 * Defines the deffest types of messaging paradigms that are supported.
 */
public enum MessagingParadigm {
   /**
    * A publish/subscribe paradigm involves multiple components.  Some components only publish, some only subscribe, and
    * some do both.  This type of messaging implies asynchronous behavior.
    */
   PUBLISH_SUBSCRIBE,

   /**
    * A request/response paradigm involves exactly two components: a client component which begins a request and a
    * server component which responds to a request.  This type of messaging implies synchronous behavior.
    */
   REQUEST_RESPONSE
}
