/**
 * Class: CatchEvent
 */
function CatchEvent(id)
{
	Event.call(this, id);
	this.interrupting ;
	this.boundaryRef = "";
};

CatchEvent.prototype = new Event();
CatchEvent.prototype.constructor = CatchEvent;


 /**
 * Class: CatchEventInfo
 */
function CatchEventInfo(id)
{
	EventInfo.call(this, id);
};

CatchEventInfo.prototype = new EventInfo();
CatchEventInfo.prototype.constructor = CatchEventInfo;

