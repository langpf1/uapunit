package uap.workflow.modeler.bpmn.bpmn2Diagram;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
public class BpmnParser2Diagram {
	private Bpmn2MemoryModel model;
	private mxGraph graph;
	public BpmnParser2Diagram(Bpmn2MemoryModel model, mxGraph graph) {
		this.model = model;
		this.graph = graph;
	}
	public void parser2Diagram() {
		String filePath = "C:\\MyProcess.bpmn";
		XMLStreamReader xtr = null;
		try {
			File bpmnFile = new File(filePath);
			FileInputStream fileStream = new FileInputStream(bpmnFile);
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = new InputStreamReader(fileStream, "UTF-8");
			xtr = xif.createXMLStreamReader(in);
			// new BpmnParser().parseBpmn(xtr, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (mxCell cell : model.getClipboard()) {
			graph.addCell(cell);
		}
	}
}
