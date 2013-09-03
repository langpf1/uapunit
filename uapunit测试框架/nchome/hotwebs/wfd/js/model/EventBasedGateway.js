/**
 * Class: EventBasedGateway
 */
function EventBasedGateway(id)
{
	Gateway.call(this, id);
	this.name = "EventBasedGateway";
	this.instantiate = false;
	this.eventGatewayType;
	this.label = "EventBasedGateway";
	this.infoClass = "EventBasedGatewayInfo";
};

EventBasedGateway.prototype = new Gateway();
EventBasedGateway.prototype.constructor = EventBasedGateway;



/**
 * Class: EventBasedGatewayInfo
 */
function EventBasedGatewayInfo(id)
{
	GatewayInfo.call(this, id);
    this.properties.push(new Property('instantiate', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'instantiate','instantiateHints'));
	this.properties.push(new Property('eventGatewayType', 'Main Config', false, 
		EditorType.combobox, {data:[{value:'Exclusive',text:mxResources.get('Exclusive')}, {value:'Parallel',text:mxResources.get('Parallel')}]},'eventGatewayType','eventGatewayTypeHints'));
};

EventBasedGatewayInfo.prototype = new GatewayInfo();
EventBasedGatewayInfo.prototype.constructor = EventBasedGatewayInfo;
