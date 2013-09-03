function IntermediateCatchConditionalEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchConditionalEvent";
	this.name = "CatchConditionalEvent";
	this.infoClass = "IntermediateCatchConditionalEventInfo";
};

IntermediateCatchConditionalEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchConditionalEvent.prototype.constructor = IntermediateCatchConditionalEvent;




function IntermediateCatchConditionalEventInfo(id)
{
	ConditionalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchConditionalEventInfo.prototype = new ConditionalEventDefinitionInfo();
IntermediateCatchConditionalEventInfo.prototype.constructor = IntermediateCatchConditionalEventInfo;