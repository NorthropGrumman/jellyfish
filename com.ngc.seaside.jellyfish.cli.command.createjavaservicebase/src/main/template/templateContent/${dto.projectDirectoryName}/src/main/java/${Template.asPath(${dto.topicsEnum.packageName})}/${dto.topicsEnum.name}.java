package ${dto.topicsEnum.packageName};

#foreach ($i in $dto.topicsEnum.imports)
import ${i};
#end

public enum ${dto.topicsEnum.name}#if ($dto.topicsEnum.implementedInterface) implements ${dto.topicsEnum.implementedInterface.name}#end {
#foreach ($topic in $dto.topicsEnum.values)
   $topic,
#end
}
