/**
 * Class: ReceiveTask
 */
function ReceiveTask(id)
{
	Task.call(this, id);
	this.name = "Receive Task";
	this.label = "Receive Task";
	this.infoClass = "ReceiveTaskInfo";
};

ReceiveTask.prototype = new Task();
ReceiveTask.prototype.constructor = ReceiveTask;


/**
 * Class: ReceiveTaskInfo
 */
function ReceiveTaskInfo(id)
{
	TaskInfo.call(this, id);
};

ReceiveTaskInfo.prototype = new TaskInfo();
ReceiveTaskInfo.prototype.constructor = ReceiveTaskInfo;

