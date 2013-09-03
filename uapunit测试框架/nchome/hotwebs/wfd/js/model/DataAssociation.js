/**
 * Class: DataAssociation
 */
function DataAssociation(id)
{
	BaseElement.call(this, id);
	this.source = "";
	this.sourceExpression = "";
	this.target = "";
	this.targetExpression = "";
	this.label = "Data Association";
	this.infoClass = "DataAssociationInfo";
};

DataAssociation.prototype = new BaseElement();
DataAssociation.prototype.constructor = DataAssociation;

/**
 * Class: DataAssociationInfo
 */
function DataAssociationInfo(id)
{
	BaseElementInfo.call(this, id);
};

DataAssociationInfo.prototype = new BaseElementInfo();
DataAssociationInfo.prototype.constructor = DataAssociationInfo;
