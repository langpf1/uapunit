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
	 *�Ѿ��Ǵ����ģ���ˣ����ڶ�̬����������ģ�͵ĳ��� 
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
			// ���ñ���
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			// �����Ƿ�Ҫ��ʽ�����
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
			// ע�����������ռ���ڲ���
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
			// ���������
			Writer outPut = new StringWriter();
			// ��ʼ���л�
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
	// ����ģ��ʱ���Ѷ������չ����ͬ����Marshaller�ṹ����
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
			// ����һ
			{
				// // ����SaxFactory
				// SAXParserFactory saxFactory = SAXParserFactory.newInstance();
				// // �Ƿ�Ҫ���������ռ�
				// saxFactory.setNamespaceAware(true);
				// // �Ƿ���У��
				// saxFactory.setValidating(true);
				// // ����Saxparser
				// SAXParser saxParser = saxFactory.newSAXParser();
				// {
				// // ����Shema����������
				// saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				// "http://www.w3.org/2001/XMLSchema");
				// // ��ȡshema��Դ·��
				// URL url =
				// ReflectUtil.getResource(BpmnParser.BPMN_20_SCHEMA_LOCATION);
				// // ����shemaУ��
				// saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
				// url.toString());
				// }
				// // ��ȡXmlReader
				// XMLReader xmlReader = saxParser.getXMLReader();
				// // �����SaxSource
				// Source source = new SAXSource(xmlReader, new
				// InputSource(rd));
				// // ��ʼ����
				// object = (Bpmn2MemoryModel) us.unmarshal(source);
			}
			// ����2
			{
				// ��ȡxmlinput����
				XMLInputFactory inputFactory = XMLInputFactory.newInstance();
				// ����XMLStreamReader
				XMLStreamReader streamReader = inputFactory.createXMLStreamReader(rd);
				// ����Reader��ί����
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
				// ��ʼ����
				object = (Bpmn2MemoryModel) us.unmarshal(delegatingStreamReader);
			}
			// ����3
			{
				// // ����DocumentBuilderFactoryu
				// DocumentBuilderFactory dbf =
				// DocumentBuilderFactory.newInstance();
				// dbf.setNamespaceAware(true);
				// // ����DocumentBuilder
				// DocumentBuilder db = dbf.newDocumentBuilder();
				// // ����Document
				// Document doc = db.parse(new InputSource(rd));
				// // ��ʼ����
				// object = (Bpmn2MemoryModel) us.unmarshal(doc);
			}
			{
				// ���ñ���
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				// �����Ƿ�Ҫ��ʽ�����
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
				// ���������ڲ������������Զ��������ռ�,����ط���Ҫ����jaxb-impl-2.0.3.jar
				// ע�����������ռ���ڲ���
				marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
				// ���������
				OutputStream outPut = new ByteArrayOutputStream();
				// ��ʼ���л�
				marshaller.marshal(object, outPut);
				outPut.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��������
	 * 
	 * @param reader
	 * @return
	 */
	public static Bpmn2MemoryModel parser(Reader reader) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(Bpmn2MemoryModel.class.getPackage().getName());
			Unmarshaller us = jc.createUnmarshaller();
			// ��ȡxmlinput����
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// ����XMLStreamReader
			XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
			// ����Reader��ί����
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
			// ��ʼ����
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
	// parseģ��ʱ����unmarshaller�ṹ������չ����ͬ����������
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
		// ����Ԫ�ص�Map
		Map<String, BaseElement> elementMap = model.getElementMap();
		for (Process process : model.getProcesses()) {
			BuildElementMapElement(process.getFlowElements(), elementMap);
		}
		for (MessageFlow element: model.getCollaboration().getMessageFlows()) {//messageFlow ���������У���Ҫ��������
			elementMap.put(element.getId(), element);
		}
		
		// �ḻBpmnSequenceFlow��target��source����
		for (Process process : model.getProcesses()) {
			BuildElementMapConnector(process.getFlowElements(), elementMap);
		}
	}
	/**
	 * ����xml�ļ�
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
