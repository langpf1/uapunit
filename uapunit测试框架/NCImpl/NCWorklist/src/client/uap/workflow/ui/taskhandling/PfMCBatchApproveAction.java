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
 * ��Ϣ������������Action
 * 
 * @author yanke1 2011-12-26
 * 
 */
public class PfMCBatchApproveAction extends AbstractNCAction {

	INCMessageActionProcessor processor = null;
	Container container = null;
	PfOperationNotifier notifier = null;

	public PfMCBatchApproveAction(INCMessageActionProcessor processor) {
		super("batchApprove", NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000000")/*��������*/, NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000001")/*��ѡ�е��ݽ�����������*/);

		this.processor = processor;

		// ��õ�ǰAction��UI����
		if (processor instanceof Container) {
			container = (Container) processor;
		} else {
			container = JOptionPane.getRootFrame();
		}

		notifier = new PfOperationNotifier(container);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// ��ù�ѡ��NCMessage
		List<NCMessage> list = processor.getSelectedMessage();

		if (list == null || list.size() == 0) {
			return;
		}

		// ������Ҫ�����msg����Ϣ
		List<String> billTypeList = new ArrayList<String>();
		List<String> billIdList = new ArrayList<String>();
		Map<String, NCMessage> billidMsgMap = new HashMap<String, NCMessage>();

		for (NCMessage msg : list) {

			nc.message.vo.MessageVO msgVo = msg.getMessage();

			// ����δ���������NCMessage
			// ȡ����������, ����ID
			if (!msgVo.getIshandled().booleanValue()
					&& msgVo.getContenttype().startsWith(WorkflownoteVO.WORKITEM_TYPE_APPROVE)) {

				String detail[] = msgVo.getDetail().split("@");

				String billid = detail[0];
				String billType = detail[1];
				
				// ��ʱ���ò���
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
																									 * ��ѡ����Ϣ��������������
																									 * ��
																									 */);
			return;
		}

		// ����������, ����WorkflownoteVO��
		WorkflownoteVO noteVO = new WorkflownoteVO();
		BatchApproveWorkitemAcceptDlg dlg = new BatchApproveWorkitemAcceptDlg(container, noteVO);
		if (!(dlg.showModal() == UIDialog.ID_OK)) {
			return;
		}

		try {
			
			String cuserid = InvocationInfoProxy.getInstance().getUserId();

			// ����������������������
			Object retObj = NCLocator
					.getInstance()
					.lookup(IplatFormEntry.class)
					.processBatch(IPFActionName.APPROVE + cuserid, noteVO, billTypeList.toArray(new String[0]),
							billIdList.toArray(new String[0]));

			// �������ΪPfProcessBatchRetObject�б�
			// ÿ��PfProcessBatchRetObject��Ӧһ�ֵ�������
			List<PfProcessBatchRetObject> appResult = (List<PfProcessBatchRetObject>) retObj;

			List<NCMessage> handledList = new ArrayList<NCMessage>();
			
			// �鿴����������Ƿ��д������
			StringBuffer errMsg = new StringBuffer();
			for (PfProcessBatchRetObject info : appResult) {
				String err = info.getExceptionMsg();

				if (!StringUtil.isEmptyWithTrim(err)) {
					errMsg.append(err);
					errMsg.append("\n");
				}
				
				// �����ɹ��ĵ��ݵ�AggVO
				Object[] retObjs = info.getRetObj();
				
				if (retObjs != null && retObjs.length > 0) {
					for (Object singleBillRet : retObjs) {
						if (singleBillRet instanceof AggregatedValueObject) {
							NCObject ncobj = NCObject.newInstance(singleBillRet);
							IFlowBizItf fbi = ncobj.getBizInterface(IFlowBizItf.class);
							
							// �������ɹ��ĵ��ݶ�Ӧ��msg��Ϊhandled
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
				notifier.fireOperationPerformed(0, true, NCLangRes.getInstance().getStrByID("platformapp", "PfMCBatchApproveAction-000002")/*���������ɹ���*/);
			}
			
			processor.processMessages(handledList);

		} catch (BusinessException e1) {
			notifier.fireOperationPerformed(0, false, e1.getMessage());
		}
		
	}
	
}