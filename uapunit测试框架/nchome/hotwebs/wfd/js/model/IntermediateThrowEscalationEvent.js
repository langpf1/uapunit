function IntermediateThrowEscalationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowEscalationEvent";
	this.name = "ThrowEscalationEvent";
	this.infoClass = "IntermediateThrowEscalationEventInfo";
};

IntermediateThrowEscalationEvent.prototype = new ThrowEvent();
IntermediateThrowEscalationEvent.prototype.constructor = IntermediateThrowEscalationEvent;




function IntermediateThrowEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};

IntermediateThrowEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
IntermediateThrowEscalationEventInfo.prototype.constructor = IntermediateThrowEscalationEventInfo;