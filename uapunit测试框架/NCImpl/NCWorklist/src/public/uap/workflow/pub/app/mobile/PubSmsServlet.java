package uap.workflow.pub.app.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uap.workflow.pub.app.mobile.vo.MobileData;
import uap.workflow.pub.app.mobile.vo.MsgTrans2XML;
import uap.workflow.pub.app.mobile.vo.PubXMLDataInput;
import uap.workflow.pub.app.mobile.vo.PubXMLDataInputR;
import uap.workflow.pub.app.mobile.vo.PubXMLRowInput;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import com.thoughtworks.xstream.XStream;

/**
 * �����ɹ������������ŷ���������������Ϣ
 * 
 * @author ewei 2007-10-8
 * modifier ewei 2007-11-28 ������������Ҫnc�յ���Ϣ�����ִ,��֮
 */
public class PubSmsServlet implements IHttpServletAdaptor {

	/**
	 * ���չ����������������Ķ���Ϣ�����������Ϊ��Ӧ��object
	 */
	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger.debug(">>>PubSmsServlet.doAction() called");

		ServletInputStream sis = request.getInputStream();
		Scanner in = new Scanner(sis, "utf-8");
		StringBuffer sb = new StringBuffer();
		while (in.hasNext()) {
			sb.append(in.nextLine());
		}
		String xmlString = new String(sb);

		Logger.debug("***��ȡ���Ķ��Ŵ�=" + xmlString);
		String type = xmlString.substring(xmlString.indexOf("type=") + 6,
				xmlString.indexOf("type=") + 8);
		String flag = null;//��ִ��־λ
		String msg = null;//��ִ��Ϣ
		MsgTrans2XML m2x = new MsgTrans2XML();
		Object pubmsg;
		XStream xs = new XStream();
		try {
			if (type.equals("02")) {
				//����Ƕ���ģʽ��������������XStream����
				xs.alias("Data", PubXMLDataInput.class);
				xs.useAttributeFor(PubXMLDataInput.class, "type");
			} else if (type.equals("03")) {
				//����ǻظ�ģʽ��������������XStream����
				xs.alias("Data", PubXMLDataInputR.class);
				xs.alias("Row", PubXMLRowInput.class);
				xs.alias("ID", String.class);
				xs.alias("MobileNumber", String.class);
				xs.alias("Content", String.class);
				xs.useAttributeFor(PubXMLDataInputR.class, "System");
				xs.useAttributeFor(PubXMLDataInputR.class, "type");
				xs.useAttributeFor(PubXMLDataInputR.class, "appname");
				xs.useAttributeFor(PubXMLDataInputR.class, "accountnum");
				xs.useAttributeFor(PubXMLDataInputR.class, "accountname");
				xs.useAttributeFor(PubXMLDataInputR.class, "sendname");
			} else {
				throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PubSmsServlet-000000")/*����Ϣ���Ͳ���Ҫ��*/);
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			flag = "1";
			msg = e.getMessage();
			PrintWriter out = response.getWriter();
			out.print(m2x.sendBack(flag, msg));
			out.close();
		}

		pubmsg = xs.fromXML(xmlString);
		try {
			this.dealSmsObj(pubmsg);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			flag = "1";
			msg = e.getMessage();
			PrintWriter out = response.getWriter();
			out.print(m2x.sendBack(flag, msg));
			out.close();
		}
		flag = "0";
		msg = "ok";
		PrintWriter out = response.getWriter();
		out.print(m2x.sendBack(flag, msg));
		out.close();

	}

