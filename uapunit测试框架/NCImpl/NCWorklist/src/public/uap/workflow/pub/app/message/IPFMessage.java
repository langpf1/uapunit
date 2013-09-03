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
 * 流程平台消息服务接口
 * 
 * @author 雷军 2005-8-17 9:48:04
 * @modifier guowl 2008-4-2 为实现公告消息的封存和修改增加新的接口
 */
public interface IPFMessage {

	/**
	 * 插入普通消息.不包括审批消息
	 * 
	 * @param cMsgVO 普通消息VO
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void insertCommonMsg(CommonMessageVO cMsgVO) throws BusinessException;

	/**
	 * 更新普通消息.
	 * 
	 * @param cMsgVO 普通消息VO
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void updateCommonMsg(MessageVO cMsgVO) throws BusinessException;

	/**
	 * 插入普通消息VO数组
	 * 
	 * @param cMsgVOs 普通消息VO数组
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void insertCommonMsgAry(CommonMessageVO[] cMsgVOs) throws BusinessException;
	
	/**
	 * 插入普通消息VO数组（新启事务）
	 * 
	 * @param cMsgVOs 普通消息VO数组
	 * @throws BusinessException
	 * @since 5.6
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void insertCommonMsgAry_RequiresNew(CommonMessageVO[] cMsgVOs) throws BusinessException;
	
	/**
	 * 插入普通消息-主要是用于发送消息及公告消息。
	 * 
	 * @param cMsgVO 欲发送的消息
	 * @param pkcorp 存储的公司PK
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void insertCommonMsg(CommonMessageVO cMsgVO, String pkcorp) throws BusinessException;

	/**
	 * 插入普通消息-主要是用于发送消息及公告消息。
	 * 
	 * @param cMsgVO 欲发送的消息
	 * @param pkcorp 存储的公司PK
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void insertCommonMsgs(CommonMessageVO[] cMsgVOs, String pkcorp) throws BusinessException;

	/**
	 * 将消息（或任务项）标记为“已处理”<br>
	 * The Message is Called No-Biz Message.including the PA,P2P and the Approve
	 * Message. Because the PA and P2P Message is stored in table
	 * pub_messageinfo,but the Approve Message is stored in table
	 * pub_workflownote.so the parameter msgType is necessary.
	 * 
	 * @param checkFlowPK 消息PK
	 * @param msgType 消息类型.huangzg++ 2006-3-29
	 * @return
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public UFDateTime signMessageDeal(String nobizmsgPK, int msgType) throws BusinessException;

	/**
	 * 将消息（或任务项）标记为“未处理”
	 * 
	 * @param checkFlowPK 消息PK
	 * @param msgType 消息类型.huangzg++ 2006-3-29
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void signMessageUndeal(String nobizmsgPK, int msgType) throws BusinessException;

	/**
	 * 同步批量发送短信
	 * @param targetPhones 手机号二维数组{{123,234},{345,456}}
	 * @param msges 消息数组{asdf,retr}
	 * @return 是否成功发送到每个手机号，即对每个手机号的回执。HashMap结构
	 * @throws BusinessException
	 * @since NC5.02
	 */
	public Object syncSendSMS(String[][] targetPhones, String[] msges) throws BusinessException;

	/**
	 * 发送短信
	 * <li>异步方式，使用调度引擎
	 * @param mobileVO 短信数据VO
	 * @throws BusinessException
	 */
	public void sendMobileMessage(MobileMsg mobileVO) throws BusinessException;

	/**
	 * 发送短信
	 * <li>异步方式，使用调度引擎
	 * @param userIds 接收操作员PK数组
	 * @param content 短信内容
	 * @throws BusinessException
	 */
	public void sendMobileMessage2(String[] userIds, String content) throws BusinessException;

	/**
	 * 得到某个人的回收站消息
	 * <li>即消息的时间戳必须晚于上次访问时间.
	 * <li>其得到的消息类型与getRecvedMsgs()方法的6种消息一样。
	 * <li>所不同的是查询出的结果是应该放在回收站-即receivedeleteflag=Y的。
	 * 
	 * @param userPK 操作员PK
	 * @param pk_group 集团PK
	 * @param lastAccessTime 上次查询时间
	 * @return
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public MessageDateTimeVO getRecycleMsgs(String userPK, String pk_group, UFDateTime lastAccessTime)
			throws BusinessException;

	/**
	 * 将消息标记为逻辑删除态，即receivedeleteflag='Y'
	 * 
	 * @param hmDelete
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void signMessageDelete(HashMap hmDelete) throws BusinessException;

	/**
	 * 将消息标记为已封存态
	 * 
	 * @param hmSeal
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public UFDateTime signMessageSeal(HashMap hmSeal) throws BusinessException;

	/**
	 * 将消息标记为未封存态
	 * 
	 * @param hmSeal
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public UFDateTime signMessageUnSeal(HashMap hmSeal) throws BusinessException;

	/**
	 * 物理删除消息
	 * 
	 * @param hmDelete 类型-PK数组
	 * @throws BusinessException
	 */
	public void deleteMessages(HashMap hmDelete) throws BusinessException;
	
