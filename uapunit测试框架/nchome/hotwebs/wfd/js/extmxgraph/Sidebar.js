/**
 * $Id: Sidebar.js,v 1.67 2012-07-19 19:09:23 gaudenz Exp $
 * Copyright (c) 2006-2012, JGraph Ltd
 */
/**
 * Construcs a new sidebar for the given editor.
 */
function Sidebar(editorUi, container)
{
	this.editorUi = editorUi;
	this.container = container;
	this.palettes = new Object();
	this.showTooltips = true;
	this.graph = new Graph(document.createElement('div'), null, null, this.editorUi.editor.graph.getStylesheet());
	this.graph.foldingEnabled = false;
	this.graph.autoScroll = false;
	this.graph.setTooltips(false);
	this.graph.setConnectable(false);
	this.graph.resetViewOnRootChange = false;
	this.graph.view.setTranslate(this.thumbBorder, this.thumbBorder);
	this.graph.setEnabled(false);
    this.mydiv;
    this.groupsContainer;
    this.commonUsed;
	// Workaround for VML rendering in IE8 standards mode where the container must be in the DOM
	// so that VML references can be restored via document.getElementById in mxShape.init.
	if (document.documentMode == 8)
	{
		document.body.appendChild(this.graph.container);
	}
	
	// Workaround for no rendering in 0 coordinate in FF 10
	if (this.shiftThumbs)
	{
		this.graph.view.canvas.setAttribute('transform', 'translate(1, 1)');
	}
	
	if (!mxClient.IS_TOUCH)
	{
		mxEvent.addListener(document, 'mouseup', mxUtils.bind(this, function()
		{
			this.showTooltips = true;
		}));
	
		// Enables tooltips after scroll
		mxEvent.addListener(container, 'scroll', mxUtils.bind(this, function()
		{
			this.showTooltips = true;
		}));
		
		mxEvent.addListener(document, 'mousedown', mxUtils.bind(this, function()
		{
			this.showTooltips = false;
			this.hideTooltip();
		}));

		mxEvent.addListener(document, 'mousemove', mxUtils.bind(this, function(evt)
		{
			var src = mxEvent.getSource(evt);
			
			while (src != null)
			{
				if (src == this.currentElt)
				{
					return;
				}
				
				src = src.parentNode;
			}
			
			this.hideTooltip();
		}));

		// Handles mouse leaving the window
		mxEvent.addListener(document, 'mouseout', mxUtils.bind(this, function(evt)
		{
			if (evt.toElement == null && evt.relatedTarget == null)
			{
				this.hideTooltip();
			}
		}));
	}
	
	this.init();
	
	// Pre-fetches tooltip image
	new Image().src = IMAGE_PATH + '/tooltip.png';
};

/**
 * Adds all palettes to the sidebar.
 */
Sidebar.prototype.init = function()
{
	this.mydiv = document.createElement('div');
	this.mydiv.id='tabs';
	this.mydiv.setAttribute("id","tabs");
	this.mydiv.setAttribute("class","easyui-tabs"); 
	this.mydiv.style.width="185px";
    this.mydiv.style.height="400px"; 
    this.groupsContainer=document.createElement('div');
    this.groupsContainer.setAttribute("title","Groups");
    this.commonUsed=document.createElement('div');
    this.commonUsed.setAttribute("title","commonUsed");
    this.mydiv.appendChild(this.commonUsed);
    this.mydiv.appendChild(this.groupsContainer);
    this.container.appendChild(this.mydiv);
   
     var commonUsedInner=document.createElement('div');
     var value=new StartEvent();
	 this.addNode(commonUsedInner,value,'Y',36, 36, 'shape=event;fontFamily=宋体;fontSize=12;fillColor=#8CB032;gradientColor=#CCFF00','shape=image;image=images/cons/activity/list/', 8, 10, 0, 0, 17, 14); 
     value=new UserTask();
	 this.addNode(commonUsedInner,value,'Y',90, 55, 'shape=task;shadow=1;rounded=1;fontFamily=宋体;fontSize=12;fillColor=#1496F7;gradientColor=#AAD9F1;','shape=image;image=images/icons/activity/list/type.user.png;', 5, 5, 0, 0, 26, 20); 
     value=new ParallelGateway();
	 this.addNode(commonUsedInner,value,'Y',50, 50, 'shape=gateway;fontFamily=宋体;fontSize=12;fillColor=#FFFF00;gradientColor=#FFFF00','shape=image;image=images/icons/gateway/list/eventbased.parallel.png;', 16, 16, 0, 0, 17, 17); 
     value=new EndEvent();
	 this.addNode(commonUsedInner,value,'Y',36, 36, 'shape=event;strokeWidth=3;fontFamily=宋体;fontSize=12;fillColor=#EE3D3D;gradientColor=#FF9999','shape=image;image=images/icons/endevent/inner/', 9, 10, 0, 0, 17, 16); 
	 
	 this.commonUsed.appendChild(commonUsedInner);
	 
	var dir = STENCIL_PATH;
	this.addAllPalette();
	//this.addTestShapePalette(false);
	//this.addBpmnPalette(dir, false);
};

