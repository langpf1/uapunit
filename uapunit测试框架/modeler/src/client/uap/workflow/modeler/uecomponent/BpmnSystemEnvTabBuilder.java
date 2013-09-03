package uap.workflow.modeler.uecomponent;

import java.util.HashMap;

import uap.workflow.modeler.utils.BmpnEvnParameterUtils;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;
import nc.vo.pub.formulaedit.FormulaItem;

class BpmnSystemEnvTabBuilder extends AbstractTabBuilder {
	HashMap<String, Object> hsFormulaItems = null;

	public BpmnSystemEnvTabBuilder(FormulaWordSorter fws, HashMap<String, Object> hsFormulaItems) {
		super(fws);
		this.hsFormulaItems = hsFormulaItems;
	}

	@Override
	public HashMap getHSFormulaItems() {
		return hsFormulaItems;

	}
	
	@Override
	public String getTabName() {
		return NCLangRes.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000036")/*系统变量*/;
	}

}