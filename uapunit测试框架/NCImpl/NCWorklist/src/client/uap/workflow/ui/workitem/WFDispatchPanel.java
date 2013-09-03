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
 * 工作流审批指派panel，包括后继分支panel 和 指派后继活动参与者panel
 * @author dingxm 2008-9-3
 *
 */
public class WFDispatchPanel extends UIPanel {

	/*判断panel是否显示*/
	private boolean isShow = false;

	private boolean chooseIsShow = false;

	private boolean userIsShow = false;

	private UISplitPane splitpane;

	/*选择后继分支panel*/
	private UIPanel choosePanel;

	/*指派参与者panel*/
	private WFUserAssignPanel cud = null;

	/*指派用户信息*/
	private Vector<AssignableInfo> assignInfos = null;

	/*后继分支信息*/
	private Vector<TransitionSelectableInfo> selectInfos = null;

	/*保存 后继分支panel 按钮信息的map*/
	private HashMap<TransitionSelectableInfo, UIRadioButton> radioMap = new HashMap<TransitionSelectableInfo, UIRadioButton>();

	public WFDispatchPanel(Vector assignInfos, Vector selectInfos) {
		super();
		this.assignInfos = assignInfos;
		this.selectInfos = selectInfos;
		initUI();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		setName("workflowAssign");
		setLayout(new BorderLayout());
		add(getUISplitPane(), BorderLayout.CENTER);
	}

	/**
	 * 获得 UISplitPane
	 * @return
	 */
	private UISplitPane getUISplitPane() {
		if (splitpane == null) {
			splitpane = new UISplitPane(JSplitPane.HORIZONTAL_SPLIT);

			//根据条件选择 设置分支 panel
			if (selectInfos != null && selectInfos.size() > 1) {
				splitpane.setLeftComponent(getChoosePanel());
				chooseIsShow = true;
			}
			//根据条件选择 设置参与者 panel
			if (assignInfos != null && assignInfos.size() > 0) {
				splitpane.setBottomComponent(getAppoint());
				userIsShow = true;
			}
			if (userIsShow || chooseIsShow) {
				isShow = true;
			}
			//设置分隔符位置
			splitpane.setDividerLocation(200);
			//设置分隔符尺寸
			splitpane.setDividerSize(3);
		}
		return splitpane;
	}

	/**
	 * 该panel是否显示
	 * @return
	 */
	public boolean isShow() {
		return isShow;
	}

	/**
	 * 获得后继分支panel
	 * @return
	 */
	public UIPanel getChoosePanel() {
		if (choosePanel == null) {
			choosePanel = new UIPanel();
			choosePanel.setSize(200, 200);
			choosePanel.setBorder(BorderFactory.createTitledBorder(null, NCLangRes.getInstance()
					.getStrByID("pfworkflow", "UPPpfworkflow-000031")/*选择后继分支*/, TitledBorder.LEFT,
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
	 * 指派活动参与者panel
	 * @return
	 */
	public WFUserAssignPanel getAppoint() {
		if (cud == null) {
			cud = new WFUserAssignPanel(assignInfos);
		}
		return cud;
	}

	/**
	 * 是否指派了 指派参与者panel中 的可指派信息
	 * @return
	 */
	public boolean getAssignableInfo() {
		return getAppoint().getAssignableInfo();
	}

	/**
	 * 分支选择，并保存选择信息
	 * @return
	 */
	public boolean selectTransition() {
		boolean isSelected = false;
		if (selectInfos != null && selectInfos.size() > 0) {
			Set rs = radioMap.keySet();
			for (Iterator iter = rs.iterator(); iter.hasNext();) {
				TransitionSelectableInfo trans = (TransitionSelectableInfo) iter.next();
				trans.setChoiced(false);
				//记录选择的分支
				if (radioMap.get(trans).isSelected()) {
					trans.setChoiced(true);
					isSelected = true;
				}
			}
		}
		return isSelected;
	}
}
