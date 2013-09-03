package uap.workflow.engine.extend.gadget;

import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.ActivityInstanceStatus;


/**
 * 工作流组件的运行上下文 
 * 
 * @author leijun 2008-8
 * @since 5.5
 */
public interface IGadgetContext
{
	public ProcessInstanceStatus getProcessStatus();

	public void setProcessStatus(ProcessInstanceStatus processStatus);

	public ActivityInstanceStatus getTaskStatus();

	public void setTaskStatus(ActivityInstanceStatus taskStatus);
	
	public Object getBillEntity();

	public void setBillEntity(Object billEntity);

	public void setGadgetVO(Object gadgetVO);

	public Object getGadgetVO();

	public boolean isAutoActivity();

	public void setAutoActivity(boolean isAutoActivity);

}
