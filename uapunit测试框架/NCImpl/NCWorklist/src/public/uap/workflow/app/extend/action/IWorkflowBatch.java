package uap.workflow.app.extend.action;

import java.util.Hashtable;

/**
 * ����������ű� �ķ���ֵ
 * 
 * @author ���ھ� 2002-4-16
 */
public interface IWorkflowBatch {
	/**
	 * ����δͨ��������еĵ���������
	 */
	Hashtable getNoPassAndGoing();

	/**
	 * ����û�����
	 */
	Object getUserObj();
}
