/**
 * Class: ServiceTask
 */
function ServiceTask(id)
{
	Task.call(this, id);
	this.name ="Service Task";
	this.implementation = "";
	this.extendClass = "";
	this.method = "";
	this.operationRef = "";
	this.fieldExtensions;// later modify
	this.resultVariableName = "";
	this.label = "Service Task";
	this.infoClass = "ServiceTaskInfo";
};

ServiceTask.prototype = new Task();
ServiceTask.prototype.constructor = ServiceTask;

/**
 * Class: ServiceTaskInfo
 */
function ServiceTaskInfo(id)
{
	TaskInfo.call(this, id);
	this.properties.push(new Property('implementation', 'Main Config', false, 
		EditorType.combobox, {data:[{value:'Standard',text:mxResources.get('Standard')}, {value:'WebService',text:mxResources.get('WebService')}, {value:'Expression',text:mxResources.get('Expression')}, {value:'DelegateExpression',text:mxResources.get('DelegateExpression')}, {value:'CallMethod',text:mxResources.get('CallMethod')}, {value:'GenerateBill',text:mxResources.get('GenerateBill')}, {value:'WorkflowGadget',text:mxResources.get('WorkflowGadget')}]}
		,'implementation','implementationHints'));
	this.properties.push(new Property('extendClass', 'Main Config', false, 
		getEditorType("extendClass"), getOptions("extendClass"),'extendClass','extendClassHints'));
	this.properties.push(new Property('method', 'Main Config', false, 
		EditorType.text, '','method','methodHints'));
	this.properties.push(new Property('operationRef', 'Main Config', false, 
		EditorType.text, '','operationRef','operationRefHints'));
	this.properties.push(new Property('fieldExtensions', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/fieldExtensions.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'fieldExtensions','fieldExtensionsHints'));
	this.properties.push(new Property('resultVariableName', 'Main Config', false, 
		EditorType.text, '','resultVariableName','resultVariableNameHints'));
	function getEditorType(name){
		if(name === "extendClass" && mxClient.IS_SWTBROWSER){
			return EditorType.refbox;
		}
		return EditorType.text;
	};

	function getOptions(name){
		if(name === "extendClass" && mxClient.IS_SWTBROWSER){
			return {id:'servicetask.extendClass'};
		}
		return "";
	};
};

ServiceTaskInfo.prototype = new TaskInfo();
ServiceTaskInfo.prototype.constructor = ServiceTaskInfo;
