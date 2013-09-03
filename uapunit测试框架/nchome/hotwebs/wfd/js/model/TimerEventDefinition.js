/**
 * Class: TimerEventDefinition
 */
function TimerEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.timeDate = "";
	this.timeDuration = "";
	this.timeCycle = "";
	this.label = "Timer EventDefinition";
};

TimerEventDefinition.prototype = new EventDefinition();
TimerEventDefinition.prototype.constructor = TimerEventDefinition;


function TimerEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('timeDate', 'Timer Event Definition', false, 
	EditorType.text, '' ,'timeDate','timeDateHints'));  
    this.properties.push(new Property('timeDuration', 'Timer Event Definition', false, 
    EditorType.text, '','timeDuration','timeDurationHints'));  
    this.properties.push(new Property('timeCycle', 'Timer Event Definition', false, 
    EditorType.text, '','timeCycle','timeCycleHints'));  
};
