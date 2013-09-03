function EndEsacalationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndEsacalationEvent";
	this.name = "EndEsacalationEvent";
	this.infoClass = "EndEsacalationEventInfo";
};
EndEsacalationEvent.prototype = new ThrowEvent();
EndEsacalationEvent.prototype.constructor = EndEsacalationEvent;

function EndEsacalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};
EndEsacalationEventInfo.prototype = new EscalationEventDefinitionInfo();
EndEsacalationEventInfo.prototype.constructor = EndEsacalationEventInfo;
