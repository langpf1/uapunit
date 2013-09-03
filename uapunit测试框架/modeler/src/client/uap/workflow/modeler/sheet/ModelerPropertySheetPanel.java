package uap.workflow.modeler.sheet;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.BeanInfo;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.util.Comparator;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import uap.workflow.modeler.bpmn2.beaninfos.ExtendedGraphBeanDesriptor;
import uap.workflow.modeler.sheet.PropertyDescriptorAdapter;
import uap.workflow.modeler.utils.ModelerComponetFactory;

import nc.bs.logging.Logger;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextPane;
import nc.ui.pub.beans.UIToggleButton;
import nc.ui.wfengine.sheet.swing.LookAndFeelTweaks;
import nc.uitheme.ui.ThemeResourceCenter;

/**
 * 属性单面板 <li>1.使用一个表来编辑和显示属性值； <li>2.描述面板显示每个属性的含义； <li>3.属性值还可分组显示； <li>
 * 4.属性值还可根据名称排序，如果需要按照其他排序，须实现：
 * 
 * {@link PropertySheetTableModel#setCategorySortingComparator(Comparator)} 和
 * {@link PropertySheetTableModel#setPropertySortingComparator(Comparator)}
 * 
 * <p>
 * 修改人：雷军 2004-6-3 为actionPanel增加了一个Label
 * <p>
 * 修改人：雷军 2004-10-9 废弃了排序按钮
 * <p>
 * 修改人：雷军 2005-6-3 改用JTextPane控件来显示属性描述
 */
public class ModelerPropertySheetPanel extends UIPanel implements PropertySheet {

	private ModelerPropertySheetTable table;
	private UIScrollPane tableScroll;
	private ListSelectionListener selectionListener;

	private UIPanel actionPanel;
	private UIToggleButton asCategoryButton;
	private UIToggleButton descriptionButton;

	private JTextPane descTextPane;
	private JScrollPane descScroll;

	public ModelerPropertySheetPanel() {
		this(new ModelerPropertySheetTable());
	}

	public ModelerPropertySheetPanel(ModelerPropertySheetTable table) {
		buildUI();
		setTable(table);
	}
	
	private void setTable(ModelerPropertySheetTable table) {
		if (table == null) {
			throw new IllegalArgumentException("table must not be null");
		}
		table.getSelectionModel().addListSelectionListener(selectionListener);
		tableScroll.getViewport().setView(table);
		tableScroll.getViewport().setBackground(table.getBackground());

		// remove the listener from the old table
		if (this.table != null) {
			this.table.getSelectionModel().removeListSelectionListener(
					selectionListener);
		}

		// use the new table as our table
		this.table = table;
	}

	/**
	 * @return the table used to edit/view Properties.
	 */
	public ModelerPropertySheetTable getTable() {
		return table;
	}

	public PropertySheetTableModel getTableModel() {
		return (PropertySheetTableModel) table.getModel();
	}

	/**
	 * Toggles the visibility of the description panel.
	 * 
	 * @param visible
	 */
	public void setDescriptionVisible(boolean visible) {
		// descTextPane.setVisible(visible);
		descScroll.setVisible(visible);
		descriptionButton.setSelected(visible);
		ModelerPropertySheetPanel.this.revalidate();
	}

	/**
	 * Toggles the visibility of the toolbar panel
	 * 
	 * @param visible
	 */
	public void setToolBarVisible(boolean visible) {
		actionPanel.setVisible(visible);
		ModelerPropertySheetPanel.this.revalidate();
	}

	public void setMode(int mode) {
		getTableModel().setMode(mode);
		asCategoryButton.setSelected(PropertySheet.VIEW_AS_CATEGORIES == mode);
	}

	public void setProperties(Property[] properties) {
		getTableModel().setProperties(properties);
	}

	public Property[] getProperties() {
		return getTableModel().getProperties();
	}

	public void setBeanInfo(BeanInfo beanInfo) {
		setProperties(beanInfo.getPropertyDescriptors());
	}

