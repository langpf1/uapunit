/**
 * 
 */
package uap.workflow.bizitf.bizaction;

import java.util.Vector;
import uap.workflow.bizimpl.BizAction;
import uap.workflow.bizitf.exception.BizException;


/**
 * @author 
 *
 */
public interface IBizActionHelper {
	
	
	/*
	 * ��������ҵ���������
	 */
	Vector<String> getObjectTypeGroupList() throws BizException;
	/*
	 * ���ն������ͷ���ҵ������
	 */
	Vector<String> getAllObjectTypeList() throws BizException;
	/*
	 * ���շ��鷵��ҵ������
	 */
	Vector<String> getObjectTypeListByGroup(String group) throws BizException;
	
	
	
	/*
	 * �������еĶ������ͼ����� 
	 */
	Vector<BizAction> getAllBizActionList() throws BizException;
	/*
	 * ���շ��鷵�ض����б�
	 */
	Vector<BizAction> getBizActionListByGroup(String group) throws BizException;
	/*
	 * ���ն������ͷ��ض����б�
	 */
	Vector<BizAction> getBizActionListByObjectType(String objectType) throws BizException;
	/*
	 * ���ض������Ͷ�Ӧ�Ĳ���
	 */
	BizAction getBizActionByAction(String objectType, String action);	
	
	/*
	 * ���ն����������ƥ��
	 */
	Boolean MatchActionToActivity(BizAction bizAction, Object activity);
}
