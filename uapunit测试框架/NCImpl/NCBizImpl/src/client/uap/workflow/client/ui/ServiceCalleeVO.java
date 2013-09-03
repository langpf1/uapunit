package uap.workflow.client.ui;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wcj
 */
public class ServiceCalleeVO {

    private static String NODE_NAME_SERVICE = "nc:service";
    private static String NODE_NAME_TYPE = "type";
    private static String NODE_NAME_CLASS = "class";
    private static String NODE_NAME_METHOD = "method";
    private static String NODE_NAME_PARAMETERS = "parameters";
    private static String NODE_NAME_PARAMETER = "parameter";

    private int serviceType;
    private String serviceName;
    private String method;
    private String[] parameters;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParameters() {
        return parameters;
    }

    public void setParameters(String[] parameters) {
        this.parameters = parameters;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int type) {
        this.serviceType = type;
    }
/*
    public static String toXML(ServiceCalleeVO vo) throws Exception{
        Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement(NODE_NAME_SERVICE);

        Element node = doc.createElement(NODE_NAME_TYPE);
        node.setTextContent(String.valueOf(vo.getServiceType()));
        root.appendChild(node);

        node = doc.createElement(NODE_NAME_CLASS);
        node.setTextContent(vo.getServiceName());
        root.appendChild(node);

        node = doc.createElement(NODE_NAME_METHOD);
        node.setTextContent(vo.getMethod());
        root.appendChild(node);

        node = doc.createElement(NODE_NAME_PARAMETERS);
        for(int i = 0; i < vo.getParameters().length; i++){
            Node parameter = doc.createElement(NODE_NAME_PARAMETER);
            parameter.setTextContent(vo.getParameters()[i]);
            node.appendChild(parameter);
        }
        root.appendChild(node);
        doc.appendChild(root);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        //t.setOutputProperty(\"encoding\",\"GB23121\");//解决中文问题，试过用GBK不行
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        t.transform(new DOMSource(doc), new StreamResult(bos));
        String xmlStr = bos.toString();
        return xmlStr;
    }

    public static ServiceCalleeVO fromXML(String serviceString) throws Exception{
        Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(serviceString)));

        Node node = doc.getDocumentElement().getFirstChild();//type
        ServiceCalleeVO vo = new ServiceCalleeVO();
        while(node != null){
            if (node.getNodeName().equals(NODE_NAME_TYPE)){
                vo.setServiceType(Integer.parseInt(node.getTextContent()));
            }else if (node.getNodeName().equals(NODE_NAME_CLASS)){
                vo.setServiceName(node.getTextContent());
            }else if (node.getNodeName().equals(NODE_NAME_METHOD)){
                vo.setMethod(node.getTextContent());
            }else if (node.getNodeName().equals(NODE_NAME_PARAMETERS)){
                String[] parameters = new String[node.getChildNodes().getLength()];
                Node nodeParam = node.getFirstChild();
                int i = 0;
                while(nodeParam != null){
                    parameters[i++] = nodeParam.getTextContent();
                    nodeParam = nodeParam.getNextSibling();
                }
                vo.setParameters(parameters);
            }
            node = node.getNextSibling();
        }
        return vo;
    }
*/
}