	public void setProperties(PropertyDescriptor[] descriptors) {
		Property[] properties = new Property[descriptors.length];
		for (int i = 0, c = descriptors.length; i < c; i++) {
			properties[i] = new PropertyDescriptorAdapter(descriptors[i]);
		}
		getTableModel().setProperties(properties);
	}

	public Object getParentValue(String parentProperty){
		Property[] properties = getTableModel().getProperties();
		for(int j = 0; j < properties.length; j++){
			if (parentProperty.equals(properties[j].getName())){
				return properties[j].getValue();
			}
		}
		return null;
	}
	public void readFromObject(Object data) {
		Property[] properties = getTableModel().getProperties();
		
		for (int i = 0, c = properties.length; i < c; i++) {
			Property property = properties[i];
			Object obj = ((PropertyDescriptorAdapter)property).getDescriptor();
			if (obj instanceof ExtendedGraphBeanDesriptor){
			ExtendedGraphBeanDesriptor descriptor = (ExtendedGraphBeanDesriptor)obj;
			if (descriptor.getParentProperty() != null){
				Object objAttribute = getParentValue(descriptor.getParentProperty());
				properties[i].readFromObject(objAttribute);
			}else{
				properties[i].readFromObject(data);
			}
			}
		}
		repaint();
	}

	public void writeToObject(Object data) {
		Property[] properties = getProperties();
		for (int i = 0, c = properties.length; i < c; i++) {
			properties[i].writeToObject(data);
		}
	}

	public void addPropertySheetChangeListener(PropertyChangeListener listener) {
		getTableModel().addPropertyChangeListener(listener);
	}

	public void removePropertySheetChangeListener(
			PropertyChangeListener listener) {
		getTableModel().removePropertyChangeListener(listener);
	}

	public final void setEditorRegistry(PropertyEditorRegistry registry) {
		table.setEditorRegistry(registry);
	}

	public final PropertyEditorRegistry getEditorRegistry() {
		return table.getEditorRegistry();
	}

	public final void setRendererRegistry(PropertyRendererRegistry registry) {
		table.setRendererRegistry(registry);
	}

	public final PropertyRendererRegistry getRendererRegistry() {
		return table.getRendererRegistry();
	}

	/**
	 * Sets sorting of categories enabled or disabled.
	 * 
	 * @param value
	 *            true to enable sorting
	 */
	public void setSortingCategories(boolean value) {
		getTableModel().setSortingCategories(value);
	}

	/**
	 * Is sorting of categories enabled.
	 * 
	 * @return true if category sorting is enabled
	 */
	public boolean isSortingCategories() {
		return getTableModel().isSortingCategories();
	}

	/**
	 * Sets sorting of properties enabled or disabled.
	 * 
	 * @param value
	 *            true to enable sorting
	 */
	public void setSortingProperties(boolean value) {
		getTableModel().setSortingProperties(value);
	}

	/**
	 * Is sorting of properties enabled.
	 * 
	 * @return true if property sorting is enabled
	 */
	public boolean isSortingProperties() {
		return getTableModel().isSortingProperties();
	}

	/**
	 * Sets the Comparator to be used with categories. Categories are treated as
	 * String-objects.
	 * 
	 * @param comp
	 *            java.util.Comparator used to compare categories
	 */
	public void setCategorySortingComparator(Comparator comp) {
		getTableModel().setCategorySortingComparator(comp);
	}

	/**
	 * Sets the Comparator to be used with Property-objects.
	 * 
	 * @param comp
	 *            java.util.Comparator used to compare Property-objects
	 */
	public void setPropertySortingComparator(Comparator comp) {
		getTableModel().setPropertySortingComparator(comp);
	}

