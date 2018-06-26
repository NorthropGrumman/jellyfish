package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.junit.Before;
import org.junit.Test;

public class SyntaxWarningRuleTest extends AbstractRuleTestBase {

   private SyntaxWarningRule rule;

   @Before
   public void setupBase() {
      rule = new SyntaxWarningRule();
   }

   @Test
   public void testDoesConfigureItself() {
      rule.configure(newRuleInstance);
      assertRuleConfigured();
   }
}
