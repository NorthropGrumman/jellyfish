package ${dto.packageName}

public class ${dto.model.name}DataConversion {
#foreach($value in ${dto.allInputs})
#set ($eventsPackage = $dto.getEventsPackageName().apply($value))
   public static ${eventsPackage}.${value.name} convert(${value.fullyQualifiedName}Wrapper.${value.name} from) {
#if (${IData.isInstance($value)})
#set ($data = $value)
      ${eventsPackage}.${value.name} to = new ${eventsPackage}.${value.name}();

#foreach ($field in $data.fields)
#set( $fieldCap = "${field.name.substring(0, 1).toUpperCase()}${field.name.substring(1)}" )
#if ( $field.type == "DATA" || $field.type == "ENUM" )
#if ( $field.type == "DATA")
#set ( $name = $field.referencedDataType.name )
#else
#set ( $name = $field.referencedEnumeration.name )
#end
#if ( $field.cardinality == "MANY" )
      to.set${fieldCap}(new java.util.ArrayList<>(from.get${fieldCap}Count()));
      for (${data.fullyQualifiedName}Wrapper.${name} value : from.get${fieldCap}List()) {
         to.get${fieldCap}().add(convert(value));
      }
#else
      to.set${fieldCap}(convert(from.get${fieldCap}()));
#end
#else
#if ( $field.cardinality == "MANY" )
      to.set${fieldCap}(new java.util.ArrayList<>(from.get${fieldCap}List()));
#else
      to.set${fieldCap}(from.get${fieldCap}());
#end
#end
#end
#else
#set ($enum = $value)
      final ${eventsPackage}.${enum.name} to;

      switch (from) {
#foreach ( $enumValue in $enum.values)
      case $enumValue:
         to = ${eventsPackage}.${enum.name}.$enumValue;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }
#end

      return to;
   }
#end
#foreach($value in ${dto.allOutputs})
#set ($eventsPackage = $dto.getEventsPackageName().apply($value))

   public static ${value.fullyQualifiedName}Wrapper.${value.name} convert(${eventsPackage}.${value.name} from) {
#if (${IData.isInstance($value)})
#set ($data = $value)
      ${data.fullyQualifiedName}Wrapper.${data.name}.Builder to = ${data.fullyQualifiedName}Wrapper.${data.name}.newBuilder();

#foreach ( $field in $data.fields )
#set( $fieldCap = "${field.name.substring(0, 1).toUpperCase()}${field.name.substring(1)}" )
#if ( $field.type == "DATA" || $field.type == "ENUM" )
#if ( $field.type == "DATA")
#set ( $name = $field.referencedDataType.name )
#else
#set ( $name = $field.referencedEnumeration.name )
#end
#if ( $field.cardinality == "MANY" )
      for (${eventsPackage}.${name} value : from.get${fieldCap}()) {
         to.add${fieldCap}(convert(value));
      }
#else
      to.set${fieldCap}(convert(from.get${fieldCap}()));
#end
#else
#if ( $field.cardinality == "MANY" )
      to.addAll${fieldCap}(from.get${fieldCap}());
#else
      to.set${fieldCap}(from.get${fieldCap}());
#end
#end
#end

      return to.build();
   }
#else
#set ($enum = $value)
      final ${enum.fullyQualifiedName}Wrapper.${enum.name} to;

      switch (from) {
#foreach ( $enumValue in $enum.values)
      case $enumValue:
         to = ${enum.fullyQualifiedName}Wrapper.${enum.name}.$enumValue;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }

      return to;
   }
#end
#end

}
