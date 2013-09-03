/**
 * Class: TaskListener
 */
function TaskListener(id)
{
	//EventListener.call(this, id);
	//下面的属性，在TaskListener.java里面没有这些属性
	this.eventType=""
	this.implementationType=""
	this.implementation=""
	this.method=""
	this.fields=null;
	this.label = "Task Listener";
};

//TaskListener.prototype = new EventListener();
TaskListener.prototype.constructor = TaskListener;

