package uap.workflow.pub.app.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import nc.bs.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.workflow.pub.app.mobile.vo.MobileConfig;
import uap.workflow.pub.app.mobile.vo.MsgTrans2XML;
import uap.workflow.pub.app.mobile.vo.PubDevSmsConfig;
import uap.workflow.pub.app.mobile.vo.PubStatBack;
import uap.workflow.pub.app.mobile.vo.ReceivedSmsVO;

import com.thoughtworks.xstream.XStream;

/**
 * 使用公共开发部的短消息服务实现类
 * 
 * @author ewei 2007-9-28
 * @since NC5.02 
 */
public class PubDevSmsImpl extends ShortMessageService {

	@Override
	public boolean initialize() {
		return true;
	}

	/**
	 * 接收短信处理，接收短信方法使用PubSmsServlet
	 * @deprecated
	 */
	@Override
	public ReceivedSmsVO[] receiveMessages() {
		return null;
	}

	/**
	 * 适用于向不同的手机号发不同的消息 发布模式（type01）
	 */
	@Override
	public Object sendMessages(String[][] targetPhones, String[] msg) {

		MsgTrans2XML m2x = new MsgTrans2XML();
		String responsemsg = null;
		try {
			responsemsg = execute(m2x.sendBroadcast(targetPhones, msg));
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}

		XStream xs = new XStream();

		xs.alias("Data", PubStatBack.class);
		xs.alias("TranFlag", String.class);
		xs.alias("ErrMsg", String.class);

		xs.useAttributeFor(PubStatBack.class, "System");
		xs.useAttributeFor(PubStatBack.class, "type");
		xs.useAttributeFor(PubStatBack.class, "accountnum");
		xs.useAttributeFor(PubStatBack.class, "accountname");
		xs.useAttributeFor(PubStatBack.class, "sendname");

		return responsemsg == null ? "failed" : xs.fromXML(responsemsg);
	}

