function EndErrorEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndErrorEvent";
	this.name = "EndErrorEvent";
	this.infoClass = "EndErrorEventInfo";
};

EndErrorEvent.prototype = new ThrowEvent();
EndErrorEvent.prototype.constructor = EndErrorEvent;

function EndErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
};

EndErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
EndErrorEventInfo.prototype.constructor = EndErrorEventInfo;

