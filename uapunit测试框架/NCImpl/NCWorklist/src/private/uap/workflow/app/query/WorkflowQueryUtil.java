package uap.workflow.app.query;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.metadata.IHeadBodyQueryItf;

import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.md.data.access.NCObject;
import nc.md.model.IBusinessEntity;
import nc.uap.pf.metadata.PfMetadataTools;
import nc.vo.pf.pub.IDapType;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 *
 */
public class WorkflowQueryUtil {
	private static final int MAX_QUERY_ROW_COUNT = 5000;
	
	/**
	 * 根据SQL语句查询对应的数据,使用Vector返回 <li>处理大数据量查询(只返回前5000行数)
	 * 
	 * @param strSQL    String 传入的SQL语句
	 * @param intCols   int 被返回的查询字段个数
	 * @param fieldType int[] 被返回的查询字段类型,必须为IDapType中的常量
	 */
	public static Vector queryDataBySQL(String strSQL, final int intCols, final int[] fieldType) throws DbException {

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			Vector vResult = (Vector) jdbc.executeQuery(strSQL, new BaseProcessor() {
				public Object processResultSet(ResultSet rs) throws SQLException {
					Vector retVec = new Vector();

					int lineCount = 0;
					int intRow = 1;
					while (rs.next()) {
						// 只返回前5000行数据
						if (lineCount++ >= MAX_QUERY_ROW_COUNT) {
							Logger.warn(">>WorknoteManager.queryDataBySQL():查询数据量太大,只返回前面5000数据");
							break;
						}

						Vector v = new Vector();
						v.addElement(Integer.valueOf(intRow++));
						for (int i = 1; i <= intCols; i++) {
							switch (fieldType[i - 1]) {
							case IDapType.INTEGER: {
								int tmpInt = rs.getInt(i);
								v.addElement(Integer.valueOf(tmpInt));
								break;
							}
							case IDapType.UFDOUBLE: {
								BigDecimal tmpDbl = (BigDecimal) rs.getObject(i);
								v.addElement(tmpDbl == null ? null : new UFDouble(tmpDbl));
								break;
							}
							case IDapType.STRING: {
								v.addElement(rs.getString(i));
								break;
							}
							default: {
								v.addElement(rs.getString(i));
								break;
							}
							}
						}
						retVec.addElement(v);
					}
					return retVec;
				}
			});
			return vResult;
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * 查询单据聚合VO
	 * 
	 * @param billType
	 * @param billId
	 * @return
	 * @throws BusinessException
	 */
	public static AggregatedValueObject fetchBillVO(String billType, String billId)
			throws BusinessException {

		// 1.先查询单据类型关联的元数据模型，使用业务接口IHeadBodyQueryItf查询单据VO
		IBusinessEntity be = PfMetadataTools.queryMetaOfBilltype(billType);
		IHeadBodyQueryItf qi = NCObject.newInstance(be, null).getBizInterface(IHeadBodyQueryItf.class);
		if (qi != null) {
			return qi.queryAggData(billType, billId);
		} else
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "PFConfigImpl-0000")/*单据实体没有实现IHeadBodyQueryItf，无法查询到单据子表VO*/);
	}	
}
