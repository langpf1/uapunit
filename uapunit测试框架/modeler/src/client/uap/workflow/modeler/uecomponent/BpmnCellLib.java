package uap.workflow.modeler.uecomponent;

import nc.uitheme.ui.ThemeResourceCenter;


public class BpmnCellLib extends DefaultCellLib {
	
	
	private static final String current_themere =ThemeResourceCenter.getInstance().getCurrTheme().getCode();
	
	private static final String PATH_ICON="/themeroot/"+current_themere+"/themeres/control/workflow/icons/";
	
	private static final String PNG_SUFFIX=".png";
	
	private static final String GIF_SUFFIX=".gif";
	
	//ICON
	//Task
	public static final String ICON_PALETTE_TASK =PATH_ICON+"activity/task"+PNG_SUFFIX;;
	public static final String ICON_PALETTE_USERTASK =PATH_ICON+"activity/list/type.user"+PNG_SUFFIX;;
	public static final String ICON_PALETTE_SCRIPTTASK=PATH_ICON+"activity/list/type.script"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SERVICETASK=PATH_ICON+"activity/list/type.service"+PNG_SUFFIX;
	public static final String ICON_PALETTE_MAILTASK=PATH_ICON+"activity/list/type.send"+PNG_SUFFIX;
	public static final String ICON_PALETTE_MANUALTASK=PATH_ICON+"activity/list/type.manual"+PNG_SUFFIX;
	public static final String ICON_PALETTE_RECEIVETASK=PATH_ICON+"activity/list/type.receive"+PNG_SUFFIX;
	public static final String ICON_PALETTE_BUSINESSRULETASK=PATH_ICON+"activity/list/type.business.rule"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CALLACTIVITYTTASK=PATH_ICON+"activity/subprocess"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SUBPROCESS =PATH_ICON+"activity/list/expanded.subprocess"+PNG_SUFFIX;	
	public static final String ICON_PALETTE_EVENT_SUPPROCESSCOLLAPSED=PATH_ICON+"activity/event.subprocess.collapsed"+PNG_SUFFIX;
	public static final String ICON_PALETTE_EVENT_SUPPROCESS =PATH_ICON+"activity/list/event.subprocess"+PNG_SUFFIX;	
	//Start Event
	public static final String ICON_PALETTE_STARTEVENT_NONE=PATH_ICON+"startevent/none"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_SIGNEL=PATH_ICON+"startevent/signal"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_TIMER=PATH_ICON+"startevent/timer"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_COMPENSATION=PATH_ICON+"startevent/compensation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_ERROR=PATH_ICON+"startevent/error"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_ESCALATION=PATH_ICON+"startevent/escalation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_MESSAGE=PATH_ICON+"startevent/message"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_MULTIPLE=PATH_ICON+"startevent/multiple"+PNG_SUFFIX;
	public static final String ICON_PALETTE_STARTEVENT_CONDITIONAL=PATH_ICON+"startevent/conditional"+PNG_SUFFIX;
	//End Event
	public static final String ICON_PALETTE_ENDEVENT_NONE=PATH_ICON+"endevent/none"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_CANCEL=PATH_ICON+"endevent/cancel"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_COMPENSATION=PATH_ICON+"endevent/compensation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_ERROR=PATH_ICON+"endevent/error"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_ESCALATION=PATH_ICON+"endevent/escalation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_MESSAGE=PATH_ICON+"endevent/message"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_MULTIPLE=PATH_ICON+"endevent/multiple"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_SIGNAL=PATH_ICON+"endevent/signal"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ENDEVENT_TERMINATE=PATH_ICON+"endevent/terminate"+PNG_SUFFIX;
	//Connection
	public static final String ICON_PALETTE_SEQUENCEFLOW_DEFAULT=PATH_ICON+"connector/sequenceflow"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SEQUENCEFLOW_MESSAGEFLOW=PATH_ICON+"connector/messageflow"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SEQUENCEFLOW_ASSOCIATION=PATH_ICON+"connector/association.undirected"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SEQUENCEFLOW_DATAASSOCIATIONINPUT=PATH_ICON+"connector/association.unidirectional"+PNG_SUFFIX;
	//Select
	public static final String ICON_PALETTE_SELECT=PATH_ICON+"select"+PNG_SUFFIX;
	//Gateway
	public static final String ICON_PALETTE_PARALLELGATEWAY =PATH_ICON+"gateway/parallel"+PNG_SUFFIX;
	public static final String ICON_PALETTE_EXCLUSIVEGATEWAY =PATH_ICON+"gateway/exclusive.databased"+PNG_SUFFIX;
	public static final String ICON_PALETTE_INCLUSIVEGATEWAY =PATH_ICON+"gateway/inclusive"+PNG_SUFFIX;
	public static final String ICON_PALETTE_EVENTGATEWAY_EVENTBASED =PATH_ICON+"gateway/eventbased"+PNG_SUFFIX;
	public static final String ICON_PALETTE_EVENTGATEWAY_COMPLEX =PATH_ICON+"gateway/complex"+PNG_SUFFIX;
	//Artifacts
	public static final String ICON_PALETTE_ARTIFACTS_GROUP =PATH_ICON+"artifact/group"+PNG_SUFFIX;
	public static final String ICON_PALETTE_ARTIFACTS_ANNOTATION=PATH_ICON+"artifact/text.annotation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SWIMLANE_LANE =PATH_ICON+"swimlane/lane"+PNG_SUFFIX;
	public static final String ICON_PALETTE_SWIMLANE_POOL =PATH_ICON+"swimlane/pool"+PNG_SUFFIX;
	//Throwing
	public static final String ICON_PALETTE_THROWING_NONE =PATH_ICON+"throwing/none"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_COMPENSATION =PATH_ICON+"throwing/compensation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_ESCALATION =PATH_ICON+"throwing/escalation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_LINK =PATH_ICON+"throwing/link"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_MESSAGE =PATH_ICON+"throwing/message"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_MULTIPLE =PATH_ICON+"throwing/multiple"+PNG_SUFFIX;
	public static final String ICON_PALETTE_THROWING_SIGNAL =PATH_ICON+"throwing/signal"+PNG_SUFFIX;
	//Catching
	public static final String ICON_PALETTE_CATCHING_CANCEL =PATH_ICON+"catching/cancel"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_COMPENSATION =PATH_ICON+"catching/compensation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_CONDITIONAL =PATH_ICON+"catching/conditional"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_ERROR =PATH_ICON+"catching/error"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_ESCALATION =PATH_ICON+"catching/escalation"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_LINK =PATH_ICON+"catching/link"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_MESSAGE =PATH_ICON+"catching/message"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_MULTIPARALLEL =PATH_ICON+"catching/multiple.parallel"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_MULTIPLE =PATH_ICON+"catching/multiple"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_SIGNAL =PATH_ICON+"catching/signal"+PNG_SUFFIX;
	public static final String ICON_PALETTE_CATCHING_TIMER =PATH_ICON+"catching/timer"+PNG_SUFFIX;
	
