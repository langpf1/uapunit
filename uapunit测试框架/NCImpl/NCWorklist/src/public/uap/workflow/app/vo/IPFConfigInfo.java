package uap.workflow.app.vo;

/**
 * ��ƽ̨��������Ϣ���г�������
 * @author fangj 2005-1-21
 * @modifier leijun 2006-4-19 ����� ��Ϊ ��ɫ���
 */
public interface IPFConfigInfo {
	//ϵͳ�̶���ҵ������
	public static final String STATEBUSINESSTYPE = "KHHH0000000000000001";
	

	//	/*ͨ�õ������� ��׺��*/
	//	public static final String  BILLTYPE_COMMON = "-COMMON";
	//	
	//	public static final String  BILLTYPE_COMMON_NAME = "-COMMON";
	
	//��ǰ�������ڵ�
	public static final String CurrentWorkFlow = "CurrentWorkFlow";
	
	public static final String FUNCTION_OUT = "FunctionOutObject";

	//����ƽ̨��ı���ԭ����Ŀ¼
	public final static String ActionDir = "nc/bs/pub/action/";

	//�����������λ��
	public final static String ActionPack = "nc.bs.pub.action";

	/**
	 * ��������/Լ��-����Ա�޹�
	 */
	public static final int UserNoRelation = 1;

	/**
	 * ��������/Լ��-����Ա���
	 */
	public static final int UserRelation = 2;

	/**
	 * ��������/Լ��-�����--->>
	 * ��������/Լ��-��ɫ���
	 */
	public static final int RoleRelation = 3;
}