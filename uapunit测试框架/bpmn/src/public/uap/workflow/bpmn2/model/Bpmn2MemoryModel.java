package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.modeler.uecomponent.UfGraphCell;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

@XmlRootElement(name = "definitions")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Bpmn2MemoryModel")
public class Bpmn2MemoryModel implements Serializable {
	private static final long serialVersionUID = -2673239621133665376L;
	@XmlElement(name = "process")
	public List<Process> processes = new ArrayList<Process>();
	@XmlTransient
	protected List<mxCell> clipboard = new ArrayList<mxCell>();
	@XmlTransient
	protected Map<String, BaseElement> elementMap = new HashMap<String, BaseElement>();
	@XmlTransient
	protected Map<String, mxCell> cellSort = new HashMap<String, mxCell>();
	@XmlTransient
	protected List<Signal> signals = new ArrayList<Signal>();
	@XmlElement(name = "collaboration")
	protected Collaboration collaboration = new Collaboration();
	@XmlTransient
	protected List<Pool> pools = new ArrayList<Pool>();
	/*
	@XmlAttribute
	public String typeLanguage = "http://www.w3.org/2001/XMLSchema";
	@XmlAttribute
	public String expressionLanguage = "http://www.w3.org/1999/XPath";
	*/
	@XmlAttribute
	public String targetNamespace = "http://www.activiti.org/test";
	@XmlElement(name = "BPMNDiagram",namespace=NameSpaceConst.BPMNDI_URL)
	public BpmnDiagram bpmnDiagram;
	@XmlTransient
	private static Bpmn2MemoryModel instance;
	public Bpmn2MemoryModel() {}
	public Map<String, mxCell> getCellSort() {
		return cellSort;
	}
	public Map<String, BaseElement> getElementMap() {
		return elementMap;
	}
	public List<Process> getProcesses() {
		return processes;
	}
	public void setProcesses(List<Process> processes) {
		this.processes = processes;
	}
	public void addProcess(Process process) {
		this.processes.add(process);
	}
	public void setElementMap(Map<String, BaseElement> elementMap) {
		this.elementMap = elementMap;
	}
/*	public static Bpmn2MemoryModel getInstance() {
		if (instance == null)
			instance = new Bpmn2MemoryModel();
		return instance;
	}*/
	public static Bpmn2MemoryModel newInstance() {
		return instance = new Bpmn2MemoryModel();
	}
	public List<mxCell> getClipboard() {
		return this.clipboard;
	}
	public void setClipboard(List<mxCell> clipboard) {
		this.clipboard = clipboard;
	}
	public List<Signal> getSignals() {
		return this.signals;
	}
	public void setSignals(List<Signal> signals) {
		this.signals = signals;
	}
	public Collaboration getCollaboration() {
		return collaboration;
	}
	public void setCollaboration(Collaboration collaboration) {
		this.collaboration = collaboration;
	}
	public List<Pool> getPools() {
		return this.pools;
	}
	public void setPools(List<Pool> pools) {
		this.pools = pools;
	}
	
	/*
	public String getTypeLanguage() {
		return typeLanguage;
	}
	public void setTypeLanguage(String typeLanguage) {
		this.typeLanguage = typeLanguage;
	}
	public String getExpressionLanguage() {
		return expressionLanguage;
	}
	public void setExpressionLanguage(String expressionLanguage) {
		this.expressionLanguage = expressionLanguage;
	}
	*/
	public String getTargetNamespace() {
		return this.targetNamespace;
	}
	public void setTargetNamespace(String targetNamespace) {
		this.targetNamespace = targetNamespace;
	}
	public BpmnDiagram getBpmnDiagram() {
		return bpmnDiagram;
	}
	public void setBpmnDiagram(BpmnDiagram bpmnDiagram) {
		this.bpmnDiagram = bpmnDiagram;
	}
		
	public static void ClearAll(){
		instance.clipboard.clear();
		instance.processes.clear();
		if (instance.bpmnDiagram!= null){
			instance.bpmnDiagram.getBpmnPlane().bpmnEdges.clear();
			instance.bpmnDiagram.getBpmnPlane().bpmnShapes.clear();
			instance.bpmnDiagram.id = "";
			instance.bpmnDiagram.bpmnPlane.bpmnElement = "";
			instance.bpmnDiagram.bpmnPlane.id = "";
		}
		instance.collaboration.getParticipants().clear();
		instance.collaboration.messageFlows.clear();
		//instance.collaboration.;
		instance.elementMap.clear();
		instance.pools.clear();
		instance.signals.clear();
	}

	
	public Process getMainProcess() {
		if(processes.size()>0)
			return processes.get(0);
		return null;
	}
	
	private static void setParent(Map<String, mxCell> graphInfoMap, List<FlowElement> elements, mxCell parent){
		mxCell cell = null;
		for(FlowElement element : elements){
			cell = graphInfoMap.get(element.getId());
			if(cell == null)
				continue;
			cell.setParent(parent);
			if (element instanceof SubProcess){
				setParent(graphInfoMap, ((SubProcess)element).getFlowElements(), cell);
			}
		}
	}
	
