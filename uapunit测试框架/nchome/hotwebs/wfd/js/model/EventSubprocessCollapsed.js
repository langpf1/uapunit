/**
 * Class: EventSubprocessCollapsed
 */
function EventSubprocessCollapsed(id)
{
	Activity.call(this, id);
	this.name = "EventSubprocessCollapsed";
	this.calledElement = "";
	this.inParameters = [];
	this.outParameters = [];
	this.label = "EventSubprocessCollapsed";
	this.infoClass = "EventSubprocessCollapsedInfo";
};

EventSubprocessCollapsed.prototype = new Activity();
EventSubprocessCollapsed.prototype.constructor = EventSubprocessCollapsed;



/**
 * Class: EventSubprocessCollapsedInfo
 */
function EventSubprocessCollapsedInfo(id)
{
	ActivityInfo.call(this, id);
    this.properties.push(new Property('calledElement', 'Main Config', false, 
		EditorType.text, '','calledElement','calledElementHints'));
	this.properties.push(new Property('inParameters', 'Main Config', false, 
		EditorType.text, '','inParameters','inParametersHints'));
	this.properties.push(new Property('outParameters', 'Main Config', false, 
		EditorType.text, '','outParameters','outParametersHints'));
};

EventSubprocessCollapsedInfo.prototype = new ActivityInfo();
EventSubprocessCollapsedInfo.prototype.constructor = EventSubprocessCollapsedInfo;