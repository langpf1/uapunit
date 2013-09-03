/**
 * Class: MessageFlow
 */
function MessageFlow(id)
{
	Connector.call(this, id);
	this.name = "MessageFlow";
	this.messageRef = "";
	this.label = "MessageFlow";
	this.infoClass = "MessageFlowInfo";
};

MessageFlow.prototype = new Connector();
MessageFlow.prototype.constructor = MessageFlow;



/**
 * Class: MessageFlowInfo
 */
function MessageFlowInfo(id)
{
	FlowNodeInfo.call(this, id);
    this.properties.push(new Property('MessageRef', 'Main Config', false, 
		EditorType.text, '','messageRef','messageRefHints'));
};
MessageFlowInfo.prototype = new FlowNodeInfo();
MessageFlowInfo.prototype.constructor = MessageFlowInfo;
