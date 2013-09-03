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
import uap.workflow.engine.context.AddSignUserInfoCtx;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.Logic;
import uap.workflow.reslet.application.receiveData.AddSignTask;
import uap.workflow.reslet.application.receiveData.AddsignType;
import uap.workflow.reslet.application.receiveData.Participant;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import com.google.gson.Gson;
public class AddSignTaskResource extends ServerResource{

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
			AddSignTask task =  new AddSignTask();
			task =gson.fromJson(json, AddSignTask.class);
			
			WorkflownoteVO workflownotevo = null;
			//����workflownotevo
			workflownotevo = wfEngineService.getWorkitem(task.getTaskID());
			workflownotevo.setChecknote(task.getComment());//����
			//���������ڳ���,�ѳ��ͷ�ʽ�ͳ��͸���reny
			if(task.getCc()!= null){
			//TODO	
			}
			workflownotevo.setApproveresult("Y");//�����Ľ��
			AddSignUserInfoCtx[] addSignUsers = creatAddSignUsers(task);
			if(task.getAddsignType() == AddsignType.before){ 
				paraVo.setActionName("beforeaddsign");
				
				//����ǰ��ǩ��Ϣ
				
				CreateBeforeAddSignCtx beforeAddSignCtx = new CreateBeforeAddSignCtx();
				beforeAddSignCtx.setAddSignUsers(addSignUsers);
				beforeAddSignCtx.setTaskPk(task.getTaskID());
				beforeAddSignCtx.setComment(task.getComment());
				beforeAddSignCtx.setLogic(Logic.Sequence);
				workflownotevo.setBeforeAddSignCtx(beforeAddSignCtx);
				
				wfEngineService.beforeAddSign(workflownotevo);			
			}else{
				paraVo.setActionName("afteraddsign");
				
				//������ǩ����Ϣ
				CreateAfterAddSignCtx afterAddSignCtx = new CreateAfterAddSignCtx();
				addSignUsers = creatAddSignUsers(task);
				afterAddSignCtx.setTaskPk(task.getTaskID());
				afterAddSignCtx.setComment(task.getComment());
				afterAddSignCtx.setLogic(Logic.Sequence);
				afterAddSignCtx.setAddSignUsers(addSignUsers);
				workflownotevo.setAfterAddSignCtx(afterAddSignCtx);

				wfEngineService.afterAddSign(workflownotevo);
			}
			taskJson = new JSONObject();
			taskJson.put("returnvalue", "ǰ��ǩ�ɹ�");
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

	private AddSignUserInfoCtx[] creatAddSignUsers(AddSignTask task) {
		AddSignUserInfoCtx[] addSignUsers;
		addSignUsers = new AddSignUserInfoCtx[task.getParticipants().size()];
		for(int i = 0;i< task.getParticipants().size();i++){
			AddSignUserInfoCtx addsignuser = new AddSignUserInfoCtx();
			addsignuser.setUserPk(task.getParticipants().get(i).getParticipantID());
			addSignUsers[i] = addsignuser;
		}
		return addSignUsers;
	}
}
