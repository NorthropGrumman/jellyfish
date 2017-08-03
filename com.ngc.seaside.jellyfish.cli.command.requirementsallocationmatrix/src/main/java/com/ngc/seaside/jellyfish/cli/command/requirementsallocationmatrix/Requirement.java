package com.ngc.seaside.jellyfish.cli.command.requirementsallocationmatrix;

import com.ngc.seaside.systemdescriptor.model.api.model.IModel;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.TreeSet;

/**
 * POJO for a requirement
 */
public class Requirement implements Comparable {
   private String ID;
   private TreeSet<IModel> models = new TreeSet<>(Collections.reverseOrder());

   public Requirement(String name) {
      this.ID = name;

   }

   /**
    * The ID of this requirement
    *
    * @return feature ID
    */
   public String getID() {
      return ID;
   }

   /**
    * Returns a set of models that satisfy this requirement
    *
    * @return models satisfying this requirement
    */
   public TreeSet<IModel> getModels() {
      return models;
   }

   /**
    * Adds a model to the requirement
    *
    * @param model model to add
    */
   void addModel(IModel model) {
      this.models.add(model);
   }

   /**
    * Adds a collection of models to the requirement
    *
    * @param models models to add to the requirement
    */
   void addModels(Collection<IModel> models) {
      models.forEach(this::addModel);
   }

   @Override
   public int hashCode() {
      return Objects.hash(ID);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      }

      if (!(obj instanceof Requirement)) {
         return false;
      }
      Requirement that = (Requirement) obj;
      return Objects.equals(ID, that.ID);
   }

   @Override
   public int compareTo(Object obj) throws ClassCastException {
      if (!(obj instanceof Requirement)) {
         throw new ClassCastException("A Requirement object expected.");
      }
      int thatHashCode = obj.hashCode();
      return this.hashCode() - thatHashCode;
   }
}

