function StartErrorEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new ErrorEventDefinition();
	this.name = "StartErrorEvent";
	this.label = "StartErrorEvent";
	this.infoClass = "StartErrorEventInfo";
};

StartErrorEvent.prototype = new StartEvent();
StartErrorEvent.prototype.constructor = StartErrorEvent;


function StartErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
StartErrorEventInfo.prototype.constructor = StartErrorEventInfo;

