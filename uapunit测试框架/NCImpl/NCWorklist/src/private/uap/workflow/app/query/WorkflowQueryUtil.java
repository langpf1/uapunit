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
	 * ����SQL����ѯ��Ӧ������,ʹ��Vector���� <li>�������������ѯ(ֻ����ǰ5000����)
	 * 
	 * @param strSQL    String �����SQL���
	 * @param intCols   int �����صĲ�ѯ�ֶθ���
	 * @param fieldType int[] �����صĲ�ѯ�ֶ�����,����ΪIDapType�еĳ���
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
						// ֻ����ǰ5000������
						if (lineCount++ >= MAX_QUERY_ROW_COUNT) {
							Logger.warn(">>WorknoteManager.queryDataBySQL():��ѯ������̫��,ֻ����ǰ��5000����");
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
	 * ��ѯ���ݾۺ�VO
	 * 
	 * @param billType
	 * @param billId
	 * @return
	 * @throws BusinessException
	 */
	public static AggregatedValueObject fetchBillVO(String billType, String billId)
			throws BusinessException {

		// 1.�Ȳ�ѯ�������͹�����Ԫ����ģ�ͣ�ʹ��ҵ��ӿ�IHeadBodyQueryItf��ѯ����VO
		IBusinessEntity be = PfMetadataTools.queryMetaOfBilltype(billType);
		IHeadBodyQueryItf qi = NCObject.newInstance(be, null).getBizInterface(IHeadBodyQueryItf.class);
		if (qi != null) {
			return qi.queryAggData(billType, billId);
		} else
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "PFConfigImpl-0000")/*����ʵ��û��ʵ��IHeadBodyQueryItf���޷���ѯ�������ӱ�VO*/);
	}	
}
