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
 * 操作pub_busiclass表的DMO类
 * <li>单据动作脚本编译后，其相关信息保存到这个表中了。
 * <li>执行动作时，就是从这个表中获取动作类名称。
 * 
 * @author 樊冠军 2002-3-12
 */
public class BusiclassDMO {

	public BusiclassDMO() {
		super();
	}

	/**
	 * 根据条件查找一条记录
	 * 
	 * @param isBefore
	 * @param pk_group 公司PK
	 * @param pk_billtype 单据类型PK
	 * @param actionType 动作名称
	 * @param pk_busitype 业务类型PK
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
		// 拼接后的SQL语句
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
	 * 根据公司、单据类型、动作名称、业务类型 更新对应的数据（时间戳）
	 * 
	 * @param pk_group
	 * @param pk_billtype
	 * @param actionType
	 * @param pk_busitype
	 * @param ts
	 * @throws DbException
	 * @modifier 雷军 2005-8-15 多帐套共享统一动作脚本 leijun 2005-8-15
	 */
	public void updateClassnameAndDrByCond(String classname, String pk_group, String pk_billtype,
			String actionType, String pk_busitype, UFDateTime ts) throws DbException {

		// 为了支持:多帐套共享统一动作脚本,增加对classname的更新 leijun 2005-8-15
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