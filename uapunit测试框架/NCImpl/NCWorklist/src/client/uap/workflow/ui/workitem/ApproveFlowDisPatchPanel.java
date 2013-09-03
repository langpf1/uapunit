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
 * ������ �����Ի����е�ָ��panel,��������֮ǰ�ĸ���ָ�ɶԻ���
 * @modifier zhangrui ������UE�޸ģ�ȷ��ʱ��ָ�ɣ����ⲿ����
 * @author dingxm 2009-06-01
 *
 */
public class ApproveFlowDisPatchPanel extends UIPanel {
	private UITabbedPane UITabbedPane = null;

	//ӳ��UIListToList��AssignableInfo����
	private Hashtable _hashListToInfo = new Hashtable();

	private Vector _assignableInfos = null;
	
	private boolean _assignablePostCondition =false;
	
	public ApproveFlowDisPatchPanel() {
		initialize();
	}

	/**
	 * ��ʼ��ָ�ɶԻ���
	 * @param wfVo
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo) {
		initByWorknoteVO(wfVo, AssignableInfo.CRITERION_NOTGIVEN);
	}

	/**
	 * ָ�ɶԻ���������浱ǰ�����������׼����׼���仯
	 * @param wfVo
	 * @param strCriterion
	 */
	public void initByWorknoteVO(WorkflownoteVO wfVo, String strCriterion) {
		getUITabbedPane().removeAll();
		_assignableInfos = wfVo.getAssignableInfos();
		//xry TODO:_assignablePostCondition =wfVo.isAssignablePostCondition();
		//��ʼ��ָ�ɵĸ���ҳǩ
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
	 * �²���һ��ListToList�ؼ�,�����г�ʼ��
	 * <li>1.����ұ��б�
	 * <li>2.�������б�
	 * <li>3.�����
	 * @param ainfo ���ݽ����Ŀ�ָ�ɶ���
	 * @return
	 */
	private UIListToList newUIListToList(AssignableInfo ainfo) {
		// ���ݴ��ݽ�����ָ����Ϣ,��ʼ������
		/*********************
		 * 1.����ұ��б�
		 *********************/
		Vector<OrganizeUnit> ouAssignedUsers = ainfo.getOuAssignedUsers();
		UIListToList ltl = new UIListToList();
		ListItemSearcher lis = new ListItemSearcher(ltl.getLstLeft());
		lis.setMatcher(new PatternMather());
		lis.setSearchHint(NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000139"))/* @res "������������:" */;
		_hashListToInfo.put(ltl, ainfo);

		int numRightUsers = ouAssignedUsers.size();
		ltl.setRightData(ouAssignedUsers.toArray(new OrganizeUnit[0]));

		/****************
		 * 2.���������������б�
		 ****************/
		Vector<OrganizeUnit> vecLeftUsers = new Vector<OrganizeUnit>();
		Iterator iter = ainfo.getOuUsers().iterator();
		while (iter.hasNext()) {
			OrganizeUnit element = (OrganizeUnit) iter.next();
			boolean hasAssigned = false; //�ж���ߴ�ָ���û��Ƿ����ұߴ���
			for (int i = 0; i < numRightUsers; i++) {
				if (ouAssignedUsers.get(i).getPk().equals(element.getPk())) {
					hasAssigned = true;
					break;
				}
			}
			if (!hasAssigned)
				vecLeftUsers.add(element);
		}

		//��ߵĴ�ѡ�û�����
		if (vecLeftUsers.size() > 0) {
			Collections.sort(vecLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//�����ַ�������
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
		//���㱾��ָ�ɵĽ��
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
					MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000227")/*@res "��ʾ"*/, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000096")/*��֧Ϊ�룬��ҪΪÿ���������ָ�ɲ�����*/);
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
			MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("pfworkflow","UPPpfworkflow-000227")/*@res "��ʾ"*/, NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000097")/*û��Ϊ�ָ�ɲ�����*/);
			return false;
		}
	}

	/**
	 * ����û������"ȷ��"��ť,���ȡ����ָ����Ϣ
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
				//����
				ainfo.getAssignedOperatorPKs().clear();
				ainfo.getOuAssignedUsers().clear();
			} else {
				//������
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

