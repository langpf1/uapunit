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
 * ��Ҫ���͵���Ϣ�͵绰����ת��Ϊ�������������ŷ����׼��ʽ
 * sendBroadcast Ϊ����ģʽ��ʽ sendCustom Ϊ����ģʽ��ʽ sendRevert Ϊ�ظ�ģʽ��ʽ
 * @author ewei 2007-09-26
 * modifier ewei 2007-11-28 �ظ�ģʽʱnc�յ����Ÿ�����һ����ִ,��ִ��׼��ʽ sendBack
 */
public class MsgTrans2XML {
	
	/**
	 * ����ģʽ��ֻ����ͬ�ֻ��鷢��ͬ��Ϣ
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
	 * ����ģʽ������ֻ��ͬһ�����鷢ͬһ��Ϣ
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
	 * ����ģʽ����nc��Ŀǰֻ����ȷ��Ϣ�ظ�����˸�ʽд���ڳ����У�
	 * ֻ������ȷʱ�������� 
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
	 * �ظ�ģʽ��Ŀǰֻ֧����һ�������鷢ͬһ����Ϣ����֧����ͬ�����鷢��ͬ��Ϣ
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
	 * �ظ�ģʽ����һ�����뷢һ����Ϣ
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
