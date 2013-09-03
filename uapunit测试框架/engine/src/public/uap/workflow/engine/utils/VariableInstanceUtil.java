package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.List;


import uap.workflow.engine.bridge.VariableInstanceBridge;
import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.vos.VariableInstanceVO;
public class VariableInstanceUtil {
	public static List<VariableInstanceEntity> getVarInsByProInsPk(String proInsPk) {
		List<VariableInstanceVO> list = WfmServiceFacility.getVariableInstanceQry().getVariableInstanceByProcessInstancePk(proInsPk);
		if (list == null) {
			return null;
		}
		List<VariableInstanceEntity> list1 = new ArrayList<VariableInstanceEntity>();
		for (int i = 0; i < list.size(); i++) {
			list1.add(new VariableInstanceBridge().convertM2T(list.get(i)));
		}
		return list1;
	}
	public static List<VariableInstanceEntity> getVarInsByActInsPk(String actInsPk) {
		List<VariableInstanceVO> list = WfmServiceFacility.getVariableInstanceQry().getVariableInstanceByActivityInstancePk(actInsPk);
		if (list == null) {
			return null;
		}
		List<VariableInstanceEntity> list1 = new ArrayList<VariableInstanceEntity>();
		for (int i = 0; i < list.size(); i++) {
			list1.add(new VariableInstanceBridge().convertM2T(list.get(i)));
		}
		return list1;
	}
	public static List<VariableInstanceEntity> getVarInsByTaskPk(String taskPk) {
		List<VariableInstanceVO> list = WfmServiceFacility.getVariableInstanceQry().getVariableInstanceByTaskInstancePk(taskPk);
		if (list == null) {
			return null;
		}
		List<VariableInstanceEntity> list1 = new ArrayList<VariableInstanceEntity>();
		for (int i = 0; i < list.size(); i++) {
			list1.add(new VariableInstanceBridge().convertM2T(list.get(i)));
		}
		return list1;
	}
}
