package uap.workflow.engine.command;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;

import nc.bs.framework.common.NCLocator;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.BpmnBounds;
import uap.workflow.bpmn2.model.BpmnDiagram;
import uap.workflow.bpmn2.model.BpmnEdge;
import uap.workflow.bpmn2.model.BpmnPlane;
import uap.workflow.bpmn2.model.BpmnShape;
import uap.workflow.bpmn2.model.BpmnWayPoint;
import uap.workflow.bpmn2.model.Connector;
import uap.workflow.bpmn2.model.DefaultParticipantDefinition;
import uap.workflow.bpmn2.model.DefaultParticipantType;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.IFlowElementsContainer;
import uap.workflow.bpmn2.model.SequenceFlow;
import uap.workflow.bpmn2.model.SubProcess;
import uap.workflow.bpmn2.model.UserTask;
import uap.workflow.bpmn2.model.event.EndEvent;
import uap.workflow.bpmn2.model.event.StartEvent;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.designer.exports.BPMN20ExportMarshaller;
import uap.workflow.engine.context.AddSignUserInfoCtx;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.Logic;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.itf.ITaskInstanceBill;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.itf.IWorkflowInstanceBill;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.TaskInstanceVO;
public class CreateAfterAddSignCmd implements ICommand<Void> {
	@Override
	public Void execute() {
		CreateAfterAddSignCtx flowInfoCtx = (CreateAfterAddSignCtx) WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
		IProcessDefinition processDefinition = this.builderProDef(flowInfoCtx);
		this.updateProInsInfo(processDefinition);
		return null;
	}
	private IProcessDefinition builderProDef(CreateAfterAddSignCtx flowInfoCtx) {
		String taskPk = flowInfoCtx.getTaskPk();
		ITask task = TaskUtil.getTaskByTaskPk(taskPk);
		// AddSignUserInfoCtx[] addSignUser = flowInfoCtx.getAddSingUsers();
		IProcessDefinition proDef = task.getProcessDefinition();
		Bpmn2MemoryModel model = genProDefModel(proDef);
		BaseElement baseElement = model.getElementMap().get(task.getExecution().getActivity().getId());
		List<Connector> outGoings = null;
		if (baseElement instanceof FlowNode) {
			FlowNode flowNode = (FlowNode) baseElement;
			outGoings = flowNode.getOutgoing();
		}
		Connector oldBsf = outGoings.get(0);
		SequenceFlow newBsf = new SequenceFlow();
		newBsf.setId(oldBsf.getId()+"1");
		FlowNode srcNode = oldBsf.getSource();// 切换源节点
		IFlowElementsContainer container = getParentElement(baseElement, model.getProcesses().get(0));
		boolean isAfterSign = false;
		if(baseElement instanceof UserTask)
		{
			isAfterSign = ((UserTask)baseElement).isAfterSign();
		}
		if (isAfterSign) {
			UserTask userTask = new UserTask();
			userTask.setId(srcNode.getId()+"1");
			if(flowInfoCtx.getLogic() == Logic.Sequence){
				userTask.setSequence(true);
			}
			userTask.setAfterSign(true);
			addParticipants(flowInfoCtx, userTask);
			this.addNewBpmnDiagram(model, userTask, baseElement, oldBsf, newBsf);
			oldBsf.setSource(userTask);
			newBsf.setSource(srcNode);
			newBsf.setTarget(userTask);
			container.getFlowElements().add(userTask);
		} else {
			SubProcess subProcess = builderSubProcess(model, baseElement, oldBsf, flowInfoCtx);// 构造子流程
			this.addNewBpmnDiagram(model, subProcess, baseElement, oldBsf, newBsf);
			oldBsf.setSource(subProcess);
			newBsf.setSource(srcNode);
			newBsf.setTarget(subProcess);
			List<Connector> inComing1 = new ArrayList<Connector>();
			inComing1.add(newBsf);
			List<Connector> outGoings1 = new ArrayList<Connector>();
			outGoings1.add(oldBsf);

			{// 设置从进入子流程的线
				subProcess.setIncoming(inComing1);
			}
			{// 设置从子流程出去的线
				subProcess.setOutgoing(outGoings1);
			}
			container.getFlowElements().add(subProcess);
			container.getFlowElements().add(newBsf);
		}
		String processId = changeProcessId(proDef, model);
		new BPMN20ExportMarshaller().marshallDiagram(model,  "", true, false);
		DeploymentCache deploymentCache = BizProcessServer.getProcessEngineConfig().getDeploymentCache();
		IProcessDefinition newProcessDefinition  = deploymentCache.getProDefByProDefId(processId);
		newProcessDefinition.setValidity(ProcessDefinitionStatusEnum.AfterSign.getIntValue());
		((uap.workflow.engine.entity.ProcessDefinitionEntity)newProcessDefinition).asyn();
		return newProcessDefinition;
	}
	
