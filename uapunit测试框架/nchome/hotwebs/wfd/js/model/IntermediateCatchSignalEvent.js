function IntermediateCatchSignalEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchSignalEvent";
	this.name = "CatchSignalEvent";
	this.infoClass = "IntermediateCatchSignalEventInfo";
};

IntermediateCatchSignalEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchSignalEvent.prototype.constructor = IntermediateCatchSignalEvent;




function IntermediateCatchSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchSignalEventInfo.prototype = new SignalEventDefinitionInfo();
IntermediateCatchSignalEventInfo.prototype.constructor = IntermediateCatchSignalEventInfo;