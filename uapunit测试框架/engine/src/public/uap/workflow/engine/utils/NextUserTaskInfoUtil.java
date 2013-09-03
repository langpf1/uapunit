package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.engine.actsgy.ActorSgyManager;
import uap.workflow.engine.bpmn.behavior.BoundaryEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ComplexGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.GatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.InclusiveGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateCatchEventActivityBehaviour;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowCompensationEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowNoneEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.IntermediateThrowSignalEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ManualTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.MultiInstanceActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneEndEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.NoneStartEventActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ParallelGatewayActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ReceiveTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ScriptTaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.ServiceTaskJavaDelegateActivityBehavior;
import uap.workflow.engine.bpmn.behavior.SubProcessActivityBehavior;
import uap.workflow.engine.bpmn.behavior.TaskActivityBehavior;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.bpmn.helper.ClassDelegate;
import uap.workflow.engine.bpmn.parser.BpmnParse;
import uap.workflow.engine.cmpltsgy.CompleteSgyManager;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessElement;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.orgs.FlowUserDesc;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.process.ActivityImpl;
import uap.workflow.engine.query.Condition;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.vos.AssignmentVO;
public class NextUserTaskInfoUtil{
	
	/**
	 * 设置流程引擎的上下文信息
	 * 提供给引擎外的project调用NextUserTaskInfoUtil时组织上下文用
	 */
	public static void setCommitProInsCtx(IBusinessKey bizObject, String operator) {
		WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
		WorkflowContext.setBpmnSession(bpmnSession);

		CommitProInsCtx flowInfo = new CommitProInsCtx();
		flowInfo.setUserPk(operator);
		
		IFlowRequest request = BizProcessServer.createFlowRequest(bizObject, flowInfo);
		IFlowResponse response = BizProcessServer.createFlowResponse();
		bpmnSession.setRequest(request);
		bpmnSession.setResponse(response);
	}

	/**
	 * 设置流程引擎的上下文信息
	 * 提供给引擎外的project调用NextUserTaskInfoUtil时组织上下文用
	 */
	public static void setNextTaskInsCtx(IBusinessKey bizObject, String operator, boolean isPass) {
		WorkflowContext.WorkflowSession bpmnSession = new WorkflowContext().new WorkflowSession();
		WorkflowContext.setBpmnSession(bpmnSession);

		NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
		nextTaskCtx.setUserPk(operator);
		nextTaskCtx.setPass(isPass);
		
		IFlowRequest request = BizProcessServer.createFlowRequest(bizObject, nextTaskCtx);
		IFlowResponse response = BizProcessServer.createFlowResponse();
		bpmnSession.setRequest(request);
		bpmnSession.setResponse(response);
	}	
	
	/**
	 * 获取流程下一步节点信息
	 */
	public static List<UserTaskPrepCtx> getNextUserTaskInfo(String taskPk, IBusinessKey formVo,String pk_user) {
		return getUserTaskPrepCtx(TaskUtil.getTaskByTaskPk(taskPk),null, formVo,pk_user);
	}

	public static List<UserTaskPrepCtx> getNextUserTaskInfo(IActivity activity, IBusinessKey formVo) {
		List<IActivity> nextPorts = new ArrayList<IActivity>();
		getNextUserTaskActivity(null, nextPorts, formVo, activity.getOutgoingTransitions().toArray(new IProcessElement[0]));
		List<UserTaskPrepCtx> list = new ArrayList<UserTaskPrepCtx>();
		if (nextPorts.size() == 0) {
			return list;
		}
		for (Iterator<IActivity> iter = nextPorts.iterator(); iter.hasNext();) {
			list.add(getUserTaskPrepCtx(iter.next(), formVo));
		}
		return list;
	}

	/**
	 * 获取流程开始下一步节点信息
	 */
	public static List<UserTaskPrepCtx> getStartNextUserTaskInfo(String proDefPk, IBusinessKey formVo, String pk_user) {
		IProcessDefinition proDef = WfmServiceFacility.getRepositoryService().getProcessDefinitionByPk(proDefPk);
		return getUserTaskPrepCtx(null, proDef, formVo,pk_user);
	}