	/**
	 * 适用于向一个号码组发同一的消息 发布模式（type01extend）
	 */
	@Override
	public Object sendMessages(String[] targetPhones, String msg) {

		MsgTrans2XML m2x = new MsgTrans2XML();
		String responsemsg = null;
		try {
			responsemsg = execute(m2x.sendBroadcast(targetPhones, msg));
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		XStream xs = new XStream();

		xs.alias("Data", PubStatBack.class);
		xs.alias("TranFlag", String.class);
		xs.alias("ErrMsg", String.class);

		xs.useAttributeFor(PubStatBack.class, "System");
		xs.useAttributeFor(PubStatBack.class, "type");
		xs.useAttributeFor(PubStatBack.class, "accountnum");
		xs.useAttributeFor(PubStatBack.class, "accountname");
		xs.useAttributeFor(PubStatBack.class, "sendname");

		return responsemsg == null ? "failed" : xs.fromXML(responsemsg);
	}

	/**
	 * 适用于订阅模式，向特定的手机号发消息（type02）
	 */
	@Override
	public Object sendMessage(String targetPhone, String msg) {

		MsgTrans2XML m2x = new MsgTrans2XML();
		String responsemsg = null;
		try {
			responsemsg = execute(m2x.sendCustom(targetPhone, msg));
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		XStream xs = new XStream();

		xs.alias("Data", PubStatBack.class);
		xs.alias("TranFlag", String.class);
		xs.alias("ErrMsg", String.class);

		xs.useAttributeFor(PubStatBack.class, "System");
		xs.useAttributeFor(PubStatBack.class, "type");
		xs.useAttributeFor(PubStatBack.class, "accountnum");
		xs.useAttributeFor(PubStatBack.class, "accountname");
		xs.useAttributeFor(PubStatBack.class, "sendname");

		return responsemsg == null ? "failed" : xs.fromXML(responsemsg);
	}

	/**
	 * 适用于审批，向一个号码发一条短信，并同时发出sid,
	 * 适用于回复模式（type03extend）
	 */
	@Override
	public Object sendMessage(String targetPhone, String msg, String sid) {

		MsgTrans2XML m2x = new MsgTrans2XML();
		String responsemsg = null;
		try {
			responsemsg = execute(m2x.sendRevert(targetPhone, msg, sid));
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		XStream xs = new XStream();

		xs.alias("Data", PubStatBack.class);
		xs.alias("TranFlag", String.class);
		xs.alias("ErrMsg", String.class);

		xs.useAttributeFor(PubStatBack.class, "System");
		xs.useAttributeFor(PubStatBack.class, "type");
		xs.useAttributeFor(PubStatBack.class, "accountnum");
		xs.useAttributeFor(PubStatBack.class, "accountname");
		xs.useAttributeFor(PubStatBack.class, "sendname");

		return responsemsg == null ? "failed" : xs.fromXML(responsemsg);
	}

	/**
	 * 适用于审批，向不同号码发相同短信，并同时发出sid,
	 * 支持向不同的手机发同一消息，每个手机对应一个sid
	 * 适用于回复模式（type03）
	 */
	@Override
	public Object sendMessages(String[] targetPhones, String msg, String[] sids) {

		MsgTrans2XML m2x = new MsgTrans2XML();
		String responsemsg = null;
		try {
			responsemsg = execute(m2x.sendRevert(targetPhones, msg, sids));
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		}
		XStream xs = new XStream();

		xs.alias("Data", PubStatBack.class);
		xs.alias("TranFlag", String.class);
		xs.alias("ErrMsg", String.class);

		xs.useAttributeFor(PubStatBack.class, "System");
		xs.useAttributeFor(PubStatBack.class, "type");
		xs.useAttributeFor(PubStatBack.class, "accountnum");
		xs.useAttributeFor(PubStatBack.class, "accountname");
		xs.useAttributeFor(PubStatBack.class, "sendname");

		return responsemsg == null ? "failed" : xs.fromXML(responsemsg);
	}

	private String execute(String strxml) {
		Logger.debug(">>PubDevSmsImpl.execute() called");
		Logger.debug(">>strxml=" + strxml);
		try {
			strxml = new String(strxml.getBytes("utf-8"), "iso8859-1");
			Logger.debug("send content = " + strxml);
		} catch (UnsupportedEncodingException e1) {
			Logger.error(e1.getMessage(), e1);
		}
		MobileConfig mc = WirelessManager.getMobileConfig();
		PubDevSmsConfig pubsms = mc.getPubSms();
		String strurl = pubsms.getURL();
		Logger.debug(">>链接的短信发送URL=" + strurl);
		
		//int timeout = pubsms.getTimeout();
		NameValuePair[] data = { new NameValuePair("senddata", strxml) };
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(strurl);
		postMethod.setRequestBody(data);
		try {
			//httpClient.setConnectionTimeout(timeout);
			httpClient.executeMethod(postMethod);
			return postMethod.getResponseBodyAsString();
		} catch (HttpException e) {
			Logger.error(e.getMessage(), e);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
		} finally {
			postMethod.releaseConnection();
		}
		return null;
	}
	
	//测使用
	//	public static void main(String[] args) throws IOException {
	//		NameValuePair[] data = { new NameValuePair("", "nihao")};
	//		HttpClient httpClient = new HttpClient();
	//		PostMethod postMethod = new PostMethod("http://10.1.78.52:80/service/pubsmsservlet");
	//		postMethod.setRequestBody(data);
	//		try {
	//			//httpClient.setConnectionTimeout(timeout);
	//			httpClient.executeMethod(postMethod);
	//		} catch (HttpException e) {
	//			Logger.error(e.getMessage(),e);
	//		} catch (IOException e) {
	//			Logger.error(e.getMessage(),e);
	//		}finally{
	//			postMethod.releaseConnection();
	//		}
	//		
	//		Task task = new Task();
	//		for (int i = 0; i < 10; i++) {
	//			new Thread(task).start();
	//		}

	//		String[][] tele={{"13439331941"},{"13439331941"}};
	//		String[] msg = {"hello moto","hello nokia"};
	//String[] tele1 = {"13691091303","13439331941"};
	//String msg1 = "hello philips";
	//		PubDevSmsImpl pdsi = new PubDevSmsImpl();
	//		Object obj = pdsi.sendMessages(tele, msg);
	//		if(obj instanceof PubStatBack){
	//			PubStatBack pxr = (PubStatBack)obj;
	//			Logger.debug(pxr.getErrMsg());
	//		}else{
	//			Logger.debug("err");
	//		}
	//		PubStatBack psb = new PubStatBack();
	//		psb.setTranFlag("y");
	//		psb.setErrMsg("cuole");
	//		
	//		XStream xs = new XStream();
	//		
	//		xs.alias("Data",PubStatBack.class);
	//		xs.alias("TranFlag", String.class);
	//		xs.alias("ErrMsg", String.class);
	//		
	//		xs.useAttributeFor(PubStatBack.class, "System");
	//		xs.useAttributeFor(PubStatBack.class, "type");
	//		xs.useAttributeFor(PubStatBack.class, "accountnum");
	//		xs.useAttributeFor(PubStatBack.class, "accountname");
	//		xs.useAttributeFor(PubStatBack.class, "sendname");
	//		
	//		PrintWriter pw = new PrintWriter(new FileOutputStream("test.xml"));
	//		pw.write(xs.toXML(psb));
	//		pw.close();
	//	}
}
