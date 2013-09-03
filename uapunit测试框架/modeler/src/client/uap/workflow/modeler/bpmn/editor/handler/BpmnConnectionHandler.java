package uap.workflow.modeler.bpmn.editor.handler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.utils.BpmnModelerConstants;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxCellTracker;
import com.mxgraph.swing.handler.mxConnectPreview;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxCellState;

public class BpmnConnectionHandler extends mxConnectionHandler {

	public BpmnConnectionHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
		initializeDefaultStyle();
	}
	
	public void start(MouseEvent e, mxCellState state)
	{
		first = e.getPoint();
		String style = ";" + mxConstants.STYLE_NOEDGESTYLE+"=1";;
		if(e.isShiftDown()){
			style ="";
		}
		connectPreview.start(e, state, style);
	}
	
	protected mxConnectPreview createConnectPreview()
	{
		return new BpmnConnectPreview(graphComponent);
	}
	
	public void mousePressed(MouseEvent e)
	{
		if (!graphComponent.isForceMarqueeEvent(e)
				&& !graphComponent.isPanningEvent(e)
				&& !e.isPopupTrigger()
				&& graphComponent.isEnabled()
				&& isEnabled()
				&& !e.isConsumed()
				&& ((isHighlighting() && marker.hasValidState()) || (!isHighlighting()
						&& bounds != null && bounds.contains(e.getPoint())))&&isEdgeEntrySelected())
		{
			start(e, marker.getValidState());
			e.consume();
		}
	}
	
	public void mouseMoved(MouseEvent e)
	{
		mouseDragged(e);

		if (isHighlighting() && !marker.hasValidState())
		{
			source = null;
		}

		if (!isHighlighting() && source != null)
		{
			int imgWidth = handleSize;
			int imgHeight = handleSize;

			if (connectIcon != null)
			{
				imgWidth = connectIcon.getIconWidth();
				imgHeight = connectIcon.getIconHeight();
			}

			int x = (int) source.getCenterX() - imgWidth / 2;
			int y = (int) source.getCenterY() - imgHeight / 2;

			if (graphComponent.getGraph().isSwimlane(source.getCell()))
			{
				mxRectangle size = graphComponent.getGraph().getStartSize(
						source.getCell());

				if (size.getWidth() > 0)
				{
					x = (int) (source.getX() + size.getWidth() / 2 - imgWidth / 2);
				}
				else
				{
					y = (int) (source.getY() + size.getHeight() / 2 - imgHeight / 2);
				}
			}

			setBounds(new Rectangle(x, y, imgWidth, imgHeight));
		}
		else
		{
			setBounds(null);
		}

		if (source != null && (bounds == null || bounds.contains(e.getPoint())))
		{
//			graphComponent.getGraphControl().setCursor(CONNECT_CURSOR);
			e.consume();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if (!e.isConsumed() && graphComponent.isEnabled() && isEnabled()&&isEdgeEntrySelected())
		{
			// Activates the handler
			if (!active && first != null)
			{
				double dx = Math.abs(first.getX() - e.getX());
				double dy = Math.abs(first.getY() - e.getY());
				int tol = graphComponent.getTolerance();
				
				if (dx > tol || dy > tol)
				{
					active = true;
				}
			}
			
			if (e.getButton() == 0 || (isActive() && connectPreview.isActive()))
			{
				mxCellState state = marker.process(e);
	
				if (connectPreview.isActive())
				{
					connectPreview.update(e, marker.getValidState(), e.getX(),
							e.getY());
					setBounds(null);
					e.consume();
				}
				else
				{
					source = state;
				}
			}
		}
	}
	
	private boolean isEdgeEntrySelected(){
		return ((BpmnGraphComponent)graphComponent).getSelectedEntry()==null?false:((BpmnGraphComponent)graphComponent).getSelectedEntry().isEdge();
	}
	
	private void initializeDefaultStyle(){
		 CONNECT_CURSOR = new Cursor(Cursor.HAND_CURSOR);
		 initCellMarker();
		 marker.setHotspot(BpmnModelerConstants.DEFAULT_HOTSPOT);
	}
	private void initCellMarker(){
		marker=new mxCellTracker(graphComponent, BpmnModelerConstants.VALID_COLOR){
			private static final long serialVersionUID = -3861766879112297913L;
			
			public boolean isSwimlaneContentEnabled()
			{
				return true;
			}
			
			protected Object getCell(MouseEvent e)
			{
				Object cell = super.getCell(e);
				if (isConnecting())
				{
					if (source != null)
					{
						error = validateConnection(source.getCell(), cell);

						if (error != null && error.length() == 0)
						{
							//其余情况用invalidColor渲染
							if (source.getCell() == cell)
								cell = null;

							// Enables create target inside groups
							if (createTarget)
							{
								error = null;
							}
						}
					}
				}
				else if (!isValidSource(cell))
				{
//						cell = null;
				}

				return cell;
			}

			// Sets the highlight color according to isValidConnection
			protected boolean isValidState(mxCellState state)
			{
				if (isConnecting())
				{
					return error == null;
				}
				else
				{
					return super.isValidState(state);
				}
			}
			protected Color getMarkerColor(MouseEvent e, mxCellState state,
					boolean isValid)
			{
				return (isHighlighting() || isConnecting()) ? super
						.getMarkerColor(e, state, isValid) : null;
			}
			protected boolean intersects(mxCellState state, MouseEvent e)
			{
				if (!isHighlighting() || isConnecting())
				{
					return true;
				}

				return super.intersects(state, e);
			}
			
			public boolean isHotspotEnabled()
			{
				return true;
			}
		};
	}
}
