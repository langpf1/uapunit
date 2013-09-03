/**
 * Class: BusinessRuleTask
 */
function BusinessRuleTask(id)
{
	Task.call(this, id);
	this.name = "BusinessRule Task";
	this.resultVariableName = "";
	this.exclude ="";
	this.ruleNames ="";
	this.inputVariables ="";
	this.label = "BusinessRule Task";
	this.infoClass = "BusinessRuleTaskInfo";
};

BusinessRuleTask.prototype = new Task();
BusinessRuleTask.prototype.constructor = BusinessRuleTask;


 /**
 * Class: BusinessRuleTaskInfo
 */
function BusinessRuleTaskInfo(id)
{
	TaskInfo.call(this, id);
    this.properties.push(new Property('resultVariableName', 'Main Config', false, 
		EditorType.text, 'rulesOutput','resultVariableName','resultVariableNameHints'));
	this.properties.push(new Property('exclude', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'exclude','excludeHints'));	
	this.properties.push(new Property('ruleNames', 'Main Config', false, 
		EditorType.text, '','ruleNames','ruleNamesHints'));
	this.properties.push(new Property('inputVariables', 'Main Config', false, 
		EditorType.text, '','inputVariables','inputVariablesHints'));
};

BusinessRuleTaskInfo.prototype = new TaskInfo();
BusinessRuleTaskInfo.prototype.constructor = BusinessRuleTaskInfo;