/**
 * Specifies if tooltips should be visible. Default is true.
 */
Sidebar.prototype.enableTooltips = !mxClient.IS_TOUCH;

/**
 * Shifts the thumbnail by 1 px.
 */
Sidebar.prototype.shiftThumbs = mxClient.IS_SVG || document.documentMode == 8;

/**
 * Specifies the delay for the tooltip. Default is 16 px.
 */
Sidebar.prototype.tooltipBorderHorizontal = 50;
Sidebar.prototype.tooltipBorderVertical= 20;
/**
 * Specifies the delay for the tooltip. Default is 2 px.
 */
Sidebar.prototype.thumbBorder = 2;

/**
 * Specifies the delay for the tooltip. Default is 300 ms.
 */
Sidebar.prototype.tooltipDelay = 300;

/**
 * Specifies if edges should be used as templates if clicked. Default is true.
 */
Sidebar.prototype.installEdges = true;

/**
 * Specifies the URL of the gear image.
 */
Sidebar.prototype.gearImage = STENCIL_PATH + '/clipart/Gear_128x128.png';

/**
 * Specifies the width of the thumbnails.
 */
Sidebar.prototype.thumbWidth = 34;

/**
 * Specifies the height of the thumbnails.
 */
Sidebar.prototype.thumbHeight = 30;

/**
 * 显示预览
 */
Sidebar.prototype.showTooltip = function(elt, cells)
{
	//简化label
	for(var i=0;i<cells.length;i++)
	{
	   if(!(cells[i].getValue() instanceof Object)&&cells[i].getValue().substring(0,12)=="Intermediate")
		 cells[i].setValue(cells[i].getValue().substring(12,cells[i].getValue().length-5));
	   else if(cells[i].getValue()=='Pool')
	     cells[i].setValue("Process");
	   else if(cells[i].getValue()=='Lane')
		 cells[i].setValue("Participant");
	}
	//显示label
	for(var i=0;i<cells.length;i++)
	{
		if(cells[i].getStyle().indexOf("noLabel=0")==-1)
		{
		  if(cells[i].getStyle().indexOf("noLabel=1")!=-1)
		     cells[i].setStyle(cells[i].getStyle().replace("noLabel=1","noLabel=0"));
		  else
		     cells[i].setStyle(cells[i].getStyle()+";noLabel=0");
		}
	}
	
	if (this.enableTooltips && this.showTooltips)
	{
		if (this.currentElt != elt)
		{
			if (this.thread != null)
			{
				window.clearTimeout(this.thread);
				this.thread = null;
			}
			
			var show = mxUtils.bind(this, function()
			{
				// Workaround for off-screen text rendering in IE
				var old = mxText.prototype.getTableSize;
				
				if (this.graph.dialect != mxConstants.DIALECT_SVG)
				{
					mxText.prototype.getTableSize = function(table)
					{
						var oldParent = table.parentNode;
						
						document.body.appendChild(table);
						var size = new mxRectangle(0, 0, table.offsetWidth, table.offsetHeight);
						oldParent.appendChild(table);
						
						return size;
					};
				}
				
				// Lazy creation of the DOM nodes and graph instance
				if (this.tooltip == null)
				{
					this.tooltip = document.createElement('div');  //生成tooltip容器
					this.tooltip.className = 'geSidebarTooltip';
					document.body.appendChild(this.tooltip);
					
					this.graph2 = new Graph(this.tooltip, null, null, this.editorUi.editor.graph.getStylesheet());  //生成graph显示tooltip
					this.graph2.view.setTranslate(this.tooltipBorderHorizontal, this.tooltipBorderVertical);
					this.graph2.resetViewOnRootChange = false;
					this.graph2.foldingEnabled = true;
					this.graph2.autoScroll = false;
					this.graph2.setTooltips(false);
					this.graph2.setConnectable(false);
					this.graph2.setEnabled(false);
					
					this.tooltipImage = mxUtils.createImage(IMAGE_PATH + '/tooltip.png');  //指定tooltip箭头形状
					this.tooltipImage.style.position = 'absolute';
					this.tooltipImage.style.width = '14px';
					this.tooltipImage.style.height = '27px';
					
					document.body.appendChild(this.tooltipImage);				
				}
				this.graph2.model.clear();
				this.graph2.addCells(cells);  //在tooltip中显示cells
				//计算tooltip长和宽
				var bounds = this.graph2.getGraphBounds();
				var width = bounds.x + bounds.width + this.tooltipBorderHorizontal;
				var height = bounds.y + bounds.height + this.tooltipBorderVertical;
				
				if (mxClient.IS_QUIRKS)
				{
					width += 4;
					height += 4;
				}
				//指定tooltip样式，大小，位置
				this.tooltip.style.display = 'block';
				this.tooltip.style.overflow = 'visible';
				this.tooltipImage.style.visibility = 'visible';
				this.tooltip.style.width = width + 'px';
				this.tooltip.style.height = height + 'px';
		
				var left = this.mydiv.clientWidth + this.editorUi.splitSize + 3;
				var top = Math.max(0, (this.groupsContainer.offsetTop+this.mydiv.offsetTop + elt.offsetTop - this.mydiv.scrollTop-this.groupsContainer.scrollTop - height / 2 + 120));
                if(navigator.userAgent.indexOf("Firefox")>0||navigator.userAgent.indexOf("Safari")>0)
	            {
                    top =top-50;
		        }
				// Workaround for ignored position CSS style in IE9
				// (changes to relative without the following line)
				this.tooltip.style.position = 'absolute';
				this.tooltip.style.left = left + 'px';
				this.tooltip.style.top = top + 'px';
				this.tooltipImage.style.left = (left - 13) + 'px';
				this.tooltipImage.style.top = (top + height / 2 - 13) + 'px';
				
				mxText.prototype.getTableSize = old;
			});

			if (this.tooltip != null && this.tooltip.style.display != 'none')
			{
				show();
			}
			else
			{
				this.thread = window.setTimeout(show, this.tooltipDelay);
			}

			this.currentElt = elt;
		}
	}
};

