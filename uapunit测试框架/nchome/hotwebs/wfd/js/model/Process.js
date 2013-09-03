/**
 * Class: Process
 */
function Process(id)
{
	FlowElementsContainer.call(this, id);
	this.name = "Process";
	this.processDefinitionPk = "";
	this.executable = true;
	this.processType = "Public";
	this.objectType = "";
	this.matchPolicy = "getUserCode()=\"initor\" && organization in(\"\",\"\")";
	this.group ;
	this.organization = null ;
	this.customProperties = null ;
	this.label = "Process";
	this.infoClass = "ProcessInfo";
};

Process.prototype = new FlowElementsContainer();
Process.prototype.constructor = Process;

/**
 * Class: ProcessInfo
 */
function ProcessInfo(id)
{
	FlowElementsContainerInfo.call(this, id);

	this.properties.push(new Property('executable', 'General', false, EditorType.checkbox, {on:true,off:false},'executable','executableHints'));
	this.properties.push(new Property('processType', 'General', false, EditorType.combobox, 
		{data:[{value:'None',text:mxResources.get('procTypeNone')},
			{value:'Public',text:mxResources.get('procTypePublic')}, 
			{value:'Private',text:mxResources.get('procTypePrivate')}]},
		'processType','processTypeHints'));
	this.properties.push(new Property('objectType', 'Main Config', false, EditorType.text, '','objectType','objectTypeHints'));
	this.properties.push(new Property('matchPolicy', 'Main Config', false, EditorType.refbox, 
		{url:'./dialog/expression/expression.html',features:'dialogHeight:600px;dialogWidth:800px;center:yes;status:no'},'matchPolicy','matchPolicyHints'));
	this.properties.push(new Property('customProperties', 'Extension', false, EditorType.refbox, 
		{url:'./dialog/PropertyDialog/customProperties.html',features:'dialogHeight:480px;dialogWidth:600px;center:yes'},'customProperties','customPropertiesHints'));
};

ProcessInfo.prototype = new FlowElementsContainerInfo();
ProcessInfo.prototype.constructor = ProcessInfo;
