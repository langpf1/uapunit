/**
 * Class: IntermediateCatchEvent
 */
function IntermediateCatchEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "Intermediate CatchEvent";
	this.name = "IntermediateCatchEvent";
	this.infoClass = "IntermediateCatchEventInfo";
};

IntermediateCatchEvent.prototype = new CatchEvent();
IntermediateCatchEvent.prototype.constructor = IntermediateCatchEvent;



/**
 * Class: IntermediateCatchEventInfo
 */
function IntermediateCatchEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateCatchEventInfo.prototype = new EventInfo();
IntermediateCatchEventInfo.prototype.constructor = IntermediateCatchEventInfo;