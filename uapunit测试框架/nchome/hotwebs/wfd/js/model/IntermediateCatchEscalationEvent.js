function IntermediateCatchEscalationEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchEscalationEvent";
	this.name = "CatchEscalationEvent";
	this.infoClass = "IntermediateCatchEscalationEventInfo";
};

IntermediateCatchEscalationEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchEscalationEvent.prototype.constructor = IntermediateCatchEscalationEvent;




function IntermediateCatchEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};

IntermediateCatchEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
IntermediateCatchEscalationEventInfo.prototype.constructor = IntermediateCatchEscalationEventInfo;