function IntermediateCatchMultiParallelEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMultiParallelEvent";
	this.name = "CatchMultiParallelEvent";
	this.infoClass = "IntermediateCatchMultiParallelEventInfo";
};

IntermediateCatchMultiParallelEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMultiParallelEvent.prototype.constructor = IntermediateCatchMultiParallelEvent;




function IntermediateCatchMultiParallelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints')); 
};

IntermediateCatchMultiParallelEventInfo.prototype = new CancelEventDefinitionInfo();
IntermediateCatchMultiParallelEventInfo.prototype.constructor = IntermediateCatchMultiParallelEventInfo;