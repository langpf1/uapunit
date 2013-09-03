package uap.workflow.ui.workitem;

import uap.workflow.vo.WorkflownoteVO;
import nc.vo.pub.BusinessException;

public interface IApplicationRuntimeAdjust {
	/** 应用校正方式*/
	public static int ADJUST_TYPE_ADDASSIGN = 1;//加签
	public static int ADJUST_TYPE_JUMP=2;//跳转
	
	/** 加签方式*/
	public static int ADDASSIGN_STYLE_SERIAL = 1;//串行
	public static int ADDASSIGN_STYLE_PARALLEL = 2;//并行
	public static int ADDASSIGN_STYLE_COOPERATION = 3;//协作
	public static int ADDASSIGN_STYLE_NOTICE = 4;//知会
	
	public WorkflownoteVO adjust(ApplicationRuntimeAdjustContext context) throws BusinessException;
}
