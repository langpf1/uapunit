/**
 * Class: EventDefinition
 */
function EventDefinition(id)
{
	BaseElement.call(this, id);
	this.label = "EventDefinition";
};

EventDefinition.prototype = new BaseElement();
EventDefinition.prototype.constructor = EventDefinition;

