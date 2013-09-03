package uap.workflow.modeler;

import java.util.HashMap;

import uap.workflow.engine.vos.ProcessDefinitionVO;

import nc.bs.logging.Logger;
import nc.bs.pf.pub.PfDataCache;
import nc.md.MDBaseQueryFacade;
import nc.md.model.MetaDataException;
import nc.md.model.type.IEnumType;
import nc.ui.ml.NCLangRes;
import nc.ui.pf.multilang.PfMultiLangUtil;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.beans.table.VOTableModel;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pf.pub.BusitypeVO;
import nc.vo.pf.pub.util.OrgUtil;
import nc.vo.wfengine.definition.WorkflowDefStatusEnum;

public class WorkflowsTableModel extends VOTableModel {

	private String[] Columns = { "ID",
			"Name",
			"Description",
			"Default bill type",
			"Publish date",
			"Organization",
			"Flow state",
			"Version"};

	private HashMap<Integer, String> busiprop = new HashMap<Integer, String>();

	public WorkflowsTableModel(Class c) {
		super(c);
//		IEnumType type;
//		try {
//			type = (IEnumType) MDBaseQueryFacade.getInstance().getTypeByFullClassName(
//					"nc.vo.pub.pfflow00.BusiTypeClassVO");
//			IConstEnum[] enums = type.getConstEnums();
//			for (IConstEnum constenum : enums) {
//				busiprop.put((Integer) constenum.getValue(), constenum.getName());
//			}
//		} catch (MetaDataException e) {
//			Logger.error(e.getMessage(), e);
//		}
	}

	@Override
	public int getColumnCount() {
		return Columns.length;
	}

	public int[] getColumnWidth() {
		int[] iWidths = new int[] { 100, 120, 100, 100, 180, 100, 100, 100};
		return iWidths;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ProcessDefinitionVO vo = (ProcessDefinitionVO) getVO(rowIndex);
		String value = "";
		if (vo != null)
			switch (columnIndex) {
			case 0:
				return vo.getProdef_id();
			case 1:
				return vo.getProdef_name();
			case 2:
				return vo.getProdef_desc();
			case 3:
				return vo.getPk_bizobject();
			case 4:
				return vo.getTs().toLocalString();
			case 5:
				String orgsName = vo.getPk_org()== null? "" : OrgUtil.convertOrgNameByOrgPK(vo.getPk_org().split(","));
				return orgsName;
			case 6:
				//int state = vo.getValidity() == null ? 0 : vo.getValidity();
				return vo.getValidity();//state==0?"Stop":"Start";
			case 7:
				return StringUtil.isEmptyWithTrim(vo.getProdef_version()) ? "1.0" : vo.getProdef_version();
			}
		return value;
	}
	
	@Override
	public String getColumnName(int count) {
		// TODO Auto-generated method stub
		return Columns[count];
	}

	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
