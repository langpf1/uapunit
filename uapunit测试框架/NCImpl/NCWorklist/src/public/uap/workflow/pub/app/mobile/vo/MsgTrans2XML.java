package uap.workflow.pub.app.mobile.vo;

import java.io.FileNotFoundException;
import java.io.IOException;

import nc.bs.logging.Logger;
import nc.vo.pub.mobile.PubXMLData;
import nc.vo.pub.mobile.PubXMLDataCustom;
import nc.vo.pub.mobile.PubXMLDataR;
import nc.vo.pub.mobile.PubXMLDataRspns;
import nc.vo.pub.mobile.PubXMLMobile;
import nc.vo.pub.mobile.PubXMLMobileR;
import nc.vo.pub.mobile.PubXMLRow;
import nc.vo.pub.mobile.PubXMLRowR;

import com.thoughtworks.xstream.XStream;

/**
 * 将要发送的消息和电话号码转换为公共开发部短信服务标准格式
 * sendBroadcast 为发布模式格式 sendCustom 为订阅模式格式 sendRevert 为回复模式格式
 * @author ewei 2007-09-26
 * modifier ewei 2007-11-28 回复模式时nc收到短信给公共一个回执,回执标准格式 sendBack
 */
public class MsgTrans2XML {
	
	/**
	 * 发布模式，只持向不同手机组发不同消息
	 * @param tele
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public String sendBroadcast(String[][] tele, String[] msg) throws IOException {
		
		PubXMLData pubdata = new PubXMLData();
		
		for (int i = 0; i < tele.length; i++) {
			PubXMLRow row = new PubXMLRow(msg[i]);
			for (int j = 0; j < tele[i].length; j++) {
				row.getMobile().add(new PubXMLMobile(tele[i][j]));
			}
			pubdata.getPubxmlrow().add(row);
		}
		
		XStream xs = new XStream();
		xs.addImplicitCollection(PubXMLData.class, "pubxmlrow");
		xs.addImplicitCollection(PubXMLRow.class, "Mobile");
		xs.alias("Data", PubXMLData.class);
		xs.alias("Row", PubXMLRow.class);
		xs.alias("Mobile", PubXMLMobile.class);
		xs.alias("MobileNumber", String.class);

		xs.useAttributeFor(PubXMLData.class, "System");
		xs.useAttributeFor(PubXMLData.class, "type");
		xs.useAttributeFor(PubXMLData.class, "accountnum");
		xs.useAttributeFor(PubXMLData.class, "accountname");
		xs.useAttributeFor(PubXMLData.class, "sendname");

		return xs.toXML(pubdata);
	}
	
	/**
	 * 发布模式，但是只向同一号码组发同一消息
	 * @param tele
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public String sendBroadcast(String[] tele, String msg) throws IOException {
		
		PubXMLData pubdata = new PubXMLData();
		PubXMLRow row = new PubXMLRow(msg);
		for(int i=0;i<tele.length;i++){
			row.getMobile().add(new PubXMLMobile(tele[i]));
		}
		pubdata.getPubxmlrow().add(row);
			
		XStream xs = new XStream();
		xs.addImplicitCollection(PubXMLData.class, "pubxmlrow");
		xs.addImplicitCollection(PubXMLRow.class, "Mobile");
		xs.alias("Data", PubXMLData.class);
		xs.alias("Row", PubXMLRow.class);
		xs.alias("Mobile", PubXMLMobile.class);
		xs.alias("MobileNumber", String.class);

		xs.useAttributeFor(PubXMLData.class, "System");
		xs.useAttributeFor(PubXMLData.class, "type");
		xs.useAttributeFor(PubXMLData.class, "accountnum");
		xs.useAttributeFor(PubXMLData.class, "accountname");
		xs.useAttributeFor(PubXMLData.class, "sendname");

		return xs.toXML(pubdata);
	}
	
	/**
	 * 订阅模式，在nc中目前只对正确信息回复，因此格式写死在程序中，
	 * 只返回正确时短信内容 
	 * @param tele
	 * @param flag
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public String sendCustom(String tele, String msg) throws IOException {
		
		PubXMLDataCustom pubdata = new PubXMLDataCustom();
		pubdata.setMobileNumber(tele);
		pubdata.setContent(msg);
		
		XStream xs = new XStream();
		xs.alias("Data", PubXMLDataCustom.class);
		
		xs.useAttributeFor(PubXMLDataCustom.class, "System");
		xs.useAttributeFor(PubXMLDataCustom.class, "type");
		xs.useAttributeFor(PubXMLDataCustom.class, "accountnum");
		xs.useAttributeFor(PubXMLDataCustom.class, "accountname");

		return xs.toXML(pubdata);
	}
	
	/**
	 * 回复模式，目前只支持向一个号码组发同一个消息，不支持向不同号码组发不同消息
	 * @param tele
	 * @param msg
	 * @param sids
	 * @return Document
	 * @throws IOException
	 */
	public String sendRevert(String[] tele, String msg, String[] sids) throws IOException {
		
		PubXMLDataR pubdata = new PubXMLDataR();
		PubXMLRowR row = new PubXMLRowR(msg);
		for(int i=0;i<tele.length;i++){
			row.getMobile().add(new PubXMLMobileR(tele[i],sids[i]));
		}
		pubdata.getPubxmlrow().add(row);
			
		XStream xs = new XStream();
		xs.addImplicitCollection(PubXMLDataR.class, "pubxmlrow");
		xs.addImplicitCollection(PubXMLRowR.class, "Mobile");
		xs.alias("Data", PubXMLDataR.class);
		xs.alias("Row", PubXMLRowR.class);
		xs.alias("Mobile", PubXMLMobileR.class);
		xs.alias("MobileNumber", String.class);
		xs.alias("ID", String.class);
		xs.omitField(PubXMLDataR.class, "PubXMLRowInput");

		xs.useAttributeFor(PubXMLDataR.class, "System");
		xs.useAttributeFor(PubXMLDataR.class, "type");
		xs.useAttributeFor(PubXMLDataR.class, "appname");
		xs.useAttributeFor(PubXMLDataR.class, "accountnum");
		xs.useAttributeFor(PubXMLDataR.class, "accountname");
		xs.useAttributeFor(PubXMLDataR.class, "sendname");

		return xs.toXML(pubdata);
	}

