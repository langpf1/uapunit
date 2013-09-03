/**
 * $Id: Graph.js,v 1.45 2012-08-02 13:56:37 gaudenz Exp $
 * Copyright (c) 2006-2012, JGraph Ltd
 */
/**
 * Constructs a new graph instance. Note that the constructor does not take a
 * container because the graph instance is needed for creating the UI, which
 * in turn will create the container for the graph. Hence, the container is
 * assigned later in EditorUi.
 */
Graph = function(container, model, renderHint, stylesheet)
{
	mxGraph.call(this, container, model, renderHint, stylesheet);
    var process = new Process();
	process.id = new GUID().newGUID();
	process.name="mainProcess_web";
	this.model.getRoot().getChildAt('0').setId("1");
	this.model.getRoot().getChildAt('0').setValue(process);
	
	this.modelInfoMap = new Object();
	this.defaultValueMap = new Object();
	this.setConnectable(true);
	this.setDropEnabled(true);
	this.setPanning(true);
	this.setTooltips(!mxClient.IS_TOUCH);
	this.setAllowLoops(true);
	this.allowAutoPanning = true;

	this.connectionHandler.setCreateTarget(true);

	// Sets the style to be used when an elbow edge is double clicked
	this.alternateEdgeStyle = 'vertical';

	if (stylesheet == null)
	{
		this.loadStylesheet();
	}
	
	// Creates rubberband selection
    var rubberband = new mxRubberband(this);
    
    this.getRubberband = function()
    {
    	return rubberband;
    };
    
    // Shows hand cursor while panning
	this.panningHandler.addListener(mxEvent.PAN_START, mxUtils.bind(this, function()
	{
		this.container.style.cursor = 'pointer';
	}));
			
	this.panningHandler.addListener(mxEvent.PAN_END, mxUtils.bind(this, function()
	{
		this.container.style.cursor = 'default';
	}));

    // Adds support for HTML labels via style. Note: Currently, only the Java
    // backend supports HTML labels but CSS support is limited to the following:
    // http://docs.oracle.com/javase/6/docs/api/index.html?javax/swing/text/html/CSS.html
	this.isHtmlLabel = function(cell)
	{
		var state = this.view.getState(cell);
		var style = (state != null) ? state.style : this.getCellStyle(cell);
		
		return style['html'] == '1';
	};
	
	// Unlocks all cells
	this.isCellLocked = function(cell)
	{
		return false;
	};

	// Tap and hold brings up context menu.
	// Tolerance slightly below graph tolerance is better.
	this.connectionHandler.tapAndHoldTolerance = 16;
	
	//  Tap and hold on background starts rubberband on cell starts connecting
	var connectionHandlerTapAndHold = this.connectionHandler.tapAndHold;
	this.connectionHandler.tapAndHold = function(me, state)
	{
		if (state == null)
		{
			if (!this.graph.panningHandler.active)
			{
				rubberband.start(me.getGraphX(), me.getGraphY());
				this.graph.panningHandler.panningTrigger = false;
			}
		}
		else if (tapAndHoldStartsConnection)
		{
			connectionHandlerTapAndHold.apply(this, arguments);	
		}
		else if (this.graph.isCellSelected(state.cell) && this.graph.getSelectionCount() > 1)
		{
			this.graph.removeSelectionCell(state.cell);
		}
	};

	if (touchStyle)
	{
		this.initTouch();
	}
};

// Graph inherits from mxGraph
mxUtils.extend(Graph, mxGraph);

/**
 * Allows to all values in fit.
 */
Graph.prototype.minFitScale = null;

/**
 * Allows to all values in fit.
 */
Graph.prototype.maxFitScale = null;

/**
 * Loads the stylesheet for this graph.
 */

Graph.prototype.loadStylesheet = function()
{
    var node = mxUtils.load(STYLE_PATH + '/default.xml').getDocumentElement();
	var dec = new mxCodec(node.ownerDocument);
	dec.decode(node, this.getStylesheet());
};

/**
 * Inverts the elbow edge style without removing existing styles.
 */
Graph.prototype.flipEdge = function(edge)
{
	if (edge != null)
	{
		var state = this.view.getState(edge);
		var style = (state != null) ? state.style : this.getCellStyle(edge);
		
		if (style != null)
		{
			var elbow = mxUtils.getValue(style, mxConstants.STYLE_ELBOW,
				mxConstants.ELBOW_HORIZONTAL);
			var value = (elbow == mxConstants.ELBOW_HORIZONTAL) ?
				mxConstants.ELBOW_VERTICAL : mxConstants.ELBOW_HORIZONTAL;
			this.setCellStyles(mxConstants.STYLE_ELBOW, value, [edge]);
		}
	}
};

