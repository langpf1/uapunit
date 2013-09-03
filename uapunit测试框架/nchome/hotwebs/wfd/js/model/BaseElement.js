/**
 * Class: BaseElement
 */
function BaseElement(id)
{
	this.id = id;
	this.isContainer = false;
	this.documentation = "";
	this.canBound = false;
	this.label = "BaseElement";
};

BaseElement.prototype.constructor = BaseElement;

BaseElement.prototype.toXML = function (mxCodec) {
	var mxObjectCodec = mxCodecRegistry.getCodec(this.constructor);
	return mxObjectCodec.encode(mxCodec, this);
};

BaseElement.prototype.getId = function () {
	return this.id;
};

BaseElement.prototype.setId = function (id) {
	this.id = id;
};
/**
 * Class: BaseElementInfo
 */
function BaseElementInfo(id)
{
	this.properties = new Array();
	this.properties.push(new Property('id', 'General', true, 
		EditorType.text,id,'ID','idHints'));
	this.properties.push(new Property('name', 'General', true, 
		EditorType.text, '','name', 'nameHints'));
//	this.properties.push(new Property('isContainer', '', false, 
//		EditorType.checkbox, {on:true,off:false}));
//	this.properties.push(new Property('canBound', '', false, 
//		EditorType.checkbox, {on:true,off:false}));
		this.properties.push(new Property('documention', 'General', false, 
		EditorType.expbox, {url:'./dialog/PropertyDialog/documention.html',
			features:'dialogHeight:300px;dialogWidth:400px;center:yes'},'documention','documentHints'
			));
			
};
BaseElementInfo.prototype.constructor = BaseElementInfo;

BaseElementInfo.prototype = {
	get_properties : BaseElementInfo$get_properties
};

function BaseElementInfo$get_properties() {
    /// <value type="BaseElementInfo" locid="P:J#BaseElementInfo.properties"></value>
    return this.properties;
};
