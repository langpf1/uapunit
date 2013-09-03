package uap.workflow.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uap.workflow.parameter.IParameter;
import uap.workflow.servlet.Constants;

/**
 * Servlet implementation class SaveServlet.
 * 
 * The SaveDialog in Dialogs.js implements the user interface. Editor.saveFile
 * in Editor.js implements the request to the server. Note that this request
 * is carried out in a separate iframe in order to allow for the response to
 * be handled by the browser. (This is required in order to bring up a native
 * Save dialog and save the file to the local filestyem.) Finally, the code in
 * this servlet echoes the XML and sends it back to the client with the
 * required headers (see Content-Disposition in RFC 2183).
 */
public class SaveFileAction implements IAction
{
	public void perform(IParameter parameter) {
		HttpServletRequest request = parameter.getRequest();
		HttpServletResponse response = parameter.getResponse();
		
		if (request.getContentLength() < Constants.MAX_REQUEST_SIZE)
		{
			String filename = request.getParameter("filename");
			String xml = request.getParameter("xml");

			if (xml != null && xml.length() > 0)
			{
				String format = request.getParameter("format");

				if (format == null)
				{
					format = "xml";
				}

				if (!filename.toLowerCase().endsWith("." + format))
				{
					filename += "." + format;
				}

				response.setContentType("application/xml");
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + filename + "\"");
				response.setStatus(HttpServletResponse.SC_OK);

				OutputStream out;
				try {
					out = response.getOutputStream();
					String encoding = request.getHeader("Accept-Encoding");
	
					// Supports GZIP content encoding
					if (encoding != null && encoding.indexOf("gzip") >= 0)
					{
						response.setHeader("Content-Encoding", "gzip");
						out = new GZIPOutputStream(out);
					}
	
					out.write(URLDecoder.decode(xml, "UTF-8").getBytes("UTF-8"));
					out.flush();
					out.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			else
			{
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE);
		}
	}

}
