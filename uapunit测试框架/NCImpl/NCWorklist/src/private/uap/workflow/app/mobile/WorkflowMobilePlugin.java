package uap.workflow.app.mobile;

import uap.workflow.app.client.PfUtilTools;
import uap.workflow.pub.app.mobile.IPFMobileCommand;
import uap.workflow.pub.app.mobile.MobilePluginBase;
import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 审批流移动应用插件
 * <li>需要注册在ierp/bin/mobileplugin.xml中
 * <li>只支持回复模式
 * 
 * @author leijun 2006-3-9
 * @modifier leijun 2006-7-6 用户与人员基本档案关联
 */
public class WorkflowMobilePlugin extends MobilePluginBase {

	public WorkflowMobilePlugin() {
		super();
	}

	@Override
	public boolean isNeedValidatePwd() {
		//WARN::如果需要密码交验，请返回true
		return false;
	}

	@Override
	public boolean shouldDeleteSid() {
		return isDeleteSid();
	}

	/**
	 * 审批某张单据
	 * 
	 * @return
	 */
	private String approveBill() {
		//检查参数格式：单据类型 单据ID Y/N/R [批语]
		String[] params = getParams();
		Logger.info("进入WorkflowMobilePlugin.approveBill getParams()："+ params);
		
		if (params.length < 3)
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0000")/*错误：消息格式不对*/;

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
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0001", null, new String[]{e.getMessage()})/*异常：{0}*/;
		}
	}

	@Override
	public String dealMobileMsg() {
		//检查指令：不区分大小写
		String comd = getCommand().toUpperCase();
		if (comd.equals(IPFMobileCommand.APPROVE)) {
			return approveBill();
		} else if (comd.equals(IPFMobileCommand.UNAPPROVE)) {
			//TODO::尚未实现该短信指令
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*错误：尚未实现该短信指令*/;
		} else if (comd.equals(IPFMobileCommand.QUERY_WORKITEM_TODO)) {
			//TODO::尚未实现该短信指令
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*错误：尚未实现该短信指令*/;
		} else if (comd.equals(IPFMobileCommand.QUERY_WORKITEM_DONE)) {
			//TODO::尚未实现该短信指令
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0002")/*错误：尚未实现该短信指令*/;
		} else {
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "wfMobilePlugin-0003")/*错误：不支持的指令集*/;
		}
	}

}
