package uap.workflow.restlet.resources;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.reslet.application.receiveData.ActionCode;
import uap.workflow.reslet.application.receiveData.ApproveTask;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.restlet.application.Pagination;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import com.google.gson.Gson;
public class ApproveResource extends ServerResource{

	private WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
	private WFAppParameter paraVo = new WFAppParameter() ;

	@Post
	public JSONArray handelTask(Representation entity){	
		JSONArray taskJson = null;
		try {
			CharacterSet characterSet = new CharacterSet("utf-8");
			entity.setCharacterSet(characterSet);
			JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
			Gson gson = new Gson();
			String json = jsonRepresention.getText();
			ApproveTask task =  new ApproveTask();
			task =gson.fromJson(json, ApproveTask.class);
			
			WorkflownoteVO workflownotevo = null;
			//构造workflownotevo
			workflownotevo = wfEngineService.getWorkitem(task.getTaskID());
			workflownotevo.setChecknote(task.getComment());//批语
			//如果任务存在抄送,把抄送方式和抄送给的reny
			if(task.getCc()!= null){
				
			}
			//如果任务需要指派
			if(task.getAssign().getParticipants()!= null && !task.getAssign().getParticipants().isEmpty()){
				List <String> participantIDlist = new ArrayList<String>();
				for(Participant participant :task.getAssign().getParticipants()){
					participantIDlist.add(participant.getParticipantID());
				}
				workflownotevo.getAssignInfoMap().put(workflownotevo.getTaskInstanceVO().getActivity_id(), participantIDlist);
				workflownotevo.setAddSign(true);
			}
			//构造paravo
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			if(task.getActioncode() == ActionCode.APPROVE.getIntValue()){
				workflownotevo.setApproveresult("Y");//审批的结果
				paraVo.setActionName("approve");
				wfEngineService.signalWorkflow(paraVo);
			}
			
//			if(paraVo.getWorkFlow().getNewTasks().length > 0){
//				List<TaskInstanceVO> nextTask = new ArrayList<TaskInstanceVO>();
//				Pagination page  = new Pagination();
//				page.setPageNumber(1);
//				page.setPageSize(10);
//				nextTask = wfEngineService.getTasks(TaskInstanceStatus.End.getIntValue(), "User Task", null, "0001AA1000000001AAOO", " and process_def_name = '44444'", true,page);  
//				taskJson = new JSONArray(gson.toJson(nextTask));
//			 }		
		  } catch (Exception e) {
			e.printStackTrace();
		  }
		return taskJson; 		
	}
}
