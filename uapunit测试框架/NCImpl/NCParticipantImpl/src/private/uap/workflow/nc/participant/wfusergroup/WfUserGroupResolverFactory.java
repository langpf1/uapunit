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
 * 规则型流程用户组处理器 工厂
 * 
 * @author guowl
 * 
 */
public class WfUserGroupResolverFactory {

	/**
	 * 单例
	 */
	private static WfUserGroupResolverFactory inst = new WfUserGroupResolverFactory();

	/**
	 * 流程用户组规则编码与流程用户组处理器实例的映射表
	 */
	private HashMap<String, IWfUserGroupResolver> hmCodeToResolver = new HashMap<String, IWfUserGroupResolver>();

	private WfUserGroupResolverFactory() {
	}

	public static WfUserGroupResolverFactory getInstance() {
		return inst;
	}

	/**
	 * 根据单据类型和编码，查询缓存的流程用户组处理器实例
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
				// 用户自定义的流程用户组
				ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(
						billtype, ExtendedClassEnum.USERGROUP_RULE
								.getIntValue());
				// 实例化该单据类型注册的自定义规则流程用户组
				for (Iterator iterator = bt2VOs.iterator(); iterator.hasNext();) {
					Billtype2VO bt2VO = (Billtype2VO) iterator.next();
					try {
						Object obj = PfUtilTools.findBizImplOfBilltype(
								billtype, bt2VO.getClassname());
						hmCodeToResolver.put(bt2VO.getCode(),
								(IWfUserGroupResolver) obj);
					} catch (Exception e) {
						Logger.error("获取用户自定义规则流程用户组失败billType=" + billtype
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
	 * 实例化UAP预置的几个规则型流程用户组
	 */
	private IWfUserGroupResolver createDefaultResolver(String ruleCode) {
		IWfUserGroupResolver pf = null;
		int iCode = Integer.valueOf(ruleCode);
		if (WfRuleType.EmployerType.getIntValue() == iCode) {
			// 人员.直接上级
			pf = new WfUserGroup4Employer();
		} else if (WfRuleType.AllEmployeeType.getIntValue() == iCode) {
			// 所有员工
			pf = new WfUserGroup4AllEmployee();
		} else if (WfRuleType.DirectEmployeeType.getIntValue() == iCode) {
			// 组织.直属员工
			pf = new WfUserGroup4DirectEmployee();
		} else if (WfRuleType.PrincipalType.getIntValue() == iCode) {
			// 组织.负责人
			pf = new WfUserGroup4Principal();
		} else if (WfRuleType.SuperintendType.getIntValue() == iCode) {
			// 组织.主管
			pf = new WfUserGroup4Superintend();
		} else if (WfRuleType.HigherUpType.getIntValue() == iCode) {
			// 组织.上级负责人
			pf = new WfUserGroup4HigherUp();
		} else if (WfRuleType.SuperiorType.getIntValue() == iCode) {
			// 组织.上级主管
			pf = new WfUserGroup4Superior();
		}
		return pf;
	}

}
