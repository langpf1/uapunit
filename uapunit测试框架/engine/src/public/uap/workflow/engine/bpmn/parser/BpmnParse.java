/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.bpmn.parser;
import java.io.InputStream;
import java.net.URL;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import uap.workflow.app.config.NoticeTypeFactory;
import uap.workflow.app.config.ParticipantFilterTypeFactory;
import uap.workflow.app.config.ParticipantTypeFactory;
import uap.workflow.app.config.TaskHandlingTypeFactory;
import uap.workflow.app.notice.INoticeDefinition;
import uap.workflow.app.notice.INoticeType;
import uap.workflow.app.notice.NoticeDefinition;
import uap.workflow.app.notice.NoticeTimeTypeEnum;
import uap.workflow.app.notice.ReceiverVO;
import uap.workflow.app.participant.BasicParticipant;
import uap.workflow.app.participant.IParticipant;
import uap.workflow.app.participant.IParticipantFilterType;
import uap.workflow.app.participant.IParticipantType;
import uap.workflow.app.taskhandling.ITaskHandlingDefinition;
import uap.workflow.app.taskhandling.ITaskHandlingType;
import uap.workflow.app.taskhandling.TaskHandlingDefinition;
import uap.workflow.bizimpl.listener.ListenerDefinition;
import uap.workflow.bpmn2.model.NameSpaceConst;
import uap.workflow.engine.bpmn.behavior.AbstractBpmnActivityBehavior;
import uap.workflow.engine.bpmn.behavior.BoundaryEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.BusinessRuleTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.CallActivityBehavior;
import uap.workflow.engine.bpmn.behavior.CancelBoundaryEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.CancelEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.CompensateEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ComplexGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ErrorEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.EventBasedGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.EventSubProcessCompensateStartEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.EventSubProcessSignalStartActivityBehavior;
import uap.workflow.engine.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ExtensionService;
import uap.workflow.engine.bpmn.behavior.ExtensionServiceConfig;
import uap.workflow.engine.bpmn.behavior.InclusiveGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateCatchEventActivityBehaviour;
import uap.workflow.engine.bpmn.behavior.IntermediateLinkCatchEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowCompensationEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowLinkEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowMessageEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowNoneEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowSignalEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.MailActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ManualTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.MultiInstanceActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneStartEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ParallelGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ParallelMultiInstanceBehavior;
import uap.workflow.engine.bpmn.behavior.ReceiveTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ScriptTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SequentialMultiInstanceBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskDelegateExpressionActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskExpressionActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskJavaMethodActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ShellActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SignalEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SubProcessActivityBehavior;
import uap.workflow.engine.bpmn.behavior.TaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.WebServiceActivityBehavior;
import uap.workflow.engine.bpmn.data.AbstractDataAssociation;
import uap.workflow.engine.bpmn.data.Assignment;
import uap.workflow.engine.bpmn.data.ClassStructureDefinition;
import uap.workflow.engine.bpmn.data.Data;
import uap.workflow.engine.bpmn.data.DataRef;
import uap.workflow.engine.bpmn.data.IOSpecification;
import uap.workflow.engine.bpmn.data.ItemDefinition;
import uap.workflow.engine.bpmn.data.ItemKind;
import uap.workflow.engine.bpmn.data.SimpleDataInputAssociation;
import uap.workflow.engine.bpmn.data.StructureDefinition;
import uap.workflow.engine.bpmn.data.TransformationDataOutputAssociation;
import uap.workflow.engine.bpmn.helper.ClassDelegate;
import uap.workflow.engine.bpmn.listener.CallMethodExecutionListener;
import uap.workflow.engine.bpmn.listener.CallMethodTaskListener;
import uap.workflow.engine.bpmn.listener.DelegateExpressionExecutionListener;
import uap.workflow.engine.bpmn.listener.DelegateExpressionTaskListener;
import uap.workflow.engine.bpmn.listener.ExpressionExecutionListener;
import uap.workflow.engine.bpmn.listener.ExpressionTaskListener;
import uap.workflow.engine.bpmn.webservice.BpmnInterface;
import uap.workflow.engine.bpmn.webservice.BpmnInterfaceImplementation;
import uap.workflow.engine.bpmn.webservice.MessageDefinition;
import uap.workflow.engine.bpmn.webservice.MessageImplicitDataInputAssociation;
import uap.workflow.engine.bpmn.webservice.MessageImplicitDataOutputAssociation;
import uap.workflow.engine.bpmn.webservice.Operation;
import uap.workflow.engine.bpmn.webservice.OperationImplementation;
import uap.workflow.engine.core.ExtExecutionListener;
import uap.workflow.engine.core.ExtTaskListener;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.el.ExpressionManager;
import uap.workflow.engine.el.FixedValue;
import uap.workflow.engine.el.UelExpressionCondition;
import uap.workflow.engine.entity.DeploymentEntity;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.form.DefaultStartFormHandler;
import uap.workflow.engine.form.DefaultTaskFormHandler;
import uap.workflow.engine.form.StartFormHandler;
import uap.workflow.engine.form.TaskFormHandler;
import uap.workflow.engine.invocation.ExtensionListenerConfig;
import uap.workflow.engine.jobexecutor.TimerCatchIntermediateEventJobHandler;
import uap.workflow.engine.jobexecutor.TimerDeclarationImpl;
import uap.workflow.engine.jobexecutor.TimerDeclarationType;
import uap.workflow.engine.jobexecutor.TimerExecuteNestedActivityJobHandler;
import uap.workflow.engine.jobexecutor.TimerStartEventJobHandler;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.pvm.process.ScopeImpl;
import uap.workflow.engine.pvm.process.TransitionImpl;
import uap.workflow.engine.query.Condition;
import uap.workflow.engine.scripting.ScriptingEngines;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.util.ReflectUtil;
import uap.workflow.engine.utils.ClassUtil;
import uap.workflow.engine.utils.StringUtil;
import uap.workflow.engine.variable.VariableDeclaration;
import uap.workflow.engine.xml.Element;
import uap.workflow.engine.xml.Parse;
/**
 * Specific parsing of one BPMN 2.0 XML file, created by the {@link BpmnParser}.
 * 
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Christian Stettler
 * @author Frederik Heremans
 * @author Falko Menge
 * @author Esteban Robles
 * @author Daniel Meyer
 */
