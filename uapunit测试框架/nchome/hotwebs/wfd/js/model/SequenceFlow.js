/**
 * Class: SequenceFlow
 */
function SequenceFlow(id)
{
	Connector.call(this, id);
	this.name = "SequenceFlow";
	this.conditionExpression ;
	this.defaultSequenceFlow = false;
	this.immediate = false;
	this.method = "";
	this.executionListeners = null;
	this.label = "SequenceFlow";
	this.infoClass = "SequenceFlowInfo";
};

SequenceFlow.prototype = new Connector();
SequenceFlow.prototype.constructor = SequenceFlow;



/**
 * Class: SequenceFlowInfo
 */
function SequenceFlowInfo(id)
{
	 FlowNodeInfo.call(this, id);
	 this.properties.push(new Property('conditionExpression', 'Main Config', false, 
        EditorType.refbox, {url:'./dialog/expression/expression.html',features:'dialogHeight:600px;dialogWidth:800px;center:yes'},'conditionExpression','conditionExpressionHints'));
	 this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'defaultSequenceFlow','defaultSequenceFlowHints'));
	 this.properties.push(new Property('immediate', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'immediate','immediateHints'));
};

SequenceFlowInfo.prototype = new  FlowNodeInfo();
SequenceFlowInfo.prototype.constructor = SequenceFlowInfo;

