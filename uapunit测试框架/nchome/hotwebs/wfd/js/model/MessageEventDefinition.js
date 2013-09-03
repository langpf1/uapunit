/**
 * Class: MessageEventDefinition
 */
function MessageEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.operationRef = "";
	this.messageRef = "";
	this.label = "Message EventDefinition";
	this.infoClass = "MessageEventDefinitionInfo";
};

MessageEventDefinition.prototype = new EventDefinition();
MessageEventDefinition.prototype.constructor = MessageEventDefinition;


function MessageEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('operationRef', 'Message Event Definition', false, 
    EditorType.text, '','operationRef','operationRefHints'));  
    this.properties.push(new Property('messageRef', 'Message Event Definition', false, 
    EditorType.text, '','messageRef','messageRefHints'));  
};

