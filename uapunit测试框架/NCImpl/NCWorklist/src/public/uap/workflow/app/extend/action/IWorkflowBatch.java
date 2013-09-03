package uap.workflow.app.extend.action;

import java.util.Hashtable;

/**
 * 批动作处理脚本 的返回值
 * 
 * @author 樊冠军 2002-4-16
 */
public interface IWorkflowBatch {
	/**
	 * 返回未通过或进行中的单据索引号
	 */
	Hashtable getNoPassAndGoing();

	/**
	 * 获得用户对象
	 */
	Object getUserObj();
}