/**
 * Hides the current tooltip.
 */
Sidebar.prototype.hideTooltip = function()
{
	if (this.thread != null)
	{
		window.clearTimeout(this.thread);
		this.thread = null;
	}
	
	if (this.tooltip != null)
	{
		this.tooltip.style.display = 'none';
		this.tooltipImage.style.visibility = 'hidden';
		this.currentElt = null;
	}
};


/**
 * Adds all palettes to the sidebar.
 */
Sidebar.prototype.addAllPalette = function()
{
	var xmlDoc = mxUtils.load("js/extmxgraph/notationConfig.xml");
   	var root = xmlDoc.getDocumentElement();
	var font = root.getElementsByTagName("defaultfont")[0].getAttribute("value");
	
	this.parseConfig(root,font);
	
	
	this.addOtherPalette(null);    //加载service palette
};

//Adds Business Service palette
Sidebar.prototype.addOtherPalette = function(type){
	this.addPalette('Business Service', 'Business Service', false, mxUtils.bind(this, function(content){}));

	//var metadataRoot = $('#metadataRoot'); 
	//metadataRoot.tree('loadData', json);

}

//增加流程元素
Sidebar.prototype.insertAnnotation = function(content,annotation, font,  iconPath, color,offsetx,offsety,shapex,shapey,shapeWidth,shapeHeight){
	this.addNode(content, 
		annotation.value,
		annotation.isVertex,
		annotation.width, 
		annotation.height, 
		annotation.style+font+";"+color+";",
		annotation.innerStyle+";image="+iconPath+annotation.icon+";",
		offsetx,offsety,shapex,shapey,shapeWidth,shapeHeight);
}

//解析notationConfig.xml
Sidebar.prototype.parseConfig = function(root, font){
	var node = root.getElementsByTagName("notationsGroup")[0];
	var i=0;
	while(node){
		this.parseGroup(node,font);
		node = root.getElementsByTagName("notationsGroup")[++i];
	}
}

//解析流程元素组
Sidebar.prototype.parseGroup = function(group, font){
	var anotation = new Object();
	var iconPath = group.getAttribute("icon_path");  //图标路径
	var color = group.getAttribute("color");        //cell颜色
	var name= group.getAttribute("name");           //palette名称
	var offsetx=group.getAttribute("offsetx");      //图标水平偏移
	var offsety=group.getAttribute("offsety");      //图标垂直偏移
	var shapex=group.getAttribute("shapex");
	var shapey=group.getAttribute("shapey");
	var shapeWidth=group.getAttribute("shapeWidth");  //图标宽
	var shapeHeight=group.getAttribute("shapeHeight"); //图标高
	var expand=group.getAttribute("expand");        //是否默认展开
	var groupExpand=true ;
	if(expand!="Y")
		groupExpand=false;
	var node = group.getElementsByTagName("notation")[0];
	var i=0;
	if(this.editorUi.style!="approve"||(this.editorUi.style=="approve"&&group.getAttribute("approve")=="Y"))  //判断当前流程编辑器是否为审批版
	this.addPalette(name, name, groupExpand, mxUtils.bind(this, function(content){    //新建palette
		while (node){
			anotation.name = node.getAttribute("name");   //cell名称
			anotation.value = node.getAttribute("value");   //cell value属性
			anotation.style = node.getAttribute("style");   //cell样式
			anotation.innerStyle = node.getAttribute("innerStyle");//图标样式
			anotation.icon = node.getAttribute("icon");   //图标选用图片名称
			anotation.width = node.getAttribute("width");  //cell宽
			anotation.height = node.getAttribute("height");  //cell高
			anotation.isVertex = node.getAttribute("isVertex");  //cell是否为节点
			if(anotation.name=="Sub-Process"||anotation.name=="Event Subprocess")  //判断cell是否为subProcess，若为则图标在右上角
			{
				var offsetxSubProcess=node.getAttribute("offsetx");
	            var offsetySubProcess=node.getAttribute("offsety");
	            var shapeWidthSubProcess=node.getAttribute("shapeWidth");
	            var shapeHeightSubProcess=node.getAttribute("shapeHeight");
			}
			if(this.editorUi.style!="approve"||(this.editorUi.style=="approve"&&node.getAttribute("approve")=="Y"))//判断当前流程编辑器是否为审批版
			{
				if(anotation.name=="Sub-Process"||anotation.name=="Event Subprocess")
				   this.insertAnnotation(content,anotation, font, iconPath, color,parseInt(offsetxSubProcess),parseInt(offsetySubProcess),parseInt(shapex),parseInt(shapey),parseInt(shapeWidthSubProcess),parseInt(shapeHeightSubProcess));
				else
		          this.insertAnnotation(content,anotation, font, iconPath, color,parseInt(offsetx),parseInt(offsety),parseInt(shapex),parseInt(shapey),parseInt(shapeWidth),parseInt(shapeHeight));
			}
			node = group.getElementsByTagName("notation")[++i];
		}
    }));
}