	public static final String ICON_PALETTE_DATA_NONE=PATH_ICON+"data/data"+PNG_SUFFIX;
	
	public static final String ICON_NOTATION_EVENT_CANCEL = PATH_ICON+"activity/list/event.cancel"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_COMPENSATION = PATH_ICON+"activity/list/event.compensation"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_CONDITIONAL = PATH_ICON+"activity/list/event.conditional"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_ERROR = PATH_ICON+"activity/list/event.error"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_ESCALATION = PATH_ICON+"activity/list/event.escalation"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_MESSAGE = PATH_ICON+"activity/list/event.message"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_MULTIPARALLEL = PATH_ICON+"activity/list/event.multiple.parallel"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_SIGNAL = PATH_ICON+"activity/list/event.signal"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_MULTIPLE= PATH_ICON+"activity/list/event.multiple"+PNG_SUFFIX;
	public static final String ICON_NOTATION_EVENT_TIMER = PATH_ICON+"activity/list/event.timer"+PNG_SUFFIX;
	
	public static final String ICON_NOTATION_ENDEVENT_COMPENSATION = PATH_ICON+"endevent/inner/endevent.compensation"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_CANCEL = PATH_ICON+"endevent/inner/endevent.cancel"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_ERROR = PATH_ICON+"endevent/inner/endevent.error"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_ESCALATION = PATH_ICON+"endevent/inner/endevent.escalation"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_MESSAGE = PATH_ICON+"endevent/inner/endevent.message"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_MULTIPLE = PATH_ICON+"endevent/inner/endevent.multiple"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_SIGNAL = PATH_ICON+"endevent/inner/endevent.signal"+PNG_SUFFIX;
	public static final String ICON_NOTATION_ENDEVENT_TERMINATE= PATH_ICON+"endevent/inner/endevent.terminate"+PNG_SUFFIX;
	
