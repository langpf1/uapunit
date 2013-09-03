package uap.workflow.bpmn2.parser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import uap.workflow.bpmn2.exception.Bpmn2Exception;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.Connector;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.ISynchronization;
import uap.workflow.bpmn2.model.MessageFlow;
import uap.workflow.bpmn2.model.NameSpaceConst;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.SubProcess;

import org.apache.commons.io.FileUtils;
import com.mxgraph.model.mxCell;

public class ProcessDefinitionsManager {
	/*
	 *已经是纯粹的模型了，用于动态创建工作流模型的场景 
	 */
	public static String toXml(Bpmn2MemoryModel object, boolean isPureModel) {
		class MyNamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {
			public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
				if (namespaceUri == null || namespaceUri.length() == 0) {
					return "";
				}
				if (NameSpaceConst.XMLNS_URL.equalsIgnoreCase(namespaceUri)) {
					requirePrefix = false;
				} else {
					requirePrefix = true;
				}
				if (requirePrefix) {
					if (NameSpaceConst.BPMNDI_URL.equals(namespaceUri)) {
						return NameSpaceConst.BPMNDI_PREFIX;
					}
					if (NameSpaceConst.OMGDC_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.OMGDC_PREFIX;
					}
					if (NameSpaceConst.OMGDI_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.OMGDI_PREFIX;
					}
					if (NameSpaceConst.BIZEX_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.BIZEX_PREFIX;
					}
					return suggestion;
				} else {
					return "";
				}
			}
		}
//		Person person = new Person();
//		person.setId("1");
//		person.setName("asdf");
//		Student student = new Student();
//		student.setId("1");
//		student.setName("2");
//		person.add(student);9
		
