package uap.workflow.modeler.sheet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import nc.ui.pub.flowdesigner.editor.ColorConstants;
import nc.ui.wfengine.designer.editors.ICustomRendererEdit;
import nc.ui.wfengine.sheet.editor.AbstractPropertyEditor;
import nc.ui.wfengine.sheet.swing.HeaderlessColumnResizer;

/**
 * 属性单所使用的表。 <br>
 * 
 * @author 雷军 2004-5-6
 */
public class PropertySheetTable extends JTable {

	private static final TableCellRenderer categoryRenderer = new CategoryRenderer();

	private static final TableCellRenderer propertyRenderer = new PropertyRenderer();

	private static final int HOTSPOT_SIZE = 18;

	private static final String BACKGROUND_COLOR_KEY = "Panel.background";

	private PropertyEditorRegistry registry;

	private PropertyRendererRegistry renderers;

	public PropertySheetTable() {
		this(new PropertySheetTableModel());
	}

	public PropertySheetTable(PropertySheetTableModel dm) {
		super(dm);
		addMouseListener(new CategoryVisibilityToggle());

		setGridColor(ColorConstants.GRIDCOLOR);

		// select only one property at a time
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// hide the table header, we do not need it
		Dimension nullSize = new Dimension(0, 0);
		
		getTableHeader().setPreferredSize(nullSize);
		getTableHeader().setMinimumSize(nullSize);
		getTableHeader().setMaximumSize(nullSize);
		getTableHeader().setVisible(false);

		// table header not being visible, make sure we can still resize
		// the
		// columns
		new HeaderlessColumnResizer(this);

		// default renderers and editors
		setRendererRegistry(new PropertyRendererRegistry());
		setEditorRegistry(new PropertyEditorRegistry());
	
	}
	//默认值返回16pixle,导致参照按钮显示不全
	public int getRowHeight() {
		return 19;
	}

	public final void setEditorRegistry(PropertyEditorRegistry registry) {
		this.registry = registry;
	}

	public final PropertyEditorRegistry getEditorRegistry() {
		return registry;
	}

	public final void setRendererRegistry(PropertyRendererRegistry registry) {
		this.renderers = registry;
	}

	public final PropertyRendererRegistry getRendererRegistry() {
		return renderers;
	}

