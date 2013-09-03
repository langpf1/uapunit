/**
 * Class: LinkEventDefinition
 */
function LinkEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.value = "";
	this.label = "Link EventDefinition";
	this.infoClass = "LinkEventDefinitionInfo";
};

LinkEventDefinition.prototype = new EventDefinition();
LinkEventDefinition.prototype.constructor = LinkEventDefinition;


function LinkEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('Value', 'Main Config', false, 
    EditorType.text, '','Value','ValueHints'));   	
};

