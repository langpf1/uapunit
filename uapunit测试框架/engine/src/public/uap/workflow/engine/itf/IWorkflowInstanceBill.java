package uap.workflow.engine.itf;
import uap.workflow.engine.vos.ActivityInstanceVO;
public interface IWorkflowInstanceBill {
	ActivityInstanceVO asyn(ActivityInstanceVO actInsVo);
	void update(ActivityInstanceVO[] actInsVos);
}
