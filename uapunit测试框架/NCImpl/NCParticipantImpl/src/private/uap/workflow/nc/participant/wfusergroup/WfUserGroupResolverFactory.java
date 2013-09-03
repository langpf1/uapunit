package uap.workflow.nc.participant.wfusergroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;

/**
 * �����������û��鴦���� ����
 * 
 * @author guowl
 * 
 */
public class WfUserGroupResolverFactory {

	/**
	 * ����
	 */
	private static WfUserGroupResolverFactory inst = new WfUserGroupResolverFactory();

	/**
	 * �����û����������������û��鴦����ʵ����ӳ���
	 */
	private HashMap<String, IWfUserGroupResolver> hmCodeToResolver = new HashMap<String, IWfUserGroupResolver>();

	private WfUserGroupResolverFactory() {
	}

	public static WfUserGroupResolverFactory getInstance() {
		return inst;
	}

	/**
	 * ���ݵ������ͺͱ��룬��ѯ����������û��鴦����ʵ��
	 * 
	 * @return
	 */
	public IWfUserGroupResolver getResolver(String billtype, String ruleCode) {
		IWfUserGroupResolver wfUsergroupResolver = hmCodeToResolver
				.get(ruleCode);
		if (wfUsergroupResolver == null) {
			if (isPredefineRule(ruleCode)) {
				wfUsergroupResolver = createDefaultResolver(ruleCode);
				hmCodeToResolver.put(ruleCode, wfUsergroupResolver);
			} else {
				// �û��Զ���������û���
				ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(
						billtype, ExtendedClassEnum.USERGROUP_RULE
								.getIntValue());
				// ʵ�����õ�������ע����Զ�����������û���
				for (Iterator iterator = bt2VOs.iterator(); iterator.hasNext();) {
					Billtype2VO bt2VO = (Billtype2VO) iterator.next();
					try {
						Object obj = PfUtilTools.findBizImplOfBilltype(
								billtype, bt2VO.getClassname());
						hmCodeToResolver.put(bt2VO.getCode(),
								(IWfUserGroupResolver) obj);
					} catch (Exception e) {
						Logger.error("��ȡ�û��Զ�����������û���ʧ��billType=" + billtype
								+ ",className=" + bt2VO.getClassname(), e);
					}
				}
			}

		}

		return hmCodeToResolver.get(ruleCode);
	}

	private boolean isPredefineRule(String ruleCode) {
		int iCode = Integer.valueOf(ruleCode);
		return WfRuleType.SuperiorType.getIntValue() == iCode
				|| WfRuleType.EmployerType.getIntValue() == iCode
				|| WfRuleType.AllEmployeeType.getIntValue() == iCode
				|| WfRuleType.DirectEmployeeType.getIntValue() == iCode
				|| WfRuleType.HigherUpType.getIntValue() == iCode
				|| WfRuleType.PrincipalType.getIntValue() == iCode
				|| WfRuleType.SuperintendType.getIntValue() == iCode;
	}

	/**
	 * ʵ����UAPԤ�õļ��������������û���
	 */
	private IWfUserGroupResolver createDefaultResolver(String ruleCode) {
		IWfUserGroupResolver pf = null;
		int iCode = Integer.valueOf(ruleCode);
		if (WfRuleType.EmployerType.getIntValue() == iCode) {
			// ��Ա.ֱ���ϼ�
			pf = new WfUserGroup4Employer();
		} else if (WfRuleType.AllEmployeeType.getIntValue() == iCode) {
			// ����Ա��
			pf = new WfUserGroup4AllEmployee();
		} else if (WfRuleType.DirectEmployeeType.getIntValue() == iCode) {
			// ��֯.ֱ��Ա��
			pf = new WfUserGroup4DirectEmployee();
		} else if (WfRuleType.PrincipalType.getIntValue() == iCode) {
			// ��֯.������
			pf = new WfUserGroup4Principal();
		} else if (WfRuleType.SuperintendType.getIntValue() == iCode) {
			// ��֯.����
			pf = new WfUserGroup4Superintend();
		} else if (WfRuleType.HigherUpType.getIntValue() == iCode) {
			// ��֯.�ϼ�������
			pf = new WfUserGroup4HigherUp();
		} else if (WfRuleType.SuperiorType.getIntValue() == iCode) {
			// ��֯.�ϼ�����
			pf = new WfUserGroup4Superior();
		}
		return pf;
	}

}
