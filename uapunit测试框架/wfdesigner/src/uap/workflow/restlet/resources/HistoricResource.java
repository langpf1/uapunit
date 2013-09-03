package uap.workflow.restlet.resources;

import java.io.File;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import nc.bs.framework.common.RuntimeEnv;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.parser.ProcessDefinitionsManager;
import uap.workflow.engine.bpmn.diagram.ProcessDiagramGenerator;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.impl.WorkflowInstanceQry;
import uap.workflow.engine.io.IoUtil;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.ActivityInstanceVO;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.reslet.application.receiveData.CreatHistoricActivityData;
import uap.workflow.reslet.application.receiveData.HistoricActivity;
import uap.workflow.reslet.application.receiveData.History;
import uap.workflow.restlet.application.RuntimeConstants;
public class HistoricResource extends ServerResource {
	private String taskInstanceID ;
	private Logger logger = Logger.getLogger(this.getClass());

	@Get
	public  JSONObject gethistory(){
		JSONObject historicTaskJson = null;
		try {
			taskInstanceID = getQuery().getFirstValue(RuntimeConstants.ActivityInstanceID.getStringValue());
			WorkflowInstanceQry wfinsqry = new WorkflowInstanceQry() ;
			//查找活动实例 
			ActivityInstanceVO activityins = wfinsqry.getActInsVoByPk(taskInstanceID);
			//查找流程实例下面的活动实例（ 按照审批时间进行排序）
			List<TaskInstanceVO> task = TaskUtil.getTaskByProcessInstancePk(activityins.getPk_proins());
			CreatHistoricActivityData creatHistoricTaskjson = new CreatHistoricActivityData();
			History  history = new History();
			List<HistoricActivity>  historicTaskList = new ArrayList<HistoricActivity>();
			if(!task.isEmpty()){
				historicTaskList = creatHistoricTaskjson.creatHistoricTask(task,historicTaskList);
				history.setHistoricActivities(historicTaskList);
				logger.info("得到"+taskInstanceID+"所在流程实例的历史记录");
				Gson gson = new Gson();
				historicTaskJson = new JSONObject(gson.toJson(history));
			}
			
//			//得到流程实例的图片
//			IProcessDefinition prodef = ProcessDefinitionUtil.getProDefByProDefPk(activityins.getPk_prodef());
//			byte[] diagramBytes = IoUtil.readInputStream(ProcessDiagramGenerator.generatePngDiagram(prodef), null);
//			
//			String filepath = RuntimeEnv.getInstance().getNCHome();
//			filepath = filepath + "\\" + "workflow";
//			File floder = new File(filepath);
//			if (!floder.exists()) {
//				floder.mkdir();
//			}
//			String fileName = UUID.randomUUID().toString() + ".png";
//			String fullFileName = filepath + "\\" + fileName;
//			File file = new File(fullFileName);
//		    // 文件存在退出
//			if (file.exists()) {
//				floder.mkdir();
//		    }
//			// wirte bytes to file
//		   FileUtils.writeByteArrayToFile(file, diagramBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return historicTaskJson;
	
	}
}