public class BpmnParse extends Parse {
	protected static final Logger LOGGER = Logger.getLogger(BpmnParse.class.getName());
	public static final String PROPERTYNAME_DOCUMENTATION = "documentation";
	public static final String PROPERTYNAME_INITIAL = "initial";
	public static final String PROPERTYNAME_INITIATOR_VARIABLE_NAME = "initiatorVariableName";
	public static final String PROPERTYNAME_CONDITION = "condition";
	public static final String PROPERTYNAME_CONDITION_TEXT = "conditionText";
	public static final String PROPERTYNAME_VARIABLE_DECLARATIONS = "variableDeclarations";
	public static final String PROPERTYNAME_TIMER_DECLARATION = "timerDeclarations";
	public static final String PROPERTYNAME_ISEXPANDED = "isExpanded";
	public static final String PROPERTYNAME_START_TIMER = "timerStart";
	public static final String PROPERTYNAME_SIGNAL_DEFINITION_NAME = "signalDefinition";
	public static final String PROPERTYNAME_COMPENSATION_HANDLER_ID = "compensationHandler";
	public static final String PROPERTYNAME_IS_FOR_COMPENSATION = "isForCompensation";
	public static final String PROPERTYNAME_ERROR_EVENT_DEFINITIONS = "errorEventDefinitions";
	public static final String PROPERTYNAME_MESSAGE_EVENT_DEFINITIONS = "messageEventDefinitions";
	public static final String PROPERTYNAME_COMPLEXGATEWAY_ACTIVATIONCONDITION = "activationCondition";
	public static final String PROPERTYNAME_LINK_DEFINITION_NAME = "linkEventDefinition";
	/** The deployment to which the parsed process definitions will be added. */
	protected DeploymentEntity deployment;
	/** The end result of the parsing: a list of process definition. */
	protected List<IProcessDefinition> processDefinitions = new ArrayList<IProcessDefinition>();
	/** Mapping of found errors in BPMN 2.0 file */
	protected Map<String, Error> errors = new HashMap<String, Error>();
	/** A map for storing sequence flow based on their id during parsing. */
	protected Map<String, ITransition> sequenceFlows;
	/**
	 * A list of all element IDs. This allows us to parse only what we actually
	 * support but still validate the references among elements we do not
	 * support.
	 */
	protected List<String> elementIds = new ArrayList<String>();
	/**
	 * Mapping containing values stored during the first phase of parsing since
	 * other elements can reference these messages.
	 * 
	 * All the map's elements are defined outside the process definition(s),
	 * which means that this map doesn't need to be re-initialized for each new
	 * process definition.
	 */
	protected Map<String, MessageDefinition> messages = new HashMap<String, MessageDefinition>();
	protected Map<String, StructureDefinition> structures = new HashMap<String, StructureDefinition>();
	protected Map<String, BpmnInterfaceImplementation> interfaceImplementations = new HashMap<String, BpmnInterfaceImplementation>();
	protected Map<String, OperationImplementation> operationImplementations = new HashMap<String, OperationImplementation>();
	protected Map<String, ItemDefinition> itemDefinitions = new HashMap<String, ItemDefinition>();
	protected Map<String, BpmnInterface> bpmnInterfaces = new HashMap<String, BpmnInterface>();
	protected Map<String, Operation> operations = new HashMap<String, Operation>();
	protected Map<String, SignalDefinition> signals = new HashMap<String, SignalDefinition>();
	protected Map<String, LinkEventDefinition> links = new HashMap<String, LinkEventDefinition>();
	// Members
	protected ExpressionManager expressionManager;
	protected List<BpmnParseListener> parseListeners;
	protected Map<String, XMLImporter> importers = new HashMap<String, XMLImporter>();
	protected Map<String, String> prefixs = new HashMap<String, String>();
	protected String targetNamespace;
	public static Condition expressionCondition;
	/**
	 * Constructor to be called by the {@link BpmnParser}.
	 * 
	 * Note the package modifier here: only the {@link BpmnParser} is allowed to
	 * create instances.
	 */
	BpmnParse(BpmnParser parser) {
		this.expressionManager = parser.getExpressionManager();
		this.parseListeners = parser.getParseListeners();
		setSchemaResource(ReflectUtil.getResource(BpmnParser.BPMN_20_SCHEMA_LOCATION).toString());
		this.initializeXSDItemDefinitions();
	}
	protected void initializeXSDItemDefinitions() {
		this.itemDefinitions.put("http://www.w3.org/2001/XMLSchema:string", new ItemDefinition("http://www.w3.org/2001/XMLSchema:string", new ClassStructureDefinition(String.class)));
	}
	public BpmnParse deployment(DeploymentEntity deployment) {
		this.deployment = deployment;
		return this;
	}
	@Override
	public BpmnParse execute() {
		super.execute(); // schema validation
		try {
			parseRootElement();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Uknown exception", e);
		} finally {
			if (hasWarnings()) {
				logWarnings();
			}
			if (hasErrors()) {
				throwActivitiExceptionForErrors();
			}
		}
		return this;
	}
	/**
	 * Parses the 'definitions' root element
	 */
	protected void parseRootElement() {
		collectElementIds();
		parseDefinitionsAttributes();
		parseImports();
		parseItemDefinitions();
		parseMessages();
		parseInterfaces();
		parseErrors();
		parseSignals();
		parseProcessDefinitions();
		parseDiagramInterchangeElements();
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseRootElement(rootElement, getProcessDefinitions());
		}
	}
	protected void collectElementIds() {
		rootElement.collectIds(elementIds);
	}
	protected void parseDefinitionsAttributes() {
		String typeLanguage = rootElement.attribute("typeLanguage");
		String expressionLanguage = rootElement.attribute("expressionLanguage");
		this.targetNamespace = rootElement.attribute("targetNamespace");
		// this.targetNamespace ="http://www.activiti.org/test";
		if (typeLanguage != null) {
			if (typeLanguage.contains("XMLSchema")) {
				LOGGER.info("XMLSchema currently not supported as typeLanguage");
			}
		}
		if (expressionLanguage != null) {
			if (expressionLanguage.contains("XPath")) {
				LOGGER.info("XPath currently not supported as expressionLanguage");
			}
		}
		for (String attribute : rootElement.attributes()) {
			if (attribute.startsWith("xmlns:")) {
				String prefixValue = rootElement.attribute(attribute);
				String prefixName = attribute.substring(6);
				this.prefixs.put(prefixName, prefixValue);
			}
		}
	}
	protected String resolveName(String name) {
		if (name == null) {
			return null;
		}
		int indexOfP = name.indexOf(':');
		if (indexOfP != -1) {
			String prefix = name.substring(0, indexOfP);
			String resolvedPrefix = this.prefixs.get(prefix);
			return resolvedPrefix + ":" + name.substring(indexOfP + 1);
		} else {
			return name;
		}
	}
	/**
	 * Parses the rootElement importing structures
	 * 
	 * @param rootElement
	 *            The root element of the XML file.
	 */
	protected void parseImports() {
		List<Element> imports = rootElement.elements("import");
		for (Element theImport : imports) {
			String importType = theImport.attribute("importType");
			XMLImporter importer = this.getImporter(importType, theImport);
			if (importer == null) {
				addError("Could not import item of type " + importType, theImport);
			} else {
				importer.importFrom(theImport, this);
			}
		}
	}
	protected XMLImporter getImporter(String importType, Element theImport) {
		if (this.importers.containsKey(importType)) {
			return this.importers.get(importType);
		} else {
			if (importType.equals("http://schemas.xmlsoap.org/wsdl/")) {
				Class<?> wsdlImporterClass;
				try {
					wsdlImporterClass = Class.forName("org.activiti.engine.impl.webservice.CxfWSDLImporter", true, Thread.currentThread().getContextClassLoader());
					XMLImporter newInstance = (XMLImporter) wsdlImporterClass.newInstance();
					this.importers.put(importType, newInstance);
					return newInstance;
				} catch (Exception e) {
					addError("Could not find importer for type " + importType, theImport);
				}
			}
			return null;
		}
	}
	/**
	 * Parses the itemDefinitions of the given definitions file. Item
	 * definitions are not contained within a process element, but they can be
	 * referenced from inner process elements.
	 * 
	 * @param definitionsElement
	 *            The root element of the XML file.
	 */
	public void parseItemDefinitions() {
		for (Element itemDefinitionElement : rootElement.elements("itemDefinition")) {
			String id = itemDefinitionElement.attribute("id");
			String structureRef = this.resolveName(itemDefinitionElement.attribute("structureRef"));
			String itemKind = itemDefinitionElement.attribute("itemKind");
			StructureDefinition structure = null;
			try {
				// it is a class
				Class<?> classStructure = ReflectUtil.loadClass(structureRef);
				structure = new ClassStructureDefinition(classStructure);
			} catch (WorkflowException e) {
				// it is a reference to a different structure
				structure = this.structures.get(structureRef);
			}
			ItemDefinition itemDefinition = new ItemDefinition(this.targetNamespace + ":" + id, structure);
			if (itemKind != null) {
				itemDefinition.setItemKind(ItemKind.valueOf(itemKind));
			}
			itemDefinitions.put(itemDefinition.getId(), itemDefinition);
		}
	}
	/**
	 * Parses the messages of the given definitions file. Messages are not
	 * contained within a process element, but they can be referenced from inner
	 * process elements.
	 * 
	 * @param definitionsElement
	 *            The root element of the XML file/
	 */
	public void parseMessages() {
		for (Element messageElement : rootElement.elements("message")) {
			String id = messageElement.attribute("id");
			String itemRef = this.resolveName(messageElement.attribute("itemRef"));
			String name = messageElement.attribute("name");
			MessageDefinition messageDefinition = new MessageDefinition(this.targetNamespace + ":" + id, name);
			if (itemRef != null) {
				if (!this.itemDefinitions.containsKey(itemRef)) {
					addError(itemRef + " does not exist", messageElement);
				} else {
					ItemDefinition itemDefinition = this.itemDefinitions.get(itemRef);
					messageDefinition.setItemDefinition(itemDefinition);
				}
			}
			this.messages.put(messageDefinition.getId(), messageDefinition);
		}
	}
	/**
	 * Parses the signals of the given definitions file. Signals are not
	 * contained within a process element, but they can be referenced from inner
	 * process elements.
	 * 
	 * @param definitionsElement
	 *            The root element of the XML file/
	 */
	protected void parseSignals() {
		for (Element signalElement : rootElement.elements("signal")) {
			String id = signalElement.attribute("id");
			String signalName = this.resolveName(signalElement.attribute("name"));
			for (SignalDefinition signalDefinition : signals.values()) {
				if (signalDefinition.getName().equals(signalName)) {
					addError("duplicate signal name '" + signalName + "'.", signalElement);
				}
			}
			if (id == null) {
				addError("signal must have an id", signalElement);
			} else if (signalName == null) {
				addError("signal with id '" + id + "' has no name", signalElement);
			} else {
				SignalDefinition signal = new SignalDefinition();
				signal.setId(this.targetNamespace + ":" + id);
				signal.setName(signalName);
				this.signals.put(id, signal);
			}
		}
	}
	/**
	 * Parses the interfaces and operations defined withing the root element.
	 * 
	 * @param definitionsElement
	 *            The root element of the XML file/
	 */
	public void parseInterfaces() {
		for (Element interfaceElement : rootElement.elements("interface")) {
			// Create the interface
			String id = interfaceElement.attribute("id");
			String name = interfaceElement.attribute("name");
			String implementationRef = this.resolveName(interfaceElement.attribute("implementationRef"));
			BpmnInterface bpmnInterface = new BpmnInterface(this.targetNamespace + ":" + id, name);
			bpmnInterface.setImplementation(this.interfaceImplementations.get(implementationRef));
			// Handle all its operations
			for (Element operationElement : interfaceElement.elements("operation")) {
				Operation operation = parseOperation(operationElement, bpmnInterface);
				bpmnInterface.addOperation(operation);
			}
			bpmnInterfaces.put(bpmnInterface.getId(), bpmnInterface);
		}
	}
	public Operation parseOperation(Element operationElement, BpmnInterface bpmnInterface) {
		Element inMessageRefElement = operationElement.element("inMessageRef");
		String inMessageRef = this.resolveName(inMessageRefElement.getText());
		if (!this.messages.containsKey(inMessageRef)) {
			addError(inMessageRef + " does not exist", inMessageRefElement);
			return null;
		} else {
			MessageDefinition inMessage = this.messages.get(inMessageRef);
			String id = operationElement.attribute("id");
			String name = operationElement.attribute("name");
			String implementationRef = this.resolveName(operationElement.attribute("implementationRef"));
			Operation operation = new Operation(this.targetNamespace + ":" + id, name, bpmnInterface, inMessage);
			operation.setImplementation(this.operationImplementations.get(implementationRef));
			Element outMessageRefElement = operationElement.element("outMessageRef");
			if (outMessageRefElement != null) {
				String outMessageRef = this.resolveName(outMessageRefElement.getText());
				if (this.messages.containsKey(outMessageRef)) {
					MessageDefinition outMessage = this.messages.get(outMessageRef);
					operation.setOutMessage(outMessage);
				}
			}
			operations.put(operation.getId(), operation);
			return operation;
		}
	}
	public void parseErrors() {
		for (Element errorElement : rootElement.elements("error")) {
			Error error = new Error();
			String id = errorElement.attribute("id");
			if (id == null) {
				addError("'id' is mandatory on error definition", errorElement);
			}
			error.setId(id);
			String errorCode = errorElement.attribute("errorCode");
			if (errorCode == null) {
				addError("'errorCode' is mandatory on error definition", errorElement);
			}
			error.setErrorCode(errorCode);
			errors.put(id, error);
		}
	}
	/**
	 * Parses all the process definitions defined within the 'definitions' root
	 * element.
	 * 
	 * @param definitionsElement
	 *            The root element of the XML file.
	 */
	public void parseProcessDefinitions() {
		for (Element processElement : rootElement.elements("process")) {
			boolean processProcess = true;
			String isExecutableStr = processElement.attribute("isExecutable");
			if (isExecutableStr != null) {
				boolean isExecutable = Boolean.parseBoolean(isExecutableStr);
				if (!isExecutable) {
					processProcess = false;
				}
			}
			// Only process executable processes
			if (processProcess) {
				processDefinitions.add(parseProcess(processElement));
			}
		}
	}
	/**
	 * Parses one process (ie anything inside a &lt;process&gt; element).
	 * 
	 * @param processElement
	 *            The 'process' element.
	 * @return The parsed version of the XML: a {@link ProcessDefinitionEntity}
	 *         object.
	 */
	public IProcessDefinition parseProcess(Element processElement) {
		// reset all mappings that are related to one process definition
		sequenceFlows = new HashMap<String, ITransition>();
		IProcessDefinition processDefinition = new ProcessDefinitionEntity(processElement.attribute("id"));
		processDefinition.setProDefPk(processElement.attribute("processDefinitionPk"));
		processDefinition.setPk_bizobject(processElement.attribute("objectType"));
		processDefinition.setPk_biztrans(processElement.attribute("biztrans"));
		/*
		 * Mapping object model - bpmn xml: processDefinition.id -> generated by
		 * activiti engine processDefinition.key -> bpmn id (required)
		 * processDefinition.name -> bpmn name (optional)
		 */
		processDefinition.setName(processElement.attribute("name"));
		processDefinition.setCategory(rootElement.attribute("targetNamespace"));
		processDefinition.setProperty(PROPERTYNAME_DOCUMENTATION, parseDocumentation(processElement));
		processDefinition.setTaskDefinitions(new HashMap<String, TaskDefinition>());
		parseScope(processElement, processDefinition);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseProcess(processElement, processDefinition);
		}
		return processDefinition;
	}
	/**
	 * Parses a scope: a process, subprocess, etc.
	 * 
	 * Note that a process definition is a scope on itself.
	 * 
	 * @param scopeElement
	 *            The XML element defining the scope
	 * @param parentScope
	 *            The scope that contains the nested scope.
	 */
	public void parseScope(Element scopeElement, IScope parentScope) {
		// Not yet supported on process level (PVM additions needed):
		// parseProperties(processElement);
		HashMap<String, Element> postponedElements = new HashMap<String, Element>();
		parseStartEvents(scopeElement, parentScope);
		parseActivities(scopeElement, parentScope, postponedElements);
		parsePostponedElements(scopeElement, parentScope, postponedElements);
		parseEndEvents(scopeElement, parentScope);
		parseBoundaryEvents(scopeElement, parentScope);
		parseSequenceFlow(scopeElement, parentScope);
		parseExecutionListenersOnScope(scopeElement, parentScope);
		parseAssociations(scopeElement, parentScope);
		postponedElements.clear();
		IOSpecification ioSpecification = parseIOSpecification(scopeElement.element("ioSpecification"));
		parentScope.setIoSpecification(ioSpecification);
	}
	protected void parsePostponedElements(Element scopeElement, IScope parentScope, HashMap<String, Element> postponedElements) {
		for (Element postponedElement : postponedElements.values()) {
			if (parentScope.findActivity(postponedElement.attribute("id")) == null) {
				if (postponedElement.getTagName().equals("intermediateCatchEvent")) {
					parseIntermediateCatchEvent(postponedElement, parentScope, false);
				}
			}
		}
	}
	protected void parseAssociations(Element scopeElement, IScope parentScope) {
		for (Element associationElement : scopeElement.elements("association")) {
			String sourceRef = associationElement.attribute("sourceRef");
			if (sourceRef == null) {
				addError("association element missing attribute 'sourceRef'", associationElement);
			}
			String targetRef = associationElement.attribute("targetRef");
			if (targetRef == null) {
				addError("association element missing attribute 'targetRef'", associationElement);
			}
			IActivity sourceActivity = parentScope.findActivity(sourceRef);
			IActivity targetActivity = parentScope.findActivity(targetRef);
			// an association may reference elements that are not parsed as
			// activities (like for instance
			// test annotations so do not throw an exception if source
			// sourceActivity or targetActivity are null)
			// However, we make sure they reference 'something':
			if (sourceActivity == null && !elementIds.contains(sourceRef)) {
				addError("Invalid reference sourceRef '" + sourceRef + "' of association element ", associationElement);
			} else if (targetRef == null && !elementIds.contains(targetRef)) {
				addError("Invalid reference targetRef '" + targetRef + "' of association element ", associationElement);
			} else {
				if (sourceActivity.getProperty("type").equals("compensationBoundaryCatch")) {
					Object isForCompensation = targetActivity.getProperty(PROPERTYNAME_IS_FOR_COMPENSATION);
					if (isForCompensation == null || !(Boolean) isForCompensation) {
						addError("compensation boundary catch must be connected to element with isForCompensation=true", associationElement);
					} else {
						ActivityImpl compensatedActivity = (ActivityImpl) sourceActivity.getParent();
						compensatedActivity.setProperty(PROPERTYNAME_COMPENSATION_HANDLER_ID, targetActivity.getId());
					}
				}
			}
		}
	}
	protected IOSpecification parseIOSpecification(Element ioSpecificationElement) {
		if (ioSpecificationElement == null) {
			return null;
		}
		IOSpecification ioSpecification = new IOSpecification();
		for (Element dataInputElement : ioSpecificationElement.elements("dataInput")) {
			String id = dataInputElement.attribute("id");
			String itemSubjectRef = this.resolveName(dataInputElement.attribute("itemSubjectRef"));
			ItemDefinition itemDefinition = this.itemDefinitions.get(itemSubjectRef);
			Data dataInput = new Data(this.targetNamespace + ":" + id, id, itemDefinition);
			ioSpecification.addInput(dataInput);
		}
		for (Element dataOutputElement : ioSpecificationElement.elements("dataOutput")) {
			String id = dataOutputElement.attribute("id");
			String itemSubjectRef = this.resolveName(dataOutputElement.attribute("itemSubjectRef"));
			ItemDefinition itemDefinition = this.itemDefinitions.get(itemSubjectRef);
			Data dataOutput = new Data(this.targetNamespace + ":" + id, id, itemDefinition);
			ioSpecification.addOutput(dataOutput);
		}
		for (Element inputSetElement : ioSpecificationElement.elements("inputSet")) {
			for (Element dataInputRef : inputSetElement.elements("dataInputRefs")) {
				DataRef dataRef = new DataRef(dataInputRef.getText());
				ioSpecification.addInputRef(dataRef);
			}
		}
		for (Element outputSetElement : ioSpecificationElement.elements("outputSet")) {
			for (Element dataInputRef : outputSetElement.elements("dataOutputRefs")) {
				DataRef dataRef = new DataRef(dataInputRef.getText());
				ioSpecification.addOutputRef(dataRef);
			}
		}
		return ioSpecification;
	}
	protected AbstractDataAssociation parseDataInputAssociation(Element dataAssociationElement) {
		String sourceRef = dataAssociationElement.element("sourceRef").getText();
		String targetRef = dataAssociationElement.element("targetRef").getText();
		List<Element> assignments = dataAssociationElement.elements("assignment");
		if (assignments.isEmpty()) {
			return new MessageImplicitDataInputAssociation(sourceRef, targetRef);
		} else {
			SimpleDataInputAssociation dataAssociation = new SimpleDataInputAssociation(sourceRef, targetRef);
			for (Element assigmentElement : dataAssociationElement.elements("assignment")) {
				Expression from = this.expressionManager.createExpression(assigmentElement.element("from").getText());
				Expression to = this.expressionManager.createExpression(assigmentElement.element("to").getText());
				Assignment assignment = new Assignment(from, to);
				dataAssociation.addAssignment(assignment);
			}
			return dataAssociation;
		}
	}
	/**
	 * Parses the start events of a certain level in the process (process,
	 * subprocess or another scope).
	 * 
	 * @param parentElement
	 *            The 'parent' element that contains the start events (process,
	 *            subprocess).
	 * @param scope
	 *            The {@link ScopeImpl} to which the start events must be added.
	 */
	public void parseStartEvents(Element parentElement, IScope scope) {
		List<Element> startEventElements = parentElement.elements("startEvent");
		List<IActivity> startEventActivities = new ArrayList<IActivity>();
		for (Element startEventElement : startEventElements) {
			IActivity startEventActivity = createActivityOnScope(startEventElement, scope);
			if (scope instanceof IProcessDefinition) {
				parseProcessDefinitionStartEvent(startEventActivity, startEventElement, parentElement, scope);
				startEventActivities.add(startEventActivity);
			} else {
				parseScopeStartEvent(startEventActivity, startEventElement, parentElement, scope);
			}
			for (BpmnParseListener parseListener : parseListeners) {
				parseListener.parseStartEvent(startEventElement, scope, startEventActivity);
			}
		}
		if (scope instanceof IProcessDefinition) {
			selectInitial(startEventActivities, (IProcessDefinition) scope, parentElement);
			parseStartFormHandlers(startEventElements, (IProcessDefinition) scope);
		}
	}
	protected void selectInitial(List<IActivity> startEventActivities, IProcessDefinition processDefinition, Element parentElement) {
		IActivity initial = null;
		// validate that there is s single none start event / timer start event:
		for (IActivity activityImpl : startEventActivities) {
			if (!activityImpl.getProperty("type").equals("messageStartEvent")) {
				if (initial == null) {
					initial = activityImpl;
				} else {
					addError("multiple none start events or timer start events not supported on process definition", parentElement);
				}
			}
		}
		// if there is a single start event, select it as initial, regardless of
		// it's type:
		if (initial == null && startEventActivities.size() == 1) {
			initial = startEventActivities.get(0);
		}
		processDefinition.setInitial(initial);
	}
	protected void parseProcessDefinitionStartEvent(IActivity startEventActivity, Element startEventElement, Element parentElement, IScope scope) {
		IProcessDefinition processDefinition = (IProcessDefinition) scope;
		String initiatorVariableName = startEventElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "initiator");
		if (initiatorVariableName != null) {
			processDefinition.setProperty(PROPERTYNAME_INITIATOR_VARIABLE_NAME, initiatorVariableName);
		}
		// all start events share the same behavior:
		startEventActivity.setActivityBehavior(new NoneStartEventActivityBehavior());
		Element timerEventDefinition = startEventElement.element("timerEventDefinition");
		Element messageEventDefinition = startEventElement.element("messageEventDefinition");
		if (timerEventDefinition != null) {
			parseTimerStartEventDefinition(timerEventDefinition, startEventActivity, processDefinition);
		} else if (messageEventDefinition != null) {
			parseMessageStartEventDefinition(messageEventDefinition, startEventActivity, processDefinition);
		}
	}
	protected void parseStartFormHandlers(List<Element> startEventElements, IProcessDefinition processDefinition) {
		if (processDefinition.getInitial() != null) {
			for (Element startEventElement : startEventElements) {
				if (startEventElement.attribute("id").equals(processDefinition.getInitial().getId())) {
					StartFormHandler startFormHandler;
					String startFormHandlerClassName = startEventElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formHandlerClass");
					if (startFormHandlerClassName != null) {
						startFormHandler = (StartFormHandler) ReflectUtil.instantiate(startFormHandlerClassName);
					} else {
						startFormHandler = new DefaultStartFormHandler();
					}
					startFormHandler.parseConfiguration(startEventElement, deployment, processDefinition, this);
					processDefinition.setStartFormHandler(startFormHandler);
				}
			}
		}
	}

	protected void parseScopeStartEvent(IActivity startEventActivity,
			Element startEventElement, Element parentElement, IScope scope) {
		    if (scope.getProperty(PROPERTYNAME_INITIAL) == null) {
			scope.setProperty(PROPERTYNAME_INITIAL, startEventActivity);
			Object triggeredByEvent = scope.getProperty("triggeredByEvent");
			boolean isTriggeredByEvent = triggeredByEvent != null && ((Boolean) triggeredByEvent == true);
			Element errorEventDefinition = startEventElement.element("errorEventDefinition");
			Element compensateEventDefinition = startEventElement.element("compensateEventDefinition");
			Element messageEventDefinition = startEventElement.element("messageEventDefinition");
			Element signalEventDefinition = startEventElement.element("signalEventDefinition");
			if (errorEventDefinition != null) {
				if (isTriggeredByEvent) {
					parseErrorStartEventDefinition(errorEventDefinition, startEventActivity, scope);
				} else {
					addError("errorEventDefinition only allowed on start event if subprocess is an event subprocess", errorEventDefinition);
				}
				return;
			}
			if (compensateEventDefinition != null) {
				if (isTriggeredByEvent) {
					parseCompensateStartEventDefinition(compensateEventDefinition, startEventActivity, scope);
				} else {
					addError("compensateEventDefinition only allowed on start event if subprocess is an event subprocess", compensateEventDefinition);
				}
				return;
			}
			if (messageEventDefinition != null) {
				if (isTriggeredByEvent) {
					parseMessageStartEventDefinition(messageEventDefinition, startEventActivity, scope);
				} else {
					addError("messageEventDefinition only allowed on start event if subprocess is an event subprocess", compensateEventDefinition);
				}
				return;
			}
			if (signalEventDefinition != null) {
				if (isTriggeredByEvent) {
					parseSignalStartEventDefinition(signalEventDefinition, startEventActivity, scope);
				} else {
					addError("signalEventDefinition only allowed on start event if subprocess is an event subprocess", compensateEventDefinition);
				}
				return;
			}
			if (!isTriggeredByEvent) {
				startEventActivity.setActivityBehavior(new NoneStartEventActivityBehavior());
			} else {
				addError("none start event not allowed for event subprocess", startEventElement);
			}
		}else {
			addError("multiple start events not supported for subprocess", startEventElement);
		}
	}