	private void buildUI() {
		LookAndFeelTweaks.setBorderLayout(this);
		LookAndFeelTweaks.setBorder(this);

		// lj@
		// actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
		actionPanel = new UIPanel(
//				new PercentLayout(PercentLayout.HORIZONTAL, 4)
				);
		actionPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
		add("North", actionPanel);

		// lj+
		Icon icon = ThemeResourceCenter.getInstance().getImage("themeres/control/activiti/prop_ps.gif");
		JLabel lab = new UILabel(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"101203", "UPP101203-000046")/* @res "属性编辑器" */, icon,
				SwingConstants.LEFT);
		lab.setFont(new Font("Dialog", Font.PLAIN, 12));
		actionPanel.add(lab, "*");

		asCategoryButton = ModelerComponetFactory.instance().createJtoggleButton("",
						ThemeResourceCenter.getInstance().getImage(
								"themeres/control/activiti/alphab_sort_co.gif"),
						true,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101203",
								"UPP101203-000124")/*
													 * @res "在分类视图和列表视图间切换"
													 */);
		asCategoryButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (asCategoryButton.isSelected()) {
					getTableModel().setMode(PropertySheet.VIEW_AS_CATEGORIES);
				} else {
					getTableModel().setMode(PropertySheet.VIEW_AS_FLAT_LIST);
				}
			}
			
		});

		actionPanel.add(asCategoryButton, "26");

		descriptionButton = ModelerComponetFactory.instance()
				.createJtoggleButton(
						"",
						ThemeResourceCenter.getInstance().getImage(
								"themeres/control/activiti/info_tsk.gif"),
						true,
						nc.ui.ml.NCLangRes.getInstance().getStrByID("101203",
								"UPP101203-000125")/* @res "显示/隐藏描述面板" */);
		
		descriptionButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				descScroll.setVisible(descriptionButton.isSelected());
				ModelerPropertySheetPanel.this.revalidate();
			}
			
		});

		actionPanel.add(descriptionButton, "26");

		tableScroll = new UIScrollPane();
		add("Center", tableScroll);

		descTextPane = new UITextPane();
		descTextPane.setEditable(false);
		descTextPane.setBorder(LookAndFeelTweaks.addMargin(BorderFactory
				.createLineBorder(UIManager.getColor("controlDkShadow"))));
		descTextPane.setBackground(UIManager.getColor("Panel.background"));
		descTextPane.setPreferredSize(new Dimension(50, 60));
		// LookAndFeelTweaks.htmlize(descriptionPanel);

		selectionListener = new SelectionListener();

		descScroll = new UIScrollPane(descTextPane);
		descScroll
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add("South", descScroll);

		// by default description is not visible, toolbar is visible.
		setDescriptionVisible(false);
		setToolBarVisible(true);
	}

	class SelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			int row = table.getSelectedRow();
			if (row >= 0 && table.getRowCount() > row) {
				Object o = getTableModel().getPropertySheetElement(row);
				if (o instanceof Property) {
					Property prop = (Property) o;
					setDescritionText(prop);
				} else {
					descTextPane.setText("");
				}
			} else {
				descTextPane.setText("");
			}
		}

		/**
		 * @param prop
		 */
		private void setDescritionText(Property prop) {
			//
			String[] initString = {
					prop.getDisplayName() == null ? "" : prop.getDisplayName()
							+ "\n",
					prop.getShortDescription() == null ? "" : prop
							.getShortDescription() };
			String[] initStyles = { "bold", "regular" };
			// 设置样式
			javax.swing.text.Style def = StyleContext.getDefaultStyleContext()
					.getStyle(StyleContext.DEFAULT_STYLE);
			javax.swing.text.Style regular = descTextPane.addStyle("regular",
					def);
			StyleConstants.setFontFamily(def, "SansSerif");
			javax.swing.text.Style s = descTextPane.addStyle("bold", regular);
			StyleConstants.setBold(s, true);

			try {
				Document doc = descTextPane.getDocument();
				doc.remove(0, doc.getLength());
				for (int i = 0; i < initString.length; i++) {
					doc.insertString(doc.getLength(), initString[i],
							descTextPane.getStyle(initStyles[i]));
				}
			} catch (BadLocationException ble) {
				Logger.error(ble.getMessage(), ble);
			}
		}
	}


}