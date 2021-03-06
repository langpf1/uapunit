package uap.workflow.nc.participant.wfusergroup;

import java.util.Collection;
import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.IUAPQueryBS;
import nc.md.data.access.NCObject;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

/**
 * 组织.直属员工
 * @author guowl
 *
 */
public class WfUserGroup4DirectEmployee implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		Object obj = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			obj = pk_org;
		}
		if(obj == null) {
			Logger.debug("流程用户组（组织.直属员工）从元数据上没有获取到属性值，属性：" + parameter);
			return hsUser;
		}
		
		//参数应该是一个组织pk
		String orgPk = obj.toString();
		//人员档案上任职部门=[参数]或任职组织=[参数]
		IUAPQueryBS queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		try {
			Collection<PsnjobVO> psnJobVOs = queryBS.retrieveByClause(PsnjobVO.class, "pk_dept='" + orgPk + "' or pk_org='" + orgPk + "'");
			if(psnJobVOs == null || psnJobVOs.size() == 0) {
				Logger.debug("流程用户组（组织.直属员工）根据组织pk没有找到直属员工，组织pk：" + orgPk);
				return hsUser;
			}
			
			HashSet<String> psndocPks = new HashSet<String>();
			for (PsnjobVO psnjobVO : psnJobVOs) {
				psndocPks.add(psnjobVO.getPk_psndoc());
			}
			IUserPubService userpubService = NCLocator.getInstance().lookup(IUserPubService.class);
			for (String pk_psndoc : psndocPks) {
				UserVO uservo = userpubService.queryUserVOByPsnDocID(pk_psndoc);
				if(uservo != null)
					hsUser.add(uservo.getCuserid());
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		
		return hsUser;
	}

}
