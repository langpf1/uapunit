package uap.workflow.app.extend.action;

import java.util.HashSet;

import nc.bs.logging.Logger;
import nc.bs.pub.compiler.IPfRun;
import nc.jdbc.framework.exception.DbException;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.compiler.BusiclassVO;
import nc.vo.pub.compiler.PfParameterVO;
import nc.vo.pub.lang.UFBoolean;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.extend.BusiclassDMO;
import uap.workflow.app.vo.IPFConfigInfo;
import uap.workflow.pub.util.PfUtilBaseTools;

/**
 * �����ű�ִ���� 
 * 
 * @author fangj 2005-01-24 15:20 
 * @modifier leijun 2005-12-31 ��ֹʹ��Class.forName()ʵ��������ģ�����
 */
public class PFRunClass {

	public PFRunClass() {
		super();
	}

	/**
	 * ��ѯpub_busiclass����ע���Ĭ�϶����ű���
	 * @param billType �������ͻ�������
	 * @param actionName ��������
	 * @return
	 * @throws BusinessException
	 * @throws DbException
	 */
	private BusiclassVO queryDefaultActionScriptClass(String billType, String actionName)
			throws BusinessException, DbException {
		Logger.debug("�ӱ�pub_busiclass��ѯ���ݵ�Ĭ�϶����ű��࿪ʼ......");
		HashSet<String> hsRet = PfUtilBaseTools.querySimilarTypes(billType);
		BusiclassDMO dmo = new BusiclassDMO();

		for (String strTypeCode : hsRet) {
			BusiclassVO bClassVO = dmo.queryByCond(UFBoolean.FALSE, null, strTypeCode, actionName, null);
			if (bClassVO != null) {
				Logger.debug("�ӱ�pub_busiclass��ѯ���ݵ�Ĭ�϶����ű������......" + bClassVO);
				return bClassVO;
			}
		}
		throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFRunClass-0000")/*�ӱ�pub_busiclass���Ҳ����õ��ݵ�Ĭ�϶����ű���ʵ��*/);
	}

	/**
	 * ��pub_busiclass���в���ע��Ķ����࣬�����㷨��
	 * <li>1.���������ö��ƹ��Ķ����ű��࣬��û�У���
	 * <li>2.��Ĭ�϶����ű��࣬��û�У���
	 * <li>3.�Ҹ��������͵�Ĭ�϶����ű���
	 * 
	 * @param actionName ��������,����"SAVE","EDIT"
	 * @param isBefore FIXME:һ�㶼ΪFALSE?
	 * @param pfVo ����������VO
	 * @return 
	 * @throws BusinessException
	 */
	private BusiclassVO findActionScriptClass(String actionName, UFBoolean isBefore,
			PfParameterVO pfVo) throws BusinessException {
		Logger.debug("�ӱ�pub_busiclass��ѯ���ݶ����ű��࿪ʼ......");
		Logger.debug("*********************************************");
		Logger.debug("* actionName=" + actionName);
		Logger.debug("* billType=" + pfVo.m_billType);
		Logger.debug("* busiType=" + pfVo.m_businessType);
		Logger.debug("*********************************************");

		BusiclassVO bClassVO = null;
		try {
			BusiclassDMO dmo = new BusiclassDMO();
			if (isBefore.booleanValue()) {
				//��Ϊ�� XXX:Ŀǰ5.5�������ߵķ�֧��
				bClassVO = dmo.queryByCond(isBefore, null, pfVo.m_billType, actionName, null);
			} else {
				//1.�����������ö��ƹ��Ķ����ű��ࣨ��ҵ���������ԾͲ��ÿ��ǹ�˾���ԣ�
				//bClassVO = dmo.queryByCond(isBefore, pfVo.m_coId, pfVo.m_billType, actionName, pfVo.m_businessType);
				bClassVO = dmo
						.queryByCond(isBefore, null, pfVo.m_billType, actionName, pfVo.m_businessType);
				if (bClassVO == null)
					//2.����Ĭ�϶����ű���
					bClassVO = queryDefaultActionScriptClass(pfVo.m_billType, actionName);
			}
		} catch (DbException e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PFRunClass-0001", null, new String[]{e.getMessage()})/*�ӱ�pub_busiclass��ѯ�����ű��෢�����ݿ��쳣={0}*/);
		}

		Logger.debug("�ӱ�pub_busiclass��ѯ���ݶ����ű������......");
		return bClassVO;
	}

	/**
	 * �����ű���ʵ������ ִ��
	 * 
	 * @param pfVo
	 * @param isBefore
	 * @param actionName ��������,����"SAVE","EDIT"
	 * @return
	 */
	public Object runComBusi(PfParameterVO pfVo, UFBoolean isBefore, String actionName)
			throws BusinessException {
		Logger.debug("�����ű�ִ�п�ʼ.....");

		//���Ҷ����ű���pub_busiclass��ע�����Ϣ
		BusiclassVO busiClassVo = findActionScriptClass(actionName, isBefore, pfVo);
		String strClassNameNoPackage = busiClassVo.getClassname().trim();
		String className = IPFConfigInfo.ActionPack + "." + strClassNameNoPackage;
		pfVo.m_actionName = actionName;

		Object instOfActionscript = PfUtilTools.findBizImplOfBilltype(busiClassVo.getPk_billtype(),	className);
		Object actionReturnObj = ((IPfRun) instOfActionscript).runComClass(pfVo);

		Logger.debug("�����ű�ִ�����....." + instOfActionscript);
		return actionReturnObj;
	}
}