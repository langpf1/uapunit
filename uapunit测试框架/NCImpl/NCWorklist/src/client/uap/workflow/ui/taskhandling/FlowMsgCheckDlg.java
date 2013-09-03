package uap.workflow.ui.taskhandling;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.WindowConstants;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pf.PfMessageUtil;
import nc.desktop.ui.ProductNavigatePanel;
import nc.itf.uap.pf.IPFMessage;
import nc.message.itf.IMessageService;
import nc.message.msgcenter.event.IStateChangeProcessor;
import nc.message.vo.NCMessage;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.msg.MessagePanelUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.MessageVO;

/**
 * @author chengsc
 * 
 */
public class FlowMsgCheckDlg extends UIDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String pk_wf_task;
	UIPanel ivjUIDialogContentPane;
	UIPanel btnPanel;
	UIButton btnOk;
	UITextArea msgCheckNote;
	UIButton btnCancel;
	UIButton lookupBill;
	String pk_wf_msg;
	NCMessage ncmsg;
	MessageVO msgvo;
	IStateChangeProcessor processor;

	
	public FlowMsgCheckDlg(Container container, NCMessage ncmsg, IStateChangeProcessor processor) {
		super(container);
		this.ncmsg = ncmsg;
		this.processor = processor;
		this.msgvo = PfMessageUtil.transferNCMessageToMessageVO(ncmsg);
		this.pk_wf_task = ncmsg.getMessage().getPk_detail();
		
		String[] values = ncmsg.getMessage().getDetail().split("@");
		this.msgvo.setPk_billtype(values[1]);
		
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// Rectangle screenRect = OperatingSystem.getScreenBounds();
		// setSize(720, 400);
		setTitle(NCLangRes.getInstance().getStrByID("pfworkflow",
				"AssignDlgTitle"));// 消息签收
		setSize(500, 400);
		setContentPane(getUIDialogContentPane());
	}

	private UITextArea getTextCheckNote() {
		if (msgCheckNote == null) {
			msgCheckNote = new UITextArea();

			msgCheckNote.setText(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "FlowMsgCheckDlg-000000")/* 已确认 */);
		}
		return msgCheckNote;
	}

	private UIButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new UIButton(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "FlowMsgCheckDlg-000001")/* 签收 */);
			btnOk.addActionListener(this);
		}
		return btnOk;
	}

	private UIButton getBtnBillLookup() {
		if (lookupBill == null) {
			lookupBill = new UIButton(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "FlowMsgCheckDlg-000002")/* 联查单据 */);
			lookupBill.addActionListener(this);
		}
		return lookupBill;
	}

	private UIButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new UIButton(NCLangRes.getInstance().getStrByID(
					"pfworkflow", "cancelBtnName")/* 取消 */);
			btnCancel.addActionListener(this);
		}
		return btnCancel;
	}

	private UIPanel getBtnPanel() {
		if (btnPanel == null) {
			btnPanel = new UIPanel();
			btnPanel.add(getBtnBillLookup());
			btnPanel.add(getBtnOk());
			btnPanel.add(getBtnCancel());
		}

		return btnPanel;
	}

	private JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {
			ivjUIDialogContentPane = new UIPanel();
			ivjUIDialogContentPane
					.setName(NCLangRes.getInstance().getStrByID("pfworkflow",
							"FlowMsgCheckDlg-000003")/* UIDialogContentPane */);
			ivjUIDialogContentPane.setLayout(new BorderLayout());
			UIPanel msgContentPanel = new UIPanel();
			msgContentPanel.setLayout(new BorderLayout());
			UIPanel title = new UIPanel();
			title.setLayout(new BorderLayout());
			title.add(
					new UILabel(NCLangRes.getInstance().getStrByID(
							"pfworkflow", "FlowMsgCheckDlg-000004")/* 消息内容: */),
					BorderLayout.WEST);
			msgContentPanel.add(title, BorderLayout.NORTH);
			UITextArea msgcontent = new UITextArea();
			msgcontent.setText(msgvo == null ? "" : msgvo.getMessageNote());
			msgcontent.setEditable(false);
			msgContentPanel.add(msgcontent, BorderLayout.CENTER);

			UIPanel northPanel = new UIPanel();
			northPanel.setLayout(new BorderLayout());
			UIPanel title1 = new UIPanel();
			title1.setLayout(new BorderLayout());

			title1.add(
					new UILabel(NCLangRes.getInstance().getStrByID(
							"pfworkflow", "FlowMsgCheckDlg-000005")/* 流程消息签收意见: */),
					BorderLayout.WEST);
			northPanel.add(title1, BorderLayout.NORTH);
			northPanel.add(getTextCheckNote(), BorderLayout.CENTER);

			ivjUIDialogContentPane.add(northPanel, BorderLayout.CENTER);//
			ivjUIDialogContentPane.add(getBtnPanel(), BorderLayout.SOUTH);
			ivjUIDialogContentPane.add(msgContentPanel, BorderLayout.NORTH);//
		}
		return ivjUIDialogContentPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnBillLookup()) {
			// 联查单据
			MessagePanelUtils.openMaintainBillUI(getNavigateParent(), msgvo,
					null);
		} else if (e.getSource() == getBtnCancel()) {
			// 取消
			this.closeCancel();
		} else if (e.getSource() == getBtnOk()) {
			// 签收
			checkMessage();
			this.closeOK();
		}
	}

	/**
	 * 解决焦点问题
	 * */
	private ProductNavigatePanel getNavigateParent() {
		Container c = this.getParent();
		while (c != null) {
			if (c instanceof ProductNavigatePanel) {
				return (ProductNavigatePanel) this.getParent();
			} else {
				c = c.getParent();
			}
		}
		return null;

	}

	private void checkMessage() {
		IPFMessage msg = NCLocator.getInstance().lookup(IPFMessage.class);
		try {
			msg.dealFlowCheckMsgs(ncmsg, getTextCheckNote().getText());
			ncmsg.getMessage().setIshandled(UFBoolean.TRUE);
			ncmsg.getMessage().setIsread(UFBoolean.TRUE);
			if (processor != null) {
				processor.processStateChange(ncmsg);
				NCLocator.getInstance().lookup(IMessageService.class)
						.udpateMessage(new NCMessage[] { ncmsg });
			}
		} catch (BusinessException e1) {
			Logger.error(e1.getMessage(), e1);
		}

	}

}