	/**
	 * 根据where条件物理删除消息
	 * 
	 * @param whereSql 
	 * @throws BusinessException
	 */
//	public void deleteCommonMsgByWhere(String whereSql) throws BusinessException;

	/**
	 * 根据where条件物理删除消息
	 * 
	 * @param whereSql 
	 * @throws BusinessException
	 */
	public void deleteBizMsgByWhere(String whereSql) throws BusinessException;
	
	/**
	 * 消息状态还原-即把删除标志置非
	 * @param hmRestore 类型-PK数组
	 * @throws BusinessException
	 * @deprecated since 6.0，请用新的消息中心提供的服务来发送、更新和删除消息，具体可自行ewei
	 */
	public void restoreDeleteMsgs(HashMap hmRestore) throws BusinessException;

	/**
	 * 把一个后台的HTML文件都取.并以String的形式返回
	 * @param paMsgURL 预警消息文件URL
	 * @return 该HTML文件的String格式
	 * @throws BusinessException
	 */
	public String readHTML(String paMsgURL) throws BusinessException;

	/**
	 * 插入业务消息，可打开节点UI
	 * <li>需要设置的参数如下：
	 * <p><blockquote><pre>
	 *   noteVO.setBillid("1001WW10000000000IIT"); //单据ID
	 *   noteVO.setBillno("u2_biz11"); //单据号
	 *   noteVO.setPk_billtype("88"); //单据类型
	 *   或noteVO.setPk_billtype("[F]203040"); //使用[F]前缀也可直接指定消息欲打开的节点号
	 *   noteVO.setPk_businesstype(IPFConfigInfo.STATEBUSINESSTYPE); //业务类型，如果无业务类型则置空
	 *   noteVO.setMessagenote("测试打开UI的业务消息"); //消息内容
	 *   noteVO.setPk_corp("1001"); //公司
	 *   noteVO.setSenderman("0001WW100000000001A8"); //发送用户
	 *   noteVO.setCheckman("0001WW100000000001A7");  //接收用户
	 * </pre></blockquote><p>
	 * @param noteVOs 待插入的业务消息VO数组
	 * @throws BusinessException
	 */
//	public void insertBizMsgs(WorkflownoteVO[] noteVOs) throws BusinessException;

	/**
	 * 插入推式或拉式业务消息，可打开节点UI
	 * <li>需要设置的参数如下：
	 * <p><blockquote><pre>
	 *   infoVO.setBillid("1001WW10000000000IIT"); //单据ID
	 *   infoVO.setBillno("u2_biz11"); //单据号
	 *   infoVO.setPk_billtype("88"); //单据类型
	 *   infoVO.setPk_srcbilltype(srcBillType); //来源单据类型
	 *   infoVO.setPk_busitype(IPFConfigInfo.STATEBUSINESSTYPE); //业务类型
	 *   infoVO.setTitle("测试打开UI的业务消息"); //消息内容
	 *   infoVO.setPk_corp("1001"); //公司
	 *   infoVO.setSenderman("0001WW100000000001A8"); //发送用户
	 *   infoVO.setCheckman("0001WW100000000001A7");  //接收用户
	 * </pre></blockquote><p>
	 * @param infoVOs
	 * @param iPushOrPull 取值MessageTypes.MSG_TYPE_BUSIFLOW_PUSH或MessageTypes.MSG_TYPE_BUSIFLOW_PULL
	 * @throws BusinessException
	 */
//	public void insertPushOrPullMsgs(MessageinfoVO[] infoVOs, int iPushOrPull)throws BusinessException;

	/**
	 * 根据打印模板，后台输出HTML单据信息
	 * 
	 * @param billID
	 * @param billType
	 * @param printTempletid
	 * @param checkman 审批人ID
	 * @return
	 */
	public String generateBillHtml(String billID, String billType, String printTempletid,
			String checkman) throws BusinessException;

	/**
	 * 使用系统默认邮件服务器和账号 发送电子邮件 
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param recEmails 接收地址
	 * @param attachFiles 附件名地址
	 * @throws BusinessException
	 */
	public void sendEmail(String title, String content, String[] recEmails, String[] attachFiles)
			throws BusinessException;
	
	/**
	 * 查询某操作员接收到的消息（或任务项）
	 * @param userPK 操作员PK
	 * @param pk_group 集团PK 
	 * @param hmFilters 筛选器
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
