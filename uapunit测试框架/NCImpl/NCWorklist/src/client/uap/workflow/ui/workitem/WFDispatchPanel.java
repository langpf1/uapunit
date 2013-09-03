package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UISplitPane;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.pub.pf.TransitionSelectableInfo;

/**
 * ����������ָ��panel��������̷�֧panel �� ָ�ɺ�̻������panel
 * @author dingxm 2008-9-3
 *
 */
public class WFDispatchPanel extends UIPanel {

	/*�ж�panel�Ƿ���ʾ*/
	private boolean isShow = false;

	private boolean chooseIsShow = false;

	private boolean userIsShow = false;

	private UISplitPane splitpane;

	/*ѡ���̷�֧panel*/
	private UIPanel choosePanel;

	/*ָ�ɲ�����panel*/
	private WFUserAssignPanel cud = null;

	/*ָ���û���Ϣ*/
	private Vector<AssignableInfo> assignInfos = null;

	/*��̷�֧��Ϣ*/
	private Vector<TransitionSelectableInfo> selectInfos = null;

	/*���� ��̷�֧panel ��ť��Ϣ��map*/
	private HashMap<TransitionSelectableInfo, UIRadioButton> radioMap = new HashMap<TransitionSelectableInfo, UIRadioButton>();

	public WFDispatchPanel(Vector assignInfos, Vector selectInfos) {
		super();
		this.assignInfos = assignInfos;
		this.selectInfos = selectInfos;
		initUI();
	}

	/**
	 * ��ʼ��UI
	 */
	private void initUI() {
		setName("workflowAssign");
		setLayout(new BorderLayout());
		add(getUISplitPane(), BorderLayout.CENTER);
	}

	/**
	 * ��� UISplitPane
	 * @return
	 */
	private UISplitPane getUISplitPane() {
		if (splitpane == null) {
			splitpane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT);

			//��������ѡ�� ���÷�֧ panel
			if (selectInfos != null && selectInfos.size() > 1) {
				splitpane.setLeftComponent(getChoosePanel());
				chooseIsShow = true;
			}
			//��������ѡ�� ���ò����� panel
			if (assignInfos != null && assignInfos.size() > 0) {
				splitpane.setBottomComponent(getAppoint());
				userIsShow = true;
			}
			if (userIsShow || chooseIsShow) {
				isShow = true;
			}
			//���÷ָ���λ��
			splitpane.setDividerLocation(200);
			//���÷ָ����ߴ�
			splitpane.setDividerSize(3);
		}
		return splitpane;
	}

	/**
	 * ��panel�Ƿ���ʾ
	 * @return
	 */
	public boolean isShow() {
		return isShow;
	}

	/**
	 * ��ú�̷�֧panel
	 * @return
	 */
	public UIPanel getChoosePanel() {
		if (choosePanel == null) {
			choosePanel = new UIPanel();
			choosePanel.setSize(200, 200);
			choosePanel.setBorder(BorderFactory.createTitledBorder(null, NCLangRes.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000031")/*ѡ���̷�֧*/, TitledBorder.LEFT,
					TitledBorder.TOP, null, Color.BLUE));
			if (selectInfos != null && selectInfos.size() > 0) {
				int row = selectInfos.size();
				choosePanel.setLayout(new GridLayout(row, 1));
				ButtonGroup bgroup = new ButtonGroup();
				int i = 0;
				for (Iterator iter = selectInfos.iterator(); iter.hasNext();) {
					TransitionSelectableInfo trans = (TransitionSelectableInfo) iter.next();
					UIRadioButton radioButton = new UIRadioButton(trans.getDesc());
					bgroup.add(radioButton);
					if (i++ == 0) {
						radioButton.setSelected(true);
					}
					choosePanel.add(radioButton);
					radioMap.put(trans, radioButton);
				}
			}
		}
		return choosePanel;
	}

	/**
	 * ָ�ɻ������panel
	 * @return
	 */
	public WFUserAssignPanel getAppoint() {
		if (cud == null) {
			cud = new WFUserAssignPanel(assignInfos);
		}
		return cud;
	}

	/**
	 * �Ƿ�ָ���� ָ�ɲ�����panel�� �Ŀ�ָ����Ϣ
	 * @return
	 */
	public boolean getAssignableInfo() {
		return getAppoint().getAssignableInfo();
	}

	/**
	 * ��֧ѡ�񣬲�����ѡ����Ϣ
	 * @return
	 */
	public boolean selectTransition() {
		boolean isSelected = false;
		if (selectInfos != null && selectInfos.size() > 0) {
			Set rs = radioMap.keySet();
			for (Iterator iter = rs.iterator(); iter.hasNext();) {
				TransitionSelectableInfo trans = (TransitionSelectableInfo) iter.next();
				trans.setChoiced(false);
				//��¼ѡ��ķ�֧
				if (radioMap.get(trans).isSelected()) {
					trans.setChoiced(true);
					isSelected = true;
				}
			}
		}
		return isSelected;
	}
}