	public static final String ICON_NOTATION_MISC_LOOPSTYLE = PATH_ICON+"activity/list/looptype.standard"+PNG_SUFFIX;
	public static final String ICON_NOTATION_MISC_MIPARALLEL = PATH_ICON+"activity/list/mi.parallel"+PNG_SUFFIX;
	public static final String ICON_NOTATION_MISC_MISEQUENTIAL = PATH_ICON+"activity/list/mi.sequential"+PNG_SUFFIX;
	public static final String ICON_NOTATION_CONNECTION_DEFAULT = PATH_ICON+"connector/list/type.default"+PNG_SUFFIX;
	public static final String ICON_NOTATION_CONNECTION_EXPRESSION = PATH_ICON+"connector/list/type.expression"+PNG_SUFFIX;
	public static final String ICON_NOTATION_CONVERSATIONLINK = PATH_ICON+"conversations/connector/conversationlink"+PNG_SUFFIX;
	public static final String ICON_NOTATION_GATEWAY_EXCLUSIVE = PATH_ICON+"gateway/list/eventbased.exclusive"+PNG_SUFFIX;
	public static final String ICON_NOTATION_GATEWAY_PARALLEL = PATH_ICON+"gateway/list/eventbased.parallel"+PNG_SUFFIX;
	public static final String ICON_NOTATION_GATEWAY_INCLUSIVE = PATH_ICON+"gateway/list/eventbased.inclusive"+PNG_SUFFIX;
	public static final String ICON_NOTATION_GATEWAY_COMPLEX = PATH_ICON+"gateway/list/eventbased.complex"+PNG_SUFFIX;
	public static final String ICON_NOTATION_GATEWAY_EVENTBASED = PATH_ICON+"gateway/list/eventbased"+PNG_SUFFIX;
	
	public static final String ICON_NOTATION_CATCHING_LINK =PATH_ICON+"catching/list/link"+PNG_SUFFIX;
	public static final String ICON_NOTATION_THROWING_LINK =PATH_ICON+"throwing/list/link"+PNG_SUFFIX;
	public static final String ICON_NOTATION_DATAOBJECT_INPUT = PATH_ICON+"dataobject/list/input"+PNG_SUFFIX;
	public static final String ICON_NOTATION_DATAOBJECT_OUTPUT = PATH_ICON+"dataobject/list/output"+PNG_SUFFIX;
	
	//字体信息
	public static final String FONT_TYPE=";fontFamily=宋体;fontSize=12";
	
