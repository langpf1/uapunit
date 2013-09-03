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
 * 指派用户的panel(指派对话框右边那个 指派参与者)
 * @author dingxm 2008-9-3
 *
 */
public class WFUserAssignPanel extends UIPanel {

	private UITabbedPane UITabbedPane = null;

	private UIPanel mainPane = null;

	/*指派用户信息*/
	private Vector _assignableInfos = null;

	//映射UIListToList到AssignableInfo对象
	private Hashtable _hashListToInfo = new Hashtable();

	public WFUserAssignPanel(Vector assignInfos) {
		super();
		this._assignableInfos = assignInfos;
		initialize();
	}

	private void initialize() {
		setBorder(BorderFactory.createTitledBorder(null, "指派后继活动的参与者", TitledBorder.LEFT,
				TitledBorder.TOP, null, Color.BLUE));

		setLayout(new GridLayout(1, 1));
		add(getJPanel());
		setName(NCLangRes.getInstance().getStrByID("101203", "UPP101203-000004")/*@res "指派下一个审批环节的参与者"*/);

		//初始化指派的各个页签
		if (_assignableInfos != null) {
			for (Iterator iter = _assignableInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				UIListToList ltl = new WFGroupListToList(ainfo);
				getUITabbedPane().addTab(ainfo.getDesc(), null, ltl, null);//添加页签
				_hashListToInfo.put(ltl, ainfo);
			}
			//			//添加监听器
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
	 * 是否指派了活动参与者，并保存活动参与者信息
	 */
	public boolean getAssignableInfo() {
		//是否为某个活动指派了参与者
		boolean assignable = false;
		Component[] comps = getUITabbedPane().getComponents();
		for (int i = 0; i < comps.length; i++) {
			UIListToList ltl = (UIListToList) comps[i];
			AssignableInfo ainfo = (AssignableInfo) _hashListToInfo.get(ltl);

			Object[] rightData = ltl.getRightData();
			int isAssigned = rightData == null ? 0 : rightData.length;
			if (isAssigned == 0) {
				//清空
				ainfo.getAssignedOperatorPKs().clear();
			} else {
				//先清后加
				ainfo.getAssignedOperatorPKs().clear();
				for (int j = 0; j < isAssigned; j++)
					ainfo.getAssignedOperatorPKs().addElement(((OrganizeUnit) rightData[j]).getPk());
				assignable = true;
			}
		}
		return assignable;
	}
}
