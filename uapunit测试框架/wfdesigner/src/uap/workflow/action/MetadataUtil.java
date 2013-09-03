
package uap.workflow.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.md.MDBaseQueryFacade;
import nc.md.MDBizQueryFacade;
import nc.md.model.IAttribute;
import nc.md.model.IBean;
import nc.md.model.IBusinessEntity;
import nc.md.model.IComponent;
import nc.md.model.IOpInterface;
import nc.md.model.IOperation;
import nc.md.model.IParameter;
import nc.md.model.MetaDataException;
import nc.md.model.impl.MDBean;
import nc.md.model.type.ICollectionType;
import nc.md.model.type.IType;
import nc.md.model.type.impl.RefType;
import nc.md.util.MDUtil;


@SuppressWarnings("restriction")
public class MetadataUtil implements IAction {

	private static final long serialVersionUID = -3037863229785310434L;
	private static final String FUNCTION = "function";
	private static final String ACTION_LOAD_PROPERTY = "loadProperty";
	private static final String ACTION_LOAD_PROPERTYALL = "loadPropertyAll";
	private static final String ACTION_LOAD_PROPERTYSUB = "loadPropertySub";
	private static final String ACTION_LOAD_OPERATION = "loadOperation";
	private static final String METATYPE = "type";
	//private static final String METATYPE_PROPERTY = "property";
	//private static final String METATYPE_OPERATION = "operation";
	private static final String ENCODING = "UTF-8";

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter(FUNCTION);
		String metaType = req.getParameter(METATYPE);

		ServletOutputStream stream = resp.getOutputStream();
		resp.setContentType("text/html;charset=" + ENCODING);
		resp.setCharacterEncoding(ENCODING);
		String out = getMetadata(action, metaType); 
		
