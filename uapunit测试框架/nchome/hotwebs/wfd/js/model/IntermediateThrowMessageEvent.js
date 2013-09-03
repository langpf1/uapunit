function IntermediateThrowMessageEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowMessageEvent";
	this.name = "ThrowMessageEvent";
	this.infoClass = "IntermediateThrowMessageEventInfo";
};

IntermediateThrowMessageEvent.prototype = new ThrowEvent();
IntermediateThrowMessageEvent.prototype.constructor = IntermediateThrowMessageEvent;




function IntermediateThrowMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
};

IntermediateThrowMessageEventInfo.prototype = new MessageEventDefinitionInfo();
IntermediateThrowMessageEventInfo.prototype.constructor = IntermediateThrowMessageEventInfo;