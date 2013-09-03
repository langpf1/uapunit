package uap.workflow.pub.app.message;

import java.util.HashMap;

import uap.workflow.pub.app.message.vo.CommonMessageVO;
import uap.workflow.pub.app.message.vo.MessageDateTimeVO;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.mobile.vo.MobileMsg;

import nc.message.vo.NCMessage;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;


/**
 * ����ƽ̨��Ϣ����ӿ�
 * 
 * @author �׾� 2005-8-17 9:48:04
 * @modifier guowl 2008-4-2 Ϊʵ�ֹ�����Ϣ�ķ����޸������µĽӿ�
 */
public interface IPFMessage {

	/**
	 * ������ͨ��Ϣ.������������Ϣ
	 * 
	 * @param cMsgVO ��ͨ��ϢVO
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void insertCommonMsg(CommonMessageVO cMsgVO) throws BusinessException;

	/**
	 * ������ͨ��Ϣ.
	 * 
	 * @param cMsgVO ��ͨ��ϢVO
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void updateCommonMsg(MessageVO cMsgVO) throws BusinessException;

	/**
	 * ������ͨ��ϢVO����
	 * 
	 * @param cMsgVOs ��ͨ��ϢVO����
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void insertCommonMsgAry(CommonMessageVO[] cMsgVOs) throws BusinessException;
	
	/**
	 * ������ͨ��ϢVO���飨��������
	 * 
	 * @param cMsgVOs ��ͨ��ϢVO����
	 * @throws BusinessException
	 * @since 5.6
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void insertCommonMsgAry_RequiresNew(CommonMessageVO[] cMsgVOs) throws BusinessException;
	
	/**
	 * ������ͨ��Ϣ-��Ҫ�����ڷ�����Ϣ��������Ϣ��
	 * 
	 * @param cMsgVO �����͵���Ϣ
	 * @param pkcorp �洢�Ĺ�˾PK
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void insertCommonMsg(CommonMessageVO cMsgVO, String pkcorp) throws BusinessException;

	/**
	 * ������ͨ��Ϣ-��Ҫ�����ڷ�����Ϣ��������Ϣ��
	 * 
	 * @param cMsgVO �����͵���Ϣ
	 * @param pkcorp �洢�Ĺ�˾PK
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void insertCommonMsgs(CommonMessageVO[] cMsgVOs, String pkcorp) throws BusinessException;

	/**
	 * ����Ϣ������������Ϊ���Ѵ���<br>
	 * The Message is Called No-Biz Message.including the PA,P2P and the Approve
	 * Message. Because the PA and P2P Message is stored in table
	 * pub_messageinfo,but the Approve Message is stored in table
	 * pub_workflownote.so the parameter msgType is necessary.
	 * 
	 * @param checkFlowPK ��ϢPK
	 * @param msgType ��Ϣ����.huangzg++ 2006-3-29
	 * @return
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public UFDateTime signMessageDeal(String nobizmsgPK, int msgType) throws BusinessException;

	/**
	 * ����Ϣ������������Ϊ��δ����
	 * 
	 * @param checkFlowPK ��ϢPK
	 * @param msgType ��Ϣ����.huangzg++ 2006-3-29
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void signMessageUndeal(String nobizmsgPK, int msgType) throws BusinessException;

	/**
	 * ͬ���������Ͷ���
	 * @param targetPhones �ֻ��Ŷ�ά����{{123,234},{345,456}}
	 * @param msges ��Ϣ����{asdf,retr}
	 * @return �Ƿ�ɹ����͵�ÿ���ֻ��ţ�����ÿ���ֻ��ŵĻ�ִ��HashMap�ṹ
	 * @throws BusinessException
	 * @since NC5.02
	 */
	public Object syncSendSMS(String[][] targetPhones, String[] msges) throws BusinessException;

	/**
	 * ���Ͷ���
	 * <li>�첽��ʽ��ʹ�õ�������
	 * @param mobileVO ��������VO
	 * @throws BusinessException
	 */
	public void sendMobileMessage(MobileMsg mobileVO) throws BusinessException;

	/**
	 * ���Ͷ���
	 * <li>�첽��ʽ��ʹ�õ�������
	 * @param userIds ���ղ���ԱPK����
	 * @param content ��������
	 * @throws BusinessException
	 */
	public void sendMobileMessage2(String[] userIds, String content) throws BusinessException;

	/**
	 * �õ�ĳ���˵Ļ���վ��Ϣ
	 * <li>����Ϣ��ʱ������������ϴη���ʱ��.
	 * <li>��õ�����Ϣ������getRecvedMsgs()������6����Ϣһ����
	 * <li>����ͬ���ǲ�ѯ���Ľ����Ӧ�÷��ڻ���վ-��receivedeleteflag=Y�ġ�
	 * 
	 * @param userPK ����ԱPK
	 * @param pk_group ����PK
	 * @param lastAccessTime �ϴβ�ѯʱ��
	 * @return
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public MessageDateTimeVO getRecycleMsgs(String userPK, String pk_group, UFDateTime lastAccessTime)
			throws BusinessException;

	/**
	 * ����Ϣ���Ϊ�߼�ɾ��̬����receivedeleteflag='Y'
	 * 
	 * @param hmDelete
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void signMessageDelete(HashMap hmDelete) throws BusinessException;

	/**
	 * ����Ϣ���Ϊ�ѷ��̬
	 * 
	 * @param hmSeal
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public UFDateTime signMessageSeal(HashMap hmSeal) throws BusinessException;

	/**
	 * ����Ϣ���Ϊδ���̬
	 * 
	 * @param hmSeal
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public UFDateTime signMessageUnSeal(HashMap hmSeal) throws BusinessException;

	/**
	 * ����ɾ����Ϣ
	 * 
	 * @param hmDelete ����-PK����
	 * @throws BusinessException
	 */
	public void deleteMessages(HashMap hmDelete) throws BusinessException;
	
