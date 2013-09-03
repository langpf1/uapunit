/**
 * Class: SignalBoundaryEvent
 */
function SignalBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "SignalBoundaryEventInfo";
};
 
SignalBoundaryEvent.prototype = new CatchEvent();
SignalBoundaryEvent.prototype.constructor = SignalBoundaryEvent;


/**
 * Class: SignalBoundaryEventInfo
 */
function SignalBoundaryEventInfo(id)
{
	IntermediateCatchSignalEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

SignalBoundaryEventInfo.prototype = new IntermediateCatchSignalEventInfo();
SignalBoundaryEventInfo.prototype.constructor = SignalBoundaryEventInfo;