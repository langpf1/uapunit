function IntermediateCatchMultipleEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMultipleEvent";
	this.name = "CatchMultipleEvent";
	this.infoClass = "IntermediateCatchMultipleEventInfo";
};

IntermediateCatchMultipleEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMultipleEvent.prototype.constructor = IntermediateCatchMultipleEvent;




function IntermediateCatchMultipleEventInfo(id)
{
	EventInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchMultipleEventInfo.prototype = new EventInfo();
IntermediateCatchMultipleEventInfo.prototype.constructor = IntermediateCatchMultipleEventInfo;