/**
 *//*
Sidebar.prototype.addTestShapePalette = function(expand)
{
	this.addPalette('testShape', 'TestShape', expand, mxUtils.bind(this, function(content)
	{
		this.addNode(content, 'cube', 140, 60, 'shape=cube', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'note', 140, 60, 'shape=note', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'folder', 140, 60, 'shape=folder', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'card', 140, 60, 'shape=card', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'tape', 140, 60, 'shape=tape', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'step', 140, 60, 'shape=step', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'plus', 140, 60, 'shape=plus', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'message', 140, 60, 'shape=message', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'umlActor', 140, 60, 'shape=umlActor', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'lollipop', 140, 60, 'shape=lollipop', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'component', 140, 60, 'shape=component', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'endState', 140, 60, 'shape=endState', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'startState', 140, 60, 'shape=startState', 5, 5, 0, 0, 20, 14);
		//this.addNode(content, 'link', 140, 60, 'shape=link', 5, 5, 0, 0, 20, 14);

		//this.addNode(content, 'arrow', 140, 60, 'shape=arrow', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'rectangle', 140, 60, 'shape=rectangle', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'ellipse', 140, 60, 'shape=ellipse', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'doubleEllipse', 140, 60, 'shape=doubleEllipse', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'rhombus', 140, 60, 'shape=rhombus', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'image', 140, 60, 'shape=image;image=' + 'images\\icons\\activity\\type.send.png', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'line', 140, 60, 'shape=line', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'label', 140, 60, 'shape=label', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'cylinder', 140, 60, 'shape=cylinder', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'swimlane', 140, 60, 'shape=swimlane', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'connector', 140, 60, 'shape=connector', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'actor', 140, 60, 'shape=actor', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'cloud', 140, 60, 'shape=cloud', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'triangle', 140, 60, 'shape=triangle', 5, 5, 0, 0, 20, 14);
		this.addNode(content, 'hexagon', 140, 60, 'shape=hexagon', 5, 5, 0, 0, 20, 14);
	}));
};*/

/**
 * 增加流程元素
 */
Sidebar.prototype.addNode = function(content, value,isVertex, width, height, style,innerStyle, offsetx, offsety, shapex, shapey, shapeWidth, shapeHeight)
{

    if(isVertex=="N")  //新增连线类型
    {
    	var assoc = new mxCell(value, new mxGeometry(0, 0, 0, 0), style);
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
		content.appendChild(this.createEdgeTemplateFromCells([assoc], width, height));
    }
    else{              //新增节点类型
      var classCell = new mxCell(value, new mxGeometry(0, 0, parseInt(width), parseInt(height)), style);
      classCell.vertex = true;
	  if(innerStyle.indexOf(".png")>0)
	  {
	   var classCell1 = new mxCell("", new mxGeometry(shapex, shapey, shapeWidth, shapeHeight),innerStyle);
	   classCell1.vertex = true;
	   classCell1.connectable = false;
	   classCell1.geometry.relative = true;
	   classCell1.geometry.offset = new mxPoint(offsetx, offsety);
	   classCell.insert(classCell1);
	  }
	  content.appendChild(this.createVertexTemplateFromCells([classCell], width, height));
    }
};

