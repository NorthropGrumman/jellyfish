package com.ngc.seaside.jellyfish.utilities.command;

/**
 * Defines the different phases a command can be executed in.
 */
public enum JellyfishCommandPhase {
   /**
    * The default phase occurs when Jellyfish should generate an initial project and stubs only.  These stubs may be
    * later modified but will never be regenerated.  Fully generated code should not be generated in this phase.
    */
   DEFAULT,

   /**
    * The deferred phase occurs when Jellyfish should generate fully generated code but not generate any stubs.  Only
    * fully generated code that is never modified should be generated in this phase.
    */
   DEFERRED,
}
