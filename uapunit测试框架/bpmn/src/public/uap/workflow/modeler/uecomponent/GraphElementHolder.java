package uap.workflow.modeler.uecomponent;

import nc.vo.pub.SuperVO;

/**
 * @author chengsc
 *
 */
public class GraphElementHolder extends SuperVO{
	
	String pk_ufgraph,id,targetid,sourceid,graphstyle;
	int type;
	Object geometry,userobject;
	String pk_graph;
	String parentid;
	public String getPk_ufgraph() {
		return pk_ufgraph;
	}
	public void setPk_ufgraph(String pk_ufgraph) {
		this.pk_ufgraph = pk_ufgraph;
	}
	public String getGraphstyle() {
		return graphstyle;
	}
	public void setGraphstyle(String graphstyle) {
		this.graphstyle = graphstyle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTargetid() {
		return targetid;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getGeometry() {
		return geometry;
	}
	public void setGeometry(Object geometry) {
		this.geometry = geometry;
	}
	public Object getUserobject() {
		return userobject;
	}
	public void setUserobject(Object userobject) {
		this.userobject = userobject;
	}
	public String getPk_graph() {
		return pk_graph;
	}
	public void setPk_graph(String pk_graph) {
		this.pk_graph = pk_graph;
	}
	
	public String getPKFieldName() {
		return "pk_ufgraph";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nc.vo.pub.SuperVO#getTableName()
	 */
	public String getTableName() {
		return "pub_ufgraph";
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	
	
}
