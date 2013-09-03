package uap.workflow.engine.dao;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.jdbc.framework.mapping.IMappingMeta;
import nc.jdbc.framework.page.LimitSQLBuilder;
import nc.jdbc.framework.page.SQLBuilderFactory;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.ResultSetProcessor;
import nc.uap.lfw.core.data.PaginationInfo;
import nc.vo.pub.SuperVO;
import uap.workflow.engine.logger.WorkflowLogger;
/**
 * 
 * �����֯�µĻ����
 * 
 * @author tianchw
 * 
 */
public class WfBaseDAO {
	public WfBaseDAO() {}
	private String dataSource = null;
	public WfBaseDAO(String dataSource) {
		this.dataSource = dataSource;
	}
	public String insertVo(SuperVO vo) throws DAOException {
		if (vo == null)
			return null;
		return insertVos(new SuperVO[] { vo })[0];
	}
	public String[] insertVos(SuperVO[] vos) throws DAOException {
		if (vos == null || vos.length == 0) {
			return null;
		}
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			String groupno = getGroupNO();
			String[] pks = new SequenceGenerator(dataSource).generate(groupno, vos.length);
			for (int i = 0; i < vos.length; i++) {
				vos[i].setPrimaryKey(pks[i]);
			}
			return manager.insertWithPK(vos);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
	}
	private String getGroupNO() {
		return "0001";
	}
	public String insertVoWithPk(SuperVO superVo) throws DAOException {
		PersistenceManager manager = null;
		String pk = null;
		try {
			manager = createPersistenceManager(dataSource);
			pk = manager.insertWithPK(superVo);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
		return pk;
	}
	public String[] insertVosWithPks(SuperVO[] vos) throws DAOException {
		PersistenceManager manager = null;
		String[] pks = null;
		try {
			manager = createPersistenceManager(dataSource);;
			pks = manager.insertWithPK(vos);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
		return pks;
	}
	public String insertObject(Object vo, IMappingMeta meta) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);;
			return manager.insertObject(vo, meta);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public String insertObjectWithPk(Object vo, IMappingMeta meta) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);;
			return manager.insertObjectWithPK(vo, meta);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
	}
	public String[] insertObjectsWithPk(Object[] vo, IMappingMeta meta) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);;
			return manager.insertObjectWithPK(vo, meta);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
	}
	public void deleteVo(SuperVO vo) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.delete(vo);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public void deleteVos(SuperVO[] vos) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.delete(vos);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
//	public void deleteAggVo(AggregatedValueObject billVo) throws BusinessException {
//		new BillDelete().deleteBill(billVo);
//	}
	public void deleteByPk(Class<? extends SuperVO> className, String pk) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByPK(className, pk);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public void deleteByPks(Class<? extends SuperVO> className, String[] pks) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByPKs(className, pks);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public void deleteByClause(Class<? extends SuperVO> className, String wherestr) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByClause(className, wherestr);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public void deleteByClause(Class<? extends SuperVO> className, String wherestr, SQLParameter params) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			manager.deleteByClause(className, wherestr, params);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public int executeUpdate(String sql) throws DAOException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
	public int executeUpdate(String sql, SQLParameter parameter) throws DAOException {
		PersistenceManager manager = null;
		int value;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeUpdate(sql, parameter);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
	public int updateVo(SuperVO vo) throws DAOException {
		return updateVos(new SuperVO[] { vo });
	}
	public int updateVos(SuperVO[] vos) throws DAOException {
		return updateVos(vos, null);
	}
	public int updateVos(SuperVO[] vos, String[] fieldNames) throws DAOException {
		return updateVos(vos, fieldNames, null, null);
	}
	public int updateVos(final SuperVO[] vos, String[] fieldNames, String whereClause, SQLParameter param) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			return manager.update(vos, fieldNames, whereClause, param);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public int updateObject(Object vo, IMappingMeta meta) throws DAOException {
		return updateObject(vo, meta, null);
	}
	public int updateObject(Object vo, IMappingMeta meta, String whereClause) throws DAOException {
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			return manager.updateObject(vo, meta, whereClause);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
	}
	public Object executeQuery(String sql, ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		try {
			manager = createPersistenceManager(dataSource);
			JdbcSession session = manager.getJdbcSession();
			value = session.executeQuery(sql, processor);
			
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
   /**
    * 构建分页查找sql语句
    * @author zhailzh
    * */
	public String creatQuerySqlBypage(String sql,PaginationInfo pg){
	
		PersistenceManager pm = null;
		try {
			pm = PersistenceManager.getInstance();
		} catch (DbException e) {
			e.printStackTrace();
		}
		int index = pg.getPageIndex();
	    int pageSize = pg.getPageSize();
		LimitSQLBuilder builder = SQLBuilderFactory.getInstance().createLimitSQLBuilder(pm.getDBType());	
	    sql = builder.build(sql, index + 1, pageSize);  
	    return sql;		
	}
	public Object executeQuery(String sql, SQLParameter parameter, ResultSetProcessor processor) throws DAOException {
		PersistenceManager manager = null;
		Object value = null;
		try {
			manager = createPersistenceManager(dataSource);;
			JdbcSession session = manager.getJdbcSession();
			value = session.executeQuery(sql, parameter, processor);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return value;
	}
	@SuppressWarnings("unchecked")
	public SuperVO[] queryByCondition(Class<? extends SuperVO> voClass, String strWhere) throws DAOException {
		if (strWhere != null && strWhere.length() != 0)
			strWhere = " (dr=0) and " + strWhere;
		else
			strWhere = "dr=0";
		PersistenceManager manager = null;
		try {
			manager = createPersistenceManager(dataSource);
			List<SuperVO> list = (List<SuperVO>) manager.retrieveByClause(voClass, strWhere);
			return (SuperVO[]) list.toArray((SuperVO[]) Array.newInstance(voClass, 0));
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null) {
				manager.release();
			}
		}
	}
	@SuppressWarnings("unchecked")
	public Collection<? extends SuperVO> retrieveAll(Class<? extends SuperVO> className) throws DAOException {
		PersistenceManager manager = null;
		Collection<? extends SuperVO> values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveAll(className);
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	public Object retrieveByPk(Class<? extends SuperVO> className, String pk) throws DAOException {
		PersistenceManager manager = null;
		Object values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveByPK(className, pk);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	@SuppressWarnings("unchecked")
	public Collection<? extends SuperVO> retrieveByClause(Class<? extends SuperVO> className, String condition) throws DAOException {
		PersistenceManager manager = null;
		Collection<SuperVO> values = null;
		try {
			manager = createPersistenceManager(dataSource);
			values = manager.retrieveByClause(className, condition);
		} catch (DbException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new DAOException(e.getMessage());
		} finally {
			if (manager != null)
				manager.release();
		}
		return values;
	}
	private PersistenceManager createPersistenceManager(String ds) throws DbException {
		PersistenceManager manager = PersistenceManager.getInstance(ds);
		return manager;
	}
}
