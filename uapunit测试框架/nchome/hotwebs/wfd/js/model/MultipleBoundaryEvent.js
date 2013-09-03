/**
 * Class: MultipleBoundaryEvent
 */
function MultipleBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "MultipleBoundaryEventInfo";
};
 
MultipleBoundaryEvent.prototype = new CatchEvent();
MultipleBoundaryEvent.prototype.constructor = MultipleBoundaryEvent;


/**
 * Class: MultipleBoundaryEventInfo
 */
function MultipleBoundaryEventInfo(id)
{
	IntermediateCatchMultipleEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

MultipleBoundaryEventInfo.prototype = new IntermediateCatchMultipleEventInfo();
MultipleBoundaryEventInfo.prototype.constructor = MultipleBoundaryEventInfo;