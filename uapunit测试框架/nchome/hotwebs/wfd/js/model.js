/**
 * Class: Property
 */
function Property(name, group, isReadOnly, editorType, editorOptions, displayName, hints)
{
	this.name = name;
	this.displayName = displayName;
	this.group = group;
	this.isReadOnly = isReadOnly;
	this.editorType = editorType;
	this.editorOptions = editorOptions;
	this.hints = hints;
};

Property.prototype.constructor = Property;

Property.prototype = {
	get_name : Property$get_name,
	get_displayName : Property$get_displayName,
 	get_group : Property$get_group,
 	get_isReadOnly : Property$get_isReadOnly,
 	get_editorType : Property$get_editorType,
 	get_editorOptions : Property$get_editorOptions,
 	get_hints : Property$get_hints
};

function Property$get_name() {
    /// <value type="Property" locid="P:J#Property.name"></value>
    return this.name;
};

function Property$get_displayName() {
    /// <value type="Property" locid="P:J#Property.name"></value>
    return this.displayName;
};

function Property$get_group() {
    /// <value type="Property" locid="P:J#Property.group"></value>
    return this.group;
};

function Property$get_isReadOnly() {
    /// <value type="Property" locid="P:J#Property.isReadOnly"></value>
    return this.isReadOnly;
};

function Property$get_editorType() {
    /// <value type="Property" locid="P:J#Property.editorType"></value>
    return this.editorType;
};

function Property$get_editorOptions() {
    /// <value type="Property" locid="P:J#Property.editorOptions"></value>
    return this.editorOptions;
};

function Property$get_hints() {
    /// <value type="Property" locid="P:J#Property.hints"></value>
    return this.hints;
};

$.extend($.fn.datagrid.defaults.editors, { 
	refbox: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var returnValue = showModalDialog(options.url, input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(JSON.stringify(returnValue));
				}
			});
			return span; 
		}, 
		getValue: function(target){
			var value = $(target)[0].children[0].value;
			if(value != undefined && value!=""){
				return JSON.parse(value);
			}
			return value; 
		}, 
		setValue: function(target, value){
			if(value != undefined && value!=""){
				$(target)[0].children[0].value = JSON.stringify(value);
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});
$.extend($.fn.datagrid.defaults.editors, { 
	refcolumnbox: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var returnValue = showModalDialog(options.url, input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(JSON.stringify(returnValue));
				}
			});
			return span; 
		}, 
		getValue: function(target){
			var value = $(target)[0].children[0].value;
			if(value != undefined && value!=""){
				return JSON.parse(value);
			}
			return value; 
		}, 
		setValue: function(target, value){
			if(value != undefined && value!=""){
				$(target)[0].children[0].value = JSON.stringify(value);
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

$.extend($.fn.datagrid.defaults.editors, { 
	refcolumn: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var selectkind = new Object();
				selectkind.name=options.kind;
				var returnValue = showModalDialog(options.url,selectkind,input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(returnValue.name);
					var gridid = options.gridid;
					var selectRow = $('#'+gridid).datagrid('getSelected');
					var selectIndex = $('#'+gridid).datagrid('getRowIndex',selectRow);
					
					var keyEditor = $('#'+gridid).datagrid('getEditor',{index:selectIndex,field:options.keycolumn});
					if(keyEditor != null && keyEditor != undefined)
						keyEditor.target.val(returnValue.id);
						
					var codeEditor = $('#'+gridid).datagrid('getEditor',{index:selectIndex,field:options.codecolumn});
					if(codeEditor != null && codeEditor != undefined)
						codeEditor.target.val(returnValue.code);
				}
			});
			return span; 
		}, 
		getValue: function(target){ 
			return $(target)[0].children[0].value; 
		}, 
		setValue: function(target, value){
			if(value != undefined)
			{
				$(target)[0].children[0].value = value;
			} 
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

$.extend($.fn.datagrid.defaults.editors, { 
	expbox: { 
		init: function(container, options){ 
			var span = $('<span class="ref" style="width: 105px;"></span>').appendTo(container); 
			var input = $('<input class="ref-text" style="width: 85px;" autocomplete="off" value=""/>').appendTo(span); 
			var innerspan = $('<span class="reficon"/>').appendTo(span); 
			innerspan.hover(function(){
				$(this).addClass("reficon-hover");
			},function(){
				$(this).removeClass("reficon-hover");
			}).click(function(){
				var returnValue = showModalDialog(options.url, input.val(), options.features);
				if(returnValue!=undefined)
				{
					input.val(returnValue);
				}
			});
			return span; 
		}, 
		getValue: function(target){
			return $(target)[0].children[0].value;
		}, 
		setValue: function(target, value){
			if(value != undefined){
				$(target)[0].children[0].value = value;
			}
		}, 
		resize: function(target, width){ 
			var input = $(target); 
			input.width(width);
			$($(target)[0].children[0]).width(width-15); 
		}
	} 
});

function EditorType(){};
EditorType.prototype.constructor = EditorType;
EditorType.text = "text";
EditorType.textarea = "textarea";
EditorType.numberbox = "numberbox";
EditorType.datebox = "datebox";
EditorType.validatebox = "validatebox";
EditorType.combobox = "combobox";
EditorType.combotree = "combotree";
EditorType.checkbox = "checkbox";
EditorType.refbox = "refbox";
EditorType.refcolumn = "refcolumn";
EditorType.expbox = "expbox";
EditorType.refcolumnbox = "refcolumnbox";

/**
 * Class: BaseElement
 */
function BaseElement(id)
{
	this.id = id;
	this.isContainer = false;
	this.documentation = "";
	this.canBound = false;
	this.label = "BaseElement";
};

BaseElement.prototype.constructor = BaseElement;

BaseElement.prototype.toXML = function (mxCodec) {
	var mxObjectCodec = mxCodecRegistry.getCodec(this.constructor);
	return mxObjectCodec.encode(mxCodec, this);
};

BaseElement.prototype.getId = function () {
	return this.id;
};

BaseElement.prototype.setId = function (id) {
	this.id = id;
};
/**
 * Class: BaseElementInfo
 */
function BaseElementInfo(id)
{
	this.properties = new Array();
	this.properties.push(new Property('id', 'General', true, 
		EditorType.text,id,'ID','idHints'));
	this.properties.push(new Property('name', 'General', true, 
		EditorType.text, '','name', 'nameHints'));
//	this.properties.push(new Property('isContainer', '', false, 
//		EditorType.checkbox, {on:true,off:false}));
//	this.properties.push(new Property('canBound', '', false, 
//		EditorType.checkbox, {on:true,off:false}));
		this.properties.push(new Property('documention', 'General', false, 
		EditorType.expbox, {url:'./dialog/PropertyDialog/documention.html',
			features:'dialogHeight:300px;dialogWidth:400px;center:yes'},'documention','documentHints'
			));
			
};
BaseElementInfo.prototype.constructor = BaseElementInfo;

BaseElementInfo.prototype = {
	get_properties : BaseElementInfo$get_properties
};

function BaseElementInfo$get_properties() {
    /// <value type="BaseElementInfo" locid="P:J#BaseElementInfo.properties"></value>
    return this.properties;
};
function Artifact(id)
{
	BaseElement.call(this, id);
	this.name = "";
};

Artifact.prototype = new BaseElement();
Artifact.prototype.constructor = Artifact;


/**
  * Class: ArtifactInfo
  */
function ArtifactInfo(id)
{
	BaseElementInfo.call(this, id);
			
};

ArtifactInfo.prototype = new BaseElementInfo();
ArtifactInfo.prototype.constructor = ArtifactInfo;

/**
 * Class: Annotation
 */
function Annotation(id)
{ 
	Artifact.call(this, id);
	this.name = "Annotation";
	this.text="";
	this.label="Annotation";
	this.infoClass = "AnnotationInfo";
};

Annotation.prototype = new Artifact();
Annotation.prototype.constructor = Annotation;


/**
  * Class: AnnotationInfo
  */
function AnnotationInfo(id)
{
	FlowElementInfo.call(this, id);					
	this.properties.push(new Property('text', 'Main Config', false, 
		EditorType.text, '','text','textHints'));
};

AnnotationInfo.prototype = new FlowElementInfo();
AnnotationInfo.prototype.constructor = AnnotationInfo;/**
 * Class: Group
 */
function Group(id)
{
	Artifact.call(this, id);
	this.name = "Group";
	this.label = "Group"; 
	this.infoClass = "GroupInfo";
};

Group.prototype = new Artifact();
Group.prototype.constructor = Group;


/**
 * Class: GroupInfo
 */
function GroupInfo(id)
{
	FlowElementInfo.call(this, id);
};

GroupInfo.prototype = new FlowElementInfo();
GroupInfo.prototype.constructor = GroupInfo;
/**
 * Class: Lane
 */
function Lane(id)
{
	Artifact.call(this, id);
	this.name = "Participant";
	//public Process parentProcess;
	this.flowReferences = [];
	this.label = "Participant";
	this.infoClass = "LaneInfo";
};
Lane.prototype = new Artifact();
Lane.prototype.constructor = Lane;


/**
 * Class: LaneInfo
 */
function LaneInfo(id)
{
	BaseElementInfo.call(this, id);
};

LaneInfo.prototype = new BaseElementInfo();
LaneInfo.prototype.constructor = LaneInfo;
/**
 * Class: Collaboration
 */
function Collaboration(id)
{
	BaseElement.call(this, id);

};

Collaboration.prototype = new BaseElement();
Collaboration.prototype.constructor = Collaboration;



/**
  * Class: CollaborationInfo
  */
function CollaborationInfo(id)
{
	BaseElementInfo.call(this, id);
	this.properties.push(new Property('asynchronous', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'asynchronous','asynchronousHints'));		
	this.properties.push(new Property('isForCompensation', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isForCompensation','isForCompensationHints'));	
		
	this.properties.push(new Property('executionListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/executionListeners.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'executionListeners','executionListenersHints'));
		
	this.properties.push(new Property('loopCharacteristics', 'Multi Instance', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/loopCharacteristics.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'loopCharacteristics','loopCharacteristicsHints'));
			
	this.properties.push(new Property('customProperties', 'Extends', false, 
		EditorType.text, '','customProperties','customPropertiesHints'));			
		
};

CollaborationInfo.prototype = new BaseElementInfo();
CollaborationInfo.prototype.constructor = CollaborationInfo;

/**
 * Class: CustomProperty
 */
function CustomProperty(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.simpleValue = "";
	this.label = "Custom Property";
};

CustomProperty.prototype = new BaseElement();
CustomProperty.prototype.constructor = CustomProperty;


/**
 * Class: CustomPropertyInfo
 */
function CustomPropertyInfo(id)
{
	BaseElementInfo.call(this, id);

};

CustomPropertyInfo.prototype = new BaseElementInfo();
CustomPropertyInfo.prototype.constructor = CustomPropertyInfo;
/**
 * Class: DataAssociation
 */
function DataAssociation(id)
{
	BaseElement.call(this, id);
	this.source = "";
	this.sourceExpression = "";
	this.target = "";
	this.targetExpression = "";
	this.label = "Data Association";
	this.infoClass = "DataAssociationInfo";
};

DataAssociation.prototype = new BaseElement();
DataAssociation.prototype.constructor = DataAssociation;

/**
 * Class: DataAssociationInfo
 */
function DataAssociationInfo(id)
{
	BaseElementInfo.call(this, id);
};

DataAssociationInfo.prototype = new BaseElementInfo();
DataAssociationInfo.prototype.constructor = DataAssociationInfo;
/**
 * Class: DataState
 */
function DataState(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "Data State"
};

DataState.prototype = new BaseElement();
DataState.prototype.constructor = DataState;



/**
 * Class: DataStateInfo
 */
function DataStateInfo(id)
{
	BaseElementInfo.call(this, id);
};

DataStateInfo.prototype = new BaseElementInfo();
DataStateInfo.prototype.constructor = DataStateInfo;/**
 * Class: EventDefinition
 */
function EventDefinition(id)
{
	BaseElement.call(this, id);
	this.label = "EventDefinition";
};

EventDefinition.prototype = new BaseElement();
EventDefinition.prototype.constructor = EventDefinition;

/**
 * Class: CancelEventDefinition
 */
function CancelEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.label = "CancelEventDefinition";
};

CancelEventDefinition.prototype = new EventDefinition();
CancelEventDefinition.prototype.constructor = CancelEventDefinition;


function CancelEventDefinitionInfo(id){
    EventInfo.call(this,id);
    	
};

/**
 * Class: CompensateEventDefinition
 */
function CompensateEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.waitForCompletion = "";
	this.activityReference = "";
	this.label = "Compensate EventDefinition";
	this.infoClass = "CompensateEventDefinitionInfo";
};

CompensateEventDefinition.prototype = new EventDefinition();
CompensateEventDefinition.prototype.constructor = CompensateEventDefinition;


function CompensateEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('waitForCompletion', 'CompensateEventDefinition', false, 
	EditorType.text, '','waitForCompletion','waitForCompletionHints'));
   	this.properties.push(new Property('activityReference', 'CompensateEventDefinition', false, 
	EditorType.text, '','activityReference','activityReferenceHints'));
};
/**
 * Class: ConditionalEventDefinition
 */
function ConditionalEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.condition;//public Condition condition
	this.label = "Conditional EventDefinition";
	this.infoClass = "ConditionalEventDefinitionInfo";
};

ConditionalEventDefinition.prototype = new EventDefinition();
ConditionalEventDefinition.prototype.constructor = ConditionalEventDefinition;


function ConditionalEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('condition', 'Conditional Event Definition', false, 
    EditorType.refbox, {url:'./dialog/expression/expression.html',features:'dialogHeight:600px;dialogWidth:800px;center:yes'},'condition','conditionHints'));
   	
};
/**
 * Class: ErrorEventDefinition
 */
function ErrorEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.errorCode = "";
	this.label = "Error EventDefinition";
	this.infoClass = "ErrorEventDefinitionInfo";
	
};

ErrorEventDefinition.prototype = new EventDefinition();
ErrorEventDefinition.prototype.constructor = ErrorEventDefinition;


function ErrorEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('errorCode', 'Error Event Definition', false, 
    EditorType.text, '','errorCode','errorCodeHints'));   	
};
/**
 * Class: EscalationEventDefinition
 */
function EscalationEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.escalationRef = "";
	this.label = "Escalation EventDefinition";
	this.infoClass = "EscalationEventDefinitionInfo";
	
};

EscalationEventDefinition.prototype = new EventDefinition();
EscalationEventDefinition.prototype.constructor = EscalationEventDefinition;


function EscalationEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('escalationRef', 'Escalation Event Definition', false, 
    EditorType.text, '','escalationRef','escalationRefHints'));   	
};

/**
 * Class: LinkEventDefinition
 */
function LinkEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.value = "";
	this.label = "Link EventDefinition";
	this.infoClass = "LinkEventDefinitionInfo";
};

LinkEventDefinition.prototype = new EventDefinition();
LinkEventDefinition.prototype.constructor = LinkEventDefinition;


function LinkEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('Value', 'Main Config', false, 
    EditorType.text, '','Value','ValueHints'));   	
};

/**
 * Class: MessageEventDefinition
 */
function MessageEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.operationRef = "";
	this.messageRef = "";
	this.label = "Message EventDefinition";
	this.infoClass = "MessageEventDefinitionInfo";
};

MessageEventDefinition.prototype = new EventDefinition();
MessageEventDefinition.prototype.constructor = MessageEventDefinition;


function MessageEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('operationRef', 'Message Event Definition', false, 
    EditorType.text, '','operationRef','operationRefHints'));  
    this.properties.push(new Property('messageRef', 'Message Event Definition', false, 
    EditorType.text, '','messageRef','messageRefHints'));  
};

/**
 * Class: SignalEventDefinition
 */
function SignalEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.signalRef = "";
	this.label = "Signal EventDefinition";
	
};

SignalEventDefinition.prototype = new EventDefinition();
SignalEventDefinition.prototype.constructor = SignalEventDefinition;


function SignalEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('signalRef', 'Signal Event Definition', false, 
    EditorType.text, '','signalRef','signalRefHints'));  
   
};

/**
 * Class: TerminateEventDefinition
 */
function TerminateEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.label = "Terminate EventDefinition";
	
};

TerminateEventDefinition.prototype = new EventDefinition();
TerminateEventDefinition.prototype.constructor = TerminateEventDefinition;


function TerminateEventDefinitionInfo(id){
    EventInfo.call(this,id); 
};



/**
 * Class: TimerEventDefinition
 */
function TimerEventDefinition(id)
{
	EventDefinition.call(this, id);
	this.timeDate = "";
	this.timeDuration = "";
	this.timeCycle = "";
	this.label = "Timer EventDefinition";
};

TimerEventDefinition.prototype = new EventDefinition();
TimerEventDefinition.prototype.constructor = TimerEventDefinition;


function TimerEventDefinitionInfo(id){
    EventInfo.call(this,id);
    this.properties.push(new Property('timeDate', 'Timer Event Definition', false, 
	EditorType.text, '' ,'timeDate','timeDateHints'));  
    this.properties.push(new Property('timeDuration', 'Timer Event Definition', false, 
    EditorType.text, '','timeDuration','timeDurationHints'));  
    this.properties.push(new Property('timeCycle', 'Timer Event Definition', false, 
    EditorType.text, '','timeCycle','timeCycleHints'));  
};
/**
 * Class: EventListener
 */
function EventListener(id)
{
	BaseElement.call(this, id);
	this.event = "";
	this.implementationType = "";
	this.implementation = "";
	this.method = "";
	this.scriptProcessor = "";
	this.fieldExtensions = [];
	this.label = "Event Listener";
};

EventListener.prototype = new BaseElement();
EventListener.prototype.constructor = EventListener;

/**
 * Class: ExecutionListener
 */
function ExecutionListener(id)
{
	EventListener.call(this, id);
	this.label = "Execution Listener";
};

ExecutionListener.prototype = new EventListener();
ExecutionListener.prototype.constructor = ExecutionListener;



/**
 * Class: TaskListener
 */
function TaskListener(id)
{
	//EventListener.call(this, id);
	//下面的属性，在TaskListener.java里面没有这些属性
	this.eventType=""
	this.implementationType=""
	this.implementation=""
	this.method=""
	this.fields=null;
	this.label = "Task Listener";
};

//TaskListener.prototype = new EventListener();
TaskListener.prototype.constructor = TaskListener;

/**
 * Class: FlowElement
 */
function FlowElement(id)
{
	BaseElement.call(this, id);
	this.name = "";
};

FlowElement.prototype = new BaseElement();
FlowElement.prototype.constructor = FlowElement;

/**
 * Class: FlowElementInfo
 */
function FlowElementInfo(id)
{
	BaseElementInfo.call(this, id);
	
};

FlowElementInfo.prototype = new BaseElementInfo();
FlowElementInfo.prototype.constructor = FlowElementInfo;
/**
 * Class: Connector
 */
function Connector(id)
{
	FlowElement.call(this, id);
	//this.source = null;
	this.sourceRef = "";
	//this.target = null;
	this.targetRef = "";
	this.label = "Connector";
};

Connector.prototype = new FlowElement();
Connector.prototype.constructor = Connector;

/**
 * Class: ConnectorInfo
 */
function ConnectorInfo(id)
{
	FlowElementInfo.call(this, id);

};

ConnectorInfo.prototype = new FlowElementInfo();
ConnectorInfo.prototype.constructor = ConnectorInfo;
/**
 * Class: Association
 */
function Association(id)
{
	Connector.call(this, id);
	this.name = "Association";
	this.label = "Association";
	this.infoClass = "AssociationInfo";
};

Association.prototype = new Connector();
Association.prototype.constructor = Association;


/**
 * Class: AssociationInfo
 */
function AssociationInfo(id)
{
	FlowNodeInfo.call(this, id);
};

AssociationInfo.prototype = new FlowNodeInfo();
AssociationInfo.prototype.constructor = AssociationInfo;/**
 * Class: MessageFlow
 */
function MessageFlow(id)
{
	Connector.call(this, id);
	this.name = "MessageFlow";
	this.messageRef = "";
	this.label = "MessageFlow";
	this.infoClass = "MessageFlowInfo";
};

