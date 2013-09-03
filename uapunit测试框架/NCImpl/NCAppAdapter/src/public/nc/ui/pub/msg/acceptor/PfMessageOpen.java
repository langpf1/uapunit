package nc.ui.pub.msg.acceptor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.funcnode.ui.action.AbstractNCAction;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFConfig;
import nc.message.msgcenter.event.AbstractMessageTypeInfo;
import nc.message.msgcenter.event.IStateChangeProcessor;
import nc.message.reconstruction.INCMessageActionProcessor;
import nc.message.util.IDefaultMsgConst;
import nc.message.vo.NCMessage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.action.SeparatorAction;
import nc.vo.org.GroupVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.wfengine.core.WFEException;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.ui.client.PfUtilClient;
import uap.workflow.ui.taskhandling.FlowMsgCheckDlg;
import uap.workflow.ui.taskhandling.MessagePanelUtils;
import uap.workflow.ui.workitem.CommonFormDialog;
import uap.workflow.vo.WorkflownoteVO;
import javax.swing.Action;

/**
 * v6 消息中心 工作任务inbox 中 双击消息的处理类
 * @modifier yanke1 2011-10-19 顺应message的调整，从platform移至platformapp
 */
public class PfMessageOpen extends AbstractMessageTypeInfo {
	
	private IUAPQueryBS qry=null;

	private Action[] actions = null;
	private INCMessageActionProcessor processor = null;
	
	@Override
	public Action[] getBusiProcessAction(INCMessageActionProcessor processor) {
		if (actions == null) {

			Action separator = new SeparatorAction();

			Action batchApprove = new PfMCBatchApproveAction(processor);
			Action setHandled = new HandleAction("setHandled", NCLangRes.getInstance().getStrByID("pfworkflow1",
					"PfMessageOpen-000000")/* 设置为已处理 */, NCLangRes.getInstance().getStrByID("pfworkflow1",
					"PfMessageOpen-000001")/* 将勾选消息设置为已处理 */);
			Action setUnHandled = new HandleAction("setUnHandled", NCLangRes.getInstance().getStrByID("pfworkflow1",
					"PfMessageOpen-000002")/* 设置为未处理 */, NCLangRes.getInstance().getStrByID("pfworkflow1",
					"PfMessageOpen-000003")/* 将勾选消息设置为未处理 */);
			this.processor = processor;
			actions = new Action[] { batchApprove, separator, setHandled, setUnHandled };
		}
		return actions;
	}

	@Override
	public void processMsgOpen(NCMessage ncmsg, Container parent, IStateChangeProcessor processor) {
		MessageVO msgVO = PfMessageUtil.transferNCMessageToMessageVO(ncmsg);
		String msgSrcType = ncmsg.getMessage().getMsgsourcetype();
		if (!IDefaultMsgConst.WORKLIST.equals(msgSrcType)&&!IDefaultMsgConst.NOTICE.equals(msgSrcType))
			return;
		if(!InvocationInfoProxy.getInstance().getGroupId().equals(ncmsg.getMessage().getPk_group())) {
			GroupVO groupvo = null;
			try {
				groupvo = (GroupVO)getQueryService().retrieveByPK(GroupVO.class, ncmsg.getMessage().getPk_group());
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
			if(groupvo!=null)
				MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow1", "BillactionManageUI-000004")/*提示*/, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfMessageOpen-000004", null, new String[]{groupvo.getName()})/*请切换到{0}集团去处理该事务。*/);
			else
				MessageDialog.showHintDlg(parent, NCLangRes.getInstance().getStrByID("pfworkflow1", "BillactionManageUI-000004")/*提示*/, NCLangRes.getInstance().getStrByID("pfworkflow1", "PfMessageOpen-000005")/*集团为空*/);
			return;
		}
		
		
		
		String contentType = ncmsg.getMessage().getContenttype();
		if (!(MessagePanelUtils.isApprove(contentType)) && !(MessagePanelUtils.isMakeBill(contentType))
				&& !WorkflownoteVO.FLOWMSG_NEED_CHECK.equals(contentType)) {
			// XXX:消息只要双击后就标记为"已处理"状态，但修单、审批工作项、签收消息除外
			ncmsg.getMessage().setIshandled(UFBoolean.TRUE);
			processor.processStateChange(ncmsg);
		}
		
