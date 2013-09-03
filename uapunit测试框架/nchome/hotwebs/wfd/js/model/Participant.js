function Participant(id)
{
	BaseElement.call(this, id);
	this.name = "Participant";
	this.processRef = "";
	this.participanTable =null;
	this.label = "Participant";
};

Participant.prototype = new BaseElement();
Participant.prototype.constructor = Participant;

/**
 * Class:ParticipantInfo
 */
function ParticipantInfo(id)
{
	BaseElementInfo.call(this, id);
};

ParticipantInfo.prototype = new BaseElementInfo();
ParticipantInfo.prototype.constructor = ParticipantInfo;