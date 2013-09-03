/**
 * Class: SendTask
 */
function SendTask(id)
{
	Task.call(this, id);
	this.to = "";
	this.subject = "";
	this.text= "";
	this.html= "";
	this.from= "";
	this.cc = "";
    this.bcc= "";
    this.charset = "";
	this.name = "Send Task"
	this.label = "Send Task";
	this.infoClass = "SendTaskInfo";
};

SendTask.prototype = new Task();
SendTask.prototype.constructor = SendTask;

/**
 * Class: SendTaskInfo
 */
function SendTaskInfo(id)
{
	TaskInfo.call(this, id);

	this.properties.push(new Property('to', 'Main Config', false, 
		EditorType.text, '','to','toHints'));
	this.properties.push(new Property('subject', 'Main Config', false, 
		EditorType.text, '','subject','subjectHints'));
	this.properties.push(new Property('text', 'Main Config', false, 
		EditorType.text, '','',''));
	this.properties.push(new Property('html', 'Main Config', false, 
		EditorType.text, '','','s'));
	this.properties.push(new Property('from', 'Main Config', false, 
		EditorType.text, '','from','fromHints'));	
	this.properties.push(new Property('cc', 'Main Config', false, 
		EditorType.text, '','',''));
	this.properties.push(new Property('bcc', 'Main Config', false, 
		EditorType.text, '','',''));
	this.properties.push(new Property('charset', 'Main Config', false, 
		EditorType.text, '','charset','charsetHints'));	
};

SendTaskInfo.prototype = new TaskInfo();
SendTaskInfo.prototype.constructor = SendTaskInfo;
