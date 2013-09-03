package uap.workflow.nc.participantfilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import uap.workflow.app.config.ParticipantFilterTypeFactory;
import uap.workflow.app.participant.IParticipantFilter;
import uap.workflow.app.participant.IParticipantFilterType;
import uap.workflow.app.participant.ParticipantException;

import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.billtype2.Billtype2VO;
import nc.vo.pub.billtype2.ExtendedClassEnum;

/**
 * �������޶�ģʽ��չ�� ��ʵ��������
 * <li>ʹ���˻���
 * <li>ֻ�ɺ�̨���ã���Ϊ�ж�Private���ʵ����
 */
public class ParticipantFilterFactory {
	/**
	 * ����
	 */
	private static ParticipantFilterFactory inst = new ParticipantFilterFactory();

	/**
	 * �޶�ģʽ�������޶�ģʽʵ����ӳ���
	 */
	private HashMap<IParticipantFilterType, IParticipantFilter> hmCodeToFilter = new HashMap<IParticipantFilterType, IParticipantFilter>();

	private ParticipantFilterFactory() {
	}

	public static ParticipantFilterFactory getInstance() {
		return inst;
	}

	/**
	 * �����޶�ģʽ���ͣ���ѯ������޶�ģʽʵ�� 
	 * 
	 * @param participantFilterType �޶�ģʽ����
	 * @return
	 */
	public IParticipantFilter getParticipantFilter(IParticipantFilterType participantFilterType, String billType) {
		IParticipantFilter pf = hmCodeToFilter.get(participantFilterType);
		if (pf == null) {
			pf = createDefaultFilter(participantFilterType);
			if (pf != null) {
				hmCodeToFilter.put(participantFilterType, pf);
			} else {
				ArrayList<Billtype2VO> bt2VOs = PfDataCache.getBillType2Info(billType, ExtendedClassEnum.PARTICIPANT_FILTER.getIntValue());

				// ʵ�����õ�������ע������в����߹�����
				for (Iterator iterator = bt2VOs.iterator(); iterator.hasNext();) {
					Billtype2VO bt2VO = (Billtype2VO) iterator.next();
					try {
						Object obj = PfUtilTools.findBizImplOfBilltype(	billType, bt2VO.getClassname());
						///�Լ���չ���޶�ģʽ��û�г�ʼ��???
						///hmCodeToFilter.put(bt2VO.getCode(),	(IParticipantFilter) obj);
					} catch (Exception e) {
						Logger.error("�޷�ʵ�����������޶���billType=" + billType	+ ",className=" + bt2VO.getClassname(), e);
					}
				}
			}
		}

		return hmCodeToFilter.get(participantFilterType);
	}

	/**
	 * ʵ����UAP����Ĭ�ϵļ���������
	 * 
	 * @param filterCode
	 * @return
	 */
	private IParticipantFilter createDefaultFilter(IParticipantFilterType participantFilterType) {
		IParticipantFilter pf = null;
		ParticipantFilterTypeFactory filterTypeFactory = ParticipantFilterTypeFactory.getInstance();
		String implClassName = filterTypeFactory.getImpl(participantFilterType.getCode());
		
		try {
			Class<IParticipantFilter> clazz;
			clazz = (Class<IParticipantFilter>) Class.forName(implClassName);
			pf = clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (InstantiationException e) {
			throw new ParticipantException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ParticipantException(e.getMessage(), e);
		}

		/*
		if(participantFilterType instanceof SameCorpAndDeptFilterType)
		{
			pf = new SameCorpAndDeptFilter();
		}
		
		if(participantFilterType instanceof SameCorpFilterType)
		{
			pf = new SameCorpFilter();
		}

		if(participantFilterType instanceof SameDeptFilterType)
		{
			pf = new SameDeptFilter();
		}

		if(participantFilterType instanceof SameOrg4ResponsibilityFilterType)
		{
			pf = new SameOrg4ResponsibilityFilter();
		}

		if(participantFilterType instanceof SuperiorFilterType)
		{
			pf = new SuperiorFilter();
		}

		if(participantFilterType instanceof SuperiorOrg4ResponsibilityFilterType)
		{
			pf = new SuperiorOrg4ResponsibilityFilter();
		}
		*/
		return pf;
	}
}
