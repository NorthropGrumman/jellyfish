package ${dto.topicsEnum.packageName};

#set ($ignore = $dto.topicsEnum.imports.add('com.ngc.seaside.service.transport.api.ITransportTopic'))
#foreach ($i in $dto.topicsEnum.imports)
import ${i};
#end

public enum ${dto.topicsEnum.name} implements ITransportTopic {
#foreach ($topic in $dto.topicsEnum.values)
   $topic,
#end
}
