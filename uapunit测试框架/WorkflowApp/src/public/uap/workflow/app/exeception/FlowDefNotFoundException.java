package uap.workflow.app.exeception;

import nc.vo.ml.NCLangRes4VoTransl;

/**
 * ����ƽ̨-�ύʱ�Ҳ���ƥ������̶���
 * 
 * @author guowl
 * @since 6.0
 * 
 */
public class FlowDefNotFoundException extends PFBusinessException {

	private final static String errMsg = NCLangRes4VoTransl.getNCLangRes()
			.getStrByID("pfworkflow1", "FlowDefNotFoundException-000000")/*
																		 * ����û�ж�Ӧ������������
																		 * ,
																		 * �κ��˶�������ͨ��
																		 * !
																		 */;

	public FlowDefNotFoundException() {
		super(errMsg);
	}

}
