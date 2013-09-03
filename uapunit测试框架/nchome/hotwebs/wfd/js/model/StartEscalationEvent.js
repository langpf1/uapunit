function StartEscalationEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new EscalationEventDefinition();
	this.name = "StartEscalationEvent";
	this.label = "StartEscalationEvent";
	this.infoClass = "StartEscalationEventInfo";
};

StartEscalationEvent.prototype = new StartEvent();
StartEscalationEvent.prototype.constructor = StartEscalationEvent;

function StartEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
}	
StartEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
StartEscalationEventInfo.prototype.constructor = StartEscalationEventInfo;