/**
 * Adds the BPMN library to the sidebar.
 *//*
Sidebar.prototype.addBpmnPalette = function(dir, expand)
{
	this.addStencilPalette('bpmn', 'BPMN', dir + '/bpmn.xml',
		';fillColor=#ffffff;strokeColor=#000000;perimeter=ellipsePerimeter;',
		['Cancel', 'Error', 'Link', 'Message', 'Compensation', 'Multiple', 'Rule', 'Timer'],
		function(content)
		{
			content.appendChild(this.createVertexTemplate('swimlane;horizontal=0;', 300, 160, 'Pool'));
		
			var classCell = new mxCell('Process', new mxGeometry(0, 0, 140, 60),
		    	'rounded=1');
			classCell.vertex = true;
			var classCell1 = new mxCell('', new mxGeometry(1, 1, 30, 30), 'shape=mxgraph.bpmn.timer_start;perimeter=ellipsePerimeter;');
			classCell1.vertex = true;
			classCell1.geometry.relative = true;
			classCell1.geometry.offset = new mxPoint(-40, -15);
			classCell.insert(classCell1);
			
			content.appendChild(this.createVertexTemplateFromCells([classCell], 140, 60));
			
			var classCell = new mxCell('Process', new mxGeometry(0, 0, 140, 60),
		    	'rounded=1');
			classCell.vertex = true;
			var classCell1 = new mxCell('', new mxGeometry(0.5, 1, 12, 12), 'shape=plus');
			classCell1.vertex = true;
			classCell1.connectable = false;
			classCell1.geometry.relative = true;
			classCell1.geometry.offset = new mxPoint(-6, -12);
			classCell.insert(classCell1);
			
			content.appendChild(this.createVertexTemplateFromCells([classCell], 140, 60));
			
			var classCell = new mxCell('Process', new mxGeometry(0, 0, 140, 60),
		    	'rounded=1');
			classCell.vertex = true;
			var classCell1 = new mxCell('', new mxGeometry(0, 0, 20, 14), 'shape=message');
			classCell1.vertex = true;
			classCell1.connectable = false;
			classCell1.geometry.relative = true;
			classCell1.geometry.offset = new mxPoint(5, 5);
			classCell.insert(classCell1);
			
			content.appendChild(this.createVertexTemplateFromCells([classCell], 140, 60));
			
		    var classCell = new mxCell('', new mxGeometry(0, 0, 60, 40), 'shape=message');
	    	classCell.vertex = true;
	
			content.appendChild(this.createEdgeTemplateFromCells([classCell], 60, 40));
	
			var assoc = new mxCell('Sequence', new mxGeometry(0, 0, 0, 0), 'endArrow=block;endFill=1;endSize=6');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
	
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
			
			var assoc = new mxCell('Default', new mxGeometry(0, 0, 0, 0), 'startArrow=dash;startSize=8;endArrow=block;endFill=1;endSize=6');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
			
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
			
			var assoc = new mxCell('Conditional', new mxGeometry(0, 0, 0, 0), 'startArrow=diamondThin;startFill=0;startSize=14;endArrow=block;endFill=1;endSize=6');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
			
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
			
			var assoc = new mxCell('', new mxGeometry(0, 0, 0, 0), 'startArrow=oval;startFill=0;startSize=7;endArrow=block;endFill=0;endSize=10;dashed=1');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
	
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
	
			var assoc = new mxCell('', new mxGeometry(0, 0, 0, 0), 'startArrow=oval;startFill=0;startSize=7;endArrow=block;endFill=0;endSize=10;dashed=1');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
			
	    	var sourceLabel = new mxCell('', new mxGeometry(0, 0, 20, 14), 'shape=message');
	    	sourceLabel.geometry.relative = true;
	    	sourceLabel.setConnectable(false);
	    	sourceLabel.vertex = true;
	    	sourceLabel.geometry.offset = new mxPoint(-10, -7);
	    	assoc.insert(sourceLabel);
	
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
			
			var assoc = new mxCell('', new mxGeometry(0, 0, 0, 0), 'shape=link');
			assoc.geometry.setTerminalPoint(new mxPoint(0, 0), true);
			assoc.geometry.setTerminalPoint(new mxPoint(160, 0), false);
			assoc.edge = true;
	
			content.appendChild(this.createEdgeTemplateFromCells([assoc], 160, 0));
		}, 0.5);
};*/

/**
 * Creates and returns the given title element.
 */
Sidebar.prototype.createTitle = function(label)
{
	var elt = document.createElement('a');
	elt.setAttribute('href', 'javascript:void(0);');
	elt.className = 'geTitle';
	mxUtils.write(elt, label);

	return elt;
};

/**
 * Creates a thumbnail for the given cells.
 * cells：palette中的流程元素
 * width：每个cell在palette中的宽度
 * height：每个cell在palette中的高度
 * parent：容器
 */
