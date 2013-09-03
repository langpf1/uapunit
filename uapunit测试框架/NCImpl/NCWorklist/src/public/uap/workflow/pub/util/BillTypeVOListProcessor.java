package uap.workflow.pub.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import nc.bs.logging.Logger;
import nc.jdbc.framework.processor.BaseProcessor;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * 用来将ResultSet转换为List<BilltypeVO>
 * 直接使用BeanListProcessor使用了过多反射，在PfDataCache初始化时需要处理大数据量、时耗时较长，无法通过性能测试
 * 因此增加了这个类, 用来实现将ResultSet映射到BilltypeVO
 * 
 * @author yanke1
 * @modifier yanke1 2012-3-31 重构一下，不再要求select语句中字段顺序，以后添加新字段时会容易一些
 * 
 */
public class BillTypeVOListProcessor extends BaseProcessor {

	/** 支持的字段Java类型列在下面 */
	/** 新增新类型的字段时，需要在下面和getValueOfField()方法中添加一下 */
	public static Class<?> STRING = String.class;
	public static Class<?> INTEGER = Integer.class;
	public static Class<?> UFBOOLEAN = UFBoolean.class;
	public static Class<?> UFDATETIME = UFDateTime.class;

	/** bd_billtype和BillTypeVO的字段 */
	/** 新增新字段时，需要在下面和transferResultSetRowToVO()方法中添加一下 */
	private static final BillTypeField ACCOUNTCLASS = new BillTypeField("ACCOUNTCLASS", STRING);
	private static final BillTypeField BILLSTYPE = new BillTypeField("BILLSTYLE", INTEGER);
	private static final BillTypeField BILLTYPENAME = new BillTypeField("BILLTYPENAME", STRING);
	private static final BillTypeField BILLTYPENAME2 = new BillTypeField("BILLTYPENAME2", STRING);
	private static final BillTypeField BILLTYPENAME3 = new BillTypeField("BILLTYPENAME3", STRING);
	private static final BillTypeField BILLTYPENAME4 = new BillTypeField("BILLTYPENAME4", STRING);
	private static final BillTypeField BILLTYPENAME5 = new BillTypeField("BILLTYPENAME5", STRING);
	private static final BillTypeField BILLTYPENAME6 = new BillTypeField("BILLTYPENAME6", STRING);
	private static final BillTypeField CANEXTENDTRANSACTION = new BillTypeField("CANEXTENDTRANSACTION", UFBOOLEAN);
	private static final BillTypeField CHECKCLASSNAME = new BillTypeField("CHECKCLASSNAME", STRING);
	private static final BillTypeField CLASSNAME = new BillTypeField("CLASSNAME", STRING);
	private static final BillTypeField COMPONENT = new BillTypeField("COMPONENT", STRING);
	private static final BillTypeField DATAFINDERCLZ = new BillTypeField("DATAFINDERCLZ", STRING);
	private static final BillTypeField DEF1 = new BillTypeField("DEF1", STRING);
	private static final BillTypeField DEF2 = new BillTypeField("DEF2", STRING);
	private static final BillTypeField DEF3 = new BillTypeField("DEF3", STRING);
	private static final BillTypeField DR = new BillTypeField("DR", INTEGER);
	private static final BillTypeField FORWARDBILLTYPE = new BillTypeField("FORWARDBILLTYPE", STRING);
	private static final BillTypeField ISACCOUNT = new BillTypeField("ISACCOUNT", UFBOOLEAN);
	private static final BillTypeField ISAPPROVEBILL = new BillTypeField("ISAPPROVEBILL", UFBOOLEAN);
	private static final BillTypeField ISBIZFLOWBILL = new BillTypeField("ISBIZFLOWBILL", UFBOOLEAN);
	private static final BillTypeField ISLOCK = new BillTypeField("ISLOCK", UFBOOLEAN);
	private static final BillTypeField ISROOT = new BillTypeField("ISROOT", UFBOOLEAN);
	private static final BillTypeField ISTRANSACTION = new BillTypeField("ISTRANSACTION", UFBOOLEAN);
	private static final BillTypeField NODECODE = new BillTypeField("NODECODE", STRING);
	private static final BillTypeField PARENTBILLTYPE = new BillTypeField("PARENTBILLTYPE", STRING);
	private static final BillTypeField PK_BILLTYPECODE = new BillTypeField("PK_BILLTYPECODE", STRING);
	private static final BillTypeField PK_BILLTYPEID = new BillTypeField("PK_BILLTYPEID", STRING);
	private static final BillTypeField PK_GROUP = new BillTypeField("PK_GROUP", STRING);
	private static final BillTypeField PK_ORG = new BillTypeField("PK_ORG", STRING);
	private static final BillTypeField REFERCLASSNAME = new BillTypeField("REFERCLASSNAME", STRING);
	private static final BillTypeField SYSTEMCODE = new BillTypeField("SYSTEMCODE", STRING);
	private static final BillTypeField TRANSTYPE_CLASS = new BillTypeField("TRANSTYPE_CLASS", STRING);
	private static final BillTypeField TS = new BillTypeField("TS", UFDATETIME);
	private static final BillTypeField WEBNODECODE = new BillTypeField("WEBNODECODE", STRING);
	private static final BillTypeField WHERESTRING = new BillTypeField("WHERESTRING", STRING);
	private static final BillTypeField ISEDITABLEPROPERTY = new BillTypeField("ISEDITABLEPROPERTY", UFBOOLEAN);
	private static final BillTypeField ISENABLEBUTTON = new BillTypeField("ISENABLEBUTTON", UFBOOLEAN);
	private static final BillTypeField EMENDENUMCLASS = new BillTypeField("EMENDENUMCLASS", STRING);
	private static final BillTypeField NCBRCODE = new BillTypeField("NCBRCODE", STRING);
	private static final BillTypeField BILLCODERULE = new BillTypeField("BILLCODERULE", STRING);

