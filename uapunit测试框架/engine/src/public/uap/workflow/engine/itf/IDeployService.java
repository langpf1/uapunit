package uap.workflow.engine.itf;

import uap.workflow.engine.core.ProcessDefinitionStatusEnum;

public interface IDeployService {
	void deploy(String fullFilename, String filename, boolean isDraft);
	void deleteProcessDefinition(String pkProcDef);
	void deleteProcessDefinition(String[] pkProcDefs);
	void setProcessDefinitionStatus(String pk_processDef, ProcessDefinitionStatusEnum status);
}