	protected  static void getNextUserTaskActivity(ITask task, List<IActivity> nextInfo, IBusinessKey formVo, IProcessElement[] ges) {
		for (int i = 0; i < ges.length; i++) {
			IProcessElement o = ges[i];
			if (o instanceof IActivity) {
				IActivity port = (IActivity) o;
				ActivityBehavior beanavior = ((IActivity) o).getActivityBehavior();
				if (beanavior instanceof NoneStartEventActivityBehavior) {// 开始
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof NoneEndEventActivityBehavior) {// 结束
					if (port.getParent() instanceof IActivity) {
						IActivity initialActivity = (ActivityImpl) port.getParent();
						boolean flag = CompleteSgyManager.getInstance().isComplete(task.getExecution().getSuperExecution(), null);
						if (flag) {
							getNextUserTaskActivity(task, nextInfo, formVo, initialActivity.getOutgoingTransitions().toArray(new IProcessElement[0]));
						} else {
							IActivity initialActivity1 = (ActivityImpl) task.getExecution().getSuperExecution().getActivity().getProperty(BpmnParse.PROPERTYNAME_INITIAL);
							getNextUserTaskActivity(task, nextInfo, formVo, new IProcessElement[] { initialActivity1 });
						}
					}
				} else if (beanavior instanceof GatewayActivityBehavior) {// 网关
					if (beanavior instanceof ExclusiveGatewayActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					} else if (beanavior instanceof InclusiveGatewayActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					} else if (beanavior instanceof ParallelGatewayActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					}else if(beanavior instanceof ComplexGatewayActivityBehavior){
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					}
				} else if (beanavior instanceof TaskActivityBehavior) {// 任务
					if (beanavior instanceof UserTaskActivityBehavior) {
						nextInfo.add(port);
					} else if (beanavior instanceof ServiceTaskJavaDelegateActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					} else if (beanavior instanceof ManualTaskActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					} else if (beanavior instanceof ReceiveTaskActivityBehavior) {
						//nextInfo.add(port);
					} else if (beanavior instanceof ScriptTaskActivityBehavior) {
						getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
					}
				} else if (beanavior instanceof SubProcessActivityBehavior) {
					IActivity initialActivity = (ActivityImpl) port.getProperty(BpmnParse.PROPERTYNAME_INITIAL);
					getNextUserTaskActivity(task, nextInfo, formVo, new IProcessElement[] { initialActivity });
				} else if (beanavior instanceof ClassDelegate) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof BoundaryEventActivityBehavior) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof IntermediateThrowSignalEventActivityBehavior) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof IntermediateThrowCompensationEventActivityBehavior) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof IntermediateThrowNoneEventActivityBehavior) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				} else if (beanavior instanceof IntermediateCatchEventActivityBehaviour) {
					getNextUserTaskActivity(task, nextInfo, formVo, port.getOutgoingTransitions().toArray(new IProcessElement[0]));
				}else if(beanavior instanceof MultiInstanceActivityBehavior){
					IActivity initialActivity = (ActivityImpl) port.getProperty(BpmnParse.PROPERTYNAME_INITIAL);
					getNextUserTaskActivity(task, nextInfo, formVo, new IProcessElement[] { initialActivity });
				}
			} else if (o instanceof ITransition) {// 连线处理
				ITransition sf = (ITransition) o;
				Condition condition = (Condition) sf.getProperty(BpmnParse.PROPERTYNAME_CONDITION); //得到线上面的条件
				if (condition == null) {
					getNextUserTaskActivity(task, nextInfo, formVo, new IProcessElement[] { sf.getDestination() });
				} else {
					ActivityInstanceEntity entity = null;
					if (task == null) {
						entity = new ActivityInstanceEntity();
						entity.setActivity(sf.getSource());
						entity.setFormInfoCtx(formVo);
					} else {
						entity = (ActivityInstanceEntity) task.getExecution().createExecution(sf.getSource());
						entity.setFormInfoCtx(formVo);
					}
					if (condition.evaluate(entity)) {
						getNextUserTaskActivity(task, nextInfo, formVo, new IProcessElement[] { sf.getDestination() });
					}
				}
			}
		}
	}

	// 获取预计算的下一步的参与者信息
	private static String[] getPrepNextActors(IBusinessKey formVo, IActivity activity) {
		ActivityBehavior behavior = activity.getActivityBehavior();
		TaskDefinition taskDefinition = null;
		if (behavior instanceof UserTaskActivityBehavior) {
			UserTaskActivityBehavior userTaskBehaviro = (UserTaskActivityBehavior) behavior;
			taskDefinition = userTaskBehaviro.getTaskDefinition();
		}
		ParticipantContext pc = new ParticipantContext();
		pc.setBillEntity(formVo);
		pc.setPk_org("");
		pc.setParticipants(taskDefinition.getParticipants());
		List<String> users = WfmServiceFacility.getIParticipantService().getUsers(pc);
		return users.toArray(new String[0]);
	}
	private static UserTaskPrepCtx getUserTaskPrepCtx(IActivity activity, IBusinessKey formInoCtx) {
		UserTaskPrepCtx userTaskPrepCtx = new UserTaskPrepCtx();
		ActivityBehavior beanavior = activity.getActivityBehavior();
		if (beanavior instanceof UserTaskActivityBehavior) {
			String[] userPks = getPrepNextActors(formInoCtx, activity);
			if (userPks == null || userPks.length == 0) {// 如果没有计算出参与者，说明活动设置有问题
				throw new WorkflowRuntimeException(activity.getId() + "无有效的任务执行人");
			} else if (userPks.length == 1) {// 如果只有一个人，不需要进行指派
				userTaskPrepCtx.setAssign(false);
			} else {
				boolean isPreAssign = ((UserTaskActivityBehavior) beanavior).getTaskDefinition().isAssign();
				if (isPreAssign) {
					userTaskPrepCtx.setAssign(true);
				} else {
					userTaskPrepCtx.setAssign(false);
				}
			}
			userTaskPrepCtx.setActivityId(activity.getId());
			userTaskPrepCtx.setActivityName((String) activity.getProperty("name"));
			userTaskPrepCtx.setUserPks(userPks);
			userTaskPrepCtx.setUserNames(getUserNamesByUserPks(userPks));
		}
		return userTaskPrepCtx;
	}
	/**
	 * 获取所有节点的下一步信息
	 */
	private static List<UserTaskPrepCtx> getUserTaskPrepCtx(ITask task, IProcessDefinition proDef, IBusinessKey formVo,String pk_user) {
		List<IActivity> nextPorts = new ArrayList<IActivity>();
		IActivity cntHumAct = null;
		if (task == null) {// 流程没有开始，直接获取开始节点的信息
			cntHumAct = proDef.getInitial();
			getNextUserTaskActivity(task, nextPorts, formVo, cntHumAct.getOutgoingTransitions().toArray(new IProcessElement[0]));
		} else {
			cntHumAct = task.getExecution().getActivity();
			boolean flag = false;
			TaskEntity taskIns = (TaskEntity) task;
			if (taskIns.getCreateType().equals(TaskInstanceCreateType.BeforeAddSign)) {
				flag = false;
			} else {
				flag = CompleteSgyManager.getInstance().isComplete((ActivityInstanceEntity) task.getExecution(), null);
			}
			if (flag) {// 如果当前活动节点已经完成的话，直接获取下一步信息
				getNextUserTaskActivity(task, nextPorts, formVo, cntHumAct.getOutgoingTransitions().toArray(new IProcessElement[0]));
			} else {
				/**
				 * 1 顺序后加签
				 * 2 顺序前加签
				 * 3 顺序指派
				 */
				boolean afterSignSequence = false;
				if (cntHumAct.isAfterSign() && cntHumAct.isSequence()) {
					afterSignSequence = true;
				}
				if (taskIns.getCreateType() == TaskInstanceCreateType.BeforeAddSign || afterSignSequence) {
					nextPorts.add(cntHumAct);
				}else{
					AssignmentVO assignmentVo = WfmServiceFacility.getAssignmentQry().getAssignmentVo(
							taskIns.getProcessInstance().getProInsPk(), cntHumAct.getId(), "0");
					if(assignmentVo!=null && assignmentVo.getSequence().booleanValue()){
						nextPorts.add(cntHumAct);
					}
				}
				
			}
		}
		List<UserTaskPrepCtx> list = new ArrayList<UserTaskPrepCtx>();
		if (nextPorts.size() == 0) {
			return list;
		}
		for (Iterator<IActivity> iter = nextPorts.iterator(); iter.hasNext();) {
			list.add(getUserTaskPrepCtx(task, proDef, formVo, iter.next(),pk_user));
		}
		return list;
	}
	/**
	 * 获取某个节点的下一步信息
	 */
	private static UserTaskPrepCtx getUserTaskPrepCtx(ITask task, IProcessDefinition proDef, IBusinessKey formInfo, IActivity port,String pk_user) {
		UserTaskPrepCtx userTaskPrepCtx = new UserTaskPrepCtx();
		IActivity o = (IActivity) port;
		ActivityBehavior beanavior = ((IActivity) o).getActivityBehavior();
		if (beanavior instanceof UserTaskActivityBehavior) {
			String[] userPks = getUserPks(task, port, formInfo,pk_user);
			if (userPks == null || userPks.length == 0) {// 如果没有计算出参与者，说明活动设置有问题
				throw new WorkflowRuntimeException(port.getId() + "无有效的任务执行人");
			} else if (userPks.length == 1) {// 如果只有一个人，不需要进行指派
				userTaskPrepCtx.setAssign(false);
			} else {
				boolean isPreAssign = ((UserTaskActivityBehavior) beanavior).getTaskDefinition().isAssign();
				if (isPreAssign) {
					userTaskPrepCtx.setAssign(true);
				} else {
					userTaskPrepCtx.setAssign(false);
				}
			}
			userTaskPrepCtx.setActivityId(port.getId());
			userTaskPrepCtx.setActivityName((String) o.getProperty("name"));
			userTaskPrepCtx.setUserPks(userPks);
			userTaskPrepCtx.setUserNames(getUserNamesByUserPks(userPks));
		}
		return userTaskPrepCtx;
	}

	private static String[] getUserNamesByUserPks(String[] userPks) {
		FlowUserDesc[] userVos = getFlowUserDesc(userPks);
		if (userVos == null) {
			return null;
		}
		FlowUserDesc userVo = null;
		String userNames[] = new String[userPks.length];
		int length = userVos.length;
		for (int i = 0; i < length; i++) {
			userVo = userVos[i];
			userNames[i] = userVo.getName();
		}
		return userNames;
	}
	
	private static String[] getUserPks(ITask task, IActivity humAct, IBusinessKey formVo,String pk_user) {
		String[] userPks = ActorSgyManager.getInstance().getActorsRange(formVo, humAct, task,pk_user);
		return userPks;
	}
	
	private static FlowUserDesc[] getFlowUserDesc(String[] userPks) {
		if(userPks==null||userPks.length==0){
			return  null;
		}
		FlowUserDesc[] descs=new FlowUserDesc[userPks.length];
		UserVO uservo=null;
		IUserManageQuery userMgr=NCLocator.getInstance().lookup(IUserManageQuery.class);
		for(int i=0;i<userPks.length;i++){
			try {
				uservo=	userMgr.getUser(userPks[i]);
				if(uservo==null){
					continue;
				}
			} catch (BusinessException e) {
				WorkflowLogger.error(e.getMessage());
				throw new WorkflowRuntimeException(e.getMessage(),e);
			}
			FlowUserDesc desc=new FlowUserDesc();
			desc.setName(uservo.getUser_name());
			desc.setPk_flowuser(uservo.getCuserid());
			descs[i]=desc;
		   	
		}
		return descs;
	}
}