	public static void addToGraphModel(mxGraphComponent graphComponent, Bpmn2MemoryModel model){
		mxGraph graph = graphComponent.getGraph();

		graph.selectAll();
		graph.removeCells();
		
		Map<String, mxCell> graphInfoMap = null;
		try {
			graphInfoMap = model.getCellSort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int i = 0;
		for(Process process : model.getProcesses()){
			if (0 == i++ || process.getId().equalsIgnoreCase("_mainProcess")){
				((mxCell)graph.getDefaultParent()).setValue(process);
				setParent(graphInfoMap, process.getFlowElements(), (mxCell)null);
			}else{
				Pool pool = new Pool();
				mxCell poolCell = graphInfoMap.get(process.getId());
				pool.setId(process.getId());
				pool.setName(process.getName());
				poolCell.setValue(pool);
				
				double laneHeight = poolCell.getGeometry().getHeight() / process.getLanes().size();
				int n = 0;
				for (Lane lane : process.getLanes()){
					mxGeometry geometry = new mxGeometry(mxConstants.DEFAULT_STARTSIZE, laneHeight * n++, 
							poolCell.getGeometry().getWidth()-mxConstants.DEFAULT_STARTSIZE, laneHeight);
					geometry.setRelative(false);
					UfGraphCell laneCell = new UfGraphCell(lane, geometry, "shape=swimlane;horizontal=false");
					laneCell.setId(lane.id);
					laneCell.setVertex(true);
					laneCell.setEdge(false);
					laneCell.setParent(poolCell);
					model.getClipboard().add(model.getClipboard().indexOf(poolCell)+1, laneCell);
					graphInfoMap.put(lane.id, laneCell);
					for(String elementRef : lane.getFlowReferences()){
						mxCell cell = graphInfoMap.get(elementRef);
						if (cell != null){
							graphInfoMap.get(cell.getId()).setParent(laneCell);
						}
					}
				}
			}
		}

		for(MessageFlow element : model.getCollaboration().getMessageFlows()){
			mxCell cell = graphInfoMap.get(element.getId());
			if(cell != null)
				cell.setParent(null);
		}
		
		Object parent = null;
		mxCell[] cells = model.getClipboard().toArray(new mxCell[0]);
		for (mxCell cell : cells) {
			if (cell.getParent() == null){
				parent = graph.getDefaultParent();
			}else{
				parent = cell.getParent();
			}
			i = graph.getModel().getChildCount(parent);
			cell.setParent(null);
			graph.cellsAdded(new Object[]{cell}, parent, i, cell.getSource(), cell.getTarget(), false);
		}
		
		
		graph.setSelectionCell(((mxCell)graph.getDefaultParent()).getChildAt(0));		//Resolve mxGraph bug

	}

	private static void addOutputModel(Bpmn2MemoryModel model, List<FlowElement> elements, mxCell cell){
		if (cell.getValue() instanceof SubProcess){
			SubProcess subProcess = (SubProcess) cell.getValue();
			subProcess.getFlowElements().clear();
			elements.add(subProcess);
			model.getClipboard().add(cell);
			for(int i = 0; i < cell.getChildCount(); i++){
				addOutputModel(model, subProcess.getFlowElements(), (mxCell)cell.getChildAt(i));
			}
		}else if (cell.getValue() instanceof Pool){
			
			Process process = new Process();
			model.getProcesses().add(process);

			Pool pool = (Pool) cell.getValue();
			process.setName(pool.getName());
			process.setId(pool.getId());
			pool.setProcessRef(process.getId());
			Participant participant = new Participant();
			participant.setId("particiant" + pool.getId());
			participant.setProcessRef(pool.getProcessRef());
			model.getCollaboration().getParticipants().add(participant);
			model.getPools().add(pool);
			model.getClipboard().add(cell);
			
			for(int j = 0; j < cell.getChildCount(); j++){
				mxCell laneCell = (mxCell) cell.getChildAt(j);
				if (laneCell.getValue() instanceof Lane){
					Lane lane = (Lane) laneCell.getValue();
					lane.getFlowReferences().clear();
					for(int i = 0; i < laneCell.getChildCount(); i++){
						mxCell tempCell = (mxCell) laneCell.getChildAt(i);
						lane.getFlowReferences().add(((BaseElement)tempCell.getValue()).getId());
						addOutputModel(model, process.getFlowElements(), tempCell);
					}
					process.getLanes().add(lane);
					model.getClipboard().add(cell);
				}else{
					addOutputModel(model, process.getFlowElements(), laneCell);
					model.getClipboard().add(cell);
				}
			}
		}else if(cell.getValue() instanceof MessageFlow){
			model.getCollaboration().getMessageFlows().add((MessageFlow)cell.getValue());
			model.getClipboard().add(cell);
		}else{
			elements.add((FlowElement)cell.getValue());
			model.getClipboard().add(cell);
		}
	}
	
	public static Bpmn2MemoryModel constructOutputModel(mxGraphComponent graphComponent) {
		mxCell root = (mxCell) graphComponent.getGraph().getDefaultParent();
		Bpmn2MemoryModel model = new Bpmn2MemoryModel();
		Process process = (Process) root.getValue();
			model.getProcesses().add(process);
			process.getFlowElements().clear();
			for (int i = 0; i < root.getChildCount(); i++) {
				addOutputModel(model, process.getFlowElements(), (mxCell) root.getChildAt(i));
			}
			return model;
	}
	
}
