function StartCompensateEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new CompensateEventDefinition();
	this.label = "StartCompensateEvent";
	this.infoClass = "StartCompensateEventInfo";
	this.name = "StartCompensateEvent";
};

StartCompensateEvent.prototype = new StartEvent();
StartCompensateEvent.prototype.constructor = StartCompensateEvent;

function StartCompensateEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
}	
StartCompensateEventInfo.prototype = new CompensateEventDefinitionInfo();
StartCompensateEventInfo.prototype.constructor = StartCompensateEventInfo;
