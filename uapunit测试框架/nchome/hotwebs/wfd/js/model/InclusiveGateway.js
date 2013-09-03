/**
 * Class: InclusiveGateway
 */
function InclusiveGateway(id)
{
	Gateway.call(this, id);
	this.name = "Inclusive Gateway";
	this.defaultSequenceFlow ;
    this.label = "InclusiveGateway";
    this.infoClass = "InclusiveGatewayInfo";
};

InclusiveGateway.prototype = new Gateway();
InclusiveGateway.prototype.constructor = InclusiveGateway;



/**
 * Class: InclusiveGatewayInfo
 */
function InclusiveGatewayInfo(id)
{
	GatewayInfo.call(this, id);
    this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.text, '','defaultSequenceFlow','defaultSequenceFlowHints'));
};

InclusiveGatewayInfo.prototype = new GatewayInfo();
InclusiveGatewayInfo.prototype.constructor = InclusiveGatewayInfo;