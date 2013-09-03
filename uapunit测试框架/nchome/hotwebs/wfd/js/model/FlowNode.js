/**
 * Class: FlowNode
 */
function FlowNode(id)
{
	FlowElement.call(this, id);
};

FlowNode.prototype = new FlowElement();
FlowNode.prototype.constructor = FlowNode;

/**
 * Class: FlowNodeInfo
 */
function FlowNodeInfo(id)
{
	FlowElementInfo.call(this, id);
};

FlowNodeInfo.prototype = new FlowElementInfo();
FlowNodeInfo.prototype.constructor = FlowNodeInfo;