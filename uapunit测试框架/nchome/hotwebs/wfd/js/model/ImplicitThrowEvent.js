/**
 * Class: ImplicitThrowEvent
 */
function ImplicitThrowEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "Implicit ThrowEvent";
};

ImplicitThrowEvent.prototype = new ThrowEvent();
ImplicitThrowEvent.prototype.constructor = ImplicitThrowEvent;



/**
 * Class: ImplicitThrowEventInfo
 */
function ImplicitThrowEventInfo(id)
{
	ThrowEventInfo.call(this, id);
};

ImplicitThrowEventInfo.prototype = new ThrowEventInfo();
ImplicitThrowEventInfo.prototype.constructor = ImplicitThrowEventInfo;