MessageFlow.prototype = new Connector();
MessageFlow.prototype.constructor = MessageFlow;



/**
 * Class: MessageFlowInfo
 */
function MessageFlowInfo(id)
{
	FlowNodeInfo.call(this, id);
    this.properties.push(new Property('MessageRef', 'Main Config', false, 
		EditorType.text, '','messageRef','messageRefHints'));
};
MessageFlowInfo.prototype = new FlowNodeInfo();
MessageFlowInfo.prototype.constructor = MessageFlowInfo;
/**
 * Class: SequenceFlow
 */
function SequenceFlow(id)
{
	Connector.call(this, id);
	this.name = "SequenceFlow";
	this.conditionExpression ;
	this.defaultSequenceFlow = false;
	this.immediate = false;
	this.method = "";
	this.executionListeners = null;
	this.label = "SequenceFlow";
	this.infoClass = "SequenceFlowInfo";
};

SequenceFlow.prototype = new Connector();
SequenceFlow.prototype.constructor = SequenceFlow;



/**
 * Class: SequenceFlowInfo
 */
function SequenceFlowInfo(id)
{
	 FlowNodeInfo.call(this, id);
	 this.properties.push(new Property('conditionExpression', 'Main Config', false, 
        EditorType.refbox, {url:'./dialog/expression/expression.html',features:'dialogHeight:600px;dialogWidth:800px;center:yes'},'conditionExpression','conditionExpressionHints'));
	 this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'defaultSequenceFlow','defaultSequenceFlowHints'));
	 this.properties.push(new Property('immediate', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'immediate','immediateHints'));
};

SequenceFlowInfo.prototype = new  FlowNodeInfo();
SequenceFlowInfo.prototype.constructor = SequenceFlowInfo;

/**
 * Class: DataObject
 */
function DataObject(id)
{
	FlowElement.call(this, id);
	this.name = "DataObject";
	this.isCollection = false;
	this.label="DataObject";
	this.infoClass = "DataObjectInfo";
};

DataObject.prototype = new FlowElement();
DataObject.prototype.constructor = DataObject;


/**
 * Class: DataObjectInfo
 */
