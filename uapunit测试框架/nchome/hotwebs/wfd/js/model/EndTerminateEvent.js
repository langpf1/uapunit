function EndTerminateEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndTerminateEvent";
	this.name = "EndTerminateEvent";
	this.infoClass = "EndTerminateEventInfo";
};

EndTerminateEvent.prototype = new ThrowEvent();
EndTerminateEvent.prototype.constructor = EndTerminateEvent;


function EndTerminateEventInfo(id)
{
	TerminateEventDefinitionInfo.call(this, id);
};

EndTerminateEventInfo.prototype = new TerminateEventDefinitionInfo();
EndTerminateEventInfo.prototype.constructor = EndTerminateEventInfo;