/**
 * Disables folding for non-swimlanes.
 */
Graph.prototype.isCellFoldable = function(cell)
{
	return this.foldingEnabled && (this.isSwimlane(cell)||this.isContainer(cell));
};

Graph.prototype.isContainer = function (cell)
{
	if (cell != null)
	{
		if (this.model.getParent(cell) != this.model.getRoot())
		{
			var state = this.view.getState(cell);
			var style = (state != null) ? state.style : this.getCellStyle(cell);

			if (style != null && !this.model.isEdge(cell))
			{
				return style[mxConstants.STYLE_SHAPE] =="subprocess";
			}
		}
	}
	
	return false;
};
Graph.prototype.isValidDropTarget = function(cell, cells, evt)
{
/*	return  cell != null && ((this.isSplitEnabled() &&
		this.isSplitTarget(cell, cells, evt)) || (!this.model.isEdge(cell) &&
		(this.isSwimlane(cell)||this.isContainer(cell))));*/
		return  cell != null && ((this.isSplitEnabled() &&
		this.isSplitTarget(cell, cells, evt)) || (!this.model.isEdge(cell)));
		
};

Graph.prototype.isSplitTarget = function(target, cells, evt)
{
	if (this.model.isEdge(target) && cells != null && cells.length == 1 &&
		this.isCellConnectable(cells[0]) && this.getEdgeValidationError(target,
			this.model.getTerminal(target, true), cells[0]) == null)
	{
		var src = this.model.getTerminal(target, true);
		var trg = this.model.getTerminal(target, false);
		return (this.isValidSource(src)&&this.isValidSource(cells[0])&&this.isValidTarget(cells[0])&&this.isValidTarget(trg)&&!this.model.isAncestor(cells[0], src) &&
				!this.model.isAncestor(cells[0], trg));
	}

	return false;
};


/**
 * Disables drill-down for non-swimlanes.
 */
Graph.prototype.isValidRoot = function(cell)
{
	return this.isSwimlane(cell)||this.isContainer(cell);
};

/**
 * Overrides createGroupCell to set the group style for new groups to 'group'.
 */
Graph.prototype.createGroupCell = function()
{
	var group = mxGraph.prototype.createGroupCell.apply(this, arguments);
	group.setStyle('group');
	
	return group;
};

/**
 * Overrides tooltips to show position and size
 */
Graph.prototype.getTooltipForCell = function(cell)
{
	var tip = '';
	
	if (this.getModel().isVertex(cell))
	{
		var geo = this.getCellGeometry(cell);
		
		var f2 = function(x)
		{
			return Math.round(parseFloat(x) * 100) / 100;
		};
		
		if (geo != null)
		{
			if (tip == null)
			{
				tip = '';
			}
			else if (tip.length > 0)
			{
				tip += '\n';
			}
			
			tip += 'X: ' + f2(geo.x) + '\nY: ' + f2(geo.y) + '\nW: ' + f2(geo.width) + '\nH: ' + f2(geo.height);
		}
	}
	else if (this.getModel().isEdge(cell))
	{
		tip = mxGraph.prototype.getTooltipForCell.apply(this, arguments);
	}
	
	return tip;
};

/**
 * Returns the label for the given cell.
 */
Graph.prototype.convertValueToString = function(cell)
{
	if (cell.value != null && typeof(cell.value) == 'object')
	{
		//return cell.value.getAttribute('label');
		return cell.value['label'];
	}
	
	return mxGraph.prototype.convertValueToString.apply(this, arguments);
};

/**
 * Handles label changes for XML user objects.
 */
Graph.prototype.cellLabelChanged = function(cell, value, autoSize)
{
	if (cell.value != null && typeof(cell.value) == 'object')
	{
		var tmp = cell.getValue();
		tmp.label=value;
		if(!(cell.value instanceof Annotation))
		tmp.name=value;
		else
		tmp.text=value;
		value = tmp;
	}
	
	this.model.beginUpdate();
	try
	{
		this.model.setValue(cell, value);
		
		if (autoSize)
		{
			this.cellSizeUpdated(cell, false);
		}
	}
	finally
	{
		this.model.endUpdate();
	}

};

/**
 * Sets the link for the given cell.
 */
