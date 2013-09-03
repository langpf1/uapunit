package uap.workflow.modeler.uecomponent;

import java.util.HashMap;

import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;
import nc.vo.pub.formulaedit.FormulaItem;

public class ExprApproveResultVariables extends AbstractTabBuilder {

	private HashMap<String, Object> variables = null;
	public ExprApproveResultVariables(FormulaWordSorter fws) {
		super(fws);
	}

	public ExprApproveResultVariables(FormulaWordSorter fws, HashMap<String, Object> variables) {
		super(fws);
		this.variables = variables;
	}

	@Override
	public HashMap<String,Object> getHSFormulaItems() {
		return variables;
	}

	@Override
	public String getTabName() {
		return "…Û≈˙±‰¡ø";
	}

}
