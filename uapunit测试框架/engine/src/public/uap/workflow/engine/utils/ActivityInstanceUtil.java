package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.List;
import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.ActivityInstanceBridge;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.vos.ActivityInstanceVO;
public class ActivityInstanceUtil {
	public static List<IActivityInstance> getSubActInsByActInsPk(String actInsPk) {
		List<IActivityInstance> list = new ArrayList<IActivityInstance>();
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO[] actInsVos = actInsQry.getSubActInsVoByPk(actInsPk);
		if (actInsVos == null || actInsVos.length == 0) {
			return list;
		}
		for (int i = 0; i < actInsVos.length; i++) {
			list.add(new ActivityInstanceBridge().convertM2T(actInsVos[0]));
		}
		return list;
	}
	public static IActivityInstance getActInsByActInsPk(String actInsPk) {
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO proDefVo = actInsQry.getActInsVoByPk(actInsPk);
		ActivityInstanceEntity actIns = new ActivityInstanceBridge().convertM2T(proDefVo);
		return actIns;
	}
	
	public static IActivityInstance getActivityInstanceVoByActivityID(String pk_ProcessInstance, String activityID) {
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO actInsVo = actInsQry.getActivityInstanceVoByActivityID(pk_ProcessInstance, activityID);
		if(actInsVo == null)
		{
			return null;
		}
		ActivityInstanceEntity actIns = new ActivityInstanceBridge().convertM2T(actInsVo);
		return actIns;
	}
	
	public static IActivityInstance[] getActInsByProInsPk(String proInsPk) {
		IWorkflowInstanceQry actInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceQry.class);
		ActivityInstanceVO[] actInsVos = actInsQry.getActInsVoByProInsPk(proInsPk);
		List<IActivityInstance> execution = new ArrayList<IActivityInstance>();
		for (int i = 0; i < actInsVos.length; i++) {
			execution.add(new ActivityInstanceBridge().convertM2T(actInsVos[0]));
		}
		return execution.toArray(new IActivityInstance[0]);
	}
}