		stream.write(out.getBytes(ENCODING));
		stream.flush();
		stream.close();
	}
	
	
	private String getMetadata(String action, String metadata){
		String out = ""; 
		StringBuffer json = new StringBuffer();
		if (action.equalsIgnoreCase(ACTION_LOAD_PROPERTY)){
			IBean ibe = getBeanMetadata(metadata);
			buildJSONByEntity(json, (IBusinessEntity)ibe);
			out = "[";//"<pre>["; 
			out += json.toString();
			if (out.endsWith(",\n"))
				out = out.substring(0, out.length()-2);
			out += "]";//"]</pre>";
		}else if (action.equalsIgnoreCase(ACTION_LOAD_PROPERTYALL)){
			buildJSONByModule(json, metadata);
			out = json.toString();
		}else if (action.equalsIgnoreCase(ACTION_LOAD_PROPERTYSUB)){
			
			try {
				String classType = metadata;
				IBean ibe = MDBaseQueryFacade.getInstance().getBeanByFullClassName(classType);
				buildJSONByEntity(json, (IBusinessEntity)ibe);
				out = "[" + json.toString();
				if (out.endsWith(",\n"))
					out = out.substring(0, out.length()-2);
				out += "]";//"</pre>";
			} catch (MetaDataException e) {
				e.printStackTrace();
			}
		}else if (action.equalsIgnoreCase(ACTION_LOAD_OPERATION)){
			buildOperationByEntity(json, metadata);
			out = json.toString();
		}
		return out;
	}

	
	private IBean getBeanMetadata(String componentName){

		IBean ibe = null;
		try {
			ibe = MDBaseQueryFacade.getInstance().getBeanByFullClassName(componentName);
			if (ibe != null)
				return ibe;
		} catch (MetaDataException e1) {
			e1.printStackTrace();
		}
		
		IComponent comp = null;
		try {
			comp = MDBaseQueryFacade.getInstance().getComponentByName(componentName);
		} catch (MetaDataException e) {
			e.printStackTrace();
		}
		
		if (comp == null){
			try {
				comp = nc.uap.pf.metadata.PfMetadataTools.queryComponentOfBilltype(componentName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return comp == null ? null : comp.getPrimaryBusinessEntity();
	}

	private static final String JSON_ID = "\"id\"";
	private static final String JSON_TEXT = "\"text\"";
	private static final String JSON_ATTRIBUTES = "\"attributes\"";
	private static final String JSON_CHILDREN = "\"children\"";
	private static final String JSON_NAME = "\"name\"";
	private static final String JSON_DISPLAYNAME = "\"displayName\"";
	private static final String JSON_CLASSNAME = "\"className\"";
	private static final String JSON_COLON = ":";
	private static final String JSON_COMMA = ",";
	private static final String JSON_RETURN = "\n";
	private static final String JSON_QUOTATION = "\"";
	
	private void buildJSONByEntity(StringBuffer json, IBusinessEntity ibe){
		List<IAttribute> ibeanList = ibe.getAttributes();
		for (IAttribute attribute : ibeanList) {
			if (!attribute.isHide()) {
				json.append("{");
				json.append(JSON_ID).append(JSON_COLON).append(JSON_QUOTATION).append(attribute.getID()).append(JSON_QUOTATION).append(JSON_COMMA).append(JSON_RETURN);
				json.append(JSON_TEXT).append(JSON_COLON).append(JSON_QUOTATION).append(attribute.getDisplayName()).append(JSON_QUOTATION).append(JSON_COMMA).append(JSON_RETURN);
				json.append(JSON_ATTRIBUTES).append(JSON_COLON).append("{");
				json.append(JSON_NAME).append(JSON_COLON).append(JSON_QUOTATION).append(attribute.getName()).append(JSON_QUOTATION).append(JSON_COMMA);
				json.append(JSON_DISPLAYNAME).append(JSON_COLON).append(JSON_QUOTATION).append(attribute.getDisplayName()).append(JSON_QUOTATION);

				if (MDUtil.isRefType(attribute.getDataType()) || MDUtil.isMDBean(attribute.getDataType())
						|| MDUtil.isCollectionType(attribute.getDataType())) {
					json.append(JSON_COMMA);
					if(MDUtil.isRefType(attribute.getDataType()))
						json.append(JSON_CLASSNAME).append(JSON_COLON).append(JSON_QUOTATION).append(((RefType)attribute.getDataType()).getRefType().getFullClassName()).append(JSON_QUOTATION);
					else if (MDUtil.isCollectionType(attribute.getDataType()))
						json.append(JSON_CLASSNAME).append(JSON_COLON).append(JSON_QUOTATION).append(((ICollectionType) attribute.getDataType()).getElementType().getFullClassName()).append(JSON_QUOTATION);
					else if (MDUtil.isMDBean(attribute.getDataType()))
						json.append(JSON_CLASSNAME).append(JSON_COLON).append(JSON_QUOTATION).append(((MDBean)attribute.getDataType()).getFullClassName()).append(JSON_QUOTATION);
					json.append("}").append(JSON_COMMA).append(JSON_RETURN);
					json.append(JSON_CHILDREN).append(JSON_COLON).append("[{").append(JSON_TEXT).append(JSON_COLON).append("\"loading ...\"}]");
					json.append(",\n\"state\":\"closed\"\n},\n");
				}else 
					json.append("}").append(JSON_RETURN).append("}").append(JSON_COMMA).append(JSON_RETURN);
			}
		}
	}
	
	private void buildJSONByModule(StringBuffer json, String metadata){
		List<IComponent> components = null;
		int compCount = 0;
		try {
			components = MDBaseQueryFacade.getInstance().getAllComponentsByModuleID(metadata);
			json.append("components:[");
			for(IComponent component : components){
				if (compCount > 0)
					json.append(",");
				
				json.append("{").append("id:").append(component.getID()).append(",name:").append(component.getName()).append(",displayName:").append(component.getDisplayName()).append("}");
				compCount++;
			}
		} catch (MetaDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void buildOperationByEntity(StringBuffer json, String entityID){
		List<IOpInterface> opInterfaces = null;
		IBean ibe = null;
		try {
			ibe = MDBaseQueryFacade.getInstance().getBeanByFullClassName(entityID);
		} catch (MetaDataException e1) {
			e1.printStackTrace();
		}
		if (ibe == null){
			json.append("不能找到相应的类，请检查正确性！");
			return;
		}
		int itfCount = 0, operCount = 0, paraCount = 0;
		try {
			opInterfaces = MDBizQueryFacade.getInstance().getOpInterfacesByBeanID(ibe.getID());
			json.append("interfaces:[");
			for(IOpInterface opInterface : opInterfaces){
				if (itfCount > 0)
					json.append(",\n");
				json.append("{").append("id:'").append(opInterface.getID()).append("',name:'").append(opInterface.getName()).append("',displayName:'").append(opInterface.getDisplayName()).append("',operations:[");
				List<IOperation> operations = opInterface.getOperations();
				operCount = 0;
				for(IOperation operation : operations){
					if (operCount > 0)
						json.append(",\n");
					json.append("{").append("id:'").append(operation.getID()).append("',name:'").append(operation.getName()).append("',displayName:'").append(operation.getDisplayName()).append("',parameters:[");
					List<IParameter> parameters = operation.getParameters();
					paraCount = 0;
					if(parameters!=null)
					{
					  for(IParameter parameter : parameters){
						if (paraCount > 0)
							json.append(",\n");
						IType type = parameter.getDataType();
						json.append("{").append("type:'").append(type.getClass()).append("',name:'").append(parameter.getName()).append("',displayName:'").append(parameter.getDisplayName()).append("'}");
						paraCount++;
					  }
					}
					json.append("]}");
					operCount++;
				}
				json.append("]}");
				itfCount++;
			}
			json.append("]");
		} catch (MetaDataException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void perform(uap.workflow.parameter.IParameter parameter) {
		HttpServletRequest req = parameter.getRequest();
		HttpServletResponse resp = parameter.getResponse();
		
		String action = req.getParameter(FUNCTION);
		String metaType = req.getParameter(METATYPE);

		ServletOutputStream stream = null;
		String out = null;
		try {
			stream = resp.getOutputStream();
			resp.setContentType("text/html;charset=" + ENCODING);
			resp.setCharacterEncoding(ENCODING);
			out = getMetadata(action, metaType); 
		} catch (IOException e) {
			out = "\"error\":true,\"errorDesc\":\""+ e.getMessage() +"\"";
			e.printStackTrace();
		}		

		try {
			stream.write(out.getBytes(ENCODING));
			stream.flush();
			stream.close();		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
