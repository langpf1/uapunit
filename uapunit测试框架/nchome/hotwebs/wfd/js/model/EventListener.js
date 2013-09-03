/**
 * Class: EventListener
 */
function EventListener(id)
{
	BaseElement.call(this, id);
	this.event = "";
	this.implementationType = "";
	this.implementation = "";
	this.method = "";
	this.scriptProcessor = "";
	this.fieldExtensions = [];
	this.label = "Event Listener";
};

EventListener.prototype = new BaseElement();
EventListener.prototype.constructor = EventListener;

