package uap.workflow.pub.app.mobile.vo;

/**
 * 回复模式接收到审批消息后给公共开发部一个回执
 * @author ewei
 * 2007-11-28
 * <Data>	
 *	 <TranFlag>0</ TranFlag>	
 *	 <ErrMsg>错误描述</ErrMsg>
 * </Data>
 */
public class PubXMLDataRspns {
	String TranFlag = null;
	String ErrMsg = null;
	public String getErrMsg() {
		return ErrMsg;
	}
	public void setErrMsg(String errMsg) {
		ErrMsg = errMsg;
	}
	public String getTranFlag() {
		return TranFlag;
	}
	public void setTranFlag(String tranFlag) {
		TranFlag = tranFlag;
	}
	
}
