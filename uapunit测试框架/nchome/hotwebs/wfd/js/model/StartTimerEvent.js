function StartTimerEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new TimerEventDefinition();
	this.name = "StartTimerEvent";
	this.label = "StartTimerEvent";
	this.infoClass = "StartTimerEventInfo";
};

StartTimerEvent.prototype = new StartEvent();
StartTimerEvent.prototype.constructor = StartTimerEvent;

function StartTimerEventInfo(id)
{
	TimerEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartTimerEventInfo.prototype = new TimerEventDefinitionInfo();
StartTimerEventInfo.prototype.constructor = StartTimerEventInfo;

