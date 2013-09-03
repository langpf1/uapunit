BpmnGraphHandler = function(graph)
{
	mxGraphHandler.call(this, graph);
	this.boundaryActived=false;
};

// BpmnGraphHandler inherits from mxGraphHandler
mxUtils.extend(BpmnGraphHandler, mxGraphHandler);
/**
 * Function: mouseUp
 * 
 * Handles the event by applying the changes to the selection cells.
 */

BpmnGraphHandler.prototype.mouseUp = function(sender, me)
{
	//��󣬽�����߽��BoundaryEvent�ָ�ΪIntermediateCatchEvent
	if(!this.boundaryActived&&this.cell != null)
	{
		if(this.cell.getValue()=="")//��ǰѡ��cell��valueֵΪ�գ���ʾѡ�е���cell�ڲ���ͼƬ������ת��Ϊ��parent
		this.cell=this.cell.parent;
		var valueName = this.cell.getValue().constructor.toString();
        if(valueName.indexOf('function') != -1&&valueName.indexOf('Boundary')!=-1)//�жϵ�ǰcell�Ƿ�ΪBoundaryEvent
        {  
           valueName = valueName.replace('function','');  
           var idx = valueName.indexOf('(');  
           valueName = valueName.substring(0, idx);  
           valueName = valueName.replace(" ", ""); 
           var idBoundary=valueName.indexOf('Boundary');
           valueName = valueName.substring(0, idBoundary);
           valueName="IntermediateCatch"+valueName+"Event";
           var temp=this.cell.value;
		   this.cell.value=eval("new "+valueName+"()");//��cell��value�ָ�ΪIntermediateCatchEvent
		   this.cell.value.id=temp.id;
		   this.cell.value.name=temp.name;
		   this.cell.value.documention=temp.documention;
        }
    }
	if (!me.isConsumed())
	{

		var graph = this.graph;	
		if (this.cell != null && this.first != null && this.shape != null &&
			this.currentDx != null && this.currentDy != null)
		{
			
			var scale = graph.getView().scale;
			var clone = graph.isCloneEvent(me.getEvent()) && graph.isCellsCloneable() && this.isCloneEnabled();
			var dx = this.currentDx / scale;
			var dy = this.currentDy / scale;

			var cell = me.getCell();
			if(this.boundaryActived&&cell!=null&& this.cell!=cell&&(this.cell.parent.value instanceof IntermediateCatchEvent||this.cell.value instanceof IntermediateCatchEvent))//���ƶ����߽��ϵ�IntermediateCatchEventת��ΪBoundaryEvent
			 {
			 	if(this.cell.parent.value instanceof IntermediateCatchEvent)//��ǰcellΪͼƬcell�������丸ΪCatchEvent
			  	{
			  		
			  		var valueName = this.cell.parent.value.constructor.toString();//��ȡ��ǰcell��value��������
                    valueName = valueName.replace('function','');  
                    var idx = valueName.indexOf('(');  
                    valueName = valueName.substring(0, idx);  
                    valueName = valueName.replace(" ", "");
                    if(valueName.indexOf('BoundaryEvent')==-1)//��ǰcellΪIntermediateCatchEvent
                    {
                      var idBoundary=valueName.indexOf('Event');
                      valueName = valueName.substring(17, idBoundary);
                      valueName=valueName+"BoundaryEvent"; 
                      var temp=this.cell.parent.value;
		              this.cell.parent.value=eval("new "+valueName+"()");
			          this.cell.parent.value.id=temp.id;
	                  this.cell.parent.value.name=temp.name;
		              this.cell.parent.value.documention=temp.documention;
			          this.cell.parent.value.attachedToRef= cell.value.id;
                    }
                    else //��ǰcellΪBoundaryEvent,����Դ�����ı�
                    {
                    	 this.cell.parent.value.attachedToRef= cell.value.id;
                    }
			  	}
			  	else if(this.cell.value instanceof IntermediateCatchEvent)//��ǰcellΪCatchEvent
			  	{
			     	var valueName = this.cell.value.constructor.toString();//��ȡ��ǰcell��value��������
                    valueName = valueName.replace('function','');  
                    var idx = valueName.indexOf('(');  
                    valueName = valueName.substring(0, idx);  
                    valueName = valueName.replace(" ", ""); 
                    if(valueName.indexOf('BoundaryEvent')==-1)//��ǰcellΪIntermediateCatchEvent
                    {
                      var idBoundary=valueName.indexOf('Event');
                      valueName = valueName.substring(17, idBoundary);
                      valueName=valueName+"BoundaryEvent";
                      var temp=this.cell.value;
		              this.cell.value=eval("new "+valueName+"()");
		              this.cell.value.id=temp.id;
	                  this.cell.value.name=temp.name;
		              this.cell.value.documention=temp.documention;
			          this.cell.value.attachedToRef= cell.value.id;
                    }
                     else //��ǰcellΪBoundaryEvent,����Դ�����ı�
                    {
                    	 this.cell.value.attachedToRef= cell.value.id;
                    }
			  	}
			 }
			 
			if (this.connectOnDrop && this.target == null && cell != null && graph.getModel().isVertex(cell) &&
				graph.isCellConnectable(cell) && graph.isEdgeValid(null, this.cell, cell))
			{
				graph.connectionHandler.connect(this.cell, cell, me.getEvent());
			}
			else
			{
				
				var target = this.target;
				
				if (graph.isSplitEnabled() && graph.isSplitTarget(target, this.cells, me.getEvent()))
				{
					graph.splitEdge(target, this.cells, null, dx, dy);
				}
				else 
				{

                    var cloneCell=new Array();
				    var j=0;
				    for(var i=0;i<this.cells.length;i++)
				    {
					if(!this.cells[i].edge)
					cloneCell[j++]=this.cells[i];
				    }
				    var parent = this.cell.parent;
				    if(this.cell.value=="")//��ǰѡ��cell��valueֵΪ�գ���ʾѡ�е���cell�ڲ���ͼƬ������ת��Ϊ��parent
				    {
				      this.cell=parent;
				      parent=parent.parent;
				    }
				    for(var i=0;i<parent.getChildCount();i++)//ʹ�����߽��ϵ�cell���������ƶ�
				    {
					  var cellValue=parent.getChildAt(i).getValue();
					  var valueName = cellValue.constructor.toString();
					  if((valueName.indexOf('function')!= -1)&&(valueName.indexOf('BoundaryEvent') != -1)&&cellValue.attachedToRef==this.cell.getValue().id)
					   {
						cloneCell[j++]=parent.getChildAt(i);
					   }
				   }
				    if (cloneCell.length > 0)
					this.moveCells(cloneCell, dx, dy, clone, this.target, me.getEvent());
				}
			}
		}
		else if (this.isSelectEnabled() && this.delayedSelection && this.cell != null)
		{
			this.selectDelayed(me);
		}
	}

	// Consumes the event if a cell was initially clicked
	if (this.cellWasClicked)
	{
		me.consume();
	}
	
	this.reset();
};

