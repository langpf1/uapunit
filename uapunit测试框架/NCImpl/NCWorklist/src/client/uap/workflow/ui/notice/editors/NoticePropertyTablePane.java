package uap.workflow.ui.notice.editors;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.message.templet.vo.MessageTempletRefModel;
import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.plaf.basic.UIRefButtonUI;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.wfengine.sheet.swing.DefaultCellRenderer;
import nc.uitheme.ui.ThemeResourceCenter;
import nc.vo.jcom.lang.StringUtil;
import uap.workflow.app.config.NoticeTypeFactory;
import uap.workflow.app.notice.INoticeType;
import uap.workflow.app.notice.NoticeTimeTypeEnum;
import uap.workflow.app.notice.ReceiverVO;
import uap.workflow.bpmn2.model.DefaultNoticeDefinition;
import uap.workflow.bpmn2.model.DefaultNoticeType;
import uap.workflow.modeler.editors.ExpressionEditor;
import uap.workflow.modeler.sheet.CellEditorAdapter;
import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.uecomponent.AbstractTablePane;

import com.thoughtworks.xstream.XStream;

public class NoticePropertyTablePane extends AbstractTablePane {

	private static final long serialVersionUID = -1915237695544434728L;
	private UIRefPane msgTempRefPane = null;
	private AbstractRefModel msgTempRefModel = null;

	public NoticePropertyTablePane(NoticePropertyModel model) {
		super(model, model.getColumnWidth());
	}

	protected void initCellEditor() {
		TableColumnModel columnModel = getTable().getColumnModel();
		columnModel.getColumn(0).setCellEditor(new CellEditorAdapter(new ExpressionEditor()));
		columnModel.getColumn(1).setCellEditor(new UIRefCellEditor(getMsgTempRefPane()));
		columnModel.getColumn(1).setCellRenderer(new MsgTempCellRender());
		columnModel.getColumn(3).setCellEditor(new DefaultCellEditor(getNoticeTimeTypeComboBox()));
		columnModel.getColumn(4).setCellEditor(new DefaultCellEditor(getNoticeTypeComboBox()));
		columnModel.getColumn(5).setCellRenderer(new ReceiverCellRender());
		columnModel.getColumn(5).setCellEditor(new ReceiverCellEditor());
	}

	private UIComboBox getNoticeTimeTypeComboBox() {
		UIComboBox	comboBox = new UIComboBox();
		comboBox.addItem("");
		comboBox.addItem(NoticeTimeTypeEnum.TaskInstanceCreate.name());
		comboBox.addItem(NoticeTimeTypeEnum.TaskInstanceComplete.name());
		comboBox.addItem(NoticeTimeTypeEnum.TaskInstanceOverTime.name());
		comboBox.addItem(NoticeTimeTypeEnum.ProcessInstanceOverTime.name());
		return comboBox;
	}
	
