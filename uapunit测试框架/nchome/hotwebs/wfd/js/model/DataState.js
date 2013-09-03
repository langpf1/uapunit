/**
 * Class: DataState
 */
function DataState(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "Data State"
};

DataState.prototype = new BaseElement();
DataState.prototype.constructor = DataState;



/**
 * Class: DataStateInfo
 */
function DataStateInfo(id)
{
	BaseElementInfo.call(this, id);
};

DataStateInfo.prototype = new BaseElementInfo();
DataStateInfo.prototype.constructor = DataStateInfo;