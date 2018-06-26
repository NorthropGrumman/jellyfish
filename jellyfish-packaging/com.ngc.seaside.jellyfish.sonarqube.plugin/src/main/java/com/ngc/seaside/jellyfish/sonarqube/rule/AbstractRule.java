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
