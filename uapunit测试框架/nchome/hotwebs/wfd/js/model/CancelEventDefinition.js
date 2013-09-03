/**
 * Class: CancelEventDefinition
 */
function CancelEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.label = "CancelEventDefinition";
};

CancelEventDefinition.prototype = new EventDefinition();
CancelEventDefinition.prototype.constructor = CancelEventDefinition;


function CancelEventDefinitionInfo(id){
    EventInfo.call(this,id);
    	
};

