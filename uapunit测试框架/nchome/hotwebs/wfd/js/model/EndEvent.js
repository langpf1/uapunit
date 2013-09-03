/**
 * Class: EndEvent
 */
function EndEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "End Event";
	this.name = "EndEvent";
	this.infoClass = "EndEventInfo";
};

EndEvent.prototype = new ThrowEvent();
EndEvent.prototype.constructor = EndEvent;


/**
 * Class: EndEventInfo
 */
function EndEventInfo(id)
{
	EventInfo.call(this, id);
};

EndEventInfo.prototype = new EventInfo();
EndEventInfo.prototype.constructor = EndEventInfo;