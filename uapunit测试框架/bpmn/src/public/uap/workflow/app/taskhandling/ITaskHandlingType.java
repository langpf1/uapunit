package uap.workflow.app.taskhandling;
/**
 * 工作任务处理类型接口
 * 设计期时的工作任务处理类型接口，具体业务从此接口实现自己的具体工作任务处理类型类
 * 
 * @author
 */
public interface ITaskHandlingType {
	// /不支持修改
	// /工作任务处理类型代码
	public String getCode();
	public void setCode(String code);
	// /多语的工作任务处理类型名称
	public String getName();
	public void setName(String name);
}