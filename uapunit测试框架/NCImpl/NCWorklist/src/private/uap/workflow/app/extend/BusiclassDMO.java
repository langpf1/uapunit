package uap.workflow.app.extend;

import java.util.List;

import nc.jdbc.framework.JdbcSession;
import nc.jdbc.framework.PersistenceManager;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.compiler.BusiclassVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * ����pub_busiclass���DMO��
 * <li>���ݶ����ű�������������Ϣ���浽��������ˡ�
 * <li>ִ�ж���ʱ�����Ǵ�������л�ȡ���������ơ�
 * 
 * @author ���ھ� 2002-3-12
 */
public class BusiclassDMO {

	public BusiclassDMO() {
		super();
	}

	/**
	 * ������������һ����¼
	 * 
	 * @param isBefore
	 * @param pk_group ��˾PK
	 * @param pk_billtype ��������PK
	 * @param actionType ��������
	 * @param pk_busitype ҵ������PK
	 * @return
	 * @throws DbException
	 */
	public BusiclassVO queryByCond(UFBoolean isBefore, String pk_group, String pk_billtype,
			String actionType, String pk_busitype) throws DbException {

		BusiclassVO busiclass = null;

		String strSql = "select pk_busiclass, pk_group, pk_billtype, actiontype, pk_businesstype, "
				+ "isbefore,classname, ts, dr from pub_busiclass";
		String strConditionNames = "";
		String strAndOr = "and ";
		SQLParameter para = new SQLParameter();
		if (isBefore != null) {
			strConditionNames += strAndOr + " isbefore=? ";
			para.addParam(isBefore);
		}

		if (pk_group != null) {
			strConditionNames += strAndOr + " pk_group=? ";
			para.addParam(pk_group);
		}
		if (pk_billtype != null) {
			String cond = " pk_billtype=? ";
			String realBilltype = null;
			if(PfUtilBaseTools.isTranstype(pk_billtype)) {
				realBilltype = PfUtilBaseTools.getRealBilltype(pk_billtype);
				cond = " (pk_billtype=? or pk_billtype='" + realBilltype + "') ";
			}
			strConditionNames += strAndOr + cond;
			para.addParam(pk_billtype);
		}
		if (actionType != null) {
			strConditionNames += strAndOr + " actiontype=? ";
			para.addParam(actionType);
		}
		if (StringUtil.isEmptyWithTrim(pk_busitype)) {
			strConditionNames += strAndOr + " isnull(pk_businesstype,'~') ='~' ";
		} else {
			strConditionNames += strAndOr + " pk_businesstype=? ";
			para.addParam(pk_busitype);
		}
		if (strConditionNames.length() > 0) {
			strConditionNames = strConditionNames.substring(3, strConditionNames.length() - 1);
		}
		// ƴ�Ӻ��SQL���
		strSql = strSql + " where " + strConditionNames;
		//
		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();

			List<BusiclassVO> list = (List<BusiclassVO>) jdbc.executeQuery(strSql, para,
					new BeanListProcessor(BusiclassVO.class));
			if(list == null)
				return null;
			if(list.size() >0) {
				for (BusiclassVO busiclassVO : list) {
					if(pk_billtype!= null && pk_billtype.equals(busiclassVO.getPk_billtype())) {
						busiclass = busiclassVO;
						break;
					}
				}
				if(busiclass == null)
					busiclass = list.get(0);
			} 
			return busiclass;
		} finally {
			if (persist != null)
				persist.release();
		}
	}

	/**
	 * ���ݹ�˾���������͡��������ơ�ҵ������ ���¶�Ӧ�����ݣ�ʱ�����
	 * 
	 * @param pk_group
	 * @param pk_billtype
	 * @param actionType
	 * @param pk_busitype
	 * @param ts
	 * @throws DbException
	 * @modifier �׾� 2005-8-15 �����׹���ͳһ�����ű� leijun 2005-8-15
	 */
	public void updateClassnameAndDrByCond(String classname, String pk_group, String pk_billtype,
			String actionType, String pk_busitype, UFDateTime ts) throws DbException {

		// Ϊ��֧��:�����׹���ͳһ�����ű�,���Ӷ�classname�ĸ��� leijun 2005-8-15
		String sql = "update pub_busiclass set dr=0,classname=? where ";
		String strCond = "";
		SQLParameter para = new SQLParameter();
		if (classname != null) {
			para.addParam(classname);
		}
		if (pk_group != null) {
			strCond += "and pk_group=? ";
			para.addParam(pk_group);
		}
		if (pk_billtype != null) {
			strCond += "and pk_billtype=? ";
			para.addParam(pk_billtype);
		}
		if (actionType != null) {
			strCond += "and actiontype=? ";
			para.addParam(actionType);
		}
		if (pk_busitype != null) {
			strCond += "and pk_businesstype=? ";
			para.addParam(pk_busitype);
		}
		sql = sql + strCond.substring(3);

		PersistenceManager persist = null;
		try {
			persist = PersistenceManager.getInstance();
			JdbcSession jdbc = persist.getJdbcSession();
			jdbc.executeUpdate(sql, para);
		} finally {
			if (persist != null)
				persist.release();
		}
	}
}