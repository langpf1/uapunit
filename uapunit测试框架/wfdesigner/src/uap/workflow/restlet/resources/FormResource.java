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
	/* Ϊ�˲��Թ����һ�ŵ��� */
	private AggregatedValueObject billVO = null;//����vo
	private WFAppParameter paraVo = new WFAppParameter() ;
	private WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
	@Override
	/*��ʼ���Ķ���*/
	public void doInit(){
		this.actioncode = (String)getRequest().getAttributes().get("ActionCode");
	}
	
	@Post
	/**
	 * ģ������ύ���������ݵ�number��20130416TEST ���ͣ�10KH  �������������֣�44444  pk�ǣ�0001AA10000000024N9F
	 * ����������ݿ��в���һ�����������
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
			/*���ݴ������̽����Ĵ��������ҵ����ݵ�ƥ�����̺͹���ƥ�����̵ĳ�ʼ��������*/
			WorkflownoteVO workflownotevo = null;
			HashMap eParam = null;//ePram Ϊ��չ����
			if (PfUtilBaseTools.isSaveAction(actionCode, billtype) 
					|| PfUtilBaseTools.isStartAction(actionCode, billtype)){
				//���ڵĵ��ݻ�û����ɣ��������ˣ����ֱ�ӵĹ���ʹ��checkWorkflow
//					workflownotevo = workflowmachine.checkWorkFlow(actionCode, billtype, billVO, eParam);
			//����û����ɵ�����£����õ�����Я���ĳ�Ա��������������
				workflownotevo = workflowmachine.webDesigercheckWorkFlow(paraVo, eParam);
			}
			workflownotevo.setApproveresult("Y");//�������
			/*���ݶ�������*/
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
			HashMap hmPfExParams = null;
			wfEngineService.startWorkflow(paraVo, hmPfExParams );	
			returnvalue.put("result", "�����Ѿ��ύ�ɹ�");
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
	/*���� TaskKind �õ���ͬ���͵�task �õ����ݵ���Ϣ*/
	public  JSONArray getrr() throws ResourceException {
		return null; 
		
	    
	} 
}