	public final boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}

		Object o = ((PropertySheetTableModel) getModel()).getPropertySheetElement(row);
		if (o instanceof Property) {
			return ((Property) o).isEditable();
		} else {
			return false;
		}
	}

	/**
	 * Gets the CellEditor for the given row and column. It uses the editor
	 * registry to find a suitable editor for the property.
	 */
	public final TableCellEditor getCellEditor(int row, int column) {
		if (column == 0) {
			return null;
		}

		Object o = ((PropertySheetTableModel) getModel()).getPropertySheetElement(row);
		if (o instanceof Property) {
			Property prop = (Property) o;
			AbstractPropertyEditor editor = (AbstractPropertyEditor) getEditorRegistry().getEditor(prop);
			if (editor != null) {
				return new CellEditorAdapter(editor);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Gets the cell renderer for the given row and column. It overrides the
	 * default behaviour to return custom renderers for "Category" rows, for
	 * "Property" columns. For the cell containing the value of the property, it
	 * calls {@link #getDefaultCellRenderer(java.lang.Class)}with the type of
	 * the property.
	 */
	public final TableCellRenderer getCellRenderer(int row, int column) {
		Object o = ((PropertySheetTableModel) getModel()).getPropertySheetElement(row);
		if (o instanceof PropertySheetTableModel.Category) {
			return categoryRenderer;
		} else if (column == 0) {
			return propertyRenderer;
		} else if (column == 1) {
			Property property = (Property) o;
			TableCellRenderer renderer = getRendererRegistry().getRenderer(property);
			AbstractPropertyEditor editor = (AbstractPropertyEditor) getEditorRegistry().getEditor(property);
			if (editor != null && editor instanceof ICustomRendererEdit) {
				renderer = ((ICustomRendererEdit) editor).getRender();
			}
			if (renderer == null) {
				renderer = getCellRenderer(property.getType());
			}
			return new WrappedRenderer(renderer);
		} else {
			return super.getCellRenderer(row, column);
		}
	}

	/**
	 * Lookup for renderer is: look for the type look for the super super type
	 * use the table default Object renderer
	 * 
	 * @param type
	 * @return
	 */
	private TableCellRenderer getCellRenderer(Class type) {
		TableCellRenderer renderer = getRendererRegistry().getRenderer(type);
		// if no renderer for the type, try with the supertype
		if (renderer == null && type != null) {
			renderer = getCellRenderer(type.getSuperclass());
		}
		if (renderer == null) {
			renderer = super.getDefaultRenderer(Object.class);
		}
		return renderer;
	}

	public final PropertySheetTableModel getSheetModel() {
		return (PropertySheetTableModel) getModel();
	}

	/**
	 * Overriden to register a listener on the model. This listener ensures
	 * editing is cancelled when editing row is being changed.
	 * 
	 * @see javax.swing.JTable#setModel(javax.swing.table.TableModel)
	 */
	public void setModel(TableModel dataModel) {
		TableModel oldModel = getModel();
		if (oldModel != null) {
			oldModel.removeTableModelListener(cancelEditing);
		}
		super.setModel(dataModel);
		dataModel.addTableModelListener(cancelEditing);
	}

	/**
	 * Cancel editing when editing row is changed
	 */
	private transient TableModelListener cancelEditing = new TableModelListener() {

		public void tableChanged(TableModelEvent e) {
			// if the editing row has been changed by an external event, it
			// is better to cancel the editing of the row as our editor may
			// no longer be the right one. It happens when you play with the
			// sorting while having the focus in one editor.
			if (e.getType() == TableModelEvent.UPDATE) {
				int first = e.getFirstRow();
				int last = e.getLastRow();
				int editingRow = PropertySheetTable.this.getEditingRow();
				TableCellEditor editor = PropertySheetTable.this.getCellEditor();
				if (editor != null && editingRow >= first && editingRow <= last) {
					editor.cancelCellEditing();
				}
			}
		}
	};

	private static class CategoryVisibilityToggle extends MouseAdapter {

		public void mouseReleased(MouseEvent event) {
			// did we click on a Category?
			PropertySheetTable table = (PropertySheetTable) event.getComponent();
			int row = table.rowAtPoint(event.getPoint());
			int column = table.columnAtPoint(event.getPoint());
			if (row != -1 && column == 0 && event.getX() < HOTSPOT_SIZE) {
				Object o = table.getSheetModel().getPropertySheetElement(row);
				if (o instanceof PropertySheetTableModel.Category) {
					((PropertySheetTableModel.Category) o).toggle();
				}
			}
		}
	}

	private static class CellBorder implements Border {

		private Color background;

		private boolean showToggle;

		private boolean toggleState;

		private Icon expandedIcon = (Icon) UIManager.get("Tree.expandedIcon");

		private Icon collapsedIcon = (Icon) UIManager.get("Tree.collapsedIcon");

		public CellBorder(Color background) {
			this.background = background;
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(1, HOTSPOT_SIZE, 1, 1);
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Color oldColor = g.getColor();
			g.setColor(background);
			g.fillRect(x, y, x + HOTSPOT_SIZE - 2, y + height);
			g.setColor(oldColor);

			if (showToggle) {
				if (toggleState) {
					expandedIcon.paintIcon(c, g, x + (HOTSPOT_SIZE - 2 - expandedIcon.getIconWidth()) / 2, y + (height - expandedIcon.getIconHeight()) / 2);
				} else {
					collapsedIcon.paintIcon(c, g, x + (HOTSPOT_SIZE - 2 - collapsedIcon.getIconWidth()) / 2, y + (height - collapsedIcon.getIconHeight()) / 2);
				}
			}
		}

		public boolean isBorderOpaque() {
			return true;
		}

		public void setToggle(boolean state) {
			toggleState = state;
		}

		public void setToggleVisible(boolean visible) {
			showToggle = visible;
		}
	}

	private static class PropertyRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Property property = (Property) value;
			setText(property.getDisplayName());
			if (!isSelected) {
				setEnabled(property.isEditable());
			} else {
				setEnabled(true);
			}
			setBorder(new CellBorder(UIManager.getColor(BACKGROUND_COLOR_KEY)));
			return this;
		}
	}

	private static class WrappedRenderer implements TableCellRenderer {

		private TableCellRenderer renderer;

		public WrappedRenderer(TableCellRenderer renderer) {
			this.renderer = renderer;
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			return renderer.getTableCellRendererComponent(table, value, false, false, row, column);
		}
	}

	private static class CategoryRenderer extends DefaultTableCellRenderer {

		private Color background;

		private Color foreground;

		public CategoryRenderer() {
			this(UIManager.getColor(BACKGROUND_COLOR_KEY), UIManager.getColor(BACKGROUND_COLOR_KEY).darker());
		}

		public CategoryRenderer(Color background, Color foreground) {
			setBorder(new CellBorder(UIManager.getColor(BACKGROUND_COLOR_KEY)));
			this.background = background;
			this.foreground = foreground;
			setFont(getFont().deriveFont(Font.BOLD));
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

			PropertySheetTableModel.Category category = (PropertySheetTableModel.Category) value;

			setBackground(background);
			setForeground(foreground);

			if (column == 0) {
				((CellBorder) getBorder()).setToggle(category.isVisible());
				((CellBorder) getBorder()).setToggleVisible(true);
				setText(category.getName());
			} else {
				((CellBorder) getBorder()).setToggleVisible(false);
				setText("");
			}
			return this;
		}
	}

}
