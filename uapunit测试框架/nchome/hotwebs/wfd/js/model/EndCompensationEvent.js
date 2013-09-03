function EndCompensationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndCompensationEvent";
	this.name = "EndCompensationEvent";
	this.infoClass = "EndCompensationEventInfo";
};

EndCompensationEvent.prototype = new ThrowEvent();
EndCompensationEvent.prototype.constructor = EndCompensationEvent;



function EndCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
};

EndCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
EndCompensationEventInfo.prototype.constructor = EndCompensationEventInfo;