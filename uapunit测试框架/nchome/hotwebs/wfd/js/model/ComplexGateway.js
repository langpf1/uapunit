/**
 * Class: ComplexGateway
 */
function ComplexGateway(id)
{
	Gateway.call(this, id);
	this.name = "Complex Gateway"
	this.defaultSequenceFlow = "";
	this.activationCondition = "";
	this.label = "ComplexGateway";
	this.infoClass = "ComplexGatewayInfo";
};

ComplexGateway.prototype = new Gateway();
ComplexGateway.prototype.constructor = ComplexGateway;

/**
 * Class: ComplexGatewayInfo
 */
function ComplexGatewayInfo(id)
{
	GatewayInfo.call(this, id);
	this.properties.push(new Property('activationCondition', 'Main Config', false, 
		EditorType.text, '','activationCondition','activationConditionHints'));
};

ComplexGatewayInfo.prototype = new GatewayInfo();
ComplexGatewayInfo.prototype.constructor = ComplexGatewayInfo;

