function IntermediateThrowCompensationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowCompensationEvent";
	this.name = "ThrowCompensationEvent";
	this.infoClass = "IntermediateThrowCompensationEventInfo";
};

IntermediateThrowCompensationEvent.prototype = new ThrowEvent();
IntermediateThrowCompensationEvent.prototype.constructor = IntermediateThrowCompensationEvent;




function IntermediateThrowCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
};

IntermediateThrowCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
IntermediateThrowCompensationEventInfo.prototype.constructor = IntermediateThrowCompensationEventInfo;