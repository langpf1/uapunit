package uap.workflow.nc.participant.wfusergroup;

import java.util.HashSet;

import uap.workflow.app.participant.ParticipantFilterContext;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IOrgManagerQryService;
import nc.itf.org.IOrgUnitQryService;
import nc.md.data.access.NCObject;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.OrgManagerVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

/**
 * 组织.上级主管
 * @author guowl
 *
 */
public class WfUserGroup4Superior implements IWfUserGroupResolver {

	@Override
	public HashSet<String> queryUsers(String billtype, ParticipantFilterContext context,
			String parameter, String pk_org, String prevParticipant) {
		HashSet<String> hsUser = new HashSet<String>();
		AggregatedValueObject billvo = (AggregatedValueObject)context.getBillEntity();
		NCObject ncObj = NCObject.newInstance(billvo);
		//参数应该是一个组织pk
		Object value = ncObj.getAttributeValue(parameter);
		if(billtype == null){
			value = pk_org;
		}
		if(value == null) {
			Logger.debug("流程用户组（组织.上级主管）从元数据上没有获取到属性值，属性：" + parameter);
			return hsUser;
		}		
		String orgPk = value.toString();		
		return fetchSuperiorRecursion(orgPk);
	}

	
	private HashSet<String> fetchSuperiorRecursion(String orgPk){
		HashSet<String> hsUser = new HashSet<String>();
		IOrgUnitQryService orgQry = NCLocator.getInstance().lookup(IOrgUnitQryService.class);
		IOrgManagerQryService orgManagerQry = NCLocator.getInstance().lookup(IOrgManagerQryService.class);
		try {
			//获得上级组织
			OrgVO parentOrg = orgQry.getParentOrg(orgPk);
			if(parentOrg == null) {
				Logger.debug("流程用户组（组织.上级主管）根据组织pk没有获取到上级组织，组织pk：" + orgPk);
				return hsUser;
			}			
			OrgManagerVO[] orgManagerVOs = orgManagerQry.queryOrgManagerVOSByOrgID(parentOrg.getPk_org());
			if (orgManagerVOs != null && orgManagerVOs.length != 0)
				for (OrgManagerVO orgManagerVO : orgManagerVOs) {
					String userPk = orgManagerVO.getCuserid();
					if (!StringUtil.isEmptyWithTrim(userPk))
						hsUser.add(userPk);
				}
			if(!hsUser.isEmpty())
				return hsUser;
			else{
				Logger.debug("流程用户组（组织.上级主管）根据组织pk上溯查找主管，组织pk：" + parentOrg.getPk_org());
				return fetchSuperiorRecursion(parentOrg.getPk_org());
			}
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
		}
		return hsUser;
	}

}
