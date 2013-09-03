/**
 * Class: Event
 */
function Event(id)
{
	FlowNode.call(this, id);
	this.cancelEventDefinition ;//CancelEventDefinition
	this.compensateEventDefinition ;
	this.conditionalEventDefinition ;
	this.errorEventDefinition ;
	this.linkEventDefinition ;
	this.messageEventDefinition ;
	this.signalEventDefinition ;
	this.timerEventDefinition ;
	this.terminateEventDefinition ;
	this.eventDefinitions = null ;
	//有些函数没有完成
	this.label = "Event Listener";
};

Event.prototype = new FlowNode();
Event.prototype.constructor = Event;

/**
 * Class: EventInfo
 */
function EventInfo(id)
{
	FlowNodeInfo.call(this, id);
};

EventInfo.prototype = new FlowNodeInfo();
EventInfo.prototype.constructor = EventInfo;