Graph.prototype.setLinkForCell = function(cell, link)
{
	var value = null;
	
	if (cell.value != null && typeof(cell.value) == 'object')
	{
		value = cell.value.cloneNode(true);
	}
	else
	{
		var doc = mxUtils.createXmlDocument();
		
		value = doc.createElement('UserObject');
		value.setAttribute('label', cell.value);
	}
	
	if (link != null && link.length > 0)
	{
		value.setAttribute('link', link);
	}
	else
	{
		value.removeAttribute('link');
	}
	
	this.model.setValue(cell, value);
};

/**
 * Returns the link for the given cell.
 */
Graph.prototype.getLinkForCell = function(cell)
{
	if (cell.value != null && typeof(cell.value) == 'object'&&cell.link!=null)
	{
		return cell.value.getAttribute('link');
	}
	
	return null;
};

Graph.prototype.setPropertyGrid = function(propertyGrid)
{
	this.propertyGrid = propertyGrid;	
};

/**
 * 清除已有数据.
 * /
Graph.prototype.deleteAllRows = function()
{
	$('#' + this.propertyGrid).propertygrid('selectAll');
	var rows = 	$('#' + this.propertyGrid).propertygrid('getSelections');
	for(var i = 0; i < rows.length; i++)
	{
		var rowIndex = $('#' + this.propertyGrid).propertygrid('getRowIndex',rows[i]);
		$('#' + this.propertyGrid).propertygrid('deleteRow',rowIndex);
	}
};
*/

/**
 * 设置当前cell中对象的属性.
 */
Graph.prototype.setPropertyGridRows = function(cell){
	/*
	var cellValue = cell.getValue();
	this.deleteAllRows();
	var row = {};
	var properties = cellValue.properties;
	for(var i=0; i< properties.length; i++)
	{
		var property = properties[i];
		var name = property.get_name();
		var group = property.get_group();
		var isReadOnly = property.get_isReadOnly();
		var editorType = property.get_editorType();
		var editorOptions = property.get_editorOptions();
		
		if(editorOptions != ''){
			row = {name:name, value:cellValue[name], group:group, editor:{type:editorType,options:editorOptions}};
		}
		else{
			row = {name:name, value:cellValue[name], group:group, editor:editorType};
		}
		$('#' + this.propertyGrid).propertygrid('appendRow',row);
	}
	*/
	//两种方法的速度都不快
	if(cell!=null&&cell.getValue()!=null)
    {
		var cellValue = cell.getValue();
		//this.deleteAllRows();
		var row = {};
		if(cellValue != null && cellValue != undefined && cellValue != "")
		{
			var funcName = mxUtils.getFunctionName(cellValue.constructor);
			var defaultValue;
			if(this.defaultValueMap[funcName]!=null && this.defaultValueMap[funcName]!=undefined){
				defaultValue = this.defaultValueMap[funcName];
			}
			else{
	   			defaultValue = eval('new '+ funcName +'()');
	   			this.defaultValueMap[funcName] = defaultValue;
			}

			var info = null;
			var infoClass = cellValue.infoClass;
			if(this.modelInfoMap[infoClass]!=null && this.modelInfoMap[infoClass]!=undefined){
				info = this.modelInfoMap[infoClass];
			}
			else{
	   			info = eval('new '+ infoClass +'()');
	   			this.modelInfoMap[infoClass] = info;
			}
		  	var properties = info.properties;
			var json = new Object();
			json.total = properties.length;
			json.rows = new Array();
			for(var i=0; i< properties.length; i++)
			{
				var property = properties[i];
				var name = property.get_name();
				var displayName = property.get_displayName();
				var group = property.get_group();
				var isReadOnly = property.get_isReadOnly();
				var editorType = property.get_editorType();
				var editorOptions = property.get_editorOptions();
				if (group && group !== undefined)
					group = mxResources.get(group);
				if (!displayName || displayName==undefined)
					displayName = name;

				displayName = mxResources.get(displayName);
				var propertyValue=this.getPropertyValue(cellValue,name);
				if(editorOptions != ''){
					row = {displayname:displayName, value:propertyValue, name:name, group:group, editor:{type:editorType,options:editorOptions}};
				}
				else{
					row = {displayname:displayName, value:propertyValue, name:name, group:group, editor:editorType};
				}
				json.rows.push(row);
			}
			
			$('#' + this.propertyGrid).propertygrid('loadData',eval(json));
			json = null;
			var that=this;
			
			$('#' + this.propertyGrid).propertygrid({
				onAfterEdit:function(rowIndex, rowData, changes)
				{
					if(changes && (changes.value != undefined)){
						var aDefaultValue = that.getDefaultValue(defaultValue, rowData['name']);//类的默认值
						if(changes.value == ""){//不是字符串的属性置为默认值
							if(!(typeof changes.value == "string")){
								that.setPropertyValue(cellValue, rowData['name'], aDefaultValue);
							}
						}
						else{
							if(aDefaultValue!=undefined&&typeof changes.value != typeof aDefaultValue){//属性编辑返回的类型和类的不一样
								that.setPropertyValue(cellValue, rowData['name'], eval(changes.value));
							}
							else{
								that.setPropertyValue(cellValue, rowData['name'], changes.value);
							}
						}
						if((rowData['name']=="name"&&!(cell.value instanceof Annotation))||(rowData['name']=="text"&&cell.value instanceof Annotation))
						{
						  cell.value.label=changes.value;
						  var tmp = cell.getValue();
						  that.model.setValue(cell, tmp);
						}
						if(rowData['name']=="interrupting")
						{
						 if(cell.getStyle().indexOf("dashed")!=null&&changes.value=="true")
					     var style=cell.getStyle()+";dashed=true;"; 
					     else
					     {
					     	if(changes.value=="true")
					     	   var style=cell.getStyle().replace("dashed=false","dashed=true");
					     	else
					     	   var style=cell.getStyle().replace(";dashed=true;","");
					     }
					     that.model.setStyle(cell, style);
						}
					}
				}
			});
		}
    }
}

