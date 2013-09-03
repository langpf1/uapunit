package test.biz.purchaseorder.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import test.biz.purchaseorder.itf.IServicesInterface;
import test.biz.purchaseorder.vo.AggTestPO;
import test.biz.purchaseorder.vo.TestPODetail;
import test.biz.purchaseorder.vo.TestPOMaster;

public class Services implements IServicesInterface{

	public AggTestPO[] SavePOByWorkflow(Object[] pos) throws BusinessException{
		List<AggTestPO> result = new ArrayList<AggTestPO>();
		String pk = null;
		AggTestPO temp = null;
		for(Object po : pos){
			if (!(po instanceof AggTestPO))
				throw new BusinessException("数据类型不正确！");
			temp = SavePO((AggTestPO)po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPO(pk));
		}
        return result.toArray(new AggTestPO[1]);
	}

	public AggTestPO[] SavePO(AggTestPO[] pos) throws BusinessException{
		List<AggTestPO> result = new ArrayList<AggTestPO>();
		String pk = null;
		AggTestPO temp = null;
		for(AggTestPO po : pos){
			temp = SavePO(po);
			pk = temp.getParentVO().getPrimaryKey();
			result.add(getPO(pk));
		}
        return result.toArray(new AggTestPO[1]);
	}

	public AggTestPO SavePO(AggTestPO po) throws BusinessException{
        if(po == null || po.getParentVO() == null)
            return null;
        TestPOMaster vo = (TestPOMaster)po.getParentVO();
        BaseDAO dao = new BaseDAO();
        String pk = vo.getPrimaryKey();
        if(StringUtil.isEmptyWithTrim(pk))
            pk = dao.insertVO(vo);
        else
            dao.updateVO(vo);
        String detailId = null;
        for(int n = 0; n < po.getChildrenVO().length; n++){
        	TestPODetail detail = (TestPODetail)po.getChildrenVO()[n];
        	detail.setParentid(pk);
            detailId = detail.getPrimaryKey();
            if(StringUtil.isEmptyWithTrim(detailId))
            	detailId = dao.insertVO(detail);
            else
                dao.updateVO(detail);
        }
        
        //Query
        //return getPO(pk);
        return po;
	}
	
	public void SaveMaster(TestPOMaster master){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(master);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void SaveDetail(TestPODetail detail){
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(detail);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public TestPOMaster getMasterByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestPOMaster)dao.retrieveByPK(TestPOMaster.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public TestPODetail getDetailByID(String ID){
		BaseDAO dao = new BaseDAO();
		try {
			return (TestPODetail)dao.retrieveByPK(TestPODetail.class, ID);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestPO getPO(String ID) {
		BaseDAO dao = new BaseDAO();
		try {
			
			AggTestPO tempAggVO = new AggTestPO();
			TestPOMaster master = getMasterByID(ID);
			tempAggVO.setParentVO(master);
			
			Collection<TestPODetail> details = dao.retrieveByClause(
					TestPODetail.class, "parentid='"+ID+"'");
			tempAggVO.setChildrenVO((TestPODetail[])details.toArray(new TestPODetail[0]));
			return tempAggVO;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AggTestPO[] getPO() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		try {
			List<AggTestPO> aggVOList = new ArrayList<AggTestPO>();
			Collection<TestPOMaster> masters = (Collection<TestPOMaster>)dao.retrieveByClause(TestPOMaster.class, "", "ts");
			Collection<TestPODetail> details = null;
			AggTestPO tempAggVO = null;
			for(TestPOMaster master : masters.toArray(new TestPOMaster[0])){
				details = dao.retrieveByClause(TestPODetail.class, "parentid='"+master.getId()+"'", "ts");
				tempAggVO = new AggTestPO();
				tempAggVO.setParentVO(master);
				tempAggVO.setChildrenVO(details.toArray(new TestPODetail[0]));
				aggVOList.add(tempAggVO);
			}
			return aggVOList.toArray(new AggTestPO[0]);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
