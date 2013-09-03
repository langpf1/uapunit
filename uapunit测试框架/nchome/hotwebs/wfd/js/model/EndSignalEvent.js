function EndSignalEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndSignalEvent";
	this.name = "EndSignalEvent";
	this.infoClass = "EndSignalEventInfo";
};

EndSignalEvent.prototype = new ThrowEvent();
EndSignalEvent.prototype.constructor = EndSignalEvent;


function EndSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
};

EndSignalEventInfo.prototype = new SignalEventDefinitionInfo();
EndSignalEventInfo.prototype.constructor = EndSignalEventInfo;