package uap.workflow.ui.workitem;

import nc.ui.ml.NCLangRes;
import nc.vo.pf.term.ApproveTermConfig;
import nc.vo.pf.term.IApproveTerm;

/**
 * 审批多语工具类
 * @author zhangrui
 *
 */
public class ApproveLangUtil {
	
	public static String getPleaseDispatchNextUser() {
		return NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "指派下一个审批环节的参与者"*/;
	}
	
	public static String getAddAttach() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000211");/* 添加附件 */
	}
	
	public static String getTrack() {
		return NCLangRes.getInstance().getStrByID("102220",	
			"UPP102220-000210");/* 跟踪 */
	}
	
	public static String getAttach() {
		return NCLangRes.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000660");/* "附件" */
	}
	
	public static String getCopySend() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000205"); /* 抄送 */
	}
	
	public static String getUploadAttach() {
		return NCLangRes.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000661");/* "上传附件" */
	}
	
	public static String getCloseBillAfterApprove() {
		return NCLangRes.getInstance().getStrByID("102220",
				"ApproveWorkitemAcceptDlg-000000");/* 审批完成后关闭单据界面 */
	}
	
	public static String getHintTo() {
		return NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000227");/* @res "提示>>" */
	}
	
	public static String getOK() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
			"UPP_Beans-000021");/* @res "确定" */
	}
	
	public static String getCancel() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
						"UPP_Beans-000002");/* @res "取消" */
	}
	
	/**
	 * 改派时出现异常{0}
	 */
	public static String getExceptionWhenTransfer(String message) {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000212", null,
				new String[] { message });/*
										   * 改派时出现异常{0}
										   */
	}
	/**
	 * 审批处理情况
	 */
	public static String getApproveDealStatus() {
		return NCLangRes.getInstance().getStrByID("102220",
			"UPP102220-000163");/* @res "审批处理情况" */
	}
	/**
	 * 审批
	 */
	public static String getApprove() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000130");/* @res "审批" */
	}
	
	public static String getPass() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.PASS);
	}
	
	public static String getNoPass() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.NO_PASS);
	}
	/**
	 * 驳回
	 */
	public static String getReject() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.REJECT);
	}
	/**
	 * 改派
	 */
	public static String getTransfer() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.TRANSFER);
	}
	/**
	 * 加签
	 */
	public static String getAddAssign() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.ADD_APPROVER);
	}
}
