/**
 * Class: TaskListener
 */
function TaskListener(id)
{
	//EventListener.call(this, id);
	//��������ԣ���TaskListener.java����û����Щ����
	this.eventType=""
	this.implementationType=""
	this.implementation=""
	this.method=""
	this.fields=null;
	this.label = "Task Listener";
};

//TaskListener.prototype = new EventListener();
TaskListener.prototype.constructor = TaskListener;

