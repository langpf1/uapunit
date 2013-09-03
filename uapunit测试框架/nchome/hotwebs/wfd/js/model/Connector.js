/**
 * Class: Connector
 */
function Connector(id)
{
	FlowElement.call(this, id);
	//this.source = null;
	this.sourceRef = "";
	//this.target = null;
	this.targetRef = "";
	this.label = "Connector";
};

Connector.prototype = new FlowElement();
Connector.prototype.constructor = Connector;

/**
 * Class: ConnectorInfo
 */
function ConnectorInfo(id)
{
	FlowElementInfo.call(this, id);

};

ConnectorInfo.prototype = new FlowElementInfo();
ConnectorInfo.prototype.constructor = ConnectorInfo;
