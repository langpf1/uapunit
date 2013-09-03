/**
 * Class: FlowElementsContainer
 */
function FlowElementsContainer(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.flowElements = null;
	this.executionListeners = null;
	this.lanes = "";
	this.extensionElements = null;
	this.label = "name Listener";
};

FlowElementsContainer.prototype = new BaseElement();
FlowElementsContainer.prototype.constructor = FlowElementsContainer;

/**
 * Class: FlowElementsContainerInfo
 */
function FlowElementsContainerInfo(id)
{
	BaseElementInfo.call(this, id);
	this.properties.push(new Property('executionListeners','Listeners', false, EditorType.refbox, 
		{url:'./dialog/PropertyDialog/executionListeners.html',
			features:'dialogHeight:600px;dialogWidth:800px;center:yes;status:no'},'executionListeners','executionListenersHints'));
};

FlowElementsContainerInfo.prototype = new BaseElementInfo();
FlowElementsContainerInfo.prototype.constructor = FlowElementsContainerInfo;




