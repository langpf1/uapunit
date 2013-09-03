package uap.workflow.nc.participant.wfusergroup;

import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.pub.workflowusergroup.UserGroupControlCTL;

public class UserGroupDMO {

	public UserGroupDMO() {
		super();
	}

	public WFUserGroupDetailVO[] getUserGroupDetailVOBySuperPK(String pk) throws DbException {
		String querySQL = "select * from pub_wfgroup_b where pk_wfgroup = ? ";
		PersistenceManager persist = null;
		List<WFUserGroupDetailVO> listResult;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(pk);
			listResult = (List) jdbc.executeQuery(querySQL, para, new BeanListProcessor(
					WFUserGroupDetailVO.class));
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return null;
		} finally {
			if (persist != null)
				persist.release();
		}
		return (WFUserGroupDetailVO[]) listResult.toArray(new WFUserGroupDetailVO[] {});
	}

	public WFUserGroupDetailVO[] getUserGroupDetailVO(WFUserGroupVO vo) throws DbException {
		String querySQL = null;
		if (vo.getDeftype().equals(String.valueOf(WfGroupType.DisperseType.getIntValue()))) {
			querySQL = "select * from pub_wfgroup_b where pk_wfgroup = ? and isnull(rule_type,'~') != '~' ";
		} else {
			querySQL = "select * from pub_wfgroup_b where pk_wfgroup = ? and isnull(rule_type,'~') = '~' ";
		}
		PersistenceManager persist = null;
		List<WFUserGroupDetailVO> listResult;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(vo.getPk_wfgroup());
			listResult = (List) jdbc.executeQuery(querySQL, para, new BeanListProcessor(
					WFUserGroupDetailVO.class));
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return null;
		} finally {
			if (persist != null)
				persist.release();
		}
		return (WFUserGroupDetailVO[]) listResult.toArray(new WFUserGroupDetailVO[] {});
	}

	public WFUserGroupVO[] getUserGroupVO(String pk) throws DbException {
		String querySQL = "select * from pub_wfgroup where pk_wfgroup = ? ";
		PersistenceManager persist = null;
		List<WFUserGroupDetailVO> listResult;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();
			para.addParam(pk);
			listResult = (List) jdbc.executeQuery(querySQL, para, new BeanListProcessor(
					WFUserGroupVO.class));
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return null;
		} finally {
			if (persist != null)
				persist.release();
		}
		return (WFUserGroupVO[]) listResult.toArray(new WFUserGroupVO[] {});
	}

	public boolean isCheckRepeat(String code, String pk_wf_dynamicmember, int m_state)
			throws DbException {
		//流程用户组编码是在集团内限制，而不是全局唯一
		String addSql = "select * from pub_wfgroup where code = ? and pk_group=?";
		String updateSql = "select * from pub_wfgroup where code = ? and pk_wfgroup != ? and pk_group=?";
		PersistenceManager persist = null;
		List<WFUserGroupDetailVO> listResult;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			SQLParameter para = new SQLParameter();			
			if (m_state == UserGroupControlCTL.ADD) {
				para.addParam(code);
				para.addParam(InvocationInfoProxy.getInstance().getGroupId());
				listResult = (List) jdbc.executeQuery(addSql, para, new BeanListProcessor(
						WFUserGroupDetailVO.class));
			} else {
				para.addParam(code);
				para.addParam(pk_wf_dynamicmember);
				para.addParam(InvocationInfoProxy.getInstance().getGroupId());
				listResult = (List) jdbc.executeQuery(updateSql, para, new BeanListProcessor(
						WFUserGroupDetailVO.class));
			}
			if (listResult.size() > 0) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			Logger.error(ex.getMessage(),ex);
			return false;
		} finally {
			if (persist != null)
				persist.release();
		}

	}
}