//Event Sub_Process Start event  
	private void parseSignalStartEventDefinition(Element signalEventDefinition, IActivity startEventActivity,IScope scope) {
	 	startEventActivity.setProperty("type", "signalStartEvent");
		SignalEventDefinition signalDefinition = parseSignalEventDefinition(signalEventDefinition);
		signalDefinition.setActivityId(startEventActivity.getId());
		addSignalDefinition(startEventActivity, signalDefinition);
    	startEventActivity.setScope(true);
		startEventActivity.setActivityBehavior(new EventSubProcessSignalStartActivityBehavior(signalDefinition));		
	}
	protected void parseCompensateStartEventDefinition(Element compensateEventDefinition, IActivity startEventActivity,IScope scope) {
		startEventActivity.setProperty("type", "compensateStartEvent");
		String activityRef= compensateEventDefinition.attribute("activityRef");
		boolean waitForCompletion = "true".equals(compensateEventDefinition.attribute("waitForCompletion", "true"));
		if (activityRef != null) {
				if(elementIds.contains(activityRef)==false)
				addError("Invalid attribute value for 'activityRef': no activity with id '" + activityRef + "' in current scope", compensateEventDefinition);
			}	
		CompensateEventDefinition compensateStartEventDefinition = new CompensateEventDefinition();
		compensateStartEventDefinition.setActivityRef(activityRef);
		compensateStartEventDefinition.setWaitForCompletion(waitForCompletion);
		startEventActivity.setActivityBehavior(new EventSubProcessCompensateStartEventActivityBehavior(compensateStartEventDefinition));
		
	}
	protected void parseErrorStartEventDefinition(Element errorEventDefinition, IActivity startEventActivity, IScope scope) {
		startEventActivity.setProperty("type", "errorStartEvent");
		String errorRef = errorEventDefinition.attribute("errorRef");
		Error error = null;
		ErrorEventDefinition definition = new ErrorEventDefinition(startEventActivity.getId());
		if (errorRef != null) {
			error = errors.get(errorRef);
			String errorCode = error == null ? errorRef : error.getErrorCode();
			definition.setErrorCode(errorCode);
		}
		ScopeImpl catchingScope = (ScopeImpl) ((ActivityImpl) scope).getParent();
		definition.setPrecedence(10);
		addErrorEventDefinition(definition, catchingScope);
		startEventActivity.setActivityBehavior(new EventSubProcessStartEventActivityBehavior());
	}
	protected void parseMessageStartEventDefinition(Element messageEventDefinition, IActivity startEventActivity, IScope  processDefinition) {
		String messageRef = messageEventDefinition.attribute("messageRef");
		if (messageRef == null) {
		    addError("attriute 'messageRef' is required", messageEventDefinition);
		}		
		MessageDefinition messageDefinition = new MessageDefinition(this.targetNamespace + ":" + messageRef, name);//modify begine
		messages.put(messageRef, messageDefinition);//modify end
	    messageDefinition = messages.get(resolveName(messageRef));
		if (messageDefinition == null) {
			addError("Invalid 'messageRef': no message with id '" + messageRef + "' found.", messageEventDefinition);
		}
		startEventActivity.setProperty("type", "messageStartEvent");
		// create message event subscription:
		MessageEventDefinition subscription = new MessageEventDefinition(messageDefinition.getId(), messageDefinition.getName(), startEventActivity.getId());
		subscription.setStartEvent(true);
		addMessageEventDefinition(subscription, processDefinition);
	}
	@SuppressWarnings("unchecked")
	protected void addMessageEventDefinition(MessageEventDefinition subscription, IScope scope) {
		List<MessageEventDefinition> messageEventDefinitions = (List<MessageEventDefinition>) scope.getProperty(PROPERTYNAME_MESSAGE_EVENT_DEFINITIONS);
		if (messageEventDefinitions == null) {
			messageEventDefinitions = new ArrayList<MessageEventDefinition>();
			scope.setProperty(PROPERTYNAME_MESSAGE_EVENT_DEFINITIONS, messageEventDefinitions);
		}
		messageEventDefinitions.add(subscription);
	}
	/**
	 * Parses the activities of a certain level in the process (process,
	 * subprocess or another scope).
	 * 
	 * @param parentElement
	 *            The 'parent' element that contains the activities (process,
	 *            subprocess).
	 * @param scopeElement
	 *            The {@link ScopeImpl} to which the activities must be added.
	 * @param postponedElements
	 * @param postProcessActivities
	 */
	public void parseActivities(Element parentElement, IScope scopeElement, HashMap<String, Element> postponedElements) {
		for (Element activityElement : parentElement.elements()) {
			parseActivity(activityElement, parentElement, scopeElement, postponedElements);
		}
	}
	protected void parseActivity(Element activityElement, Element parentElement, IScope scopeElement, HashMap<String, Element> postponedElements) {
		IActivity activity = null;
		if (activityElement.getTagName().equals("exclusiveGateway")) {
			activity = parseExclusiveGateway(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("inclusiveGateway")) {
			activity = parseInclusiveGateway(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("parallelGateway")) {
			activity = parseParallelGateway(activityElement, scopeElement);
		}  else if (activityElement.getTagName().equals("complexGateway")) {
			activity = parseComplexGateway(activityElement, scopeElement);
		}else if (activityElement.getTagName().equals("scriptTask")) {
			activity = parseScriptTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("serviceTask")) {
			activity = parseServiceTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("businessRuleTask")) {
			activity = parseBusinessRuleTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("task")) {
			activity = parseTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("manualTask")) {
			activity = parseManualTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("userTask")) {
			activity = parseUserTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("sendTask")) {
			activity = parseSendTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("receiveTask")) {
			activity = parseReceiveTask(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("subProcess")) {
			activity = parseSubProcess(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("callActivity")) {
			activity = parseCallActivity(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("intermediateCatchEvent")) {
			postponedElements.put(activityElement.attribute("id"), activityElement);
		} else if (activityElement.getTagName().equals("intermediateThrowEvent")) {
			activity = parseIntermediateThrowEvent(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("eventGateway")) {
			activity = parseEventBasedGateway(activityElement, parentElement, scopeElement);
		} else if (activityElement.getTagName().equals("transaction")) {
			activity = parseTransaction(activityElement, scopeElement);
		} else if (activityElement.getTagName().equals("adHocSubProcess") || activityElement.getTagName().equals("complexGateway")) {
			addWarning("Ignoring unsupported activity type", activityElement);
		}
		if (activity != null) {
			parseMultiInstanceLoopCharacteristics(activityElement, activity);
		}
		parserExecutionListener();
	}
	private void parserExecutionListener() {
		// TODO Auto-generated method stub
	}
	public IActivity parseIntermediateCatchEvent(Element intermediateEventElement, IScope scopeElement, boolean isAfterEventBasedGateway) {
		IActivity nestedActivity = createActivityOnScope(intermediateEventElement, scopeElement);
		nestedActivity.setActivityBehavior(new IntermediateCatchEventActivityBehaviour());
		Element timerEventDefinition = intermediateEventElement.element("timerEventDefinition");
		Element signalEventDefinition = intermediateEventElement.element("signalEventDefinition");
		Element compensateEventDefinition = intermediateEventElement.element("compensateEventDefinition");
		Element messageEventDefinition = intermediateEventElement.element("messageEventDefinition");
		//Element conditionalEventDefinition = intermediateEventElement.element("conditionalEventDefinition");
		Element linkEventDefinition = intermediateEventElement.element("linkEventDefinition");
		//Element multipleEventDefinition = intermediateEventElement.element("multipleEventDefinition");
		if (timerEventDefinition != null) {
			parseIntemediateTimerEventDefinition(timerEventDefinition, nestedActivity, isAfterEventBasedGateway);
		} else if (signalEventDefinition != null) {
			parseIntemediateSignalEventDefinition(signalEventDefinition, nestedActivity, isAfterEventBasedGateway);
		}  else if (compensateEventDefinition != null) {
			parseIntemediateCompensateEventDefinition(compensateEventDefinition, nestedActivity, isAfterEventBasedGateway);
		}  else if (messageEventDefinition != null) {
			parseIntemediateMessageEventDefinition(messageEventDefinition, nestedActivity, isAfterEventBasedGateway);
		} else if (linkEventDefinition != null) {
			nestedActivity.setActivityBehavior(new IntermediateLinkCatchEventActivityBehavior());
			parseIntemediateLinkEventDefinition(linkEventDefinition, nestedActivity, isAfterEventBasedGateway);
		} 	
		else {
			addError("Unsupported intermediate catch event type", intermediateEventElement);
		}
		return nestedActivity;
	}
	
	
	private void parseIntemediateLinkEventDefinition(Element linkEventDefinition, IActivity linkActivity,	boolean isAfterEventBasedGateway) {
		linkActivity.setProperty("type", "intermediateSignalCatch");
		LinkEventDefinition linkDefinition = new LinkEventDefinition();
		linkDefinition.setActivityId(linkActivity.getId());
		if (isAfterEventBasedGateway) {
			addLinkDefinition((ScopeImpl) linkActivity.getParent(), linkDefinition);
		} else {
			addLinkDefinition(linkActivity, linkDefinition);
			linkActivity.setScope(true);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseIntermediateLinkCatchEventDefinition(linkEventDefinition, linkActivity);
		}
	}
	
	private void addLinkDefinition(IScope scopeImpl, LinkEventDefinition linkDefinition) {
		@SuppressWarnings("unchecked")
		List<LinkEventDefinition> linkDefinitions = (List<LinkEventDefinition>) scopeImpl.getProperty(PROPERTYNAME_LINK_DEFINITION_NAME);
		if (linkDefinitions == null) {
			linkDefinitions = new ArrayList<LinkEventDefinition>();
			scopeImpl.setProperty(PROPERTYNAME_LINK_DEFINITION_NAME, linkDefinitions);
		}
		linkDefinitions.add(linkDefinition);
	
	}
	private void parseIntemediateMessageEventDefinition(Element messageEventDefinition, IActivity nestedActivity,boolean isAfterEventBasedGateway) {
       //等待实现 
	}
	public IActivity parseIntermediateThrowEvent(Element intermediateEventElement, IScope scopeElement) {
		IActivity nestedActivityImpl = createActivityOnScope(intermediateEventElement, scopeElement);
		ActivityBehavior activityBehavior = null;
		Element signalEventDefinitionElement = intermediateEventElement.element("signalEventDefinition");
		Element compensateEventDefinitionElement = intermediateEventElement.element("compensateEventDefinition");
		Element messageEventDefinitionElement = intermediateEventElement.element("messageEventDefinition");
		Element linkEventDefinitionElement = intermediateEventElement.element("linkEventDefinition");	
		boolean otherUnsupportedThrowingIntermediateEvent = (intermediateEventElement.element("escalationEventDefinition") != null); 
				//||(intermediateEventElement.element("messageEventDefinition") != null) || //
				//(intermediateEventElement.element("linkEventDefinition") != null);
		// All other event definition types cannot be intermediate throwing
		// (cancelEventDefinition, conditionalEventDefinition,
		// errorEventDefinition, terminateEventDefinition, timerEventDefinition
		if (signalEventDefinitionElement != null) {
			SignalEventDefinition signalDefinition = parseSignalEventDefinition(signalEventDefinitionElement);
			activityBehavior = new IntermediateThrowSignalEventActivityBehavior(signalDefinition);
		} else if (compensateEventDefinitionElement != null) {
			CompensateEventDefinition compensateEventDefinition = parseCompensateEventDefinition(compensateEventDefinitionElement, scopeElement);
			activityBehavior = new IntermediateThrowCompensationEventActivityBehavior(compensateEventDefinition);
			// IntermediateThrowNoneEventActivityBehavior
		}else if (messageEventDefinitionElement != null) {
			MessageEventDefinition messageEventDefinition = parseMessageEventDefinition(messageEventDefinitionElement, scopeElement);
			activityBehavior = new IntermediateThrowMessageEventActivityBehavior(messageEventDefinition);
			// IntermediateThrowNoneEventActivityBehavior
		}else if (linkEventDefinitionElement != null) {
			LinkEventDefinition linkEventDefinition = parseLinkEventDefinition(linkEventDefinitionElement, scopeElement);
			activityBehavior = new IntermediateThrowLinkEventActivityBehavior(linkEventDefinition);
		}
		
		else if (otherUnsupportedThrowingIntermediateEvent) {
			addError("Unsupported intermediate throw event type", intermediateEventElement);
		} else { // None intermediate event
			activityBehavior = new IntermediateThrowNoneEventActivityBehavior();
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseIntermediateThrowEvent(intermediateEventElement, scopeElement, nestedActivityImpl);
		}
		nestedActivityImpl.setActivityBehavior(activityBehavior);
		parseExecutionListenersOnScope(intermediateEventElement, nestedActivityImpl);
		return nestedActivityImpl;
	}
	private LinkEventDefinition parseLinkEventDefinition(Element linkEventDefinitionElement, IScope scopeElement) {
		Element targetElement = linkEventDefinitionElement.element("target");
		String value = targetElement.getText();
		Target target = new Target();
		target.setValue(value);
		LinkEventDefinition linkEventDefinition = new LinkEventDefinition();
		linkEventDefinition.setTarget(target);
		links.put(target.getValue(), linkEventDefinition);
		return linkEventDefinition;
	}
	protected MessageEventDefinition parseMessageEventDefinition(Element messageEventDefinitionElement, IScope scopeElement) {
		//没有实现
		String operationRef = messageEventDefinitionElement.attribute("operationRef");
		String messageRef = messageEventDefinitionElement.attribute("messageRef");
		MessageEventDefinition messageEventDefinition=null;
			return messageEventDefinition;

	}

	protected void parseIntemediateCompensateEventDefinition(Element compensateEventDefinition, IActivity compensateActivity, boolean isAfterEventBasedGateway)
	{
		compensateActivity.setProperty("type", "intermediateCompensateCatch");
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseIntermediateCompensateEventDefinition(compensateEventDefinition, compensateActivity);
		}
	}
	protected CompensateEventDefinition parseCompensateEventDefinition(Element compensateEventDefinitionElement, IScope scopeElement) {
		String activityRef = compensateEventDefinitionElement.attribute("activityRef");
		boolean waitForCompletion = "true".equals(compensateEventDefinitionElement.attribute("waitForCompletion", "true"));
		if (activityRef != null) {
			if (scopeElement.findActivity(activityRef) == null) {
				if(elementIds.contains(activityRef)==false)
				addError("Invalid attribute value for 'activityRef': no activity with id '" + activityRef + "' in current scope", compensateEventDefinitionElement);
			}	
		}
		CompensateEventDefinition compensateEventDefinition = new CompensateEventDefinition();
		compensateEventDefinition.setActivityRef(activityRef);
		compensateEventDefinition.setWaitForCompletion(waitForCompletion);
		return compensateEventDefinition;
	}
	protected void parseCatchCompensateEventDefinition(Element compensateEventDefinition, IActivity activity) {
		activity.setProperty("type", "compensationBoundaryCatch");
		ScopeImpl parent = (ScopeImpl) activity.getParent();
		for (IActivity child : parent.getActivities()) {
			if (child.getProperty("type").equals("compensationBoundaryCatch") && child != activity) {
				addError("multiple boundary events with compensateEventDefinition not supported on same activity", compensateEventDefinition);
			}
		}
	}
	protected ActivityBehavior parseBoundaryCancelEventDefinition(Element cancelEventDefinition, IActivity activity) {
		activity.setProperty("type", "cancelBoundaryCatch");
		ActivityImpl parent = (ActivityImpl) activity.getParent();
		if (!parent.getProperty("type").equals("transaction")) {
			addError("boundary event with cancelEventDefinition only supported on transaction subprocesses", cancelEventDefinition);
		}
		for (IActivity child : parent.getActivities()) {
			if (child.getProperty("type").equals("cancelBoundaryCatch") && child != activity) {
				addError("multiple boundary events with cancelEventDefinition not supported on same transaction subprocess", cancelEventDefinition);
			}
		}
		return new CancelBoundaryEventActivityBehavior();
	}
	/**
	 * Parses loopCharacteristics (standardLoop/Multi-instance) of an activity,
	 * if any is defined.
	 */
	public void parseMultiInstanceLoopCharacteristics(Element activityElement, IActivity activity) {
		// Only 'activities' (in the BPMN 2.0 spec meaning) can have mi
		// characteristics
		if (!(activity.getActivityBehavior() instanceof AbstractBpmnActivityBehavior)) {
			return;
		}
		Element miLoopCharacteristics = activityElement.element("multiInstanceLoopCharacteristics");
		if (miLoopCharacteristics != null) {
			MultiInstanceActivityBehavior miActivityBehavior = null;
			boolean isSequential = parseBooleanAttribute(miLoopCharacteristics.attribute("sequential"), false);
			if (isSequential) {
				miActivityBehavior = new SequentialMultiInstanceBehavior(activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior());
			} else {
				miActivityBehavior = new ParallelMultiInstanceBehavior(activity, (AbstractBpmnActivityBehavior) activity.getActivityBehavior());
			}
			activity.setScope(true);
			activity.setProperty("multiInstance", isSequential ? "sequential" : "parallel");
			activity.setActivityBehavior(miActivityBehavior);
			// loopCardinality
			Element loopCardinality = miLoopCharacteristics.element("loopCardinality");
			if (loopCardinality != null && loopCardinality.getText() != null && loopCardinality.getText().length() != 0) {
				String loopCardinalityText = loopCardinality.getText();
				if (loopCardinalityText == null || "".equals(loopCardinalityText)) {
					addError("loopCardinality must be defined for a multiInstanceLoopCharacteristics definition ", miLoopCharacteristics);
				}
				miActivityBehavior.setLoopCardinalityExpression(expressionManager.createExpression(loopCardinalityText));
			}
			// completionCondition
			Element completionCondition = miLoopCharacteristics.element("completionCondition");
			if (completionCondition != null) {
				String completionConditionText = completionCondition.getText();
				miActivityBehavior.setCompletionConditionExpression(expressionManager.createExpression("${"+completionConditionText+"}"));
			}
			{
				// activiti:collection
				String collection = miLoopCharacteristics.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "collection");
				if (collection != null) {
					if (collection.contains("{")) {
						miActivityBehavior.setCollectionExpression(expressionManager.createExpression(collection));
					} else {
						miActivityBehavior.setCollectionVariable(collection);
					}
				}
				// activiti:elementVariable
				String elementVariable = miLoopCharacteristics.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "elementVariable");
				if (elementVariable != null) {
					miActivityBehavior.setCollectionElementVariable(elementVariable);
				}
			
			}
			{
				// loopDataInputRef
				Element loopDataInputRef = miLoopCharacteristics.element("loopDataInputRef");
				if (loopDataInputRef != null) {
					String loopDataInputRefText = loopDataInputRef.getText();
					if (loopDataInputRefText != null) {
						if (loopDataInputRefText.contains("{")) {
							miActivityBehavior.setCollectionExpression(expressionManager.createExpression(loopDataInputRefText));
						} else {
							//miActivityBehavior.setCollectionVariable(loopDataInputRefText);
							miActivityBehavior.setCollectionExpression(expressionManager.createExpression("${"+loopDataInputRefText+"}"));
						}
					}
				}
				// dataInputItem
				Element inputDataItem = miLoopCharacteristics.element("inputDataItem");
				if (inputDataItem != null) {
					String inputDataItemName = inputDataItem.attribute("name");
					miActivityBehavior.setCollectionElementVariable(inputDataItemName);
				}
			
			}
			// Validation
			if (miActivityBehavior.getLoopCardinalityExpression() == null && miActivityBehavior.getCollectionExpression() == null && miActivityBehavior.getCollectionVariable() == null) {
				addError("Either loopCardinality or loopDataInputRef/activiti:collection must been set", miLoopCharacteristics);
			}
			// Validation
			if (miActivityBehavior.getCollectionExpression() == null && miActivityBehavior.getCollectionVariable() == null && miActivityBehavior.getCollectionElementVariable() != null) {
				addError("LoopDataInputRef/activiti:collection must be set when using inputDataItem or activiti:elementVariable", miLoopCharacteristics);
			}
			for (BpmnParseListener parseListener : parseListeners) {
				parseListener.parseMultiInstanceLoopCharacteristics(activityElement, miLoopCharacteristics, activity);
			}
		}
	}
	/**
	 * Parses the generic information of an activity element (id, name,
	 * documentation, etc.), and creates a new {@link ActivityImpl} on the given
	 * scope element.
	 */
	public IActivity createActivityOnScope(Element activityElement, IScope scopeElement) {
		String id = activityElement.attribute("id");
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.fine("Parsing activity " + id);
		}
		IActivity activity = scopeElement.createActivity(id);
		activity.setProperty("name", activityElement.attribute("name"));
		activity.setProperty("documentation", parseDocumentation(activityElement));
		activity.setProperty("default", activityElement.attribute("default"));
		activity.setProperty("type", activityElement.getTagName());
		activity.setProperty("line", activityElement.getLine());
		String isForCompensation = activityElement.attribute("isForCompensation");
		if (isForCompensation != null && (isForCompensation.equals("true") || isForCompensation.equals("TRUE"))) {
			activity.setProperty(PROPERTYNAME_IS_FOR_COMPENSATION, true);
		}
		return activity;
	}
	public String parseDocumentation(Element element) {
		Element docElement = element.element("documentation");
		if (docElement != null) {
			return docElement.getText().trim();
		}
		return null;
	}
	/**
	 * Parses an exclusive gateway declaration.
	 */
	public IActivity parseExclusiveGateway(Element exclusiveGwElement, IScope scope) {
		IActivity activity = createActivityOnScope(exclusiveGwElement, scope);
		activity.setActivityBehavior(new ExclusiveGatewayActivityBehavior());
		parseExecutionListenersOnScope(exclusiveGwElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseExclusiveGateway(exclusiveGwElement, scope, activity);
		}
		return activity;
	}
	public IActivity parseComplexGateway(Element complexGwElement, IScope scope) 
	{
		IActivity activity = createActivityOnScope(complexGwElement, scope);
		ComplexGatewayActivityBehavior complexGatewayActivityBehavior= new ComplexGatewayActivityBehavior();
		activity.setActivityBehavior(complexGatewayActivityBehavior);	
		String complexGatewayExpression = complexGwElement.attribute("activationCondition");
		complexGatewayActivityBehavior.setConditionExpression(new UelExpressionCondition(expressionManager.createExpression("${"+complexGatewayExpression+"}")));
		
		//((INoticeDefinition)complexGwElement).setProperty(PROPERTYNAME_COMPLEXGATEWAY_ACTIVATIONCONDITION, expression);
		parseExecutionListenersOnScope(complexGwElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseExclusiveGateway(complexGwElement, scope, activity);
		}
		
		return activity;
		
	}
	/**
	 * Parses an inclusive gateway declaration.
	 */
	public IActivity parseInclusiveGateway(Element inclusiveGwElement, IScope scope) {
		IActivity activity = createActivityOnScope(inclusiveGwElement, scope);
		activity.setActivityBehavior(new InclusiveGatewayActivityBehavior());
		parseExecutionListenersOnScope(inclusiveGwElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseInclusiveGateway(inclusiveGwElement, scope, activity);
		}
		return activity;
	}
	public IActivity parseEventBasedGateway(Element eventBasedGwElement, Element parentElement, IScope scope) {
		IActivity activity = createActivityOnScope(eventBasedGwElement, scope);
		activity.setActivityBehavior(new EventBasedGatewayActivityBehavior());
		activity.setScope(true);
		parseExecutionListenersOnScope(eventBasedGwElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseEventBasedGateway(eventBasedGwElement, scope, activity);
		}
		// find all outgoing sequence flows:
		List<Element> sequenceFlows = parentElement.elements("sequenceFlow");
		// collect all siblings in a map
		Map<String, Element> siblingsMap = new HashMap<String, Element>();
		List<Element> siblings = parentElement.elements();
		for (Element sibling : siblings) {
			siblingsMap.put(sibling.attribute("id"), sibling);
		}
		for (Element sequenceFlow : sequenceFlows) {
			String sourceRef = sequenceFlow.attribute("sourceRef");
			String targetRef = sequenceFlow.attribute("targetRef");
			if (activity.getId().equals(sourceRef)) {
				Element sibling = siblingsMap.get(targetRef);
				if (sibling != null) {
					if (sibling.getTagName().equals("intermediateCatchEvent")) {
						parseIntermediateCatchEvent(sibling, activity, true);
					} else {
						addError("Event based gateway can only be connected to elements of type intermediateCatchEvent", sibling);
					}
				}
			}
		}
		return activity;
	}
	/**
	 * Parses a parallel gateway declaration.
	 */
	public IActivity parseParallelGateway(Element parallelGwElement, IScope scope) {
		IActivity activity = createActivityOnScope(parallelGwElement, scope);
		activity.setActivityBehavior(new ParallelGatewayActivityBehavior());
		parseExecutionListenersOnScope(parallelGwElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseParallelGateway(parallelGwElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a scriptTask declaration.
	 */
	public IActivity parseScriptTask(Element scriptTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(scriptTaskElement, scope);
		String script = null;
		String language = null;
		String resultVariableName = null;
		Element scriptElement = scriptTaskElement.element("script");
		if (scriptElement != null) {
			script = scriptElement.getText();
			if (language == null) {
				language = scriptTaskElement.attribute("scriptFormat");
			}
			if (language == null) {
				language = ScriptingEngines.DEFAULT_SCRIPTING_LANGUAGE;
			}
			resultVariableName = scriptTaskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "resultVariable");
			if (resultVariableName == null) {
				// for backwards compatible reasons
				resultVariableName = scriptTaskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "resultVariableName");
			}
		}
		activity.setAsync(isAsync(scriptTaskElement));
		activity.setExclusive(isExclusive(scriptTaskElement));
		activity.setActivityBehavior(new ScriptTaskActivityBehavior(script, language, resultVariableName));
		parseExecutionListenersOnScope(scriptTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseScriptTask(scriptTaskElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a serviceTask declaration.
	 */
	public IActivity parseServiceTask(Element serviceTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(serviceTaskElement, scope);
		String type = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "type");
		String implementation = serviceTaskElement.attribute("implementation");
		String className = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "class");
		//String expression = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "expression");
		//String delegateExpression = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "delegateExpression");
		String resultVariableName = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariable");
		if (resultVariableName == null) {
			resultVariableName = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariableName");
		}
		activity.setAsync(isAsync(serviceTaskElement));
		activity.setExclusive(isExclusive(serviceTaskElement));
		if (type != null) {
			if (type.equalsIgnoreCase("mail")) {// 发邮件
				parseEmailServiceTask(activity, serviceTaskElement, parseFieldDeclarations(serviceTaskElement));
			} else if (type.equalsIgnoreCase("mule")) {//
				parseMuleServiceTask(activity, serviceTaskElement, parseFieldDeclarations(serviceTaskElement));
			} else if (type.equalsIgnoreCase("shell")) {// shell
				parseShellServiceTask(activity, serviceTaskElement, parseFieldDeclarations(serviceTaskElement));
			} else {
				addError("Invalid usage of type attribute: '" + type + "'", serviceTaskElement);
			}
		} else if (implementation != null && implementation.equalsIgnoreCase("WebService")) {
			String operationRef = this.resolveName(serviceTaskElement.attribute("operationRef"));
			if (operationRef == null || !this.operations.containsKey(operationRef)) {// webservice
				addError(operationRef + " does not exist", serviceTaskElement);
			} else {
				Operation operation = this.operations.get(operationRef);
				WebServiceActivityBehavior webServiceActivityBehavior = new WebServiceActivityBehavior(operation);
				Element ioSpecificationElement = serviceTaskElement.element("ioSpecification");
				if (ioSpecificationElement != null) {
					IOSpecification ioSpecification = this.parseIOSpecification(ioSpecificationElement);
					webServiceActivityBehavior.setIoSpecification(ioSpecification);
				}
				for (Element dataAssociationElement : serviceTaskElement.elements("dataInputAssociation")) {
					AbstractDataAssociation dataAssociation = this.parseDataInputAssociation(dataAssociationElement);
					webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
				}
				for (Element dataAssociationElement : serviceTaskElement.elements("dataOutputAssociation")) {
					AbstractDataAssociation dataAssociation = this.parseDataOutputAssociation(dataAssociationElement);
					webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
				}
				activity.setActivityBehavior(webServiceActivityBehavior);
			}
		} else if (implementation != null && implementation.equalsIgnoreCase("DelegateExpression")) {
			if (resultVariableName != null) {
				addError("'resultVariableName' not supported for service tasks using 'delegateExpression'",
						serviceTaskElement);
			}
			activity.setActivityBehavior(new ServiceTaskDelegateExpressionActivityBehavior(expressionManager
					.createExpression(className)));
		} else if (implementation != null && implementation.equalsIgnoreCase("Expression")) {
			if(className != null && className.trim().length() > 0){
				activity.setActivityBehavior(new ServiceTaskExpressionActivityBehavior(expressionManager
					.createExpression(className), resultVariableName));
			}
		} else if (implementation != null && implementation.equalsIgnoreCase("CallMethod")) {
			String methodName = serviceTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "method");
			if (className !=null && methodName != null ) {
				Object delegateInstance = ReflectUtil.instantiate(className);
				activity.setActivityBehavior(new ServiceTaskJavaMethodActivityBehavior(delegateInstance, methodName,
						parseFieldDeclarations(serviceTaskElement),resultVariableName));
			}else{
				addError("One of the attributes 'class', 'method' is mandatory on serviceTask", serviceTaskElement);
			}
		} else {
			boolean processed = false;
			if (implementation != null) {
				List<ExtensionServiceConfig> extensionServices = 
					BizProcessServer.getProcessEngineConfig().getExtensionconfig().getServiceExtension();
				for(ExtensionServiceConfig extensionService : extensionServices){
					if (implementation.equalsIgnoreCase(extensionService.getServiceName())){
						String implClass = extensionService.getImplClass();
						ExtensionService service = (ExtensionService)ReflectUtil.instantiate(implClass);
						service.parseService(serviceTaskElement, activity/*, parseFieldDeclarations(serviceTaskElement)*/);
						processed = true;
					}
				}
			}
			if (!processed){ 
				if (className != null && className.trim().length() > 0) {
					if (resultVariableName != null) {
						// addError("'resultVariableName' not supported for service tasks using 'class'",
						// serviceTaskElement);
					}
					activity.setActivityBehavior(new ClassDelegate(className, parseFieldDeclarations(serviceTaskElement)));
				} else {
					addError(
							"One of the attributes 'class', 'delegateExpression', 'type', 'operation', or 'expression' is mandatory on serviceTask.",
							serviceTaskElement);
				}
			}
		}
		parseExecutionListenersOnScope(serviceTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseServiceTask(serviceTaskElement, scope, activity);
		}
		activity.setProperty("resultVariableName", resultVariableName);
		return activity;
	}
	/**
	 * Parses a businessRuleTask declaration.
	 */
	public IActivity parseBusinessRuleTask(Element businessRuleTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(businessRuleTaskElement, scope);
		BusinessRuleTaskActivityBehavior ruleActivity = new BusinessRuleTaskActivityBehavior();
		String ruleVariableInputString = businessRuleTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "ruleVariablesInput");
		String rulesString = businessRuleTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "rules");
		String excludeString = businessRuleTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "exclude");
		String resultVariableNameString = businessRuleTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariable");
		activity.setAsync(isAsync(businessRuleTaskElement));
		activity.setExclusive(isExclusive(businessRuleTaskElement));
		if (resultVariableNameString == null) {
			resultVariableNameString = businessRuleTaskElement.attributeNS(NameSpaceConst.BIZEX_URL, "resultVariableName");
		}
		if (ruleVariableInputString != null) {
			String[] ruleVariableInputObjects = ruleVariableInputString.split(";");
			for (String ruleVariableInputObject : ruleVariableInputObjects) {
				ruleActivity.addRuleVariableInputIdExpression(expressionManager.createExpression(ruleVariableInputObject.trim()));
			}
		}
		if (rulesString != null) {
			String[] rules = rulesString.split(";");
			for (String rule : rules) {
				ruleActivity.addRuleIdExpression(expressionManager.createExpression(rule.trim()));
			}
			if (excludeString != null) {
				excludeString = excludeString.trim();
				if ("true".equalsIgnoreCase(excludeString) == false && "false".equalsIgnoreCase(excludeString) == false) {
					addError("'exclude' only supports true or false for business rule tasks", businessRuleTaskElement);
				} else {
					ruleActivity.setExclude(Boolean.valueOf(excludeString.toLowerCase()));
				}
			}
		} else if (excludeString != null) {
			addError("'exclude' not supported for business rule tasks not defining 'rules'", businessRuleTaskElement);
		}
		if (resultVariableNameString != null) {
			resultVariableNameString = resultVariableNameString.trim();
			if (resultVariableNameString.length() > 0 == false) {
				addError("'resultVariable' must contain a text value for business rule tasks", businessRuleTaskElement);
			} else {
				ruleActivity.setResultVariable(resultVariableNameString);
			}
		} else {
			ruleActivity.setResultVariable("rulesOutput");
		}
		activity.setActivityBehavior(ruleActivity);
		parseExecutionListenersOnScope(businessRuleTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseBusinessRuleTask(businessRuleTaskElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a sendTask declaration.
	 */
	public IActivity parseSendTask(Element sendTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(sendTaskElement, scope);
		activity.setAsync(isAsync(sendTaskElement));
		activity.setExclusive(isExclusive(sendTaskElement));
		// for e-mail
		String type = sendTaskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "type");
		// for web service
		String implementation = sendTaskElement.attribute("implementation");
		String operationRef = this.resolveName(sendTaskElement.attribute("operationRef"));
		// for e-mail
		if (type != null) {
			if (type.equalsIgnoreCase("mail")) {
				parseEmailServiceTask(activity, sendTaskElement, parseFieldDeclarations(sendTaskElement));
			} else if (type.equalsIgnoreCase("mule")) {
				parseMuleServiceTask(activity, sendTaskElement, parseFieldDeclarations(sendTaskElement));
			} else {
				addError("Invalid usage of type attribute: '" + type + "'", sendTaskElement);
			}
			// for web service
		} else if (implementation != null && operationRef != null && implementation.equalsIgnoreCase("##WebService")) {
			if (!this.operations.containsKey(operationRef)) {
				addError(operationRef + " does not exist", sendTaskElement);
			} else {
				Operation operation = this.operations.get(operationRef);
				WebServiceActivityBehavior webServiceActivityBehavior = new WebServiceActivityBehavior(operation);
				Element ioSpecificationElement = sendTaskElement.element("ioSpecification");
				if (ioSpecificationElement != null) {
					IOSpecification ioSpecification = this.parseIOSpecification(ioSpecificationElement);
					webServiceActivityBehavior.setIoSpecification(ioSpecification);
				}
				for (Element dataAssociationElement : sendTaskElement.elements("dataInputAssociation")) {
					AbstractDataAssociation dataAssociation = this.parseDataInputAssociation(dataAssociationElement);
					webServiceActivityBehavior.addDataInputAssociation(dataAssociation);
				}
				for (Element dataAssociationElement : sendTaskElement.elements("dataOutputAssociation")) {
					AbstractDataAssociation dataAssociation = this.parseDataOutputAssociation(dataAssociationElement);
					webServiceActivityBehavior.addDataOutputAssociation(dataAssociation);
				}
				activity.setActivityBehavior(webServiceActivityBehavior);
			}
		} else {
			addError("One of the attributes 'type' or 'operation' is mandatory on sendTask.", sendTaskElement);
		}
		parseExecutionListenersOnScope(sendTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseSendTask(sendTaskElement, scope, activity);
		}
		return activity;
	}
	protected AbstractDataAssociation parseDataOutputAssociation(Element dataAssociationElement) {
		String targetRef = dataAssociationElement.element("targetRef").getText();
		if (dataAssociationElement.element("sourceRef") != null) {
			String sourceRef = dataAssociationElement.element("sourceRef").getText();
			return new MessageImplicitDataOutputAssociation(targetRef, sourceRef);
		} else {
			Expression transformation = this.expressionManager.createExpression(dataAssociationElement.element("transformation").getText());
			AbstractDataAssociation dataOutputAssociation = new TransformationDataOutputAssociation(null, targetRef, transformation);
			return dataOutputAssociation;
		}
	}
	protected void parseMuleServiceTask(IActivity activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
		try {
			Class<?> theClass = Class.forName("org.activiti.mule.MuleSendActivitiBehavior");
			activity.setActivityBehavior((ActivityBehavior) ClassDelegate.instantiateDelegate(theClass, fieldDeclarations));
		} catch (ClassNotFoundException e) {
			addError("Could not find org.activiti.mule.MuleSendActivitiBehavior", serviceTaskElement);
		}
	}
	protected void parseEmailServiceTask(IActivity activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
		validateFieldDeclarationsForEmail(serviceTaskElement, fieldDeclarations);
		activity.setActivityBehavior((MailActivityBehavior) ClassDelegate.instantiateDelegate(MailActivityBehavior.class, fieldDeclarations));
	}
	protected void parseShellServiceTask(IActivity activity, Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
		validateFieldDeclarationsForShell(serviceTaskElement, fieldDeclarations);
		activity.setActivityBehavior((ActivityBehavior) ClassDelegate.instantiateDelegate(ShellActivityBehavior.class, fieldDeclarations));
	}
	protected void validateFieldDeclarationsForEmail(Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
		boolean toDefined = false;
		boolean textOrHtmlDefined = false;
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			if (fieldDeclaration.getName().equals("to")) {
				toDefined = true;
			}
			if (fieldDeclaration.getName().equals("html")) {
				textOrHtmlDefined = true;
			}
			if (fieldDeclaration.getName().equals("text")) {
				textOrHtmlDefined = true;
			}
		}
		if (!toDefined) {
			addError("No recipient is defined on the mail activity", serviceTaskElement);
		}
		if (!textOrHtmlDefined) {
			addError("Text or html field should be provided", serviceTaskElement);
		}
	}
	protected void validateFieldDeclarationsForShell(Element serviceTaskElement, List<FieldDeclaration> fieldDeclarations) {
		boolean shellCommandDefined = false;
		for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
			String fieldName = fieldDeclaration.getName();
			FixedValue fieldFixedValue = (FixedValue) fieldDeclaration.getValue();
			String fieldValue = fieldFixedValue.getExpressionText();
			shellCommandDefined |= fieldName.equals("command");
			if ((fieldName.equals("wait") || fieldName.equals("redirectError") || fieldName.equals("cleanEnv")) && !fieldValue.toLowerCase().equals("true")
					&& !fieldValue.toLowerCase().equals("false")) {
				addError("undefined value for shell " + fieldName + " parameter :" + fieldValue.toString(), serviceTaskElement);
			}
		}
		if (!shellCommandDefined) {
			addError("No shell command is defined on the shell activity", serviceTaskElement);
		}
	}
	public List<FieldDeclaration> parseFieldDeclarations(Element element) {
		List<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
		Element elementWithFieldInjections = element.element("extensionElements");
		if (elementWithFieldInjections == null) { // Custom extensions will just
			// have the <field.. as a
			// subelement
			elementWithFieldInjections = element;
		}
		List<Element> fieldDeclarationElements = elementWithFieldInjections.elementsNS(NameSpaceConst.BIZEX_URL, "field");
		if (fieldDeclarationElements != null && !fieldDeclarationElements.isEmpty()) {
			for (Element fieldDeclarationElement : fieldDeclarationElements) {
				FieldDeclaration fieldDeclaration = parseFieldDeclaration(element, fieldDeclarationElement);
				if (fieldDeclaration != null) {
					fieldDeclarations.add(fieldDeclaration);
				}
			}
		}
		return fieldDeclarations;
	}
	protected FieldDeclaration parseFieldDeclaration(Element serviceTaskElement, Element fieldDeclarationElement) {
		String fieldName = fieldDeclarationElement.attribute("name");
		FieldDeclaration fieldDeclaration = parseStringFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
		if (fieldDeclaration == null) {
			fieldDeclaration = parseExpressionFieldDeclaration(fieldDeclarationElement, serviceTaskElement, fieldName);
		}
		if (fieldDeclaration == null) {
			addError("One of the following is mandatory on a field declaration: one of attributes stringValue|expression " + "or one of child elements string|expression", serviceTaskElement);
		}
		return fieldDeclaration;
	}
	protected FieldDeclaration parseStringFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
		try {
			String fieldValue = getStringValueFromAttributeOrElement("stringValue", "string", fieldDeclarationElement);
			if (fieldValue != null) {
				return new FieldDeclaration(fieldName, Expression.class.getName(), new FixedValue(fieldValue));
			}
		} catch (WorkflowException ae) {
			if (ae.getMessage().contains("multiple elements with tag name")) {
				addError("Multiple string field declarations found", serviceTaskElement);
			} else {
				addError("Error when paring field declarations: " + ae.getMessage(), serviceTaskElement);
			}
		}
		return null;
	}
	protected FieldDeclaration parseExpressionFieldDeclaration(Element fieldDeclarationElement, Element serviceTaskElement, String fieldName) {
		try {
			String expression = getStringValueFromAttributeOrElement("expression", "expression", fieldDeclarationElement);
			if (expression != null && expression.trim().length() > 0) {
				String type = fieldDeclarationElement.attribute("type");
				return new FieldDeclaration(fieldName, type, expressionManager.createExpression(expression));
			}
		} catch (WorkflowException ae) {
			if (ae.getMessage().contains("multiple elements with tag name")) {
				addError("Multiple expression field declarations found", serviceTaskElement);
			} else {
				addError("Error when paring field declarations: " + ae.getMessage(), serviceTaskElement);
			}
		}
		return null;
	}
	protected String getStringValueFromAttributeOrElement(String attributeName, String elementName, Element element) {
		String value = null;
		String attributeValue = element.attribute(attributeName);
		Element childElement = element.elementNS(NameSpaceConst.BIZEX_URL, elementName);
		String stringElementText = null;
		if (attributeValue != null && childElement != null) {
			addError("Can't use attribute '" + attributeName + "' and element '" + elementName + "' together, only use one", element);
		} else if (childElement != null) {
			stringElementText = childElement.getText();
			if (stringElementText == null || stringElementText.length() == 0) {
				addError("No valid value found in attribute '" + attributeName + "' nor element '" + elementName + "'", element);
			} else {
				// Use text of element
				value = stringElementText;
			}
		} else if (attributeValue != null && attributeValue.length() > 0) {
			// Using attribute
			value = "${" + attributeValue + "}";
		}
		return value;
	}
	/**
	 * Parses a task with no specific type (behaves as passthrough).
	 */
	public IActivity parseTask(Element taskElement, IScope scope) {
		IActivity activity = createActivityOnScope(taskElement, scope);
		activity.setActivityBehavior(new TaskActivityBehavior());
		activity.setAsync(isAsync(taskElement));
		activity.setExclusive(isExclusive(taskElement));
		parseExecutionListenersOnScope(taskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseTask(taskElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a manual task.
	 */
	public IActivity parseManualTask(Element manualTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(manualTaskElement, scope);
		activity.setActivityBehavior(new ManualTaskActivityBehavior());
		parseExecutionListenersOnScope(manualTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseManualTask(manualTaskElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a receive task.
	 */
	public IActivity parseReceiveTask(Element receiveTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(receiveTaskElement, scope);
		activity.setActivityBehavior(new ReceiveTaskActivityBehavior());
		activity.setAsync(isAsync(receiveTaskElement));
		activity.setExclusive(isExclusive(receiveTaskElement));
		parseExecutionListenersOnScope(receiveTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseReceiveTask(receiveTaskElement, scope, activity);
		}
		return activity;
	}
	/* userTask specific finals */
	protected static final String HUMAN_PERFORMER = "humanPerformer";
	protected static final String POTENTIAL_OWNER = "potentialOwner";
	protected static final String RESOURCE_ASSIGNMENT_EXPR = "resourceAssignmentExpression";
	protected static final String FORMAL_EXPRESSION = "formalExpression";
	protected static final String USER_PREFIX = "user(";
	protected static final String GROUP_PREFIX = "group(";
	protected static final String ASSIGNEE_EXTENSION = "assignee";
	protected static final String CANDIDATE_USERS_EXTENSION = "candidateUsers";
	protected static final String CANDIDATE_GROUPS_EXTENSION = "candidateGroups";
	protected static final String DUE_DATE_EXTENSION = "dueDate";
	protected static final String PRIORITY_EXTENSION = "priority";
	/**
	 * Parses a userTask declaration.
	 */
	public IActivity parseUserTask(Element userTaskElement, IScope scope) {
		IActivity activity = createActivityOnScope(userTaskElement, scope);
		activity.setAsync(isAsync(userTaskElement));
		activity.setExclusive(isExclusive(userTaskElement));
		TaskDefinition taskDefinition = parseTaskDefinition(userTaskElement, activity.getId(), (IProcessDefinition) scope.getProcessDefinition());
		UserTaskActivityBehavior userTaskActivity = new UserTaskActivityBehavior(expressionManager, taskDefinition);
		activity.setActivityBehavior(userTaskActivity);
		parseProperties(userTaskElement, activity);
		parseExecutionListenersOnScope(userTaskElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseUserTask(userTaskElement, scope, activity);
		}
		return activity;
	}
	
	public TaskDefinition parseTaskDefinition(Element taskElement, String taskDefinitionKey, IProcessDefinition processDefinition) {
		TaskFormHandler taskFormHandler;
		String taskFormHandlerClassName = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "formHandlerClass");
		if (taskFormHandlerClassName != null) {
			taskFormHandler = (TaskFormHandler) ReflectUtil.instantiate(taskFormHandlerClassName);
		} else {
			taskFormHandler = new DefaultTaskFormHandler();
		}
		taskFormHandler.parseConfiguration(taskElement, deployment, processDefinition, this);
		TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);
		taskDefinition.setKey(taskDefinitionKey);
		processDefinition.getTaskDefinitions().put(taskDefinitionKey, taskDefinition);
		String name = taskElement.attribute("name");
		if (name != null) {
			taskDefinition.setNameExpression(expressionManager.createExpression(name));
		}
		String descriptionStr = parseDocumentation(taskElement);
		if (descriptionStr != null) {
			taskDefinition.setDescriptionExpression(expressionManager.createExpression(descriptionStr));
		}
		boolean isMakeBill = Boolean.parseBoolean(taskElement.attribute("makeBill"));
		String openUIStyle = taskElement.attribute("openUIStyle");
		String openURI  = taskElement.attribute("openURI");
		String sequence  = taskElement.attribute("sequence");
		String afterSign  = taskElement.attribute("afterSign");
		//TODO 处理打开方式调整,如果设置了formproperty 则默认设置为definedUI，openUIStyle不等于customURI，则uri清空
		//if(taskFormHandler. .getFormPropertyHandlers())
		taskDefinition.setOpenUIStyle(openUIStyle);
		taskDefinition.setOpenURI(openURI);
		taskDefinition.setMakeBill(isMakeBill);
		if(sequence!=null && !sequence.equals(""))
		{
			taskDefinition.setSequence(Boolean.parseBoolean(sequence));
		}
		if(afterSign!=null && !afterSign.equals(""))
		{
			taskDefinition.setAfterSign(Boolean.parseBoolean(afterSign));
		}
		parseParticipants(taskElement, taskDefinition);
		parseNotices(taskElement, taskDefinition);
		parseTaskHandlings(taskElement, taskDefinition);
		parseHumanPerformer(taskElement, taskDefinition);
		parserTaskListener(taskElement, taskDefinition);
		parsePotentialOwner(taskElement, taskDefinition);
		parseUserTaskPolicyControl(taskElement, taskDefinition);
		// Activiti custom extension
		parseUserTaskCustomExtensions(taskElement, taskDefinition);
		return taskDefinition;
	}
	
	public void parseCollaborationParticipants(Element userTaskPolicyControlElement, TaskDefinition taskDefinition) {
		List<IParticipant> participants = new ArrayList<IParticipant>();
		//Element extentionsElement = userTaskPolicyControlElement.element("extensionElements");
		if (userTaskPolicyControlElement != null) {
			List<Element> participantElements = userTaskPolicyControlElement.elements("collaborationParticipants");
			for (Element participantElement : participantElements) {
				IParticipant participant = new BasicParticipant();
				participant.setID(participantElement.attribute("id"));
				participant.setName(participantElement.attribute("name"));
				participant.setParticipantID(participantElement.attribute("participantID"));
				Element participantTypeElement = participantElement.element("participantType");
				if (participantTypeElement != null) {
					IParticipantType participantType = ParticipantTypeFactory.getInstance().getType(participantTypeElement.attribute("code"));
					participant.setParticipantType(participantType);
				}
				Element participantFilterTypeElement = participantElement.element("participantFilterType");
				if (participantFilterTypeElement != null) {
					IParticipantFilterType participantFilterType = ParticipantFilterTypeFactory.getInstance().getFilterType(participantFilterTypeElement.attribute("participantFilterType"));
					participant.setParticipantFilterType(participantFilterType);
				}
				participants.add(participant);
			}
		}
		taskDefinition.setCollaborationParticipants(participants);
	}

	
	public void parseUserTaskPolicyControl(Element taskElement, TaskDefinition taskDefinition) {
		Element extentionsElement = taskElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> userTaskPolicyControlElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "userTaskPolicyControl");
			for (Element userTaskPolicyControlElement : userTaskPolicyControlElements) {
				taskDefinition.setAddSign(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canAddSign")));
				taskDefinition.setAssign(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canAssign")));
				taskDefinition.setDelegate(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canDelegate")));
				
				taskDefinition.setApprove(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("approve")));			//审批，使用动作标记
				taskDefinition.setDeliver(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("deliver")));			//传阅，使用固定的内置逻辑
				taskDefinition.setUndertake(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("undertake")));		//承办，
				//protected String processClass;	//处理类
				//protected String form;			//表单
				//protected void modifyResources(){}	//修改审批对话框资源信息
				taskDefinition.setCanTransfer(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canTransfer")));		//可转发
				taskDefinition.setCanDeliver(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canDeliver")));		//可传阅
				taskDefinition.setOpinionEditable(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("opinionEditable")));	//可编辑意见
				taskDefinition.setOpinionNullable(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("opinionNullable")));	//是否意见可空
				taskDefinition.setCanHasten(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canHasten")));		//允许催办
				taskDefinition.setCanPrint(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canPrint")));			//允许打印
				taskDefinition.setCanRecycle(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canRecycle")));		//允许收回
				taskDefinition.setCanPassthrough(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canPassthrough"))); 	//允许快速通道
				taskDefinition.setCanUploadAttachment(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canUploadAttachment")));	//允许附件上传
				taskDefinition.setCanDownloadAttachment(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canDownloadAttachment")));//允许附件下载
				taskDefinition.setCanDeleteAttachment(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canDeleteAttachment")));	//允许附件删除
				taskDefinition.setCanModifyAttachment(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canModifyAttachment")));	//允许附件修改
				taskDefinition.setCanViewAttachment(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canViewAttachment")));	//允许附件查看
				parseCollaborationParticipants(userTaskPolicyControlElement, taskDefinition);
				//protected Object voucherPrivilege;
				taskDefinition.setCanReject(Boolean.parseBoolean(userTaskPolicyControlElement.attribute("canReject"))); 		//禁止回退，允许回退
				taskDefinition.setRejectPolicy(userTaskPolicyControlElement.attribute("rejectPolicy")); 		//上一步，制单人，全部活动，指定活动
				taskDefinition.setActivityRef(userTaskPolicyControlElement.attribute("activityRef"));
			}
		}
	}
	
	private void parserTaskListener(Element taskElement, TaskDefinition taskDefinition) {
		Map<String, List<ITaskListener>> taskListener = taskDefinition.getTaskListeners();
		if (taskListener == null) {
			taskListener = new HashMap<String, List<ITaskListener>>();
		}
		List<ITaskListener> list = null;
		Element extentionsElement = taskElement.element("extensionElements");
		if (extentionsElement == null) {
			taskDefinition.setTaskListeners(null);
		}
		List<Element> taskListenerElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "taskListener");
		if (taskListenerElements == null) {
			taskDefinition.setTaskListeners(null);
		}
		for (int i = 0; i < taskListenerElements.size(); i++) {
			Element element = taskListenerElements.get(i);
			String eventName = element.attribute("event");
			list = taskListener.get(eventName);
			if (list == null) {
				list = new ArrayList<ITaskListener>();
				taskListener.put(eventName, list);
			}
			list.add((ITaskListener) ClassUtil.loadClass(element.attribute("implementation")));
		}
	}

	public void parseParticipants(Element taskElement, TaskDefinition taskDefinition) {
		List<IParticipant> participants = new ArrayList<IParticipant>();
		Element extentionsElement = taskElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> participantElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "participant");
			for (Element participantElement : participantElements) {
				IParticipant participant = new BasicParticipant();
				participant.setID(participantElement.attribute("id"));
				participant.setName(participantElement.attribute("name"));
				participant.setParticipantID(participantElement.attribute("participantID"));
				Element participantTypeElement = participantElement.element("participantType");
				if (participantTypeElement != null) {
					IParticipantType participantType = ParticipantTypeFactory.getInstance().getType(participantTypeElement.attribute("code"));
					participant.setParticipantType(participantType);
				}
				Element participantFilterTypeElement = participantElement.element("participantFilterType");
				if (participantFilterTypeElement != null) {
					IParticipantFilterType participantFilterType = ParticipantFilterTypeFactory.getInstance().getFilterType(participantFilterTypeElement.attribute("participantFilterType"));
					participant.setParticipantFilterType(participantFilterType);
				}
				participants.add(participant);
			}
		}
		taskDefinition.setParticipants(participants);
	}

	public void parseNotices(Element taskElement, TaskDefinition taskDefinition) {
		List<INoticeDefinition> notices = new ArrayList<INoticeDefinition>();
		Element extentionsElement = taskElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> noticeElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "notice");
			for (Element noticeElement : noticeElements) {
				INoticeDefinition notice = new NoticeDefinition();
				notice.setHasReceipt(Boolean.parseBoolean(noticeElement.attribute("hasReceipt")));
				String noticeTimeTypeStrValue = noticeElement.attribute("noticeTime");
				if (noticeTimeTypeStrValue != null && noticeTimeTypeStrValue != "") {
					NoticeTimeTypeEnum noticeTimeType = (NoticeTimeTypeEnum) Enum.valueOf(NoticeTimeTypeEnum.class, noticeTimeTypeStrValue);
					notice.setNoticeTime(noticeTimeType);
				}
				notice.setCondition(noticeElement.attribute("condition"));
				notice.setContentTemplate(noticeElement.attribute("contentTemplate"));
				Element noticeTypeElement = noticeElement.element("noticeType");
				if (noticeTypeElement != null) {
					INoticeType noticeType = NoticeTypeFactory.getInstance().getType(noticeTypeElement.attribute("code"));
					notice.setNoticeType(noticeType);
				}
				List<Element> receiverElements = noticeElement.elements("receiver");
				for (Element receiverElement : receiverElements) {
					ReceiverVO receiverVO = new ReceiverVO();
					receiverVO.setCorppk(receiverElement.attribute("corppk"));
					receiverVO.setType(Integer.parseInt(receiverElement.attribute("type")));
					receiverVO.setPk(receiverElement.attribute("pk"));
					receiverVO.setCode(receiverElement.attribute("code"));
					receiverVO.setName(receiverElement.attribute("name"));
					notice.getReceivers().add(receiverVO);
				}
				notices.add(notice);
			}
		}
		taskDefinition.setNotices(notices);
	}
	public void parseTaskHandlings(Element taskElement, TaskDefinition taskDefinition) {
		List<ITaskHandlingDefinition> taskHandlings = new ArrayList<ITaskHandlingDefinition>();
		Element extentionsElement = taskElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> taskHandlingElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "taskHandling");
			for (Element taskHandlingElement : taskHandlingElements) {
				ITaskHandlingDefinition taskHandling = new TaskHandlingDefinition();
				Element taskHandleTypeElement = taskHandlingElement.element("taskHandleType");
				if (taskHandleTypeElement != null) {
					ITaskHandlingType taskHandlingType = TaskHandlingTypeFactory.getInstance().getType(taskHandleTypeElement.attribute("code"));
					taskHandling.setTaskHandleType(taskHandlingType);
				}
				taskHandlings.add(taskHandling);
			}
		}
		taskDefinition.setTaskHandlings(taskHandlings);
	}
	protected void parseHumanPerformer(Element taskElement, TaskDefinition taskDefinition) {
		List<Element> humanPerformerElements = taskElement.elements(HUMAN_PERFORMER);
		if (humanPerformerElements.size() > 1) {
			addError("Invalid task definition: multiple " + HUMAN_PERFORMER + " sub elements defined for " + taskDefinition.getNameExpression(), taskElement);
		} else if (humanPerformerElements.size() == 1) {
			Element humanPerformerElement = humanPerformerElements.get(0);
			if (humanPerformerElement != null) {
				parseHumanPerformerResourceAssignment(humanPerformerElement, taskDefinition);
			}
		}
	}
	protected void parsePotentialOwner(Element taskElement, TaskDefinition taskDefinition) {
		List<Element> potentialOwnerElements = taskElement.elements(POTENTIAL_OWNER);
		for (Element potentialOwnerElement : potentialOwnerElements) {
			parsePotentialOwnerResourceAssignment(potentialOwnerElement, taskDefinition);
		}
	}
	protected void parseHumanPerformerResourceAssignment(Element performerElement, TaskDefinition taskDefinition) {
		Element raeElement = performerElement.element(RESOURCE_ASSIGNMENT_EXPR);
		if (raeElement != null) {
			Element feElement = raeElement.element(FORMAL_EXPRESSION);
			if (feElement != null) {
				taskDefinition.setAssigneeExpression(expressionManager.createExpression(feElement.getText()));
			}
		}
	}
	protected void parsePotentialOwnerResourceAssignment(Element performerElement, TaskDefinition taskDefinition) {
		Element raeElement = performerElement.element(RESOURCE_ASSIGNMENT_EXPR);
		if (raeElement != null) {
			Element feElement = raeElement.element(FORMAL_EXPRESSION);
			if (feElement != null) {
				List<String> assignmentExpressions = parseCommaSeparatedList(feElement.getText());
				for (String assignmentExpression : assignmentExpressions) {
					assignmentExpression = assignmentExpression.trim();
					if (assignmentExpression.startsWith(USER_PREFIX)) {
						String userAssignementId = getAssignmentId(assignmentExpression, USER_PREFIX);
						taskDefinition.addCandidateUserIdExpression(expressionManager.createExpression(userAssignementId));
					} else if (assignmentExpression.startsWith(GROUP_PREFIX)) {
						String groupAssignementId = getAssignmentId(assignmentExpression, GROUP_PREFIX);
						taskDefinition.addCandidateGroupIdExpression(expressionManager.createExpression(groupAssignementId));
					} else { // default: given string is a goupId, as-is.
						taskDefinition.addCandidateGroupIdExpression(expressionManager.createExpression(assignmentExpression));
					}
				}
			}
		}
	}
	protected String getAssignmentId(String expression, String prefix) {
		return expression.substring(prefix.length(), expression.length() - 1).trim();
	}
	protected void parseUserTaskCustomExtensions(Element taskElement, TaskDefinition taskDefinition) {
		// assignee
		String assignee = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, ASSIGNEE_EXTENSION);
		if (assignee != null) {
			if (taskDefinition.getAssigneeExpression() == null) {
				taskDefinition.setAssigneeExpression(expressionManager.createExpression(assignee));
			} else {
				addError("Invalid usage: duplicate assignee declaration for task " + taskDefinition.getNameExpression(), taskElement);
			}
		}
		// Candidate users
		String candidateUsersString = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, CANDIDATE_USERS_EXTENSION);
		if (candidateUsersString != null) {
			List<String> candidateUsers = parseCommaSeparatedList(candidateUsersString);
			for (String candidateUser : candidateUsers) {
				taskDefinition.addCandidateUserIdExpression(expressionManager.createExpression(candidateUser.trim()));
			}
		}
		// Candidate groups
		String candidateGroupsString = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, CANDIDATE_GROUPS_EXTENSION);
		if (candidateGroupsString != null) {
			List<String> candidateGroups = parseCommaSeparatedList(candidateGroupsString);
			for (String candidateGroup : candidateGroups) {
				taskDefinition.addCandidateGroupIdExpression(expressionManager.createExpression(candidateGroup.trim()));
			}
		}
		// Task listeners
		parseTaskListeners(taskElement, taskDefinition);
		// Due date
		String dueDateExpression = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, DUE_DATE_EXTENSION);
		if (dueDateExpression != null) {
			taskDefinition.setDueDateExpression(expressionManager.createExpression(dueDateExpression));
		}
		// Priority
		final String priorityExpression = taskElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, PRIORITY_EXTENSION);
		if (priorityExpression != null) {
			taskDefinition.setPriorityExpression(expressionManager.createExpression(priorityExpression));
		}
	}
	/**
	 * Parses the given String as a list of comma separated entries, where an
	 * entry can possibly be an expression that has comma's.
	 * 
	 * If somebody is smart enough to write a regex for this, please let us
	 * know.
	 * 
	 * @return the entries of the comma separated list, trimmed.
	 */
	protected List<String> parseCommaSeparatedList(String s) {
		List<String> result = new ArrayList<String>();
		if (s != null && !"".equals(s)) {
			StringCharacterIterator iterator = new StringCharacterIterator(s);
			char c = iterator.first();
			StringBuilder strb = new StringBuilder();
			boolean insideExpression = false;
			while (c != StringCharacterIterator.DONE) {
				if (c == '{' || c == '$') {
					insideExpression = true;
				} else if (c == '}') {
					insideExpression = false;
				} else if (c == ',' && !insideExpression) {
					result.add(strb.toString().trim());
					strb.delete(0, strb.length());
				}
				if (c != ',' || (insideExpression)) {
					strb.append(c);
				}
				c = iterator.next();
			}
			if (strb.length() > 0) {
				result.add(strb.toString().trim());
			}
		}
		return result;
	}
	
	protected List<Object> parseCommonExtensionListener(String listenerType){
		List<Object> extListener = new ArrayList<Object>();
		List<ExtensionListenerConfig> listenerConfigs = 
			BizProcessServer.getProcessEngineConfig().getExtensionconfig().getListenerExtension();
		
		for(ExtensionListenerConfig listener : listenerConfigs){
			if(listener.getListenerType().equalsIgnoreCase(listenerType) && StringUtil.isEmpty(listener.getEvent())){
				extListener.add(ClassUtil.loadClass(listener.getImplementation()));
			}
		}
		return extListener;
	}

	protected void parseTaskListeners(Element userTaskElement, TaskDefinition taskDefinition) {
		//处理内置的Listener
		List<Object> listenres = parseCommonExtensionListener("task");
		for(Object listener : listenres )
			if(listener instanceof ITaskListener){
				taskDefinition.addTaskListener("taskListener", (ITaskListener)listener);
			}
			else{
				WorkflowLogger.error("监听类"+listener.getClass()+"没有实现接口ITaskListener");
				throw new WorkflowRuntimeException("监听类"+listener.getClass()+"没有实现接口ITaskListener");
			}
		//解析
		Element extentionsElement = userTaskElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> taskListenerElements = extentionsElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "taskListener");
			for (Element taskListenerElement : taskListenerElements) {
				String eventName = taskListenerElement.attribute("event");
				if (eventName != null) {
					if (ITaskListener.EVENTNAME_CREATE.equals(eventName) || 
						ITaskListener.EVENTNAME_DELEGATE_AFTER.equals(eventName) || 
						ITaskListener.EVENTNAME_COMPLETE_AFTER.equals(eventName)) {
						ITaskListener taskListener = parseTaskListener(taskListenerElement);
						taskDefinition.addTaskListener(eventName, taskListener);
					} else {
						addError("Invalid eventName for taskListener: choose 'create' |'assignment'", userTaskElement);
					}
				} else {
					addError("Event is mandatory on taskListener", userTaskElement);
				}
			}
		}
	}
	
	protected ITaskListener parseTaskListener(Element taskListenerElement) {
		ITaskListener taskListener = null;
		String implType = taskListenerElement.attribute("implementationType");
		if (implType != null) {
			String implementation = taskListenerElement.attribute("implementation");
			//String expression = executionListenerElement.attribute("expression");
			//String delegateExpression = executionListenerElement.attribute("delegateExpression");
			if (implType.equalsIgnoreCase("Standard")) {
				taskListener = new ClassDelegate(implementation, parseFieldDeclarations(taskListenerElement));
			} else if (implType.equalsIgnoreCase("Expression")) {
				taskListener = new ExpressionTaskListener(expressionManager.createExpression(implementation));
			} else if (implType.equalsIgnoreCase("DelegateExpression")) {
				taskListener = new DelegateExpressionTaskListener(expressionManager.createExpression(implementation));
			}else if (implType.equalsIgnoreCase("CallMethod")){
				ListenerDefinition definition = parseListenerDefinition("taskListener", taskListenerElement);
				taskListener = new CallMethodTaskListener();
				((ExtTaskListener)taskListener).setListenerDefinition(definition);
			} else {
				ListenerDefinition definition = parseListenerDefinition("taskListener", taskListenerElement);
				taskListener = (ITaskListener)parseExtListener(definition, taskListenerElement);
				if (taskListener == null)
					addError("Element 'class' or 'expression' is mandatory on taskListener", taskListenerElement);
			}
		}
		return taskListener;
	}