//�ж�cell�Ƿ���container������
BpmnGraphHandler.prototype.contains = function(container,cell){ 
	var w = container.geometry.width;
	var h = container.geometry.height;
	var W=cell.geometry.width;
	var H=cell.geometry.height;
	if ((w | h | W | H) < 0) {
	    // At least one of the dimensions is negative...
	    return false;
	}
	// Note: if any dimension is zero, tests below must return false...
	var x = container.geometry.x;
	var y = container.geometry.y;
	var X=cell.geometry.x;
	var Y=cell.geometry.y;
	if (X < x || Y < y) {
	    return false;
	}
	w += x;
	W += X;
	if (W <= X) {
	    // X+W overflowed or W was zero, return false if...
	    // either original w or W was zero or
	    // x+w did not overflow or
	    // the overflowed x+w is smaller than the overflowed X+W
	    if (w >= x || W > w) return false;
	} else {
	    // X+W did not overflow and W was not zero, return false if...
	    // original w was zero or
	    // x+w did not overflow and x+w is smaller than X+W
	    if (w >= x && W > w) return false;
	}
	h += y;
	H += Y;
	if (H <= Y) {
	    if (h >= y || H > h) return false;
	} else {
	    if (h >= y && H > h) return false;
	}
	return true;
    }
/**
 * Function: mouseMove
 * 
 * Handles the event by highlighting possible drop targets and updating the
 * preview.
 */
