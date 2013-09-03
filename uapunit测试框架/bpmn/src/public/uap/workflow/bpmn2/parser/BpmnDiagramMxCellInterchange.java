package uap.workflow.bpmn2.parser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.BpmnBounds;
import uap.workflow.bpmn2.model.BpmnDiagram;
import uap.workflow.bpmn2.model.BpmnEdge;
import uap.workflow.bpmn2.model.BpmnPlane;
import uap.workflow.bpmn2.model.BpmnShape;
import uap.workflow.bpmn2.model.BpmnWayPoint;
import uap.workflow.bpmn2.model.Connector;
import uap.workflow.bpmn2.model.GraphicInfo;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
/*
 * BpmnDiagram ºÍmxCellÏà»¥×ª»»
 */
public class BpmnDiagramMxCellInterchange {
	public static void setBpmnDiagram(Bpmn2MemoryModel model) throws Exception {
		BpmnDiagram bpmnDiagram = new BpmnDiagram();
		model.setBpmnDiagram(bpmnDiagram);
		bpmnDiagram.setId("BPMNDiagram_" + model.getMainProcess().getId());
		BpmnPlane bpmnPlane = new BpmnPlane();
		bpmnDiagram.setBpmnPlane(bpmnPlane);
		bpmnPlane.setBpmnElement(model.getMainProcess().getId());
		bpmnPlane.setId("BPMNPlane_" + bpmnPlane.getBpmnElement());
		BpmnShape bpmnShape;
		BpmnEdge bpmnEdge;
		List<String> list=new ArrayList<String>();
		for (mxCell cell : model.getClipboard()) {
			BaseElement baseElement = (BaseElement) cell.getValue();
			if(baseElement==null){
				continue;
			}
			if(list.contains(baseElement.getId())){
				continue;
			}else{
				list.add(baseElement.getId());
			}
			if (baseElement instanceof Connector) {
				bpmnEdge = new BpmnEdge();
				bpmnPlane.getBpmnEdges().add(bpmnEdge);
				bpmnEdge.setBpmnElement(baseElement.getId());
				bpmnEdge.setId("BPMNEdge_" + baseElement.getId());
				bpmnEdge.setGraphStyle(cell.getStyle());
				BpmnWayPoint wayPoint = new BpmnWayPoint();
				bpmnEdge.getWaypoints().add(wayPoint);
				wayPoint.setX((int) (cell.getGeometry().getX()));
				wayPoint.setY((int) (cell.getGeometry().getY()));
				wayPoint = new BpmnWayPoint();
				bpmnEdge.getWaypoints().add(wayPoint);
				wayPoint.setX((int) (cell.getGeometry().getX()));
				wayPoint.setY((int) (cell.getGeometry().getY()));
				if (cell.getParent() != null && cell.getParent().getValue() != null) {
					BaseElement tmpBase=(BaseElement)cell.getParent().getValue();
					bpmnEdge.setParentId(tmpBase.getId());
				}
			} else {
				bpmnShape = new BpmnShape();
				bpmnPlane.getBpmnShapes().add(bpmnShape);
				if (cell.getParent() != null && cell.getParent().getValue() != null) {
					BaseElement tmpBase=(BaseElement)cell.getParent().getValue();
					bpmnShape.setParentId(tmpBase.getId());
				}
				bpmnShape.setBpmnElement(baseElement.getId());
				bpmnShape.setId("BPMNShape_" + baseElement.getId());
				// String parentid = cell.getParent().getValue() == null ?
				// model.getMainProcess().getId()
				// : ((BaseElement) (cell.getParent().getValue())).getId();
				// bpmnShape.setParentid(parentid);
				//cell.setStyle(cell.getStyle().replaceFirst("/themeroot/blue/themeres/control/workflow/icons/", "icons/"));
				bpmnShape.setGraphStyle(cell.getStyle());
				BpmnBounds bpmnBounds = new BpmnBounds();
				bpmnShape.setBounds(bpmnBounds);
				bpmnBounds.setHeight((int) (cell.getGeometry().getHeight()));
				bpmnBounds.setWidth((int) (cell.getGeometry().getWidth()));
				bpmnBounds.setX((int) (cell.getGeometry().getX()));
				bpmnBounds.setY((int) (cell.getGeometry().getY()));
			}
		}
	}

