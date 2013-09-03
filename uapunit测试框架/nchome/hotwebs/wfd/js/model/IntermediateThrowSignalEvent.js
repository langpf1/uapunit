function IntermediateThrowSignalEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowSignalEvent";
	this.name = "ThrowSignalEvent";
	this.infoClass = "IntermediateThrowSignalEventInfo";
};

IntermediateThrowSignalEvent.prototype = new ThrowEvent();
IntermediateThrowSignalEvent.prototype.constructor = IntermediateThrowSignalEvent;




function IntermediateThrowSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
};

IntermediateThrowSignalEventInfo.prototype = new SignalEventDefinitionInfo();
IntermediateThrowSignalEventInfo.prototype.constructor = IntermediateThrowSignalEventInfo;