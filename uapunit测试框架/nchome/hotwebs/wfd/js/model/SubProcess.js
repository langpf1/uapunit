/**
 * Class: SubProcess
 */
function SubProcess(id)
{
	Activity.call(this, id);
	this.name = "Sub-Process";
	this.triggeredByEvent = false;
	this.flowElements = null;
	this.isContainer = true;
	this.isCanBound = true;
	this.label = "Sub-Process";
	this.infoClass = "SubProcessInfo";
};

SubProcess.prototype = new Activity();
SubProcess.prototype.constructor = SubProcess;

/**
 * Class: SubProcessInfo
 */
function SubProcessInfo(id)
{
	ActivityInfo.call(this, id);
};

SubProcessInfo.prototype = new ActivityInfo();
SubProcessInfo.prototype.constructor = SubProcessInfo;