/*	protected ITaskListener parseTaskListener(Element taskListenerElement) {
		ITaskListener taskListener = null;
		String className = taskListenerElement.attribute("class");
		String expression = taskListenerElement.attribute("expression");
		String delegateExpression = taskListenerElement.attribute("delegateExpression");
		if (className != null) {
			taskListener = new ClassDelegate(className, parseFieldDeclarations(taskListenerElement));
		} else if (expression != null) {
			taskListener = new ExpressionTaskListener(expressionManager.createExpression(expression));
		} else if (delegateExpression != null) {
			taskListener = new DelegateExpressionTaskListener(expressionManager.createExpression(delegateExpression));
		} else {
			addError("Element 'class' or 'expression' is mandatory on taskListener", taskListenerElement);
		}
		return taskListener;
	}
*/	/**
	 * Parses the end events of a certain level in the process (process,
	 * subprocess or another scope).
	 * 
	 * @param parentElement
	 *            The 'parent' element that contains the end events (process,
	 *            subprocess).
	 * @param scope
	 *            The {@link ScopeImpl} to which the end events must be added.
	 */
	public void parseEndEvents(Element parentElement, IScope scope) {
		for (Element endEventElement : parentElement.elements("endEvent")) {
			IActivity activity = createActivityOnScope(endEventElement, scope);
			Element errorEventDefinition = endEventElement.element("errorEventDefinition");
			Element cancelEventDefinition = endEventElement.element("cancelEventDefinition");
			Element compensateEventDefinition = endEventElement.element("compensateEventDefinition");
			Element signalEventDefinition = endEventElement.element("signalEventDefinition");
			if (errorEventDefinition != null) { // error end event
				String errorRef = errorEventDefinition.attribute("errorRef");
				if (errorRef == null || "".equals(errorRef)) {
					addError("'errorRef' attribute is mandatory on error end event", errorEventDefinition);
				} else {
					Error error = errors.get(errorRef);
					activity.setProperty("type", "errorEndEvent");
					activity.setActivityBehavior(new ErrorEndEventActivityBehavior(error != null ? error.getErrorCode() : errorRef));
				}
			} else if (cancelEventDefinition != null) {
				if (scope.getProperty("type") == null || !scope.getProperty("type").equals("transaction")) {
					addError("end event with cancelEventDefinition only supported inside transaction subprocess", cancelEventDefinition);
				} else {
					activity.setProperty("type", "cancelEndEvent");
					activity.setActivityBehavior(new CancelEndEventActivityBehavior());
				}
			} else if (compensateEventDefinition != null) {
			    String activityRef= compensateEventDefinition.attribute("activityRef");
				if (activityRef==null||"".equals(activityRef)) {
					addError("'activityReference' attribute is mandatory on compensate end event", compensateEventDefinition);
				} else {
					activity.setProperty("type", "compensateEndEvent");
					activity.setActivityBehavior(new CompensateEndEventActivityBehavior(activityRef));
				}
			}else if (signalEventDefinition != null) {
					    String signalRef= signalEventDefinition.attribute("signalRef");
					    //signalRef 可以为 null
					    //if (signalRef==null||"".equals(signalRef)) {
						//	addError("'signalRef' attribute is mandatory signal end event", signalEventDefinition);
						//} else {
							activity.setProperty("type", "signalEndEvent");
							activity.setActivityBehavior(new SignalEndEventActivityBehavior(signalRef));
						//}
			}else { // default: none end event
				activity.setActivityBehavior(new NoneEndEventActivityBehavior());
			}
			for (BpmnParseListener parseListener : parseListeners) {
				parseListener.parseEndEvent(endEventElement, scope, activity);
			}
		}
	}
	/**
	 * Parses the boundary events of a certain 'level' (process, subprocess or
	 * other scope).
	 * 
	 * Note that the boundary events are not parsed during the parsing of the
	 * bpmn activities, since the semantics are different (boundaryEvent needs
	 * to be added as nested activity to the reference activity on PVM level).
	 * 
	 * @param parentElement
	 *            The 'parent' element that contains the activities (process,
	 *            subprocess).
	 * @param scopeElement
	 *            The {@link ScopeImpl} to which the activities must be added.
	 */
	public void parseBoundaryEvents(Element parentElement, IScope scopeElement) {
		for (Element boundaryEventElement : parentElement.elements("boundaryEvent")) {
			// The boundary event is attached to an activity, reference by the
			// 'attachedToRef' attribute
			String attachedToRef = boundaryEventElement.attribute("attachedToRef");
			if (attachedToRef == null || attachedToRef.equals("")) {
				addError("AttachedToRef is required when using a boundaryEventDefinition", boundaryEventElement);
			}
			// Representation structure-wise is a nested activity in the
			// activity to
			// which its attached
			String id = boundaryEventElement.attribute("id");
			if (LOGGER.isLoggable(Level.FINE)) {
				LOGGER.fine("Parsing boundary event " + id);
			}
			IActivity parentActivity = scopeElement.findActivity(attachedToRef);
			if (parentActivity == null) {
				addError("Invalid reference in boundary event. Make sure that the referenced activity is " + "defined in the same scope as the boundary event", boundaryEventElement);
			}
			IActivity nestedActivity = createActivityOnScope(boundaryEventElement, parentActivity);
			String cancelActivity = boundaryEventElement.attribute("cancelActivity", "true");
			boolean interrupting = cancelActivity.equals("true") ? true : false;
			// Catch event behavior is the same for most types
			ActivityBehavior behavior = new BoundaryEventActivityBehavior(interrupting);
			// Depending on the sub-element definition, the correct
			// activityBehavior
			// parsing is selected
			Element timerEventDefinition = boundaryEventElement.element("timerEventDefinition");
			Element errorEventDefinition = boundaryEventElement.element("errorEventDefinition");
			Element signalEventDefinition = boundaryEventElement.element("signalEventDefinition");
			Element cancelEventDefinition = boundaryEventElement.element("cancelEventDefinition");
			Element compensateEventDefinition = boundaryEventElement.element("compensateEventDefinition");
			if (timerEventDefinition != null) {
				parseBoundaryTimerEventDefinition(timerEventDefinition, interrupting, nestedActivity);
			} else if (errorEventDefinition != null) {
				interrupting = true; // non-interrupting not yet supported
				((BoundaryEventActivityBehavior)behavior).setInterrupting(true);
				parseBoundaryErrorEventDefinition(errorEventDefinition, interrupting, parentActivity, nestedActivity);
			} else if (signalEventDefinition != null) {
				parseBoundarySignalEventDefinition(signalEventDefinition, interrupting, nestedActivity);
			} else if (cancelEventDefinition != null) {
				// always interrupting
				behavior = parseBoundaryCancelEventDefinition(cancelEventDefinition, nestedActivity);
			} else if (compensateEventDefinition != null) {
				
				parseCatchCompensateEventDefinition(compensateEventDefinition, nestedActivity);
			} else {
				addError("Unsupported boundary event type", boundaryEventElement);
			}
			nestedActivity.setActivityBehavior(behavior);
		}
	}
	/**
	 * Parses a boundary timer event. The end-result will be that the given
	 * nested activity will get the appropriate {@link ActivityBehavior}.
	 * 
	 * @param timerEventDefinition
	 *            The XML element corresponding with the timer event details
	 * @param interrupting
	 *            Indicates whether this timer is interrupting.
	 * @param timerActivity
	 *            The activity which maps to the structure of the timer event on
	 *            the boundary of another activity. Note that this is NOT the
	 *            activity onto which the boundary event is attached, but a
	 *            nested activity inside this activity, specifically created for
	 *            this event.
	 */
	public void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, IActivity timerActivity) {
		timerActivity.setProperty("type", "boundaryTimer");
		TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, TimerExecuteNestedActivityJobHandler.TYPE);
		addTimerDeclaration((ScopeImpl) timerActivity.getParent(), timerDeclaration);
		if (timerActivity.getParent() instanceof ActivityImpl) {
			((ActivityImpl) timerActivity.getParent()).setScope(true);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseBoundaryTimerEventDefinition(timerEventDefinition, interrupting, timerActivity);
		}
	}
	public void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, IActivity signalActivity) {
		signalActivity.setProperty("type", "boundarySignal");
		SignalEventDefinition signalDefinition = parseSignalEventDefinition(signalEventDefinition);
		if (signalActivity.getId() == null) {
			addError("boundary event has no id", signalEventDefinition);
		}
		signalDefinition.setActivityId(signalActivity.getId());
		addSignalDefinition((ScopeImpl) signalActivity.getParent(), signalDefinition);
		if (signalActivity.getParent() instanceof ActivityImpl) {
			((ActivityImpl) signalActivity.getParent()).setScope(true);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseBoundarySignalEventDefinition(signalEventDefinition, interrupting, signalActivity);
		}
	}
	@SuppressWarnings("unchecked")
	private void parseTimerStartEventDefinition(Element timerEventDefinition, IActivity timerActivity, IProcessDefinition processDefinition) {
		timerActivity.setProperty("type", "startTimerEvent");
		TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, TimerStartEventJobHandler.TYPE);
		timerDeclaration.setJobHandlerConfiguration(processDefinition.getProDefPk());
		List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>) processDefinition.getProperty(PROPERTYNAME_START_TIMER);
		if (timerDeclarations == null) {
			timerDeclarations = new ArrayList<TimerDeclarationImpl>();
			processDefinition.setProperty(PROPERTYNAME_START_TIMER, timerDeclarations);
		}
		timerDeclarations.add(timerDeclaration);
	}
	protected void parseIntemediateSignalEventDefinition(Element signalEventDefinition, IActivity signalActivity, boolean isAfterEventBasedGateway) {
		signalActivity.setProperty("type", "intermediateSignalCatch");
		SignalEventDefinition signalDefinition = parseSignalEventDefinition(signalEventDefinition);
		signalDefinition.setActivityId(signalActivity.getId());
		if (isAfterEventBasedGateway) {
			addSignalDefinition((ScopeImpl) signalActivity.getParent(), signalDefinition);
		} else {
			addSignalDefinition(signalActivity, signalDefinition);
			signalActivity.setScope(true);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseIntermediateSignalCatchEventDefinition(signalEventDefinition, signalActivity);
		}
	}
	@SuppressWarnings("unchecked")
	protected void addSignalDefinition(IScope scopeImpl, SignalEventDefinition signalDefinition) {
		List<SignalEventDefinition> signalDefinitions = (List<SignalEventDefinition>) scopeImpl.getProperty(PROPERTYNAME_SIGNAL_DEFINITION_NAME);
		if (signalDefinitions == null) {
			signalDefinitions = new ArrayList<SignalEventDefinition>();
			scopeImpl.setProperty(PROPERTYNAME_SIGNAL_DEFINITION_NAME, signalDefinitions);
		}
		signalDefinitions.add(signalDefinition);
	}
	protected SignalEventDefinition parseSignalEventDefinition(Element signalEventDefinitionElement) {
		String signalRef = signalEventDefinitionElement.attribute("signalRef");
		if (signalRef == null) {
			addError("signalEventDefinition does not have required property 'signalRef'", signalEventDefinitionElement);
			return null;
		} else {
			SignalDefinition signalDefinition= new SignalDefinition();
			signalDefinition.setName(signalRef);
			signals.put(signalRef, signalDefinition);
		    signalDefinition = signals.get(signalRef);
			if (signalDefinition == null) {
				if(elementIds.contains(signalRef));
				addError("Could not find signal with id '" + signalRef + "'", signalEventDefinitionElement);
			}
			SignalEventDefinition signalEventDefinition = new SignalEventDefinition(signalDefinition);
			boolean asynch = "true".equals(signalEventDefinitionElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "async", "false"));
			signalEventDefinition.setAsync(asynch);
			return signalEventDefinition;
		}
	}
	private void parseIntemediateTimerEventDefinition(Element timerEventDefinition, IActivity timerActivity, boolean isAfterEventBasedGateway) {
		timerActivity.setProperty("type", "intermediateTimer");
		TimerDeclarationImpl timerDeclaration = parseTimer(timerEventDefinition, timerActivity, TimerCatchIntermediateEventJobHandler.TYPE);
		if (isAfterEventBasedGateway) {
			addTimerDeclaration((ScopeImpl) timerActivity.getParent(), timerDeclaration);
		} else {
			addTimerDeclaration(timerActivity, timerDeclaration);
			timerActivity.setScope(true);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseIntermediateTimerEventDefinition(timerEventDefinition, timerActivity);
		}
	}
	private TimerDeclarationImpl parseTimer(Element timerEventDefinition, IScope timerActivity, String jobHandlerType) {
		// TimeDate
		TimerDeclarationType type = TimerDeclarationType.DATE;
		Expression expression = parseExpression(timerEventDefinition, "timeDate");
		// TimeCycle
		if (expression == null) {
			type = TimerDeclarationType.CYCLE;
			expression = parseExpression(timerEventDefinition, "timeCycle");
		}
		// TimeDuration
		if (expression == null) {
			type = TimerDeclarationType.DURATION;
			expression = parseExpression(timerEventDefinition, "timeDuration");
		}
		// neither date, cycle or duration configured!
		if (expression == null) {
			addError("Timer needs configuration (either timeDate, timeCycle or timeDuration is needed).", timerEventDefinition);
		}
		// Parse the timer declaration
		// TODO move the timer declaration into the bpmn activity or next to the
		// TimerSession
		TimerDeclarationImpl timerDeclaration = new TimerDeclarationImpl(expression, type, jobHandlerType);
		timerDeclaration.setJobHandlerConfiguration(timerActivity.getId());
		timerDeclaration.setExclusive("true".equals(timerEventDefinition.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "exclusive", String.valueOf(JobEntity.DEFAULT_EXCLUSIVE))));
		return timerDeclaration;
	}
	private Expression parseExpression(Element parent, String name) {
		Element value = parent.element(name);
		if (value != null) {
			String expressionText = value.getText().trim();
			if(expressionText == null || expressionText =="")
				return null;
			return expressionManager.createExpression(expressionText);
		}
		return null;
	}
	public void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, IActivity activity, IActivity nestedErrorEventActivity) {
		nestedErrorEventActivity.setProperty("type", "boundaryError");
		ScopeImpl catchingScope = (ScopeImpl) nestedErrorEventActivity.getParent();
		((ActivityImpl) catchingScope).setScope(true);
		String errorRef = errorEventDefinition.attribute("errorRef");
		Error error = null;
		ErrorEventDefinition definition = new ErrorEventDefinition(nestedErrorEventActivity.getId());
		if (errorRef != null) {
			error = errors.get(errorRef);
			definition.setErrorCode(error == null ? errorRef : error.getErrorCode());
		}
		addErrorEventDefinition(definition, catchingScope);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseBoundaryErrorEventDefinition(errorEventDefinition, interrupting, activity, nestedErrorEventActivity);
		}
	}
	@SuppressWarnings("unchecked")
	protected void addErrorEventDefinition(ErrorEventDefinition errorEventDefinition, ScopeImpl catchingScope) {
		List<ErrorEventDefinition> errorEventDefinitions = (List<ErrorEventDefinition>) catchingScope.getProperty(PROPERTYNAME_ERROR_EVENT_DEFINITIONS);
		if (errorEventDefinitions == null) {
			errorEventDefinitions = new ArrayList<ErrorEventDefinition>();
			catchingScope.setProperty(PROPERTYNAME_ERROR_EVENT_DEFINITIONS, errorEventDefinitions);
		}
		errorEventDefinitions.add(errorEventDefinition);
		Collections.sort(errorEventDefinitions, ErrorEventDefinition.comparator);
	}
	protected List<IActivity> getAllChildActivitiesOfType(String type, IScope scope) {
		List<IActivity> children = new ArrayList<IActivity>();
		for (IActivity childActivity : scope.getActivities()) {
			if (type.equals(childActivity.getProperty("type"))) {
				children.add(childActivity);
			}
			children.addAll(getAllChildActivitiesOfType(type, childActivity));
		}
		return children;
	}
	/**
	 * Checks if the given activity is a child activity of the
	 * possibleParentActivity.
	 */
	protected boolean isChildActivity(IActivity activityToCheck, IActivity possibleParentActivity) {
		for (IActivity child : possibleParentActivity.getActivities()) {
			if (child.getId().equals(activityToCheck.getId()) || isChildActivity(activityToCheck, child)) {
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("unchecked")
	protected void addTimerDeclaration(IScope scope, TimerDeclarationImpl timerDeclaration) {
		List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>) scope.getProperty(PROPERTYNAME_TIMER_DECLARATION);
		if (timerDeclarations == null) {
			timerDeclarations = new ArrayList<TimerDeclarationImpl>();
			scope.setProperty(PROPERTYNAME_TIMER_DECLARATION, timerDeclarations);
		}
		timerDeclarations.add(timerDeclaration);
	}
	@SuppressWarnings("unchecked")
	protected void addVariableDeclaration(IScope scope, VariableDeclaration variableDeclaration) {
		List<VariableDeclaration> variableDeclarations = (List<VariableDeclaration>) scope.getProperty(PROPERTYNAME_VARIABLE_DECLARATIONS);
		if (variableDeclarations == null) {
			variableDeclarations = new ArrayList<VariableDeclaration>();
			scope.setProperty(PROPERTYNAME_VARIABLE_DECLARATIONS, variableDeclarations);
		}
		variableDeclarations.add(variableDeclaration);
	}
	/**
	 * Parses a subprocess (formally known as an embedded subprocess): a
	 * subprocess defined within another process definition.
	 * 
	 * @param subProcessElement
	 *            The XML element corresponding with the subprocess definition
	 * @param scope
	 *            The current scope on which the subprocess is defined.
	 */
	public IActivity parseSubProcess(Element subProcessElement, IScope scope) {
		IActivity activity = createActivityOnScope(subProcessElement, scope);
		activity.setAsync(isAsync(subProcessElement));
		activity.setExclusive(isExclusive(subProcessElement));
		Boolean isTriggeredByEvent = parseBooleanAttribute(subProcessElement.attribute("triggeredByEvent"), false);
		activity.setProperty("triggeredByEvent", isTriggeredByEvent);
		// event subprocesses are not scopes
		activity.setScope(!isTriggeredByEvent);
		activity.setActivityBehavior(new SubProcessActivityBehavior());
		parseScope(subProcessElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseSubProcess(subProcessElement, scope, activity);
		}
		return activity;
	}
	private IActivity parseTransaction(Element transactionElement, IScope scope) {
		IActivity activity = createActivityOnScope(transactionElement, scope);
		activity.setAsync(isAsync(transactionElement));
		activity.setExclusive(isExclusive(transactionElement));
		activity.setScope(true);
		// activity.setActivityBehavior(new TransactionActivityBehavior());
		parseScope(transactionElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseTransaction(transactionElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses a call activity (currently only supporting calling subprocesses).
	 * 
	 * @param callActivityElement
	 *            The XML element defining the call activity
	 * @param scope
	 *            The current scope on which the call activity is defined.
	 */
	public IActivity parseCallActivity(Element callActivityElement, IScope scope) {
		IActivity activity = createActivityOnScope(callActivityElement, scope);
		activity.setAsync(isAsync(callActivityElement));
		activity.setExclusive(isExclusive(callActivityElement));
		String calledElement = callActivityElement.attribute("calledElement");
		if (calledElement == null) {
			addError("Missing attribute 'calledElement'", callActivityElement);
		}
		CallActivityBehavior callActivityBehaviour = null;
		String expressionRegex = "\\$+\\{+.+\\}";
		if (calledElement.matches(expressionRegex)) {
			callActivityBehaviour = new CallActivityBehavior(expressionManager.createExpression(calledElement));
		} else {
			callActivityBehaviour = new CallActivityBehavior(calledElement);
		}
		Element extentionsElement = callActivityElement.element("extensionElements");
		if (extentionsElement != null) {
			// input data elements
			for (Element listenerElement : extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "in")) {
				String sourceExpression = listenerElement.attribute("sourceExpression");
				String target = listenerElement.attribute("target");
				if (sourceExpression != null) {
					Expression expression = expressionManager.createExpression("${"+sourceExpression.trim()+"}");
					callActivityBehaviour.addDataInputAssociation(new SimpleDataInputAssociation(expression, target));
				} else {
					String source = listenerElement.attribute("source");
					callActivityBehaviour.addDataInputAssociation(new SimpleDataInputAssociation(source, target));
				}
			}
			// output data elements
			for (Element listenerElement : extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "out")) {
				String sourceExpression = listenerElement.attribute("sourceExpression");
				String target = listenerElement.attribute("target");
				if (sourceExpression != null) {
					Expression expression = expressionManager.createExpression("${"+sourceExpression.trim()+"}");
					callActivityBehaviour.addDataOutputAssociation(new MessageImplicitDataOutputAssociation(target, expression));
				} else {
					String source = listenerElement.attribute("source");
					callActivityBehaviour.addDataOutputAssociation(new MessageImplicitDataOutputAssociation(target, source));
				}
			}
		}
		// // parse data input and output
		// for (Element dataAssociationElement :
		// callActivityElement.elements("dataInputAssociation")) {
		// AbstractDataAssociation dataAssociation =
		// this.parseDataInputAssociation(dataAssociationElement);
		// callActivityBehaviour.addDataInputAssociation(dataAssociation);
		// }
		//
		// for (Element dataAssociationElement :
		// callActivityElement.elements("dataOutputAssociation")) {
		// AbstractDataAssociation dataAssociation =
		// this.parseDataOutputAssociation(dataAssociationElement);
		// callActivityBehaviour.addDataOutputAssociation(dataAssociation);
		// }
		activity.setScope(true);
		activity.setActivityBehavior(callActivityBehaviour);
		parseExecutionListenersOnScope(callActivityElement, activity);
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseCallActivity(callActivityElement, scope, activity);
		}
		return activity;
	}
	/**
	 * Parses the properties of an element (if any) that can contain properties
	 * (processes, activities, etc.)
	 * 
	 * Returns true if property subelemens are found.
	 * 
	 * @param element
	 *            The element that can contain properties.
	 * @param activity
	 *            The activity where the property declaration is done.
	 */
	public void parseProperties(Element element, IActivity activity) {
		List<Element> propertyElements = element.elements("property");
		for (Element propertyElement : propertyElements) {
			parseProperty(propertyElement, activity);
		}
	}
	/**
	 * Parses one property definition.
	 * 
	 * @param propertyElement
	 *            The 'property' element that defines how a property looks like
	 *            and is handled.
	 */
	public void parseProperty(Element propertyElement, IActivity activity) {
		String id = propertyElement.attribute("id");
		String name = propertyElement.attribute("name");
		// If name isn't given, use the id as name
		if (name == null) {
			if (id == null) {
				addError("Invalid property usage on line " + propertyElement.getLine() + ": no id or name specified.", propertyElement);
			} else {
				name = id;
			}
		}
		String itemSubjectRef = propertyElement.attribute("itemSubjectRef");
		String type = null;
		if (itemSubjectRef != null) {
			ItemDefinition itemDefinition = itemDefinitions.get(itemSubjectRef);
			if (itemDefinition != null) {
				StructureDefinition structure = itemDefinition.getStructureDefinition();
				type = structure.getId();
			} else {
				addError("Invalid itemDefinition reference: " + itemSubjectRef + " not found", propertyElement);
			}
		}
		parsePropertyCustomExtensions(activity, propertyElement, name, type);
	}
	/**
	 * Parses the custom extensions for properties.
	 * 
	 * @param activity
	 *            The activity where the property declaration is done.
	 * @param propertyElement
	 *            The 'property' element defining the property.
	 * @param propertyName
	 *            The name of the property.
	 * @param propertyType
	 *            The type of the property.
	 */
	public void parsePropertyCustomExtensions(IActivity activity, Element propertyElement, String propertyName, String propertyType) {
		if (propertyType == null) {
			String type = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "type");
			propertyType = type != null ? type : "string"; // default is string
		}
		VariableDeclaration variableDeclaration = new VariableDeclaration(propertyName, propertyType);
		addVariableDeclaration(activity, variableDeclaration);
		activity.setScope(true);
		String src = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "src");
		if (src != null) {
			variableDeclaration.setSourceVariableName(src);
		}
		String srcExpr = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "srcExpr");
		if (srcExpr != null) {
			Expression sourceExpression = expressionManager.createExpression(srcExpr);
			variableDeclaration.setSourceExpression(sourceExpression);
		}
		String dst = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "dst");
		if (dst != null) {
			variableDeclaration.setDestinationVariableName(dst);
		}
		String destExpr = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "dstExpr");
		if (destExpr != null) {
			Expression destinationExpression = expressionManager.createExpression(destExpr);
			variableDeclaration.setDestinationExpression(destinationExpression);
		}
		String link = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "link");
		if (link != null) {
			variableDeclaration.setLink(link);
		}
		String linkExpr = propertyElement.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "linkExpr");
		if (linkExpr != null) {
			Expression linkExpression = expressionManager.createExpression(linkExpr);
			variableDeclaration.setLinkExpression(linkExpression);
		}
		for (BpmnParseListener parseListener : parseListeners) {
			parseListener.parseProperty(propertyElement, variableDeclaration, activity);
		}
	}
	/**
	 * Parses all sequence flow of a scope.
	 * 
	 * @param processElement
	 *            The 'process' element wherein the sequence flow are defined.
	 * @param scope
	 *            The scope to which the sequence flow must be added.
	 */
	public void parseSequenceFlow(Element processElement, IScope scope) {
		for (Element sequenceFlowElement : processElement.elements("sequenceFlow")) {
			String id = sequenceFlowElement.attribute("id");
			String sourceRef = sequenceFlowElement.attribute("sourceRef");
			String destinationRef = sequenceFlowElement.attribute("targetRef");
			IActivity sourceActivity = scope.findActivity(sourceRef);
			IActivity destinationActivity = scope.findActivity(destinationRef);
			if (sourceActivity == null) {
				addError("Invalid source '" + sourceRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
			} else if (destinationActivity == null) {
				addError("Invalid destination '" + destinationRef + "' of sequence flow '" + id + "'", sequenceFlowElement);
			} else if (sourceActivity.getActivityBehavior() instanceof EventBasedGatewayActivityBehavior) {
				// ignore
			} else if (destinationActivity.getActivityBehavior() instanceof IntermediateCatchEventActivityBehaviour && destinationActivity.getParent() instanceof IActivity) {
				if (((IActivity) destinationActivity.getParent()).getActivityBehavior() instanceof EventBasedGatewayActivityBehavior) {
					addError("Invalid incoming sequenceflow for intermediateCatchEvent with id '" + destinationActivity.getId() + "' connected to an event-based gateway.", sequenceFlowElement);
				}
			} else {
				TransitionImpl transition = (TransitionImpl) sourceActivity.createOutgoingTransition(id);
				sequenceFlows.put(id, transition);
				transition.setProperty("name", sequenceFlowElement.attribute("name"));
				transition.setProperty("documentation", parseDocumentation(sequenceFlowElement));
				transition.setDestination(destinationActivity);
				parseSequenceFlowConditionExpression(sequenceFlowElement, transition);
				parseExecutionListenersOnTransition(sequenceFlowElement, transition);
				for (BpmnParseListener parseListener : parseListeners) {
					parseListener.parseSequenceFlow(sequenceFlowElement, scope, transition);
				}
			}
		}
	}
	/**
	 * Parses a condition expression on a sequence flow.
	 * 
	 * @param seqFlowElement
	 *            The 'sequenceFlow' element that can contain a condition.
	 * @param seqFlow
	 *            The sequenceFlow object representation to which the condition
	 *            must be added.
	 */
	public void parseSequenceFlowConditionExpression(Element seqFlowElement, TransitionImpl seqFlow) {
		Element conditionExprElement = seqFlowElement.element("conditionExpression");
		if (conditionExprElement != null) {
			String expression = conditionExprElement.getText().trim();
			String type = conditionExprElement.attributeNS(BpmnParser.XSI_NS, "type");
			if (type != null && !type.equals("tFormalExpression")) {
			}
			if(expression!=null&&!expression.isEmpty()){//过滤掉 expression = "" 这种情况  zhailzh 2013.1.31
				Condition expressionCondition = new UelExpressionCondition(expressionManager.createExpression("${"+expression+"}"));
				seqFlow.setProperty(PROPERTYNAME_CONDITION_TEXT, expression);
				seqFlow.setProperty(PROPERTYNAME_CONDITION, expressionCondition);
			}
		}
	}
	/**
	 * Parses all execution-listeners on a scope.
	 * 
	 * @param scopeElement
	 *            the XML element containing the scope definition.
	 * @param scope
	 *            the scope to add the executionListeners to.
	 * @param postProcessActivities
	 */
	public void parseExecutionListenersOnScope(Element scopeElement, IScope scope) {
		//处理内置的Listener
		
		List<Object> extensionListeners = parseCommonExtensionListener("execution");
		for(Object listener : extensionListeners ){
			if(listener instanceof IInstanceListener){
				scope.addExecutionListener("extensionListener", (IInstanceListener)listener);
			}
			else{
				WorkflowLogger.error("监听类"+listener.getClass()+"没有实现接口IInstanceListener");
				throw new WorkflowRuntimeException("监听类"+listener.getClass()+"没有实现接口IInstanceListener");
			}
		}
		
		//解析
		Element extentionsElement = scopeElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> listenerElements = extentionsElement.elementsNS(NameSpaceConst.BIZEX_URL, "executionListener");
			for (Element listenerElement : listenerElements) {
				String eventName = listenerElement.attribute("event");
				if (isValidEventNameForScope(eventName, listenerElement)) {
					IInstanceListener listener = parseExecutionListener(listenerElement);
					if (listener != null) {
						scope.addExecutionListener(eventName, listener);
					}
				}
			}
		}
/*		if (scope instanceof IActivity) {
			IActivity activity = (IActivity) scope;
			ListenerBizImplExtend listenerBizImplExtend = new ListenerBizImplExtend();
			listenerBizImplExtend.parseListener(extentionsElement);
			activity.setListenerBizImplExtend(listenerBizImplExtend);
		}
*/	}
	/**
	 * Check if the given event name is valid. If not, an appropriate error is
	 * added.
	 */
	protected boolean isValidEventNameForScope(String eventName, Element listenerElement) {
		if (eventName != null && eventName.trim().length() > 0) {
			if ("Start".equalsIgnoreCase(eventName) || "End".equalsIgnoreCase(eventName)) {
				return true;
			} else {
				addError("Attribute 'eventName' must be one of {start|end}", listenerElement);
			}
		} else {
			addError("Attribute 'eventName' is mandatory on listener", listenerElement);
		}
		return false;
	}
	public void parseExecutionListenersOnTransition(Element activitiElement, TransitionImpl activity) {
		//处理内置的Listener
		List<Object> listenres = parseCommonExtensionListener("execution");
		for(Object listener : listenres )
			if(listener instanceof IInstanceListener){
				activity.addExecutionListener((IInstanceListener)listener);
			}
			else{
				WorkflowLogger.error("监听类"+listener.getClass()+"没有实现接口IInstanceListener");
				throw new WorkflowRuntimeException("监听类"+listener.getClass()+"没有实现接口IInstanceListener");
			}
		//解析
		Element extentionsElement = activitiElement.element("extensionElements");
		if (extentionsElement != null) {
			List<Element> listenerElements = extentionsElement.elementsNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "executionListener");
			for (Element listenerElement : listenerElements) {
				IInstanceListener listener = parseExecutionListener(listenerElement);
				if (listener != null) {
					// Since a transition only fires event 'take', we don't
					// parse the
					// eventName, it is ignored
					activity.addExecutionListener(listener);
				}
			}
		}
	}
	/**
	 * Parses an {@link IInstanceListener} implementation for the given
	 * executionListener element.
	 * 
	 * @param executionListenerElement
	 *            the XML element containing the executionListener definition.
	 */
	public IInstanceListener parseExecutionListener(Element executionListenerElement) {
		IInstanceListener executionListener = null;
		String implType = executionListenerElement.attribute("implementationType");
		if (implType != null) {
			String implementation = executionListenerElement.attribute("implementation");
			//String expression = executionListenerElement.attribute("expression");
			//String delegateExpression = executionListenerElement.attribute("delegateExpression");
			if (implType.equalsIgnoreCase("Standard")) {
				executionListener = new ClassDelegate(implementation, parseFieldDeclarations(executionListenerElement));
			} else if (implType.equalsIgnoreCase("Expression")) {
				executionListener = new ExpressionExecutionListener(expressionManager.createExpression(implementation));
			} else if (implType.equalsIgnoreCase("DelegateExpression")) {
				executionListener = 
					new DelegateExpressionExecutionListener(expressionManager.createExpression(implementation));
			}else if (implType.equalsIgnoreCase("CallMethod")){
				ListenerDefinition definition = parseListenerDefinition("executionListener", executionListenerElement);
				executionListener = new CallMethodExecutionListener();
				((ExtExecutionListener)executionListener).setListenerDefinition(definition);
			} else {
				ListenerDefinition definition = parseListenerDefinition("executionListener", executionListenerElement);
				executionListener = (IInstanceListener)parseExtListener(definition, executionListenerElement);
				if (executionListener == null)
					addError("Element 'class' or 'expression' is mandatory on executionListener", executionListenerElement);
			}
		}
		return executionListener;
	}
	private ListenerDefinition parseListenerDefinition(String listenerType, Element listenerElement) {
		ListenerDefinition definition = null;
		definition = new ListenerDefinition();
		definition.setListenerType(listenerType);
		definition.setEvent(listenerElement.attribute("event"));
		definition.setImplementationType(listenerElement.attribute("implementationType"));
		definition.setImplementation(listenerElement.attribute("implementation"));
		definition.setMethod(listenerElement.attribute("method"));
		definition.setFieldExtensions(new ArrayList<FieldDeclaration>());
		FieldDeclaration field = null;
		for (Element fieldElement : listenerElement.elements("field")) {
			field = new FieldDeclaration();
			field.setValue(fieldElement.attribute("expression"));
			field.setName(fieldElement.attribute("name"));
			field.setType(fieldElement.attribute("type"));
			definition.getFieldExtensions().add(field);
		}
		return definition;
	}
	private Object parseExtListener(ListenerDefinition definition, Element listenerElement) {
		Object extListener = null;
		List<ExtensionListenerConfig> listenerConfigs = 
			BizProcessServer.getProcessEngineConfig().getExtensionconfig().getListenerExtension();
		for(ExtensionListenerConfig listener : listenerConfigs){
			if (definition.getImplementationType().equalsIgnoreCase(listener.getName())){
				extListener = ClassUtil.loadClass(listener.getImplementation());
				break;
			}
		}
		return extListener;
	}
	/**
	 * Retrieves the {@link Operation} corresponding with the given operation
	 * identifier.
	 */
	public Operation getOperation(String operationId) {
		return operations.get(operationId);
	}
	// Diagram interchange
	// /////////////////////////////////////////////////////////////////
	public void parseDiagramInterchangeElements() {
		// Multiple BPMNDiagram possible
		List<Element> diagrams = rootElement.elementsNS(BpmnParser.BPMN_DI_NS, "BPMNDiagram");
		if (!diagrams.isEmpty()) {
			for (Element diagramElement : diagrams) {
				parseBPMNDiagram(diagramElement);
			}
		}
	}
	public void parseBPMNDiagram(Element bpmndiagramElement) {
		// Each BPMNdiagram needs to have exactly one BPMNPlane
		Element bpmnPlane = bpmndiagramElement.elementNS(BpmnParser.BPMN_DI_NS, "BPMNPlane");
		if (bpmnPlane != null) {
			parseBPMNPlane(bpmnPlane);
		}
	}
	public void parseBPMNPlane(Element bpmnPlaneElement) {
		// bpmnPlaneElement.attributeNS(NameSpaceConst.BPMNDI_PREFIX,
		// "bpmnElement");
		String processId = bpmnPlaneElement.attribute("bpmnElement");
		if (processId != null && !"".equals(processId)) {
			IProcessDefinition processDefinition = this.getProcessDefinitionByProDefId(processId);
			if (processDefinition != null) {
				List<Element> shapes = bpmnPlaneElement.elementsNS(BpmnParser.BPMN_DI_NS, "BPMNShape");
				for (Element shape : shapes) {
					parseBPMNShape(shape, processDefinition);
				}
				List<Element> edges = bpmnPlaneElement.elementsNS(BpmnParser.BPMN_DI_NS, "BPMNEdge");
				for (Element edge : edges) {
					parseBPMNEdge(edge, processDefinition);
				}
			} else {
				addError("Invalid reference in 'bpmnElement' attribute, process " + processId + " not found", bpmnPlaneElement);
			}
		} else {
			addError("'bpmnElement' attribute is required on BPMNPlane ", bpmnPlaneElement);
		}
	}
	public void parseBPMNShape(Element bpmnShapeElement, IProcessDefinition processDefinition) {
		String activityId = bpmnShapeElement.attribute("bpmnElement");
		if (activityId != null && !"".equals(activityId)) {
			IActivity activity = processDefinition.findActivity(activityId);
			// bounds
			if (activity != null) {
				Element bounds = bpmnShapeElement.elementNS(BpmnParser.BPMN_DC_NS, "Bounds");
				if (bounds != null) {
					activity.setX(parseDoubleAttribute(bpmnShapeElement, "x", bounds.attribute("x"), true).intValue());
					activity.setY(parseDoubleAttribute(bpmnShapeElement, "y", bounds.attribute("y"), true).intValue());
					activity.setWidth(parseDoubleAttribute(bpmnShapeElement, "width", bounds.attribute("width"), true).intValue());
					activity.setHeight(parseDoubleAttribute(bpmnShapeElement, "height", bounds.attribute("height"), true).intValue());
				} else {
					addError("'Bounds' element is required", bpmnShapeElement);
				}
				// collapsed or expanded
				String isExpanded = bpmnShapeElement.attribute("isExpanded");
				if (isExpanded != null) {
					activity.setProperty(PROPERTYNAME_ISEXPANDED, parseBooleanAttribute(isExpanded));
				}
			} else if (!elementIds.contains(activityId)) {
				addError("Invalid reference in 'bpmnElement' attribute, activity " + activityId + "not found", bpmnShapeElement);
			}
		} else {
			addError("'bpmnElement' attribute is required on BPMNShape", bpmnShapeElement);
		}
	}
	public void parseBPMNEdge(Element bpmnEdgeElement, IProcessDefinition processDefinition) {
		String sequenceFlowId = bpmnEdgeElement.attribute("bpmnElement");
		if (sequenceFlowId != null && !"".equals(sequenceFlowId)) {
			ITransition sequenceFlow = sequenceFlows.get(sequenceFlowId);
			if (sequenceFlow != null) {
				List<Element> waypointElements = bpmnEdgeElement.elementsNS(BpmnParser.OMG_DI_NS, "waypoint");
				if (waypointElements.size() >= 2) {
					List<Integer> waypoints = new ArrayList<Integer>();
					for (Element waypointElement : waypointElements) {
						waypoints.add(parseDoubleAttribute(waypointElement, "x", waypointElement.attribute("x"), true).intValue());
						waypoints.add(parseDoubleAttribute(waypointElement, "y", waypointElement.attribute("y"), true).intValue());
					}
					sequenceFlow.setWaypoints(waypoints);
				} else {
					addError("Minimum 2 waypoint elements must be definted for a 'BPMNEdge'", bpmnEdgeElement);
				}
			} else if (!elementIds.contains(sequenceFlowId)) { // it might not
				addError("Invalid reference in 'bpmnElement' attribute, sequenceFlow " + sequenceFlowId + "not found", bpmnEdgeElement);
			}
		} else {
			addError("'bpmnElement' attribute is required on BPMNEdge", bpmnEdgeElement);
		}
	}
	// Getters, setters and Parser overriden operations
	// ////////////////////////////////////////
	public List<IProcessDefinition> getProcessDefinitions() {
		return processDefinitions;
	}
	public IProcessDefinition getProcessDefinitionByProDefId(String proDefId) {
		for (IProcessDefinition processDefinition : processDefinitions) {
			if (proDefId.equalsIgnoreCase(processDefinition.getProDefId())) {
				return processDefinition;
			}
		}
		return null;
	}
	public IProcessDefinition getProcessDefinitionByProDefPk(String proDefPk) {
		for (IProcessDefinition processDefinition : processDefinitions) {
			if (proDefPk.equalsIgnoreCase(processDefinition.getProDefPk())) {
				return processDefinition;
			}
		}
		return null;
	}
	@Override
	public BpmnParse name(String name) {
		super.name(name);
		return this;
	}
	@Override
	public BpmnParse sourceInputStream(InputStream inputStream) {
		super.sourceInputStream(inputStream);
		return this;
	}
	@Override
	public BpmnParse sourceResource(String resource, ClassLoader classLoader) {
		super.sourceResource(resource, classLoader);
		return this;
	}
	@Override
	public BpmnParse sourceResource(String resource) {
		super.sourceResource(resource);
		return this;
	}
	@Override
	public BpmnParse sourceString(String string) {
		super.sourceString(string);
		return this;
	}
	@Override
	public BpmnParse sourceUrl(String url) {
		super.sourceUrl(url);
		return this;
	}
	@Override
	public BpmnParse sourceUrl(URL url) {
		super.sourceUrl(url);
		return this;
	}
	public void addStructure(StructureDefinition structure) {
		this.structures.put(structure.getId(), structure);
	}
	public void addService(BpmnInterfaceImplementation bpmnInterfaceImplementation) {
		this.interfaceImplementations.put(bpmnInterfaceImplementation.getName(), bpmnInterfaceImplementation);
	}
	public void addOperation(OperationImplementation operationImplementation) {
		this.operationImplementations.put(operationImplementation.getId(), operationImplementation);
	}
	public Boolean parseBooleanAttribute(String booleanText, boolean defaultValue) {
		if (booleanText == null) {
			return defaultValue;
		} else {
			return parseBooleanAttribute(booleanText);
		}
	}
	public Boolean parseBooleanAttribute(String booleanText) {
		if ("true".equals(booleanText) || "enabled".equals(booleanText) || "on".equals(booleanText) || "active".equals(booleanText) || "yes".equals(booleanText)) {
			return Boolean.TRUE;
		}
		if ("false".equals(booleanText) || "disabled".equals(booleanText) || "off".equals(booleanText) || "inactive".equals(booleanText) || "no".equals(booleanText)) {
			return Boolean.FALSE;
		}
		return null;
	}
	public Double parseDoubleAttribute(Element element, String attributename, String doubleText, boolean required) {
		if (required && (doubleText == null || "".equals(doubleText))) {
			addError(attributename + " is required", element);
		} else {
			try {
				return Double.parseDouble(doubleText);
			} catch (NumberFormatException e) {
				addError("Cannot parse " + attributename + ": " + e.getMessage(), element);
			}
		}
		return -1.0;
	}
	protected boolean isExclusive(Element element) {
		return "true".equals(element.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "exclusive", String.valueOf(JobEntity.DEFAULT_EXCLUSIVE)));
	}
	protected boolean isAsync(Element element) {
		return "true".equals(element.attributeNS(BpmnParser.ACTIVITI_BPMN_EXTENSIONS_NS, "async"));
	}
}
