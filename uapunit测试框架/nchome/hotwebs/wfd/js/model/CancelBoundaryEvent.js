function CancelBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "CancelBoundaryEventInfo";
};
 
CancelBoundaryEvent.prototype = new CatchEvent();
CancelBoundaryEvent.prototype.constructor = CancelBoundaryEvent;


/**
 * Class: CancelBoundaryEventInfo
 */
function CancelBoundaryEventInfo(id)
{
	IntermediateCatchCancelEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

CancelBoundaryEventInfo.prototype = new IntermediateCatchCancelEventInfo();
CancelBoundaryEventInfo.prototype.constructor = CancelBoundaryEventInfo;
