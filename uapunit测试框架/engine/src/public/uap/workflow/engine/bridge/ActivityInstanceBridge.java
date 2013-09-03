package uap.workflow.engine.bridge;
import nc.bs.framework.common.NCLocator;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.itf.IWorkflowInstanceQry;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.context.Context;
public class ActivityInstanceBridge implements IBridge<ActivityInstanceVO, ActivityInstanceEntity> {
	@Override
	public ActivityInstanceVO convertT2M(ActivityInstanceEntity object) {
		ActivityInstanceVO actIns = new ActivityInstanceVO();
		actIns.setPk_actins(object.getPk_actins());
		actIns.setPk_proins(object.getProcessInstance().getProInsPk());
		if (object.getParent() != null) {
			actIns.setPk_parent(object.getParent().getActInsPk());
		}
		actIns.setPort_id(object.getActivity().getId());
		if (object.getParentActivity() != null) {
			actIns.setPport_id(object.getParentActivity().getId());
		}
		actIns.setPk_prodef(object.getProcessDefinition().getProDefPk());
		actIns.setProdef_id(object.getProcessDefinition().getProDefId());
		actIns.setIsexe(new UFBoolean(object.isExe()));
		actIns.setIspass(new UFBoolean(object.isPass()));
		actIns.setIsreject(new UFBoolean(object.isRejected()));
		actIns.setState_actins(object.getStatus().getIntValue());
		actIns.setBegindate(new UFDateTime(object.getBegindate()));
		if (object.getSuperExecution() != null) {
			actIns.setPk_super(object.getSuperExecution().getActInsPk());
		}
		return actIns;
	}
	@Override
	public ActivityInstanceEntity convertM2T(ActivityInstanceVO object) {
		ActivityInstanceEntity entity = new ActivityInstanceEntity();
		entity.setActInsVo(object);
		entity.setPk_actins(object.getPk_actins());
		String portId = object.getPort_id();
		IProcessDefinition proDef = ProcessDefinitionUtil.getProDefByProDefPk(object.getPk_prodef());
		entity.setActivity(proDef.findActivity(portId));
		entity.setExe(object.getIsexe().booleanValue());
		entity.setPass(object.getIspass().booleanValue());
		entity.setProcessInstance(null);
		entity.setParent(null);
		//因callActivity调用子流程，子流程中的流程元素没有superExecution，不能够返回到主流程，改。zhailzh 2013.1.29
		if(object.getPk_super() != null){
			//object成员变量pk_super 为调用流程的活动的pk
			IWorkflowInstanceQry actInsQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceQry.class);
			ActivityInstanceVO proDefVo = actInsQry.getActInsVoByPk(object.getPk_super());
			ActivityInstanceEntity actIns = new ActivityInstanceBridge().convertM2T(proDefVo);
			entity.setSuperExecution(actIns);
		}
		return entity;
	}
}
