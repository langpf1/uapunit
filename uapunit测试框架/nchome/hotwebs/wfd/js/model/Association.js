/**
 * Class: Association
 */
function Association(id)
{
	Connector.call(this, id);
	this.name = "Association";
	this.label = "Association";
	this.infoClass = "AssociationInfo";
};

Association.prototype = new Connector();
Association.prototype.constructor = Association;


/**
 * Class: AssociationInfo
 */
function AssociationInfo(id)
{
	FlowNodeInfo.call(this, id);
};

AssociationInfo.prototype = new FlowNodeInfo();
AssociationInfo.prototype.constructor = AssociationInfo;