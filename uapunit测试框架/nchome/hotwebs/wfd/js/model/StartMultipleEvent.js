function StartMultipleEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new EventDefinition();
	this.name = "StartMultipleEvent";
	this.label = "StartMultipleEvent";
	this.infoClass = "StartMultipleEventInfo";
};

StartMultipleEvent.prototype = new StartEvent();
StartMultipleEvent.prototype.constructor = StartMultipleEvent;

function StartMultipleEventInfo(id)
{
	EventInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartMultipleEventInfo.prototype = new EventInfo();
StartMultipleEventInfo.prototype.constructor = StartMultipleEventInfo;