	/**
	 * �������Ϣ����
	 * @param rsms
	 * @throws BusinessException
	 */
	private void dealSmsObj(Object rsms) throws BusinessException {
		if (rsms == null)
			throw new BusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PubSmsServlet-000001")/*����Ϣ����Ϊ��*/);
		if (rsms instanceof PubXMLDataInput) {
			//type"02"
			PubXMLDataInput pxdi = (PubXMLDataInput) rsms;
			MobileHandler.getInstance()
					.mobileMsgReceived(pxdi.getMobileNumber(), pxdi.getContent(), null);

		} else if (rsms instanceof PubXMLDataInputR) {
			//type"03"
			PubXMLDataInputR pxd = (PubXMLDataInputR) rsms;
			MobileData condVO = new MobileData();
			condVO.setMobile(pxd.getPubxmlrow().getMobileNumber());
			condVO.setPk_sid(pxd.getPubxmlrow().getID());
			//FIXME::ֻ��֧�ֵ�����Դ���������ף�
			BaseDAO dao = new BaseDAO(WirelessManager.getMobileConfig().getDatasource());
			Collection co = dao.retrieve(condVO, true);
			String content = pxd.getPubxmlrow().getContent(); //�����������Ϊ"Y ͬ��"
			if (!co.isEmpty()) {
				//Logger.debug("String received <<< +coΪ�ǿ�");
				MobileData retVO = (MobileData) co.iterator().next();

				InvocationInfoProxy.getInstance().setBizCenterCode(retVO.getBizCenterCode());
				InvocationInfoProxy.getInstance().setBizCenterCode(retVO.getBizCenterCode());
				InvocationInfoProxy.getInstance().setGroupId(retVO.getGroupId());
				InvocationInfoProxy.getInstance().setGroupNumber(retVO.getGroupNumber());
				InvocationInfoProxy.getInstance().setHyCode(retVO.getHyCode());
				InvocationInfoProxy.getInstance().setUserDataSource(retVO.getUserDataSource());
				InvocationInfoProxy.getInstance().setUserId(retVO.getUserId());
				InvocationInfoProxy.getInstance().setLangCode(retVO.getLangCode());
				InvocationInfoProxy.getInstance().setBizDateTime(retVO.getBizDateTime().getMillis());

				content = retVO.getCmd() + "#" + retVO.getBilltype() + " " + retVO.getBillid() + " "
						+ content;
			}
			//4.�������Ϣ
			MobileHandler.getInstance().mobileMsgReceived(pxd.getPubxmlrow().getMobileNumber(), content,
					pxd.getPubxmlrow().getID());
		}
	}

	//	public static void main(String[] args) {
	//		PubSmsServlet name = new PubSmsServlet();
	//		String flag = "0";
	//		String msg = "haole";
	//		name.sendBack(flag, msg);
	//		Logger.debug("nihao");
	//	}

	//	public static void main(String[] args) throws FileNotFoundException {
	//		
	//		String xmlString ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><Data system=\"\" type=\"03\" appname=\"\" accountnum=\"accout\" accountname=\"name\" sendname=\"sname\"><Row><ID>1</ID><MobileNumber>13439331941</MobileNumber><Content>y ggg</Content></Row></Data>";
	//		String type = xmlString.substring(xmlString.indexOf("type=")+6, xmlString.indexOf("type=")+8);
	//		Logger.debug(type);
	//		XStream xs = new XStream();
	//		
	//		xs.alias("Data", PubXMLDataInputR.class);
	//		xs.alias("Row", PubXMLRowInput.class);
	//		xs.alias("ID", String.class);
	//		xs.alias("MobileNumber", String.class);
	//		xs.alias("Content", String.class);
	//		xs.useAttributeFor(PubXMLDataInputR.class, "System");
	//		xs.useAttributeFor(PubXMLDataInputR.class, "type");
	//		xs.useAttributeFor(PubXMLDataInputR.class, "appname");
	//		xs.useAttributeFor(PubXMLDataInputR.class, "accountnum");
	//		xs.useAttributeFor(PubXMLDataInputR.class, "accountname");
	//		xs.useAttributeFor(PubXMLDataInputR.class, "sendname");
	//		
	//		Object obj = xs.fromXML(xmlString);
	//		if(type.equals("03")){
	//		if(obj instanceof PubXMLDataInputR){
	//			PubXMLDataInputR pxd = (PubXMLDataInputR)obj;
	//			Logger.debug(pxd.getPubxmlrow().getMobileNumber()+
	//					pxd.getPubxmlrow().getContent()+pxd.getPubxmlrow().getID());
	//		}
	//		}else{
	//			Logger.debug("!!!");
	//		}
	//	}
}