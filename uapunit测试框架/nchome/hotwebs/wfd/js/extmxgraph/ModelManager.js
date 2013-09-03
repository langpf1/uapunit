ModelManager = function(mxCodec, rootCell)
{
	this.rootCell = rootCell;
	this.mainProcess = null;
	this.mxCodec = mxCodec;
};

ModelManager.prototype.getMainProcess = function()
{
	if(this.mainProcess)
		return this.mainProcess;
	if(!this.rootCell)
		return null;
		
	this.mainProcess = this.getProcess(this.rootCell);
	if(this.mainProcess)
		return this.mainProcess;

	var childCount = this.rootCell.getChildCount();
		
	for (var i = 0; i < childCount; i++)
	{
		var cell = this.rootCell.getChildAt(i);
		this.mainProcess = this.getProcess(cell);
		if(this.mainProcess)
			return this.mainProcess;
	}	
	return null;
}
ModelManager.prototype.getProcess = function(cell)
{
	var value = cell.getValue();
	if(value instanceof Process)
	{
		return value;
	}
	return null;
}

ModelManager.prototype.xmlModelRecursion = function(mxcode, parentCell, node)
{
	var childCount = parentCell.getChildCount();
		
	for (var i = 0; i < childCount; i++)
	{
		var cell = parentCell.getChildAt(i);
		var value = cell.getValue();
		var thisnode = null;
		if(value!= null && value!=undefined && value!="" && (value.toXML instanceof Function))
		{
			thisnode = value.toXML(mxcode);
			node.appendChild(thisnode);
		}
		if(thisnode == null || thisnode == undefined)
			thisnode=node;
		if(cell.getChildCount() > 0)
		{
			this.xmlModelRecursion(mxcode, cell,thisnode);
		}
	}
}
	
ModelManager.prototype.xmlDiagram = function()
{
	var rootBPMNDiagram = this.mxCodec.document.createElement('bpmndi:BPMNDiagram');
	rootBPMNDiagram.setAttribute("id", "BPMNDiagram_" + this.getMainProcess().getId());
	
	var bpmnPlaneNode = this.mxCodec.document.createElement('bpmndi:BPMNPlane');
	bpmnPlaneNode.setAttribute("bpmnElement", this.getMainProcess().getId());
	bpmnPlaneNode.setAttribute("id", "BPMNPlane_" + this.getMainProcess().getId());
	rootBPMNDiagram.appendChild(bpmnPlaneNode);
	
	this.xmlDiagramRecursion(bpmnPlaneNode, this.rootCell);

	return rootBPMNDiagram;
}
	
ModelManager.prototype.xmlDiagramRecursion = function(bpmnPlaneNode,parentCell){
	var bpmnShapeNode;
	var bpmnEdgeNode;
	var childCount = parentCell.getChildCount();
	for (var i = 0; i < childCount; i++)
	{
		var cell = parentCell.getChildAt(i);
		var value = cell.getValue();
		if(value== null || value == undefined || value == ""){
			continue;
		}

		if (value instanceof Connector) {
			var source = cell.source;
			if(source && source.getValue()){
				value.sourceRef = source.getValue().getId();
			}
			var target = cell.target;
			if(target && target.getValue()){
				value.targetRef = target.getValue().getId();
			}
			
			bpmnEdgeNode = this.mxCodec.document.createElement('bpmndi:BPMNEdge');
			bpmnPlaneNode.appendChild(bpmnEdgeNode);
			bpmnEdgeNode.setAttribute("bpmnElement", value.getId());
			bpmnEdgeNode.setAttribute("id", cell.getId());
			bpmnEdgeNode.setAttribute("graphStyle", cell.getStyle());
			
			if(cell.getGeometry()){
				var wayPointNode = this.mxCodec.document.createElement('omgdi:waypoint');
				bpmnEdgeNode.appendChild(wayPointNode);
				wayPointNode.setAttribute("x", cell.getGeometry().x);
				wayPointNode.setAttribute("y", cell.getGeometry().y);
	
				wayPointNode = this.mxCodec.document.createElement('omgdi:waypoint');
				bpmnEdgeNode.appendChild(wayPointNode);
				wayPointNode.setAttribute("x", cell.getGeometry().x);
				wayPointNode.setAttribute("y", cell.getGeometry().y);
			}
				
			if (cell.getParent() && cell.getParent().getValue()) {
				var parent = cell.getParent().getValue();
				bpmnEdgeNode.setAttribute("parentId", parent.getId());
			}
		} else {
			if (!(value instanceof Process)){ 
				bpmnShapeNode = this.mxCodec.document.createElement('bpmndi:BPMNShape');
				bpmnPlaneNode.appendChild(bpmnShapeNode);
	
				if (cell.getParent() && cell.getParent().getValue()) {
					var parent = cell.getParent().getValue();
					bpmnShapeNode.setAttribute("parentId", parent.getId());
				}
				bpmnShapeNode.setAttribute("bpmnElement", value.getId());
				bpmnShapeNode.setAttribute("id", cell.getId());
				
				var style = cell.getStyle();
				if(cell.getChildCount() == 1 && !(cell.getChildAt(0).value)){
					var imageStyle = cell.getChildAt(0).getStyle();
					imageStyle = imageStyle.replace('shape=image;','');
					imageStyle=";"+imageStyle.substring(0,6)+"/themeroot/blue/themeres/control/workflow/"+imageStyle.substring(13,imageStyle.length);
					style = style + imageStyle;
				}
				bpmnShapeNode.setAttribute("graphStyle", style);
	
				if(cell.getGeometry()){
					var bpmnBoundsNode = this.mxCodec.document.createElement('omgdc:Bounds');
					bpmnShapeNode.appendChild(bpmnBoundsNode);
					bpmnBoundsNode.setAttribute("height", cell.getGeometry().height);
					bpmnBoundsNode.setAttribute("width", cell.getGeometry().width);
					bpmnBoundsNode.setAttribute("x", cell.getGeometry().x);
					bpmnBoundsNode.setAttribute("y", cell.getGeometry().y);
				}
			}
		}
		if(cell.getChildCount() > 0)
		{
			this.xmlDiagramRecursion(bpmnPlaneNode, cell);
		}
	} 	
 }		
/**
 * Saves the current graph to database and deploy it.
 */
ModelManager.prototype.toXmlDocument = function()
{
	var root = this.mxCodec.document.createElement('definitions');
	root.setAttribute("targetNamespace", "http://www.activiti.org/test");
	root.setAttribute("xmlns", "http://www.omg.org/spec/BPMN/20100524/MODEL");
	root.setAttribute("xmlns:nc", "http://www.yonyou.com");
	root.setAttribute("xmlns:omgdc", "http://www.omg.org/spec/DD/20100524/DC");
	root.setAttribute("xmlns:bpmndi", "http://www.omg.org/spec/BPMN/20100524/DI");
	root.setAttribute("xmlns:omgdi", "http://www.omg.org/spec/DD/20100524/DI");

	var rootDiagram = this.xmlDiagram();

	this.xmlModelRecursion(this.mxCodec, this.rootCell,root);
	
	root.appendChild(rootDiagram);
	return root;
};

GUID = function()
{
};

GUID.prototype.S4 = function()
{
	return (((1+Math.random())*0x10000)|0).toString(16).substring(1);	
}

GUID.prototype.newGUID = function()
{
	return (this.S4()+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+"-"+this.S4()+this.S4()+this.S4())
}
