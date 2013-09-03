package uap.workflow.app.taskhandling;


/** 
   通知实现类
 * @author 
 */
public class TaskHandlingDefinition implements ITaskHandlingDefinition{

	ITaskHandlingType taskHandleType;

	public ITaskHandlingType getTaskHandleType() {
		return taskHandleType;
	}

	public void setTaskHandleType(ITaskHandlingType taskHandleType) {
		this.taskHandleType = taskHandleType;
	}
}