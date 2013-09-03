package uap.workflow.ui.workitem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Vector;

import nc.ui.pub.beans.UIListToList;
import nc.vo.pub.pf.AssignableInfo;
import nc.vo.uap.pf.OrganizeUnit;

/**
 * ������ָ���е� ָ�ɴ�����panel�е� ListToListҳǩ
 * @author dingxm
 *
 */
public class WFGroupListToList extends UIListToList {

	/*�Ƿ��Ѽ������ݣ������ӳټ���*/
//	private boolean hasInit = false;

	public WFGroupListToList() {
		super();
	}

	public WFGroupListToList(AssignableInfo ainfo) {
		super();
		initData(ainfo);
	}

	public void initData(AssignableInfo ainfo) {
		// ���ݴ��ݽ�����ָ����Ϣ,��ʼ������
		//		1.����ұ��б�
		//Vector vec = ainfo.getAssignedOperatorPKs();
		Vector<OrganizeUnit> vecAssignedUsers = ainfo.getOuAssignedUsers();
		setRightData(vecAssignedUsers.toArray(new OrganizeUnit[0]));

		//		2.�������б�
		Vector<OrganizeUnit> alLeftUsers = ainfo.getOuUsers();

		//		3.���ұ��б��е����ݴ�����б��Ƴ�
		Iterator iter = alLeftUsers.iterator();
		ArrayList alRemoved = new ArrayList();
		while (iter.hasNext()) {
			OrganizeUnit element = (OrganizeUnit) iter.next();
			for (int i = 0; i < vecAssignedUsers.size(); i++) {
				if (vecAssignedUsers.get(i).getPk().equals(element.getPk())) {
					alRemoved.add(element);
					break;
				}
			}
		}
		alLeftUsers.removeAll(alRemoved);

		//��ߵĴ�ѡ�û�����
		if (alLeftUsers.size() > 0) {
			Collections.sort(alLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//�����ַ�������
					return String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
				}
			});
		}
		setLeftData(alLeftUsers.toArray(new OrganizeUnit[alLeftUsers.size()]));
	}
}
