package uap.workflow.engine.core;
/**
 * ����XPDL�����е�һЩ��ǳ�����������չ����
 * 
 * @author �׾� created on 2004-4-19
 */
public interface XPDLNames {
	/** NameSpace prefix to use for XSD elements. */
	String XSD_NS_PREFIX = "xsd";
	/** The XSD namespace URI. */
	String XSD_URI = "http://www.w3.org/2000/10/XMLSchema";
	String XSI_SCHEMA_LOCATION = "schemaLocation";
	/** NameSpace prefix to use for XPDL elements. */
	String XPDL_NS_PREFIX = "xpdl";
	/** The XPDL namespace URI. */
	String XPDL_URI = "http://www.wfmc.org/2002/XPDL1.0";
	/** The XPDL schema URI. */
	String XPDL_SCHEMA_LOCATION = "http://www.wfmc.org/2002/XPDL1.0 xpdl-1.0.xsd";
	/** NameSpace prefix to use for UFW extensions. */
	String UFW_NS_PREFIX = "ufw";
	/** The UFW namespace URI. */
	String UFW_URI = "http://www.ufsoft.com/2003/UFW1.0";
	/** Unique identifier. */
	String ID = "Id";
	/** �˹����������� */
	String ORGANIZETRANSFERTYPE = "OrganizeTransferType";
	/** �˹����������� */
	String ORGANIZETRANSFEROBJ = "OrganizeTransferObj";
	/** Entity name. */
	String NAME = "Name";
	/** Tag which defines a brief description of an element. */
	String DESCRIPTION = "Description";
	String EXTENDED_ATTRIBUTES = "ExtendedAttributes";
	String EXTENDED_ATTRIBUTE = "ExtendedAttribute";
	String VALUE = "Value";
	/** Tag which identifies a documentation URL. */
	String DOCUMENTATION = "Documentation";
	/** Tag which identifies an icon URL. */
	String ICON = "Icon";
	/** The main enclosing tag which defines a complete package. */
	String PACKAGE = "Package";
	String PACKAGE_HEADER = "PackageHeader";
	String REDEFINABLE_HEADER = "RedefinableHeader";
	String CONFORMANCE_CLASS = "ConformanceClass";
	String GRAPH_CONFORMANCE = "GraphConformance";
	/** Tag which identifies the XPDL version. */
	String XPDL_VERSION = "XPDLVersion";
	/** Tag which identifiers the workflow designer vendor. */
	String VENDOR = "Vendor";
	/** Tag which represents a creation date. */
	String CREATED = "Created";
	/** Tag which represents an entity author. */
	String AUTHOR = "Author";
	/** Tag which represents an entity version. */
	String VERSION = "Version";
	String CODEPAGE = "Codepage";
	String COUNTRYKEY = "Countrykey";
	String PUBLICATION_STATUS = "PublicationStatus";
	String PARTICIPANTS = "Participants";
	String PARTICIPANT = "Participant";
	String PARTICIPANT_TYPE = "ParticipantType";
	String UFW_EVENT = "ufw:Event";
	String UFW_EVENT_TYPES = "ufw:EventTypes";
	String EVENT_TYPES = "EventTypes";
	String EVENT_TYPE = "EventType";
	String EVENT = "Event";
	String APPLICATIONS = "Applications";
	String APPLICATION = "Application";
	String APPLICATION_ARGS = "ApplicationArgs";
	String WORKFLOW_PROCESSES = "WorkflowProcesses";
	String WORKFLOW_PROCESS = "WorkflowProcess";
	String ACCESS_LEVEL = "AccessLevel";
	String PROCESS_HEADER = "ProcessHeader";
	String EXTERNAL_PACKAGES = "ExternalPackages";
	String EXTERNAL_PACKAGE = "ExternalPackage";
	String HREF = "href";
	String PRIORITY = "Priority";
	String PRIORITY_UNIT = "PriorityUnit";
	String COST = "Cost";
	String COST_UNIT = "CostUnit";
	String RESPONSIBLES = "Responsibles";
	String RESPONSIBLE = "Responsible";
	String LIMIT = "Limit";
	String VALID_FROM = "ValidFrom";
	String VALID_TO = "ValidTo";
	String TIME_ESTIMATION = "TimeEstimation";
	String DURATION = "Duration";
	String DURATION_UNIT = "DurationUnit";
	String SIMULATION_INFORMATION = "SimulationInformation";
	String INSTANTIATION_TYPE = "InstantiationType";
	String WAITING_TIME = "WaitingTime";
	String WORKING_TIME = "WorkingTime";
	String ACTIVITIES = "Activities";
	String ACTIVITY = "Activity";
	String ACTIVITY_SET = "ActivitySet";
	String ACTIVITY_SETS = "ActivitySets";
	String SUBFLOW = "SubFlow";
	String TOOL = "Tool";
	String NO = "No";
	String UFW_LOOP = "ufw:Loop";
	String LOOP = "Loop";
	String WHILE = "While";
	String UNTIL = "Until";
	String FOR_EACH = "ForEach";
	String IN = "In";
	String EXECUTION = "Execution";
	String FORMAL_PARAMETERS = "FormalParameters";
	String FORMAL_PARAMETER = "FormalParameter";
	String INDEX = "Index";
	String MODE = "Mode";
	String ACTUAL_PARAMETERS = "ActualParameters";
	String ACTUAL_PARAMETER = "ActualParameter";
	String DATA_FIELDS = "DataFields";
	String DATA_FIELD = "DataField";
	String INITIAL_VALUE = "InitialValue";
	String IS_ARRAY = "IsArray";
	String LENGTH = "Length";
	String ROUTE = "Route";
	String IMPLEMENTATION = "Implementation";
	String BLOCKACTIVITY = "BlockActivity";
	String BLOCKID = "BlockId";
	String PERFORMER = "Performer";
	String START_MODE = "StartMode";
	String FINISH_MODE = "FinishMode";
	String MANUAL = "Manual";
	String AUTOMATIC = "Automatic";
	String UFW_COMPLETION_STRATEGY = "ufw:CompletionStrategy";
	String TRANSITION_RESTRICTIONS = "TransitionRestrictions";
	String TRANSITION_RESTRICTION = "TransitionRestriction";
	String INLINE_BLOCK = "InlineBlock";
	String JOIN = "Join";
	String SPLIT = "Split";
	String BLOCK_NAME = "BlockName";
	String BEGIN = "Begin";
	String END = "End";
	String TRANSITIONS = "Transitions";
	String AGENTS = "Agents";
	String TRANSITION = "Transition";
	String FROM = "From";
	String TO = "To";
	String FROM_ACTIVITY = "FromActivity";
	String TO_ACTIVITY = "ToActivity";
	String TRANSITION_REFERENCES = "TransitionRefs";
	String TRANSITION_REFERENCE = "TransitionRef";
	String CONDITION = "Condition";
	String XPRESSION = "Xpression";
	String TYPE_DECLARATIONS = "TypeDeclarations";
	String TYPE_DECLARATION = "TypeDeclaration";
	String TYPE = "Type";
	String MEMBER = "Member";
	String DATA_TYPE = "DataType";
	String PLAIN_TYPE = "PlainType";
	String BASIC_TYPE = "BasicType";
	String RECORD_TYPE = "RecordType";
	String UNION_TYPE = "UnionType";
	String ENUMERATION_TYPE = "EnumerationType";
	String ENUMERATION_VALUE = "EnumerationValue";
	String ARRAY_TYPE = "ArrayType";
	String LOWER_INDEX = "LowerIndex";
	String UPPER_INDEX = "UpperIndex";
	String LIST_TYPE = "ListType";
	String DECLARED_TYPE = "DeclaredType";
	String EXTERNAL_REFERENCE = "ExternalReference";
	String NAMESPACE = "namespace";
	String LOCATION = "location";
	String XREF = "xref";
	String SCRIPT = "Script";
	String GRAMMAR = "Grammar";
	String DEADLINE = "Deadline";
	String DEADLINE_CONDITION = "DeadlineCondition";
	String EXCEPTION_NAME = "ExceptionName";
	// ���������չ����
	String DIMENSION = "Dimension";
	String SHAPE_STYLE = "ShapeStyle";
	String FONT = "Font";
	String TEXT_COLOR = "TextColor";
	String TEXT_POSITION = "TextPosition";
	String BACKGROUND = "Background";
	String HAS_BORDER = "HasBorder";
	String BORDER_COLOR = "BorderColor";
	String BORDER_WIDTH = "BorderWidth";
	String OPAQUE = "Opaque";
	String USE_ICON = "UseIcon";
	String PERFORMER_NAME = "PerformerName";
	String PERFORMER_TYPE = "PerformerType";
	String PERFORMER_BELONG_ORG = "PerformerBelongOrg";
	String PRECONDITION = "Precondition";
	// ���ǰ��ִ������ֵ
	String PRECONDITIONVALUE = "PreconditionValue";
	String POSTCONDITION = "Postcondition";
	// ��ĺ���ִ������ֵ
	String POSTCONDITIONVALUE = "PostconditionValue";
	String CELL_ICON = "CellIcon";
	// leijun+ 2004-10-18
	String RACE_MODAL = "RaceModal";
	// zhouzheng+ ��Ȼ�����߰ٷֱ�
	// yanek1+ ��ǩʱ�����������������ﵽ�˴�ֵ ��ô������ɣ���������ת
	// ���ڵĽ����ͨ������ͨ����ȡ����TogetherApprovePassingThreshold
	String RACE_MODALVALUE = "RaceModalValue";
	// yanke1+ ��ǩʱ�����Ϊ��׼�������ﵽ�˴�ֵ����ô�����������Ϊ"ͨ��"������Ϊ"��ͨ��"
	String TOGETHERAPPROVE_PASSING_THRESHOLD = "TogetherApprovePassingThreshold";
	// �Ƿ��ָ��
	String IS_ASSIGN = "Assigned";
	// �Ƿ�ɼ�ǩ
	String CAN_ADDAPPROVER = "CanAddApprover";
	// �Ƿ�ɸ���
	String CAN_TRANSFER = "CanTransfer";
	// ������Ϣ����
	String MSG_CONFIG = "MsgConfig";
	// yuyonga 2009-03-05
	String TREE_ORGUNIT = "OrgUnitTree";
	String TIME_LIMIT = "TimeLimit";
	// guowl+ 2010-01
	String EDITABLE_PROPERTIES = "EditableProperties";
	String ENABLE_BUTTON = "EnableButton";
	String TIME_REMIND = "TimeRemind";
	String EXCHANGE = "Exchange";
	// Ӧ�ó���
	String TOOL_TYPE = "ToolType";
	String APPLICATION_ID = "AppId";
	String APPLICATION_NAME = "AppName";
	String WORKFLOW_GADGET = "WorkflowGadget";
	String WORKFLOW_GADGET_PARAM = "WorkflowGadgetParam"; // XXX:leijun+2009-6
	String WORKFLOW_GADGET_NAME = "WorkflowGadgetName";
	// ��ű�
	String PRE_SCRIPT = "PreScript";
	String POST_SCRIPT = "PostScript";
	// ��ת�ơ�����չ����
	String LABEL_POSITION = "LabelPosition";
	String LABEL_OFFSET = "LabelOffset";
	String LINE_COLOR = "LineColor";
	String LINE_WIDTH = "LineWidth";
	String LINE_TYPE = "LineType";
	String TRANSITION_MANUAL_CHOICE = "ManualChoice";
	String ROUTING_TYPE = "RoutingType";
	String BREAK_POINT = "BreakPoint";
	String START_ARROW = "StartArrow";
	String START_ARROW_STYLE = "StartArrowStyle";
	String START_ARROW_SIZE = "StartArrowSize";
	String START_ARROW_FILL = "StartArrowFill";
	String END_ARROW = "EndArrow";
	String END_ARROW_STYLE = "EndArrowStyle";
	String END_ARROW_SIZE = "EndArrowSize";
	String END_ARROW_FILL = "EndArrowFill";
	String DASH_PATTERN = "DashPattern";
	String DASHED = "Dashed";
	String DASH_EMPTY_SPAN = "EmptySpan";
	String DASH_SOLID_SPAN = "SolidSpan";
	String CONDITION_TYPE = "ConditionType";
	String CONDITION_XPRESSION = "ConditionXpression";
	// WorkflowProcess����չ����
	String START_OF_WORKFLOW = "UFW_StartOfWorkflow";
	String END_OF_WORKFLOW = "UFW_EndOfWorkflow";
	// String IS_OAWORKFLOW = "IsOAWorkflow";
	String WORKFLOW_TYPE = "WorkflowType";
	String BACK_MODAL = "BackModal";
	String RELEVANT_DATA = "RelevantData";
	String MAIL_MODAL = "MailModal";
	String MAIL_PRINTTEMPLET = "MailPrintTemplet";
	String MOBILE_MODAL = "MobileModal";
	String PRINT_TEMPLET_NAME = "PrintTempletName";
	String PRINT_TEMPLET_ID = "PrintTempletId";
	String SUPERVISOR = "Supervisor";
	String AUTO_APPROVE = "AutoApprove";
	// BlockActivity����չ����
	String START_OF_BLOCK = "UFW_StartOfBlock";
	String END_OF_BLOCK = "UFW_EndOfBlock";
	// ��ʼ������������ʾ����
	String TITLE = "Title";
	// Package����չ����
	String MADE_BY = "MadeBy";
	// �������ı���߶�ռ����ͼԪ�߶ȵİٷֱ�
	String TEXT_PERCENT = "TextPercent";
	String ORGANIZE_UNIT_PK = "OrganizeUnitPK";
	String ORGANIZE_UNIT_TYPE = "OrganizeUnitType";
	String ORGANIZE_UNIT_BELONGORG = "OrganizeUnitBelongOrg";
	// ��֯�޶�ģʽ
	String ORGANIZE_UNIT_FILTER_MODE = "OrganizeUnitFilterMode";
	// �����չ����-�������޶�ģʽ
	String PARTICIPANT_FILTER_MODE = "ParticipantFilterMode";
	//
	String WORKITEM_TITLE_CONFIG = "WorkitemTitle";
	// ����״̬�����ı䷢����Ϣ����
	String WF_STATECHGINFO_CONFIG = "WorkflowStateChangeInfo";
	// �޶�������
	String EMEND_FLOW = "EmendFlow";
	// �ύ��������
	String SUBMITTO_REJECTER = "SubmitTORejecter";
	// ��ϵ
	String SYSTEM = "OrgSystem";
}