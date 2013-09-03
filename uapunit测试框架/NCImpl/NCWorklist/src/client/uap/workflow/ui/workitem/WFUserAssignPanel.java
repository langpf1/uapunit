package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIListToList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.uap.pf.OrganizeUnit;

/**
 * ָ���û���panel(ָ�ɶԻ����ұ��Ǹ� ָ�ɲ�����)
 * @author dingxm 2008-9-3
 *
 */
public class WFUserAssignPanel extends UIPanel {

	private UITabbedPane UITabbedPane = null;

	private UIPanel mainPane = null;

	/*ָ���û���Ϣ*/
	private Vector _assignableInfos = null;

	//ӳ��UIListToList��AssignableInfo����
	private Hashtable _hashListToInfo = new Hashtable();

	public WFUserAssignPanel(Vector assignInfos) {
		super();
		this._assignableInfos = assignInfos;
		initialize();
	}

	private void initialize() {
		setBorder(BorderFactory.createTitledBorder(null, "ָ�ɺ�̻�Ĳ�����", TitledBorder.LEFT,
				TitledBorder.TOP, null, Color.BLUE));

		setLayout(new GridLayout(1, 1));
		add(getJPanel());
		setName(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "ָ����һ���������ڵĲ�����"*/);

		//��ʼ��ָ�ɵĸ���ҳǩ
		if (_assignableInfos != null) {
			for (Iterator iter = _assignableInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				UIListToList ltl = new WFGroupListToList(ainfo);
				getUITabbedPane().addTab(ainfo.getDesc(), null, ltl, null);//���ҳǩ
				_hashListToInfo.put(ltl, ainfo);
			}
			//			//��Ӽ�����
			//			getUITabbedPane().addChangeListener(new ChangeListener() {
			//				public void stateChanged(ChangeEvent e) {
			//					if (!((WFGroupListToList) (getUITabbedPane().getSelectedComponent())).isHasInit()) {
			//						int index = getUITabbedPane().getSelectedIndex();
			//						((WFGroupListToList) (getUITabbedPane().getSelectedComponent()))
			//								.initData((AssignableInfo) _assignableInfos.get(index));
			//					}
			//				}
			//			});
		}
	}

	private UIPanel getJPanel() {
		if (mainPane == null) {
			mainPane = new UIPanel();
			mainPane.setLayout(new BorderLayout());
			mainPane.add(getUITabbedPane(), BorderLayout.CENTER);
		}
		return mainPane;
	}

	public UITabbedPane getUITabbedPane() {
		if (UITabbedPane == null) {
			UITabbedPane = new UITabbedPane();
		}
		return UITabbedPane;
	}

	/**
	 * �Ƿ�ָ���˻�����ߣ���������������Ϣ
	 */
	public boolean getAssignableInfo() {
		//�Ƿ�Ϊĳ���ָ���˲�����
		boolean assignable = false;
		Component[] comps = getUITabbedPane().getComponents();
		for (int i = 0; i < comps.length; i++) {
			UIListToList ltl = (UIListToList) comps[i];
			AssignableInfo ainfo = (AssignableInfo) _hashListToInfo.get(ltl);

			Object[] rightData = ltl.getRightData();
			int isAssigned = rightData == null ? 0 : rightData.length;
			if (isAssigned == 0) {
				//���
				ainfo.getAssignedOperatorPKs().clear();
			} else {
				//������
				ainfo.getAssignedOperatorPKs().clear();
				for (int j = 0; j < isAssigned; j++)
					ainfo.getAssignedOperatorPKs().addElement(((OrganizeUnit) rightData[j]).getPk());
				assignable = true;
			}
		}
		return assignable;
	}
}