	/**
	 * 回复模式，向一个号码发一个消息
	 * @param tele
	 * @param msg
	 * @param sids
	 * @return Document
	 * @throws IOException
	 */
	public String sendRevert(String tele, String msg, String sid) throws IOException {
		
		PubXMLDataR pubdata = new PubXMLDataR();
		PubXMLRowR row = new PubXMLRowR(msg);
		row.getMobile().add(new PubXMLMobileR(tele,sid));
		pubdata.getPubxmlrow().add(row);
			
		XStream xs = new XStream();
		xs.addImplicitCollection(PubXMLDataR.class, "pubxmlrow");
		xs.addImplicitCollection(PubXMLRowR.class, "Mobile");
		xs.alias("Data", PubXMLDataR.class);
		xs.alias("Row", PubXMLRowR.class);
		xs.alias("Mobile", PubXMLMobileR.class);
		xs.alias("MobileNumber", String.class);
		xs.alias("ID", String.class);
		xs.omitField(PubXMLDataR.class, "PubXMLRowInput");

		xs.useAttributeFor(PubXMLDataR.class, "System");
		xs.useAttributeFor(PubXMLDataR.class, "type");
		xs.useAttributeFor(PubXMLDataR.class, "appname");
		xs.useAttributeFor(PubXMLDataR.class, "accountnum");
		xs.useAttributeFor(PubXMLDataR.class, "accountname");
		xs.useAttributeFor(PubXMLDataR.class, "sendname");

		return xs.toXML(pubdata);
	}
	
    public String sendBack(String TranFlag,String ErrMsg){
    	
    	PubXMLDataRspns rspns = new PubXMLDataRspns();
    	
    	rspns.setTranFlag(TranFlag);
    	rspns.setErrMsg(ErrMsg);
		
		XStream xs = new XStream();
		xs.alias("Data", PubXMLDataRspns.class);
		
		return xs.toXML(rspns);  	
    }
	
//	public static void main(String[] args) throws IOException {
//		MsgTrans2XML msg2xml = new MsgTrans2XML();
//
//		Logger.debug(msg2xml.sendBack("0", "ok"));
//	}
}