Sidebar.prototype.createThumb = function(cells, width, height, parent)
{
	// Workaround for off-screen text rendering in IE
	var old = mxText.prototype.getTableSize;
	
	if (this.graph.dialect != mxConstants.DIALECT_SVG)
	{
		mxText.prototype.getTableSize = function(table)
		{
			var oldParent = table.parentNode;
			
			document.body.appendChild(table);
			var size = new mxRectangle(0, 0, table.offsetWidth, table.offsetHeight);
			oldParent.appendChild(table);
			
			return size;
		};
	}
	
	var prev = mxImageShape.prototype.preserveImageAspect;
	mxImageShape.prototype.preserveImageAspect = false;
	
	this.graph.view.rendering = false;
	this.graph.view.setScale(1);
	this.graph.addCells(cells);
	var bounds = this.graph.getGraphBounds();

	var corr = (this.shiftThumbs) ? this.thumbBorder + 1 : this.thumbBorder;
	var s = Math.min((width - 1) / (bounds.x + bounds.width + corr),
		(height - 1) / (bounds.y + bounds.height + corr));
	this.graph.view.setScale(s);
	this.graph.view.rendering = true;
	this.graph.refresh();
	mxImageShape.prototype.preserveImageAspect = prev;

	bounds = this.graph.getGraphBounds();
	var dx = Math.max(0, Math.floor((width - bounds.width) / 2));
	var dy = Math.max(0, Math.floor((height - bounds.height) / 2));
	
	var node = null;
	
	// For supporting HTML labels in IE9 standards mode the container is cloned instead
	if (this.graph.dialect == mxConstants.DIALECT_SVG && !mxClient.IS_IE)
	{
		node = this.graph.view.getCanvas().ownerSVGElement.cloneNode(true);
	}
	// Workaround for VML rendering in IE8 standards mode
	else if (document.documentMode == 8)
	{
		node = this.graph.container.cloneNode(false);
		node.innerHTML = this.graph.container.innerHTML;
	}
	else
	{
		node = this.graph.container.cloneNode(true);
	}
	
	this.graph.getModel().clear();
	
	// Outer dimension is (32, 32)
	var dd = (this.shiftThumbs) ? 2 : 3;
	node.style.position = 'relative';
	node.style.overflow = 'visible';
	node.style.cursor = 'pointer';
	node.style.left = (dx + dd) + 'px';
	node.style.top = (dy + dd) + 'px';
	node.style.width = width + 'px';
	node.style.height = height + 'px';
	
	parent.appendChild(node);
	mxText.prototype.getTableSize = old;
};

/**
 * 为cells创建容器，并将cells放入其中
 */
Sidebar.prototype.createItem = function(cells)
{
	var elt = document.createElement('a');
	elt.setAttribute('href', 'javascript:void(0);');
	elt.className = 'geItem';
	
	// Blocks default click action
	mxEvent.addListener(elt, 'click', function(evt)
	{
		mxEvent.consume(evt);
	});

	this.createThumb(cells, this.thumbWidth, this.thumbHeight, elt);
	
	return elt;
};

/**
 * 为cell拖拽事件创建Handler
 */
Sidebar.prototype.createDropHandler = function(cells, allowSplit)
{
	return function(graph, evt, target, x, y)
	{
		cells = graph.getImportableCells(cells);
		if (cells.length > 0)
		{
			var validDropTarget = (target != null) ?
				graph.isValidDropTarget(target, cells, evt) : false;
			var select = null;
			
			if (target != null && !validDropTarget)
			{
				target = null;
			}
			
			// Splits the target edge or inserts into target group
			if (allowSplit && graph.isSplitEnabled() && graph.isSplitTarget(target, cells, evt))
			{
				graph.splitEdge(target, cells, null, x, y);
				select = cells;
			}
			else if (cells.length > 0)
			{
				var cloneCell=new Array();
				var j=0;
				for(var i=0;i<cells.length;i++)
				{
					if(!cells[i].edge)
					cloneCell[j++]=cells[i];
				}
				if (cloneCell.length > 0)
				select = graph.importCells(cloneCell, x, y, target);
			}
			if (select != null && select.length > 0)
			{
				graph.scrollCellToVisible(select[0]);
				graph.setSelectionCells(select);
			}
		}
	};
};

/**
 * Creates and returns a preview element for the given width and height.
 * 创建流程元素拖拽复制时，鼠标未落下前的预览
 */
Sidebar.prototype.createDragPreview = function(width, height)
{
	var elt = document.createElement('div');
	elt.style.border = '1px dashed black';
	elt.style.width = width + 'px';
	elt.style.height = height + 'px';
	
	return elt;
};

/**
 * 为特定元素创建拖拽元
 */
Sidebar.prototype.createDragSource = function(elt, dropHandler, preview)
{
	var dragSource = mxUtils.makeDraggable(elt, this.editorUi.editor.graph, dropHandler,
		preview, 0, 0, this.editorUi.editor.graph.autoscroll, true, true);

	// Allows drop into cell only if target is a valid root
	dragSource.getDropTarget = function(graph, x, y)
	{
		var target = mxDragSource.prototype.getDropTarget.apply(this, arguments);
		
		if (!graph.isValidRoot(target))
		{
			target = null;
		}
		
		return target;
	};
	
	return dragSource;
};

/**
 * 为点击事件增加Handler
 */
Sidebar.prototype.addClickHandler = function(elt, ds)
{
	var graph = this.editorUi.editor.graph;
	var first = null;
	
	var md = (mxClient.IS_TOUCH) ? 'touchstart' : 'mousedown';
	mxEvent.addListener(elt, md, function(evt)
	{
		first = new mxPoint(mxEvent.getClientX(evt), mxEvent.getClientY(evt));
	});
	
	var oldMouseUp = ds.mouseUp;
	ds.mouseUp = function(evt)
	{
		if (!mxEvent.isPopupTrigger(evt) && this.currentGraph == null && first != null)
		{
			var tol = graph.tolerance;
			
			if (Math.abs(first.x - mxEvent.getClientX(evt)) <= tol &&
				Math.abs(first.y - mxEvent.getClientY(evt)) <= tol)
			{
				var gs = graph.getGridSize();
				ds.drop(graph, evt, null, gs, gs);
			}
		}
		oldMouseUp.apply(this, arguments);
		first = null;
	};
};

