function StartMessageEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new MessageEventDefinition();
	this.name = "StartMessageEvent";
	this.label = "StartMessageEvent";
	this.infoClass = "StartMessageEventInfo";
};

StartMessageEvent.prototype = new StartEvent();
StartMessageEvent.prototype.constructor = StartMessageEvent;

function StartMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
}	
StartMessageEventInfo.prototype = new MessageEventDefinitionInfo();
StartMessageEventInfo.prototype.constructor = StartMessageEventInfo;

