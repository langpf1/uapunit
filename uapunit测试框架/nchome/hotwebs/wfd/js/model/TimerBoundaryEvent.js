/**
 * Class: TimerBoundaryEvent
 */
function TimerBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "TimerBoundaryEventInfo";
};
 
TimerBoundaryEvent.prototype = new CatchEvent();
TimerBoundaryEvent.prototype.constructor = TimerBoundaryEvent;


/**
 * Class: TimerBoundaryEventInfo
 */
function TimerBoundaryEventInfo(id)
{
	IntermediateCatchTimerEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

TimerBoundaryEventInfo.prototype = new IntermediateCatchTimerEventInfo();
TimerBoundaryEventInfo.prototype.constructor = TimerBoundaryEventInfo;