function DataObjectInfo(id)
{
	FlowElementInfo.call(this, id);
	this.properties.push(new Property('isCollection', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isCollection','isCollectionHints'));
};

DataObjectInfo.prototype = new FlowElementInfo();
DataObjectInfo.prototype.constructor = DataObjectInfo;
/**
 * Class: FlowNode
 */
function FlowNode(id)
{
	FlowElement.call(this, id);
};

FlowNode.prototype = new FlowElement();
FlowNode.prototype.constructor = FlowNode;

/**
 * Class: FlowNodeInfo
 */
function FlowNodeInfo(id)
{
	FlowElementInfo.call(this, id);
};

FlowNodeInfo.prototype = new FlowElementInfo();
FlowNodeInfo.prototype.constructor = FlowNodeInfo;/**
 * Class: Activity
 */
function Activity(id)
{
	FlowNode.call(this, id);
	this.asynchronous = false;
	this.ForCompensation = false;
	this.defaultFlow = "";
	
	this.loopCharacteristics;
	this.executionListeners;
	this.boundaryEvents;// not show
	this.customProperties;
};

Activity.prototype = new FlowNode();
Activity.prototype.constructor = Activity;

/**
  * Class: ActivityInfo
  */
function ActivityInfo(id)
{
	FlowNodeInfo.call(this, id);
	this.properties.push(new Property('asynchronous', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'asynchronous','asynchronousHints'));		
	this.properties.push(new Property('isForCompensation', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'isForCompensation','isForCompensationHints'));	
		
	this.properties.push(new Property('executionListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/executionListeners.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'executionListeners','executionListenersHints'));
		
	this.properties.push(new Property('loopCharacteristics', 'Multi Instance', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/loopCharacteristics.html',features:'dialogHeight:300px;dialogWidth:400px;center:yes'},'loopCharacteristics','loopCharacteristicsHints'));
			
	this.properties.push(new Property('customProperties', 'Extends', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/customProperties.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'customProperties','customPropertiesHints'));		
		
};

ActivityInfo.prototype = new FlowNodeInfo();
ActivityInfo.prototype.constructor = ActivityInfo;
/**
 * Class: CallActivity
 */
function CallActivity(id)
{
	Activity.call(this, id);
	this.name = "Call Activity";
	this.calledElement = "";
	this.inParameters = null;
	this.outParameters = null;
	this.label = "Call Activity";
	this.infoClass = "CallActivityInfo";
};

CallActivity.prototype = new Activity();
CallActivity.prototype.constructor = CallActivity;


/**
 * Class: CallActivityInfo
 */
function CallActivityInfo(id)
{
	ActivityInfo.call(this, id);
    this.properties.push(new Property('calledElement', 'Main Config', false, 
		EditorType.text, '','calledElement','calledElementHints'));
	this.properties.push(new Property('inParameters', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/inParameters.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'inParameters','inParametersHints'));
	this.properties.push(new Property('outParameters', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/outParameters.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'outParameters','outParametersHints'));
};

CallActivityInfo.prototype = new ActivityInfo();
CallActivityInfo.prototype.constructor = CallActivityInfo;

/**
 * Class: EventSubprocessCollapsed
 */
function EventSubprocessCollapsed(id)
{
	Activity.call(this, id);
	this.name = "EventSubprocessCollapsed";
	this.calledElement = "";
	this.inParameters = [];
	this.outParameters = [];
	this.label = "EventSubprocessCollapsed";
	this.infoClass = "EventSubprocessCollapsedInfo";
};

EventSubprocessCollapsed.prototype = new Activity();
EventSubprocessCollapsed.prototype.constructor = EventSubprocessCollapsed;



/**
 * Class: EventSubprocessCollapsedInfo
 */
function EventSubprocessCollapsedInfo(id)
{
	ActivityInfo.call(this, id);
    this.properties.push(new Property('calledElement', 'Main Config', false, 
		EditorType.text, '','calledElement','calledElementHints'));
	this.properties.push(new Property('inParameters', 'Main Config', false, 
		EditorType.text, '','inParameters','inParametersHints'));
	this.properties.push(new Property('outParameters', 'Main Config', false, 
		EditorType.text, '','outParameters','outParametersHints'));
};

EventSubprocessCollapsedInfo.prototype = new ActivityInfo();
EventSubprocessCollapsedInfo.prototype.constructor = EventSubprocessCollapsedInfo;/**
 * Class: SubProcess
 */
function SubProcess(id)
{
	Activity.call(this, id);
	this.name = "Sub-Process";
	this.triggeredByEvent = false;
	this.flowElements = null;
	this.isContainer = true;
	this.isCanBound = true;
	this.label = "Sub-Process";
	this.infoClass = "SubProcessInfo";
};

SubProcess.prototype = new Activity();
SubProcess.prototype.constructor = SubProcess;

/**
 * Class: SubProcessInfo
 */
function SubProcessInfo(id)
{
	ActivityInfo.call(this, id);
};

SubProcessInfo.prototype = new ActivityInfo();
SubProcessInfo.prototype.constructor = SubProcessInfo;

/**
 * Class: EventSubProcess
 */
function EventSubProcess(id)
{
	SubProcess.call(this, id);
	this.triggeredByEvent = true;
	this.name = "EventSubProcess";
	this.label = "Event SubProcess";
	this.infoClass = "EventSubProcessInfo";
};

EventSubProcess.prototype = new SubProcess();
EventSubProcess.prototype.constructor = EventSubProcess;


/**
 * Class: EventSubProcessInfo
 */
function EventSubProcessInfo(id)
{
	SubProcessInfo.call(this, id);
};

EventSubProcessInfo.prototype = new SubProcessInfo();
EventSubProcessInfo.prototype.constructor = EventSubProcessInfo;/**
 * Class: Task
 */
function Task(id)
{
	Activity.call(this, id);
	this.taskType;
};

Task.prototype = new Activity();
Task.prototype.constructor = Task;

function BpmnTaskType(){};
BpmnTaskType.prototype.constructor = BpmnTaskType;
BpmnTaskType.UserTask = 0;
BpmnTaskType.ScriptTask = 1;
BpmnTaskType.ServiceTask = 2;
BpmnTaskType.MailTask = 3;
BpmnTaskType.ManualTask = 4;
BpmnTaskType.ReceiveTask = 5;
BpmnTaskType.BusinessRuleTask = 6;


/**
 * Class: TaskInfo
 */
function TaskInfo(id)
{
	ActivityInfo.call(this, id);

//	this.properties.push(new Property('taskType', '', true, 
//		EditorType.combobox, {data:[{value:0,text:'UserTask'}, {value:1,text:'ScriptTask'}, {value:2,text:'ServiceTask'}, {value:3,text:'MailTask'}, {value:4,text:'ManualTask'}, {value:5,text:'ReceiveTask'}, {value:6,text:'BusinessRuleTask'}]}));		
};

TaskInfo.prototype = new ActivityInfo();
TaskInfo.prototype.constructor = TaskInfo;
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
/**
 * Class: ManualTask
 */
function ManualTask(id)
{
	Task.call(this, id);
	this.name = "Manual Task";
	this.label = "Manual Task";
	this.infoClass = "ManualTaskInfo";
};

ManualTask.prototype = new Task();
ManualTask.prototype.constructor = ManualTask;

/**
 * Class: ManualTaskInfo
 */
function ManualTaskInfo(id)
{
	TaskInfo.call(this, id);
};

ManualTaskInfo.prototype = new TaskInfo();
ManualTaskInfo.prototype.constructor = ManualTaskInfo;
	/**
 * Class: ReceiveTask
 */
function ReceiveTask(id)
{
	Task.call(this, id);
	this.name = "Receive Task";
	this.label = "Receive Task";
	this.infoClass = "ReceiveTaskInfo";
};

ReceiveTask.prototype = new Task();
ReceiveTask.prototype.constructor = ReceiveTask;


/**
 * Class: ReceiveTaskInfo
 */
function ReceiveTaskInfo(id)
{
	TaskInfo.call(this, id);
};

ReceiveTaskInfo.prototype = new TaskInfo();
ReceiveTaskInfo.prototype.constructor = ReceiveTaskInfo;

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
		EditorType.text, '','extendClass','extendClassHints'));
	this.properties.push(new Property('method', 'Main Config', false, 
		EditorType.text, '','method','methodHints'));
	this.properties.push(new Property('operationRef', 'Main Config', false, 
		EditorType.text, '','operationRef','operationRefHints'));
	this.properties.push(new Property('fieldExtensions', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/fieldExtensions.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'fieldExtensions','fieldExtensionsHints'));
	this.properties.push(new Property('resultVariableName', 'Main Config', false, 
		EditorType.text, '','resultVariableName','resultVariableNameHints'))
};

ServiceTaskInfo.prototype = new TaskInfo();
ServiceTaskInfo.prototype.constructor = ServiceTaskInfo;
/**
 * Class: VOExchangeTask
 */
function VOExchangeTask(id)
{
	ServiceTask.call(this, id);
	this.label = "VOExchange Task";
};

VOExchangeTask.prototype = new ServiceTask();
VOExchangeTask.prototype.constructor = VOExchangeTask;



/**
 * Class: VOExchangeTaskInfo
 */
function VOExchangeTaskInfo(id)
{
	ServiceTaskInfo.call(this, id);
};

VOExchangeTaskInfo.prototype = new ServiceTaskInfo();
VOExchangeTaskInfo.prototype.constructor = VOExchangeTaskInfo;
function DefaultTaskHandlingDefinition(id)
{
	
	this.taskHandleType = null;
};


DefaultTaskHandlingDefinition.prototype.constructor = DefaultTaskHandlingDefinition;

function DefaultParticipantDefinition(id)
{
	//this.id=""; zhailzh 因为在Swing版中参与者存储的量中没有此项
	this.participantID="";
	this.code="";
	this.name="";
	this.participantType=null;
	this.participantFilterType=null;
	this.properties = null;
};
DefaultParticipantDefinition.prototype.constructor = DefaultParticipantDefinition;
function DefaultNoticeDefinition(id)
{
	this.receivers =null;
	this.contentTemplate="";
	this.noticeType=null;
	this.noticeTime=null;
	this.condition="";
	this.hasReceipt=false;
	this.properties=null;
};
DefaultNoticeDefinition.prototype.constructor = DefaultNoticeDefinition;function UserTaskPolicyControl(id)
{
	this.name = "UserTaskPolicyControl";
	this.infoClass = "UserTaskInfo";
		//基本信息部分--------------------------------------------------------------
	this.approve=false;		//审批，使用动作标记
	this.deliver=false;		//传阅，使用固定的内置逻辑
	this.undertake=false;	//承办，
	this.processClass="";	//处理类
	this.form="";			//表单
	//@XmlElement
	//public void modifyResources(){}//修改审批对话框资源信息
	
	//参与者，包括组织机构、角色、角色组、用户、用户组、回报关系、虚拟角色（主办人、协办人、发起人）、同其他活动节点参与者、自定义参与这类
	//权限控制
	this.canAddSign=false;	//允许加签
	this.canDelegate=false;	//允许改派
	this.canTransfer=false;	//可转发
	this.canDeliver=false;	//可传阅
	this.canAssign=false;	//由上一步指派
	this.opinionEditable=false;	//可编辑意见
	this.opinionNullable=false;//是否意见可空
	//同部门限定
	this.canHasten=false;	//允许催办
	this.canPrint=false;	//允许打印
	this.canRecycle=false;	//允许收回
	this.canPassthrough=false; //允许快速通道
	this.canUploadAttachment=false;	//允许附件上传
	this.canDownloadAttachment=false;//允许附件下载
	this.canDeleteAttachment=false;	//允许附件删除
	this.canModifyAttachment=false;	//允许附件修改
	this.canViewAttachment=false;	//允许附件查看
	//协办参与者
	this.collaborationParticipants= null;
	this.voucherPrivilege=new Object();
	//活动策略----------------------------------------------------------------
	//回退策略
	this.canReject=false; //禁止回退，允许回退
	this.rejectPolicy; //上一步，制单人，全部活动，指定活动
	this.activityRef;
	//消息提醒----------------------------------------------------------------使用消息提醒和timer节点解决
	//任务创建消息提醒
		//任务创建提醒--使用协同消息
		//制单人控制项--使用协同消息
	//任务完成消息提醒
		//任务完成提醒--使用协同消息
		//制单人控制项--使用协同消息
	//时间估算
		//时间单位
		//提醒时间
		//工作时间
	//超时消息提醒
		//超时提醒--使用协同消息
		//制单人控制项--使用协同控制
	//超时控制
		//超时动作：继续等待、超时终止、超时继续
};
UserTaskPolicyControl.prototype.constructor = UserTask;



/**
 * Class: UserTask
 */
function UserTask(id)
{
	Task.call(this, id);
	this.name = "User Task";
	this.priority = 0;
	this.formKey="";
	this.dueDate="";
	this.formProperties=null;//new FormProperty();
    this.makeBill = false;
	this.afterSign = false;
	this.sequence = false;
	this.taskListeners=null;//new TaskListener();
	this.participants=null;//ew DefaultParticipantDefinition();
	this.notices=null;//new DefaultNoticeDefinition();
	this.taskHandlings=null;//new DefaultTaskHandlingDefinition();
	this.openUIStyle="BisunessUI";
	this.openURI="";	
	this.ExtendProperties;
	this.taskType = 0;
	this.control=new UserTaskPolicyControl();
	this.label = "User Task";
	this.infoClass = "UserTaskInfo";
};

UserTask.prototype = new Task();
UserTask.prototype.constructor = UserTask;

UserTask.prototype.toXML = function (mxCodec) {
	var control = this.control;
	this.control=null;

	var participants = this.participants;
	this.participants=null;

	var notices = this.notices;
	this.notices=null;

	var taskHandlings = this.taskHandlings;
	this.taskHandlings=null;

	var formProperties = this.formProperties;
	this.formProperties=null;

	var taskListeners = this.taskListeners;
	this.taskListeners=null;
	
	var mxObjectCodec = mxCodecRegistry.getCodec(this.constructor);
	var node = mxObjectCodec.encode(mxCodec, this);
	var nodeex = mxCodec.document.createElement("extensionElements");
	node.appendChild(nodeex);
	
	var nodecontrol = mxCodec.document.createElement("nc:userTaskPolicyControl");
	nodeex.appendChild(nodecontrol);
	mxObjectCodec.encodeObject(mxCodec, control, nodecontrol);

	if(participants != null)
	{
		for (var i = 0; i < participants.length; i++)
		{
			var participant = participants[i];
			var nodeparticipantj = mxCodec.document.createElement("nc:participant");
			nodeex.appendChild(nodeparticipantj);
			mxObjectCodec.encodeObject(mxCodec, participant, nodeparticipantj);
			
			var nodeparticipantType = mxCodec.document.createElement("participantType");
			nodeparticipantj.appendChild(nodeparticipantType);
			nodeparticipantType.setAttribute("code", participant.participantType);
			
			var nodeparticipantFilterType = mxCodec.document.createElement("participantFilterType");
			nodeparticipantj.appendChild(nodeparticipantFilterType);
			nodeparticipantFilterType.setAttribute("code", participant.participantFilterType);
		}
	}
	
	this.subToXML(mxCodec, mxObjectCodec, notices, nodeex, "nc:notice")
	this.subToXML(mxCodec, mxObjectCodec, taskHandlings, nodeex, "nc:taskHandling")
	this.subToXML(mxCodec, mxObjectCodec, formProperties, nodeex, "nc:taskHandling")
	this.subToXML(mxCodec, mxObjectCodec, taskListeners, nodeex, "nc:taskListener")
	
	this.control = control;
	this.participants = participants;
	this.notices = notices;
	this.taskHandlings = taskHandlings;
	this.formProperties = formProperties;
	this.taskListeners = taskListeners;
	return node;
};

UserTask.prototype.subToXML = function (mxCodec, mxObjectCodec, subObjs, nodeex, nodeName) {
	if(subObjs != null)
	{
		for (var i = 0; i < subObjs.length; i++)
		{
			var subObj = subObjs[i];
			var nodesubObj = mxCodec.document.createElement(nodeName);
			nodeex.appendChild(nodesubObj);
			mxObjectCodec.encodeObject(mxCodec, subObj, nodesubObj);
		}
	}
}

/**
 * Class: UserTaskInfo
 */
function UserTaskInfo(id)
{
	TaskInfo.call(this, id);
	this.properties.push(new Property('ExtendProperties', 'General', false, 
	    EditorType.text, '','ExtendProperties','ExtendPropertiesHints'));
   	this.properties.push(new Property('formKey', 'Form', false, 
		EditorType.text, '','formKey','formKeyHints'));
	this.properties.push(new Property('formProperties', 'Form', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/formProperties.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'formProperties','formPropertiesHints'));
	this.properties.push(new Property('dueDate', 'Main Config', false, 
		EditorType.datebox, '','dueDate','dueDateHints'));
	this.properties.push(new Property('priority', 'Main Config', false, 
		EditorType.numberbox, '','priority','priorityHints'));
	this.properties.push(new Property('makeBill', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'makeBill','makeBillHints'));		
	this.properties.push(new Property('taskListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/taskListeners.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'taskListeners','taskListenersHints'));
	this.properties.push(new Property('participants', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/Participants.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'participants','participantsHints'));
	this.properties.push(new Property('notices', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/notices.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'notices','noticesHints'));
	this.properties.push(new Property('taskHandlings', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/taskHandlings.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'taskHandlings','taskHandlingsHints'));
	this.properties.push(new Property('openUIStyle', 'Main Config', false, 
	    EditorType.combobox, {data:
	    [{value:'BisunessUI',text:mxResources.get('BisunessUI')}, {value:'ApproveUI',text:mxResources.get('ApproveUI')}, {value:'DefinedUI',text:mxResources.get('DefinedUI')}, {value:'CustomURI',text:mxResources.get('CustomURI')}]},'openUIStyle','openUIStyleHints'));
	this.properties.push(new Property('openURI', 'Main Config', false, 
		EditorType.text, '','openURI','openURIHints'));
    this.properties.push(new Property('control.approve', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.approve','control.approveHints'));
	this.properties.push(new Property('control.deliver', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.deliver','control.deliverHints'));
	this.properties.push(new Property('control.undertake', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.undertake','control.undertakeHints'));
	this.properties.push(new Property('control.processClass', 'Policy Control', false, 
		EditorType.text, '','control.processClass','control.processClassHints'));
	this.properties.push(new Property('control.canAddSign', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canAddSign','control.canAddSignHints'));
	this.properties.push(new Property('control.canDelegate', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDelegate','control.canDelegateHints'));
	this.properties.push(new Property('control.canTransfer', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canTransfer','control.canTransferHints'));		
	this.properties.push(new Property('control.canDeliver', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDeliver','control.canDeliverHints'));
	this.properties.push(new Property('control.canAssign', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canAssign','control.canAssignHints'));
	this.properties.push(new Property('control.opinionEditable', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.opinionEditable','control.opinionEditableHints'));
	this.properties.push(new Property('control.opinionNullable', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.opinionNullable','control.opinionNullableHints'));
	this.properties.push(new Property('control.canHasten', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canHasten','control.canHastenHints'));
	this.properties.push(new Property('control.canPrint', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canPrint','control.canPrintHints'));
	this.properties.push(new Property('control.canRecycle', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canRecycle','control.canRecycleHints'));
	this.properties.push(new Property('control.canPassthrough', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canPassthrough','control.canPassthroughHints'));
	this.properties.push(new Property('control.canUploadAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canUploadAttachment','control.canUploadAttachmentHints'));
	this.properties.push(new Property('control.canDownloadAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDownloadAttachment','control.canDownloadAttachmentHints'));
	this.properties.push(new Property('control.canDeleteAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDeleteAttachment','control.canDeleteAttachmentHints'));
	this.properties.push(new Property('control.canModifyAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canModifyAttachment','control.canModifyAttachmentHints'));
	this.properties.push(new Property('control.canViewAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canViewAttachment','control.canViewAttachmentHints'));
	this.properties.push(new Property('control.collaborationParticipants', 'Policy Control', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/Participants.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'control.collaborationParticipants','control.collaborationParticipantsHints'));
	this.properties.push(new Property('control.canReject', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canReject','control.canRejectHints'));
	this.properties.push(new Property('control.rejectPolicy', 'Policy Control', true, 
		EditorType.combobox, {data:[{value:0,text:mxResources.get('LastStep')}, {value:1,text:mxResources.get('BillMaker')}, {value:2,text:mxResources.get('AllActivity')},{value:3,text:mxResources.get('SpecifiedActivity')}]},'control.rejectPolicy','control.rejectPolicyHints'));		
	this.properties.push(new Property('control.activityRef', 'Policy Control', false, 
		EditorType.text, '','control.activityRef','control.activityRefHints'));
};

UserTaskInfo.prototype = new TaskInfo();
UserTaskInfo.prototype.constructor = UserTaskInfo;
/**
 * Class: Event
 */
function Event(id)
{
	FlowNode.call(this, id);
	this.cancelEventDefinition ;//CancelEventDefinition
	this.compensateEventDefinition ;
	this.conditionalEventDefinition ;
	this.errorEventDefinition ;
	this.linkEventDefinition ;
	this.messageEventDefinition ;
	this.signalEventDefinition ;
	this.timerEventDefinition ;
	this.terminateEventDefinition ;
	this.eventDefinitions = null ;
	//有些函数没有完成
	this.label = "Event Listener";
};

Event.prototype = new FlowNode();
Event.prototype.constructor = Event;

/**
 * Class: EventInfo
 */
function EventInfo(id)
{
	FlowNodeInfo.call(this, id);
};

EventInfo.prototype = new FlowNodeInfo();
EventInfo.prototype.constructor = EventInfo;
/**
 * Class: CatchEvent
 */
function CatchEvent(id)
{
	Event.call(this, id);
	this.interrupting ;
	this.boundaryRef = "";
};

CatchEvent.prototype = new Event();
CatchEvent.prototype.constructor = CatchEvent;


 /**
 * Class: CatchEventInfo
 */
function CatchEventInfo(id)
{
	EventInfo.call(this, id);
};

CatchEventInfo.prototype = new EventInfo();
CatchEventInfo.prototype.constructor = CatchEventInfo;

/**
 * Class: CompensationBoundaryEvent
 */
function CompensationBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "CompensationBoundaryEventInfo";
};
 
CompensationBoundaryEvent.prototype = new CatchEvent();
CompensationBoundaryEvent.prototype.constructor = CompensationBoundaryEvent;


/**
 * Class: CompensationBoundaryEventInfo
 */
function CompensationBoundaryEventInfo(id)
{
	IntermediateCatchCompensationEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

CompensationBoundaryEventInfo.prototype = new IntermediateCatchCompensationEventInfo();
CompensationBoundaryEventInfo.prototype.constructor = CompensationBoundaryEventInfo;
function CancelBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "CancelBoundaryEventInfo";
};
 
CancelBoundaryEvent.prototype = new CatchEvent();
CancelBoundaryEvent.prototype.constructor = CancelBoundaryEvent;


/**
 * Class: CancelBoundaryEventInfo
 */
function CancelBoundaryEventInfo(id)
{
	IntermediateCatchCancelEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

CancelBoundaryEventInfo.prototype = new IntermediateCatchCancelEventInfo();
CancelBoundaryEventInfo.prototype.constructor = CancelBoundaryEventInfo;
/**
 * Class: CompensationBoundaryEvent
 */
function ConditionalBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "ConditionalBoundaryEventInfo";
};
 
ConditionalBoundaryEvent.prototype = new CatchEvent();
ConditionalBoundaryEvent.prototype.constructor = ConditionalBoundaryEvent;


/**
 * Class: ConditionalBoundaryEventInfo
 */
function ConditionalBoundaryEventInfo(id)
{
	IntermediateCatchConditionalEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

ConditionalBoundaryEventInfo.prototype = new IntermediateCatchConditionalEventInfo();
CompensationBoundaryEventInfo.prototype.constructor = ConditionalBoundaryEventInfo;/**
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
ErrorBoundaryEventInfo.prototype.constructor = ErrorBoundaryEventInfo;/**
 * Class: EscalationBoundaryEvent
 */
function EscalationBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "EscalationBoundaryEventInfo";
};
 
EscalationBoundaryEvent.prototype = new CatchEvent();
EscalationBoundaryEvent.prototype.constructor = EscalationBoundaryEvent;


/**
 * Class: EscalationBoundaryEventInfo
 */
function EscalationBoundaryEventInfo(id)
{
	IntermediateCatchEscalationEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

EscalationBoundaryEventInfo.prototype = new IntermediateCatchEscalationEventInfo();
EscalationBoundaryEventInfo.prototype.constructor = EscalationBoundaryEventInfo;/**
 * Class: LinkBoundaryEvent
 */
function LinkBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "LinkBoundaryEventInfo";
};
 
LinkBoundaryEvent.prototype = new CatchEvent();
LinkBoundaryEvent.prototype.constructor = LinkBoundaryEvent;


/**
 * Class: LinkBoundaryEventInfo
 */
function LinkBoundaryEventInfo(id)
{
	IntermediateCatchLinkEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

LinkBoundaryEventInfo.prototype = new IntermediateCatchLinkEventInfo();
LinkBoundaryEventInfo.prototype.constructor = LinkBoundaryEventInfo;/**
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
MessageBoundaryEventInfo.prototype.constructor = MessageBoundaryEventInfo;/**
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
MultiParallelBoundaryEventInfo.prototype.constructor = MultiParallelBoundaryEventInfo;/**
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
MultipleBoundaryEventInfo.prototype.constructor = MultipleBoundaryEventInfo;/**
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
SignalBoundaryEventInfo.prototype.constructor = SignalBoundaryEventInfo;/**
 * Class: TimerBoundaryEvent
 */
function TimerBoundaryEvent(id)
{
	CatchEvent.call(this, id);
	this.cancelActivity=false;
	this.attachedToRef = "";
	this.infoClass = "TimerBoundaryEventInfo";
};
 
TimerBoundaryEvent.prototype = new CatchEvent();
TimerBoundaryEvent.prototype.constructor = TimerBoundaryEvent;


/**
 * Class: TimerBoundaryEventInfo
 */
function TimerBoundaryEventInfo(id)
{
	IntermediateCatchTimerEventInfo.call(this, id);
	this.properties.push(new Property('cancelActivity', 'Main Config', false, 
	    EditorType.checkbox,{on:true,off:false},'cancelActivity','cancelActivityHints'));
	this.properties.push(new Property('attachedToRef', 'Main Config', false, 
		EditorType.text,'','attachedToRef','attachedToRefHints'));
};

TimerBoundaryEventInfo.prototype = new IntermediateCatchTimerEventInfo();
TimerBoundaryEventInfo.prototype.constructor = TimerBoundaryEventInfo;/**
 * Class: IntermediateCatchEvent
 */
function IntermediateCatchEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "Intermediate CatchEvent";
	this.name = "IntermediateCatchEvent";
	this.infoClass = "IntermediateCatchEventInfo";
};

IntermediateCatchEvent.prototype = new CatchEvent();
IntermediateCatchEvent.prototype.constructor = IntermediateCatchEvent;



/**
 * Class: IntermediateCatchEventInfo
 */
function IntermediateCatchEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateCatchEventInfo.prototype = new EventInfo();
IntermediateCatchEventInfo.prototype.constructor = IntermediateCatchEventInfo;function IntermediateCatchCancelEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchCancelEvent";
	this.name = "CatchCancelEvent";
	this.infoClass = "IntermediateCatchCancelEventInfo";
};

IntermediateCatchCancelEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchCancelEvent.prototype.constructor = IntermediateCatchCancelEvent;




function IntermediateCatchCancelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints')); 
	
};

IntermediateCatchCancelEventInfo.prototype = new CancelEventDefinitionInfo();
IntermediateCatchCancelEventInfo.prototype.constructor = IntermediateCatchCancelEventInfo;function IntermediateCatchCompensationEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchCompensationEvent";
	this.name = "CatchCompensationEvent";
	this.infoClass = "IntermediateCatchCompensationEventInfo";
};

IntermediateCatchCompensationEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchCompensationEvent.prototype.constructor = IntermediateCatchCompensationEvent;



function IntermediateCatchCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
	
};

IntermediateCatchCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
IntermediateCatchCompensationEventInfo.prototype.constructor = IntermediateCatchCompensationEventInfo;function IntermediateCatchConditionalEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchConditionalEvent";
	this.name = "CatchConditionalEvent";
	this.infoClass = "IntermediateCatchConditionalEventInfo";
};

IntermediateCatchConditionalEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchConditionalEvent.prototype.constructor = IntermediateCatchConditionalEvent;




function IntermediateCatchConditionalEventInfo(id)
{
	ConditionalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchConditionalEventInfo.prototype = new ConditionalEventDefinitionInfo();
IntermediateCatchConditionalEventInfo.prototype.constructor = IntermediateCatchConditionalEventInfo;function IntermediateCatchErrorEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchErrorEvent";
	this.name = "CatchErrorEvent";
	this.infoClass = "IntermediateCatchErrorEventInfo";
};

IntermediateCatchErrorEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchErrorEvent.prototype.constructor = IntermediateCatchErrorEvent;



function IntermediateCatchErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
};

IntermediateCatchErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
IntermediateCatchErrorEventInfo.prototype.constructor = IntermediateCatchErrorEventInfo;function IntermediateCatchEscalationEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchEscalationEvent";
	this.name = "CatchEscalationEvent";
	this.infoClass = "IntermediateCatchEscalationEventInfo";
};

IntermediateCatchEscalationEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchEscalationEvent.prototype.constructor = IntermediateCatchEscalationEvent;




function IntermediateCatchEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};

IntermediateCatchEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
IntermediateCatchEscalationEventInfo.prototype.constructor = IntermediateCatchEscalationEventInfo;function IntermediateCatchLinkEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchLinkEvent";
	this.name = "CatchLinkEvent";
	this.infoClass = "IntermediateCatchLinkEventInfo";
};

IntermediateCatchLinkEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchLinkEvent.prototype.constructor = IntermediateCatchLinkEvent;




function IntermediateCatchLinkEventInfo(id)
{
	LinkEventDefinitionInfo.call(this, id);
};

IntermediateCatchLinkEventInfo.prototype = new LinkEventDefinitionInfo();
IntermediateCatchLinkEventInfo.prototype.constructor = IntermediateCatchLinkEventInfo;function IntermediateCatchMessageEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMessageEvent";
	this.name = "CatchMessageEvent";
	this.infoClass = "IntermediateCatchMessageEventInfo";
};

IntermediateCatchMessageEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMessageEvent.prototype.constructor = IntermediateCatchMessageEvent;




function IntermediateCatchMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchMessageEventInfo.prototype = new MessageEventDefinitionInfo();
IntermediateCatchMessageEventInfo.prototype.constructor = IntermediateCatchMessageEventInfo;function IntermediateCatchMultiParallelEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMultiParallelEvent";
	this.name = "CatchMultiParallelEvent";
	this.infoClass = "IntermediateCatchMultiParallelEventInfo";
};

IntermediateCatchMultiParallelEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMultiParallelEvent.prototype.constructor = IntermediateCatchMultiParallelEvent;




function IntermediateCatchMultiParallelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints')); 
};

IntermediateCatchMultiParallelEventInfo.prototype = new CancelEventDefinitionInfo();
IntermediateCatchMultiParallelEventInfo.prototype.constructor = IntermediateCatchMultiParallelEventInfo;function IntermediateCatchMultipleEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchMultipleEvent";
	this.name = "CatchMultipleEvent";
	this.infoClass = "IntermediateCatchMultipleEventInfo";
};

IntermediateCatchMultipleEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchMultipleEvent.prototype.constructor = IntermediateCatchMultipleEvent;




function IntermediateCatchMultipleEventInfo(id)
{
	EventInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchMultipleEventInfo.prototype = new EventInfo();
IntermediateCatchMultipleEventInfo.prototype.constructor = IntermediateCatchMultipleEventInfo;function IntermediateCatchSignalEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchSignalEvent";
	this.name = "CatchSignalEvent";
	this.infoClass = "IntermediateCatchSignalEventInfo";
};

IntermediateCatchSignalEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchSignalEvent.prototype.constructor = IntermediateCatchSignalEvent;




function IntermediateCatchSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchSignalEventInfo.prototype = new SignalEventDefinitionInfo();
IntermediateCatchSignalEventInfo.prototype.constructor = IntermediateCatchSignalEventInfo;function IntermediateCatchTimerEvent(id)
{
	CatchEvent.call(this, id);
	this.label = "CatchTimerEvent";
	this.name = "CatchTimerEvent";
	this.infoClass = "IntermediateCatchTimerEventInfo";
};

IntermediateCatchTimerEvent.prototype = new IntermediateCatchEvent();
IntermediateCatchTimerEvent.prototype.constructor = IntermediateCatchTimerEvent;




function IntermediateCatchTimerEventInfo(id)
{
	TimerEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
};

IntermediateCatchTimerEventInfo.prototype = new TimerEventDefinitionInfo();
IntermediateCatchTimerEventInfo.prototype.constructor = IntermediateCatchTimerEventInfo;/**
 * Class: StartEvent
 */
function StartEvent(id)
{
	CatchEvent.call(this, id);
	this.name = "StartEvent";
	this.initiator = "";
	this.resultVariableName = "";
	this.formKey = "";
	this.formProperties = null;
	this.extensionElements = null;
	this.label = "StartEvent";
	this.infoClass = "StartEventInfo";
};

StartEvent.prototype = new CatchEvent();
StartEvent.prototype.constructor = StartEvent;

/**
 * Class: StartEventInfo
 */
function StartEventInfo(id)
{
	EventInfo.call(this, id);
};

StartEventInfo.prototype = new EventInfo();
StartEventInfo.prototype.constructor = StartEventInfo;
function StartCompensateEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new CompensateEventDefinition();
	this.label = "StartCompensateEvent";
	this.infoClass = "StartCompensateEventInfo";
	this.name = "StartCompensateEvent";
};

StartCompensateEvent.prototype = new StartEvent();
StartCompensateEvent.prototype.constructor = StartCompensateEvent;

function StartCompensateEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
}	
StartCompensateEventInfo.prototype = new CompensateEventDefinitionInfo();
StartCompensateEventInfo.prototype.constructor = StartCompensateEventInfo;
function StartConditionalEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new ConditionalEventDefinition();
	this.name = "StartConditionalEvent";
	this.label = "StartConditionalEvent";
	this.infoClass = "StartConditionalEventInfo";
};

StartConditionalEvent.prototype = new StartEvent();
StartConditionalEvent.prototype.constructor = StartConditionalEvent;


function StartConditionalEventInfo(id)
{
	ConditionalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartConditionalEventInfo.prototype = new ConditionalEventDefinitionInfo();
StartConditionalEventInfo.prototype.constructor = StartConditionalEventInfo;

function StartErrorEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new ErrorEventDefinition();
	this.name = "StartErrorEvent";
	this.label = "StartErrorEvent";
	this.infoClass = "StartErrorEventInfo";
};

StartErrorEvent.prototype = new StartEvent();
StartErrorEvent.prototype.constructor = StartErrorEvent;


function StartErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
StartErrorEventInfo.prototype.constructor = StartErrorEventInfo;

function StartEscalationEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new EscalationEventDefinition();
	this.name = "StartEscalationEvent";
	this.label = "StartEscalationEvent";
	this.infoClass = "StartEscalationEventInfo";
};

StartEscalationEvent.prototype = new StartEvent();
StartEscalationEvent.prototype.constructor = StartEscalationEvent;

function StartEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
}	
StartEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
StartEscalationEventInfo.prototype.constructor = StartEscalationEventInfo;

function StartMessageEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new MessageEventDefinition();
	this.name = "StartMessageEvent";
	this.label = "StartMessageEvent";
	this.infoClass = "StartMessageEventInfo";
};

StartMessageEvent.prototype = new StartEvent();
StartMessageEvent.prototype.constructor = StartMessageEvent;

function StartMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
}	
StartMessageEventInfo.prototype = new MessageEventDefinitionInfo();
StartMessageEventInfo.prototype.constructor = StartMessageEventInfo;

function StartMultipleEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new EventDefinition();
	this.name = "StartMultipleEvent";
	this.label = "StartMultipleEvent";
	this.infoClass = "StartMultipleEventInfo";
};

StartMultipleEvent.prototype = new StartEvent();
StartMultipleEvent.prototype.constructor = StartMultipleEvent;

function StartMultipleEventInfo(id)
{
	EventInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartMultipleEventInfo.prototype = new EventInfo();
StartMultipleEventInfo.prototype.constructor = StartMultipleEventInfo;

function StartSignalEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new SignalEventDefinition();
	this.name = "StartSignalEvent";
	this.label = "StartSignalEvent";
	this.infoClass = "StartSignalEventInfo";
};

StartSignalEvent.prototype = new StartEvent();
StartSignalEvent.prototype.constructor = StartSignalEvent;

function StartSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartSignalEventInfo.prototype = new SignalEventDefinitionInfo();
StartSignalEventInfo.prototype.constructor = StartSignalEventInfo;

function StartTimerEvent(id)
{
	StartEvent.call(this,id);
	this.EventDefinition =  new TimerEventDefinition();
	this.name = "StartTimerEvent";
	this.label = "StartTimerEvent";
	this.infoClass = "StartTimerEventInfo";
};

StartTimerEvent.prototype = new StartEvent();
StartTimerEvent.prototype.constructor = StartTimerEvent;

function StartTimerEventInfo(id)
{
	TimerEventDefinitionInfo.call(this, id);
	this.properties.push(new Property('interrupting', 'Main Config', false, 
	EditorType.checkbox, {on:true,off:false},'interrupting','interruptingHints'));
}
StartTimerEventInfo.prototype = new TimerEventDefinitionInfo();
StartTimerEventInfo.prototype.constructor = StartTimerEventInfo;

/**
 * Class: ThrowEvent
 */
function ThrowEvent(id)
{
	Event.call(this, id);
	this.label = "Throw Event";
};

ThrowEvent.prototype = new Event();
ThrowEvent.prototype.constructor = ThrowEvent;




/**
 * Class: ThrowEventInfo
 */
function ThrowEventInfo(id)
{
	EventInfo.call(this, id);
};

ThrowEventInfo.prototype = new EventInfo();
ThrowEventInfo.prototype.constructor = ThrowEventInfo;



/**
 * Class: EndEvent
 */
function EndEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "End Event";
	this.name = "EndEvent";
	this.infoClass = "EndEventInfo";
};

EndEvent.prototype = new ThrowEvent();
EndEvent.prototype.constructor = EndEvent;


/**
 * Class: EndEventInfo
 */
function EndEventInfo(id)
{
	EventInfo.call(this, id);
};

EndEventInfo.prototype = new EventInfo();
EndEventInfo.prototype.constructor = EndEventInfo;function EndCancelEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndCancelEvent";
	this.name = "EndCancelEvent";
	this.infoClass = "EndCancelEventInfo";
};

EndCancelEvent.prototype = new ThrowEvent();
EndCancelEvent.prototype.constructor = EndCancelEvent;




function EndCancelEventInfo(id)
{
	CancelEventDefinitionInfo.call(this, id);
};

EndCancelEventInfo.prototype = new ConditionalEventDefinitionInfo();
EndCancelEventInfo.prototype.constructor = EndCancelEventInfo;function EndCompensationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndCompensationEvent";
	this.name = "EndCompensationEvent";
	this.infoClass = "EndCompensationEventInfo";
};

EndCompensationEvent.prototype = new ThrowEvent();
EndCompensationEvent.prototype.constructor = EndCompensationEvent;



function EndCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
};

EndCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
EndCompensationEventInfo.prototype.constructor = EndCompensationEventInfo;function EndErrorEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndErrorEvent";
	this.name = "EndErrorEvent";
	this.infoClass = "EndErrorEventInfo";
};

EndErrorEvent.prototype = new ThrowEvent();
EndErrorEvent.prototype.constructor = EndErrorEvent;

function EndErrorEventInfo(id)
{
	ErrorEventDefinitionInfo.call(this, id);
};

EndErrorEventInfo.prototype = new ErrorEventDefinitionInfo();
EndErrorEventInfo.prototype.constructor = EndErrorEventInfo;

function EndEsacalationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndEsacalationEvent";
	this.name = "EndEsacalationEvent";
	this.infoClass = "EndEsacalationEventInfo";
};
EndEsacalationEvent.prototype = new ThrowEvent();
EndEsacalationEvent.prototype.constructor = EndEsacalationEvent;

function EndEsacalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};
EndEsacalationEventInfo.prototype = new EscalationEventDefinitionInfo();
EndEsacalationEventInfo.prototype.constructor = EndEsacalationEventInfo;
function EndMessageEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndMessageEvent";
	this.name = "EndMessageEvent";
	this.infoClass = "EndMessageEventInfo";
};

EndMessageEvent.prototype = new ThrowEvent();
EndMessageEvent.prototype.constructor = EndMessageEvent;


function EndMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
};

EndMessageEventInfo.prototype = new MessageEventDefinitionInfo();
EndMessageEventInfo.prototype.constructor = EndMessageEventInfo;function EndMultipleEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndMultipleEvent";
	this.name = "EndMultipleEvent";
	this.infoClass = "EndMultipleEventInfo";
};

EndMultipleEvent.prototype = new ThrowEvent();
EndMultipleEvent.prototype.constructor = EndMultipleEvent;


function EndMultipleEventInfo(id)
{
	EventInfo.call(this, id);
};

EndMultipleEventInfo.prototype = new EventInfo();
EndMultipleEventInfo.prototype.constructor = EndMultipleEventInfo;function EndSignalEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndSignalEvent";
	this.name = "EndSignalEvent";
	this.infoClass = "EndSignalEventInfo";
};

EndSignalEvent.prototype = new ThrowEvent();
EndSignalEvent.prototype.constructor = EndSignalEvent;


function EndSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
};

EndSignalEventInfo.prototype = new SignalEventDefinitionInfo();
EndSignalEventInfo.prototype.constructor = EndSignalEventInfo;function EndTerminateEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "EndTerminateEvent";
	this.name = "EndTerminateEvent";
	this.infoClass = "EndTerminateEventInfo";
};