	/**
	 * ����where��������ɾ����Ϣ
	 * 
	 * @param whereSql 
	 * @throws BusinessException
	 */
//	public void deleteCommonMsgByWhere(String whereSql) throws BusinessException;

	/**
	 * ����where��������ɾ����Ϣ
	 * 
	 * @param whereSql 
	 * @throws BusinessException
	 */
	public void deleteBizMsgByWhere(String whereSql) throws BusinessException;
	
	/**
	 * ��Ϣ״̬��ԭ-����ɾ����־�÷�
	 * @param hmRestore ����-PK����
	 * @throws BusinessException
	 * @deprecated since 6.0�������µ���Ϣ�����ṩ�ķ��������͡����º�ɾ����Ϣ�����������ewei
	 */
	public void restoreDeleteMsgs(HashMap hmRestore) throws BusinessException;

	/**
	 * ��һ����̨��HTML�ļ���ȡ.����String����ʽ����
	 * @param paMsgURL Ԥ����Ϣ�ļ�URL
	 * @return ��HTML�ļ���String��ʽ
	 * @throws BusinessException
	 */
	public String readHTML(String paMsgURL) throws BusinessException;

	/**
	 * ����ҵ����Ϣ���ɴ򿪽ڵ�UI
	 * <li>��Ҫ���õĲ������£�
	 * <p><blockquote><pre>
	 *   noteVO.setBillid("1001WW10000000000IIT"); //����ID
	 *   noteVO.setBillno("u2_biz11"); //���ݺ�
	 *   noteVO.setPk_billtype("88"); //��������
	 *   ��noteVO.setPk_billtype("[F]203040"); //ʹ��[F]ǰ׺Ҳ��ֱ��ָ����Ϣ���򿪵Ľڵ��
	 *   noteVO.setPk_businesstype(IPFConfigInfo.STATEBUSINESSTYPE); //ҵ�����ͣ������ҵ���������ÿ�
	 *   noteVO.setMessagenote("���Դ�UI��ҵ����Ϣ"); //��Ϣ����
	 *   noteVO.setPk_corp("1001"); //��˾
	 *   noteVO.setSenderman("0001WW100000000001A8"); //�����û�
	 *   noteVO.setCheckman("0001WW100000000001A7");  //�����û�
	 * </pre></blockquote><p>
	 * @param noteVOs �������ҵ����ϢVO����
	 * @throws BusinessException
	 */
//	public void insertBizMsgs(WorkflownoteVO[] noteVOs) throws BusinessException;

	/**
	 * ������ʽ����ʽҵ����Ϣ���ɴ򿪽ڵ�UI
	 * <li>��Ҫ���õĲ������£�
	 * <p><blockquote><pre>
	 *   infoVO.setBillid("1001WW10000000000IIT"); //����ID
	 *   infoVO.setBillno("u2_biz11"); //���ݺ�
	 *   infoVO.setPk_billtype("88"); //��������
	 *   infoVO.setPk_srcbilltype(srcBillType); //��Դ��������
	 *   infoVO.setPk_busitype(IPFConfigInfo.STATEBUSINESSTYPE); //ҵ������
	 *   infoVO.setTitle("���Դ�UI��ҵ����Ϣ"); //��Ϣ����
	 *   infoVO.setPk_corp("1001"); //��˾
	 *   infoVO.setSenderman("0001WW100000000001A8"); //�����û�
	 *   infoVO.setCheckman("0001WW100000000001A7");  //�����û�
	 * </pre></blockquote><p>
	 * @param infoVOs
	 * @param iPushOrPull ȡֵMessageTypes.MSG_TYPE_BUSIFLOW_PUSH��MessageTypes.MSG_TYPE_BUSIFLOW_PULL
	 * @throws BusinessException
	 */
//	public void insertPushOrPullMsgs(MessageinfoVO[] infoVOs, int iPushOrPull)throws BusinessException;

	/**
	 * ���ݴ�ӡģ�壬��̨���HTML������Ϣ
	 * 
	 * @param billID
	 * @param billType
	 * @param printTempletid
	 * @param checkman ������ID
	 * @return
	 */
	public String generateBillHtml(String billID, String billType, String printTempletid,
			String checkman) throws BusinessException;

	/**
	 * ʹ��ϵͳĬ���ʼ����������˺� ���͵����ʼ� 
	 * @param title �ʼ�����
	 * @param content �ʼ�����
	 * @param recEmails ���յ�ַ
	 * @param attachFiles ��������ַ
	 * @throws BusinessException
	 */
	public void sendEmail(String title, String content, String[] recEmails, String[] attachFiles)
			throws BusinessException;
	
	/**
	 * ��ѯĳ����Ա���յ�����Ϣ���������
	 * @param userPK ����ԱPK
	 * @param pk_group ����PK 
	 * @param hmFilters ɸѡ��
	 * @return
	 * @throws BusinessException 
	 */
	public MessageDateTimeVO getFilteredReceivedMsgs(String userPK, String pk_group, HashMap hmFilters)
			throws BusinessException;
	
	/**
	 * @param userPk
	 * @param pk_wf_task
	 * @param pk_wf_msg
	 * @throws BusinessException  
	 */
	public void dealFlowCheckMsgs(String userPk,String pk_wf_task, String pk_wf_msg, MessageVO msg,String checknote) throws BusinessException;
	
	public void dealFlowCheckMsgs(NCMessage ncmsg, String checkNote) throws BusinessException;
	
}
