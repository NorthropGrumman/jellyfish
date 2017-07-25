package ${package};

public class ${model.getName()}{
	#foreach($field in $model.getInputs().toArray())
		$field.getName()
	#end

	#foreach($field in $model.getOutputs().toArray())
		$field.getName()
	#end

	${model.getOutputs().toArray()}

}