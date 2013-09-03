/**
 * Class: EventSubProcess
 */
function EventSubProcess(id)
{
	SubProcess.call(this, id);
	this.triggeredByEvent = true;
	this.name = "EventSubProcess";
	this.label = "Event SubProcess";
	this.infoClass = "EventSubProcessInfo";
};

EventSubProcess.prototype = new SubProcess();
EventSubProcess.prototype.constructor = EventSubProcess;


/**
 * Class: EventSubProcessInfo
 */
function EventSubProcessInfo(id)
{
	SubProcessInfo.call(this, id);
};

EventSubProcessInfo.prototype = new SubProcessInfo();
EventSubProcessInfo.prototype.constructor = EventSubProcessInfo;