	@Override
	public Object processResultSet(ResultSet rs) throws SQLException {
		List<BilltypeVO> list = new ArrayList<BilltypeVO>();

		while (rs.next()) {
			try {
				BilltypeVO bvo = transferResultSetRowToVO(rs);
				list.add(bvo);
			} catch (Exception e) {
				Logger.error(e.getMessage(), e);
			}
		}

		return list;
	}

	public static String getFieldString() {
		// yk1 2012-3-31 重构之后，对resultSet中字段的顺序无要求了
		// 因此此处返回*
		return " * ";
	}

	private BilltypeVO transferResultSetRowToVO(ResultSet rs) throws Exception {
		BilltypeVO bvo = new BilltypeVO();

		bvo.setAccountclass((String) getValueOfField(rs, ACCOUNTCLASS));
		bvo.setBillstyle((Integer) getValueOfField(rs, BILLSTYPE));

		bvo.setBilltypename((String) getValueOfField(rs, BILLTYPENAME));
		bvo.setBilltypename2((String) getValueOfField(rs, BILLTYPENAME2));
		bvo.setBilltypename3((String) getValueOfField(rs, BILLTYPENAME3));
		bvo.setBilltypename4((String) getValueOfField(rs, BILLTYPENAME4));
		bvo.setBilltypename5((String) getValueOfField(rs, BILLTYPENAME5));
		bvo.setBilltypename6((String) getValueOfField(rs, BILLTYPENAME6));

		bvo.setCanextendtransaction((UFBoolean) getValueOfField(rs, CANEXTENDTRANSACTION));
		bvo.setCheckclassname((String) getValueOfField(rs, CHECKCLASSNAME));
		bvo.setClassname((String) getValueOfField(rs, CLASSNAME));
		bvo.setComponent((String) getValueOfField(rs, COMPONENT));
		bvo.setDatafinderclz((String) getValueOfField(rs, DATAFINDERCLZ));
		bvo.setDef1((String) getValueOfField(rs, DEF1));
		bvo.setDef2((String) getValueOfField(rs, DEF2));
		bvo.setDef3((String) getValueOfField(rs, DEF3));
		bvo.setDr((Integer) getValueOfField(rs, DR));
		bvo.setForwardbilltype((String) getValueOfField(rs, FORWARDBILLTYPE));

		bvo.setIsaccount((UFBoolean) getValueOfField(rs, ISACCOUNT));
		bvo.setIsapprovebill((UFBoolean) getValueOfField(rs, ISAPPROVEBILL));
		bvo.setIsBizflowBill((UFBoolean) getValueOfField(rs, ISBIZFLOWBILL));
		bvo.setIsLock((UFBoolean) getValueOfField(rs, ISLOCK));
		bvo.setIsroot((UFBoolean) getValueOfField(rs, ISROOT));
		bvo.setIstransaction((UFBoolean) getValueOfField(rs, ISTRANSACTION));

		bvo.setNodecode((String) getValueOfField(rs, NODECODE));
		bvo.setParentbilltype((String) getValueOfField(rs, PARENTBILLTYPE));
		bvo.setPk_billtypecode((String) getValueOfField(rs, PK_BILLTYPECODE));
		bvo.setPk_billtypeid((String) getValueOfField(rs, PK_BILLTYPEID));
		bvo.setPk_group((String) getValueOfField(rs, PK_GROUP));
		bvo.setPk_org((String) getValueOfField(rs, PK_ORG));
		bvo.setReferclassname((String) getValueOfField(rs, REFERCLASSNAME));
		bvo.setSystemcode((String) getValueOfField(rs, SYSTEMCODE));
		bvo.setTranstype_class((String) getValueOfField(rs, TRANSTYPE_CLASS));
		bvo.setTs((UFDateTime) getValueOfField(rs, TS));
		bvo.setWebNodecode((String) getValueOfField(rs, WEBNODECODE));
		bvo.setWherestring((String) getValueOfField(rs, WHERESTRING));

		bvo.setIsEditableProperty((UFBoolean) getValueOfField(rs, ISEDITABLEPROPERTY));
		bvo.setIsEnableButton((UFBoolean) getValueOfField(rs, ISENABLEBUTTON));

		bvo.setEmendEnumClass((String) getValueOfField(rs, EMENDENUMCLASS));
		bvo.setNcbrcode((String) getValueOfField(rs, NCBRCODE));
		bvo.setBillcoderule((String) getValueOfField(rs, BILLCODERULE));

		return bvo;
	}

