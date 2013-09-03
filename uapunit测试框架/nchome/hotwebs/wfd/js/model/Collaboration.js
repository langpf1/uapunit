/**
 * Class: Collaboration
 */
function Collaboration(id)
{
	BaseElement.call(this, id);

};

Collaboration.prototype = new BaseElement();
Collaboration.prototype.constructor = Collaboration;



/**
  * Class: CollaborationInfo
  */
function CollaborationInfo(id)
{
	BaseElementInfo.call(this, id);
	this.properties.push(new Property('asynchronous', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'asynchronous','asynchronousHints'));		
	this.properties.push(new Property('isForCompensation', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isForCompensation','isForCompensationHints'));	
		
	this.properties.push(new Property('executionListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/executionListeners.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'executionListeners','executionListenersHints'));
		
	this.properties.push(new Property('loopCharacteristics', 'Multi Instance', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/loopCharacteristics.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'loopCharacteristics','loopCharacteristicsHints'));
			
	this.properties.push(new Property('customProperties', 'Extends', false, 
		EditorType.text, '','customProperties','customPropertiesHints'));			
		
};

CollaborationInfo.prototype = new BaseElementInfo();
CollaborationInfo.prototype.constructor = CollaborationInfo;

