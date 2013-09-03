package uap.workflow.test;

import java.io.IOException;
import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.vo.pub.BusinessException;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.app.impl.WorkflowMachineImpl;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

import com.google.gson.Gson;

/**
 * 
 * @author kongml
 * 
 */
public class CustomerResource extends ServerResource {

	@Post
	/**
	 * 客户申请单服务接口
	 * */
	public JSONObject Form(Representation entity) throws IOException, DAOException {
		Gson gson = new Gson();
		JSONObject result = new JSONObject();
		JsonRepresentation jsonRepresention = new JsonRepresentation(entity);
		String json = jsonRepresention.getText();
		CustomerBean customer = gson.fromJson(json, CustomerBean.class);
		if (customer.getActioncode().equalsIgnoreCase("create")) {

			if (customer != null) {
				String info = new CustomerDao().addBill(customer.toVO());
//				result.put("message", info);
			}
		} else if (customer.getActioncode().equalsIgnoreCase("update")
				|| customer.getActioncode().equalsIgnoreCase("approve")) {
			WorkflowMachineImpl workflowmachine = new WorkflowMachineImpl();
			WFAppParameter paraVo = new WFAppParameter();
			WFEngineServiceImpl wfEngineService = new WFEngineServiceImpl();

			String actionCode = "SAVE0001AA1000000001AAOO";
			String billtype = "10KH";
			paraVo.setActionName(actionCode);
			IBusinessKey businessObject = new BizObjectImpl();
			businessObject.setBillId("201304TESTFFFAAA");
			businessObject.setBillNo("20130416");
			businessObject.setBillType("10KH");
			paraVo.setBusinessObject(businessObject);
			paraVo.setGroupPK("00012410000000000H12");
			paraVo.setOperator("0001AA1000000001AAOO");
			JSONObject returnvalue = new JSONObject();
			/* 单据处理：流程交互的处理，即是找到单据的匹配流程和构造匹配流程的初始化的任务 */
			WorkflownoteVO workflownotevo = null;
			HashMap eParam = null;// ePram 为扩展参数
			if (PfUtilBaseTools.isSaveAction(actionCode, billtype)
					|| PfUtilBaseTools.isStartAction(actionCode, billtype)) {
				try {
					workflownotevo = workflowmachine.webDesigercheckWorkFlow(paraVo, eParam);
				} catch (PFBusinessException e) {
					e.printStackTrace();
				}
			}
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			HashMap hmPfExParams = null;
			try {
				wfEngineService.startWorkflow(paraVo, hmPfExParams);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
//			returnvalue.put("result", "单据已经提交成功");
//			returnvalue.put("taskId", "12121255023EEEE");
			return returnvalue;
			
			
			
			
		}
			
			
			
			
			
			
			
			if (customer != null) {
				int info = new CustomerDao().updateBill(customer.toVO());
//				result.put("message", info);
			}
		
		return result;
	}
}
