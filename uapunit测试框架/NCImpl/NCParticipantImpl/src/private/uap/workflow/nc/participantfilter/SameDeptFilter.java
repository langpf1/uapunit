package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uap.workflow.app.participant.ParticipantException;
import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.itf.uap.rbac.IUserManageQuery_C;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 审批流参与者　限定模式：同部门
 * <li>算法：
 * <li>1.查询发送人在当前登录公司的人员管理档案（进而获得其所属部门）
 * <li>2.若无，则查询发送人在发送人创建公司的人员管理档案（进而获得其所属部门）
 * <li>3.查询活动参与者对于的组织单元下属于该部门的用户
 */
public class SameDeptFilter extends DefaultParticipantFilter {

	public List<String> filterUsers(ParticipantFilterContext pfc) {

		//FIXME::leijun V6编译错误？
//		throw new PFBusinessException("V6暂不支持同部门限定模式");
		//查询发送人所属的部门
//		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
//		//发送人关联的人员基本档案在当前登录公司的人员管理档案 leijun@2008-9
//		PsndocVO voPsndoc = umq.getPsndocByUserid(pfc.getCurrCorp(), pfc.getSenderman());
//		String dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//		if (StringUtil.isEmptyWithTrim(dept)) {
//			//发送人关联的人员基本档案在用户创建公司的人员管理档案
//			String sSenderCorp = umq.getUser(pfc.getSenderman()).getPk_org();
//			voPsndoc = umq.getPsndocByUserid(sSenderCorp, pfc.getSenderman());
//			dept = voPsndoc == null ? null : voPsndoc.getPk_deptdoc();
//			if (StringUtil.isEmptyWithTrim(dept))
//				throw new PFRuntimeException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
//						"UPPpfworkflow-000281")/* @res "限定同部门时,发送人没有所属部门" */);
//		}
//
//		//查询某组织单元下的与发送人同部门的所有用户
//		OrganizeUnit[] ous = NCLocator.getInstance().lookup(IPFOrgUnit.class).queryUsersByCorpAndDept(
//				pfc.getParticipantType(), pfc.getParticipantId(), pfc.getParticipantBelongCorp(), dept);
//		HashSet<String> hsPKs = new HashSet<String>();
//		for (int i = 0; i < (ous == null ? 0 : ous.length); i++)
//			hsPKs.add(ous[i].getPk());
//
//		return hsPKs;
		
		List<String> userIDs = pfc.getUserList();
		
		return processFilter(pfc, userIDs);

	}

	private List<String> processFilter(ParticipantFilterContext pfc, List<String> userIDs)
	{
		List<String> ids = new ArrayList<String>();
		IUserManageQuery_C umq = NCLocator.getInstance().lookup(IUserManageQuery_C.class);
		try {
			//发送人关联的人员基本档案在当前登录公司的人员管理档案 leijun@2008-9
			for(String user : pfc.getFilterSource())
			{
				String psnID = umq.queryPsndocByUserid(user);
				//查user部门
				IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
				Collection<PsnjobVO> psnJobVOs = queryBS.retrieveByClause(PsnjobVO.class, " pk_psndoc ='"+psnID + "'");
				if (psnJobVOs != null && psnJobVOs.size()> 0){
					PsnjobVO vo = (PsnjobVO)psnJobVOs.toArray()[0];
					String dept = vo.getPk_dept();
					psnJobVOs.clear();
					IUserManageQuery queryUsers = NCLocator.getInstance().lookup(IUserManageQuery.class);
					UserVO[] userVOs = queryUsers.queryUserByClause(
							" pk_base_doc in (select pk_psndoc from bd_psnjob job where job.pk_dept = '"+dept+"')"); //如果返回大列表则效率很低，如果太复杂的SQL可能引起不能解析
					
					for (UserVO userVO : userVOs) 
					{
						if (userIDs.contains(userVO.getCuserid()))
							ids.add(userVO.getCuserid());
					}
				}
			}
		} catch (BusinessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		return ids;
	}
}
