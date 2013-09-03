package uap.workflow.restlet.resources;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.reslet.application.receiveData.ReSignTask;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import com.google.gson.Gson;
public class ReAssignTaskResource extends ServerResource{

	private WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
	private WFAppParameter paraVo = new WFAppParameter() ;

	@Post
	public JSONObject handelTask(Representation entity){	
		JSONObject taskJson = null;
		try {
			CharacterSet characterSet = new CharacterSet("utf-8");
			entity.setCharacterSet(characterSet);
			JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
			Gson gson = new Gson();
			String json = jsonRepresention.getText();
			ReSignTask task =  new ReSignTask();
			task =gson.fromJson(json, ReSignTask.class);
			
			WorkflownoteVO workflownotevo = null;
			//����workflownotevo
			workflownotevo = wfEngineService.getWorkitem(task.getTaskID());
			workflownotevo.setChecknote(task.getComment());//����
			//���������ڳ���,�ѳ��ͷ�ʽ�ͳ��͸���reny
			if(task.getCc()!= null){
			//TODO	
			}
			//����paravo
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			workflownotevo.setApproveresult("Y");//�����Ľ��
			paraVo.setActionName("����");
			List<String> turnUserPks = new ArrayList<String>();
			for(Participant participant:task.getReassign()){
				turnUserPks.add(participant.getParticipantID());
			}
			wfEngineService.delegateTask(workflownotevo, turnUserPks, workflownotevo.getTaskInstanceVO().getPk_owner());			
			taskJson = new JSONObject();
			taskJson.put("returnvalue", "���ɳɹ�");
//			if(paraVo.getWorkFlow().getNewTasks().length > 0){
//				List<TaskInstanceVO> nextTask = new ArrayList<TaskInstanceVO>();
//				nextTask = wfEngineService.getTasks(TaskInstanceStatus.End.getIntValue(), "User Task", null, "0001AA1000000001AAOO", " and process_def_name = '44444'", true);  
//				taskJson = new JSONArray(gson.toJson(nextTask));
//			 }		
		  } catch (Exception e) {
			e.printStackTrace();
		  }
		return taskJson; 		
	}
}