Graph.prototype.setPropertyValue = function(obj,propertyName,value)
{
	var index = propertyName.indexOf('.');
	if(index >=0)
	{
		var ps = propertyName.split(".", 2);
		var p1 = ps[0];
		var p2 = ps[1];
		obj[p1][p2] = value;
	}
	else
	{
		obj[propertyName] = value;
	}
};

/*获取属性值
 * obj:
 * propertyName:
 */
Graph.prototype.getPropertyValue = function(obj,propertyName)  
{
	var index = propertyName.indexOf('.');
	if(index >=0)
	{
		var ps = propertyName.split(".", 2);
		var p1 = ps[0];
		var p2 = ps[1];
		return obj[p1][p2];
	}
	else
	{
		return obj[propertyName];
	}
};

Graph.prototype.getDefaultValue = function(obj, propertyName)
{
	var index = propertyName.indexOf('.');
	if(index >=0)
	{
		var ps = propertyName.split(".", 2);
		var p1 = ps[0];
		var p2 = ps[1];
		return obj[p1][p2];
	}
	else
	{
		return obj[propertyName];
	}
};

Graph.prototype.setSelectionCell = function(cell)
{
	if(cell!=null&&cell.value!="")  //设置无法选中内部图片cell
	{
	  this.setPropertyGridRows(cell);
	  this.getSelectionModel().setCell(cell);
	}
};	

Graph.prototype.setSelectionCells = function(cells)
{
	if(cells.length == 1){
		this.setPropertyGridRows(cells[0]);
	}
	else{
		var row = {};
		$('#' + this.propertyGrid).propertygrid('appendRow',row);
	}
	this.getSelectionModel().setCells(cells);
};

Graph.prototype.click = function(me)
{
	var evt = me.getEvent();
	var cell = me.getCell();
	var mxe = new mxEventObject(mxEvent.CLICK, 'event', evt, 'cell', cell);
	
	if (me.isConsumed())
	{
		mxe.consume();
	}
	
	this.fireEvent(mxe);
	
	// Handles the event if it has not been consumed
	if (this.isEnabled() && !mxEvent.isConsumed(evt) && !mxe.isConsumed())
	{
		if (cell != null)
		{
			this.selectCellForEvent(cell, evt);
		}
		else
		{
			var swimlane = null;
			
			if (this.isSwimlaneSelectionEnabled())
			{
				// Gets the swimlane at the location (includes
				// content area of swimlanes)
				swimlane = this.getSwimlaneAt(me.getGraphX(), me.getGraphY());
			}

			// Selects the swimlane and consumes the event
			if (swimlane != null)
			{
				this.selectCellForEvent(swimlane, evt);
			}
			
			// Ignores the event if the control key is pressed
			else if (!this.isToggleEvent(evt))
			{
				this.clearSelection();
			}
	        this.selectCellForEvent(this.model.getRoot().getChildAt(0),evt);
		}
	}
};

