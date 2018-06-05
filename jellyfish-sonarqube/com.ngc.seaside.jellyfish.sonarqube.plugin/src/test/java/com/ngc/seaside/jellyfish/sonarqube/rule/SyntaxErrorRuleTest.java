package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.junit.Before;
import org.junit.Test;

public class SyntaxErrorRuleTest extends AbstractRuleTestBase {

   private SyntaxErrorRule rule;

   @Before
   public void setupBase() {
      rule = new SyntaxErrorRule();
   }

   @Test
   public void testDoesConfigureItself() {
      rule.configure(newRuleInstance);
      assertRuleConfigured();
   }
}
