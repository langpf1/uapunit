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
 * 工作流指派中的 指派代理人panel中的 ListToList页签
 * @author dingxm
 *
 */
public class WFGroupListToList extends UIListToList {

	/*是否已加载数据，用于延迟加载*/
//	private boolean hasInit = false;

	public WFGroupListToList() {
		super();
	}

	public WFGroupListToList(AssignableInfo ainfo) {
		super();
		initData(ainfo);
	}

	public void initData(AssignableInfo ainfo) {
		// 根据传递进来的指派信息,初始化界面
		//		1.填充右边列表
		//Vector vec = ainfo.getAssignedOperatorPKs();
		Vector<OrganizeUnit> vecAssignedUsers = ainfo.getOuAssignedUsers();
		setRightData(vecAssignedUsers.toArray(new OrganizeUnit[0]));

		//		2.填充左边列表
		Vector<OrganizeUnit> alLeftUsers = ainfo.getOuUsers();

		//		3.把右边列表中的数据从左边列表移除
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

		//左边的待选用户排序
		if (alLeftUsers.size() > 0) {
			Collections.sort(alLeftUsers, new Comparator() {
				public int compare(Object o1, Object o2) {
					//按照字符串升序
					return String.valueOf(o1).compareToIgnoreCase(String.valueOf(o2));
				}
			});
		}
		setLeftData(alLeftUsers.toArray(new OrganizeUnit[alLeftUsers.size()]));
	}
}