Graph.prototype.isValidSource = function(cell)
{
		var isValid= (cell == null && this.allowDanglingEdges)||(cell != null&& (!this.model.isEdge(cell)|| this.isConnectableEdges()) && this.isCellConnectable(cell));
		if(isValid){
			cell.getValue();
			isValid = !(cell.getValue()instanceof EndEvent)&&!(cell.getValue() instanceof Group);
		}
		return isValid;
};
 Graph.prototype.isValidTarget = function(cell)
{
		var isValidTarget = (cell == null && this.allowDanglingEdges)|| (cell != null&& (!this.model.isEdge(cell) || this.isConnectableEdges()) && this.isCellConnectable(cell));
		if(isValidTarget){
			isValidTarget = !(cell.getValue() instanceof StartEvent)&&!(cell.getValue() instanceof Group);
		}
		return isValidTarget;
};


/**
 * Customized graph for touch devices.
 */
Graph.prototype.initTouch = function()
{
	// Disables new connections via "hotspot"
	this.connectionHandler.marker.isEnabled = function()
	{
		return this.graph.connectionHandler.first != null;
	};

	// Hides menu when editing starts
	this.addListener(mxEvent.START_EDITING, function(sender, evt)
	{
		this.panningHandler.hideMenu();
	});

	// Context menu for touchstyle
	var showMenu = false;
	var menuCell = null;

	// Checks if native hit detection did not return anything and does custom
	// hit detection for edges to take into account the tolerance
	this.updateMouseEvent = function(me)
	{
		mxGraph.prototype.updateMouseEvent.apply(this, arguments);

		if (me.getState() == null)
		{
			var cell = this.getCellAt(me.graphX, me.graphY);
			
			if (this.getModel().isEdge(cell))
			{
				me.state = this.view.getState(cell);
				
				if (me.state != null && me.state.shape != null)
				{
					this.container.style.cursor = me.state.shape.node.style.cursor;
				}
			}
		}
		
		if (me.getState() == null)
		{
			this.container.style.cursor = 'default';
		}
	};
	
	// Handles popup menu on touch devices (tap selected cell)
	this.fireMouseEvent = function(evtName, me, sender)
	{
		if (evtName == mxEvent.MOUSE_DOWN)
		{
			if (!this.panningHandler.isMenuShowing())
			{
				menuCell = me.getCell();
				showMenu = (menuCell != null) ? this.isCellSelected(menuCell) : this.isSelectionEmpty();
			}
			else
			{
				showMenu = false;
				menuCell = null;
			}
		}
		else if (evtName == mxEvent.MOUSE_UP)
		{
			if (showMenu && !this.isEditing())
			{
				if (!this.panningHandler.isMenuShowing())
				{
					var x = mxEvent.getClientX(me.getEvent());
					var y = mxEvent.getClientY(me.getEvent());
					
					this.panningHandler.popup(x + 16, y, menuCell, me.getEvent());
				}
				
				showMenu = false;
				menuCell = null;
				me.consume();
				
				return;
			}
			
			showMenu = false;
			menuCell = null;
		}

		mxGraph.prototype.fireMouseEvent.apply(this, arguments);

		if (evtName == mxEvent.MOUSE_MOVE && me.isConsumed())
		{
			showMenu = false;
			menuCell = null;
		}
	};
};

// Changes order of panninghandler
		Graph.prototype.createHandlers = function(container)
		{

			this.tooltipHandler = new mxTooltipHandler(this);
			this.tooltipHandler.setEnabled(false);
			// Selection cells first
			this.selectionCellsHandler = new mxSelectionCellsHandler(this);
			this.panningHandler = new mxPanningHandler(this);
			this.panningHandler.panningEnabled = false;
			this.connectionHandler = new BpmnConnectionHandler(this);
			this.connectionHandler.setEnabled(false);
			this.graphHandler = new BpmnGraphHandler(this);
		};

/**
 * Implements touch devices.
 */
