/**
 * UNCLASSIFIED
 *
 * Copyright 2020 Northrop Grumman Systems Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ngc.seaside.jellyfish.sonarqube.rule;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AbstractRuleTest {

   private TestableRule rule;

   @Mock
   private RulesDefinition.NewRepository repository;

   @Mock
   private RulesDefinition.NewRule newRuleInstance;

   @Test
   public void testDoesConfigureRule() {
      RuleKey key = RuleKey.of("repo", "key");
      when(repository.createRule(key.rule())).thenReturn(newRuleInstance);
      when(repository.key()).thenReturn(key.repository());

      rule = new TestableRule(key);
      rule.addRuleToRepository(repository);

      assertEquals("key not correct!",
                   key,
                   rule.getKey());
      assertEquals("configure not invoked with created rule!",
                   newRuleInstance,
                   rule.rule);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testDoesRequireRepoKeysToMatch() {
      RuleKey key = RuleKey.of("repo", "key");
      when(repository.key()).thenReturn("something else");

      rule = new TestableRule(key);
      rule.addRuleToRepository(repository);
   }

   private static class TestableRule extends AbstractRule {

      RulesDefinition.NewRule rule;

      protected TestableRule(RuleKey key) {
         super(key);
      }

      @Override
      protected void configure(RulesDefinition.NewRule rule) {
         this.rule = rule;
      }
   }
}
