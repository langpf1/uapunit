package uap.workflow.engine.itf;
import uap.workflow.engine.vos.ProcessDefinitionVO;
public interface IProcessDefinitionQry {
	ProcessDefinitionVO getProDefVoByPk(String proDefPk);
	ProcessDefinitionVO getProDefVoById(String proDefId);
	ProcessDefinitionVO[] getProcessDefVOAccordingBiz(String pk_group, String bizObjectKey);
	ProcessDefinitionVO[] getAllProcessDef(String pk_group);
	ProcessDefinitionVO[] getProcessDefVOByProdefGroup(String pk_group);
	
}