(function()
{
	// Touch-specific static overrides
	if (touchStyle)
	{
		// Sets constants for touch style
		mxConstants.HANDLE_SIZE = 16;
		mxConstants.LABEL_HANDLE_SIZE = 7;
		
		// Larger tolerance and grid for real touch devices
		if (mxClient.IS_TOUCH)
		{
			mxVertexHandler.prototype.tolerance = 4;
			mxEdgeHandler.prototype.tolerance = 6;
			Graph.prototype.tolerance = 14;
			Graph.prototype.gridSize = 20;
			
			// One finger pans (no rubberband selection) must start regardless of mouse button
			mxPanningHandler.prototype.selectOnPopup = false;
			mxPanningHandler.prototype.useLeftButtonForPanning = true;
			mxPanningHandler.prototype.isPanningTrigger = function(me)
			{
				var evt = me.getEvent();
			 	
			 	return (this.useLeftButtonForPanning && (this.ignoreCell || me.getState() == null)/* &&
			 			mxEvent.isLeftMouseButton(evt)*/) || (mxEvent.isControlDown(evt) &&
			 			mxEvent.isShiftDown(evt)) || (this.usePopupTrigger &&
			 		   	mxEvent.isPopupTrigger(evt));
			};
		}
		
		// Don't clear selection if multiple cells selected
		var graphHandlerMouseDown = mxGraphHandler.prototype.mouseDown;
		mxGraphHandler.prototype.mouseDown = function(sender, me)
		{
			graphHandlerMouseDown.apply(this, arguments);

			if (this.graph.isCellSelected(me.getCell()) && this.graph.getSelectionCount() > 1)
			{
				this.delayedSelection = false;
			}
		};

		// On connect the target is selected and we clone the cell of the preview edge for insert
		BpmnConnectionHandler.prototype.selectCells = function(edge, target)
		{
			if (touchStyle && target != null)
			{
				this.graph.setSelectionCell(target);
			}
			else 
			{
				this.graph.setSelectionCell(edge);
			}
			
		};

		// Overrides double click handling to use the tolerance
		// FIXME: Double click on edges in iPad needs focus on textarea
		var graphDblClick = mxGraph.prototype.dblClick;
		Graph.prototype.dblClick = function(evt, cell)
		{
			if (cell == null)
			{
				var pt = mxUtils.convertPoint(this.container,
					mxEvent.getClientX(evt), mxEvent.getClientY(evt));
				cell = this.getCellAt(pt.x, pt.y);
			}

			graphDblClick.call(this, evt, cell);
		};

		// Rounded edge and vertex handles
		var touchHandle = new mxImage(IMAGE_PATH + '/touch-handle.png', 16, 16);
		mxVertexHandler.prototype.handleImage = touchHandle;
		mxEdgeHandler.prototype.handleImage = touchHandle;
		mxOutline.prototype.sizerImage = touchHandle;
		
		// Pre-fetches touch handle
		new Image().src = touchHandle.src;

		// Adds connect icon to selected vertices
		var connectorSrc = IMAGE_PATH + '/touch-connector.png';
		
		var vertexHandlerInit = mxVertexHandler.prototype.init;
		mxVertexHandler.prototype.init = function()
		{
			vertexHandlerInit.apply(this, arguments);
			var md = (mxClient.IS_TOUCH) ? 'touchstart' : 'mousedown';

			// Only show connector image on one cell and do not show on containers
			if (showConnectorImg && this.graph.connectionHandler.isEnabled() &&
				this.graph.isCellConnectable(this.state.cell) &&
				!this.graph.isValidRoot(this.state.cell) &&
				this.graph.getSelectionCount() == 1)
			{
				this.connectorImg = mxUtils.createImage(connectorSrc);
				this.connectorImg.style.cursor = 'pointer';
				this.connectorImg.style.width = '29px';
				this.connectorImg.style.height = '29px';
				this.connectorImg.style.position = 'absolute';
				
				if (!mxClient.IS_TOUCH)
				{
					this.connectorImg.setAttribute('title', mxResources.get('connect'));
					mxEvent.redirectMouseEvents(this.connectorImg, this.graph, this.state);
				}

				// Adds 2px tolerance
				this.connectorImg.style.padding = '2px';
				
				// Starts connecting on touch/mouse down
				mxEvent.addListener(this.connectorImg, md,
					mxUtils.bind(this, function(evt)
					{
						this.graph.panningHandler.hideMenu();
						var pt = mxUtils.convertPoint(this.graph.container,
								mxEvent.getClientX(evt), mxEvent.getClientY(evt));
						this.graph.connectionHandler.start(this.state, pt.x, pt.y);
						this.graph.isMouseDown = true;
						mxEvent.consume(evt);
					})
				);

				this.graph.container.appendChild(this.connectorImg);
			}

			this.redrawTools();
		};
		
		var vertexHandlerRedraw = mxVertexHandler.prototype.redraw;
		mxVertexHandler.prototype.redraw = function()
		{
			vertexHandlerRedraw.apply(this);
			this.redrawTools();
		};
		
		mxVertexHandler.prototype.redrawTools = function()
		{
			if (this.state != null && this.connectorImg != null)
			{
				// Top right for single-sizer
				if (mxVertexHandler.prototype.singleSizer)
				{
					this.connectorImg.style.left = (this.state.x + this.state.width - this.connectorImg.offsetWidth / 2) + 'px';
					this.connectorImg.style.top = (this.state.y - this.connectorImg.offsetHeight / 2) + 'px';
				}
				else
				{
					this.connectorImg.style.left = (this.state.x + this.state.width + mxConstants.HANDLE_SIZE / 2 + 4/* - 2 padding*/) + 'px';
					this.connectorImg.style.top = (this.state.y + (this.state.height - this.connectorImg.offsetHeight) / 2) + 'px';
				}
			}
		};
		
		var vertexHandlerDestroy = mxVertexHandler.prototype.destroy;
		mxVertexHandler.prototype.destroy = function(sender, me)
		{
			vertexHandlerDestroy.apply(this, arguments);

			if (this.connectorImg != null)
			{
				this.connectorImg.parentNode.removeChild(this.connectorImg);
				this.connectorImg = null;
			}
		};
		
		// Pre-fetches touch connector
		new Image().src = connectorSrc;
	}
	//else // not touchStyle
	//{
		//mxConnectionHandler.prototype.connectImage = new mxImage(IMAGE_PATH + '/connector.png', 15, 15);
	//}
})();



