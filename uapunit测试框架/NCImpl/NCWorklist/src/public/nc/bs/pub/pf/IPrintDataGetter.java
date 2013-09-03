package nc.bs.pub.pf;

import nc.ui.pub.print.IDataSource;
import nc.vo.pub.BusinessException;

/**
 * ��̨��ӡ����Դ��ȡ�ӿ�
 * <li>��Ʒ��ʵ����ע���ڵ������ͱ�bd_billtype.referclassname��
 * 
 * @author leijun 2007-7-7
 */
public interface IPrintDataGetter {
	/**
	 * ���ݵ���ID����ѯ����ӡģ����ѡ�Ĵ�ӡ����Դ��Ϣ
	 * @param billId ��������PK
	 * @param billtype ��������PK
	 * @param checkman ������ID
	 * @return
	 * @throws BusinessException
	 */
	IDataSource getPrintDs(String billId, String billtype, String checkman) throws BusinessException;
}
