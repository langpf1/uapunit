package uap.workflow.modeler.uecomponent;

/**
 * @author chengsc
 *
 */
public interface GraphElementMeta {
	
	/**
	 * @return
	 */
	public String getUserObjectClass();
	
	/**
	 * @return
	 */
	public String getStyle();
	
	/**
	 * @return
	 */
	public boolean isVertex();
	
	/**
	 * @return
	 */
	public String getName();
	
	/**
	 * @return
	 */
	public int getWidth();
	
	/**
	 * @return
	 */
	public int getHeight();
	
	/**
	 * @return
	 */
	public String getImageURL();

	public String getNotationGroup();
	
	public String getSubClass();
}
