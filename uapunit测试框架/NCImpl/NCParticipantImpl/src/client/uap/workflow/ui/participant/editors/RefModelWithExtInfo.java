package uap.workflow.ui.participant.editors;

import nc.ui.bd.ref.AbstractRefModel;

public class RefModelWithExtInfo extends AbstractRefModel{
	String defaultClause;
	public void addExtraWherePart(String pk_org){
		setWherePart(defaultClause+"and pk_org='"+pk_org+"'");
	}
}
