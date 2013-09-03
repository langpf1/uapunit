package uap.workflow.modeler.uecomponent;

import java.io.Serializable;

import uap.workflow.modeler.core.IBeanInfoProvider;

/**
 * @author chengsc
 * 
 */
public interface IUfGraphUserOjbect extends Serializable, IBeanInfoProvider {
	/**
	 * @return
	 */
	public Object replicate();

	/**
	 * @param busiPk
	 */
	public void setBusinessPk(String busiPk);

	/**
	 * @return
	 */
	public String getBusinessPk();

	/**
	 * @return
	 */
	public String getDisplayName();

	/**
	 * @return
	 */
	public Object getMappingObject();

	/**
	 * @param pk_group
	 */
	public void setPk_group(String pk_group);

	/**
	 * @return
	 */
	public String getPk_group();

	/**
	 * @param o
	 */
	public void cellValueChanged(Object o);
	
	/**
	 * ะฃั้
	 * */
	public boolean isValidity();
}
