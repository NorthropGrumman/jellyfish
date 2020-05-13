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

import com.google.common.base.Preconditions;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.server.rule.RulesDefinition;

import java.util.Objects;

/**
 * Base class for rules that register themselves with a repository.
 */
public abstract class AbstractRule implements Comparable<AbstractRule> {

   /**
    * The key for this rule.
    */
   protected final RuleKey key;

   /**
    * Creates a new rule with a key.
    *
    * @param key the key of the rule
    */
   protected AbstractRule(RuleKey key) {
      this.key = Preconditions.checkNotNull(key, "key may not be null!");
   }

   /**
    * Adds this rule to the given repository.  The key of the repository in this rule's key must match the key of the
    * given repository.
    *
    * @param repository the repository to add the rule to
    * @throws IllegalArgumentException the repository key of the given repository does not match the repository key of
    *                                  this rule's key
    */
   public void addRuleToRepository(RulesDefinition.NewRepository repository) {
      Preconditions.checkNotNull(repository, "repository may not be null!");
      Preconditions.checkArgument(
            repository.key().equals(key.repository()),
            "the repository key of the given repository (%s) does not match the repository key of this rule's key"
            + " (%s)!",
            repository.key(),
            key.repository());
      configure(repository.createRule(key.rule()));
   }

   /**
    * Gets the key for this rule.
    *
    * @return the key for this rule
    */
   public RuleKey getKey() {
      return key;
   }

   @Override
   public int compareTo(AbstractRule o) {
      return key.compareTo(o.key);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof AbstractRule)) {
         return false;
      }
      AbstractRule that = (AbstractRule) o;
      return Objects.equals(key, that.key);
   }

   @Override
   public int hashCode() {
      return Objects.hash(key);
   }

   /**
    * Invoked to configure a new instance of a rule.  This is invoked every time the rule is added to a repository.
    *
    * @param rule the rule to configure
    */
   protected abstract void configure(RulesDefinition.NewRule rule);
}