Graph.prototype.cloneCells = function(cells, allowInvalidEdges)
{
	allowInvalidEdges = (allowInvalidEdges != null) ? allowInvalidEdges : true;
	var clones = null;
	
	if (cells != null)
	{
		// Creates a hashtable for cell lookups
		var hash = new Object();
		var tmp = [];
		
		for (var i = 0; i < cells.length; i++)
		{
			var id = mxCellPath.create(cells[i]);
			hash[id] = cells[i];
			tmp.push(cells[i]);
		}
		
		if (tmp.length > 0)
		{
			var scale = this.view.scale;
			var trans = this.view.translate;
			clones = this.model.cloneCells(cells, true);
		    
			for (var i = 0; i < cells.length; i++)
			{
				if (!allowInvalidEdges && this.model.isEdge(clones[i]) &&
					this.getEdgeValidationError(clones[i],
						this.model.getTerminal(clones[i], true),
						this.model.getTerminal(clones[i], false)) != null)
				{
					clones[i] = null;
				}
				else
				{
					var g = this.model.getGeometry(clones[i]);
					
					if (g != null)
					{
						var state = this.view.getState(cells[i]);
						var pstate = this.view.getState(
							this.model.getParent(cells[i]));
						
						if (state != null && pstate != null)
						{
							var dx = pstate.origin.x;
							var dy = pstate.origin.y;
							
							if (this.model.isEdge(clones[i]))
							{
								var pts = state.absolutePoints;
								
								// Checks if the source is cloned or sets the terminal point
								var src = this.model.getTerminal(cells[i], true);
								var srcId = mxCellPath.create(src);
								
								while (src != null && hash[srcId] == null)
								{
									src = this.model.getParent(src);
									srcId = mxCellPath.create(src);
								}
								
								if (src == null)
								{
									g.setTerminalPoint(
										new mxPoint(pts[0].x / scale - trans.x,
											pts[0].y / scale - trans.y), true);
								}
								
								// Checks if the target is cloned or sets the terminal point
								var trg = this.model.getTerminal(cells[i], false);
								var trgId = mxCellPath.create(trg);
								
								while (trg != null && hash[trgId] == null)
								{
									trg = this.model.getParent(trg);
									trgId = mxCellPath.create(trg);
								}
								
								if (trg == null)
								{
									var n = pts.length - 1;
									g.setTerminalPoint(
										new mxPoint(pts[n].x / scale - trans.x,
											pts[n].y / scale - trans.y), false);
								}
								
								// Translates the control points
								var points = g.points;
								
								if (points != null)
								{
									for (var j = 0; j < points.length; j++)
									{
										points[j].x += dx;
										points[j].y += dy;
									}
								}
							}
							else
							{
								g.x += dx;
								g.y += dy;
							}
						}
					}
				}
			}
		}
		else
		{
			clones = [];
		}
	}
	   for(var i=0;i<clones.length;i++)
	    {
	       if(clones[i].getValue()!=null&&!(clones[i].getValue() instanceof Object))
	       {	       	
            //恢复简化后的value
	        if(!(clones[i].getValue() instanceof Object)&&(clones[i].getValue().substring(0,5)=="Throw"||clones[i].getValue().substring(0,5)=="Catch"))
		       clones[i].setValue("Intermediate"+clones[i].getValue()+"Event");
	        else if(clones[i].getValue()=='Process')
	           clones[i].setValue("Pool");
	        else if(clones[i].getValue()=='Participant')
		       clones[i].setValue("Lane");
		       
			var valueObject=eval('new ' + clones[i].getValue()+'()');		
			if(!(valueObject instanceof UserTask)&&!(valueObject instanceof Activity)&&!(valueObject instanceof Annotation)&&!(valueObject instanceof Pool)&&!(valueObject instanceof Lane))
			clones[i].setStyle(clones[i].getStyle()+";noLabel=1");
			if(valueObject.label.length>15)
			{
				var temp=valueObject.label;
				if(valueObject instanceof Annotation)
				 temp=valueObject.text;
				var arr = new Array();
                arr = temp.split(' ');
			   if(arr.length>1)
			   {
			   	 valueObject.label=arr[0];
			   	 for(var j=1;j<arr.length;j++)
			   	 {
			       valueObject.label=valueObject.label+"\n"+arr[j];
			   	 }
			   }
			   else
			     valueObject.label=valueObject.label.substring(0,15)+"\n"+valueObject.label.substring(15,valueObject.label.length);
			}
			clones[i].setValue(valueObject);
	       }
	        else if(clones[i].getValue()!=null&&clones[i].getValue() instanceof Object)
	       {
			 var valueName = clones[i].getValue().constructor.toString();
             if(valueName.indexOf('function') == -1){  
                 return null;  
              }else{  
               valueName = valueName.replace('function','');  
                 var idx = valueName.indexOf('(');  
                 valueName = valueName.substring(0, idx);  
                  valueName = valueName.replace(" ", "");  
              }  
              var valueObject=eval('new ' + valueName+'()');
              if(valueObject instanceof ServiceTask)
              {
              	valueObject.name=cells[0].value.name;
              	valueObject.label=cells[0].value.name;
              	valueObject.extendClass=cells[0].value.extendClass;
              	valueObject.method=cells[0].value.method;
              	valueObject.fieldExtensions=cells[0].value.fieldExtensions;
              }
			if(!(valueObject instanceof UserTask)&&!(valueObject instanceof Activity)&&!(valueObject instanceof Annotation)&&!(valueObject instanceof Pool)&&!(valueObject instanceof Lane)&&(cells[i].getStyle().indexOf("noLabel=1")==-1))
			clones[i].setStyle(clones[i].getStyle()+";noLabel=1");
			if(!(valueObject.label instanceof Object)&&valueObject.label.length>15)
			{
				var temp=valueObject.label;
				var arr = new Array();
                arr = temp.split(' ');
			   if(arr.length>1)
			   {
			   	 valueObject.label=arr[0];
			   	 for(var j=1;j<arr.length;j++)
			   	 {
			       valueObject.label=valueObject.label+"\n"+arr[j];
			   	 }
			   }
			   else
			     valueObject.label=valueObject.label.substring(0,15)+"\n"+valueObject.label.substring(15,valueObject.label.length);
			}
			clones[i].setValue(valueObject);
	       }
       }
	return clones;
};

