package uap.workflow.restlet.resources;
import java.util.HashMap;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.data.Form;

import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.app.impl.WorkflowMachineImpl;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;
public class FormResource extends ServerResource {
	private static int code = 0;
	private String actioncode = null;
	private Logger logger = Logger.getLogger(this.getClass());
	private WorkflowMachineImpl workflowmachine = new WorkflowMachineImpl();
	/* 为了测试构造的一张单据 */
	private AggregatedValueObject billVO = null;//单据vo
	private WFAppParameter paraVo = new WFAppParameter() ;
	private WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
	@Override
	/*初始化的动作*/
	public void doInit(){
		this.actioncode = (String)getRequest().getAttributes().get("ActionCode");
	}
	
	@Post
	/**
	 * 模拟表单的提交动作，单据的number：20130416TEST 类型：10KH  启动的流程名字：44444  pk是：0001AA10000000024N9F
	 * 结果：在数据库中产生一条待办的任务
	 * */
	public JSONObject Form(Representation entity) throws BusinessException {	
		try {
			String actionCode = "SAVE0001AA1000000001AAOO";
			String billtype = "10KH";
			paraVo.setActionName(actionCode);
			IBusinessKey businessObject = new BizObjectImpl();
			businessObject.setBillId("201304TESTFFFAAA"+code);
			businessObject.setBillNo("20130416"+code);
			businessObject.setBillType("10KH");
			paraVo.setBusinessObject(businessObject);
			paraVo.setGroupPK("00012410000000000H12");
			paraVo.setProcessDefPK("0001AA10000000024N9F");
			paraVo.setOperator("0001AA1000000001AAOO");
			JSONObject returnvalue = new JSONObject();
			/*单据处理：流程交互的处理，即是找到单据的匹配流程和构造匹配流程的初始化的任务*/
			WorkflownoteVO workflownotevo = null;
			HashMap eParam = null;//ePram 为扩展参数
			if (PfUtilBaseTools.isSaveAction(actionCode, billtype) 
					|| PfUtilBaseTools.isStartAction(actionCode, billtype)){
				//现在的单据还没有完成，如果完成了，亦可直接的构造使用checkWorkflow
//					workflownotevo = workflowmachine.checkWorkFlow(actionCode, billtype, billVO, eParam);
			//单据没有完成的情况下，利用单据中携带的成员数据来进行驱动
				workflownotevo = workflowmachine.webDesigercheckWorkFlow(paraVo, eParam);
			}
			workflownotevo.setApproveresult("Y");//审批结果
			/*单据动作处理*/
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			HashMap hmPfExParams = null;
			wfEngineService.startWorkflow(paraVo, hmPfExParams );	
			returnvalue.put("result", "单据已经提交成功");
			returnvalue.put("taskId","12121255023EEEE");
			this.code++;
			return returnvalue;
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		return null;
		
	
		
	}

	private WorkflownoteVO WorkflowInteraction(Form parentForm, String actionCode, String billtype,Form form, HashMap eParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Get
	/*根据 TaskKind 得到不同类型的task 得到单据的信息*/
	public  JSONArray getrr() throws ResourceException {
		return null; 
		
	    
	} 
}
