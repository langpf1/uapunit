package uap.workflow.modeler.uecomponent;

import java.util.HashMap;

import nc.ui.pub.formulaedit.AbstractTabBuilder;
import nc.ui.pub.formulaedit.FormulaWordSorter;

public class ExprContextVariablesBuilder extends AbstractTabBuilder {

	private HashMap<String, Object> variables;
	public ExprContextVariablesBuilder(FormulaWordSorter fws) {
		super(fws);
	}
	public ExprContextVariablesBuilder(FormulaWordSorter fws, HashMap<String, Object> variables) {
		super(fws);
		this.variables = variables;
	}

	@Override
	public HashMap getHSFormulaItems() {
		return this.variables;
	}

	@Override
	public String getTabName() {
		return "上下文变量";
	}


}
