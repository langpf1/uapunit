/**
 * Class: MultiParallelBoundaryEvent
 */
function MultiParallelBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "MultiParallelBoundaryEventInfo";
};
 
MultiParallelBoundaryEvent.prototype = new CatchEvent();
MultiParallelBoundaryEvent.prototype.constructor = MultiParallelBoundaryEvent;


/**
 * Class: MultiParallelBoundaryEventInfo
 */
function MultiParallelBoundaryEventInfo(id)
{
	IntermediateCatchMultiParallelEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

MultiParallelBoundaryEventInfo.prototype = new IntermediateCatchMultiParallelEventInfo();
MultiParallelBoundaryEventInfo.prototype.constructor = MultiParallelBoundaryEventInfo;