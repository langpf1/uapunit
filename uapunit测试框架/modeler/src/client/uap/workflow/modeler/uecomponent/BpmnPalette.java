package uap.workflow.modeler.uecomponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.flowdesigner.editor.BasicGraphEditor;
import nc.ui.pub.flowdesigner.editor.CellTemplateSource;
import nc.ui.pub.flowdesigner.editor.ShadowBorder;

public class BpmnPalette extends UIPanel {

	private static final long serialVersionUID = 7771113885935187066L;

	protected JLabel selectedEntry = null;

	protected mxEventSource eventSource = new mxEventSource(this);

	private CellTemplateSource templetSource;

	private mxGraphComponent comp;

	BasicGraphEditor editor;
	
	private JLabel escLabel;

	private mxCell escCell;

	@SuppressWarnings("serial")
	public BpmnPalette(CellTemplateSource t) {
		setLayout(new FlowLayout(FlowLayout.LEADING, 5, 5));
		templetSource = t;
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				clearSelection();
			}
		});

		setTransferHandler(new TransferHandler() {
			public boolean canImport(JComponent comp, DataFlavor[] flavors) {
				return true;
			}
		});
	}

	/**
	 * 通过编程方式选中“选取工具”
	 * */
	public void setEscLable() {
		JLabel previous = selectedEntry;
		selectedEntry = escLabel;

		if (previous != null) {
			previous.setBorder(null);
			previous.setOpaque(false);
		}
		if (selectedEntry != null) {
			selectedEntry.setBorder(ShadowBorder.getSharedInstance());
			selectedEntry.setOpaque(true);
		}
		editor.setIncomingCell(escCell);
		eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",
				selectedEntry, "transferable", null, "previous", previous));
	}

	public void clearSelection() {
		setSelectionEntry(null, null);
	}

	/**
	 * 
	 */
	public void setSelectionEntry(JLabel entry, mxGraphTransferable t) {
		JLabel previous = selectedEntry;
		selectedEntry = entry;

		if (previous != null) {
			previous.setBorder(null);
			previous.setOpaque(false);
		}
		if (selectedEntry != null) {
			selectedEntry.setBorder(ShadowBorder.getSharedInstance());
			selectedEntry.setOpaque(true);
		}
		editor.setIncomingCell(t == null ? null : ((mxCell) t.getCells()[0]));
		eventSource.fireEvent(new mxEventObject(mxEvent.SELECT, "entry",selectedEntry, "transferable", t, "previous", previous));
	}

	/**
	 * 
	 */
	public void setPreferredWidth(int width) {
		int cols = width / 55;
		setPreferredSize(new Dimension(width,
				(getComponentCount() * 55 / cols) + 30));
		revalidate();
	}

	/**
	 * 
	 * @param name
	 * @param icon
	 * @param style
	 * @param width
	 * @param height
	 * @param value
	 */
	public void addEdgeTemplate(final String name, ImageIcon icon,
			String style, int width, int height, Object value) {
		mxGeometry geometry = new mxGeometry(0, 0, width, height);
		geometry.setTerminalPoint(new mxPoint(0, height), true);
		geometry.setTerminalPoint(new mxPoint(width, 0), false);
		geometry.setRelative(true);

		mxCell cell = new mxCell(value, geometry, style);
		cell.setEdge(true);

		addTemplate(name, icon, cell);
		templetSource.addTempletSource(cell);
	}

	/**
	 * 
	 * @param name
	 * @param icon
	 * @param style
	 * @param width
	 * @param height
	 * @param value
	 */
	public void addTemplate(final String name, ImageIcon icon, String style,
			int width, int height, Object value) {
		mxCell cell = new mxCell(value, new mxGeometry(0, 0, width, height),
				style);
		cell.setVertex(true);

		addTemplate(name, icon, cell);
		templetSource.addTempletSource(cell);
	}

	/**
	 * @param cell
	 * @param name
	 * @param icon
	 */
	public void addToTemplate(mxCell cell, final String name, ImageIcon icon) {
		addTemplate(name, icon, cell);
		templetSource.addTempletSource(cell);
	}

	/**
	 * 
	 * @param name
	 * @param icon
	 * @param style
	 * @param width
	 * @param height
	 * @param value
	 */
	public void addTemplate(final String name, ImageIcon icon, mxCell cell) {
		mxRectangle bounds = (mxGeometry) cell.getGeometry().clone();
		final mxGraphTransferable t = new mxGraphTransferable(
				new Object[] { cell }, bounds);

		// Scales the image if it's too large for the library
		if (icon != null) {
			// if (icon.getIconWidth() > 32 || icon.getIconHeight() > 32) {
			// icon = new ImageIcon(icon.getImage().getScaledInstance(32, 32,
			// 0));
			// }
			icon = new ImageIcon(icon.getImage());
		}

		final JLabel entry = new JLabel(icon);
		entry.setPreferredSize(new Dimension(60, 60));
		entry.setBackground(BpmnPalette.this.getBackground().brighter());
		entry.setFont(new Font(entry.getFont().getFamily(), 0, 12));

		entry.setVerticalTextPosition(JLabel.BOTTOM);
		entry.setHorizontalTextPosition(JLabel.CENTER);
		entry.setIconTextGap(0);

		entry.setToolTipText(name);
		entry.setText(name);

		if (cell.getStyle().equals("select")) {
			escLabel = entry;
			escCell = cell;
		}

		entry.addMouseListener(new MouseListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent
			 * )
			 */
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					editor.getObjectManager().setPersistent(true);
				} else {
					editor.getObjectManager().setPersistent(false);
				}
				setSelectionEntry(entry, t);

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent
			 * )
			 */
			public void mouseClicked(MouseEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent
			 * )
			 */
			public void mouseEntered(MouseEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent
			 * )
			 */
			public void mouseExited(MouseEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent
			 * )
			 */
			public void mouseReleased(MouseEvent e) {
			}

		});

		// Install the handler for dragging nodes into a graph
		DragGestureListener dragGestureListener = new DragGestureListener() {
			/**
			 * 
			 */
			public void dragGestureRecognized(DragGestureEvent e) {
				e.startDrag(null, null, new Point(), t, null);
			}

		};

		DragSource dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(entry,
				DnDConstants.ACTION_COPY, dragGestureListener);

		add(entry);
	}

	/**
	 * @param eventName
	 * @param listener
	 * @see com.mxgraph.util.mxEventSource#addListener(java.lang.String,
	 *      com.mxgraph.util.mxEventSource.mxIEventListener)
	 */
	public void addListener(String eventName, mxIEventListener listener) {
		eventSource.addListener(eventName, listener);
	}

	/**
	 * @return
	 * @see com.mxgraph.util.mxEventSource#isEventsEnabled()
	 */
	public boolean isEventsEnabled() {
		return eventSource.isEventsEnabled();
	}

	/**
	 * @param listener
	 * @see com.mxgraph.util.mxEventSource#removeListener(com.mxgraph.util.mxEventSource.mxIEventListener)
	 */
	public void removeListener(mxIEventListener listener) {
		eventSource.removeListener(listener);
	}

	/**
	 * @param eventName
	 * @param listener
	 * @see com.mxgraph.util.mxEventSource#removeListener(java.lang.String,
	 *      com.mxgraph.util.mxEventSource.mxIEventListener)
	 */
	public void removeListener(mxIEventListener listener, String eventName) {
		eventSource.removeListener(listener, eventName);
	}

	/**
	 * @param eventsEnabled
	 * @see com.mxgraph.util.mxEventSource#setEventsEnabled(boolean)
	 */
	public void setEventsEnabled(boolean eventsEnabled) {
		eventSource.setEventsEnabled(eventsEnabled);
	}

	public mxGraphComponent getComp() {
		return comp;
	}

	public void setComp(mxGraphComponent comp) {
		this.comp = comp;
	}

	public BasicGraphEditor getEditor() {
		return editor;
	}

	public void setEditor(BasicGraphEditor editor) {
		this.editor = editor;
	}
}
