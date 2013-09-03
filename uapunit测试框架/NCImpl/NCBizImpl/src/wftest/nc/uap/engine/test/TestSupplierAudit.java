package nc.uap.engine.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.uap.bd.supplier.pf.ISupplierPfConst;
import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.pub.pf.PfUtilClient;
import nc.ui.trade.businessaction.IPFACTION;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.bd.supplier.pf.AggSupplierPfVO;
import nc.vo.bd.supplier.pf.SupplierPfVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;

public class TestSupplierAudit {

	public void TestFlow(){
		//Start Process
		try{
			//login
			
			//Save
			SaveSupplier();
			
			//Approve
			ApproveSupplier();
			
			//Logout
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void SaveSupplier() throws BusinessException{
		BaseDAO dao = new BaseDAO();
		Collection<SupplierVO> supplierVOs = (Collection<SupplierVO>)dao.retrieveByClause(SupplierVO.class, "", "");
		SupplierVO supBaseInfoVO = null;
		supBaseInfoVO = supplierVOs.iterator().next();
		//String pk_group = supBaseInfoVO.getPk_group();
		//supBaseInfoVO.setPk_group(pk_group);
		
		
		Collection<AggSupplierPfVO> aggSupplierPfVOs = 
			(Collection<AggSupplierPfVO>)dao.retrieveByClause(AggSupplierPfVO.class, "", "");
		AggSupplierPfVO aggSupplierPfVO = null;
		aggSupplierPfVO = aggSupplierPfVOs.iterator().next();
		SupplierPfVO supApproveVO = (SupplierPfVO) aggSupplierPfVO.getParentVO();
		supApproveVO.setBsupbaseinfo(supBaseInfoVO);
		supApproveVO.setPk_supclass(supBaseInfoVO.getPk_supplierclass());
		supApproveVO.setPk_org_sup(supBaseInfoVO.getPk_org());
		supApproveVO.setVsupcode(supBaseInfoVO.getCode());
		supApproveVO.setVsupname(supBaseInfoVO.getName());
		supApproveVO.setVsupname2(supBaseInfoVO.getName2());
		supApproveVO.setVsupname3(supBaseInfoVO.getName3());
		
		//validate(supApproveVO);
		
		AggregatedValueObject retVO = (AggregatedValueObject) PfUtilClient
				.runAction(null, IPFACTION.SAVE,
						ISupplierPfConst.SUPPLIER_BILL_TYPE, aggSupplierPfVO, null,
						null, null, null);
	}
	
	
	@SuppressWarnings("unchecked")
	public void ApproveSupplier() throws Exception{
		BaseDAO dao = new BaseDAO();
		Collection<AggSupplierPfVO> aggSupplierPfVOs = 
			(Collection<AggSupplierPfVO>)dao.retrieveByClause(AggSupplierPfVO.class, "", "");
		List<AggregatedValueObject> list = new ArrayList<AggregatedValueObject>();
		Iterator iterator = aggSupplierPfVOs.iterator(); 
		while (iterator.hasNext())
			list.add((AggregatedValueObject)iterator.next());
		
		AggregatedValueObject[] vos = (AggregatedValueObject[])list.toArray();
		String pk_user = WorkbenchEnvironment.getInstance().getLoginUser().getPrimaryKey();
		PfUtilClient.runBatch(null,
				IPFActionName.APPROVE + pk_user, ISupplierPfConst.SUPPLIER_BILL_TYPE, vos, null, null, null);
	}
}
