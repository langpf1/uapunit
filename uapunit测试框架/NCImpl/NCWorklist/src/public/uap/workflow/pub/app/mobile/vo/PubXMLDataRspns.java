package uap.workflow.pub.app.mobile.vo;

/**
 * �ظ�ģʽ���յ�������Ϣ�������������һ����ִ
 * @author ewei
 * 2007-11-28
 * <Data>	
 *	 <TranFlag>0</ TranFlag>	
 *	 <ErrMsg>��������</ErrMsg>
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