	//图片位置信息
	public static final String LOCATION_IMAGE_TEXT=";imageVerticalAlign=top;verticalAlign=middle;verticalLabelPosition=middle";
	public static final String LOCATION_IMAGE_BOTTOM=";imageVerticalAlign=bottom;imageAlign=center;verticalAlign=middle;verticalLabelPosition=middle";
	public static final String LOCATION_IMAGE_MIDDLE=";imageVerticalAlign=middle;imageAlign=center;verticalAlign=middle;verticalLabelPosition=bottom;noLabel=1";
	public static final String COLOR_STARTEVENT =";fillColor=#8CB032;gradientColor=#CCFF00;";
	public static final String COLOR_ENDEVENT =";fillColor=#EE3D3D;gradientColor=#FF9999;";
	public static final String COLOR_INTERMEDIATIONEVENT =";fillColor=#FF9900;gradientColor=#EEEC3E;";
	public static final String COLOR_TASK =";fillColor=#1496F7;gradientColor=#AAD9F1;";
	public static final String COLOR_CONTAINER =";fillColor=#BDBABA;";
	public static final String COLOR_GATEWAY =";fillColor=#FFFF00;";
	public static final String FILLCOLOR_SWIM =";lableColor=#BDBABA;";
	public static final String LABELCOLOR_SWIM =";fillColor=#9A9393;";

	
	public static final String STYLE_DEFAULT=FONT_TYPE+LOCATION_IMAGE_TEXT;
	public static final String STYLE_IMAGE_BOTTOM=FONT_TYPE+LOCATION_IMAGE_BOTTOM;
	public static final String STYLE_IMAGE_MIDDLE=FONT_TYPE+LOCATION_IMAGE_MIDDLE;
	
	public static final String STYLE_PROCESS =FONT_TYPE+";imageVerticalAlign=top;imageAlign=right;verticalAlign=top;verticalLabelPosition=bottom";
	public static final String STYLE_LANE =FONT_TYPE+";imageVerticalAlign=top;imageAlign=right;verticalAlign=top;verticalLabelPosition=bottom";
	public static final String STYLE_POOL =FONT_TYPE+";verticalAlign=top;verticalLabelPosition=middle;labelPosition=middle";

	public static final String NOTATION_SELECT = "Select";
	public static final String NOTATION_ACTIVITY = "Activity";
	public static final String NOTATION_GATEWAY = "Gateway";
	public static final String NOTATION_STARTEVENT = "StartEvent";
	public static final String NOTATION_ENDEVENT = "EndEvent";
	public static final String NOTATION_INTERMEDIATIONTHROWING = "IntermediationThrowing";
	public static final String NOTATION_INTERMEDIATIONCATCHING = "IntermediationCatching";
	public static final String NOTATION_CONNECTION = "Connection";
	public static final String NOTATION_ARTIFACTS = "Artifacts and swimlane";
	public static final String NOTATION_ALL = "All";
	