/**
 * Function: resizeCells
 * 
 * Sets the bounds of the given cells and fires a <mxEvent.RESIZE_CELLS>
 * event while the transaction is in progress. Returns the cells which
 * have been passed to the function.
 * 
 * Parameters:
 * 
 * cells - Array of <mxCells> whose bounds should be changed.
 * bounds - Array of <mxRectangles> that represent the new bounds.
 */
mxGraph.prototype.resizeCells = function(cells, bounds)
{
	this.model.beginUpdate();
	try
	{
		this.cellsResized(cells, bounds);
		for(var i=0;i<cells.length;i++)
		{				   
	        for(var j=0;j<cells[i].children.length;j++)
		   {
		      if(cells[i].children[j].value=="")
		      {
			      if(cells[i].value instanceof SubProcess)
			      {
				    var tempGeo = this.model.getGeometry(cells[i].children[j]);
				    tempGeo.offset.x =cells[i].geometry.width-40;
				    cells[i].children[j].geometry=tempGeo;
			      }
			      else if(cells[i].value instanceof Activity)
			      {
				    var tempGeo = this.model.getGeometry(cells[i].children[j]);
				    tempGeo.offset.x =cells[i].geometry.width/12;
				    tempGeo.offset.y =cells[i].geometry.height/10;
				    cells[i].children[j].geometry=tempGeo;
			      }
			      else
			      {
			      	var tempGeo = this.model.getGeometry(cells[i].children[j]);
				    tempGeo.offset.x =cells[i].geometry.width/2-8;
				    tempGeo.offset.y =cells[i].geometry.height/2-7;
				    cells[i].children[j].geometry=tempGeo;
			      }
		     }
		   }
		}
		
		this.fireEvent(new mxEventObject(mxEvent.RESIZE_CELLS,
				'cells', cells, 'bounds', bounds));
	}
	finally
	{
		this.model.endUpdate();
	}

	return cells;
};