	private Object getValueOfField(ResultSet rs, BillTypeField field) throws SQLException {
		Class<?> fieldClass = field.getFieldClass();
		String fieldName = field.getFieldName();

		fieldName = fieldName.toLowerCase();

		if (fieldClass.equals(STRING)) {
			return rs.getString(fieldName);
		} else if (fieldClass.equals(INTEGER)) {
			String value = rs.getString(fieldName);

			if (StringUtil.isEmptyWithTrim(value)) {
				return null;
			} else {
				return Integer.parseInt(value);
			}
			
		} else if (fieldClass.equals(UFBOOLEAN)) {
			String value = rs.getString(fieldName);

			if (StringUtil.isEmptyWithTrim(value)) {
				return null;
			} else {
				return new UFBoolean(value);
			}
		} else if (fieldClass.equals(UFDATETIME)) {
			String value = rs.getString(fieldName);

			if (StringUtil.isEmptyWithTrim(value)) {
				return null;
			} else {
				// yanke1
				// 生成UFDateTime时需要取默认时区而不是取前台当前时区
				return new UFDateTime(value);
			}
		} else {
			throw new IllegalArgumentException("Field type not supported: " + fieldClass.getCanonicalName());
		}
	}

	/**
	 * 标识BillTypeVO的字段
	 * 
	 * @author yanke1 2012-3-31
	 * 
	 */
	private static class BillTypeField {
		private String fieldName;
		private Class<?> fieldClass;

		/**
		 * @param fieldName
		 *            字段名称
		 * @param fieldClass
		 *            字段Java类型
		 */
		protected BillTypeField(String fieldName, Class<?> fieldClass) {
			this.fieldName = fieldName;
			this.fieldClass = fieldClass;
		}

		protected String getFieldName() {
			return fieldName;
		}

		protected Class<?> getFieldClass() {
			return fieldClass;
		}
	}

}
