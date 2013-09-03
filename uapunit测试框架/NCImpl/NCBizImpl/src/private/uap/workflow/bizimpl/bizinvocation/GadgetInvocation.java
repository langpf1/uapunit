package uap.workflow.bizimpl.bizinvocation;

import nc.bs.logging.Logger;
import nc.bs.wfengine.engine.ActivityInstance;
import nc.vo.ml.NCLangRes4VoTransl;


import uap.workflow.bizimpl.BizContext;
import uap.workflow.bizitf.exception.BizException;
import uap.workflow.engine.core.ActivityInstanceStatus;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.entity.ProcessInstanceEntity;
import uap.workflow.engine.extend.gadget.IGadgetContext;
import uap.workflow.engine.extend.gadget.IWorkflowGadget;

public class GadgetInvocation {

	private IWorkflowGadget gadget;
	private IActivityInstance execution;
	public GadgetInvocation(){
		
	}
	
	public GadgetInvocation(IWorkflowGadget gadget, IActivityInstance execution){
		this.execution = (IActivityInstance)execution;
		this.gadget = gadget;
	}
	
	public Object handlerInvocation(){
		
		BizContext bizContext = (BizContext)execution.getVariable("bizContext");
		
		ProcessInstanceStatus procStatus = ((ProcessInstanceEntity)execution.getProcessInstance()).getState_proins();
		ActivityInstanceStatus activityStatus = execution.getStatus();
		IGadgetContext gadgetContext = new GadgetContext(procStatus, activityStatus, bizContext.getBizEntity(), null);
		
		if (activityStatus.equals(ProcessInstanceStatus.Inefficient)){//TODO ���ػ�����ʱ����
			return undoGadget(gadgetContext, procStatus.equals(ProcessInstanceStatus.Finished));	
		}else
			return doGadget(gadgetContext, activityStatus.equals(ActivityInstanceStatus.Wait));
	}
	
	private Object doGadget(IGadgetContext gadgetContext, boolean isBefore) throws BizException {
		
		try {
			if (gadget instanceof IWorkflowGadget) {
				if (isBefore)
					return gadget.doBeforeActive(gadgetContext);	// ִ�й����������ǰ�ô��� 
				else
					return gadget.doAfterRunned(gadgetContext);	// ִ�й��������
			} else
				throw new BizException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "EngineService-0002", null, new String[]{(String)gadget.getClass().getName()})/*ע��Ĺ��������û��ʵ��IWorkflowGadget�ӿ�={0}*/);
		} catch (BizException e) {
			Logger.error("ִ�й�������������쳣��" + e.getMessage(), e);
			throw new BizException("ִ�й�������������쳣��" + e.getMessage(), e);
		}
	}

	public Object undoGadget(IGadgetContext gadgetContext, boolean isRunned) {
		Logger.debug("�����ʱ�������䵥�����=EngineService.undoGadget() called");

		// ʵ���������������ִ�л��˷���
		try {
			if (gadget instanceof IWorkflowGadget) {
				if (isRunned) {
					gadget.undoAfterRunned(gadgetContext);
					return gadget.undoBeforeActive(gadgetContext);
				} else {
					return gadget.undoBeforeActive(gadgetContext);
				}
			} else
				throw new BizException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "EngineService-0002", null, new String[]{(String)gadget.getClass().getName()})/*ע��Ĺ��������û��ʵ��IWorkflowGadget�ӿ�={0}*/);
		} catch (BizException e) {
			Logger.error(e.getMessage(), e);
			throw new BizException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "EngineService-0003", null, new String[]{e.getMessage()})/*���˹�������������쳣��{0}*/, e);
		}
	}


}
