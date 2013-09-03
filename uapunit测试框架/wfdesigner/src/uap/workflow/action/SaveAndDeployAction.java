package uap.workflow.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ISecurityTokenCallback;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.deploy.DeploymentCache;
import uap.workflow.engine.itf.IDeployService;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.parameter.IParameter;
import uap.workflow.servlet.Constants;


public class SaveAndDeployAction implements IAction
{
	public void perform(IParameter parameter) {
		HttpServletRequest request = parameter.getRequest();
		HttpServletResponse response = parameter.getResponse();

		if (request.getContentLength() < Constants.MAX_REQUEST_SIZE)
		{
			String xml = request.getParameter("xml");

			if (xml != null && xml.length() > 0)
			{
				ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
				sc.token("LGW".getBytes(),"ncc10".getBytes());
				marshallDiagram(xml);
				String processId = request.getParameter("id");
				DeploymentCache deploymentCache = BizProcessServer.getProcessEngineConfig().getDeploymentCache();
				IProcessDefinition newProcessDefinition  = deploymentCache.getProDefByProDefId(processId);
				newProcessDefinition.setPk_group("00012410000000000H12");
				((uap.workflow.engine.entity.ProcessDefinitionEntity)newProcessDefinition).asyn();
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		}
	}

	public void marshallDiagram(String xml) {
		String filepath = RuntimeEnv.getInstance().getNCHome();
		filepath = filepath + "\\" + "workflow";
		File floder = new File(filepath);
		if (!floder.exists()) {
			floder.mkdir();
		}
		String fileName = UUID.randomUUID().toString() + ".bpmn20.xml";
		String fullFileName = filepath + "\\" + fileName;
		String modelFileName = fullFileName;
		saveBPMNDiagram(xml, modelFileName);
		NCLocator.getInstance().lookup(IDeployService.class).deploy(fullFileName, fileName, false);
		/*
		File file1 = new File(modelFileName);
		if (file1.exists()) {
			file1.delete();
		}
		*/
	}
	
	private void saveBPMNDiagram(String xml, String fileName) {
		try {
			File objectsFile = new File(fileName);
			FileOutputStream fos = new FileOutputStream(objectsFile);
			ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
			OutputStreamWriter out = new OutputStreamWriter(arrayStream, "UTF-8");
			out.write(xml);
			out.flush();
			arrayStream.writeTo(fos);
			arrayStream.flush();
			fos.close();
			out.close();
			arrayStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}

