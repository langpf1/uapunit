package uap.workflow.engine.itf;
import uap.workflow.engine.vos.ActivityInstanceVO;
public interface IWorkflowInstanceQry {
	ActivityInstanceVO getActInsVoByPk(String actInsPk);
	ActivityInstanceVO getActivityInstanceVoByActivityID(String pk_ProcessInstance, String activityID);
	ActivityInstanceVO[] getActInsVoByProInsPk(String proInsPk);
	ActivityInstanceVO[] getSubActInsVoByPk(String actInsPk);
}
