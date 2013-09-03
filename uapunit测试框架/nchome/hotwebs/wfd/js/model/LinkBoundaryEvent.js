/**
 * Class: LinkBoundaryEvent
 */
function LinkBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "LinkBoundaryEventInfo";
};
 
LinkBoundaryEvent.prototype = new CatchEvent();
LinkBoundaryEvent.prototype.constructor = LinkBoundaryEvent;


/**
 * Class: LinkBoundaryEventInfo
 */
function LinkBoundaryEventInfo(id)
{
	IntermediateCatchLinkEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

LinkBoundaryEventInfo.prototype = new IntermediateCatchLinkEventInfo();
LinkBoundaryEventInfo.prototype.constructor = LinkBoundaryEventInfo;