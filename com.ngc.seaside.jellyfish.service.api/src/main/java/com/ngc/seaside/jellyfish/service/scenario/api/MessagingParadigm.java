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
