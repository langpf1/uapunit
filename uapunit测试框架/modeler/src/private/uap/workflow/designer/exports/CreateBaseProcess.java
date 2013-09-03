package uap.workflow.designer.exports;

import java.util.UUID;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.SequenceFlow;
import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.uecomponent.GraphElementMeta;
import uap.workflow.modeler.uecomponent.IGraphCellLib;
import uap.workflow.modeler.uecomponent.UfGraphCell;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import nc.bs.logging.Logger;

public class CreateBaseProcess {
	/*
	 * 创建基本流程
	 */
	private static mxCell createEdge(String className, String id, mxCell source, mxCell target, String style) {
		SequenceFlow obj = null;
		try {
			obj = (SequenceFlow) Class.forName(className).newInstance();
			obj.setId(id);
			obj.setSourceRef(source.getId());
			obj.setTargetRef(target.getId());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		mxCell edge = new UfGraphCell(obj, new mxGeometry(), style);
		edge.setSource(source);
		edge.setTarget(target);
		edge.setId(id);
		edge.setEdge(true);
		edge.getGeometry().setRelative(true);
		return edge;
	}

	private static mxCell createVertex(String name, String className, String id, double x, double y, double width, double height, String style,
			boolean relative)
	{
		FlowElement obj = null;
		try {
			obj = (FlowElement) Class.forName(className).newInstance();
			obj.setId(id);
			obj.setName(name);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		mxGeometry geometry = new mxGeometry(x, y, width, height);
		geometry.setRelative(relative);
		mxCell vertex = new UfGraphCell(obj, geometry, style);
		vertex.setId(id);
		vertex.setVertex(true);
		vertex.setConnectable(true);
		return vertex;
	}
	
	private static GraphElementMeta retriveGraphElementMeta(GraphElementMeta[] metas, String cellType){
		try {
			for (GraphElementMeta meta : metas) {
				if (meta.getName().equals(cellType))
					return meta;
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}		
		return null;
	}
	public static Bpmn2MemoryModel provideBaseProcess(String objectType, String procName, String procDescription){
		
		Bpmn2MemoryModel model = new Bpmn2MemoryModel();

		IGraphCellLib o = (IGraphCellLib) new BpmnCellLib();
		GraphElementMeta[] metas = o.getShapeCellsMeta();
		double x = 100, y = 100;

		GraphElementMeta meta = retriveGraphElementMeta(metas, "Start Event");
		mxCell startEvent = createVertex(meta.getName(),meta.getUserObjectClass(), "_1", x, y+9, meta.getWidth(), meta.getHeight(),meta.getStyle(),false);

		meta = retriveGraphElementMeta(metas, "User Task");
		mxCell userTask = createVertex(meta.getName(), meta.getUserObjectClass(), "_2", x+200, y, meta.getWidth(), meta.getHeight(),meta.getStyle(),false);

		meta = retriveGraphElementMeta(metas, "Sequence Flow");
		mxCell sequenceFlow1 = createEdge(meta.getUserObjectClass(), "_3", startEvent, userTask, meta.getStyle());

		meta = retriveGraphElementMeta(metas, "End Event");
		mxCell endEvent = createVertex(meta.getName(),meta.getUserObjectClass(), "_4", x+400, y+9, meta.getWidth(), meta.getHeight(),meta.getStyle(),false);
		
		meta = retriveGraphElementMeta(metas, "Sequence Flow");
		mxCell sequenceFlow2 = createEdge(meta.getUserObjectClass(), "_5", userTask, endEvent, meta.getStyle());
		
		Process process = new Process();
		process.setObjectType(objectType);
		if (procName == null || procName.equals(""))
			process.setName("dynamic Process");
		else
			process.setName(procName);
		
		if (procDescription == null || procDescription.equals(""))
			process.setDocumentation("dynamic process description");
		else
			process.setDocumentation(procDescription);
		
		process.setId(UUID.randomUUID().toString());
		model.getProcesses().add(process);
		
		process.getFlowElements().add((FlowElement)startEvent.getValue());
		process.getFlowElements().add((FlowElement)userTask.getValue());
		process.getFlowElements().add((FlowElement)endEvent.getValue());
		process.getFlowElements().add((FlowElement)sequenceFlow1.getValue());
		process.getFlowElements().add((FlowElement)sequenceFlow2.getValue());
		
		model.getClipboard().add(startEvent);
		model.getClipboard().add(userTask);
		model.getClipboard().add(endEvent);
		model.getClipboard().add(sequenceFlow1);
		model.getClipboard().add(sequenceFlow2);
		
		return model;
	}	
	
	
}
