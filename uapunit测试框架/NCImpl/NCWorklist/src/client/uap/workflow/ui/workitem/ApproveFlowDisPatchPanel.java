package uap.workflow.ui.workitem;


import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import uap.workflow.engine.vos.AssignableInfo;
import uap.workflow.vo.WorkflownoteVO;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIListToList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITabbedPane;
import nc.ui.pub.beans.searcher.ListItemSearcher;
import nc.ui.pub.pf.PatternMather;
import nc.vo.uap.pf.OrganizeUnit;

/**
 * 审批流 审批对话框中的指派panel,代码来自之前的根据指派对话框
 * @modifier zhangrui 根据新UE修改，确定时不指派，由外部调用
 * @author dingxm 2009-06-01
 *
 */
public class ApproveFlowDisPatchPanel extends UIPanel {
	private UITabbedPane UITabbedPane = null;

	//映射UIListToList到AssignableInfo对象
	private Hashtable _hashListToInfo = new Hashtable();

	private Vector _assignableInfos = null;
	
	private boolean _assignablePostCondition =false;
	
	public ApproveFlowDisPatchPanel() {
		initialize();
	}

	/**
	 * 初始化指派对话框
	 * @param wfVo
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo) {
		initByWorknoteVO(wfVo, AssignableInfo.CRITERION_NOTGIVEN);
	}

	/**
	 * 指派对话框的内容随当前审批意见（批准或不批准）变化
	 * @param wfVo
	 * @param strCriterion
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo, String strCriterion) {
		getUITabbedPane().removeAll();
		_assignableInfos = wfVo.getAssignableInfos();
		//xry TODO:_assignablePostCondition =wfVo.isAssignablePostCondition();
		//初始化指派的各个页签
		if (_assignableInfos != null) {
			getUITabbedPane().removeAll();
			for (Iterator iter = _assignableInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				String s = ainfo.getCheckResultCriterion();
				if (AssignableInfo.CRITERION_NOTGIVEN.equals(strCriterion)
						|| AssignableInfo.CRITERION_NOTGIVEN.equals(s) || s.equals(strCriterion))
					getUITabbedPane().addTab(ainfo.getDesc(), null, newUIListToList(ainfo), null);
			}
		}
	}

	public UITabbedPane getUITabbedPane() {
		if (UITabbedPane == null) {
			UITabbedPane = new UITabbedPane();
		}
		return UITabbedPane;
	}

	/**
	 * 新产生一个ListToList控件,并进行初始化
	 * <li>1.填充右边列表
	 * <li>2.填充左边列表
	 * <li>3.计算差
	 * @param ainfo 传递进来的可指派对象
	 * @return
	 */
	private UIListToList newUIListToList(AssignableInfo ainfo) {
		// 根据传递进来的指派信息,初始化界面
		/*********************
		 * 1.填充右边列表
		 *********************/
		Vector<OrganizeUnit> ouAssignedUsers = ainfo.getOuAssignedUsers();
		UIListToList ltl = new UIListToList();
		ListItemSearcher lis = new ListItemSearcher(ltl.getLstLeft());
		lis.setMatcher(new PatternMather());
		lis.setSearchHint(NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000139"))/* @res "输入名称搜索:" */;
		_hashListToInfo.put(ltl, ainfo);

		int numRightUsers = ouAssignedUsers.size();
		ltl.setRightData(ouAssignedUsers.toArray(new OrganizeUnit[0]));

		/****************
		 * 2.计算差后，再填充左边列表
		 ****************/
		Vector<OrganizeUnit> vecLeftUsers = new Vector<OrganizeUnit>();
		Iterator iter = ainfo.getOuUsers().iterator();
		while (iter.hasNext()) {
			OrganizeUnit element = (OrganizeUnit) iter.next();
			boolean hasAssigned = false; //判断左边待指派用户是否在右边存在
			for (int i = 0; i < numRightUsers; i++) {
				if (ouAssignedUsers.get(i).getPk().equals(element.getPk())) {
					hasAssigned = true;
					break;
				}
			}
			if (!hasAssigned)
				vecLeftUsers.add(element);
		}

		//左边的待选用户排序
		if (vecLeftUsers.size() > 0) {
			Collections.sort(vecLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//按照字符串升序
					return String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
				}
			});
		}

		ltl.setLeftData(vecLeftUsers.toArray(new OrganizeUnit[vecLeftUsers.size()]));
		return ltl;
	}

	private void initialize() {
		setSize(450, 350);
		setLayout(new BorderLayout());
		add(getUITabbedPane(), BorderLayout.CENTER);
	}

	public void runDispatch() {
		if (!checkDispatched()) {
			return;
		}
		//计算本次指派的结果
		calculateResults();
	}
	
	
	private boolean checkDispatched(){
		Component[] comps = getUITabbedPane().getComponents();	
		if(_assignablePostCondition){
			for (int i = 0; i < comps.length; i++){
				UIListToList ltl = (UIListToList) comps[i];
				Object[] rightData = ltl.getRightData();
				int iAssigned = rightData == null ? 0 : rightData.length;
				if(iAssigned==0){
					MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000227")/*@res "提示"*/, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000096")/*分支为与，需要为每个后续活动都指派参与者*/);
					return false;
				}
			}
			return true;
		}else{
			for (int i = 0; i < comps.length; i++){
				UIListToList ltl = (UIListToList) comps[i];
				Object[] rightData = ltl.getRightData();
				int iAssigned = rightData == null ? 0 : rightData.length;
				if (iAssigned > 0){
					return true;
				}
			}
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000227")/*@res "提示"*/, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000097")/*没有为活动指派参与者*/);
			return false;
		}
	}

	/**
	 * 如果用户点击了"确定"按钮,则获取本次指派信息
	 */
	private void calculateResults() {
		Component[] comps = getUITabbedPane().getComponents();
		for (int i = 0; i < comps.length; i++) {
			UIListToList ltl = (UIListToList) comps[i];
			AssignableInfo ainfo = (AssignableInfo) _hashListToInfo.get(ltl);

			Object[] rightData = ltl.getRightData();
			int iAssigned = rightData == null ? 0 : rightData.length;
			if (iAssigned == 0) {
				//return false;
				//先清
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
			} else {
				//先清后加
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
				for (int j = 0; j < iAssigned; j++) {
					ainfo.getAssignedOperatorPKs().addElement(((OrganizeUnit) rightData[j]).getPk());
					ainfo.getOuAssignedUsers().addElement((OrganizeUnit) rightData[j]);
				}
			}
		}
	}
}

