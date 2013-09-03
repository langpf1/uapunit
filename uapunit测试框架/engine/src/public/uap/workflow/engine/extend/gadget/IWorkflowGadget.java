package uap.workflow.engine.extend.gadget;

import nc.vo.pub.BusinessException;

/**
 * 工作流组件接口（无状态）
 * <li>由业务代码的Private类实现，并注册到pub_workflowgadget表中
 * 
 * @author leijun 2008-8
 * @since 5.5
 * @modifier leijun 2009-6 废弃工作流交互UI
 * @modifier leijun 2009-12 增加前置处理及其回退调用方法
 */
public interface IWorkflowGadget {
	/**
	 * 活动完成时，执行工作流组件的业务处理
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object doAfterRunned(IGadgetContext gc);

	/**
	 * 活动废弃时，将工作流组件的业务处理进行回退
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoAfterRunned(IGadgetContext gc);

	/**
	 * 活动启动时，执行工作流组件的前置处理
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object doBeforeActive(IGadgetContext gc);

	/**
	 * 活动废弃时，将工作流组件的前置处理进行回退
	 * @param gc
	 * @return
	 * @throws BusinessException
	 */
	Object undoBeforeActive(IGadgetContext gc);
}
