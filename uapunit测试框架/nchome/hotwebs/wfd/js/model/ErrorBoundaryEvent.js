/**
 * Class: CompensationBoundaryEvent
 */
function ErrorBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "ErrorBoundaryEventInfo";
};
 
ErrorBoundaryEvent.prototype = new CatchEvent();
ErrorBoundaryEvent.prototype.constructor = ErrorBoundaryEvent;


/**
 * Class: ErrorBoundaryEventInfo
 */
function ErrorBoundaryEventInfo(id)
{
	IntermediateCatchErrorEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

ErrorBoundaryEventInfo.prototype = new IntermediateCatchErrorEventInfo();
ErrorBoundaryEventInfo.prototype.constructor = ErrorBoundaryEventInfo;