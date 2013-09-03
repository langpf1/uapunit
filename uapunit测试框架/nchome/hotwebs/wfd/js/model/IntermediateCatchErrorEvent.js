function IntermediateCatchErrorEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchErrorEvent";
	this.name = "CatchErrorEvent";
	this.infoClass = "IntermediateCatchErrorEventInfo";
};

IntermediateCatchErrorEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchErrorEvent.prototype.constructor = IntermediateCatchErrorEvent;



function IntermediateCatchErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
};

IntermediateCatchErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
IntermediateCatchErrorEventInfo.prototype.constructor = IntermediateCatchErrorEventInfo;