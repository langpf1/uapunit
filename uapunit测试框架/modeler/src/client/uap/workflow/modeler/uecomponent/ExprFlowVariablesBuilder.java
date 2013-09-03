package uap.workflow.modeler.uecomponent;

import java.util.HashMap;

import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;

public class ExprFlowVariablesBuilder extends AbstractTabBuilder {

	private HashMap<String, Object> variables = null;
	public ExprFlowVariablesBuilder(FormulaWordSorter fws) {
		super(fws);
	}
	public ExprFlowVariablesBuilder(FormulaWordSorter fws, HashMap<String, Object> variables) {
		super(fws);
		this.variables = variables;
	}

	@Override
	public HashMap getHSFormulaItems() {
		return this.variables;
	}

	@Override
	public String getTabName() {
		return "流程变量";
	}

}
