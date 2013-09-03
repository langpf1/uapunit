/**
 * Class: ThrowEvent
 */
function ThrowEvent(id)
{
	Event.call(this, id);
	this.label = "Throw Event";
};

ThrowEvent.prototype = new Event();
ThrowEvent.prototype.constructor = ThrowEvent;




/**
 * Class: ThrowEventInfo
 */
function ThrowEventInfo(id)
{
	EventInfo.call(this, id);
};

ThrowEventInfo.prototype = new EventInfo();
ThrowEventInfo.prototype.constructor = ThrowEventInfo;



