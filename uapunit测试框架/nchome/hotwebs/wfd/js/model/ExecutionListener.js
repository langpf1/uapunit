/**
 * Class: ExecutionListener
 */
function ExecutionListener(id)
{
	EventListener.call(this, id);
	this.label = "Execution Listener";
};

ExecutionListener.prototype = new EventListener();
ExecutionListener.prototype.constructor = ExecutionListener;



