package test.biz.purchaseorder.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import nc.bs.framework.common.NCLocator;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.desktop.PFToftPanel;
import nc.ui.pub.workflownote.FlowStateDlg;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFDouble;
import test.biz.purchaseorder.itf.IServicesInterface;
import test.biz.purchaseorder.vo.AggTestPO;
import test.biz.purchaseorder.vo.TestPODetail;
import test.biz.purchaseorder.vo.TestPOMaster;
import uap.workflow.ui.client.WorkflowClientUtil;

public class PurchaseOrderUI extends PFToftPanel {

	private static final long serialVersionUID = -3370267260255152751L;
	private static int COMPONENT_HEIGHT = 24;
	private static int COMPONENT_WIDTH = 200;

	private UIPanel pnlHeader;
	private UITextField textSupplier;
	private UITextField textSum;
	private UITextField textID;
	private UIPanel pnlBody;
	private UITablePane tablePane;
	private AggTestPO po;
	
	private ButtonObject[] buttons;
	
	private PrayBillTableModel tableModel = null;
	
	public PurchaseOrderUI() {
		this(null);
	}
	public PurchaseOrderUI(Container parent) {
		this.setTitleText("Form Editor");
		this.setPreferredSize(new Dimension(640,480));
		initializeUI();
		po = queryData(null);
		initializeData(po);
	} 

