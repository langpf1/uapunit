/**
 * Class: MessageBoundaryEvent
 */
function MessageBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "MessageBoundaryEventInfo";
};
 
MessageBoundaryEvent.prototype = new CatchEvent();
MessageBoundaryEvent.prototype.constructor = MessageBoundaryEvent;


/**
 * Class: MessageBoundaryEventInfo
 */
function MessageBoundaryEventInfo(id)
{
	IntermediateCatchMessageEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

MessageBoundaryEventInfo.prototype = new IntermediateCatchMessageEventInfo();
MessageBoundaryEventInfo.prototype.constructor = MessageBoundaryEventInfo;