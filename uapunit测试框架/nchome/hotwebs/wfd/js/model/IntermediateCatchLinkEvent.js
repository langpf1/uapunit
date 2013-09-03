function IntermediateCatchLinkEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchLinkEvent";
	this.name = "CatchLinkEvent";
	this.infoClass = "IntermediateCatchLinkEventInfo";
};

IntermediateCatchLinkEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchLinkEvent.prototype.constructor = IntermediateCatchLinkEvent;




function IntermediateCatchLinkEventInfo(id)
{
	LinkEventDefinitionInfo.call(this, id);
};

IntermediateCatchLinkEventInfo.prototype = new LinkEventDefinitionInfo();
IntermediateCatchLinkEventInfo.prototype.constructor = IntermediateCatchLinkEventInfo;