	public static void constructCellMap(Bpmn2MemoryModel model)
			throws Exception {
		model.getClipboard().clear();
		BpmnDiagram bpmnDiagram = model.bpmnDiagram;
		if (bpmnDiagram == null)
			return ;
		BpmnPlane bpmnPlane = bpmnDiagram.getBpmnPlane();
		if (bpmnPlane == null)
			return ;
		List<BpmnShape> bpmnShapes = bpmnPlane.getBpmnShapes();
		if (bpmnShapes == null)
			return ;
		Map<String, mxCell> cellMap = model.getCellSort();
		for (BpmnShape bpmnShape : bpmnShapes) {
			GraphicInfo graphicInfo = new GraphicInfo();
			graphicInfo.graphStyle = bpmnShape.getGraphStyle();
			//if(graphicInfo.graphStyle.indexOf("/themeroot/blue/themeres/control/workflow/icons/")==-1)
			//graphicInfo.graphStyle=graphicInfo.graphStyle.replace("icons/", "/themeroot/blue/themeres/control/workflow/icons/");
			graphicInfo.isVertex = true;
			graphicInfo.id = bpmnShape.getBpmnElement();
			BpmnBounds bpmnBounds = bpmnShape.getBounds();
			graphicInfo.x = bpmnBounds.getX();
			graphicInfo.y = bpmnBounds.getY();
			graphicInfo.height = bpmnBounds.getHeight();
			graphicInfo.width = bpmnBounds.getWidth();
			mxGeometry geometry = new mxGeometry(graphicInfo.x, graphicInfo.y,
					graphicInfo.width, graphicInfo.height);
			geometry.setRelative(false);
			mxCell cell = new uap.workflow.modeler.uecomponent.UfGraphCell(model
					.getElementMap().get(graphicInfo.id), geometry,
					graphicInfo.graphStyle);
			cellMap.put(graphicInfo.id, cell);
			cell.setId(graphicInfo.id);
			cell.setVertex(graphicInfo.isVertex);
			cell.setEdge(false);
			model.getClipboard().add(cell);
		}
		for (BpmnShape bpmnShape : bpmnShapes) {
			String parnetId = bpmnShape.getParentId();
			String id = bpmnShape.getBpmnElement();

			if (parnetId != null && parnetId.length() != 0) {
				mxCell cell = cellMap.get(id);
				cell.setParent(cellMap.get(parnetId));
			}
		}
		List<BpmnEdge> bpmnEdges = bpmnPlane.getBpmnEdges();
		if (bpmnEdges == null)
			return ;
		for (BpmnEdge bpmnEdge : bpmnEdges) {
			GraphicInfo graphicInfo = new GraphicInfo();
			graphicInfo.graphStyle = bpmnEdge.getGraphStyle();
			graphicInfo.isVertex = false;
			graphicInfo.id = bpmnEdge.getBpmnElement();
			graphicInfo.x = 0;
			graphicInfo.y = 0;
			graphicInfo.height = 0;
			graphicInfo.width = 0;
			mxGeometry geometry = new mxGeometry(graphicInfo.x, graphicInfo.y,
					graphicInfo.width, graphicInfo.height);
			geometry.setRelative(true);
			mxCell cell = new uap.workflow.modeler.uecomponent.UfGraphCell(model
					.getElementMap().get(graphicInfo.id), geometry,
					graphicInfo.graphStyle);
			cellMap.put(graphicInfo.id, cell);
			cell.setId(graphicInfo.id);
			cell.setVertex(graphicInfo.isVertex);
			cell.setEdge(true);
			BaseElement element = model.getElementMap().get(
					bpmnEdge.getBpmnElement());
			if (element instanceof Connector) {
				Connector connector = (Connector) element;
				if(cellMap.get(connector.sourceRef) != null)
				{
					cellMap.get(connector.sourceRef).insertEdge(cell, true);
					cell.setSource(cellMap.get(connector.sourceRef));
				}
				if(cellMap.get(connector.targetRef) != null)
				{
					cellMap.get(connector.targetRef).insertEdge(cell, false);
					cell.setTarget(cellMap.get(connector.targetRef));
				}
			}
			cell.setValue(element);
			model.getClipboard().add(cell);
		}
		for (BpmnEdge bpmnEdge : bpmnEdges) {
			String parnetId = bpmnEdge.getParentId();
			String id = bpmnEdge.getBpmnElement();

			if (parnetId != null && parnetId.length() != 0) {
				mxCell cell = cellMap.get(id);
				cell.setParent(cellMap.get(parnetId));
			}
		}
	}
}
