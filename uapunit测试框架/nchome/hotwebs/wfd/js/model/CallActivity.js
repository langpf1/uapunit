/**
 * Class: CallActivity
 */
function CallActivity(id)
{
	Activity.call(this, id);
	this.name = "Call Activity";
	this.calledElement = "";
	this.inParameters = null;
	this.outParameters = null;
	this.label = "Call Activity";
	this.infoClass = "CallActivityInfo";
};

CallActivity.prototype = new Activity();
CallActivity.prototype.constructor = CallActivity;


/**
 * Class: CallActivityInfo
 */
function CallActivityInfo(id)
{
	ActivityInfo.call(this, id);
    this.properties.push(new Property('calledElement', 'Main Config', false, 
		EditorType.text, '','calledElement','calledElementHints'));
	this.properties.push(new Property('inParameters', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/inParameters.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'inParameters','inParametersHints'));
	this.properties.push(new Property('outParameters', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/outParameters.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'outParameters','outParametersHints'));
};

CallActivityInfo.prototype = new ActivityInfo();
CallActivityInfo.prototype.constructor = CallActivityInfo;

