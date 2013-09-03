package uap.workflow.engine.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.logger.WorkflowLogger;
import nc.vo.pub.lang.UFDateTime;
public class DateUtil {
	public static Date convert(UFDateTime datetime) {
		if (datetime == null) {
			return null;
		}
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime.toString());
		} catch (ParseException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e);
		}
		return date;
	}
}
