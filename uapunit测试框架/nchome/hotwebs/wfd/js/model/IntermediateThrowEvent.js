/**
 * Class: IntermediateThrowEvent
 */
function IntermediateThrowEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "Intermediate ThrowEvent";
	this.name = "IntermediateThrowEvent";
	this.infoClass = "IntermediateThrowEventInfo";
};

IntermediateThrowEvent.prototype = new ThrowEvent();
IntermediateThrowEvent.prototype.constructor = IntermediateThrowEvent;


/**
 * Class: IntermediateThrowEventInfo
 */
function IntermediateThrowEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateThrowEventInfo.prototype = new EventInfo();
IntermediateThrowEventInfo.prototype.constructor = IntermediateThrowEventInfo;