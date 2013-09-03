package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgUnitQryService;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 人员.直接上级
 * 参数：userid
 * @author guowl
 *
 */
public class WfUserGroup4Employer implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = prevParticipant;
		}
		if(obj == null) {
			Logger.debug("流程用户组（人员.直接上级）从元数据上没有获取到属性值，属性：" + parameter);
			return hsUser;
		}
		
		//参数应该是一个userid
		String userPk = obj.toString();
		try {
			//根据用户id找到人员档案
			IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
			String psndocpk = userService.queryPsndocByUserid(userPk);
			if(StringUtil.isEmptyWithTrim(psndocpk)) {
				Logger.debug("流程用户组（人员.直接上级）根据用户id没有找到人员档案，用户id：" + userPk);
				return hsUser;
			}
			IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			PsndocVO psndocVO = (PsndocVO)queryBS.retrieveByPK(PsndocVO.class, psndocpk);
			//找组织负责人
			String principalPk = queryPrincipalByOrgPk(psndocVO.getPk_org());
			if(!StringUtil.isEmptyWithTrim(principalPk)) {
				 if(!principalPk.equals(userPk))
					 //如果本人不是组织负责人
					hsUser.add(principalPk);
				 else {
					 //如果本人就是组织负责人,则找上级组织的负责人。如果上级组织的负责人还是他本人呢？
					 IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
					 OrgVO parentOrg = orgQry.getParentOrg(psndocVO.getPk_org());
					 String higherPrincipalPk = queryPrincipalByOrgPk(parentOrg.getPk_org());
					 hsUser.add(higherPrincipalPk);
				 }
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return hsUser;
	}
	
	private String queryPrincipalByOrgPk(String orgPk) throws BusinessException {
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		OrgVO org = orgQry.getOrg(orgPk);
	    IUserPubService userpubService = NCLocator.getInstance().lookup(IUserPubService.class);
		UserVO uservo = userpubService.queryUserVOByPsnDocID(org.getPrincipal());
		String principalPk = uservo == null? null:uservo.getCuserid();
		return principalPk;
	}

}
