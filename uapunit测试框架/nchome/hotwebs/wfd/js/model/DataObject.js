/**
 * Class: DataObject
 */
function DataObject(id)
{
	FlowElement.call(this, id);
	this.name = "DataObject";
	this.isCollection = false;
	this.label="DataObject";
	this.infoClass = "DataObjectInfo";
};

DataObject.prototype = new FlowElement();
DataObject.prototype.constructor = DataObject;


/**
 * Class: DataObjectInfo
 */
function DataObjectInfo(id)
{
	FlowElementInfo.call(this, id);
	this.properties.push(new Property('isCollection', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isCollection','isCollectionHints'));
};

DataObjectInfo.prototype = new FlowElementInfo();
DataObjectInfo.prototype.constructor = DataObjectInfo;