/**
 * 创建顶点模板
 */
Sidebar.prototype.createVertexTemplate = function(style, width, height, value)
{
	var cells = [new mxCell((value != null) ? value : '', new mxGeometry(0, 0, width, height), style)];
	cells[0].vertex = true;
	
	return this.createVertexTemplateFromCells(cells, width, height);
};

/**
 * 基于cell创建顶点模板
 */
Sidebar.prototype.createVertexTemplateFromCells = function(cells, width, height)
{
    //隐藏label
	for(var i=0;i<cells.length;i++)
	 {
	 	if(cells[i].getStyle().indexOf("noLabel=1")==-1)
	 	{
		  if(cells[i].getStyle().indexOf("noLabel=0")!=-1)
		     cells[i].setStyle(cells[i].getStyle().replace("noLabel=0","noLabel=1"));
		  else
		     cells[i].setStyle(cells[i].getStyle()+";noLabel=1");
	 	}
		
	 }
	var elt = this.createItem(cells);    //为每个cell创建容器，并将cell放入其中
	var ds = this.createDragSource(elt, this.createDropHandler(cells, true), this.createDragPreview(width, height));
	this.addClickHandler(elt, ds);

	// Uses guides for vertices only if enabled in graph
	ds.isGuidesEnabled = mxUtils.bind(this, function()
	{
		return this.editorUi.editor.graph.graphHandler.guidesEnabled;
	});

	// 为流程元素增加预览
	if (!touchStyle)
	{
		mxEvent.addListener(elt, 'mousemove', mxUtils.bind(this, function(evt)
		{
			this.showTooltip(elt, cells);
		}));
	}
	
	return elt;
	
};

/**
 * 创建连线模板
 */
Sidebar.prototype.createEdgeTemplate = function(style, width, height, value)
{
	var cells = [new mxCell((value != null) ? value : '', new mxGeometry(0, 0, width, height), style)];
	cells[0].geometry.setTerminalPoint(new mxPoint(0, height), true);
	cells[0].geometry.setTerminalPoint(new mxPoint(width, 0), false);
	cells[0].edge = true;
	
	return this.createEdgeTemplateFromCells(cells, width, height);
};

/**
 * 基于cell创建连线模板
 */
Sidebar.prototype.createEdgeTemplateFromCells = function(cells, width, height)
{
	var elt = this.createItem(cells);
	this.createDragSource(elt, this.createDropHandler(cells, false), this.createDragPreview(width, height));

	// Installs the default edge
	var graph = this.editorUi.editor.graph;
	mxEvent.addListener(elt, 'click', mxUtils.bind(this, function(evt)
	{
		if (this.installEdges)
		{
			// Uses edge template for connect preview
			graph.connectionHandler.createEdgeState = function(me)
			{
	    		return graph.view.createState(cells[0]);
		    };
	
		    // Creates new connections from edge template
		    graph.connectionHandler.factoryMethod = function()
		    {
	    		return graph.cloneCells([cells[0]])[0];
		    };
		}
		
		// Highlights the entry for 200ms
		elt.style.backgroundColor = '#ffffff';
		
		window.setTimeout(function()
		{
			elt.style.backgroundColor = '';
		}, 200);
	    
	    mxEvent.consume(evt);
	}));

	// Shows a tooltip with the rendered cell
	if (!touchStyle)
	{
		mxEvent.addListener(elt, 'mousemove', mxUtils.bind(this, function(evt)
		{
			this.showTooltip(elt, cells);
		}));
	}
	
	return elt;
};

//获取objectType对应实体的接口
Sidebar.prototype.ServiceJson = function(objectType)
{
	    var url = "/wfd/wf";
		//var type = "nc.vo.arap.receivable.ReceivableBillVO"; "nc.vo.obm.payroll.DfgzHVO" 'nc.vo.ebpur.purapply.PurApplyVO' 例子
	    var req = new mxXmlRequest(url, "action=metadataUtil&function=loadOperation&type=" + objectType, null, false);
	    req.send(null, null);
	    if (req.request.status == 404){
		  alert("不能得到元数据，请确认服务是否启动。");
		 return;
	    }
	   var json = eval(req.request.responseText);
	   if (json.error != undefined){
		alert(json.errorDesc);
		return;
	   }
	   return json;
}

