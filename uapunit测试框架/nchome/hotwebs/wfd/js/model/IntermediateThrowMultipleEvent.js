function IntermediateThrowMultipleEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowMultipleEvent";
	this.name = "ThrowMultipleEvent";
	this.infoClass = "IntermediateThrowMultipleEventInfo";
};

IntermediateThrowMultipleEvent.prototype = new ThrowEvent();
IntermediateThrowMultipleEvent.prototype.constructor = IntermediateThrowMultipleEvent;




function IntermediateThrowMultipleEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateThrowMultipleEventInfo.prototype = new EventInfo();
IntermediateThrowMultipleEventInfo.prototype.constructor = IntermediateThrowMultipleEventInfo;