function EndCancelEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndCancelEvent";
	this.name = "EndCancelEvent";
	this.infoClass = "EndCancelEventInfo";
};

EndCancelEvent.prototype = new ThrowEvent();
EndCancelEvent.prototype.constructor = EndCancelEvent;




function EndCancelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
};

EndCancelEventInfo.prototype = new ConditionalEventDefinitionInfo();
EndCancelEventInfo.prototype.constructor = EndCancelEventInfo;