package uap.workflow.engine.dftimpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IActivityHandler;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowValidateException;
import uap.workflow.engine.orgs.FlowDeptDesc;
import uap.workflow.engine.orgs.FlowOrgDesc;
import uap.workflow.engine.orgs.FlowUserDesc;
public class DftActInsHandler implements IActivityHandler {
	@Override
	public void beforeHumAct() {}
	@Override
	public void afterHumAct() {}
	@Override
	public String getTaskExtClazz() {
		return null;
	}
	@Override
	public void check(IBusinessKey formInfo) throws WorkflowValidateException {}
	@Override
	public boolean isAssign(ITask task, IActivity humAct) {
		return false;
	}
	@Override
	public FlowOrgDesc[] getBeforeAddSignOrgDesc(String taskPk) {
		return null;
	}
	@Override
	public FlowDeptDesc[] getBeforeAddSignDeptDesc(String taskPk, String orgPk) {
		return null;
	}
	@Override
	public FlowUserDesc[] getBeforeAddSignUserDesc(String taskPk, String deptPk) {
		return null;
	}
	@Override
	public FlowOrgDesc[] getDeliverOrgDesc(String taskPk) {
		return null;
	}
	@Override
	public FlowDeptDesc[] getDeliverDeptDesc(String taskPk, String orgPk) {
		return null;
	}
	@Override
	public FlowUserDesc[] getDeliverUserDesc(String taskPk, String deptPk) {
		return null;
	}
}
