/**
 * Class: ParallelGateway
 */
function ParallelGateway(id)
{
	Gateway.call(this, id);
	this.name = "Parallel Gateway";
	this.label = "ParallelGateway";
	this.infoClass = "ParallelGatewayInfo";
	
};

ParallelGateway.prototype = new Gateway();
ParallelGateway.prototype.constructor = ParallelGateway;



/**
 * Class: ParallelGatewayInfo
 */
function ParallelGatewayInfo(id)
{
	GatewayInfo.call(this, id);
};

ParallelGatewayInfo.prototype = new GatewayInfo();
ParallelGatewayInfo.prototype.constructor = ParallelGatewayInfo;

