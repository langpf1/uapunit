package uap.workflow.engine.itf;
import uap.workflow.engine.vos.VariableInstanceVO;
public interface IVariableInstanceBill {
	void inser(VariableInstanceVO variableInstanceVo);
	void update(VariableInstanceVO variableInstanceVo);
	void delete(String pk_variableInstance);
	VariableInstanceVO asyn(VariableInstanceVO vo);
}
