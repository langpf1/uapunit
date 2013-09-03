function IntermediateCatchCancelEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchCancelEvent";
	this.name = "CatchCancelEvent";
	this.infoClass = "IntermediateCatchCancelEventInfo";
};

IntermediateCatchCancelEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchCancelEvent.prototype.constructor = IntermediateCatchCancelEvent;




function IntermediateCatchCancelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints')); 
	
};

IntermediateCatchCancelEventInfo.prototype = new CancelEventDefinitionInfo();
IntermediateCatchCancelEventInfo.prototype.constructor = IntermediateCatchCancelEventInfo;