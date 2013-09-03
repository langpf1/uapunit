package uap.workflow.engine.command;

import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.dftimpl.FlowResponse;
import uap.workflow.engine.service.RuntimeService;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.session.WorkflowContext;
/**
 * 创建流程实例命令
 * 
 * @author tianchw
 * 
 */
public class CreateProInsCmd extends AbstractCommand implements ICommand<Void> {
	public CreateProInsCmd() {
		super();
	}
	public Void execute() {
		String proDefPk = WorkflowContext.getCurrentBpmnSession().getProDefPk();
		String proDefId = WorkflowContext.getCurrentBpmnSession().getProDefId();
		RuntimeService runTimeService = WfmServiceFacility.getRunTimeServie();
		IProcessInstance processInstance=null;
		if (proDefPk == null || proDefPk.length() == 0) {
			processInstance = runTimeService.startProcessInstanceById(proDefId);
		} else {
			processInstance = runTimeService.startProcessInstanceByKey(proDefPk);
		}
		FlowResponse response = (FlowResponse) WorkflowContext.getCurrentBpmnSession().getResponse();
		if (response != null && processInstance !=null) {
			response.setProcessInstance(processInstance);
		}		
		return null;
	}
}