mxGraphHandler.prototype.mouseMove = function(sender, me)
{
	var graph = this.graph;
	if (!me.isConsumed() && graph.isMouseDown && this.cell != null&&
		this.first != null && this.bounds != null)
	{
		var point = mxUtils.convertPoint(graph.container, me.getX(), me.getY());
		var dx = point.x - this.first.x;
		var dy = point.y - this.first.y;
		var tol = graph.tolerance;
		
		if (this.shape!= null || Math.abs(dx) > tol || Math.abs(dy) > tol)
		{
			if(this.cell!=null)
	        {
		        if(this.cell.value=="")//��ȡcell��value���������
		              var valueName = this.cell.parent.value.constructor.toString();
		        else
		              var valueName = this.cell.value.constructor.toString();
		        if((valueName.indexOf('function')!= -1)&&(valueName.indexOf('BoundaryEvent') != -1)) //����ǰ�ƶ�cellΪBoundaryEvent����߽��ʶ��Ϊ��Ϊ����ṩ�ж�����
		           this.boundaryActived=false;
	        }
			// Highlight is used for highlighting drop targets
			if (this.highlight == null)
			{
				this.highlight = new mxCellHighlight(this.graph,
					mxConstants.DROP_TARGET_COLOR, 3);
			}
			
			if (this.shape == null)
			{
				this.shape = this.createPreviewShape(this.bounds);
			}
			
			var gridEnabled = graph.isGridEnabledEvent(me.getEvent());
			var hideGuide = true;
			
			if (this.guide != null && this.useGuidesForEvent(me))
			{
				var delta = this.guide.move(this.bounds, new mxPoint(dx, dy), gridEnabled);
				hideGuide = false;
				dx = delta.x;
				dy = delta.y;
			}
			else if (gridEnabled)
			{
				var trx = graph.getView().translate;
				var scale = graph.getView().scale;				
				
				var tx = this.bounds.x - (graph.snap(this.bounds.x / scale - trx.x) + trx.x) * scale;
				var ty = this.bounds.y - (graph.snap(this.bounds.y / scale - trx.y) + trx.y) * scale;
				var v = this.snap(new mxPoint(dx, dy));
			
				dx = v.x - tx;
				dy = v.y - ty;
			}
			
			if (this.guide != null && hideGuide)
			{
				this.guide.hide();
			}

			// Constrained movement if shift key is pressed
			if (graph.isConstrainedEvent(me.getEvent()))
			{
				if (Math.abs(dx) > Math.abs(dy))
				{
					dy = 0;
				}
				else
				{
					dx = 0;
				}
			}

			this.currentDx = dx;
			this.currentDy = dy;

			this.updatePreviewShape();

			var target = null;
			var cell = me.getCell();

			if (graph.isDropEnabled() && this.highlightEnabled)
			{
				// Contains a call to getCellAt to find the cell under the mouse
				target = graph.getDropTarget(this.cells, me.getEvent(), cell);
			}

			// Checks if parent is dropped into child
			var parent = target;
			var model = graph.getModel();
			
			while (parent != null && parent != this.cells[0])
			{
				parent = model.getParent(parent);
			}
			
			var clone = graph.isCloneEvent(me.getEvent()) && graph.isCellsCloneable() && this.isCloneEnabled();
			var state = graph.getView().getState(target);
			var highlight = false;
			
			if(target!=null)
			{
				//cell��geometry�洢�����ڸ������е����λ��
				var positionX=target.geometry.x;
				var positionY=target.geometry.y;
				var tempTarget=target;
				while(tempTarget.parent.id!="1")//��ȡtarget����λ��
				{
					positionX=positionX+tempTarget.parent.geometry.x; 
					positionY=positionY+tempTarget.parent.geometry.y;
					tempTarget=tempTarget.parent;
				}
				 var positionTarget=new mxCell("",new mxGeometry(positionX, positionY,target.geometry.width, target.geometry.height), "");//���ɾ���λ��target
				 var extendTarget=new mxCell("",new mxGeometry(positionX-36,positionY-36, target.geometry.width+72,target.geometry.height+72), "");//�������λ��target
				 var temp=new mxCell("", new mxGeometry(me.graphX-18, me.graphY-18, 36,36), "");
				 if(target.value instanceof SubProcess||target.value instanceof Activity)//ֻ��SubProcess��Activity�ܹ�ӵ�б߽�cell
			     {
			        if((model.getParent(this.cell) != target|| clone)&&!this.contains(positionTarget,temp)&&this.contains(extendTarget,temp))//�ж�cell�Ƿ���target�߽�
			       {
			         if(this.cell.value instanceof IntermediateCatchEvent||(this.cell.parent!=null&&this.cell.parent.value instanceof IntermediateCatchEvent))//�ж�cell�����Ƿ�Ϊ�ܹ�ת���ɱ߽�cell������
			         {
				        this.setHighlightColor(mxConstants.DROP_BOUNDARY_COLOR);//target�߽���Ϊ��ɫ
				        this.boundaryActived=true; 
			            highlight = true;//���ñ߽��־Ϊtrue
			         }					
			       }
			    }
			    else
			    {
			   	  this.boundaryActived=false;//���ñ߽��־Ϊfalse
			    }
			}
		    if (state != null && parent == null && (model.getParent(this.cell) != target || clone)&&target!=null&&this.contains(positionTarget,temp))//�ж�cell�Ƿ���target�ڲ�
			{
				this.boundaryActived=false;
				if((target.value instanceof SubProcess)||(target.value instanceof Pool)||(target.value instanceof Lane))//�ж�target�Ƿ�Ϊ����
				{
			      if (this.target != target)
			      {
				    this.target = target;
				    this.setHighlightColor(mxConstants.DROP_TARGET_COLOR);//target�߽���Ϊ��ɫ
				  }
			    
			      highlight = true;
				}
			}
			//�����¼��ʧ�������¼�target
			else if(state != null && parent == null && (model.getParent(this.cell) != target || clone)&&target!=null&&((target.parent.value instanceof SubProcess)||(target.parent.value instanceof Pool)||(target.parent.value instanceof Lane)))
			{
				this.target = target.parent;
			}
			else
			{
				this.target = null;

				if (this.connectOnDrop && cell != null && this.cells.length == 1 &&
					graph.getModel().isVertex(cell) && graph.isCellConnectable(cell))
				{
					state = graph.getView().getState(cell);
					
					if (state != null)
					{
						var error = graph.getEdgeValidationError(null, this.cell, cell);
						var color = (error == null) ?
							mxConstants.VALID_COLOR :
							mxConstants.INVALID_CONNECT_TARGET_COLOR;
						this.setHighlightColor(color);
						highlight = true;
					}
				}
			}

			if (state != null && highlight)
			{
				this.highlight.highlight(state);
			}
			else
			{
				this.highlight.hide();
			}
		}

		me.consume();
		
		// Cancels the bubbling of events to the container so
		// that the droptarget is not reset due to an mouseMove
		// fired on the container with no associated state.
		mxEvent.consume(me.getEvent());
	}
	else if ((this.isMoveEnabled() || this.isCloneEnabled()) && this.updateCursor &&
		!me.isConsumed() && me.getState() != null && !graph.isMouseDown)
	{

		var cursor = graph.getCursorForCell(me.getCell());
		
		if (cursor == null && graph.isEnabled() && graph.isCellMovable(me.getCell()))
		{
			if (graph.getModel().isEdge(me.getCell()))
			{
				cursor = mxConstants.CURSOR_MOVABLE_EDGE;
			}
			else
			{
				cursor = mxConstants.CURSOR_MOVABLE_VERTEX;
			}
		}

		me.getState().setCursor(cursor);
		me.consume();
	}
};