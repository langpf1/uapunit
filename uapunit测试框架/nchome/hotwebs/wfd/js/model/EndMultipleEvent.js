function EndMultipleEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndMultipleEvent";
	this.name = "EndMultipleEvent";
	this.infoClass = "EndMultipleEventInfo";
};

EndMultipleEvent.prototype = new ThrowEvent();
EndMultipleEvent.prototype.constructor = EndMultipleEvent;


function EndMultipleEventInfo(id)
{
	EventInfo.call(this, id);
};

EndMultipleEventInfo.prototype = new EventInfo();
EndMultipleEventInfo.prototype.constructor = EndMultipleEventInfo;