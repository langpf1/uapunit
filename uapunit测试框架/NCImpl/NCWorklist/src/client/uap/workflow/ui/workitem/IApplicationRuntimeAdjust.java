package uap.workflow.ui.workitem;

import uap.workflow.vo.WorkflownoteVO;
import nc.vo.pub.BusinessException;

public interface IApplicationRuntimeAdjust {
	/** Ӧ��У����ʽ*/
	public static int ADJUST_TYPE_ADDASSIGN = 1;//��ǩ
	public static int ADJUST_TYPE_JUMP=2;//��ת
	
	/** ��ǩ��ʽ*/
	public static int ADDASSIGN_STYLE_SERIAL = 1;//����
	public static int ADDASSIGN_STYLE_PARALLEL = 2;//����
	public static int ADDASSIGN_STYLE_COOPERATION = 3;//Э��
	public static int ADDASSIGN_STYLE_NOTICE = 4;//֪��
	
	public WorkflownoteVO adjust(ApplicationRuntimeAdjustContext context) throws BusinessException;
}
