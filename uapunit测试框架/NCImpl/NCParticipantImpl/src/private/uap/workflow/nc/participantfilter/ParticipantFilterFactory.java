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
 * 参与者限定模式扩展类 的实例化工厂
 * <li>使用了缓存
 * <li>只可后台调用，因为有对Private类的实例化
 */
public class ParticipantFilterFactory {
	/**
	 * 单例
	 */
	private static ParticipantFilterFactory inst = new ParticipantFilterFactory();

	/**
	 * 限定模式类型与限定模式实例的映射表
	 */
	private HashMap<IParticipantFilterType, IParticipantFilter> hmCodeToFilter = new HashMap<IParticipantFilterType, IParticipantFilter>();

	private ParticipantFilterFactory() {
	}

	public static ParticipantFilterFactory getInstance() {
		return inst;
	}

	/**
	 * 根据限定模式类型，查询缓存的限定模式实例 
	 * 
	 * @param participantFilterType 限定模式类型
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

				// 实例化该单据类型注册的所有参与者过滤器
				for (Iterator iterator = bt2VOs.iterator(); iterator.hasNext();) {
					Billtype2VO bt2VO = (Billtype2VO) iterator.next();
					try {
						Object obj = PfUtilTools.findBizImplOfBilltype(	billType, bt2VO.getClassname());
						///自己扩展的限定模式还没有初始化???
						///hmCodeToFilter.put(bt2VO.getCode(),	(IParticipantFilter) obj);
					} catch (Exception e) {
						Logger.error("无法实例化参与者限定类billType=" + billType	+ ",className=" + bt2VO.getClassname(), e);
					}
				}
			}
		}

		return hmCodeToFilter.get(participantFilterType);
	}

	/**
	 * 实例化UAP内置默认的几个过滤器
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
