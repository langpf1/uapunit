package test.biz.rcvbill.vo;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.CircularlyAccessibleValueObject;

/**
 * 
 * ���ӱ�/����ͷ/������ۺ�VO
 *
 * ��������:
 * @author 
 * @version NCPrj ??
 */
@SuppressWarnings("serial")
@nc.vo.annotation.AggVoInfo(parentVO = "test.biz.purchaseorder.TestPOMaster")
public class  AggTestRcvBill extends AggregatedValueObject {

	private CircularlyAccessibleValueObject masterVO;
	private CircularlyAccessibleValueObject[] detailVO;
	@Override
	public CircularlyAccessibleValueObject[] getChildrenVO() {
		return detailVO;
	}

	@Override
	public CircularlyAccessibleValueObject getParentVO() {
		return masterVO;
	}

	@Override
	public void setChildrenVO(CircularlyAccessibleValueObject[] children) {
		detailVO = children;
	}

	@Override
	public void setParentVO(CircularlyAccessibleValueObject parentVO) {
		this.masterVO = parentVO;
	}
 
}
