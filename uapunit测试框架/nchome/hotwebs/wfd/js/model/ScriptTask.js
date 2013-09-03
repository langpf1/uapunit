/**
 * Class: ScriptTask
 */
function ScriptTask(id)
{
	Task.call(this, id);
	this.name = "Script Task";
	this.scriptFormat = "javascript";
	this.script = "";
	this.resultVariable = "";	
	this.label = "Script Task";
	this.infoClass = "ScriptTaskInfo";
};

ScriptTask.prototype = new Task();
ScriptTask.prototype.constructor = ScriptTask;

/**
 * Class: ScriptTaskInfo
 */
function ScriptTaskInfo(id)
{
	TaskInfo.call(this, id);
	this.properties.push(new Property('scriptFormat', 'Main Config', false, 
		EditorType.combobox, {data:[{value:'javascript',text:'javascript'}, {value:'groovy',text:'groovy'}, {value:'juel',text:'juel'}]}
		,'scriptFormat','scriptFormatHints'));
	this.properties.push(new Property('script', 'Main Config', false, 
		  EditorType.expbox, {url:'./dialog/PropertyDialog/script.html',
			features:'dialogHeight:280px;dialogWidth:250px;center:yes'}
			,'script','scriptHints'));	
	this.properties.push(new Property('resultVariable', 'Main Config', false, 
		EditorType.text, '','resultVariable','resultVariableHints'));
};

ScriptTaskInfo.prototype = new TaskInfo();
ScriptTaskInfo.prototype.constructor = ScriptTaskInfo;
