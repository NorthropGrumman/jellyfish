package ${dto.nonSuffixPackageName}.transport.topic;

import com.ngc.seaside.service.transport.api.ITransportTopic;

public enum ${dto.className}TransportTopics implements ITransportTopic {
#foreach ($topic in $dto.transportTopics)
   $topic,
#end
}
