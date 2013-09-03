BpmnConnectionHandler = function(graph)
{
	mxConnectionHandler.call(this, graph);
};

// BpmnConnectionHandler inherits from mxConnectionHandler
mxUtils.extend(BpmnConnectionHandler, mxConnectionHandler);

BpmnConnectionHandler.prototype.connect = function(source, target, evt, dropTarget)
{
	if (target != null|| this.isCreateTarget() || this.graph.allowDanglingEdges)
	{
		// Uses the common parent of source and target or
		// the default parent to insert the edge
		var model = this.graph.getModel();
		var edge = null;

		model.beginUpdate();
		try
		{
			if (source != null&& target == null && this.isCreateTarget())
			{
				target = this.createTargetVertex(evt, source);
				
				if (target != null)
				{
					dropTarget = this.graph.getDropTarget([target], evt, dropTarget);
					
					// Disables edges as drop targets if the target cell was created
					// FIXME: Should not shift if vertex was aligned (same in Java)
					if (dropTarget == null || !this.graph.getModel().isEdge(dropTarget))
					{
						var pstate = this.graph.getView().getState(dropTarget);
						
						if (pstate != null)
						{
							var tmp = model.getGeometry(target);
							tmp.x -= pstate.origin.x;
							tmp.y -= pstate.origin.y;
						}
					}
					else
					{
						dropTarget = this.graph.getDefaultParent();
					}
						
					this.graph.addCell(target, dropTarget);
				}
			}

			var parent = this.graph.getDefaultParent();

			if (source != null && target != null&&
				model.getParent(source) == model.getParent(target) &&
				model.getParent(model.getParent(source)) != model.getRoot())
			{
				parent = model.getParent(source);

				if ((source.geometry != null && source.geometry.relative) &&
					(target.geometry != null && target.geometry.relative))
				{
					parent = model.getParent(parent);
				}
			}
			
			// Uses the value of the preview edge state for inserting
			// the new edge into the graph
			var value = null;
			var style = null;
			
			if (this.edgeState != null)
			{
				value = this.edgeState.cell.value;
				style = this.edgeState.cell.style+";noLabel=1";
			}
			else
			{
				value = eval('new SequenceFlow ()');   
				style = "connector;startArrow=none;endArrow=block;strokeWidth=1.2;fontFamily=ËÎÌו;fontSize=12;noLabel=1;edgeStyle=segmentEdgeStyle";
			}
			if(this.isValidSource(source)&&this.graph.isValidTarget(target)&&source!=target)
			edge = this.insertEdge(parent, null, value, source, target, style);
			
			if (edge != null)
			{
				// Updates the connection constraints
				this.graph.setConnectionConstraint(edge, source, true, this.sourceConstraint);
				this.graph.setConnectionConstraint(edge, target, false, this.constraintHandler.currentConstraint);
				
				// Uses geometry of the preview edge state
				if (this.edgeState != null)
				{
					model.setGeometry(edge, this.edgeState.cell.geometry);
				}
				
				// Makes sure the edge has a non-null, relative geometry
				var geo = model.getGeometry(edge);

				if (geo == null)
				{
					geo = new mxGeometry();
					geo.relative = true;
					
					model.setGeometry(edge, geo);
				}
				
				// Uses scaled waypoints in geometry
				if (this.waypoints != null && this.waypoints.length > 0)
				{
					var s = this.graph.view.scale;
					var tr = this.graph.view.translate;
					geo.points = [];
					
					for (var i = 0; i < this.waypoints.length; i++)
					{
						var pt = this.waypoints[i];
						geo.points.push(new mxPoint(pt.x / s - tr.x, pt.y / s - tr.y));
					}
				}

				if (target == null)
				{
					var pt = this.graph.getPointForEvent(evt, false);
					pt.x -= this.graph.panDx / this.graph.view.scale;
					pt.y -= this.graph.panDy / this.graph.view.scale;
					geo.setTerminalPoint(pt, false);
				}
				
				this.fireEvent(new mxEventObject(mxEvent.CONNECT,
						'cell', edge, 'event', evt, 'target', dropTarget));
			}
		}
		catch (e)
		{
			mxLog.show();
			mxLog.debug(e.message);
		}
		finally
		{
			model.endUpdate();
		}
		
		if (this.select)
		{
			this.selectCells(edge, target);
		}
	}
};