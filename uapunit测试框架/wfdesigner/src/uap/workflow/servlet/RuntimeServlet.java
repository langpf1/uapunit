package uap.workflow.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

import org.apache.coyote.ActionCode;

import uap.workflow.action.IAction;
import uap.workflow.app.impl.WFEngineServiceImpl;
import uap.workflow.app.impl.WorkflowMachineImpl;
import uap.workflow.controller.ActionControllerFactory;
import uap.workflow.handler.IHandler;
import uap.workflow.parameter.IParameter;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

/**
  * servlet总控制类
  */
public class RuntimeServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4442397463551836919L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			process(request, response);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		try {
			process(request, response);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void process(HttpServletRequest request,
			HttpServletResponse response) throws BusinessException{
		String actionCode = request.getParameter(IParameter.ACTION);
		if(actionCode == null || actionCode.equals(""))
			return;
		//单据，怎么作为一个变量传过来Object billVO = parameter.getRequest().getAttribute("billVo");
		//得到task以及其他的一些数据
		String actioncode="APPROVE0001AA1000000001AAOO",  billType = "10KH" ,pk_task = "0001AA10000000024Y5D";	
		WFAppParameter paraVo = new WFAppParameter() ;
		HashMap hmPfExParams = null;
		WorkflowMachineImpl  workflowMachine  = new  WorkflowMachineImpl();
		WFEngineServiceImpl  wfEngineService  = new  WFEngineServiceImpl();
		try {
			//交互处理，把处理的意见
			
			WorkflownoteVO workflownotevo = null;//workflowMachine.checkWorkFlow(actioncode, billType, (AggregatedValueObject)billVO, hmPfExParams);
			//构造workflowNoteVO
			workflownotevo = wfEngineService.getWorkitem(pk_task);
			workflownotevo.setApproveresult("Y");//批准
			workflownotevo.setChecknote("从web端审批");//批语
			//构造paraVo
			paraVo.setWorkFlow(workflownotevo);
			paraVo.setProcessDefPK(workflownotevo.getTaskInstanceVO().getPk_process_def());
			paraVo.setActionName(actioncode);
			paraVo.setAutoApproveAfterCommit(true);//传过来的数据
			paraVo.setOperator(workflownotevo.getTaskInstanceVO().getPk_owner());
		} catch (BusinessException e) {
			e.printStackTrace();
		}	
		if(actionCode.equalsIgnoreCase("approve")){
			wfEngineService.signalWorkflow(paraVo);
		}else if(actionCode.equalsIgnoreCase("assign")){
			List<String> turnUserPks = null;
			String currentUserId = null;
			wfEngineService.delegateTask(paraVo.getWorkFlow(), turnUserPks, currentUserId);
		}else if(actionCode.equalsIgnoreCase("reject")){
			wfEngineService.rollbackWorkflow(paraVo);
		}else if(actionCode.equalsIgnoreCase("beforeaddsign")){
			wfEngineService.beforeAddSign(paraVo.getWorkFlow());
		}else if(actionCode.equalsIgnoreCase("afteraddsign")){
			wfEngineService.afterAddSign(paraVo.getWorkFlow());
		}
//		IHandler handler = ActionControllerFactory.getInstance().getHandler(actionCode);
//		IParameter parameter = handler.handle(request);
//		parameter.setResponse(response);
//		parameter.setParameter(IParameter.ACTION, actionCode);
//		
//		IAction action = ActionControllerFactory.getInstance().getAction(actionCode);
//		action.perform(parameter);
		/*java 对象和json之间的转换，可以利用json-lib来完成，测试阶段，利用手写，以便修改数据     宅*/
		try {
			PrintWriter out = response.getWriter();
			StringWriter w = new StringWriter();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < paraVo.getWorkFlow().getNewTasks().length; i++) {
				if (buffer.length() > 1)
					buffer.append(",\n");
				buffer.append("{'id':'");
				buffer.append(i);
				buffer.append("','taskpk':'");//发起人
				buffer.append(paraVo.getWorkFlow().getNewTasks()[i].getTaskPk());
				buffer.append("','taskoperator':'");//开始时间
				buffer.append(paraVo.getWorkFlow().getNewTasks()[i].getExecuter());
				buffer.append("'}");
			}
			w.append("[\n");
			w.append(buffer.toString());
			w.append("\n]");
			out.print("{'total':"+paraVo.getWorkFlow().getNewTasks().length+",'rows':"+w.toString()+"}");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

