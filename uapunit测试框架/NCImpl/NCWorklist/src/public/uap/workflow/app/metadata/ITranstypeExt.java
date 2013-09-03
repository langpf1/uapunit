package uap.workflow.app.metadata;

public interface ITranstypeExt {

	static final String ATTRIBUTE_TRANSTYPEPK = "pk_transtype";
	
	static final String ATTRIBUTE_GROUPPK = "pk_group";
	
	String getPkGroupAttr();
	
	String getPkTranstypeAttr();
	
	void setPkTranstypeId(String pk_transtypeid);
	
}