	@Override
	public String getTitle() {
		return "Pray UI";
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setTitleText(String title) {
		super.setTitleText(title);
	}
	
	@Override
	protected void setButtons(ButtonObject[] btns) {
		buttons = new ButtonObject[]{
			new ButtonObject("New", "New", "New"),
			new ButtonObject("Save","Save","Save"),
			new ButtonObject("Submit","Submit","Submit"),
			new ButtonObject("Approve","Approve","Approve"),
			new ButtonObject("Reject","Reject","Reject"),
			new ButtonObject("ViewApprove","ViewApprove","ViewApprove"),
			new ButtonObject("BillList", "BillList", "BillList")
		};
		super.setButtons(buttons);
	}
	@Override
	public void onButtonClicked(ButtonObject bo) {
		if (bo.getCode().equalsIgnoreCase("New")){
			po = queryData(null);
			initializeData(po);
			tablePane.updateUI();
		}else if (bo.getCode().equalsIgnoreCase("Save")){
			try {
				collectData();
				po = NCLocator.getInstance().lookup(IServicesInterface.class).SavePO(po);
				initializeData(po);
				tablePane.updateUI();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else if (bo.getCode().equalsIgnoreCase("Submit")){
				try {
					WorkflowClientUtil.start(this, po, null);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
		}else if (bo.getCode().equalsIgnoreCase("Approve")){
			try {
				WorkflowClientUtil.forward(this, po, null);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else if (bo.getCode().equalsIgnoreCase("Reject")){
		}else if (bo.getCode().equalsIgnoreCase("ViewApprove")){
			FlowStateDlg dlg;
			try {
				dlg = new FlowStateDlg(this, po.getParentVO().getClass().getName(), po.getParentVO().getPrimaryKey(),4);
				dlg.showModal();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else if (bo.getCode().equalsIgnoreCase("BillList")){
			BillListUI dlg = new BillListUI(this);
			dlg.showModal();
			String billID = dlg.getBillID();
			if(billID != null){
				try {
					po = NCLocator.getInstance().lookup(IServicesInterface.class).getPO(billID);
				} catch (BusinessException e) {
					e.printStackTrace();
				}
				initializeData(po);
				tablePane.updateUI();
			}
		}
		System.out.println(bo);
	}

	private AggTestPO queryData(String id){
		AggTestPO tempAggVO = null;
		if (id != null && !id.isEmpty()){
			IServicesInterface service = NCLocator.getInstance().lookup(IServicesInterface.class);
			try {
				tempAggVO = service.getPO(id);
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else{//Initialize example data
			tempAggVO = new AggTestPO();
			TestPOMaster master = new TestPOMaster();
			master.setSupplier("newSupplier");
			master.setSum(new UFDouble(0.0));
			tempAggVO.setParentVO(master);
			
			TestPODetail detail = null;
			int count = 3;
			TestPODetail[] details = new TestPODetail[count];
			for(int n = 0; n < count; n++){
				detail = new TestPODetail();
				detail.setMateriel("newMateriel"+n);
				detail.setPrice(new UFDouble(10.0*n));
				detail.setQuanlity(new UFDouble(100.0*n));
				details[n] = detail;
			}
			tempAggVO.setChildrenVO(details);
		}
		return tempAggVO;
	}
	
	private void collectData(){
		//this.textID.setValue(((TestPrayBillMaster)data.getParentVO()).getId());
		((TestPOMaster)po.getParentVO()).setSupplier(this.textSupplier.getValue().toString());
		((TestPOMaster)po.getParentVO()).setSum(new UFDouble(this.textSum.getValue().toString()));
		//tableModel.setRow(Arrays.asList(po.getChildrenVO()));
	}
	
	private void initializeUI(){
		setLayout(new BorderLayout());
		
		pnlHeader = new UIPanel();
		pnlHeader.setPreferredSize(new Dimension(400,30));
		//pnlHeader.setLayout(new BorderLayout());
		UILabel labelID = new UILabel("Purchase Order ID");
		labelID.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlHeader.add(labelID);//,BorderLayout.CENTER);
		textID = new UITextField("");
		textID.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		textID.setEditable(false);
		pnlHeader.add(textID);//,BorderLayout.CENTER);

		UILabel labelSupplier = new UILabel("Supplier");
		labelSupplier.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlHeader.add(labelSupplier);
		textSupplier = new UITextField("");
		textSupplier.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlHeader.add(textSupplier);//,BorderLayout.CENTER);
		
		UILabel labelSum = new UILabel("Sum");
		labelSum.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlHeader.add(labelSum);//,BorderLayout.CENTER);
		textSum = new UITextField("");
		textSum.setPreferredSize(new Dimension(COMPONENT_WIDTH,COMPONENT_HEIGHT));
		pnlHeader.add(textSum);//,BorderLayout.CENTER);
		add(pnlHeader, BorderLayout.NORTH);

		pnlBody = new UIPanel();
		pnlBody.setLayout(new BorderLayout());
		tablePane = new UITablePane();
		tablePane.setPreferredSize(new Dimension(200, 200));
		tableModel = new PrayBillTableModel();
		tablePane.getTable().setModel(tableModel);
		pnlBody.add(tablePane, BorderLayout.CENTER);
		pnlBody.setVisible(true);
		pnlBody.validate();
		add(pnlBody, BorderLayout.CENTER);
	}

	private void initializeData(AggTestPO data){
		this.textID.setValue(((TestPOMaster)data.getParentVO()).getId());
		this.textSupplier.setValue(((TestPOMaster)data.getParentVO()).getSupplier());
		this.textSum.setValue(((TestPOMaster)data.getParentVO()).getSum().toString());
		tableModel.setRow(Arrays.asList(po.getChildrenVO()));
	}
	
	class PrayBillTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7703306407841844L;

		List<CircularlyAccessibleValueObject> propertiesList = new ArrayList<CircularlyAccessibleValueObject>();
		
		private String[] COLUMN_NAMES = new String[] {
				"ID",
				"materiel",
				"Price",
				"Quanlity"
		};

		@Override
		public int getColumnCount() {
			return COLUMN_NAMES.length;
		}
		
		@Override
		public String getColumnName(int arg0) {
			return COLUMN_NAMES[arg0];
		}

		@Override
		public int getRowCount() {
			return propertiesList.size();
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			if (column == 0)
				return false;
			return true;
		}

		@Override
		public Object getValueAt(int row, int column) {
			TestPODetail p = (TestPODetail)propertiesList.get(row);
			
			switch (column) {
			case 0:
				return p.getId();
			case 1:
				return p.getMateriel();
			case 2:
				return p.getPrice();
			case 3:
				return p.getQuanlity();
			default:
				return null;
			}
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			TestPODetail p = (TestPODetail)propertiesList.get(row);
			switch(column){
			case 0:
				p.setId((String)value);
				break;
			case 1:
				p.setMateriel((String)value);
				break;
			case 2:
				p.setPrice(new UFDouble((String)value));
				break;
			case 3:
				p.setQuanlity(new UFDouble((String)value));
				break;
			default:
				break;
			}
		}
		
		public void setRow(List<CircularlyAccessibleValueObject> details) {
			propertiesList.clear();
			propertiesList.addAll(details);
		}
		
		public List<CircularlyAccessibleValueObject> getFormProperties() {
			return propertiesList;
		}
		
		public void validate() throws ValidationException {
		}
		
	}

}
