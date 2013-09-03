package uap.workflow.ui.notice.editors;

import uap.workflow.modeler.uecomponent.AbstractBpmnTableModel;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class NoticePropertyModel extends AbstractBpmnTableModel {

	public NoticePropertyModel(){
		super();
	}
	
	@Override
	public Class getColumnClass(int col){
		if (col ==2)
			return Boolean.class;
		return String.class;
	}
	
	public Object getValueAt(int row, int column) {
		Object obj = super.getValueAt(row, column);
		if (column == 2) {
			return obj == null ? false : Boolean.parseBoolean(obj.toString());
		} else
			return obj == null ? "" : obj.toString();
	}	
	public int[] getColumnWidth(){
		return new int[]{
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_6WIDTH,
				BpmnModelerConstants.COLUMN_4WIDTH,
				BpmnModelerConstants.COLUMN_12WIDTH,
				BpmnModelerConstants.COLUMN_12WIDTH,
				BpmnModelerConstants.COLUMN_12WIDTH};
	}
	


	@Override
	protected void initColumns() {
		m_columnIdentifiers.add("֪ͨ����");
		m_columnIdentifiers.add("֪ͨ����ģ��");
		m_columnIdentifiers.add("�Ƿ��ִ");
		m_columnIdentifiers.add("֪ͨʱ��");
		m_columnIdentifiers.add("֪ͨ��ʽ");
		m_columnIdentifiers.add("������");
	}

}