	private String[][] shapeCells =new String[][]{new String[]
	    {"Select",ICON_PALETTE_SELECT,"image;image="+ICON_PALETTE_SELECT,"0","0","","","Y",NOTATION_SELECT},
	    
		{"Start Event",ICON_PALETTE_STARTEVENT_NONE, "shape=event;"+COLOR_STARTEVENT+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","", "Y", NOTATION_STARTEVENT},
		{"Start Event Compensation",ICON_PALETTE_STARTEVENT_COMPENSATION, "shape=event;"+COLOR_STARTEVENT+"imageAlign=center;perimeter=ellipsePerimeter;image="+ICON_NOTATION_EVENT_COMPENSATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.CompensateEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Conditional",ICON_PALETTE_STARTEVENT_CONDITIONAL, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_CONDITIONAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.ConditionalEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Error",ICON_PALETTE_STARTEVENT_ERROR, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_ERROR+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.ErrorEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Escalation",ICON_PALETTE_STARTEVENT_ESCALATION, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_ESCALATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.EscalationEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Message",ICON_PALETTE_STARTEVENT_MESSAGE, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_MESSAGE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.MessageEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Multiple",ICON_PALETTE_STARTEVENT_MULTIPLE, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_MULTIPLE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_STARTEVENT},
		//{"Start Event MultiParallel",ICON_PALETTE_STARTEVENT_MULTIPLE, "shape=event;image="+ICON_NOTATION_EVENT_MULTIPARALLEL+FONT_TYPE, "36", "36", "uap.workflow.modeler.bpmn2.model.StartEvent","uap.workflow.modeler.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Signal",ICON_PALETTE_STARTEVENT_SIGNEL, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_SIGNAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.SignalEventDefinition", "Y", NOTATION_STARTEVENT},
		{"Start Event Timer",ICON_PALETTE_STARTEVENT_TIMER, "shape=event;"+COLOR_STARTEVENT+"image="+ICON_NOTATION_EVENT_TIMER+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.StartEvent","uap.workflow.bpmn2.model.definition.TimerEventDefinition", "Y", NOTATION_STARTEVENT},
		
        {"User Task",ICON_PALETTE_USERTASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_USERTASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.UserTask","", "Y" ,NOTATION_ACTIVITY},
        {"Send Task",ICON_PALETTE_MAILTASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_MAILTASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.SendTask","", "Y" ,NOTATION_ACTIVITY},
        {"Receive Task",ICON_PALETTE_RECEIVETASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_RECEIVETASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.ReceiveTask","", "Y" ,NOTATION_ACTIVITY},
        {"Manual Task",ICON_PALETTE_MANUALTASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_MANUALTASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.ManualTask","", "Y" ,NOTATION_ACTIVITY},
        {"Service Task",ICON_PALETTE_SERVICETASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_SERVICETASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.ServiceTask","", "Y" ,NOTATION_ACTIVITY},
        {"Script Task",ICON_PALETTE_SCRIPTTASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_SCRIPTTASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.ScriptTask","", "Y" ,NOTATION_ACTIVITY},
        {"BusinessRule Task",ICON_PALETTE_BUSINESSRULETASK, "shape=task;"+COLOR_TASK+"image="+ICON_PALETTE_BUSINESSRULETASK+STYLE_DEFAULT+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.BusinessRuleTask","", "Y" ,NOTATION_ACTIVITY},
        {"Call Activity",ICON_PALETTE_CALLACTIVITYTTASK, "shape=task;"+COLOR_TASK+"image="+ICON_NOTATION_EVENT_MULTIPARALLEL+STYLE_IMAGE_BOTTOM+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.CallActivity","", "Y" ,NOTATION_ACTIVITY},
		{"Sub-Process",ICON_PALETTE_SUBPROCESS,"shape=subprocess;"+COLOR_CONTAINER+"image="+ICON_PALETTE_SUBPROCESS+STYLE_PROCESS,"360", "240", "uap.workflow.bpmn2.model.SubProcess","", "Y", NOTATION_ACTIVITY},
        {"Event Subprocess Collapsed",ICON_PALETTE_EVENT_SUPPROCESSCOLLAPSED, "shape=task;"+COLOR_TASK+"dashed=true;image="+ICON_NOTATION_EVENT_MULTIPARALLEL+STYLE_IMAGE_BOTTOM+";shadow=1;rounded=1", "90", "55", "uap.workflow.bpmn2.model.CallActivity","", "Y" ,NOTATION_ACTIVITY},
		{"Event Subprocess",ICON_PALETTE_EVENT_SUPPROCESS,"shape=subprocess;"+COLOR_CONTAINER+"dashed=true;image="+ICON_PALETTE_EVENT_SUPPROCESS+STYLE_PROCESS,"320", "220", "uap.workflow.bpmn2.model.SubProcess","", "Y", NOTATION_ACTIVITY},
		
		{"Parallel Gateway",ICON_PALETTE_PARALLELGATEWAY,"shape=gateway;"+COLOR_GATEWAY+"image="+ICON_NOTATION_GATEWAY_PARALLEL+STYLE_IMAGE_MIDDLE,"50","50","uap.workflow.bpmn2.model.ParallelGateway","","Y",NOTATION_GATEWAY},		
		{"Inclusive Gateway",ICON_PALETTE_INCLUSIVEGATEWAY,"shape=gateway;"+COLOR_GATEWAY+"image="+ICON_NOTATION_GATEWAY_INCLUSIVE+STYLE_IMAGE_MIDDLE,"50","50","uap.workflow.bpmn2.model.InclusiveGateway","","Y",NOTATION_GATEWAY},		
		{"Exclusive Gateway",ICON_PALETTE_EXCLUSIVEGATEWAY,"shape=gateway;"+COLOR_GATEWAY+"image="+ICON_NOTATION_GATEWAY_EXCLUSIVE+STYLE_IMAGE_MIDDLE,"50","50","uap.workflow.bpmn2.model.ExclusiveGateway","","Y",NOTATION_GATEWAY},		
		{"Complex Gateway",ICON_PALETTE_EVENTGATEWAY_COMPLEX,"shape=gateway;"+COLOR_GATEWAY+"image="+ICON_NOTATION_GATEWAY_COMPLEX+STYLE_IMAGE_MIDDLE,"50","50","uap.workflow.bpmn2.model.ComplexGateway","","Y",NOTATION_GATEWAY},		
		{"EventBased Gateway",ICON_PALETTE_EVENTGATEWAY_EVENTBASED,"shape=gateway;"+COLOR_GATEWAY+"image="+ICON_NOTATION_GATEWAY_EVENTBASED+STYLE_IMAGE_MIDDLE,"50","50","uap.workflow.bpmn2.model.EventGateway","","Y",NOTATION_GATEWAY},		
		
		{"End Event",ICON_PALETTE_ENDEVENT_NONE, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;imgxor=true;"+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","", "Y", NOTATION_ENDEVENT},
		{"End Event Cancel",ICON_PALETTE_ENDEVENT_CANCEL, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_CANCEL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Compensation",ICON_PALETTE_ENDEVENT_COMPENSATION, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_COMPENSATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.CompensateEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Error",ICON_PALETTE_ENDEVENT_ERROR, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_ERROR+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.ErrorEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Escalation",ICON_PALETTE_ENDEVENT_ESCALATION, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_ESCALATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.EscalationEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Message",ICON_PALETTE_ENDEVENT_MESSAGE, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_MESSAGE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.MessageEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Multiple",ICON_PALETTE_ENDEVENT_MULTIPLE, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_MULTIPLE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Signal",ICON_PALETTE_ENDEVENT_SIGNAL, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_SIGNAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.SignalEventDefinition", "Y", NOTATION_ENDEVENT},
		{"End Event Terminate",ICON_PALETTE_ENDEVENT_TERMINATE, "shape=event;"+COLOR_ENDEVENT+"strokeWidth=3;image="+ICON_NOTATION_ENDEVENT_TERMINATE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.EndEvent","uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_ENDEVENT},

		{"Throw None",ICON_PALETTE_THROWING_NONE, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"imgxor=true;image="+ICON_PALETTE_THROWING_NONE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Compensation",ICON_PALETTE_THROWING_COMPENSATION, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"perimeter=ellipsePerimeter;image="+ICON_NOTATION_ENDEVENT_COMPENSATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.CompensateEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Escalation",ICON_PALETTE_THROWING_ESCALATION, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_ENDEVENT_ESCALATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.EscalationEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Link",ICON_PALETTE_THROWING_LINK, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_THROWING_LINK+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.LinkEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Message",ICON_PALETTE_THROWING_MESSAGE, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_ENDEVENT_MESSAGE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.MessageEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Multiple",ICON_PALETTE_THROWING_MULTIPLE, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_ENDEVENT_MULTIPLE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},
		{"Throw Signal",ICON_PALETTE_THROWING_SIGNAL, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_ENDEVENT_SIGNAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateThrowEvent","uap.workflow.bpmn2.model.definition.SignalEventDefinition", "Y", NOTATION_INTERMEDIATIONTHROWING},

		{"Catch Cancel",ICON_PALETTE_CATCHING_CANCEL, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_CANCEL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Compensation",ICON_PALETTE_CATCHING_COMPENSATION, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_COMPENSATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.CompensateEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Conditional",ICON_PALETTE_CATCHING_CONDITIONAL, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_CONDITIONAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.ConditionalEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Error",ICON_PALETTE_CATCHING_ERROR, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_ERROR+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.ErrorEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Escalation",ICON_PALETTE_CATCHING_ESCALATION, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_ESCALATION+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.EscalationEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Link",ICON_PALETTE_CATCHING_LINK, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_CATCHING_LINK+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.LinkEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Message",ICON_PALETTE_CATCHING_MESSAGE, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_MESSAGE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.MessageEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch MultiParallel",ICON_PALETTE_CATCHING_MULTIPARALLEL, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_MULTIPARALLEL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Multiple",ICON_PALETTE_CATCHING_MULTIPLE, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_MULTIPLE+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.CancelEventDefinition,uap.workflow.bpmn2.model.definition.TerminateEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Signal",ICON_PALETTE_CATCHING_SIGNAL, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_SIGNAL+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.SignalEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},
		{"Catch Timer",ICON_PALETTE_CATCHING_TIMER, "shape=intermediaEvent;"+COLOR_INTERMEDIATIONEVENT+"image="+ICON_NOTATION_EVENT_TIMER+STYLE_IMAGE_MIDDLE, "36", "36", "uap.workflow.bpmn2.model.event.IntermediateCatchEvent","uap.workflow.bpmn2.model.definition.TimerEventDefinition", "Y", NOTATION_INTERMEDIATIONCATCHING},

		{"Annotation",ICON_PALETTE_ARTIFACTS_ANNOTATION, "shape=annotation;whiteSpace=wrap;dashed=true;fillColor=none;align=left;verticalAlign=top;edgeStyle=0x0E;"+FONT_TYPE, "90", "55", "uap.workflow.bpmn2.model.Annotation","", "Y", NOTATION_ARTIFACTS},
		{"Group",ICON_PALETTE_ARTIFACTS_GROUP, "shape=rectangle;fillColor=none;dashed=true;rounded=1;align=left;verticalAlign=top", "300", "180", "uap.workflow.bpmn2.model.Group","", "Y", NOTATION_ARTIFACTS},
		{"Process",ICON_PALETTE_SWIMLANE_POOL, "shape=swimlane;"+LABELCOLOR_SWIM+"horizontal=false"+FILLCOLOR_SWIM+STYLE_POOL+";spacingTop=10;","300", "180", "uap.workflow.bpmn2.model.Pool","", "Y", NOTATION_ARTIFACTS},
		{"Participant",ICON_PALETTE_SWIMLANE_LANE, "shape=swimlane;"+LABELCOLOR_SWIM+"horizontal=false"+FILLCOLOR_SWIM+STYLE_POOL+";spacingTop=10;","260", "180", "uap.workflow.bpmn2.model.Lane","", "Y", NOTATION_ARTIFACTS},
		
		{"DataObject",ICON_PALETTE_DATA_NONE, "shape=dataObject;"+STYLE_DEFAULT+";shadow=1", "45", "60", "uap.workflow.bpmn2.model.DataObject","", "Y", NOTATION_ARTIFACTS},
		
		{"Sequence Flow",ICON_PALETTE_SEQUENCEFLOW_DEFAULT, "connector;startArrow=hadCondition;endArrow=block;strokeWidth=1.2;noLabel=1;"+FONT_TYPE, "100", "100", "uap.workflow.bpmn2.model.SequenceFlow","", "N", NOTATION_CONNECTION},
		{"Message Flow",ICON_PALETTE_SEQUENCEFLOW_MESSAGEFLOW, "connector;dashed=true;dashPattern=10,2;startArrow=none;endArrow=block;noLabel=1;"+FONT_TYPE, "100", "100", "uap.workflow.bpmn2.model.MessageFlow","", "N", NOTATION_CONNECTION},
		{"Association",ICON_PALETTE_SEQUENCEFLOW_ASSOCIATION, "connector;startArrow=none;dashed=true;dashPattern=2,4;endArrow=none;noLabel=1;"+FONT_TYPE, "100", "100", "uap.workflow.bpmn2.model.Association","", "N", NOTATION_CONNECTION},
		{"Data Association",ICON_PALETTE_SEQUENCEFLOW_DATAASSOCIATIONINPUT, "connector;startArrow=none;dashed=true;dashPattern=2,2;endArrow=open;noLabel=1;"+FONT_TYPE, "100", "100", "uap.workflow.bpmn2.model.Association","", "N", NOTATION_CONNECTION}};
	
	public String[][] getImageCellDesriptor() {
		return super.getImageCellDesriptor();
	}

	public String[][] getShapeCellDesriptor() {
		return shapeCells;
	}

	public String[][] getSymbolsCellDesriptor() {
		return super.getSymbolsCellDesriptor();
	}
	

}
