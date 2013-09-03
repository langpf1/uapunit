package nc.bs.pub.pf;

import nc.ui.pub.print.IDataSource;
import nc.vo.pub.BusinessException;

/**
 * 后台打印数据源获取接口
 * <li>产品组实现类注册在单据类型表bd_billtype.referclassname中
 * 
 * @author leijun 2007-7-7
 */
public interface IPrintDataGetter {
	/**
	 * 根据单据ID，查询出打印模板所选的打印数据源信息
	 * @param billId 单据主表PK
	 * @param billtype 单据类型PK
	 * @param checkman 审批人ID
	 * @return
	 * @throws BusinessException
	 */
	IDataSource getPrintDs(String billId, String billtype, String checkman) throws BusinessException;
}
