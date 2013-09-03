/**
 * Class: StartEvent
 */
function StartEvent(id)
{
	CatchEvent.call(this, id);
	this.name = "StartEvent";
	this.initiator = "";
	this.resultVariableName = "";
	this.formKey = "";
	this.formProperties = null;
	this.extensionElements = null;
	this.label = "StartEvent";
	this.infoClass = "StartEventInfo";
};

StartEvent.prototype = new CatchEvent();
StartEvent.prototype.constructor = StartEvent;

/**
 * Class: StartEventInfo
 */
function StartEventInfo(id)
{
	EventInfo.call(this, id);
};

StartEventInfo.prototype = new EventInfo();
StartEventInfo.prototype.constructor = StartEventInfo;
