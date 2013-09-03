package uap.workflow.modeler.bpmn2.beaninfos;

import java.beans.BeanDescriptor;
import java.util.MissingResourceException;

public class GraphBeanDescriptor extends BeanDescriptor {
	private String displayName;

	  public GraphBeanDescriptor(GraphBeanInfo beanInfo) {
	    super(beanInfo.getType());
	    try {
	      //setDisplayName(beanInfo.getResources().getString("beanName"));
	      setDisplayName("beanName");
	    } catch (MissingResourceException e) {
	      // this resource is not mandatory
	    }
	    try {
	      //setShortDescription(beanInfo.getResources().getString("beanDescription"));
	      setShortDescription("beanDescription");
	    } catch (MissingResourceException e) {
	      // this resource is not mandatory
	    }
	  }

	  public String getDisplayName() {
	    return displayName;
	  }

	  public void setDisplayName(String p_name) {
	    displayName = p_name;
	  }
}
