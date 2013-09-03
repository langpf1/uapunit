
@javax.xml.bind.annotation.XmlSchema
(       namespace = NameSpaceConst.XMLNS_URL,
	    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED, 
		attributeFormDefault=javax.xml.bind.annotation.XmlNsForm.UNSET,
		xmlns = { 
		 @XmlNs(prefix = NameSpaceConst.BPMNDI_PREFIX, namespaceURI = NameSpaceConst.BPMNDI_URL),
		 @XmlNs(prefix = NameSpaceConst.OMGDC_PREFIX, namespaceURI = NameSpaceConst.OMGDC_URL),
		 @XmlNs(prefix = NameSpaceConst.OMGDI_PREFIX, namespaceURI =NameSpaceConst.OMGDI_URL),
		 @XmlNs(prefix = NameSpaceConst.BIZEX_PREFIX, namespaceURI =NameSpaceConst.BIZEX_URL)
		}
)
package uap.workflow.bpmn2.model.definition;
import javax.xml.bind.annotation.XmlNs;

import uap.workflow.bpmn2.model.NameSpaceConst;

