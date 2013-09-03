package uap.workflow.controller;

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

import uap.workflow.app.config.ParseConfigException;

public class ControllerParser{
	private Controller controllerConfig;
	private static ControllerParser inst = new ControllerParser();

	private ControllerParser() {
	}

	public static ControllerParser getInstance() {
		return inst;
	}	
	/**
	 * 解析输入
	 * @param reader
	 * @return
	 */
	public void parser(Reader reader) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Controller.class);
			Unmarshaller us = jc.createUnmarshaller();
			// 获取xmlinput工厂
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// 创建XMLStreamReader
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
			// 开始解析
			controllerConfig = (Controller) us.unmarshal(streamReader);
		} catch (Exception e) {
			throw new ParseConfigException(e.getMessage());
		}
	}
	
	/**
	 * 解析xml文件
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
		}
		catch (IOException e) {} 
		finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {}
		}
		return;
	}

	public Controller getControllerConfig(String filePath) {
		if(controllerConfig == null)
		{
			reader(filePath);
		}
		return controllerConfig;
	}
}
