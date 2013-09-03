function IntermediateCatchMessageEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMessageEvent";
	this.name = "CatchMessageEvent";
	this.infoClass = "IntermediateCatchMessageEventInfo";
};

IntermediateCatchMessageEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMessageEvent.prototype.constructor = IntermediateCatchMessageEvent;




function IntermediateCatchMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchMessageEventInfo.prototype = new MessageEventDefinitionInfo();
IntermediateCatchMessageEventInfo.prototype.constructor = IntermediateCatchMessageEventInfo;