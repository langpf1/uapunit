package uap.workflow.ui.taskhandling;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.action.IplatFormEntry;
import uap.workflow.app.vo.PfProcessBatchRetObject;
import uap.workflow.ui.desktop.PfOperationNotifier;
import uap.workflow.ui.workitem.BatchApproveWorkitemAcceptDlg;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.funcnode.ui.action.AbstractNCAction;
import nc.itf.uap.pf.metadata.IFlowBizItf;
import nc.md.data.access.NCObject;
import nc.message.reconstruction.INCMessageActionProcessor;
import nc.message.vo.NCMessage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIDialog;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;

/**
 * 消息中心批量审批Action
 * 
 * @author yanke1 2011-12-26
 * 
 */
public class PfMCBatchApproveAction extends AbstractNCAction {

	INCMessageActionProcessor processor = null;
	Container container = null;
	PfOperationNotifier notifier = null;

	public PfMCBatchApproveAction(INCMessageActionProcessor processor) {
		super("batchApprove", NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000000")/*批量审批*/, NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000001")/*对选中单据进行批量审批*/);

		this.processor = processor;

		// 获得当前Action的UI容器
		if (processor instanceof Container) {
			container = (Container) processor;
		} else {
			container = JOptionPane.getRootFrame();
		}

		notifier = new PfOperationNotifier(container);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// 获得勾选的NCMessage
		List<NCMessage> list = processor.getSelectedMessage();

		if (list == null || list.size() == 0) {
			return;
		}

		// 保存需要处理的msg的信息
		List<String> billTypeList = new ArrayList<String>();
		List<String> billIdList = new ArrayList<String>();
		Map<String, NCMessage> billidMsgMap = new HashMap<String, NCMessage>();

		for (NCMessage msg : list) {

			nc.message.vo.MessageVO msgVo = msg.getMessage();

			// 对于未处理的审批NCMessage
			// 取到单据类型, 单据ID
			if (!msgVo.getIshandled().booleanValue()
					&& msgVo.getContenttype().startsWith(WorkflownoteVO.WORKITEM_TYPE_APPROVE)) {

				String detail[] = msgVo.getDetail().split("@");

				String billid = detail[0];
				String billType = detail[1];
				
				// 暂时还用不到
				// String billno = detail[2];
				// String pk_checkflow = msgVo.getPk_detail();

				billTypeList.add(billType);
				billIdList.add(billid);
				
				billidMsgMap.put(billid, msg);
			}
		}

		if (billTypeList.size() == 0) {
			notifier.fireOperationPerformed(0, false,
					NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000003")/*
																									 * 所选中消息不包含审批任务
																									 * ！
																									 */);
			return;
		}

		// 获得审批意见, 放在WorkflownoteVO中
		WorkflownoteVO noteVO = new WorkflownoteVO();
		BatchApproveWorkitemAcceptDlg dlg = new BatchApproveWorkitemAcceptDlg(container, noteVO);
		if (!(dlg.showModal() == UIDialog.ID_OK)) {
			return;
		}

		try {
			
			String cuserid = InvocationInfoProxy.getInstance().getUserId();

			// 调用组件服务进行批量审批
			Object retObj = NCLocator
					.getInstance()
					.lookup(IplatFormEntry.class)
					.processBatch(IPFActionName.APPROVE + cuserid, noteVO, billTypeList.toArray(new String[0]),
							billIdList.toArray(new String[0]));

			// 审批结果为PfProcessBatchRetObject列表
			// 每个PfProcessBatchRetObject对应一种单据类型
			List<PfProcessBatchRetObject> appResult = (List<PfProcessBatchRetObject>) retObj;

			List<NCMessage> handledList = new ArrayList<NCMessage>();
			
			// 查看批审过程中是否有错误出现
			StringBuffer errMsg = new StringBuffer();
			for (PfProcessBatchRetObject info : appResult) {
				String err = info.getExceptionMsg();

				if (!StringUtil.isEmptyWithTrim(err)) {
					errMsg.append(err);
					errMsg.append("\n");
				}
				
				// 审批成功的单据的AggVO
				Object[] retObjs = info.getRetObj();
				
				if (retObjs != null && retObjs.length > 0) {
					for (Object singleBillRet : retObjs) {
						if (singleBillRet instanceof AggregatedValueObject) {
							NCObject ncobj = NCObject.newInstance(singleBillRet);
							IFlowBizItf fbi = ncobj.getBizInterface(IFlowBizItf.class);
							
							// 将审批成功的单据对应的msg设为handled
							NCMessage toSetHandled = billidMsgMap.get(fbi.getBillId());
							toSetHandled.getMessage().setIshandled(new UFBoolean(true));
							handledList.add(toSetHandled);
						}
					}
				}
			}

			if (errMsg.length() > 0) {
				notifier.fireOperationPerformed(0, false, errMsg.toString());
			} else {
				notifier.fireOperationPerformed(0, true, NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000002")/*批量审批成功！*/);
			}
			
			processor.processMessages(handledList);

		} catch (BusinessException e1) {
			notifier.fireOperationPerformed(0, false, e1.getMessage());
		}
		
	}
	
}