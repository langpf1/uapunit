/**
 * Class: VOExchangeTask
 */
function VOExchangeTask(id)
{
	ServiceTask.call(this, id);
	this.label = "VOExchange Task";
};

VOExchangeTask.prototype = new ServiceTask();
VOExchangeTask.prototype.constructor = VOExchangeTask;



/**
 * Class: VOExchangeTaskInfo
 */
function VOExchangeTaskInfo(id)
{
	ServiceTaskInfo.call(this, id);
};

VOExchangeTaskInfo.prototype = new ServiceTaskInfo();
VOExchangeTaskInfo.prototype.constructor = VOExchangeTaskInfo;