EndTerminateEvent.prototype = new ThrowEvent();
EndTerminateEvent.prototype.constructor = EndTerminateEvent;


function EndTerminateEventInfo(id)
{
	TerminateEventDefinitionInfo.call(this, id);
};

EndTerminateEventInfo.prototype = new TerminateEventDefinitionInfo();
EndTerminateEventInfo.prototype.constructor = EndTerminateEventInfo;/**
 * Class: ImplicitThrowEvent
 */
function ImplicitThrowEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "Implicit ThrowEvent";
};

ImplicitThrowEvent.prototype = new ThrowEvent();
ImplicitThrowEvent.prototype.constructor = ImplicitThrowEvent;



/**
 * Class: ImplicitThrowEventInfo
 */
function ImplicitThrowEventInfo(id)
{
	ThrowEventInfo.call(this, id);
};

ImplicitThrowEventInfo.prototype = new ThrowEventInfo();
ImplicitThrowEventInfo.prototype.constructor = ImplicitThrowEventInfo;

/**
 * Class: IntermediateThrowEvent
 */
function IntermediateThrowEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "Intermediate ThrowEvent";
	this.name = "IntermediateThrowEvent";
	this.infoClass = "IntermediateThrowEventInfo";
};

IntermediateThrowEvent.prototype = new ThrowEvent();
IntermediateThrowEvent.prototype.constructor = IntermediateThrowEvent;


