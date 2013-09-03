/**
 * Class: Lane
 */
function Lane(id)
{
	Artifact.call(this, id);
	this.name = "Participant";
	//public Process parentProcess;
	this.flowReferences = [];
	this.label = "Participant";
	this.infoClass = "LaneInfo";
};
Lane.prototype = new Artifact();
Lane.prototype.constructor = Lane;


/**
 * Class: LaneInfo
 */
function LaneInfo(id)
{
	BaseElementInfo.call(this, id);
};

LaneInfo.prototype = new BaseElementInfo();
LaneInfo.prototype.constructor = LaneInfo;
