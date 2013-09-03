package uap.workflow.modeler.utils;

import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.Gateway;
import uap.workflow.bpmn2.model.Task;
import nc.bs.logging.Logger;

public class UserObjectFactory {

	private UserObjectFactory() {

	}

	public static UserObjectFactory instance = new UserObjectFactory();
	
	public static FlowElement getUserObject(int iType,String elementId ,String elementType){
		if(elementType.equals("task")){
			return getTask(iType,elementId);
		}else if(elementType.equals("gateWay")){
			return getGateWay(iType,elementId);
		}
		return null;
	}

	public static Task getTask(int itaskType,String elementId) {
		String taskName = null;
		switch (itaskType) {
		case 0:
			taskName = "UserTask";
			break;
		case 1:
			taskName = "ScriptTask";
			break;
		case 2:
			taskName = "ServiceTask";
			break;
		case 3:
			taskName = "MailTask";
			break;
		case 4:
			taskName = "ManualTask";
			break;
		case 5:
			taskName = "ReceiveTask";
			break;
		case 6:
			taskName = "BusinessRuleTask";
			break;
		default:
			taskName = "UserTask";
			break;
		}
		try {
			Task task = (Task) Class.forName("nc.ui.pub.activiti.bpmn2.model." + taskName).newInstance();
			task.setId(elementId);
			task.setName(taskName);
			task.setTaskType(itaskType);
			return task;
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return null;
		}
	}

	public static Task getTask(BpmnTaskTypeEnum tasktype,String elementId) {
		int itaskType = tasktype.getIntValue();
		return getTask(itaskType, elementId);
	}
	
	public static Gateway getGateWay(BpmnGatewayTypeEnum gatewayType,String elementId){
		return getGateWay(gatewayType.getIntValue(), elementId);
	}
	
	public static Gateway getGateWay(int iGatewayType,String elementId){
		String gateWayName =null;
		switch(iGatewayType){
		case 0:gateWayName ="ParallelGateway";break;
		case 1:gateWayName ="ExclusiveGateway";break;
		case 2:gateWayName ="InclusiveGateway";break;
		case 3:gateWayName ="EventGateway";break;
		default :gateWayName ="ParallelGateway";break;
		}
		try {
			Gateway gateway =(Gateway) Class.forName("nc.ui.pub.activiti.bpmn2.model." + gateWayName).newInstance();
			gateway.setId(elementId);
			gateway.setName(gateWayName);
			return gateway;
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return null;
		} 
	}
}
