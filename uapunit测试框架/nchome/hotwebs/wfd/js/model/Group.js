/**
 * Class: Group
 */
function Group(id)
{
	Artifact.call(this, id);
	this.name = "Group";
	this.label = "Group"; 
	this.infoClass = "GroupInfo";
};

Group.prototype = new Artifact();
Group.prototype.constructor = Group;


/**
 * Class: GroupInfo
 */
function GroupInfo(id)
{
	FlowElementInfo.call(this, id);
};

GroupInfo.prototype = new FlowElementInfo();
GroupInfo.prototype.constructor = GroupInfo;
