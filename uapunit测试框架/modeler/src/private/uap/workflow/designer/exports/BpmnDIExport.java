package uap.workflow.designer.exports;

import javax.xml.stream.XMLStreamWriter;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;

import com.mxgraph.model.mxCell;


/***
 * 需要记录的信息 id,mxGeometry,graphStyle,parent,isVertex,targetRef,sourceRef
 * 
 * */
public class BpmnDIExport implements ActivityNamespaceConstants {
	private static XMLStreamWriter xtw;
	private  static Bpmn2MemoryModel model;

	public static void createDIXML(Bpmn2MemoryModel md, XMLStreamWriter inputXtw) throws Exception {
		xtw = inputXtw;
		model=md;
		xtw.writeStartElement("bpmndi", "BPMNDiagram", "http://www.omg.org/spec/BPMN/20100524/DI");
		xtw.writeAttribute("id", "BPMNDiagram_" + model.getMainProcess().getId());
		xtw.writeStartElement("bpmndi", "BPMNPlane", "http://www.omg.org/spec/BPMN/20100524/DI");
		xtw.writeAttribute("bpmnElement", model.getMainProcess().getId());
		for (mxCell cell : md.getClipboard()) {
			writeBpmnElement(cell);
		}
		xtw.writeEndElement();
		xtw.writeEndElement();
	}

	private static void writeBpmnElement(mxCell cell) throws Exception {
		 BaseElement baseElement =(BaseElement) cell.getValue();
		    xtw.writeStartElement("bpmndi", "BPMNShape", "http://www.omg.org/spec/BPMN/20100524/DI");
			xtw.writeStartElement("bpmnElement");
			xtw.writeAttribute("id", baseElement.getId());
			String parentid =cell.getParent().getValue()==null?model.getMainProcess().getId():((BaseElement)(cell.getParent().getValue())).getId();
			xtw.writeAttribute("parentid", parentid);
			xtw.writeAttribute("isVertex", cell.isVertex()?"true":"false");
			if(!cell.isVertex()){
				if(cell.getSource()!=null){
					xtw.writeAttribute("sourceRef", ((BaseElement)cell.getSource().getValue()).getId());
				}
				if(cell.getTarget()!=null){
					xtw.writeAttribute("targetRef", ((BaseElement)cell.getTarget().getValue()).getId());
				}
				
			}
			xtw.writeAttribute("graphStyle",cell.getStyle());
			xtw.writeStartElement("Bounds");
			xtw.writeAttribute("height", cell.getGeometry().getHeight()+"");
			xtw.writeAttribute("width", cell.getGeometry().getWidth()+"");
			xtw.writeAttribute("x", cell.getGeometry().getX()+"");
			xtw.writeAttribute("y", cell.getGeometry().getY()+"");
			xtw.writeEndElement();
			xtw.writeEndElement();
			xtw.writeEndElement();
	}
}
