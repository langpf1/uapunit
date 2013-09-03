package uap.workflow.app.mobile;

import uap.workflow.app.client.PfUtilTools;
import uap.workflow.pub.app.mobile.IPFMobileCommand;
import uap.workflow.pub.app.mobile.MobilePluginBase;
import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * �������ƶ�Ӧ�ò��
 * <li>��Ҫע����ierp/bin/mobileplugin.xml��
 * <li>ֻ֧�ֻظ�ģʽ
 * 
 * @author leijun 2006-3-9
 * @modifier leijun 2006-7-6 �û�����Ա������������
 */
public class WorkflowMobilePlugin extends MobilePluginBase {

	public WorkflowMobilePlugin() {
		super();
	}

	@Override
	public boolean isNeedValidatePwd() {
		//WARN::�����Ҫ���뽻�飬�뷵��true
		return false;
	}

	@Override
	public boolean shouldDeleteSid() {
		return isDeleteSid();
	}

	/**
	 * ����ĳ�ŵ���
	 * 
	 * @return
	 */
	private String approveBill() {
		//��������ʽ���������� ����ID Y/N/R [����]
		String[] params = getParams();
		Logger.info("����WorkflowMobilePlugin.approveBill getParams()��"+ params);
		
		if (params.length < 3)
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0000")/*������Ϣ��ʽ����*/;

		String billType = params[0];
		String billId = params[1];
		String checkResult = params[2];
		String checkNote = null;
		if (params.length > 3)
			checkNote = params[3];

		try {
			String strRet = PfUtilTools.approveSilently(billType, billId, checkResult, checkNote,
					getPkUser(), null);
			if (strRet == null)
				setDeleteSid(true);
			return strRet;
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			setDeleteSid(false);
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0001", null, new String[]{e.getMessage()})/*�쳣��{0}*/;
		}
	}

	@Override
	public String dealMobileMsg() {
		//���ָ������ִ�Сд
		String comd = getCommand().toUpperCase();
		if (comd.equals(IPFMobileCommand.APPROVE)) {
			return approveBill();
		} else if (comd.equals(IPFMobileCommand.UNAPPROVE)) {
			//TODO::��δʵ�ָö���ָ��
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*������δʵ�ָö���ָ��*/;
		} else if (comd.equals(IPFMobileCommand.QUERY_WORKITEM_TODO)) {
			//TODO::��δʵ�ָö���ָ��
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*������δʵ�ָö���ָ��*/;
		} else if (comd.equals(IPFMobileCommand.QUERY_WORKITEM_DONE)) {
			//TODO::��δʵ�ָö���ָ��
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*������δʵ�ָö���ָ��*/;
		} else {
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0003")/*���󣺲�֧�ֵ�ָ�*/;
		}
	}

}
