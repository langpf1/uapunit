function IntermediateCatchTimerEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchTimerEvent";
	this.name = "CatchTimerEvent";
	this.infoClass = "IntermediateCatchTimerEventInfo";
};

IntermediateCatchTimerEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchTimerEvent.prototype.constructor = IntermediateCatchTimerEvent;




function IntermediateCatchTimerEventInfo(id)
{
	TimerEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchTimerEventInfo.prototype = new TimerEventDefinitionInfo();
IntermediateCatchTimerEventInfo.prototype.constructor = IntermediateCatchTimerEventInfo;