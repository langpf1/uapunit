package uap.workflow.designer.exports;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.engine.itf.IDeployService;
public class BPMN20ExportMarshaller implements ActivityNamespaceConstants {
	private Bpmn2MemoryModel model;
	private String modelFileName;
	public void marshallDiagram(Bpmn2MemoryModel model, String modelFileName, boolean isPureModel, boolean isDraft) {
		this.model = model;
		// this.modelFileName = "C:\\MyProcess.bpmn";
		// marshallBPMNDiagram();
		String filepath = RuntimeEnv.getInstance().getNCHome();
		filepath = filepath + "\\" + "workflow";
		File floder = new File(filepath);
		if (!floder.exists()) {
			floder.mkdir();
		}
		String fileName = UUID.randomUUID().toString() + ".bpmn20.xml";
		String fullFileName = filepath + "\\" + fileName;
		// this.modelFileName = "C:\\MyProcess_1.bpmn";
		this.modelFileName = fullFileName;
		saveBPMNDiagram(isPureModel);
		NCLocator.getInstance().lookup(IDeployService.class).deploy(fullFileName, fileName, isDraft);
		if(modelFileName == null)
		{
			modelFileName = this.modelFileName;
		}
		File file1 = new File(modelFileName);
		if (file1.exists()) {
			file1.delete();
		}
		// //BizProcessServer.getInstance().start();
		// WfmServiceFacility.getRepositoryService();
		// //NCLocator.getInstance().lookup(IProcess)
		// RepositoryService reposService =
		// BizProcessServer.processEngine.getRepositoryService();
		// DeploymentBuilder deployBuild = reposService.createDeployment();
		// File file = new File(fullFileName);
		// FileInputStream fileInput = null;
		// try {
		// fileInput = new FileInputStream(file);
		// deployBuild.addInputStream(fileName, fileInput);
		// deployBuild.deploy();
		// } catch (FileNotFoundException e) {
		// WorkflowLogger.error(e.getMessage(), e);
		// } finally {
		// try {
		// fileInput.close();
		// } catch (IOException e) {
		// WorkflowLogger.error(e.getMessage(), e);
		// }
		// }
	}
	private void saveBPMNDiagram(boolean isPureModel) {
		try {
			String processDefinitions = ProcessDefinitionsManager.toXml(model, isPureModel);
			ChangeProcessDefinitionXml changeXml=new ChangeProcessDefinitionXml();
			processDefinitions= changeXml.changeXml(processDefinitions);
			File objectsFile = new File(this.modelFileName);
			FileOutputStream fos = new FileOutputStream(objectsFile);
			ByteArrayOutputStream arrayStream = new ByteArrayOutputStream();
			OutputStreamWriter out = new OutputStreamWriter(arrayStream, "UTF-8");
			out.write(processDefinitions);
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
	public String provideBaseProcess()
	{
		Bpmn2MemoryModel model=CreateBaseProcess.provideBaseProcess("example", "example process", "example");
		marshallDiagram(model,"",false,false);
		return model.getProcesses().get(0).getId();
	}
}