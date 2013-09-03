/**
 * Class: ConditionalEventDefinition
 */
function ConditionalEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.condition;//public Condition condition
	this.label = "Conditional EventDefinition";
	this.infoClass = "ConditionalEventDefinitionInfo";
};

ConditionalEventDefinition.prototype = new EventDefinition();
ConditionalEventDefinition.prototype.constructor = ConditionalEventDefinition;


function ConditionalEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('condition', 'Conditional Event Definition', false, 
    EditorType.refbox, {url:'./dialog/expression/expression.html',features:'dialogHeight:600px;dialogWidth:800px;center:yes'},'condition','conditionHints'));
   	
};
