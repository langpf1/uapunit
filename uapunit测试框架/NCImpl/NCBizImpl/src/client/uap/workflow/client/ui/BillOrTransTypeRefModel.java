package uap.workflow.client.ui;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.bs.IconResources;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.itf.org.IOrgConst;
import nc.itf.org.IOrgResourceCodeConst;
import nc.ui.bd.ref.AbstractRefGridTreeModel;
import nc.ui.dbcache.DBCacheFacade;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.vo.bd.pub.BDCacheQueryUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.GroupVO;
import nc.vo.org.OrgVO;
import nc.vo.org.util.OrgPubUtil;
import nc.vo.org.util.OrgTreeCellRendererIconPolicy;
import nc.vo.org.util.RefNodeUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.IBBDPubConst;
import nc.vo.pub.billtype.BilltypeVO;

public class BillOrTransTypeRefModel extends AbstractRefGridTreeModel {
	public BillOrTransTypeRefModel() {
		super();
		reset();
	}

	@Override
	public void reset() {

		setRefNodeName("��������/��������");
		setRootVisible(false);
		setClassTableName("bd_billtype");
		setClassFatherField("pk_billtypeid");
		setClassChildField(OrgVO.PK_ORG);
		setClassFieldCode(new String[]{OrgVO.CODE, OrgVO.NAME,OrgVO.PK_ORG, OrgVO.PK_FATHERORG, OrgVO.PK_ACCPERIODSCHEME, OrgVO.PK_CURRTYPE, OrgVO.PK_EXRATESCHEME});
		//setClassJoinField(OrgVO.PK_ORG);
		setClassWherePart(" pk_group ='"+InvocationInfoProxy.getInstance().getGroupId()+"' ");
		setFieldCode(new String[] { OrgVO.CODE, OrgVO.NAME});
		setFieldName(new String[] { 
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0003279") /* @res "����" */,
				NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC000-0001155") /* @res "����" */});
		setHiddenFieldCode(new String[] {OrgVO.PK_ORG, OrgVO.PK_FATHERORG, OrgVO.PK_ACCPERIODSCHEME, OrgVO.PK_CURRTYPE, OrgVO.PK_EXRATESCHEME });
		setPkFieldCode(OrgVO.PK_ORG);
		setRefCodeField(OrgVO.CODE);
		setRefNameField(OrgVO.NAME);
		setTableName(new OrgVO().getTableName());
		setOrderPart(OrgVO.CODE);
		setResourceID(IOrgResourceCodeConst.ORG);
		//setDocJoinField(OrgVO.PK_ORG);
		
		//�����ù�����������
		setAddEnableStateWherePart(true);
		
		setFilterRefNodeName(new String[] {"����"});
		
		resetFieldName();
		
	    this.setTreeIconPolicy(new OrgTreeCellRendererIconPolicy(IconResources.ICON_Bu) {
			public String getSpecialNodeIcon(Object curTreeNode) {
				if (curTreeNode instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode n = (DefaultMutableTreeNode) curTreeNode;
					Object o = n.getUserObject();
					return RefNodeUtil.isSpecialNode(IconResources.ICON_Global, o) ? IconResources.ICON_Global : RefNodeUtil.isSpecialNode(IconResources.ICON_Group, o) ? IconResources.ICON_Group
							: null;
				}
				return null;
			}
		});
	}

	@Override
	public void filterValueChanged(ValueChangedEvent changedValue) {
		super.filterValueChanged(changedValue);
		String[] selectedPKs = (String[]) changedValue.getNewValue();
		if (selectedPKs != null && selectedPKs.length > 0) {
			setPk_group(selectedPKs[0]);
		}
	}
	
	private boolean incluedGlobleVOAndCurrGroupVOFlag = false;
	
	public void setIncluedGlobleVOAndCurrGroupVOFlag(
			boolean incluedGlobleVOAndCurrGroupVOFlag) {
		this.incluedGlobleVOAndCurrGroupVOFlag = incluedGlobleVOAndCurrGroupVOFlag;
	}

	@Override
	public Vector getData() {
		//��ǰ̨�����������˼��š�ҵ��Ԫ����ʱ���ӻ�����ȡ��ȫ����֯�ͼ�����֯������vector��
		if (incluedGlobleVOAndCurrGroupVOFlag &&
			!RuntimeEnv.getInstance().isRunningInServer() && 
			DBCacheFacade.isTableCached(OrgVO.getDefaultTableName(), null) && 
			DBCacheFacade.isTableCached(GroupVO.getDefaultTableName(), null)) {
			Vector data =  super.getData();
			try {
        data.add(RefNodeUtil.getSpecialNode(IconResources.ICON_Global,getGlobalOrgVOVector()));
        data.add(RefNodeUtil.getSpecialNode(IconResources.ICON_Group,getGroupVOVector()));
			} catch (BusinessException e) {
				throw new BusinessExceptionAdapter(e);
			}
			return data;
		} else {
			return super.getData();
		}
	}

	//�ӻ����в�ѯȫ��VO����װ��vector
	private Vector<String> getGlobalOrgVOVector() throws BusinessException {
		OrgVO[] orgvos = (OrgVO[]) BDCacheQueryUtil.queryVOsByIDs(OrgVO.class, OrgVO.PK_ORG, new String[] {IOrgConst.GLOBEORG}, 
				new String[] { OrgVO.PK_ORG, OrgVO.CODE, OrgVO.NAME, OrgVO.NAME2, OrgVO.NAME3, OrgVO.NAME4, OrgVO.NAME5, OrgVO.NAME6, OrgVO.PK_FATHERORG, OrgVO.PK_ACCPERIODSCHEME, OrgVO.PK_CURRTYPE, OrgVO.PK_EXRATESCHEME });
		if (orgvos == null || orgvos.length == 0)
			return null;
		
		Vector<String> globalorgvo = new Vector<String>();
		//�ֶ�˳����FieldCode����HiddenFieldCode
		globalorgvo.add(orgvos[0].getCode());
		globalorgvo.add(OrgPubUtil.getNameByMultiLang(orgvos[0], GroupVO.NAME));
		globalorgvo.add(orgvos[0].getPk_org());
		globalorgvo.add(orgvos[0].getPk_fatherorg());
		globalorgvo.add(orgvos[0].getPk_accperiodscheme());
		globalorgvo.add(orgvos[0].getPk_currtype());
		globalorgvo.add(orgvos[0].getPk_exratescheme());
		
		return globalorgvo;
	}

	//�ӻ����в�ѯ����VO����װ��vector
	private Vector<String> getGroupVOVector() throws BusinessException {
		GroupVO[] groupvos = (GroupVO[]) BDCacheQueryUtil.queryVOsByIDs(GroupVO.class, GroupVO.PK_GROUP, new String[] {getPk_group()}, 
				new String[] { GroupVO.PK_GROUP, GroupVO.CODE, GroupVO.NAME, GroupVO.NAME2, GroupVO.NAME3, GroupVO.NAME4, GroupVO.NAME5, GroupVO.NAME6, GroupVO.PK_FATHERGROUP, GroupVO.PK_ACCPERIODSCHEME, GroupVO.PK_CURRTYPE, GroupVO.PK_EXRATESCHEME });
		if (groupvos == null || groupvos.length == 0)
			return null;
		
		Vector<String> groupvo = new Vector<String>();
		groupvo.add(groupvos[0].getCode());
		groupvo.add(OrgPubUtil.getNameByMultiLang(groupvos[0], GroupVO.NAME));
		groupvo.add(groupvos[0].getPk_group());
		groupvo.add(groupvos[0].getPk_fathergroup());
		groupvo.add(groupvos[0].getPk_accperiodscheme());
		groupvo.add(groupvos[0].getPk_currtype());
		groupvo.add(groupvos[0].getPk_exratescheme());
		
		return groupvo;
	}
	
	@Override
	protected String getEnvWherePart() {
		//��ǰ̨�����������˼��š�ҵ��Ԫ����ʱ���ӻ�����ȡ��ȫ����֯�ͼ�����֯������vector�У�
		//���ֻ�践�ز�ѯ������ҵ��Ԫ��������
		if (!RuntimeEnv.getInstance().isRunningInServer() && 
			DBCacheFacade.isTableCached(OrgVO.getDefaultTableName(), null) && 
			DBCacheFacade.isTableCached(GroupVO.getDefaultTableName(), null)) {
			return OrgVO.ISBUSINESSUNIT + " = 'Y' and " + OrgVO.PK_GROUP + " = '" + getPk_group() + "'";
		} 
		
		if (incluedGlobleVOAndCurrGroupVOFlag) {
			//ҵ��Ԫ��������Ҫ���ճ�ȫ�֡���ǰ���š���ǰ����������ҵ��Ԫ
			return OrgVO.PK_ORG + " = '" + IOrgConst.GLOBEORG + "' or " + 
					OrgVO.PK_ORG + " = '" + getPk_group() + "' or (" + 
					OrgVO.ISBUSINESSUNIT + " = 'Y' and " + OrgVO.PK_GROUP + " = '" + getPk_group() + 
					"')";
		} else {
			return OrgVO.ISBUSINESSUNIT + " = 'Y' and " + OrgVO.PK_GROUP + " = '" + getPk_group() + "'";
		}
	}
}
