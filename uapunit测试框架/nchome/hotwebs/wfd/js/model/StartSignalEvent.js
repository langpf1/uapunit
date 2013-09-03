function StartSignalEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new SignalEventDefinition();
	this.name = "StartSignalEvent";
	this.label = "StartSignalEvent";
	this.infoClass = "StartSignalEventInfo";
};

StartSignalEvent.prototype = new StartEvent();
StartSignalEvent.prototype.constructor = StartSignalEvent;

function StartSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartSignalEventInfo.prototype = new SignalEventDefinitionInfo();
StartSignalEventInfo.prototype.constructor = StartSignalEventInfo;

