/**
 * Class: ExclusiveGateway
 */
function ExclusiveGateway(id)
{
	Gateway.call(this, id);
	this.name = "Exclusive Gateway";
	this.defaultSequenceFlow ;
	this.label = "ExclusiveGateway";
	this.infoClass = "ExclusiveGatewayInfo";
};

ExclusiveGateway.prototype = new Gateway();
ExclusiveGateway.prototype.constructor = ExclusiveGateway;


/**
 * Class: ExclusiveGatewayInfo
 */
function ExclusiveGatewayInfo(id)
{
	GatewayInfo.call(this, id);
	this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.text, '','defaultSequenceFlow','defaultSequenceFlowHints'));
};

ExclusiveGatewayInfo.prototype = new GatewayInfo();
ExclusiveGatewayInfo.prototype.constructor = ExclusiveGatewayInfo;

