/**
 * Class: EscalationBoundaryEvent
 */
function EscalationBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "EscalationBoundaryEventInfo";
};
 
EscalationBoundaryEvent.prototype = new CatchEvent();
EscalationBoundaryEvent.prototype.constructor = EscalationBoundaryEvent;


/**
 * Class: EscalationBoundaryEventInfo
 */
function EscalationBoundaryEventInfo(id)
{
	IntermediateCatchEscalationEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

EscalationBoundaryEventInfo.prototype = new IntermediateCatchEscalationEventInfo();
EscalationBoundaryEventInfo.prototype.constructor = EscalationBoundaryEventInfo;