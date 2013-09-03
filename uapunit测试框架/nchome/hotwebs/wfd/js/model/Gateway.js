/**
 * Class: Gateway
 */
function Gateway(id)
{
	FlowNode.call(this, id);
	this.defaultFlow = "";
	this.gatewayType ;
	this.gatewayDirection = null;
	this.label = "Gateway";
};

Gateway.prototype = new FlowNode();
Gateway.prototype.constructor = Gateway;


/**
 * Class: GatewayInfo
 */
function GatewayInfo(id)
{
	FlowNodeInfo.call(this, id);
    this.properties.push(new Property('gatewayDirection', 'General', false, 
		EditorType.combobox, {data:[{value:'Unspecified',text:mxResources.get('Unspecified')}, {value:'Converging',text:mxResources.get('Converging')}, {value:'Diverging',text:mxResources.get('Diverging')},{value:'Mixed',text:mxResources.get('Mixed')}]},'gatewayDirection','gatewayDirectionHints'));
};

GatewayInfo.prototype = new FlowNodeInfo();
GatewayInfo.prototype.constructor = GatewayInfo;

