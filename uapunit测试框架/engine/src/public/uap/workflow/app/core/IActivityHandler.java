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
	 * ���ɺ����չ����
	 */
	void beforeHumAct();
	/**
	 * ���ɺ����չ����
	 */
	void afterHumAct();
	/**
	 * ��ȡ�û��Ӧ��������չ��class
	 * 
	 * @return
	 */
	String getTaskExtClazz();
	/**
	 * �������ݼ����չ����
	 * 
	 * @param formVo
	 * @param flwType
	 * @throws WorkflowValidateException
	 */
	void check(IBusinessKey formInfo) throws WorkflowValidateException;
	/**
	 * �Ƿ�����ָ����չ����
	 * 
	 * @param task
	 * @param humAct
	 * @return
	 */
	boolean isAssign(ITask task, IActivity humAct);
	/**
	 * ����ǰ��ǩ����֯����
	 * 
	 * @param task
	 * @return
	 */
	FlowOrgDesc[] getBeforeAddSignOrgDesc(String taskPk);
	/**
	 * ����ǰ��ǩ�Ĳ�������
	 * 
	 * @param taskPk
	 * @param orgPk
	 * @return
	 */
	FlowDeptDesc[] getBeforeAddSignDeptDesc(String taskPk, String orgPk);
	/**
	 * ����ǰ��ǩ���û�����
	 * 
	 * @param taskPk
	 * @param deptPk
	 * @return
	 */
	FlowUserDesc[] getBeforeAddSignUserDesc(String taskPk, String deptPk);
	/**
	 * �����ĵ���֯����
	 * 
	 * @param taskPk
	 * @return
	 */
	FlowOrgDesc[] getDeliverOrgDesc(String taskPk);
	/**
	 * �����ĵĲ��Ŵ���
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
