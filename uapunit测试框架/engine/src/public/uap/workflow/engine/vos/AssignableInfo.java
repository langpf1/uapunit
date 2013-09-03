package uap.workflow.engine.vos;
import java.io.Serializable;
import java.util.Vector;
import nc.vo.uap.pf.OrganizeUnit;
/**
 * ĳ��ĺ�̿�ָ�ɻ��ָ����Ϣ
 * 
 * @author wzhy 2005-2-3
 * @modifier leijun 2006-5-23
 * @modifier leijun 2008-10 ��ָ�ɻ�������Ϣ����checkResultCriterion
 */
public class AssignableInfo implements Serializable {
	private static final long serialVersionUID = 4133123187564240043L;
	/**
	 * ·���ϵ�����ת�Ƶ��������������Ϊ"ͨ��"����
	 */
	public static String CRITERION_PASS = "Y";
	/**
	 * ·���ϵ�����ת�Ƶ��������������Ϊ"��ͨ��"����
	 */
	public static String CRITERION_NOPASS = "N";
	/**
	 * ·��������ת�Ƶ��������������Ϊ��
	 */
	public static String CRITERION_NOTGIVEN = "NotGiven";
	/**
	 * ·��������ת�Ƶ����������������"ͨ��"��"��ͨ��"���������ɴ�(��Ч)
	 */
	public static String CRITERION_INVALID = "N/A";
	/** ���ָ�ɽ��,������ԱPK���� */
	private Vector<String> assignedOperatorPKs;
	/** �������ӵ�е����в���ԱPK���� */
	private Vector<String> operatorPKs;
	/** �������ӵ�е����в���Ա���� */
	private Vector<OrganizeUnit> ouUsers;
	/** ���ָ�ɽ��,������Ա���� */
	private Vector<OrganizeUnit> ouAssignedUsers;
	/** ָ������,���������ָ�ɵ������,һ��Ϊָ�ɻ������ */
	private String desc;
	/** ָ�ɶ���,���������guid */
	private String activityDefId;
	/** ָ�ɶ���,��������������̶����PK */
	private String processDefPK;
	/** ��ǰ����ָ�ɻ ·����������ת�Ƶĺϲ���������ж� */
	private String checkResultCriterion;
	public AssignableInfo() {
		super();
	}
	public String getCheckResultCriterion() {
		return checkResultCriterion;
	}
	public void setCheckResultCriterion(String checkResultCriterion) {
		this.checkResultCriterion = checkResultCriterion;
	}
	/**
	 * ���ָ�ɽ��,������ԱPK����
	 * 
	 * @return
	 */
	public Vector<String> getAssignedOperatorPKs() {
		if (assignedOperatorPKs == null) {
			// ��ʼ������
			assignedOperatorPKs = new Vector<String>();
		}
		return assignedOperatorPKs;
	}
	/**
	 * �������ӵ�е����в���ԱPK����
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Vector<String> getOperatorPKs() {
		if (operatorPKs == null) {
			// ��ʼ������
			operatorPKs = new Vector();
		}
		return operatorPKs;
	}
	/**
	 * ���ָ�ɽ��,������Ա����
	 * 
	 * @return
	 */
	public Vector<OrganizeUnit> getOuAssignedUsers() {
		if (ouAssignedUsers == null) {
			// ��ʼ������
			ouAssignedUsers = new Vector<OrganizeUnit>();
		}
		return ouAssignedUsers;
	}
	/**
	 * �������ӵ�е����в���Ա����
	 * 
	 * @return
	 */
	public Vector<OrganizeUnit> getOuUsers() {
		if (ouUsers == null) {
			// ��ʼ������
			ouUsers = new Vector<OrganizeUnit>();
		}
		return ouUsers;
	}
	public String getDesc() {
		return desc;
	}
	/**
	 * ��ñ�ָ�ɻ��GUID
	 * 
	 * @return
	 */
	public String getActivityDefId() {
		return activityDefId;
	}
	/**
	 * ��ñ�ָ�ɻ�������̶����PK
	 * 
	 * @return
	 */
	public String getProcessDefPK() {
		return processDefPK;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * ���ñ�ָ�ɻ��GUID
	 * 
	 * @param target
	 */
	public void setActivityDefId(String target) {
		this.activityDefId = target;
	}
	/**
	 * ���ñ�ָ�ɻ�������̶����PK
	 */
	public void setProcessDefPK(String target) {
		this.processDefPK = target;
	}
}