package uap.workflow.engine.impl;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import uap.workflow.engine.core.ProcessDefinitionStatusEnum;
import uap.workflow.engine.itf.IDeployService;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.repository.DeploymentBuilder;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.service.RepositoryService;
import uap.workflow.engine.service.WfmServiceFacility;
public class DeployService implements IDeployService {
	@Override
	public void deploy(String fullFilename, String filename, boolean isDraft) {
		BizProcessServer.getInstance().start();
		RepositoryService reposService = BizProcessServer.processEngine.getRepositoryService();
		DeploymentBuilder deployBuild = reposService.createDeployment();
		File file = new File(fullFilename);
		FileInputStream fileInput = null;
		try {   
			fileInput = new FileInputStream(file);
			deployBuild.addInputStream(filename, fileInput);
			deployBuild.setDraft(isDraft);
			deployBuild.deploy();
		} catch (FileNotFoundException e) {
			WorkflowLogger.error(e.getMessage(), e);
		} finally {
			try {
				fileInput.close();
			} catch (IOException e) {
				WorkflowLogger.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void deleteProcessDefinition(String pkProcDef) {
		BizProcessServer.getInstance().start();
		RepositoryService reposService = BizProcessServer.processEngine.getRepositoryService();
		DeploymentBuilder deployBuild = reposService.createDeployment();
		deployBuild.deleteDeployment(pkProcDef, true);
	}

	@Override
	public void deleteProcessDefinition(String[] pkProcDefs) {
		BizProcessServer.getInstance().start();
		RepositoryService reposService = BizProcessServer.processEngine.getRepositoryService();
		DeploymentBuilder deployBuild = reposService.createDeployment();
		for(String pkProcDef : pkProcDefs)
			deployBuild.deleteDeployment(pkProcDef, true);
	}

	@Override
	public void setProcessDefinitionStatus(String pk_processDef, ProcessDefinitionStatusEnum status) {
		WfmServiceFacility.getRepositoryService().setProcessDefinitionStatus(pk_processDef, status);
	}
}
