/**
 * Class: EventGateway
 */
function EventGateway(id)
{
	Gateway.call(this, id);
	this.instantiate ;
	this.eventGatewayType = "";
	this.label = "EventGateway";
};

EventGateway.prototype = new Gateway();
EventGateway.prototype.constructor = EventGateway;


/**
 * Class: EventGatewayInfo
 */
function EventGatewayInfo(id)
{
	GatewayInfo.call(this, id);
};

EventGatewayInfo.prototype = new GatewayInfo();
EventGatewayInfo.prototype.constructor = EventGatewayInfo;
