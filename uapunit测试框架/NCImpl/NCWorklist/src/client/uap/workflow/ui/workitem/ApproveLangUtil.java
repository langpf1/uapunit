package uap.workflow.ui.workitem;

import nc.ui.ml.NCLangRes;
import nc.vo.pf.term.ApproveTermConfig;
import nc.vo.pf.term.IApproveTerm;

/**
 * �������﹤����
 * @author zhangrui
 *
 */
public class ApproveLangUtil {
	
	public static String getPleaseDispatchNextUser() {
		return NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "ָ����һ���������ڵĲ�����"*/;
	}
	
	public static String getAddAttach() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000211");/* ��Ӹ��� */
	}
	
	public static String getTrack() {
		return NCLangRes.getInstance().getStrByID("102220",	
			"UPP102220-000210");/* ���� */
	}
	
	public static String getAttach() {
		return NCLangRes.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000660");/* "����" */
	}
	
	public static String getCopySend() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000205"); /* ���� */
	}
	
	public static String getUploadAttach() {
		return NCLangRes.getInstance().getStrByID(
				"pfworkflow", "UPPpfworkflow-000661");/* "�ϴ�����" */
	}
	
	public static String getCloseBillAfterApprove() {
		return NCLangRes.getInstance().getStrByID("102220",
				"ApproveWorkitemAcceptDlg-000000");/* ������ɺ�رյ��ݽ��� */
	}
	
	public static String getHintTo() {
		return NCLangRes.getInstance().getStrByID("pfworkflow",
				"UPPpfworkflow-000227");/* @res "��ʾ>>" */
	}
	
	public static String getOK() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
			"UPP_Beans-000021");/* @res "ȷ��" */
	}
	
	public static String getCancel() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("_Beans",
						"UPP_Beans-000002");/* @res "ȡ��" */
	}
	
	/**
	 * ����ʱ�����쳣{0}
	 */
	public static String getExceptionWhenTransfer(String message) {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000212", null,
				new String[] { message });/*
										   * ����ʱ�����쳣{0}
										   */
	}
	/**
	 * �����������
	 */
	public static String getApproveDealStatus() {
		return NCLangRes.getInstance().getStrByID("102220",
			"UPP102220-000163");/* @res "�����������" */
	}
	/**
	 * ����
	 */
	public static String getApprove() {
		return NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000130");/* @res "����" */
	}
	
	public static String getPass() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.PASS);
	}
	
	public static String getNoPass() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.NO_PASS);
	}
	/**
	 * ����
	 */
	public static String getReject() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.REJECT);
	}
	/**
	 * ����
	 */
	public static String getTransfer() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.TRANSFER);
	}
	/**
	 * ��ǩ
	 */
	public static String getAddAssign() {
		return ApproveTermConfig.getInstance().getText(IApproveTerm.ADD_APPROVER);
	}
}
