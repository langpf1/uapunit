package uap.workflow.app.vo;

/**
 * ���̳�����
 * 
 * @author �׾� 2004-10-27
 * @modifier �׾� 2005-1-20 �������������������ö��
 * FIXME��������
 */
public interface IApproveflowConst {

	/** ��ǰ��½���� */
	public static final String LOGIN_GROUP_PK = "LOGINGROUP";
	/**��ǰ�������ͱ���*/
	public static final String CURRENT_BILLTYPE_PK = "CURRENT_BILLTYPE";
	/**��ǰ��������pk*/
	public static final String CURRENT_BILLTYPE_ID="CURRENT_BILLTYPE_PK";

	/** �Ƶ�����Ϣ */
	public static final String BILLMAKER = "BILLMAKER";

	public static final String BILLMAKER_NAME = "BILLMAKER_NAME";

	public static final String BILLMAKER_TYPE = "BILLMAKER_TYPE";

	/** ����ͨ��/��ͨ���Ĺ̶��������ʽ */
	public static final String CHECK_PASS = "ACT_Check_Result.equals(\"Y\")";

	public static final String CHECK_NOPASS = "ACT_Check_Result.equals(\"N\")";

	/** �������̣���¼��������ı������� */
	public static final String ACT_CHECK_RESULT = "ACT_Check_Result";

	public static final String CHECK_RESULT = "Check_Result";

	public static int DEFAULT_APPROVE_HEIGHT = 51;

	public static int DEFAULT_APPROVE_WIDTH = 51;

	/** ����������� */
	public static final int CHECK_RESULT_PASS = 0;

	public static final int CHECK_RESULT_NOPASS = 1;

	public static final int CHECK_RESULT_REJECT_LAST = 2;

	public static final int CHECK_RESULT_REJECT_FIRST = 3;
	
	public static final String CURRENT_APPLICATIONARGNAME = "CURRENT_APPLICATIONARGNAME";

	/**�����޶�**/
	//	public final static String sameCorpPattern = "@corp@";
	//	public final static String sameDeptPattern = "@dept@";
	//	public final static String sameDeptAndCorpPattern = "@deptcorp@";
}