	private void addParticipants(CreateAfterAddSignCtx flowInfoCtx, UserTask userTask)
	{
		List<DefaultParticipantDefinition> participants = userTask.getParticipants();
		for(AddSignUserInfoCtx addSignUserInfoCtx : flowInfoCtx.getAddSingUsers())
		{
			DefaultParticipantDefinition participant = new DefaultParticipantDefinition();
			participant.setParticipantID(addSignUserInfoCtx.getUserPk());
			DefaultParticipantType participantType = new DefaultParticipantType();
			participantType.setCode("Operator");
			participant.setParticipantType(participantType);
			participants.add(participant);
		}
	}
	
	private String changeProcessId(IProcessDefinition proDef, Bpmn2MemoryModel model) {
		String oldProcessId = proDef.getId();
		String processId = java.util.UUID.randomUUID().toString();
		model.processes.get(0).setId(processId);
		model.processes.get(0).setProcessDefinitionPk(null);
		BpmnDiagram bpmnDiagram = model.getBpmnDiagram();
		bpmnDiagram.setId("BPMNDiagram_"+processId);
		BpmnPlane bpmnPlane = bpmnDiagram.getBpmnPlane();
		if(bpmnPlane.getBpmnElement().equals(oldProcessId))
		{
			bpmnPlane.setBpmnElement(processId);
		}
		for(BpmnShape bpmnShape: bpmnPlane.getBpmnShapes())
		{
			if(bpmnShape.getParentId().equals(oldProcessId))
			{
				bpmnShape.setParentId(processId);
			}
		}
		for(BpmnEdge bpmnEdge: bpmnPlane.getBpmnEdges())
		{
			if(bpmnEdge.getParentId().equals(oldProcessId))
			{
				bpmnEdge.setParentId(processId);
			}
		}
		return processId;
	}
	private Bpmn2MemoryModel genProDefModel(IProcessDefinition proDef) throws FactoryConfigurationError {
		try {
			IProcessDefinitionQry proDefQry = NCLocator.getInstance().lookup(IProcessDefinitionQry.class);
			ProcessDefinitionVO proDefVO = proDefQry.getProDefVoByPk(proDef.getProDefPk());
			Bpmn2MemoryModel model = ProcessDefinitionsManager.parser(new StringReader(proDefVO.getProcessStr()));
			return model;
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e);
		}
	}
	private IFlowElementsContainer getParentElement(BaseElement currentElement, IFlowElementsContainer container){
		List<FlowElement> elements = container.getFlowElements();
		for(FlowElement element : elements){
			if(element.getId() == currentElement.getId())
				return container;
			if (element instanceof IFlowElementsContainer){
				return getParentElement(currentElement, container);
			}
		}
		return null;
	}
	private SubProcess builderSubProcess(Bpmn2MemoryModel model, BaseElement oldBaseElement, 
			Connector oldSequenceFlow, CreateAfterAddSignCtx flowInfoCtx) {
		SubProcess subProcess = new SubProcess();
		subProcess.setId("signSubprocess");
		subProcess.setName("加签流程");
		List<FlowElement> flowElements = subProcess.getFlowElements();
		
		StartEvent startEvent = builderStartEvent();
		flowElements.add(startEvent);
		
		EndEvent endEvent = builderEndEvent();
		flowElements.add(endEvent);
		
		UserTask userTask1 = buidlerUserTask();
		flowElements.add(userTask1);
		addParticipants(flowInfoCtx, userTask1);
		if(flowInfoCtx.getLogic() == Logic.Sequence){
			userTask1.setSequence(true);
		}
		userTask1.setAfterSign(true);
	
		SequenceFlow sequenceflow1 = builderSequenceFlow("sf1");
		flowElements.add(sequenceflow1);
		
		SequenceFlow sequenceflow2 = builderSequenceFlow("sf2");
		flowElements.add(sequenceflow2);

		builderSf(startEvent, endEvent, userTask1, sequenceflow1, sequenceflow2);
		buidlerOutgoingAndInComing(startEvent, endEvent, userTask1, sequenceflow1, sequenceflow2);
		addNewSubprocessBpmnDiagram(model, startEvent, endEvent,userTask1, sequenceflow1, 
				sequenceflow2,oldBaseElement, oldSequenceFlow, subProcess);
		return subProcess;
	}
	private void buidlerOutgoingAndInComing(StartEvent startEvent, EndEvent endEvent, UserTask userTask1,
			Connector sf1, Connector sf2) {
		{
			List<Connector> outGoings1 = new ArrayList<Connector>();
			outGoings1.add(sf1);
			startEvent.setOutgoing(outGoings1);
		}
		{
			List<Connector> inComing1 = new ArrayList<Connector>();
			inComing1.add(sf1);
			List<Connector> outGoings2 = new ArrayList<Connector>();
			outGoings2.add(sf2);
			userTask1.setIncoming(inComing1);
			userTask1.setOutgoing(outGoings2);
		}
		{
			List<Connector> inComing2 = new ArrayList<Connector>();
			inComing2.add(sf2);
			endEvent.setIncoming(inComing2);
		}
	}
	private void builderSf(StartEvent startEvent, EndEvent endEvent, UserTask userTask1, SequenceFlow sf1, SequenceFlow sf2) {
		sf1.setSource(startEvent);
		sf1.setTarget(userTask1);
		sf2.setSource(userTask1);
		sf2.setTarget(endEvent);
	}
	private SequenceFlow builderSequenceFlow(String id) {
		SequenceFlow sf1 = new SequenceFlow();
		sf1.setId(id);
		return sf1;
	}
	private UserTask buidlerUserTask() {
		UserTask userTask1 = new UserTask();
		userTask1.setId("usertask1");
		userTask1.setName("加签任务1");
		userTask1.setAfterSign(true);
		return userTask1;
	}
	private EndEvent builderEndEvent() {
		EndEvent endEvent = new EndEvent();
		endEvent.setId("endEvent");
		endEvent.setName("结束");
		return endEvent;
	}
	private StartEvent builderStartEvent() {
		StartEvent startEvent = new StartEvent();
		startEvent.setId("startEvent");
		startEvent.setName("开始");
		return startEvent;
	}
	private void updateProInsInfo(IProcessDefinition processDefinition) {
		String proDefPk = processDefinition.getProDefPk();
		ITask task = WorkflowContext.getCurrentBpmnSession().getTask();
		IProcessInstance processInstance = task.getExecution().getProcessInstance();
		processInstance.setProcessDefinition(processDefinition);
		processInstance.asyn();
		
		ITaskInstanceQry taskInsQry = NCLocator.getInstance().lookup(ITaskInstanceQry.class);
		List<TaskInstanceVO> taskInsVos = taskInsQry.getTaskByProcessInstancePk(processInstance.getProInsPk());
		for (int i = 0; i < taskInsVos.size(); i++) {
			taskInsVos.get(i).setPk_process_def(proDefPk);
		}
		ITaskInstanceBill taskInsBill = NCLocator.getInstance().lookup(ITaskInstanceBill.class);
		taskInsBill.updateTaskVos(taskInsVos.toArray(new TaskInstanceVO[0]));
		
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance().lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO[] actInsVos = actInsQry.getActInsVoByProInsPk(processInstance.getProInsPk());
		for (int i = 0; i < actInsVos.length; i++) {
			actInsVos[i].setPk_prodef(proDefPk);
		}
		IWorkflowInstanceBill actInsBill = NCLocator.getInstance().lookup(IWorkflowInstanceBill.class);
		actInsBill.update(actInsVos);

	}
	private void addNewBpmnDiagram(Bpmn2MemoryModel model, BaseElement newBaseElement, BaseElement oldBaseElement,
			Connector oldSequenceFlow, Connector newSequenceFlow){
		BpmnDiagram bpmnDiagram = model.getBpmnDiagram();
		BpmnPlane bpmnPlane = bpmnDiagram.getBpmnPlane();
		BpmnShape oldBpmnShape = bpmnPlane.getBpmnShape(oldBaseElement);
		
		//BpmnShape
		BpmnShape bpmnShape = new BpmnShape();
		bpmnPlane.getBpmnShapes().add(bpmnShape);
		bpmnShape.setParentId(oldBpmnShape.getParentId());
		bpmnShape.setBpmnElement(newBaseElement.getId());
		bpmnShape.setId("BPMNShape_" + newBaseElement.getId());
		if(newBaseElement instanceof SubProcess){
			bpmnShape.setGraphStyle("shape=task;;fillColor=#BDBABA;image=/themeroot/blue/themeres/control/activiti/icons/activity/expanded.subprocess.png;fontFamily=宋体;fontSize=12;imageVerticalAlign=top;imageAlign=right;verticalAlign=top;verticalLabelPosition=bottom");
		}
		else{
			bpmnShape.setGraphStyle(oldBpmnShape.getGraphStyle());
		}
		BpmnBounds bpmnBounds = new BpmnBounds();
		bpmnShape.setBounds(bpmnBounds);
		
		bpmnBounds.setHeight(100);
		bpmnBounds.setWidth(140);
		bpmnBounds.setX(oldBpmnShape.getBounds().getX() + oldBpmnShape.getBounds().getWidth() + 10);
		bpmnBounds.setY(oldBpmnShape.getBounds().getY() + oldBpmnShape.getBounds().getHeight() + 10);

		//old BpmnEdge
		BpmnEdge oldBpmnEdge = bpmnPlane.getBpmnEdge(oldSequenceFlow);

		//new BpmnEdge
		BpmnEdge bpmnEdge = new BpmnEdge();
		bpmnPlane.getBpmnEdges().add(bpmnEdge);
		bpmnEdge.setBpmnElement(newSequenceFlow.getId());
		bpmnEdge.setParentId(oldBpmnEdge.getParentId());
		bpmnEdge.setId("BPMNEdge_" + newSequenceFlow.getId());
		bpmnEdge.setGraphStyle(oldBpmnEdge.getGraphStyle());
		BpmnWayPoint newSrcWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newSrcWayPoint);
		newSrcWayPoint.setX(0);
		newSrcWayPoint.setY(0);
		BpmnWayPoint newTargetWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newTargetWayPoint);
		newTargetWayPoint.setX(0);
		newTargetWayPoint.setY(0);
	}

	private StartEvent getMainStartEvent(Bpmn2MemoryModel model)
	{
		StartEvent mainStartEvent = null;
		for(FlowElement element :model.processes.get(0).getFlowElements())
		{
			if(element instanceof StartEvent)
			{
				return (StartEvent)element;
			}
		}
		return mainStartEvent;
	}

	private EndEvent getMainEndEvent(Bpmn2MemoryModel model)
	{
		EndEvent mainEndEvent = null;
		for(FlowElement element :model.processes.get(0).getFlowElements())
		{
			if(element instanceof EndEvent)
			{
				return (EndEvent)element;
			}
		}
		return mainEndEvent;
	}
	
	private void addNewSubprocessBpmnDiagram(Bpmn2MemoryModel model, StartEvent startEvent, EndEvent endEvent,
			UserTask userTask1, SequenceFlow sequenceflow1, SequenceFlow sequenceflow2, 
			BaseElement oldBaseElement, Connector oldSequenceFlow, SubProcess subProcess){
		BpmnDiagram bpmnDiagram = model.getBpmnDiagram();
		BpmnPlane bpmnPlane = bpmnDiagram.getBpmnPlane();
		
		StartEvent mainStartEvent = getMainStartEvent(model);
		BpmnShape oldBpmnShape = bpmnPlane.getBpmnShape(mainStartEvent);
		
		//Start Event
		BpmnShape bpmnShape = new BpmnShape();
		bpmnPlane.getBpmnShapes().add(bpmnShape);
		bpmnShape.setParentId(subProcess.getId());
		bpmnShape.setBpmnElement(startEvent.getId());
		bpmnShape.setId("BPMNShape_" + startEvent.getId());
		bpmnShape.setGraphStyle(oldBpmnShape.getGraphStyle());
		BpmnBounds bpmnBounds = new BpmnBounds();
		bpmnShape.setBounds(bpmnBounds);
		bpmnBounds.setHeight(20);
		bpmnBounds.setWidth(20);
		bpmnBounds.setX(0);
		bpmnBounds.setY(20);
		
		EndEvent mainEndEvent = getMainEndEvent(model);
		oldBpmnShape = bpmnPlane.getBpmnShape(mainEndEvent);
		//End Event
		bpmnShape = new BpmnShape();
		bpmnPlane.getBpmnShapes().add(bpmnShape);
		bpmnShape.setParentId(subProcess.getId());
		bpmnShape.setBpmnElement(endEvent.getId());
		bpmnShape.setId("BPMNShape_" + endEvent.getId());
		bpmnShape.setGraphStyle(oldBpmnShape.getGraphStyle());
		bpmnBounds = new BpmnBounds();
		bpmnShape.setBounds(bpmnBounds);
		bpmnBounds.setHeight(20);
		bpmnBounds.setWidth(20);
		bpmnBounds.setX(50);
		bpmnBounds.setY(70);

		oldBpmnShape = bpmnPlane.getBpmnShape(oldBaseElement);
		//User Task
		bpmnShape = new BpmnShape();
		bpmnPlane.getBpmnShapes().add(bpmnShape);
		bpmnShape.setParentId(subProcess.getId());
		bpmnShape.setBpmnElement(userTask1.getId());
		bpmnShape.setId("BPMNShape_" + userTask1.getId());
		bpmnShape.setGraphStyle(oldBpmnShape.getGraphStyle());
		bpmnBounds = new BpmnBounds();
		bpmnShape.setBounds(bpmnBounds);
		bpmnBounds.setHeight(30);
		bpmnBounds.setWidth(60);
		bpmnBounds.setX(75);
		bpmnBounds.setY(20);

		//old BpmnEdge
		BpmnEdge oldBpmnEdge = bpmnPlane.getBpmnEdge(oldSequenceFlow);

		//new sequenceflow1
		BpmnEdge bpmnEdge = new BpmnEdge();
		bpmnPlane.getBpmnEdges().add(bpmnEdge);
		bpmnEdge.setParentId(subProcess.getId());
		bpmnEdge.setBpmnElement(sequenceflow1.getId());
		bpmnEdge.setId("BPMNEdge_" + sequenceflow1.getId());
		bpmnEdge.setGraphStyle(oldBpmnEdge.getGraphStyle());
		BpmnWayPoint newSrcWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newSrcWayPoint);
		newSrcWayPoint.setX(0);
		newSrcWayPoint.setY(0);
		BpmnWayPoint newTargetWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newTargetWayPoint);
		newTargetWayPoint.setX(0);
		newTargetWayPoint.setY(0);

		//new sequenceflow2
		bpmnEdge = new BpmnEdge();
		bpmnPlane.getBpmnEdges().add(bpmnEdge);
		bpmnEdge.setParentId(subProcess.getId());
		bpmnEdge.setBpmnElement(sequenceflow2.getId());
		bpmnEdge.setId("BPMNEdge_" + sequenceflow2.getId());
		bpmnEdge.setGraphStyle(oldBpmnEdge.getGraphStyle());
		newSrcWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newSrcWayPoint);
		newSrcWayPoint.setX(0);
		newSrcWayPoint.setY(0);
		newTargetWayPoint = new BpmnWayPoint();
		bpmnEdge.getWaypoints().add(newTargetWayPoint);
		newTargetWayPoint.setX(0);
		newTargetWayPoint.setY(0);
	}
}
