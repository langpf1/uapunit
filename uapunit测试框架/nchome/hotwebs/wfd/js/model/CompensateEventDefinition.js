/**
 * Class: CompensateEventDefinition
 */
function CompensateEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.waitForCompletion = "";
	this.activityReference = "";
	this.label = "Compensate EventDefinition";
	this.infoClass = "CompensateEventDefinitionInfo";
};

CompensateEventDefinition.prototype = new EventDefinition();
CompensateEventDefinition.prototype.constructor = CompensateEventDefinition;


function CompensateEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('waitForCompletion', 'CompensateEventDefinition', false, 
	EditorType.text, '','waitForCompletion','waitForCompletionHints'));
   	this.properties.push(new Property('activityReference', 'CompensateEventDefinition', false, 
	EditorType.text, '','activityReference','activityReferenceHints'));
};
