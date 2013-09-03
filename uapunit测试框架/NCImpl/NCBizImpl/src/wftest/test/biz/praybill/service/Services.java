package test.biz.praybill.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import test.biz.praybill.itf.IServicesInterface;
import test.biz.praybill.vo.AggTestPrayBill;
import test.biz.praybill.vo.TestPrayBillDetail;
import test.biz.praybill.vo.TestPrayBillMaster;

public class Services implements IServicesInterface{

	public AggTestPrayBill[] SavePrayBillByWorkflow(Object[] pos) throws BusinessException{
		List<AggTestPrayBill> result = new ArrayList<AggTestPrayBill>();
		String pk = null;
		AggTestPrayBill temp = null;
		for(Object po : pos){
			if (!(po instanceof AggTestPrayBill))
				throw new BusinessException("数据类型不正确！");
			temp = SavePrayBill((AggTestPrayBill)po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPrayBill(pk));
		}
        return result.toArray(new AggTestPrayBill[1]);
	}

	public AggTestPrayBill[] SavePrayBill(AggTestPrayBill[] pos) throws BusinessException{
		List<AggTestPrayBill> result = new ArrayList<AggTestPrayBill>();
		String pk = null;
		AggTestPrayBill temp = null;
		for(AggTestPrayBill po : pos){
			temp = SavePrayBill(po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPrayBill(pk));
		}
        return result.toArray(new AggTestPrayBill[1]);
	}

	public AggTestPrayBill SavePrayBill(AggTestPrayBill po) throws BusinessException{
        if(po == null || po.getParentVO() == null)
            return null;
        TestPrayBillMaster vo = (TestPrayBillMaster)po.getParentVO();
        BaseDAO dao = new BaseDAO();
        String pk = vo.getPrimaryKey();
        if(StringUtil.isEmptyWithTrim(pk))
            pk = dao.insertVO(vo);
        else
            dao.updateVO(vo);
        String detailId = null;
        for(int n = 0; n < po.getChildrenVO().length; n++){
        	TestPrayBillDetail detail = (TestPrayBillDetail)po.getChildrenVO()[n];
        	detail.setParentid(pk);
            detailId = detail.getPrimaryKey();
            if(StringUtil.isEmptyWithTrim(detailId))
            	detailId = dao.insertVO(detail);
            else
                dao.updateVO(detail);
        }
        
        //Query
        return getPrayBill(pk);
	}
	
	public void SaveMaster(TestPrayBillMaster master){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(master);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void SaveDetail(TestPrayBillDetail detail){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(detail);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public TestPrayBillMaster getMasterByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestPrayBillMaster)dao.retrieveByPK(TestPrayBillMaster.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TestPrayBillDetail getDetailByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestPrayBillDetail)dao.retrieveByPK(TestPrayBillDetail.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestPrayBill getPrayBill(String ID) {
		BaseDAO dao = new BaseDAO();
		try {
			
			AggTestPrayBill tempAggVO = new AggTestPrayBill();
			TestPrayBillMaster master = getMasterByID(ID);
			tempAggVO.setParentVO(master);
			
			Collection<TestPrayBillDetail> details = dao.retrieveByClause(
					TestPrayBillDetail.class, "parentid='"+ID+"'");
			tempAggVO.setChildrenVO((TestPrayBillDetail[])details.toArray(new TestPrayBillDetail[0]));
			return tempAggVO;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestPrayBill[] getPrayBill() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			List<AggTestPrayBill> aggVOList = new ArrayList<AggTestPrayBill>();
			Collection<TestPrayBillMaster> masters = (Collection<TestPrayBillMaster>)dao.retrieveByClause(TestPrayBillMaster.class, "", "ts");
			Collection<TestPrayBillDetail> details = null;
			AggTestPrayBill tempAggVO = null;
			for(TestPrayBillMaster master : masters.toArray(new TestPrayBillMaster[0])){
				details = dao.retrieveByClause(TestPrayBillDetail.class, "parentid='"+master.getId()+"'", "ts");
				tempAggVO = new AggTestPrayBill();
				tempAggVO.setParentVO(master);
				tempAggVO.setChildrenVO(details.toArray(new TestPrayBillDetail[0]));
				aggVOList.add(tempAggVO);
			}
			return aggVOList.toArray(new AggTestPrayBill[0]);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
