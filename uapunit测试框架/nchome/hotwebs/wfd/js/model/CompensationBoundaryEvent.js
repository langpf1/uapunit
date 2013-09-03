/**
 * Class: CompensationBoundaryEvent
 */
function CompensationBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "CompensationBoundaryEventInfo";
};
 
CompensationBoundaryEvent.prototype = new CatchEvent();
CompensationBoundaryEvent.prototype.constructor = CompensationBoundaryEvent;


/**
 * Class: CompensationBoundaryEventInfo
 */
function CompensationBoundaryEventInfo(id)
{
	IntermediateCatchCompensationEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

CompensationBoundaryEventInfo.prototype = new IntermediateCatchCompensationEventInfo();
CompensationBoundaryEventInfo.prototype.constructor = CompensationBoundaryEventInfo;
