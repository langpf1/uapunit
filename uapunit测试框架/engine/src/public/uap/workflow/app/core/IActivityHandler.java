package uap.workflow.app.core;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowValidateException;
import uap.workflow.engine.orgs.FlowDeptDesc;
import uap.workflow.engine.orgs.FlowOrgDesc;
import uap.workflow.engine.orgs.FlowUserDesc;
/**
 * 
 * @author tianchw
 * 
 */
public interface IActivityHandler {
	/**
	 * 活动完成后的扩展操作
	 */
	void beforeHumAct();
	/**
	 * 活动完成后的扩展操作
	 */
	void afterHumAct();
	/**
	 * 获取该活动对应的任务扩展的class
	 * 
	 * @return
	 */
	String getTaskExtClazz();
	/**
	 * 单据数据检查扩展操作
	 * 
	 * @param formVo
	 * @param flwType
	 * @throws WorkflowValidateException
	 */
	void check(IBusinessKey formInfo) throws WorkflowValidateException;
	/**
	 * 是否允许指派扩展操作
	 * 
	 * @param task
	 * @param humAct
	 * @return
	 */
	boolean isAssign(ITask task, IActivity humAct);
	/**
	 * 任务前加签的组织描述
	 * 
	 * @param task
	 * @return
	 */
	FlowOrgDesc[] getBeforeAddSignOrgDesc(String taskPk);
	/**
	 * 任务前加签的部门描述
	 * 
	 * @param taskPk
	 * @param orgPk
	 * @return
	 */
	FlowDeptDesc[] getBeforeAddSignDeptDesc(String taskPk, String orgPk);
	/**
	 * 任务前加签的用户描述
	 * 
	 * @param taskPk
	 * @param deptPk
	 * @return
	 */
	FlowUserDesc[] getBeforeAddSignUserDesc(String taskPk, String deptPk);
	/**
	 * 任务传阅的组织描述
	 * 
	 * @param taskPk
	 * @return
	 */
	FlowOrgDesc[] getDeliverOrgDesc(String taskPk);
	/**
	 * 任务传阅的部门传阅
	 * 
	 * @param taskPk
	 * @param orgPk
	 * @return
	 */
	FlowDeptDesc[] getDeliverDeptDesc(String taskPk, String orgPk);
	/**
	 * chua
	 * 
	 * @param taskPk
	 * @param deptPk
	 * @return
	 */
	FlowUserDesc[] getDeliverUserDesc(String taskPk, String deptPk);
}
