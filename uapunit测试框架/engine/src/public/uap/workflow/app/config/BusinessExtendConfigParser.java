package uap.workflow.app.config;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;

public class BusinessExtendConfigParser{
	private BusinessExtendConfig businessExtendConfig;
	private static BusinessExtendConfigParser inst = new BusinessExtendConfigParser();

	private BusinessExtendConfigParser() {
	}

	public static BusinessExtendConfigParser getInstance() {
		return inst;
	}	
	/**
	 * ��������
	 * @param reader
	 * @return
	 */
	public void parser(Reader reader) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(BusinessExtendConfig.class);
			Unmarshaller us = jc.createUnmarshaller();
			// ��ȡxmlinput����
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// ����XMLStreamReader
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
			// ��ʼ����
			businessExtendConfig = (BusinessExtendConfig) us.unmarshal(streamReader);
		} catch (Exception e) {
			throw new ParseConfigException(e.getMessage());
		}
	}
	
	/**
	 * ����xml�ļ�
	 * 
	 * @param filePath
	 * @return
	 */
	public void reader(String filePath) {
		Reader reader = null;
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);
			filePath = url.getPath();
			File f = new File(filePath);
			if (!f.exists())
				return;
			String xmlText = FileUtils.readFileToString(new File(filePath), "UTF-8");
			reader = new StringReader(xmlText);
			parser(reader);
		} catch (IOException e) {} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {}
		}
		return;
	}

	public BusinessExtendConfig getBusinessExtendConfig(String filePath) {
		if(businessExtendConfig == null)
		{
			reader(filePath);
		}
		return businessExtendConfig;
	}
}
