function IntermediateThrowLinkEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowLinkEvent";
	this.name = "ThrowLinkEvent";
	this.infoClass = "IntermediateThrowLinkEventInfo";
};

IntermediateThrowLinkEvent.prototype = new ThrowEvent();
IntermediateThrowLinkEvent.prototype.constructor = IntermediateThrowLinkEvent;



function IntermediateThrowLinkEventInfo(id)
{
	LinkEventDefinitionInfo.call(this, id);
};

IntermediateThrowLinkEventInfo.prototype = new LinkEventDefinitionInfo();
IntermediateThrowLinkEventInfo.prototype.constructor = IntermediateThrowLinkEventInfo;