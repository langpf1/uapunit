function StartConditionalEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new ConditionalEventDefinition();
	this.name = "StartConditionalEvent";
	this.label = "StartConditionalEvent";
	this.infoClass = "StartConditionalEventInfo";
};

StartConditionalEvent.prototype = new StartEvent();
StartConditionalEvent.prototype.constructor = StartConditionalEvent;


function StartConditionalEventInfo(id)
{
	ConditionalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartConditionalEventInfo.prototype = new ConditionalEventDefinitionInfo();
StartConditionalEventInfo.prototype.constructor = StartConditionalEventInfo;