		if (WorkflownoteVO.FLOWMSG_NEED_CHECK.equals(ncmsg.getMessage().getContenttype())) {
			new FlowMsgCheckDlg(parent, ncmsg, processor).showModal();
			return;
		} else if (WorkflownoteVO.FLOWMSG_AUTO.equals(ncmsg.getMessage().getContenttype())) {
			if (!MessagePanelUtils.openMaintainBillUI(parent, msgVO, null)) 
				return;
		}

		
		String detail = ncmsg.getMessage().getDetail();
		String[] clauses = detail.split("@");
		if (clauses.length < 4){
			MessageDialog.showErrorDlg(null, "提示", "信息格式不正确");
			return; 
		}
		String pkTask = clauses[3];
		ITaskInstanceQry qry = NCLocator.getInstance().lookup(ITaskInstanceQry.class);
		TaskInstanceVO task = qry.getTaskInsVoByPk(pkTask);
		PfMessageUtil.setSateProcessor(ncmsg, processor);
		if (task.getOpenUIStyle().equals("BisunessUI")/*MessagePanelUtils.isMakebillOrBiz(ncmsg.getMessage().getContenttype())*/) {	// 修单任务
			MessagePanelUtils.openMaintainBillUI(parent, msgVO, null);
			//new CommonFormDialog(parent, pkTask, null).showModal();
		}else if(task.getOpenUIStyle().equals("ApproveUI")){																// 审批任务
			try {
				AggregatedValueObject billvo = null;//构造billVo
				IPFConfig pfConf = NCLocator.getInstance().lookup(IPFConfig.class);
				try {
					billvo = pfConf.queryBillDataVO(msgVO.getPk_billtype(), msgVO.getBillPK());
				} catch (BusinessException e1) {
					throw new WorkflowRuntimeException("不能够根据Pk_billtype:"+msgVO.getPk_billtype()+" BillPK:"+msgVO.getBillPK()+"找到单据");
				}
				PfUtilClient.runAction(parent, "APPROVE", task.getPk_bizobject(), 
						billvo, null, null, null, null);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else if(task.getOpenUIStyle().equals("DefinedUI")){																//流程定义的Form
			new CommonFormDialog(parent, pkTask, null).showModal();
		}else if(task.getOpenUIStyle()=="CustomURI"){																//自定制服务
				MessageDialog.showErrorDlg(parent, "提示", "暂不支持这种方式");
		}else{
			MessageDialog.showErrorDlg(parent, "提示", "未定义打开方式，按照默认方式打开");
			MessagePanelUtils.openMaintainBillUI(parent, msgVO, null);		
		}
		return;
	}
	
	private IUAPQueryBS getQueryService(){
		if(qry==null){
			qry=NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return qry;
	}

	
	@SuppressWarnings("serial")
	private class HandleAction extends AbstractNCAction{
		
		public HandleAction(String code,String name,String tooltip){
			super(code,name,tooltip);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(processor == null)
				return;
			List<NCMessage> msglist = processor.getSelectedMessage();
			List<NCMessage> hanledMsgList = new ArrayList<NCMessage>();
			if(msglist==null)
				return;
			if(getCode().equals("setHandled")){
				for (NCMessage message : msglist) {
					if(!(MessagePanelUtils.isApprove(message.getMessage().getContenttype())||
							WorkflownoteVO.FLOWMSG_NEED_CHECK.equals(message.getMessage().getContenttype()))) {
						message.getMessage().setIshandled(UFBoolean.TRUE);
						hanledMsgList.add(message);
					}
				}			
			}
			if(getCode().equals("setUnHandled")){
				for (NCMessage message : msglist) {
					if(!(MessagePanelUtils.isApprove(message.getMessage().getContenttype())||
							WorkflownoteVO.FLOWMSG_NEED_CHECK.equals(message.getMessage().getContenttype()))) {
						message.getMessage().setIshandled(UFBoolean.FALSE);
						hanledMsgList.add(message);
					}
				}
			}
			processor.processMessages(hanledMsgList);
		}
		
	}
	
}
