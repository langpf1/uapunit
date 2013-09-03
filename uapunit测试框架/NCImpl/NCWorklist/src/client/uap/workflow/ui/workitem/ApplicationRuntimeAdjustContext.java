package uap.workflow.ui.workitem;

import uap.workflow.vo.WorkflownoteVO;

/**
 * 应用运行期校正环境
 * @author 
 *
 */
public class ApplicationRuntimeAdjustContext implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	private WorkflownoteVO m_workflow;
	private int style;//校正方式
	private Object userObject;//对象
	
	public WorkflownoteVO getWorkFlow() {
		return m_workflow;
	}
	public void setWorkFlow(WorkflownoteVO flow) {
		m_workflow = flow;
	}
	
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public Object getUserObject() {
		return userObject;
	}
	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}
}
