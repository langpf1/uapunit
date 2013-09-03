/**
 * Class: FlowElement
 */
function FlowElement(id)
{
	BaseElement.call(this, id);
	this.name = "";
};

FlowElement.prototype = new BaseElement();
FlowElement.prototype.constructor = FlowElement;

/**
 * Class: FlowElementInfo
 */
function FlowElementInfo(id)
{
	BaseElementInfo.call(this, id);
	
};

FlowElementInfo.prototype = new BaseElementInfo();
FlowElementInfo.prototype.constructor = FlowElementInfo;
