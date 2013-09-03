package uap.workflow.app.email;

import uap.workflow.pub.app.mobile.IPFMobileSequence;
import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;


/**
 * ID������
 * <li>���������кų���Ϊ��Ϊ������"0001"��"9999"��Ȼ��ѭ��ʹ��
 * <li>��Ҫ�����ƶ�����Ӧ��
 *
 * @author leijun 2006-9-15
 */
public class MobileSequence implements IPFMobileSequence {
	public static final int MAX_SIZE = 9999;

	public MobileSequence() {
	}

	public long[] nextLongValue(String mobile, int iBatchSize) throws BusinessException {
		if (iBatchSize > MAX_SIZE || iBatchSize < 1)
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0078")/*@res ">>����������ȡ���ŻỰID��������="*/ + iBatchSize);

		long[] retIds = new long[iBatchSize];
		SeqGenerator sg = new SeqGenerator();
		for (int i = 0; i < iBatchSize; i++) {
			synchronized (this) {
				retIds[i] = sg.getNextKey(mobile, iBatchSize);
			}
		}

		return retIds;
	}

	public String[] nextStringValue(String mobile, int iBatchSize, int iPaddingLength)
			throws BusinessException {
		if (iBatchSize > MAX_SIZE || iBatchSize < 1)
			throw new IllegalArgumentException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow61_0","0pfworkflow61-0078")/*@res ">>����������ȡ���ŻỰID��������="*/ + iBatchSize);

		String[] retIds = new String[iBatchSize];
		SeqGenerator sg = new SeqGenerator();
		for (int i = 0; i < iBatchSize; i++) {
			String s = "";
			synchronized (this) {
				s = Long.toString(sg.getNextKey(mobile, iBatchSize));
			}
			int len = s.length();
			if (len < iPaddingLength) {
				StringBuffer buf = new StringBuffer(iPaddingLength);
				for (int j = 0; j < iPaddingLength - len; j++) {
					buf.append('0');
				}
				buf.append(s);
				s = buf.toString();
			}
			retIds[i] = s;
		}

		return retIds;
	}

	private class SeqGenerator {
		long nextId = 0;

		int iter = 0;

		/**
		 * ����������ֵ����ѭ��ʹ��
		 * @param mobile
		 * @param iBatchSize
		 * @return
		 * @throws BusinessException
		 */
		protected long getNextKey(String mobile, int iBatchSize) throws BusinessException {
			if (iter == 0) {
				BaseDAO dao = new BaseDAO();

				String sqlMax = "select max_value from pub_mobileseq where mobile='" + mobile + "'";
				String sqlInit = "insert into pub_mobileseq(mobile,max_value) values(?,?)";
				String sqlUpdate = "update pub_mobileseq set max_value=? where mobile=?";

				Object obj = dao.executeQuery(sqlMax, new ColumnProcessor());
				int iMaxValue = 0;
				if (obj == null) {
					// ����
					SQLParameter para = new SQLParameter();
					para.addParam(mobile);
					para.addParam(iBatchSize);

					dao.executeUpdate(sqlInit, para);
					nextId = 1;
				} else {
					iMaxValue = new Integer(obj.toString()).intValue();
					// WARN::��֤���ֵ������9999
					if (iMaxValue + iBatchSize > MAX_SIZE) {
						nextId = iMaxValue + 1;
						if (nextId > MAX_SIZE)
							nextId = 1;
						iMaxValue = iMaxValue + iBatchSize - MAX_SIZE;
					} else {
						nextId = iMaxValue + 1;
						iMaxValue = iMaxValue + iBatchSize;
					}
					// increment the sequence column
					SQLParameter para = new SQLParameter();
					para.addParam(iMaxValue);
					para.addParam(mobile);

					dao.executeUpdate(sqlUpdate, para);
				}
				iter++;
			} else {
				nextId++;
				if (nextId > MAX_SIZE)
					nextId = 1;
				iter++;
			}

			return nextId;
		}
	}

}