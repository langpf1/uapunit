/**
 * Class: ErrorEventDefinition
 */
function ErrorEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.errorCode = "";
	this.label = "Error EventDefinition";
	this.infoClass = "ErrorEventDefinitionInfo";
	
};

ErrorEventDefinition.prototype = new EventDefinition();
ErrorEventDefinition.prototype.constructor = ErrorEventDefinition;


function ErrorEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('errorCode', 'Error Event Definition', false, 
    EditorType.text, '','errorCode','errorCodeHints'));   	
};