		JAXBContext jc = null;
		try {
			if(!isPureModel)
			{
				BpmnDiagramMxCellInterchange.setBpmnDiagram(object);
			}
			saveUserTaskExtension(object);
			jc = JAXBContext.newInstance(Bpmn2MemoryModel.class);
			Marshaller marshaller = jc.createMarshaller();
			// 设置编码
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			// 设置是否要格式化输出
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			// 注册生成命名空间的内部类
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
			// 创建输出流
			Writer outPut = new StringWriter();
			// 开始序列化
			marshaller.marshal(object, outPut);
			return outPut.toString();
		} catch (Exception e) {
			throw new Bpmn2Exception(e);
		}
	}
	
	private static void saveUserTaskExtension(List<FlowElement> elements){
		for(FlowElement element : elements){
			marshalCustom(element);
			if(element instanceof SubProcess){
				saveUserTaskExtension(((SubProcess)element).getFlowElements());
			}
		}
	}
	// 保存模型时，把定义的扩展内容同步到Marshaller结构体中
	private static void saveUserTaskExtension(Bpmn2MemoryModel model) {
		for (Process process : model.getProcesses()) {
			marshalCustom(process);
			saveUserTaskExtension(process.getFlowElements());
		}
	}
	private static void marshalCustom(Object flowElement) {
		if (flowElement instanceof ISynchronization) {
			ISynchronization synchronization = (ISynchronization) flowElement;
			synchronization.marshal();
		}
	}
	public void testa() {
		class MyNamespacePrefixMapper extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {
			public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
				if (namespaceUri == null || namespaceUri.length() == 0) {
					return "";
				}
				if (NameSpaceConst.XMLNS_URL.equalsIgnoreCase(namespaceUri)) {
					requirePrefix = false;
				} else {
					requirePrefix = true;
				}
				if (requirePrefix) {
					if (NameSpaceConst.BPMNDI_URL.equals(namespaceUri)) {
						return NameSpaceConst.BPMNDI_PREFIX;
					}
					if (NameSpaceConst.OMGDC_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.OMGDC_PREFIX;
					}
					if (NameSpaceConst.OMGDI_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.OMGDI_PREFIX;
					}
					if (NameSpaceConst.BIZEX_URL.equalsIgnoreCase(namespaceUri)) {
						return NameSpaceConst.BIZEX_PREFIX;
					}
					return suggestion;
				} else {
					return "";
				}
			}
		}
		try {
			JAXBContext jc = JAXBContext.newInstance(Bpmn2MemoryModel.class.getPackage().getName());
			Reader rd = new FileReader(new File("c:/simpletask.bpmn20.xml"));
			
			Unmarshaller us = jc.createUnmarshaller();
			Marshaller marshaller = jc.createMarshaller();
			Bpmn2MemoryModel object = null;
			// 方法一
			{
				// // 创建SaxFactory
				// SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				// // 是否要忽略命名空间
				// saxFactory.setNamespaceAware(true);
				// // 是否开启校验
				// saxFactory.setValidating(true);
				// // 创建Saxparser
				// SAXParser saxParser = saxFactory.newSAXParser();
				// {
				// // 设置Shema的语言描述
				// saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				// "http://www.w3.org/2001/XMLSchema");
				// // 获取shema资源路径
				// URL url =
				// ReflectUtil.getResource(BpmnParser.BPMN_20_SCHEMA_LOCATION);
				// // 设置shema校验
				// saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
				// url.toString());
				// }
				// // 获取XmlReader
				// XMLReader xmlReader = saxParser.getXMLReader();
				// // 构造出SaxSource
				// Source source = new SAXSource(xmlReader, new
				// InputSource(rd));
				// // 开始解析
				// object = (Bpmn2MemoryModel) us.unmarshal(source);
			}
			// 方法2
			{
				// 获取xmlinput工厂
				XMLInputFactory inputFactory = XMLInputFactory.newInstance();
				// 创建XMLStreamReader
				XMLStreamReader streamReader = inputFactory.createXMLStreamReader(rd);
				// 构造Reader的委托类
				StreamReaderDelegate delegatingStreamReader = new StreamReaderDelegate(streamReader) {
					String namespaceURI = "";
					public String getNamespaceURI() {
						namespaceURI = super.getNamespaceURI();
						return super.getNamespaceURI();
					}
					public String getPrefix() {
						if ("http://www.omg.org/spec/BPMN/20100524/DI".equalsIgnoreCase(namespaceURI)) {
							return "bpmndi";
						}
						if ("http://www.omg.org/spec/DD/20100524/DC".equalsIgnoreCase(namespaceURI)) {
							return "omgdc";
						}
						return super.getPrefix();
					}
				};
				// 开始解析
				object = (Bpmn2MemoryModel) us.unmarshal(delegatingStreamReader);
			}
			// 方法3
			{
				// // 构造DocumentBuilderFactoryu
				// DocumentBuilderFactory dbf =
				// DocumentBuilderFactory.newInstance();
				// dbf.setNamespaceAware(true);
				// // 构造DocumentBuilder
				// DocumentBuilder db = dbf.newDocumentBuilder();
				// // 构造Document
				// Document doc = db.parse(new InputSource(rd));
				// // 开始解析
				// object = (Bpmn2MemoryModel) us.unmarshal(doc);
			}
			{
				// 设置编码
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				// 设置是否要格式化输出
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
				// 创建方法内部类用来生成自定义命名空间,这个地方需要下载jaxb-impl-2.0.3.jar
				// 注册生成命名空间的内部类
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
				// 创建输出流
				OutputStream outPut = new ByteArrayOutputStream();
				// 开始序列化
				marshaller.marshal(object, outPut);
				outPut.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 解析输入
	 * 
	 * @param reader
	 * @return
	 */
	public static Bpmn2MemoryModel parser(Reader reader) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Bpmn2MemoryModel.class.getPackage().getName());
			Unmarshaller us = jc.createUnmarshaller();
			// 获取xmlinput工厂
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// 创建XMLStreamReader
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
			// 构造Reader的委托类
			StreamReaderDelegate delegatingStreamReader = new StreamReaderDelegate(streamReader) {
				String namespaceURI = "";
				public String getNamespaceURI() {
					namespaceURI = super.getNamespaceURI();
					return super.getNamespaceURI();
				}
				public String getPrefix() {
					if (NameSpaceConst.BPMNDI_URL.equalsIgnoreCase(namespaceURI)) {
						return NameSpaceConst.BPMNDI_PREFIX;
					}
					if (NameSpaceConst.OMGDC_URL.equalsIgnoreCase(namespaceURI)) {
						return NameSpaceConst.OMGDC_PREFIX;
					}
					if (NameSpaceConst.OMGDI_URL.equalsIgnoreCase(namespaceURI)) {
						return NameSpaceConst.OMGDI_PREFIX;
					}
					if (NameSpaceConst.BIZEX_URL.equalsIgnoreCase(namespaceURI)) {
						return NameSpaceConst.BIZEX_PREFIX;
					}
					return super.getPrefix();
				}
			};
			// 开始解析
			Bpmn2MemoryModel model = (Bpmn2MemoryModel) us.unmarshal(delegatingStreamReader);
			BuildElementMap(model);
			BpmnDiagramMxCellInterchange.constructCellMap(model);
			parseUserTaskExtension(model);
			return model;
		} catch (Exception e) {
			throw new Bpmn2Exception(e);
		}
		// exception handling omitted
	}
	
	private static void parseUserTaskExtension(List<FlowElement> elements){
		for(FlowElement element : elements){
			unmarshalCustom(element);
			if(element instanceof SubProcess){
				parseUserTaskExtension(((SubProcess)element).getFlowElements());
			}
		}
	}
	// parse模型时，把unmarshaller结构体中扩展内容同步到定义上
	private static void parseUserTaskExtension(Bpmn2MemoryModel model) {
		for(mxCell cell : model.getClipboard()){
			if(cell != null & cell.getValue() != null)
				unmarshalCustom(cell.getValue());
		}
//		for (Process process : model.getProcesses()) {
//			parseUserTaskExtension(process.getFlowElements());
//		}
	}
	private static void unmarshalCustom(Object flowElement) {
		if (flowElement instanceof ISynchronization) {
			ISynchronization synchronization = (ISynchronization) flowElement;
			synchronization.unmarshal();
		}
	}
	
	private static void BuildElementMapElement(List<FlowElement> elements, Map<String, BaseElement> elementMap){
		for(FlowElement element : elements){
			elementMap.put(element.getId(), element);
			if (element instanceof SubProcess){
				BuildElementMapElement(((SubProcess)element).getFlowElements(), elementMap);
			}
		}
	}
	
	private static void BuildElementMapConnector(List<FlowElement> elements, Map<String, BaseElement> elementMap){
		for(FlowElement element : elements){
			if (element instanceof SubProcess){
				BuildElementMapConnector(((SubProcess)element).getFlowElements(), elementMap);
			}else if (element instanceof Connector){
				Connector connector = (Connector) element;
				FlowNode srcFlowNode = (FlowNode) (elementMap.get(connector.getSourceRef()));
				FlowNode targetFlowNode = (FlowNode) (elementMap.get(connector.getTargetRef()));
				connector.setSource(srcFlowNode);
				connector.setTarget(targetFlowNode);	
				srcFlowNode.addOutGoing(connector);
				targetFlowNode.addInComing(connector);
			}
		}
		
	}
	private static void BuildElementMap(Bpmn2MemoryModel model) {
		// 构建元素的Map
		Map<String, BaseElement> elementMap = model.getElementMap();
		for (Process process : model.getProcesses()) {
			BuildElementMapElement(process.getFlowElements(), elementMap);
		}
		for (MessageFlow element: model.getCollaboration().getMessageFlows()) {//messageFlow 不在流程中，需要单独处理
			elementMap.put(element.getId(), element);
		}
		
		// 丰富BpmnSequenceFlow的target和source属性
		for (Process process : model.getProcesses()) {
			BuildElementMapConnector(process.getFlowElements(), elementMap);
		}
	}
	/**
	 * 解析xml文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bpmn2MemoryModel reader(String filePath) {
		Reader reader = null;
		try {
			File f = new File(filePath);
			if (!f.exists())
				return null;
			String xmlText = FileUtils.readFileToString(new File(filePath), "UTF-8");
			reader = new StringReader(xmlText);
			return parser(reader);
		} catch (IOException e) {} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {}
		}
		return null;
	}
}
