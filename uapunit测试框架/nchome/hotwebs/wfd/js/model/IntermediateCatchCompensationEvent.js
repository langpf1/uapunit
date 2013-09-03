function IntermediateCatchCompensationEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchCompensationEvent";
	this.name = "CatchCompensationEvent";
	this.infoClass = "IntermediateCatchCompensationEventInfo";
};

IntermediateCatchCompensationEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchCompensationEvent.prototype.constructor = IntermediateCatchCompensationEvent;



function IntermediateCatchCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
	
};

IntermediateCatchCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
IntermediateCatchCompensationEventInfo.prototype.constructor = IntermediateCatchCompensationEventInfo;