//向div中增加子palette及流程元素
Sidebar.prototype.addServicePalette = function(div,json,position)
{
		var elt = this.createTitle(json.name); //创建标题栏
		elt.style.paddingLeft=position;
		div.appendChild(elt);
		var serviceDiv = document.createElement('div');//创建流程元素容器
	    serviceDiv.className = 'geSidebar';
	    serviceDiv.style.paddingLeft=position; 
	  /*   if(json.name=='Level1NO1')
	    {
	      this.addServicePalette(serviceDiv,json,'17px');
	    }*/
	    this.addFoldingHandler(elt, serviceDiv, mxUtils.bind(this, function(content){ //添加折叠展开事件处理器
	     for(var i=0;i<json.operations.length;i++)   //增加流程元素
		{
		  var value=new ServiceTask();
		  value.name=json.operations[i].name;
		  value.label=json.operations[i].name;
		  value.extendClass=json.name;
		  value.method=json.operations[i].name;
		  value.fieldExtensions=json.operations[i].parameters;
		  this.addNode(content,value,'Y',90, 55, 'shape=task;shadow=1;rounded=1;fontFamily=宋体;fontSize=12;fillColor=#1496F7;gradientColor=#AAD9F1;','shape=image;image=images/icons/activity/list/type.service.png;', 5, 5, 0, 0, 26, 20); 
	    }
	  }
	   ));
       serviceDiv.style.display = 'none';
       var outer = document.createElement('div');
       outer.appendChild(serviceDiv);
       div.appendChild(outer);//添加到div中
       
       if (json.name != null)
      {
    	this.palettes[json.name] = [elt, outer];   //记录palette
      }
}

/**
 * 新增palette
 */
Sidebar.prototype.addPalette = function(id, title, expanded, onInit)
{
	var elt = this.createTitle(title);
	 this.groupsContainer.appendChild(elt);
	
	var div = document.createElement('div');
	div.className = 'geSidebar'; 
	if (expanded)
	{
		onInit(div);
		onInit = null;
	}
	else
	{
		div.style.display = 'none';
	}
	
    this.addFoldingHandler(elt, div, onInit);

    var outer = document.createElement('div');
    outer.appendChild(div);
     this.groupsContainer.appendChild(outer);

    // Keeps references to the DOM nodes
    if (id != null)
    {
    	this.palettes[id] = [elt, outer];
    }
};

/**
 * 折叠展开事件处理器
 */
Sidebar.prototype.addFoldingHandler = function(title, content, funct)
{
	var initialized = false;

	title.style.backgroundImage = (content.style.display == 'none') ?
		'url(' + IMAGE_PATH + '/collapsed.gif)' : 'url(' + IMAGE_PATH + '/expanded.gif)';
	title.style.backgroundRepeat = 'no-repeat';
	title.style.backgroundPosition = '100% 50%';
	var _this=this;
	mxEvent.addListener(title, 'click', function(evt)
	{
	  var objectType=_this.editorUi.editor.graph.model.getRoot().getChildAt('0').value.objectType;
		//折叠
		if (content.style.display == 'none')
		{ 
			//读取process的objectType属性指定的实体接口，生成service流程元素，加入BusinessService palette
	        if(title.innerHTML=='Business Service'&&objectType!="")
	         {
		         var json=_this.ServiceJson(objectType);
		         for(var i=0;i<json.length;i++)
		          _this.addServicePalette(content,json[i],'10px');	
	         }
			
			if (!initialized)
			{
				initialized = true;
				
				if (funct != null)
				{
					funct(content);
				}
			}
			
			title.style.backgroundImage = 'url(' + IMAGE_PATH + '/expanded.gif)';
			content.style.display = 'block';
		}
		//展开
		else
		{
			if(title.innerHTML=='Business Service'&&objectType!="")
	         {
		           _this.removePalette('Business Service');	
		           _this.addPalette('Business Service', 'Business Service', false, mxUtils.bind(this, function(content){}));
	         }
			title.style.backgroundImage = 'url(' + IMAGE_PATH + '/collapsed.gif)';
			content.style.display = 'none';
		}
		
		mxEvent.consume(evt);
	});
};

/**
 * 删除指定palette
 */
Sidebar.prototype.removePalette = function(id)
{
	var elts = this.palettes[id];
	if (elts != null)
	{
		this.palettes[id] = null;
		
		for (var i = 0; i < elts.length; i++)
		{
			this.groupsContainer.removeChild(elts[i]);
		}
		
		return true;
	}
	
	return false;
};

/**
 * Adds the given image palette.
 */
/*
Sidebar.prototype.addImagePalette = function(id, title, prefix, postfix, items)
{
	this.addPalette(id, title, false, mxUtils.bind(this, function(content)
    {
    	for (var i = 0; i < items.length; i++)
		{
			var icon = prefix + items[i] + postfix;
			content.appendChild(this.createVertexTemplate('image;image=' + icon, 80, 80, ''));
		}
    }));
};*/

/**
 * Adds the given stencil palette.
 *//*
Sidebar.prototype.addStencilPalette = function(id, title, stencilFile, style, ignore, onInit, scale)
{
	
	scale = (scale != null) ? scale : 1;
	
	this.addPalette(id, title, false, mxUtils.bind(this, function(content)
    {
		if (style == null)
		{
			style = '';
		}
		
		if (onInit != null)
		{
			onInit.call(this, content);
		}

		mxStencilRegistry.loadStencilSet(stencilFile, mxUtils.bind(this, function(packageName, stencilName, displayName, w, h)
		{
			if (ignore == null || mxUtils.indexOf(ignore, stencilName) < 0)
			{
				content.appendChild(this.createVertexTemplate('shape=' + packageName + stencilName.toLowerCase() + style,
					Math.round(w * scale), Math.round(h * scale), ''));
			}
		}), true);
    }));
};*/
