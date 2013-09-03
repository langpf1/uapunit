package uap.workflow.modeler;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import uap.workflow.engine.core.ProcessDefinitionStatusEnum;

@SuppressWarnings("serial")
public class ProcessStateRender extends DefaultTableCellRenderer {
	private Icon wf_start = new ImageIcon(ProcessStateRender.class
			.getResource("/images/PF/wf_start.gif"));

	private Icon wf_stop = new ImageIcon(ProcessStateRender.class
			.getResource("/images/PF/wf_lock.gif"));

	private Icon wf_init = new ImageIcon(ProcessStateRender.class
			.getResource("/images/PF/wf_init.gif"));

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setText("");

		if (value instanceof ProcessDefinitionStatusEnum) {
			int status = ((ProcessDefinitionStatusEnum) value).getIntValue();
			if (status == ProcessDefinitionStatusEnum.Valid.getIntValue()) {
				setIcon(wf_start);
			} else if (status == ProcessDefinitionStatusEnum.Invalid.getIntValue()) {
				setIcon(wf_stop);
			} else {
				setIcon(wf_init);
			}
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.BOTTOM);
		}
		return this;
	}
}
