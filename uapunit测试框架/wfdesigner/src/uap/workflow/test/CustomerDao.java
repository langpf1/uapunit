package uap.workflow.test;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.vo.pub.SuperVO;
import uap.workflow.engine.dao.WfBaseDAO;

/**
 * 
 * @author kongml
 * 
 */
public class CustomerDao {
	public String addBill(CustomerVO vo) throws DAOException {
		WfBaseDAO dao = new WfBaseDAO();
		return dao.insertVo(vo);
	}

	public void delBillByCode(String code) throws DAOException {
		WfBaseDAO dao = new WfBaseDAO();
		dao.deleteByPk(CustomerVO.class, code);
	}

	public int updateBill(CustomerVO vo) throws DAOException {
		WfBaseDAO dao = new WfBaseDAO();
		return dao.updateVo(vo);
	}

	public CustomerVO getBillByPk(String billcode) throws DAOException {
		CustomerVO[] bills = getBills(String.format("billcode='%s'", billcode));
		if (bills.length == 1) {
			return bills[0];
		}
		return null;
	}

	public CustomerVO[] getBills(String where) throws DAOException {
		WfBaseDAO dao = new WfBaseDAO();
		SuperVO[] result = dao.queryByCondition(CustomerVO.class, where);
		if (result != null && result.length > 0) {
			List<CustomerVO> bills = new ArrayList<CustomerVO>();
			for (SuperVO vo : result) {
				CustomerVO bill = (CustomerVO) vo;
				if (bill != null) {
					bills.add(bill);
				}
			}
			return bills.toArray(new CustomerVO[0]);
		}
		return new CustomerVO[0];
	}
}
