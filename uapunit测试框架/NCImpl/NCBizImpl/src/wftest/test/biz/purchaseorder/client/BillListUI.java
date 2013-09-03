package test.biz.purchaseorder.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import test.biz.purchaseorder.itf.IServicesInterface;
import test.biz.purchaseorder.vo.AggTestPO;
import test.biz.purchaseorder.vo.TestPOMaster;
import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.vo.pub.BusinessException;

public class BillListUI extends UIDialog {

	private static final long serialVersionUID = 8919265548294811631L;

	private String billID;
	private Boolean isOK;
	
	private UIList lstBill;
	private UIButton btnOK;
	private UIButton btnCancel;
	
	public BillListUI(Container parent){
		super(parent);
		initializeUI();
		installListener();
		initializeData();
	}
	
	private void initializeUI(){
		this.setSize(new Dimension(400,600));
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		
		UIPanel pnlFooter = new UIPanel();
		pnlFooter.setPreferredSize(new Dimension(52, 26));
		pnlFooter.setLayout(new BorderLayout());
		add(pnlFooter, BorderLayout.SOUTH);
		
		btnOK = new UIButton("OK");
		btnOK.setPreferredSize(new Dimension(160,24));
		pnlFooter.add(btnOK, BorderLayout.WEST);
		
		btnCancel = new UIButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(160,24));
		pnlFooter.add(btnCancel, BorderLayout.EAST);
		
		lstBill = new UIList();
		lstBill.setPreferredSize(new Dimension(100,100));
		add(lstBill,BorderLayout.CENTER);
	}

	private void initializeData(){
		try {
			AggTestPO[] pos = NCLocator.getInstance().lookup(IServicesInterface.class).getPO();
			List<String> items = new ArrayList<String>();
			TestPOMaster master = null;
			for(AggTestPO bill : pos){
				master = (TestPOMaster)bill.getParentVO();
				items.add(master.getId() + "|" + master.getSupplier());
			}
			lstBill.setListData(items.toArray(new String[0]));
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
	}
	
	private void performedOK(ActionEvent e){
		billID = lstBill.getSelectedValue().toString();
		billID = billID.split("\\|")[0];
		isOK = true;
		this.closeOK();
	}
	private void performedCancel(ActionEvent e){
		billID = null;
		isOK = false;
		this.closeCancel();
	}

	private void installListener(){
		btnOK.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				performedOK(e);
			}
		});
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				performedCancel(e);
			}
		});
	}
	
	public String getBillID(){
		if (isOK)
			return billID;
		return null;
	}
}
