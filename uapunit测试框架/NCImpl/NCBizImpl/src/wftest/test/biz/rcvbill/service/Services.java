package test.biz.rcvbill.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import test.biz.rcvbill.itf.IServicesInterface;
import test.biz.rcvbill.vo.AggTestRcvBill;
import test.biz.rcvbill.vo.TestRcvBillDetail;
import test.biz.rcvbill.vo.TestRcvBillMaster;

public class Services implements IServicesInterface{

	public AggTestRcvBill[] SavePOByWorkflow(Object[] pos) throws BusinessException{
		List<AggTestRcvBill> result = new ArrayList<AggTestRcvBill>();
		String pk = null;
		AggTestRcvBill temp = null;
		for(Object po : pos){
			if (!(po instanceof AggTestRcvBill))
				throw new BusinessException("数据类型不正确！");
			temp = SavePO((AggTestRcvBill)po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPO(pk));
		}
        return result.toArray(new AggTestRcvBill[1]);
	}

	public AggTestRcvBill[] SaveRcvBill(AggTestRcvBill[] pos) throws BusinessException{
		List<AggTestRcvBill> result = new ArrayList<AggTestRcvBill>();
		String pk = null;
		AggTestRcvBill temp = null;
		for(AggTestRcvBill po : pos){
			temp = SavePO(po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPO(pk));
		}
        return result.toArray(new AggTestRcvBill[1]);
	}

	public AggTestRcvBill SavePO(AggTestRcvBill po) throws BusinessException{
        if(po == null || po.getParentVO() == null)
            return null;
        TestRcvBillMaster vo = (TestRcvBillMaster)po.getParentVO();
        BaseDAO dao = new BaseDAO();
        String pk = vo.getPrimaryKey();
        if(StringUtil.isEmptyWithTrim(pk))
            pk = dao.insertVO(vo);
        else
            dao.updateVO(vo);
        String detailId = null;
        for(int n = 0; n < po.getChildrenVO().length; n++){
        	TestRcvBillDetail detail = (TestRcvBillDetail)po.getChildrenVO()[n];
        	detail.setParentid(pk);
            detailId = detail.getPrimaryKey();
            if(StringUtil.isEmptyWithTrim(detailId))
            	detailId = dao.insertVO(detail);
            else
                dao.updateVO(detail);
        }
        
        //Query
        return getPO(pk);
	}
	
	public void SaveMaster(TestRcvBillMaster master){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(master);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void SaveDetail(TestRcvBillDetail detail){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(detail);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public TestRcvBillMaster getMasterByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestRcvBillMaster)dao.retrieveByPK(TestRcvBillMaster.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TestRcvBillDetail getDetailByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestRcvBillDetail)dao.retrieveByPK(TestRcvBillDetail.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestRcvBill getPO(String ID) {
		BaseDAO dao = new BaseDAO();
		try {
			
			AggTestRcvBill tempAggVO = new AggTestRcvBill();
			TestRcvBillMaster master = getMasterByID(ID);
			tempAggVO.setParentVO(master);
			
			Collection<TestRcvBillDetail> details = dao.retrieveByClause(
					TestRcvBillDetail.class, "parentid='"+ID+"'");
			tempAggVO.setChildrenVO((TestRcvBillDetail[])details.toArray(new TestRcvBillDetail[0]));
			return tempAggVO;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestRcvBill[] getPO() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			List<AggTestRcvBill> aggVOList = new ArrayList<AggTestRcvBill>();
			Collection<TestRcvBillMaster> masters = (Collection<TestRcvBillMaster>)dao.retrieveByClause(TestRcvBillMaster.class, "", "ts");
			Collection<TestRcvBillDetail> details = null;
			AggTestRcvBill tempAggVO = null;
			for(TestRcvBillMaster master : masters.toArray(new TestRcvBillMaster[0])){
				details = dao.retrieveByClause(TestRcvBillDetail.class, "parentid='"+master.getId()+"'", "ts");
				tempAggVO = new AggTestRcvBill();
				tempAggVO.setParentVO(master);
				tempAggVO.setChildrenVO(details.toArray(new TestRcvBillDetail[0]));
				aggVOList.add(tempAggVO);
			}
			return aggVOList.toArray(new AggTestRcvBill[0]);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
