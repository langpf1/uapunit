/**
 * Class: CompensationBoundaryEvent
 */
function ConditionalBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "ConditionalBoundaryEventInfo";
};
 
ConditionalBoundaryEvent.prototype = new CatchEvent();
ConditionalBoundaryEvent.prototype.constructor = ConditionalBoundaryEvent;


/**
 * Class: ConditionalBoundaryEventInfo
 */
function ConditionalBoundaryEventInfo(id)
{
	IntermediateCatchConditionalEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

ConditionalBoundaryEventInfo.prototype = new IntermediateCatchConditionalEventInfo();
CompensationBoundaryEventInfo.prototype.constructor = ConditionalBoundaryEventInfo;