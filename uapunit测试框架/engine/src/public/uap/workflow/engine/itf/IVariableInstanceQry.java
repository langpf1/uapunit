package uap.workflow.engine.itf;
import java.util.List;
import uap.workflow.engine.vos.VariableInstanceVO;
public interface IVariableInstanceQry {
	VariableInstanceVO getVariableInstanceByPk(String pk_variableInstance);
	List<VariableInstanceVO> getVariableInstanceByProcessInstancePk(String processsInstancePk);
	List<VariableInstanceVO> getVariableInstanceByActivityInstancePk(String activityInstancePk);
	List<VariableInstanceVO> getVariableInstanceByTaskInstancePk(String taskInstancePk);
}