/**
 * Class: IntermediateThrowEventInfo
 */
function IntermediateThrowEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateThrowEventInfo.prototype = new EventInfo();
IntermediateThrowEventInfo.prototype.constructor = IntermediateThrowEventInfo;function IntermediateThrowCompensationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowCompensationEvent";
	this.name = "ThrowCompensationEvent";
	this.infoClass = "IntermediateThrowCompensationEventInfo";
};

IntermediateThrowCompensationEvent.prototype = new ThrowEvent();
IntermediateThrowCompensationEvent.prototype.constructor = IntermediateThrowCompensationEvent;




function IntermediateThrowCompensationEventInfo(id)
{
	CompensateEventDefinitionInfo.call(this, id);
};

IntermediateThrowCompensationEventInfo.prototype = new CompensateEventDefinitionInfo();
IntermediateThrowCompensationEventInfo.prototype.constructor = IntermediateThrowCompensationEventInfo;function IntermediateThrowEscalationEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowEscalationEvent";
	this.name = "ThrowEscalationEvent";
	this.infoClass = "IntermediateThrowEscalationEventInfo";
};

IntermediateThrowEscalationEvent.prototype = new ThrowEvent();
IntermediateThrowEscalationEvent.prototype.constructor = IntermediateThrowEscalationEvent;




