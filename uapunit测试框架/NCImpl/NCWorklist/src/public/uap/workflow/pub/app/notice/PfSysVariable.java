package uap.workflow.pub.app.notice;

import java.util.LinkedHashMap;

import nc.bs.logging.Logger;
import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ������Ϣ���� ��ϵͳ����
 * @author leijun 2007-4-28
 * @modifier leijun 2008-9 ����Ĭ��ϵͳ����"����������"
 */
public class PfSysVariable {

	public static final String DEFAULT_BILLMAKER = "BILLMAKER";

	public static final String DEFAULT_ALLAPPROVER = "ALLAPPROVER";
	
	//�ϻ���ʵ�ʴ�����
	public static final String DEFAULT_PREVIOUSAPPROVER="PREVIOUSAPPROVER";
	//�ϻ������̲�����
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
		 * ������
		 */
		private String name;

		/**
		 * ��������
		 */
		private String code;

		/**
		 * ����ȡֵ��
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
		//��ʼ��Ĭ��ϵͳ����
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
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000000", null, new String[]{code})/*ƽ̨ϵͳ����ע������Ѱ���={0}*/);

		lhmCodeToVar.put(code, ve);
	}

	public LinkedHashMap getAllVariables() {
		return this.lhmCodeToVar;
	}

	private VarEntry getVarEntry(String code) {
		if (!lhmCodeToVar.keySet().contains(code))
			throw new IllegalArgumentException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000001", null, new String[]{code})/*ƽ̨ϵͳ����������={0}*/);
		return lhmCodeToVar.get(code);
	}

	public void clear() {
		this.lhmCodeToVar.clear();
	}

	/**
	 * ȱʡϵͳ����
	 */
	private void initDefaultVar() {
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000002")/*�Ƶ���*/, DEFAULT_BILLMAKER, SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000003")/*����������*/, DEFAULT_ALLAPPROVER, SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000004")/*�ϻ���ʵ�ʴ�����*/,DEFAULT_PREVIOUSAPPROVER,SysVariableValueGetter.class.getName());
		register(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfSysVariable-000005")/*�ϻ������̲�����*/,DEFAULT_PREVIOUSPARTICIPATOR,SysVariableValueGetter.class.getName());
	}

	/**
	 * ���ݱ��������� ֵȡ��
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
