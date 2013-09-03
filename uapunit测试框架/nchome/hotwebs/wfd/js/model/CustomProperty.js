/**
 * Class: CustomProperty
 */
function CustomProperty(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.simpleValue = "";
	this.label = "Custom Property";
};

CustomProperty.prototype = new BaseElement();
CustomProperty.prototype.constructor = CustomProperty;


/**
 * Class: CustomPropertyInfo
 */
function CustomPropertyInfo(id)
{
	BaseElementInfo.call(this, id);

};

CustomPropertyInfo.prototype = new BaseElementInfo();
CustomPropertyInfo.prototype.constructor = CustomPropertyInfo;
