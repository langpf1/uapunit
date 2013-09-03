
package uap.workflow.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import uap.workflow.engine.itf.IExpressionValidate;
import uap.workflow.parameter.IParameter;

@SuppressWarnings("restriction")
public class ExprValidate implements IAction {

	private static final long serialVersionUID = -4655669655677319328L;
	private static final String ENCODING = "UTF-8";
	
	@Override
	public void perform(IParameter parameter) {
		HttpServletRequest req = parameter.getRequest();
		HttpServletResponse resp = parameter.getResponse();

		String expressionText = req.getParameter("expression");
		IExpressionValidate validate = NCLocator.getInstance().lookup(IExpressionValidate.class);
		String result = null;
		try{
			if(expressionText.trim().length()>0){
				ISecurityTokenCallback sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
				sc.token("LGW".getBytes(),"ncc10".getBytes());
				result = validate.verifyExpressionValid(expressionText);
			}else{
				result = "请输入表达式！";
			}
		}catch(Exception e){
			result = e.getMessage();
		}
		
		ServletOutputStream outStream = null;
		try {
			outStream = resp.getOutputStream();
		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		resp.setContentType("text/html;charset=" + ENCODING);
		resp.setCharacterEncoding(ENCODING);
		
		try {
			outStream.write(result.getBytes(ENCODING));
			outStream.flush();
			outStream.close();		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
