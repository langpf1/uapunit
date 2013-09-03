package uap.workflow.pub.app.mobile.vo;


/**
 * 订阅模式发送使用的data对象
 * @author ewei
 *
 */
public class PubXMLDataCustom {
		String System = "nc";

		String type = "02";

		String accountnum = "accout";

		String accountname = "name";

		String TranFlag = "0";
		
		String ErrMsg = "";
		
		String MobileNumber = "";
		
		String Content = "";

		public String getContent() {
			return Content;
		}

		public void setContent(String content) {
			Content = content;
		}

		public String getErrMsg() {
			return ErrMsg;
		}

		public void setErrMsg(String errMsg) {
			ErrMsg = errMsg;
		}

		public String getMobileNumber() {
			return MobileNumber;
		}

		public void setMobileNumber(String mobileNumber) {
			MobileNumber = mobileNumber;
		}

		public String getTranFlag() {
			return TranFlag;
		}

		public void setTranFlag(String tranFlag) {
			TranFlag = tranFlag;
		}
		
		
}