function IntermediateThrowEscalationEventInfo(id)
{
	EscalationEventDefinitionInfo.call(this, id);
};

IntermediateThrowEscalationEventInfo.prototype = new EscalationEventDefinitionInfo();
IntermediateThrowEscalationEventInfo.prototype.constructor = IntermediateThrowEscalationEventInfo;function IntermediateThrowLinkEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowLinkEvent";
	this.name = "ThrowLinkEvent";
	this.infoClass = "IntermediateThrowLinkEventInfo";
};

IntermediateThrowLinkEvent.prototype = new ThrowEvent();
IntermediateThrowLinkEvent.prototype.constructor = IntermediateThrowLinkEvent;



function IntermediateThrowLinkEventInfo(id)
{
	LinkEventDefinitionInfo.call(this, id);
};

IntermediateThrowLinkEventInfo.prototype = new LinkEventDefinitionInfo();
IntermediateThrowLinkEventInfo.prototype.constructor = IntermediateThrowLinkEventInfo;function IntermediateThrowMessageEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowMessageEvent";
	this.name = "ThrowMessageEvent";
	this.infoClass = "IntermediateThrowMessageEventInfo";
};

IntermediateThrowMessageEvent.prototype = new ThrowEvent();
IntermediateThrowMessageEvent.prototype.constructor = IntermediateThrowMessageEvent;




function IntermediateThrowMessageEventInfo(id)
{
	MessageEventDefinitionInfo.call(this, id);
};

IntermediateThrowMessageEventInfo.prototype = new MessageEventDefinitionInfo();
IntermediateThrowMessageEventInfo.prototype.constructor = IntermediateThrowMessageEventInfo;function IntermediateThrowMultipleEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowMultipleEvent";
	this.name = "ThrowMultipleEvent";
	this.infoClass = "IntermediateThrowMultipleEventInfo";
};

IntermediateThrowMultipleEvent.prototype = new ThrowEvent();
IntermediateThrowMultipleEvent.prototype.constructor = IntermediateThrowMultipleEvent;




function IntermediateThrowMultipleEventInfo(id)
{
	EventInfo.call(this, id);
};

IntermediateThrowMultipleEventInfo.prototype = new EventInfo();
IntermediateThrowMultipleEventInfo.prototype.constructor = IntermediateThrowMultipleEventInfo;function IntermediateThrowSignalEvent(id)
{
	ThrowEvent.call(this, id);
	this.label = "ThrowSignalEvent";
	this.name = "ThrowSignalEvent";
	this.infoClass = "IntermediateThrowSignalEventInfo";
};

IntermediateThrowSignalEvent.prototype = new ThrowEvent();
IntermediateThrowSignalEvent.prototype.constructor = IntermediateThrowSignalEvent;




function IntermediateThrowSignalEventInfo(id)
{
	SignalEventDefinitionInfo.call(this, id);
};

IntermediateThrowSignalEventInfo.prototype = new SignalEventDefinitionInfo();
IntermediateThrowSignalEventInfo.prototype.constructor = IntermediateThrowSignalEventInfo;/**
 * Class: Gateway
 */
function Gateway(id)
{
	FlowNode.call(this, id);
	this.defaultFlow = "";
	this.gatewayType ;
	this.gatewayDirection = null;
	this.label = "Gateway";
};

Gateway.prototype = new FlowNode();
Gateway.prototype.constructor = Gateway;


