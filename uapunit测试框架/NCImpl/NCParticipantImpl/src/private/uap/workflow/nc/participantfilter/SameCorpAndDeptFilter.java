package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 审批流参与者　限定模式：同公司且同部门
 * <li>算法：
 * <li>1.查询发送人的创建公司
 * <li>2.查询发送人在当前登录公司的人员管理档案，或在发送人创建公司的人员管理档案（进而获得其所属部门）
 * <li>3.查询活动参与者对于的组织单元下的可登录该公司、属于该部门的所有用户
 */
public class SameCorpAndDeptFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc){

		//FIXME::leijun V6编译错误？
//		throw new PFBusinessException("V6暂不支持同公司同部门限定模式");
		
//		//查询发送人的创建公司
//		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
//		String sSenderCorp = umq.getUser(pfc.getSenderman()).getPk_org();
//		if (StringUtil.isEmptyWithTrim(sSenderCorp))
//			throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//					"UPPpfworkflow-000284")/* @res "限定同公司和同部门时,发送人没有所属公司" */);
//
//		//查询发送人关联的人员基本档案在当前登录公司的人员管理档案
//		PsndocVO voPsndoc = umq.getPsndocByUserid(pfc.getCurrCorp(), pfc.getSenderman());
//		String dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//		if (StringUtil.isEmptyWithTrim(dept)) {
//			//发送人关联的人员基本档案在用户创建公司的人员管理档案
//			voPsndoc = umq.getPsndocByUserid(sSenderCorp, pfc.getSenderman());
//			dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//			if (StringUtil.isEmptyWithTrim(dept))
//				throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000283")/* @res "限定同公司和同部门时,发送人没有所属部门" */);
//		}
//
//		//查询某组织单元下的与发送人同公司且同部门的所有用户
//		corp = sSenderCorp;
//		OrganizeUnit[] ous = NCLocator.getInstance().lookup(IPFOrgUnit.class).queryUsersByCorpAndDept(
//				pfc.getParticipantType(), pfc.getParticipantId(), corp, dept);
//		HashSet<String> hsPKs = new HashSet<String>();
//		for (int i = 0; i < (ous == null ? 0 : ous.length); i++)
//			hsPKs.add(ous[i].getPk());
//
//		return hsPKs;
		List<String> ids = new ArrayList<String>();
		IUserManageQuery queryUsers = NCLocator.getInstance().lookup(IUserManageQuery.class);
		//如果返回大列表则效率很低，如果太复杂的SQL可能引起不能解析
		UserVO[] userVOs = null;
		for(String user : pfc.getFilterSource()){
			 try {
				userVOs = queryUsers.queryUserByClause(
						" pk_base_doc in (select pk_psndoc from bd_psnjob job where job.pk_dept in ("+
						"select pk_dept from bd_psnjob where pk_psndoc in (select pk_base_doc from sm_user where cuserid='"+user+"')))");
			} catch (BusinessException e) {
				throw new ParticipantException(e.getMessage(), e);
			} 
			
			
			for (UserVO userVO : userVOs) {
				if (pfc.getUserList().contains(userVO.getCuserid()))
					ids.add(userVO.getCuserid());
			}
		}
		return ids;
	}
}
