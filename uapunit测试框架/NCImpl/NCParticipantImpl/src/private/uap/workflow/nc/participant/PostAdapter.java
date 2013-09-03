package uap.workflow.nc.participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import uap.workflow.app.participant.IParticipantAdapter;
import uap.workflow.app.participant.ParticipantContext;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.org.IPostQueryService;
import nc.vo.bd.psn.PsnjobVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.org.PostVO;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;

public class PostAdapter implements IParticipantAdapter{

	@Override
	public List<String> findUsers(ParticipantContext context) throws ParticipantException
	{
		checkValidity(context);
		
		//根据pk_post找到PsnjobVO，然后再找到user
		String pk_post = context.getParticipantID();
		if(StringUtil.isEmptyWithTrim(pk_post))
			return null;
		
		Logger.debug("[流程平台]岗位pk：" + pk_post);
		Collection<PsnjobVO> col;
		try {
			col = new BaseDAO().retrieveByClause(PsnjobVO.class, "pk_post='" + pk_post + "'");
		} catch (DAOException e) {
	    	throw new ParticipantException(e.getMessage(), e);
		}
		if(col == null || col.size() == 0)
			return null;
		
		StringBuilder psndocPks = new StringBuilder();
		for (Iterator<PsnjobVO> iterator = col.iterator(); iterator.hasNext();) {
			PsnjobVO psnjobVO = iterator.next();
			psndocPks.append("'"+ psnjobVO.getPk_psndoc()).append("',");
		}
		psndocPks.deleteCharAt(psndocPks.length()-1);
		Collection<UserVO> colUser;
		try {
			colUser = new BaseDAO().retrieveByClause(UserVO.class, "pk_base_doc in (" + psndocPks.toString() + ")");
		} catch (DAOException e) {
			throw new ParticipantException(e.getMessage(), e);
		}
		if(colUser == null || colUser.size() == 0)
			return null;
		
		List<String> userList = new ArrayList<String>();
		for (Iterator<UserVO> iterator = colUser.iterator(); iterator.hasNext();) {
			UserVO userVO = iterator.next();
			userList.add(userVO.getPrimaryKey());
		}
		return userList;
	}

	@Override
	public void checkValidity(ParticipantContext context) throws ParticipantException
	{
		// TODO Auto-generated method stub
		PostVO[] postVOs;
		try {
			postVOs = NCLocator.getInstance().lookup(IPostQueryService.class).queryPostVOsByPKs(new String[]{context.getParticipantID()});
		} catch (BusinessException e) {
	    	throw new ParticipantException(e.getMessage(), e);
		}
	    if(postVOs==null||postVOs.length==0)
	    	throw new ParticipantException("未找到岗位 "+context.getParticipantID()+ " 请检查该岗位是否已被删除");
	}

}
