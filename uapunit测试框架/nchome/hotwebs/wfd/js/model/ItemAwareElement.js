/**
 * Class: ItemAwareElement
 */
function ItemAwareElement(id)
{
	BaseElement.call(this, id);
	this.itemSubjectRef = [];
	this.dataState = "";
	this.label = "ItemAwareElement";
};

ItemAwareElement.prototype = new BaseElement();
ItemAwareElement.prototype.constructor = ItemAwareElement;


/**
 * Class: ItemAwareElementInfo
 */
function ItemAwareElementInfo(id)
{
	BaseElementInfo.call(this, id);
};

ItemAwareElementInfo.prototype = new BaseElementInfo();
ItemAwareElementInfo.prototype.constructor = ItemAwareElementInfo;
