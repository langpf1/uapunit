function DefaultParticipantDefinition(id)
{
	//this.id=""; zhailzh 因为在Swing版中参与者存储的量中没有此项
	this.participantID="";
	this.code="";
	this.name="";
	this.participantType=null;
	this.participantFilterType=null;
	this.properties = null;
};
DefaultParticipantDefinition.prototype.constructor = DefaultParticipantDefinition;
