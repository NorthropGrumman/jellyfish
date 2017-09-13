package ${dto.packageName};

public class ${dto.model.name}DataConversion {
#foreach($data in ${dto.allInputData})

   public static ${dto.basePackage}.events.${data.name} convert(${data.fullyQualifiedName}Wrapper.${data.name} from) {
      ${dto.basePackage}.events.${data.name} to = new ${dto.basePackage}.events.${data.name}();

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

      return to;
   }
#end
#foreach($data in ${dto.allOutputData})

   public static ${data.fullyQualifiedName}Wrapper.${data.name} convert(${dto.basePackage}.events.${data.name} from) {
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
      for (${dto.basePackage}.events.${name} value : from.get${fieldCap}()) {
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
#end
#foreach ( $enum in ${dto.allInputEnums} )

   public static ${dto.basePackage}.events.${enum.name} convert(${enum.fullyQualifiedName}Wrapper.${enum.name} from) {
      final ${dto.basePackage}.events.${enum.name} to;

      switch (from) {
#foreach ( $value in $enum.values)
      case $value:
         to = ${dto.basePackage}.events.${enum.name}.$value;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }

      return to;
   }
#end
#foreach ( $enum in ${dto.allInputEnums} )

   public static ${enum.fullyQualifiedName}Wrapper.${enum.name} convert(${dto.basePackage}.events.${enum.name} from) {
      final ${enum.fullyQualifiedName}Wrapper.${enum.name} to;

      switch (from) {
#foreach ( $value in $enum.values)
      case $value:
         to = ${enum.fullyQualifiedName}Wrapper.${enum.name}.$value;
         break;
#end
      default:
         throw new IllegalArgumentException("Unknown enum: " + from);
      }

      return to;
   }
#end   

}
