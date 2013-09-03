package uap.workflow.engine.server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import nc.bs.framework.common.NCLocator;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.cache.WfCacheManager;
import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.StartedProInsCtx;
import uap.workflow.engine.dftimpl.FlowRequest;
import uap.workflow.engine.dftimpl.FlowResponse;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.filter.BpmnFilter1;
import uap.workflow.engine.filter.BpmnFilter2;
import uap.workflow.engine.filter.BpmnFlowFilter;
import uap.workflow.engine.filter.IFilter;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.plugin.PluginDefParser;
import uap.workflow.engine.repository.DeploymentBuilder;
import uap.workflow.engine.service.ProcessEngine;
import uap.workflow.engine.service.ProcessEngineConfiguration;
import uap.workflow.engine.service.RepositoryService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
public class BizProcessServer implements IProcessServer {
	private static final long serialVersionUID = 6340541014802446536L;
	public static BizProcessServer instance = null;
	public static ProcessEngine processEngine = null;
	public static ProcessEngineConfigurationImpl processEngineConfig = null;
	public static WfmServiceFacility serviceFactory = new WfmServiceFacility();
	public static List<IFilter> filters = new ArrayList<IFilter>();;
	public boolean isStart = false;
	synchronized public static BizProcessServer getInstance() {
		if (instance == null) {
			instance = new BizProcessServer();
		}
		return instance;
	}
	public static void ensuerInit() {
		BizProcessServer.getInstance();
	};
	public static ProcessEngine getProcessEngine() {
		ensuerInit();
		if(processEngine == null)
		{
			BizProcessServer.getInstance().start();
		}
		return processEngine;
	}
	public static ProcessEngineConfigurationImpl getProcessEngineConfig() {
		ensuerInit();
		return processEngineConfig;
	}
	public static CommandExecutor getCommandExecutorTxRequired() {
		ensuerInit();
		return BizProcessServer.getProcessEngineConfig().getCommandExecutorTxRequired();
	}
	public static CommandExecutor getCommandExecutorTxRequiresNew() {
		ensuerInit();
		return BizProcessServer.getProcessEngineConfig().getCommandExecutorTxRequiresNew();
	}
	public void deploy() {
		RepositoryService reposService = processEngine.getRepositoryService();
		DeploymentBuilder deployBuild = reposService.createDeployment();
		File file = new File("c:\\wf\\activiti-engine-examples.bar");
		FileInputStream fileInput = null;
		ZipInputStream input = null;
		try {
			fileInput = new FileInputStream(file);
			input = new ZipInputStream(fileInput);
			deployBuild.addZipInputStream(new ZipInputStream(fileInput));
			// deployBuild.addInputStream(resourceName, inputStream)
		} catch (FileNotFoundException e) {
			WorkflowLogger.error(e.getMessage(), e);
		} finally {
			try {
				fileInput.close();
				input.close();
			} catch (IOException e) {
				WorkflowLogger.error(e.getMessage(), e);
			}
		}
		// deployBuild.deploy();
	}
	public void destory() {
		processEngine.close();
	}
	public List<IFilter> getFilters() {
		filters.clear();
		filters.add(new BpmnFilter1());
		filters.add(new BpmnFilter2());
		filters.add(new BpmnFlowFilter());
		return filters;
	}
	public void monitor() {
		// new Executor(new TaskSheduler()).start();
	}
	public void start() {
		if (!isStart) {
			processEngineConfig = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
			processEngine = processEngineConfig.buildProcessEngine();
			// this.deploy();
			this.monitor();
			//initPlugin();
			isStart = true;
		}
	}
	
//	private void initPlugin() {
//		URL url = Thread.currentThread().getContextClassLoader().getResource("plugin.xml");
//		PluginDefParser.reader(url.getPath());
//	}
	
	public void stop() throws Exception {
		this.destory();
	}
	public static IFlowRequest createFlowRequest(IBusinessKey formInfoCtx, FlowInfoCtx flowInfoCtx) {
		IFlowRequest request = new FlowRequest();
		request.setFlowInfoCtx(flowInfoCtx);
		request.setBusinessObject(formInfoCtx);
		return request;
	}
	public static IFlowResponse createFlowResponse() {
		IFlowResponse response = new FlowResponse();
		return response;
	}
	public static void execute(IFlowRequest request, IFlowResponse response) {
		try {
			if(!(getInstance().isStart))
			{
				getInstance().start();
			}
			NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessEngine.class).execute(request, response);
		} catch (Exception e) {
			throw new WorkflowRuntimeException(e.getMessage());
		} finally {
			FlowInfoCtx flowInfoCtx = request.getFlowInfoCtx();
			if (flowInfoCtx instanceof StartedProInsCtx) {
				StartedProInsCtx startProInsCtx = (StartedProInsCtx) flowInfoCtx;
				String taskPk = startProInsCtx.getTaskPk();
				if (taskPk != null)
					WfCacheManager.getSessionCache().remove(taskPk);
			}
		}
	}
}
