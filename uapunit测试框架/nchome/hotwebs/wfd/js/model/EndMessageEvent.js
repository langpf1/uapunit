function EndMessageEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndMessageEvent";
	this.name = "EndMessageEvent";
	this.infoClass = "EndMessageEventInfo";
};

EndMessageEvent.prototype = new ThrowEvent();
EndMessageEvent.prototype.constructor = EndMessageEvent;


function EndMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
};

EndMessageEventInfo.prototype = new MessageEventDefinitionInfo();
EndMessageEventInfo.prototype.constructor = EndMessageEventInfo;