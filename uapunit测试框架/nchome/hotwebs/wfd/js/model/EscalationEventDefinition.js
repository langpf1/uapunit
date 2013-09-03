/**
 * Class: EscalationEventDefinition
 */
function EscalationEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.escalationRef = "";
	this.label = "Escalation EventDefinition";
	this.infoClass = "EscalationEventDefinitionInfo";
	
};

EscalationEventDefinition.prototype = new EventDefinition();
EscalationEventDefinition.prototype.constructor = EscalationEventDefinition;


function EscalationEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('escalationRef', 'Escalation Event Definition', false, 
    EditorType.text, '','escalationRef','escalationRefHints'));   	
};

