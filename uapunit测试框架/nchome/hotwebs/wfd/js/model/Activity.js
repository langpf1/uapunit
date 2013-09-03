/**
 * Class: Activity
 */
function Activity(id)
{
	FlowNode.call(this, id);
	this.asynchronous = false;
	this.ForCompensation = false;
	this.defaultFlow = "";
	
	this.loopCharacteristics;
	this.executionListeners;
	this.boundaryEvents;// not show
	this.customProperties;
};

Activity.prototype = new FlowNode();
Activity.prototype.constructor = Activity;

/**
  * Class: ActivityInfo
  */
function ActivityInfo(id)
{
	FlowNodeInfo.call(this, id);
	this.properties.push(new Property('asynchronous', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'asynchronous','asynchronousHints'));		
	this.properties.push(new Property('isForCompensation', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isForCompensation','isForCompensationHints'));	
		
	this.properties.push(new Property('executionListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/executionListeners.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'executionListeners','executionListenersHints'));
		
	this.properties.push(new Property('loopCharacteristics', 'Multi Instance', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/loopCharacteristics.html',features:'dialogHeight:300px;dialogWidth:400px;center:yes'},'loopCharacteristics','loopCharacteristicsHints'));
			
	this.properties.push(new Property('customProperties', 'Extends', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/customProperties.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'customProperties','customPropertiesHints'));		
		
};

ActivityInfo.prototype = new FlowNodeInfo();
ActivityInfo.prototype.constructor = ActivityInfo;
