package uap.workflow.pub.app.notice;

import java.util.LinkedHashMap;

import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * 用于消息配置 的系统变量
 * @author leijun 2007-4-28
 * @modifier leijun 2008-9 增加默认系统变量"所有审批人"
 */
public class PfSysVariable {

	public static final String DEFAULT_BILLMAKER = "BILLMAKER";

	public static final String DEFAULT_ALLAPPROVER = "ALLAPPROVER";
	
	//上环节实际处理人
	public static final String DEFAULT_PREVIOUSAPPROVER="PREVIOUSAPPROVER";
	//上环节流程参与者
	public static final String DEFAULT_PREVIOUSPARTICIPATOR="PREVIOUSPARTICIPATOR";

	private static PfSysVariable inst = new PfSysVariable();

	private LinkedHashMap<String, VarEntry> lhmCodeToVar = new LinkedHashMap<String, VarEntry>();

	public class VarEntry {
		VarEntry(String name, String code, String valueGetterClz) {
			this.name = name;
			this.code = code;
			this.valueGetterClz = valueGetterClz;
		}

		/**
		 * 变量名
		 */
		private String name;

		/**
		 * 变量编码
		 */
		private String code;

		/**
		 * 变量取值类
		 */
		private String valueGetterClz;

		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return this.name;
		}

		public String getValueGetterClz() {
			return this.valueGetterClz;
		}
	}

	private PfSysVariable() {
		//初始化默认系统变量
		initDefaultVar();
	}

	public static PfSysVariable instance() {
		return inst;
	}

	/**
	 * @param name
	 * @param code
	 */
	public void register(String name, String code, String valueGetterClz) {
		VarEntry ve = new VarEntry(name, code, valueGetterClz);
		if (lhmCodeToVar.keySet().contains(code))
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000000", null, new String[]{code})/*平台系统变量注册错误：已包含={0}*/);

		lhmCodeToVar.put(code, ve);
	}

	public LinkedHashMap getAllVariables() {
		return this.lhmCodeToVar;
	}

	private VarEntry getVarEntry(String code) {
		if (!lhmCodeToVar.keySet().contains(code))
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000001", null, new String[]{code})/*平台系统变量不存在={0}*/);
		return lhmCodeToVar.get(code);
	}

	public void clear() {
		this.lhmCodeToVar.clear();
	}

	/**
	 * 缺省系统变量
	 */
	private void initDefaultVar() {
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000002")/*制单人*/, DEFAULT_BILLMAKER, SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000003")/*所有审批人*/, DEFAULT_ALLAPPROVER, SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000004")/*上环节实际处理人*/,DEFAULT_PREVIOUSAPPROVER,SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000005")/*上环节流程参与者*/,DEFAULT_PREVIOUSPARTICIPATOR,SysVariableValueGetter.class.getName());
	}

	/**
	 * 根据变量编码获得 值取器
	 * @param code
	 * @param context
	 * @return
	 */
	public SysVariableValueGetter instanceValueGetter(String code, Object context) {
		VarEntry ve = getVarEntry(code);
		Object objImpl = null;
		try {
			objImpl = Class.forName(ve.getValueGetterClz()).getConstructor(new Class[] { Object.class })
					.newInstance(new Object[] { context });
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
		}

		if (objImpl instanceof SysVariableValueGetter)
			return (SysVariableValueGetter) objImpl;
		return null;
	}
}
