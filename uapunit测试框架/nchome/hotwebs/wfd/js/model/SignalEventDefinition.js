/**
 * Class: SignalEventDefinition
 */
function SignalEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.signalRef = "";
	this.label = "Signal EventDefinition";
	
};

SignalEventDefinition.prototype = new EventDefinition();
SignalEventDefinition.prototype.constructor = SignalEventDefinition;


function SignalEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('signalRef', 'Signal Event Definition', false, 
    EditorType.text, '','signalRef','signalRefHints'));  
   
};