	private UIComboBox getNoticeTypeComboBox() {
		UIComboBox comboBox = new UIComboBox();
		comboBox.addItem("");
		// ±È¿˙HashMap
		Map<String, INoticeType> taskHandlingTypeMap = NoticeTypeFactory.getInstance().getTypes();
		Iterator iterator = taskHandlingTypeMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, INoticeType> entry = (Map.Entry<String, INoticeType>) iterator.next();
			String key = entry.getKey();
			//INoticeType value = entry.getValue();
			comboBox.addItem(key);
		}
		return comboBox;
	}
	
	private UIRefPane getMsgTempRefPane() {
		if (msgTempRefPane == null) {
			msgTempRefPane = new UIRefPane();
			msgTempRefPane.setRefModel(getMsgTempRefModel());

			msgTempRefPane.getUITextField().setShowMustInputHint(true);
		}

		return msgTempRefPane;
	}
	private AbstractRefModel getMsgTempRefModel() {
		if (msgTempRefModel == null) {
			msgTempRefModel = new MessageTempletRefModel();
			msgTempRefModel.setWherePart("langcode='" + WorkbenchEnvironment.getLangCode() + "'");
		}

		return msgTempRefModel;
	}
	@Override
	protected void doEdit() {

	}

	@Override
	public Object assemberData() {
		Vector vector = ((AbstractBpmnTableModel) getTable().getModel()).getDataVector();
		List<DefaultNoticeDefinition> list = new ArrayList<DefaultNoticeDefinition>();
		for (int start = 0, end = getTable().getModel().getRowCount(); start < end; start++) {
			Vector rowVector = (Vector) vector.get(start);
			DefaultNoticeDefinition noticeDefinition = new DefaultNoticeDefinition();
			noticeDefinition.setCondition(rowVector.get(0).toString());
			noticeDefinition.setContentTemplate(rowVector.get(1).toString());
			Object receipt = rowVector.get(2);
			noticeDefinition.setHasReceipt(receipt == null ? false : Boolean.parseBoolean(receipt.toString()));
			
			String noticeTimeTypeName = rowVector.get(3).toString();
			if(!StringUtil.isEmpty(noticeTimeTypeName))
			{
				NoticeTimeTypeEnum noticeTimeType;
				noticeTimeType = (NoticeTimeTypeEnum)Enum.valueOf(NoticeTimeTypeEnum.class, noticeTimeTypeName);
				noticeDefinition.setNoticeTime(noticeTimeType);
			}
			
			INoticeType noticeType = NoticeTypeFactory.getInstance().getType(rowVector.get(4).toString());
			DefaultNoticeType defaultNoticeType = new DefaultNoticeType();
			if(noticeType != null)
			{
				defaultNoticeType.setCode(noticeType.getCode());
				defaultNoticeType.setName(noticeType.getName());
			}
			noticeDefinition.setNoticeType(defaultNoticeType);
			
			String rcvs = rowVector.get(5).toString();
			if (!StringUtil.isEmptyWithTrim(rcvs)) {
				Object obj = new XStream().fromXML(rcvs);
				ReceiverVO[] rcvArray = (ReceiverVO[]) obj;
				List<ReceiverVO> rcvList = new ArrayList<ReceiverVO>(rcvArray.length);
				for(ReceiverVO rcv : rcvArray)
				{
					rcvList.add(rcv);
				}
				noticeDefinition.setReceivers(rcvList);
			}

			list.add(noticeDefinition);
		}
		return list;
	}

	@Override
	public void unassemberData(Object intializeData) {
		((AbstractBpmnTableModel) getTable().getModel()).getDataVector().clear();
		if (intializeData == null)
			return;
		List list = (List) intializeData;
		for (Object obj : list) {
			Vector rowVector = new Vector();
			DefaultNoticeDefinition noticeDefinition = (DefaultNoticeDefinition) obj;
			rowVector.add(noticeDefinition.getCondition());
			rowVector.add(noticeDefinition.getContentTemplate());
			rowVector.add(noticeDefinition.getHasReceipt());
			rowVector.add(noticeDefinition.getNoticeTime().name());
			rowVector.add(noticeDefinition.getNoticeType().getCode());
			String rcvValue = new XStream().toXML(noticeDefinition.getReceivers());
			rowVector.add(rcvValue);
			((AbstractBpmnTableModel) getTable().getModel()).getDataVector().add(rowVector);
		}

	}
	
	class MsgTempCellRender extends DefaultTableCellRenderer {

		private boolean isValidating = false;

		public boolean isValidating() {
			return isValidating;
		}

		public void setValidating(boolean isValidating) {
			this.isValidating = isValidating;
		}

		private boolean validateValue(Object value) {
			if (value == null)
				return false;

			if (value instanceof String) {
				return !StringUtil.isEmptyWithTrim((String) value);
			}

			return true;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if (isValidating && validateValue(value)) {
				c.setBackground(Color.RED);
			}

			return c;
		}
	}

	class ReceiverCellRender extends DefaultCellRenderer {
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			
			if (value != null && value instanceof String) {
				value = ActivityMsgConfigUtil.getDisplayValue((String) value);
			}
			// TODO Auto-generated method stub
			return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}
	}
	
	class ReceiverCellEditor extends AbstractCellEditor implements TableCellEditor {

		ImageIcon iconRegular = ThemeResourceCenter.getInstance().getImage("themeres/control/input/input_search.png");
		ImageIcon iconPressed = ThemeResourceCenter.getInstance().getImage(
				"themeres/control/input/input_search_highlight.png");

		private String editorValue = null;

		@Override
		public Object getCellEditorValue() {
			return editorValue;
		}

		@Override
		public boolean isCellEditable(EventObject e) {
			if (e instanceof MouseEvent) {
				MouseEvent me = (MouseEvent) e;

				if (me.getClickCount() == 2) {
					return true;
				}
			}

			return false;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			UIPanel panel = new UIPanel();

			panel.setLayout(new BorderLayout());

			final UILabel label = new UILabel();
			label.setFont(table.getFont());
			
			final String recvConfig = value == null ? "" : String.valueOf(value);
			final JTable tablemodel = table;
			final int currow = row;
			final int curcolumn = column;

			if (value == null) {
				label.setText("");
				this.editorValue = null;
			} else {
				label.setText(ActivityMsgConfigUtil.getDisplayValue(recvConfig));
				this.editorValue = String.valueOf(value);
			}

			UIButton btn = new UIButton();
			btn.setUI(new UIRefButtonUI());
			btn.setText("");

			btn.setIcon(iconRegular);
			btn.setDisabledIcon(iconRegular);
			btn.setRolloverIcon(iconPressed);
			btn.setPressedIcon(iconPressed);
			btn.setPreferredSize(new java.awt.Dimension(20, iconRegular.getIconHeight()));

			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ReceiverRefDlg dlg = new ReceiverRefDlg(NoticePropertyTablePane.this, 1);

					if (!StringUtil.isEmptyWithTrim(recvConfig)) {
						Object obj = new XStream().fromXML(recvConfig);
						dlg.initReceivers((ReceiverVO[]) obj);
					}

					if (dlg.showModal() == UIDialog.ID_OK) {

						ReceiverVO[] rcvs = dlg.getReceivers();
						String rcvValue = new XStream().toXML(rcvs);
						tablemodel.setValueAt(rcvValue, currow, curcolumn);
						ReceiverCellEditor.this.editorValue = rcvValue;
						label.setText(dlg.getReceiverSTRs());
					}
				}
			});

			panel.add(label, BorderLayout.CENTER);
			panel.add(btn, BorderLayout.EAST);

			return panel;
		}

	}	
}
