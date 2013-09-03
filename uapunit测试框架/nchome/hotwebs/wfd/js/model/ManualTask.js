/**
 * Class: ManualTask
 */
function ManualTask(id)
{
	Task.call(this, id);
	this.name = "Manual Task";
	this.label = "Manual Task";
	this.infoClass = "ManualTaskInfo";
};

ManualTask.prototype = new Task();
ManualTask.prototype.constructor = ManualTask;

/**
 * Class: ManualTaskInfo
 */
function ManualTaskInfo(id)
{
	TaskInfo.call(this, id);
};

ManualTaskInfo.prototype = new TaskInfo();
ManualTaskInfo.prototype.constructor = ManualTaskInfo;
	