/**
 * Class: GatewayInfo
 */
function GatewayInfo(id)
{
	FlowNodeInfo.call(this, id);
    this.properties.push(new Property('gatewayDirection', 'General', false, 
		EditorType.combobox, {data:[{value:'Unspecified',text:mxResources.get('Unspecified')}, {value:'Converging',text:mxResources.get('Converging')}, {value:'Diverging',text:mxResources.get('Diverging')},{value:'Mixed',text:mxResources.get('Mixed')}]},'gatewayDirection','gatewayDirectionHints'));
};

GatewayInfo.prototype = new FlowNodeInfo();
GatewayInfo.prototype.constructor = GatewayInfo;

/**
 * Class: ComplexGateway
 */
function ComplexGateway(id)
{
	Gateway.call(this, id);
	this.name = "Complex Gateway"
	this.defaultSequenceFlow = "";
	this.activationCondition = "";
	this.label = "ComplexGateway";
	this.infoClass = "ComplexGatewayInfo";
};

ComplexGateway.prototype = new Gateway();
ComplexGateway.prototype.constructor = ComplexGateway;

/**
 * Class: ComplexGatewayInfo
 */
function ComplexGatewayInfo(id)
{
	GatewayInfo.call(this, id);
	this.properties.push(new Property('activationCondition', 'Main Config', false, 
		EditorType.text, '','activationCondition','activationConditionHints'));
};

ComplexGatewayInfo.prototype = new GatewayInfo();
ComplexGatewayInfo.prototype.constructor = ComplexGatewayInfo;

/**
 * Class: EventGateway
 */
function EventGateway(id)
{
	Gateway.call(this, id);
	this.instantiate ;
	this.eventGatewayType = "";
	this.label = "EventGateway";
};

EventGateway.prototype = new Gateway();
EventGateway.prototype.constructor = EventGateway;


/**
 * Class: EventGatewayInfo
 */
function EventGatewayInfo(id)
{
	GatewayInfo.call(this, id);
};

EventGatewayInfo.prototype = new GatewayInfo();
EventGatewayInfo.prototype.constructor = EventGatewayInfo;
/**
 * Class: EventBasedGateway
 */
function EventBasedGateway(id)
{
	Gateway.call(this, id);
	this.name = "EventBasedGateway";
	this.instantiate = false;
	this.eventGatewayType;
	this.label = "EventBasedGateway";
	this.infoClass = "EventBasedGatewayInfo";
};

EventBasedGateway.prototype = new Gateway();
EventBasedGateway.prototype.constructor = EventBasedGateway;



/**
 * Class: EventBasedGatewayInfo
 */
function EventBasedGatewayInfo(id)
{
	GatewayInfo.call(this, id);
    this.properties.push(new Property('instantiate', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'instantiate','instantiateHints'));
	this.properties.push(new Property('eventGatewayType', 'Main Config', false, 
		EditorType.combobox, {data:[{value:'Exclusive',text:mxResources.get('Exclusive')}, {value:'Parallel',text:mxResources.get('Parallel')}]},'eventGatewayType','eventGatewayTypeHints'));
};

EventBasedGatewayInfo.prototype = new GatewayInfo();
EventBasedGatewayInfo.prototype.constructor = EventBasedGatewayInfo;
/**
 * Class: ExclusiveGateway
 */
function ExclusiveGateway(id)
{
	Gateway.call(this, id);
	this.name = "Exclusive Gateway";
	this.defaultSequenceFlow ;
	this.label = "ExclusiveGateway";
	this.infoClass = "ExclusiveGatewayInfo";
};

ExclusiveGateway.prototype = new Gateway();
ExclusiveGateway.prototype.constructor = ExclusiveGateway;


/**
 * Class: ExclusiveGatewayInfo
 */
function ExclusiveGatewayInfo(id)
{
	GatewayInfo.call(this, id);
	this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.text, '','defaultSequenceFlow','defaultSequenceFlowHints'));
};

ExclusiveGatewayInfo.prototype = new GatewayInfo();
ExclusiveGatewayInfo.prototype.constructor = ExclusiveGatewayInfo;

/**
 * Class: InclusiveGateway
 */
function InclusiveGateway(id)
{
	Gateway.call(this, id);
	this.name = "Inclusive Gateway";
	this.defaultSequenceFlow ;
    this.label = "InclusiveGateway";
    this.infoClass = "InclusiveGatewayInfo";
};

InclusiveGateway.prototype = new Gateway();
InclusiveGateway.prototype.constructor = InclusiveGateway;



/**
 * Class: InclusiveGatewayInfo
 */
function InclusiveGatewayInfo(id)
{
	GatewayInfo.call(this, id);
    this.properties.push(new Property('defaultSequenceFlow', 'Main Config', false, 
		EditorType.text, '','defaultSequenceFlow','defaultSequenceFlowHints'));
};

InclusiveGatewayInfo.prototype = new GatewayInfo();
InclusiveGatewayInfo.prototype.constructor = InclusiveGatewayInfo;/**
 * Class: ParallelGateway
 */
function ParallelGateway(id)
{
	Gateway.call(this, id);
	this.name = "Parallel Gateway";
	this.label = "ParallelGateway";
	this.infoClass = "ParallelGatewayInfo";
	
};

ParallelGateway.prototype = new Gateway();
ParallelGateway.prototype.constructor = ParallelGateway;



/**
 * Class: ParallelGatewayInfo
 */
function ParallelGatewayInfo(id)
{
	GatewayInfo.call(this, id);
};

ParallelGatewayInfo.prototype = new GatewayInfo();
ParallelGatewayInfo.prototype.constructor = ParallelGatewayInfo;

/**
 * Class: FlowElementsContainer
 */
function FlowElementsContainer(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.flowElements = null;
	this.executionListeners = null;
	this.lanes = "";
	this.extensionElements = null;
	this.label = "name Listener";
};

FlowElementsContainer.prototype = new BaseElement();
FlowElementsContainer.prototype.constructor = FlowElementsContainer;

/**
 * Class: FlowElementsContainerInfo
 */
function FlowElementsContainerInfo(id)
{
	BaseElementInfo.call(this, id);
	this.properties.push(new Property('executionListeners','Listeners', false, EditorType.refbox, 
		{url:'./dialog/PropertyDialog/executionListeners.html',
			features:'dialogHeight:600px;dialogWidth:800px;center:yes;status:no'},'executionListeners','executionListenersHints'));
};

FlowElementsContainerInfo.prototype = new BaseElementInfo();
FlowElementsContainerInfo.prototype.constructor = FlowElementsContainerInfo;




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
/**
 * Class: FormProperty
 */
function FormProperty(id)
{
	BaseElement.call(this, id);
	this.id="";
	this.name="";
	this.type= "";
	this.value="";
	this.expression="" ;
	this.variable = "";
	this.defaultExpression= "";
	this.pattern= "" ;
	this.required=false;
	this.readable= false ;
	this.writeable=false;
	this.formValues=null;
	this.label= "FormProperty";
};

FormProperty.prototype = new BaseElement();
FormProperty.prototype.constructor = FormProperty;

/**
 * Class: FormPropertyInfo
 */
function FormPropertyInfo(id)
{
	BaseElementInfo.call(this, id);
};

FormPropertyInfo.prototype = new BaseElementInfo();
FormPropertyInfo.prototype.constructor = FormPropertyInfo;function FormValue(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "FormValue";
};

FormValue.prototype = new BaseElement();
FormValue.prototype.constructor = FormValue;


/**
 * Class: FormValueInfo
 */
function FormValueInfo(id)
{
	BaseElementInfo.call(this, id);
};

FormValueInfo.prototype = new BaseElementInfo();
FormValueInfo.prototype.constructor = FormValueInfo;
/**
 * Class: ItemAwareElement
 */
function ItemAwareElement(id)
{
	BaseElement.call(this, id);
	this.itemSubjectRef = [];
	this.dataState = "";
	this.label = "ItemAwareElement";
};

ItemAwareElement.prototype = new BaseElement();
ItemAwareElement.prototype.constructor = ItemAwareElement;


/**
 * Class: ItemAwareElementInfo
 */
function ItemAwareElementInfo(id)
{
	BaseElementInfo.call(this, id);
};

ItemAwareElementInfo.prototype = new BaseElementInfo();
ItemAwareElementInfo.prototype.constructor = ItemAwareElementInfo;
function MessageRef(id)
{
	BaseElement.call(this, id);
	this.label = "MessageRef";
};

MessageRef.prototype = new BaseElement();
MessageRef.prototype.constructor = MessageRef;



/**
 * Class: OperationRef
 */
function OperationRef(id)
{
	BaseElement.call(this, id);
	this.name="";
	this.label="OperationRef";
};

OperationRef.prototype = new BaseElement();
OperationRef.prototype.constructor = OperationRef;




function Participant(id)
{
	BaseElement.call(this, id);
	this.name = "Participant";
	this.processRef = "";
	this.participanTable =null;
	this.label = "Participant";
};

Participant.prototype = new BaseElement();
Participant.prototype.constructor = Participant;

/**
 * Class:ParticipantInfo
 */
function ParticipantInfo(id)
{
	BaseElementInfo.call(this, id);
};

ParticipantInfo.prototype = new BaseElementInfo();
ParticipantInfo.prototype.constructor = ParticipantInfo;/**
 * Class: Pool
 */
function Pool(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.processRef = "";
	this.name = "Process";
	this.label = "Process";
	this.infoClass = "PoolInfo";
};

Pool.prototype = new BaseElement();
Pool.prototype.constructor = Pool;



/**
 * Class: PoolInfo
 */
function PoolInfo(id)
{
	BaseElementInfo.call(this, id);
};

PoolInfo.prototype = new BaseElementInfo();
PoolInfo.prototype.constructor = PoolInfo;

function Signal(id)
{
	BaseElement.call(this, id);
	this.name = "";
	this.label = "Signal";
};

Signal.prototype = new BaseElement();
Signal.prototype.constructor = Signal;



/**
 * Class: SignalInfo
 */
function SignalInfo(id)
{
	BaseElementInfo.call(this, id);
};

SignalInfo.prototype = new BaseElementInfo();
SignalInfo.prototype.constructor = SignalInfo;
