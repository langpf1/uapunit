package uap.workflow.engine.observer;
import java.util.Observable;
import java.util.Observer;


import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.ActivityInstanceBridge;
import uap.workflow.engine.bridge.ProcessDefinitionBridge;
import uap.workflow.engine.bridge.ProcessInstanceBridge;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.bridge.VariableInstanceBridge;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.ProcessDefinitionEntity;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.itf.IWorkflowInstanceBill;
import uap.workflow.engine.itf.IProcessDefinitionBill;
import uap.workflow.engine.itf.IProcessInstanceBill;
import uap.workflow.engine.itf.ITaskInstanceBill;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.engine.vos.VariableInstanceVO;
public class AsynDataBase implements Observer {
	private static AsynDataBase instance = null;
	/**
	 * 所有的目标对象共有一个观察者实例
	 * 
	 * @return
	 */
	synchronized public static AsynDataBase getInstance() {
		if (instance != null)
			return instance;
		instance = new AsynDataBase();
		return instance;
	}
	private AsynDataBase() {}
	/**
	 * 数据变更后同步信息到数据库
	 */
	public void update(Observable o, Object arg) {
		if (o instanceof ActivityInstanceEntity) {
			ActivityInstanceEntity actIns = (ActivityInstanceEntity) o;
			ActivityInstanceVO actInsVo = new ActivityInstanceBridge().convertT2M(actIns);
			actInsVo = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IWorkflowInstanceBill.class).asyn(actInsVo);
			actIns.setPk_actins(actInsVo.getPk_actins());
			return;
		}
		if (o instanceof TaskEntity) {
			TaskEntity taskIns = (TaskEntity) o;
			TaskInstanceVO taskInsVo = new TaskInstanceBridge().convertT2M(taskIns);
			taskInsVo = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(ITaskInstanceBill.class).asyn(taskInsVo);
			taskIns.setTaskPk(taskInsVo.getPk_task());
			return;
		}
		if (o instanceof ProcessDefinitionEntity) {
			ProcessDefinitionEntity proDef = (ProcessDefinitionEntity) o;
			ProcessDefinitionVO proDefVo = new ProcessDefinitionBridge().convertT2M(proDef);
			proDefVo = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionBill.class).saveProDefVo(proDefVo);
			proDef.setProDefPk(proDefVo.getPk_prodef());
		}
		if (o instanceof ProcessInstanceEntity) {
			ProcessInstanceEntity proIns = (ProcessInstanceEntity) o;
			ProcessInstanceVO proInsVo = new ProcessInstanceBridge().convertT2M(proIns);
			proInsVo = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessInstanceBill.class).asyn(proInsVo);
			proIns.setPk_proins(proInsVo.getPk_proins());
		}
		if (o instanceof VariableInstanceEntity) {
			VariableInstanceEntity var = (VariableInstanceEntity) o;
			VariableInstanceVO vo = new VariableInstanceBridge().convertT2M(var);
			vo = WfmServiceFacility.getVariableInstanceBill().asyn(vo);
			var.setId(vo.getPk_variable());
		}
	}
}
