/**
 * Class: TerminateEventDefinition
 */
function TerminateEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.label = "Terminate EventDefinition";
	
};

TerminateEventDefinition.prototype = new EventDefinition();
TerminateEventDefinition.prototype.constructor = TerminateEventDefinition;


function TerminateEventDefinitionInfo(id){
    EventInfo.call(this,id); 
};



