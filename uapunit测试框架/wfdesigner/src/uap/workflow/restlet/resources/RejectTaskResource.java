package uap.workflow.restlet.resources;

import java.util.List;

import org.json.JSONObject;
import org.restlet.data.CharacterSet;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.reslet.application.receiveData.ActionCode;
import uap.workflow.reslet.application.receiveData.RejectTask;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import com.google.gson.Gson;
public class RejectTaskResource extends ServerResource{

	private WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
	private WFAppParameter paraVo = new WFAppParameter() ;

	@Post
	public JSONObject handelTask(Representation entity){	
		JSONObject taskJson = new JSONObject();
		try {
			CharacterSet characterSet = new CharacterSet("utf-8");
			entity.setCharacterSet(characterSet);
			JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
			Gson gson = new Gson();
			String json = jsonRepresention.getText();
			RejectTask task = new RejectTask();
			task = gson.fromJson(json, RejectTask.class);
			IBusinessKey businessObject = new BizObjectImpl();
			businessObject.setBillId(task.getPk_form_ins_version());
			businessObject.setBillType(task.getPk_bizobject());
			WorkflownoteVO workflownotevo = null;
			// 构造workflownotevo
			//使用驳回节点的id来构造workflownodevo 
			workflownotevo = wfEngineService.getWorkitem(task.getTaskID());
			workflownotevo.setChecknote(task.getComment());// 批语
			// 如果任务存在抄送, 抄送方式和抄送给的人员
			if (task.getCc() != null) {
//TODO
			}
			// 构造paravo
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			// 为了测试
			paraVo.setBusinessObject(businessObject);
			if (task.getActioncode() == ActionCode.REJECT.getIntValue()) {
				RejectTaskInsCtx backwardInfo = new RejectTaskInsCtx();
				backwardInfo.setComment(task.getComment());
				//这里的taskid 并不是驳回节点的taskid，而是当前节点的taskid
				backwardInfo.setTaskPk(task.getTaskID());
				UserTaskRunTimeCtx rejectInfo = new UserTaskRunTimeCtx();
				rejectInfo.setActivityId(task.getRejectNodeID());
				rejectInfo.setUserPks( new String[]{workflownotevo.getTaskInstanceVO().getPk_owner()});
				backwardInfo.setRejectInfo(rejectInfo);
				workflownotevo.setBackwardInfo(backwardInfo);
				workflownotevo.setApproveresult("R");// 审批的结果
				paraVo.setActionName("Reject");
				wfEngineService.signalWorkflow(paraVo);
			}
			taskJson.put("returnvalue", "驳回成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskJson; 		
	}
}

