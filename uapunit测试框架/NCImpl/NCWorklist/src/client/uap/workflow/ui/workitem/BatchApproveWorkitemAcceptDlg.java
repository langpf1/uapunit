package uap.workflow.ui.workitem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.vo.WorkflownoteVO;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.vo.pf.term.ApproveTermConfig;
import nc.vo.pf.term.IApproveTerm;
import nc.vo.wfengine.core.util.CoreUtilities;

/**
 * 批审时审批流的工作项处理对话框
 * 
 * @author guowl 2010-5
 * @since 6.0
 * 
 */
public class BatchApproveWorkitemAcceptDlg extends UIDialog implements
		ActionListener {

	private UIButton ivjonCancel = null;

	private UIButton ivjonOk = null;

	private UIPanel southPanel = null; // 下方的按钮panel和金额panel

	private UIPanel centerPanel = null;// 中部 选择用户以及 批语的 panel

	private UIPanel ivjpnlBtn = null;

	private UIPanel textCheckPanel = null;// texearea panel

	private UIPanel ivjpnlRadioPanel = null;

	private UIPanel ivjpnlState = null;

	private UIRefPane ivjrefRemark = null;

	private UIScrollPane ivjscpPanel = null;

	private UITextArea ivjtaCheckNote = null;

	private ButtonGroup m_bgGroup = new ButtonGroup();

	private UIRadioButton ivjrbNoPass = null;

	private UIRadioButton ivjrbPass = null;

	private UIRadioButton m_rbRejectFirst = null;

	private WorkflownoteVO m_worknoteVO = null;

	private UILabel ivjlblShowStatus = null;
	
	/**
	 * 构造
	 * 
	 * @param parent
	 * @param noteVO
	 *            审批工作项VO
	 */
	public BatchApproveWorkitemAcceptDlg(Container parent, WorkflownoteVO noteVO) {
		super(parent);
		this.m_worknoteVO = noteVO;
		// 启动一个线程,初始化随机数
		CoreUtilities.dummyThread4Performance();
		// 初始化界面
		initialize();
	}

	private void initialize() {
		setResizable(true);
		setName("PfWorkFlowCheck");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(800, 540);
		setPreferredSize(new Dimension(800,540));
		setTitle(NCLangRes.getInstance().getStrByID("102220",
				"UPP102220-000163")/*
									 * @res "审批处理情况"
									 */);

		m_bgGroup.add(getrbNoPass());
		m_bgGroup.add(getrbPass());
		m_bgGroup.add(getRbRejectFirst());

		setLayout(new BorderLayout());
		add(getpnlRadio(), BorderLayout.NORTH);
		add(getCenterPanel(), BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);

		this.addEventListener();
		// 默认选中"批准"
		getrbPass().doClick();

		// lj+
		getpnlRadio().setBorder(
				new TitledBorder(BorderFactory.createEtchedBorder(),
						NCLangRes.getInstance().getStrByID("102220",
								"UPP102220-000194")/*
													 * @res "审核意见"
													 */, TitledBorder.CENTER,
						TitledBorder.TOP, new Font("Dialog", 0, 12)));

	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == getonOk()) {
			onOk();
		} else if (obj == getonCancel()) {
			this.closeCancel();
		} else if (obj == getrbPass()) {
			// 通过 radio
			gettaCheckNote().setText(getrbPass().getText());
		} else if (obj == getrbNoPass()) {
			// 不通过 radio
			gettaCheckNote().setText(getrbNoPass().getText());
		} else if (obj == getRbRejectFirst()) {
			// 驳回 radio
			gettaCheckNote().setText(getRbRejectFirst().getText());
		} 
	}

	/**
	 * 事件侦听器 注册
	 */
	public void addEventListener() {
		getrbPass().addActionListener(this);
		getrbNoPass().addActionListener(this);
		getRbRejectFirst().addActionListener(this);
		getonOk().addActionListener(this);
		getonCancel().addActionListener(this);
		gettaCheckNote().addKeyListener(this);
	}

	private UILabel getlblShowStatus() {
		if (ivjlblShowStatus  == null) {
			ivjlblShowStatus = new UILabel();
			ivjlblShowStatus.setName("lblShowStatus");
			ivjlblShowStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
			ivjlblShowStatus.setText(NCLangRes.getInstance().getStrByID(
					"102220", "UPP102220-000154")/*
												 * @res "批示内容："
												 */);
			ivjlblShowStatus.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return ivjlblShowStatus;
	}

	private UIButton getonCancel() {
		if (ivjonCancel == null) {
			ivjonCancel = new UIButton();
			ivjonCancel.setName("onCancel");
			ivjonCancel.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000008")/*
									 * @res "取消"
									 */);
		}
		return ivjonCancel;
	}

	private UIButton getonOk() {
		if (ivjonOk == null) {
			ivjonOk = new UIButton();
			ivjonOk.setName("onOk");
			ivjonOk.setText(NCLangRes.getInstance().getStrByID("common",
					"UC001-0000044")/*
									 * @res "确定"
									 */);
			ivjonOk.setEnabled(true);
		}
		return ivjonOk;
	}

	private UIPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new UIPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(getTextCheckPanel(), BorderLayout.CENTER);
		}
		return centerPanel;
	}

	private UIPanel getSouthPanel() {
		if (southPanel == null) {
			southPanel = new UIPanel();
			southPanel.setLayout(new BorderLayout());

			southPanel.add(getpnlBtn(), BorderLayout.SOUTH);
		}

		return southPanel;
	}

	private UIPanel getpnlBtn() {
		if (ivjpnlBtn == null) {
			ivjpnlBtn = new UIPanel();
			ivjpnlBtn.setName("pnlBtn");
			ivjpnlBtn.setLayout(new FlowLayout());
			getpnlBtn().add(getonOk(), getonOk().getName());
			getpnlBtn().add(getonCancel(), getonCancel().getName());
		}
		return ivjpnlBtn;
	}

	private UIPanel getTextCheckPanel() {
		if (textCheckPanel == null) {
			textCheckPanel = new UIPanel();
			textCheckPanel.setName("pnlCheck");
			textCheckPanel.setLayout(new BorderLayout());
			textCheckPanel.add(getlblShowStatus(), "North");
			textCheckPanel.add(getpnlState(), "Center");
		}
		return textCheckPanel;
	}

	private UIPanel getpnlRadio() {
		if (ivjpnlRadioPanel == null) {
			ivjpnlRadioPanel = new UIPanel();
			ivjpnlRadioPanel.setName("pnlRadio");
			ivjpnlRadioPanel.setLayout(new FlowLayout());
			ivjpnlRadioPanel.add(getrbPass());
			
			if (!ApproveTermConfig.getInstance().isHidden(IApproveTerm.NO_PASS)) {
				ivjpnlRadioPanel.add(getrbNoPass());
			}
			ivjpnlRadioPanel.add(getRbRejectFirst());
		}
		return ivjpnlRadioPanel;
	}


	private UIPanel getpnlState() {
		if (ivjpnlState == null) {
			ivjpnlState = new UIPanel();
			ivjpnlState.setName("pnlState");
			ivjpnlState.setLayout(new BorderLayout());
			ivjpnlState.add(getscpPanel(), "Center");

			ivjpnlState.setPreferredSize(new Dimension(10, 150));
		}
		return ivjpnlState;
	}

	private UIRadioButton getrbNoPass() {
		if (ivjrbNoPass == null) {
			ivjrbNoPass = new UIRadioButton();
			ivjrbNoPass.setName("rbNoPass");
			ivjrbNoPass.setSelected(true);
			
			ivjrbNoPass.setText(ApproveTermConfig.getInstance().getText(IApproveTerm.NO_PASS));
			
//			ivjrbNoPass.setText(NCLangRes.getInstance().getStrByID("102220",
//					"UPP102220-000160")/*
//										 * @res "不批准"
//										 */);
		}
		return ivjrbNoPass;
	}

	private UIRadioButton getrbPass() {
		if (ivjrbPass == null) {
			ivjrbPass = new UIRadioButton();
			ivjrbPass.setName("rbPass");
			
			ivjrbPass.setText(ApproveTermConfig.getInstance().getText(IApproveTerm.PASS));
//			ivjrbPass.setText(NCLangRes.getInstance().getStrByID("102220",
//					"UPP102220-000161")/*
//										 * @res "批准"
//										 */);
		}
		return ivjrbPass;
	}

	private UIRadioButton getRbRejectFirst() {
		if (m_rbRejectFirst == null) {
			m_rbRejectFirst = new UIRadioButton();
			m_rbRejectFirst.setName("m_rbRejectFirst");
			
			m_rbRejectFirst.setText(ApproveTermConfig.getInstance().getText(IApproveTerm.REJECT_FIRST));
//			m_rbRejectFirst.setText(NCLangRes.getInstance().getStrByID(
//					"pfworkflow", "UPPpfworkflow-000146")/*
//														 * @res "驳回到制单人"
//														 */);
		}
		return m_rbRejectFirst;
	}

	private UIRefPane getrefRemark() {
		if (ivjrefRemark == null) {
			ivjrefRemark = new UIRefPane();
			ivjrefRemark.setName("refRemark");
			ivjrefRemark.setVisible(false);
			ivjrefRemark.setRefNodeName(NCLangRes.getInstance().getStrByID(
					"102220", "UPP102220-000209")/*
					 * @res "常用摘要"
					 */);;
		}
		return ivjrefRemark;
	}

	private UIScrollPane getscpPanel() {
		if (ivjscpPanel == null) {
			ivjscpPanel = new UIScrollPane();
			ivjscpPanel.setName("scpPanel");
			getscpPanel().setViewportView(gettaCheckNote());
		}
		return ivjscpPanel;
	}

	private UITextArea gettaCheckNote() {
		if (ivjtaCheckNote == null) {
			ivjtaCheckNote = new UITextArea();
			ivjtaCheckNote.setName("taCheckNote");
			ivjtaCheckNote.setLineWrap(true);
			ivjtaCheckNote.setMaxLength(1024);
			ivjtaCheckNote.setBounds(-1, 23, 377, 157);
		}
		return ivjtaCheckNote;
	}

	/**
	 * @return nc.vo.pub.pf.WorkflownoteVO
	 */
	public WorkflownoteVO getWorkFlow() {
		return m_worknoteVO;
	}

	protected void hotKeyPressed(KeyStroke hotKey, KeyEvent e) {
		if (hotKey.getKeyCode() == KeyEvent.VK_F2) {
			// getrefRemark().getRef().showModal();
			getrefRemark().onButtonClicked();
			int pos = gettaCheckNote().getCaretPosition();
			gettaCheckNote().insert("\n" + getrefRemark().getRefName(), pos);
			// gettaCheckNote().append("\n" + getrefRemark().getRefName());
			e.consume();
		}
	}

	/**
	 * "确定"按钮的事件响应,将为工作项VO对象填充信息
	 */
	public void onOk() {
		m_worknoteVO.setChecknote(gettaCheckNote().getText());
		if (getrbPass().isSelected()) {
			// 批准
			m_worknoteVO.setApproveresult("Y");
		} else if (getrbNoPass().isSelected()) {
			// 不批准
			m_worknoteVO.setApproveresult("N");
		} else if (getRbRejectFirst().isSelected()) {
			// 驳回到制单人
			if(m_worknoteVO.getTaskInstanceVO() != null) {
				m_worknoteVO.getTaskInstanceVO().setCreate_type(TaskInstanceCreateType.Reject.getIntValue());
				//xry TODO:m_worknoteVO.getTaskInstanceVO().getTask().setBackToFirstActivity(true);
			}
			m_worknoteVO.setApproveresult("R");
		} 
		this.closeOK();
	}
}