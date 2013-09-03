package uap.workflow.engine.el;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import nc.vo.pub.formulaset.core.ParseException;
import nc.vo.pub.formulaset.util.FormulaUtils;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFTime;
import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.session.WorkflowContext;

public final class CommonFunction {

	/*
	 * 合计函数
	 */
	public static Double max(Double...doubles){
		Double result = doubles[0];
		for(Double value : doubles){
			result = result > value ? result : value;
		}
		return result; 
	}
	
	public static Double min(Double...doubles){
		Double result = doubles[0];
		for(Double value : doubles){
			result = result < value ? result : value;
		}
		return result; 
	}
	
	public static Double avarvge(Double...doubles){
		Double result = doubles[0];
		for(Double value : doubles){
			result += value;
		}
		return result / doubles.length; 
	}
	
	public static Double sum(Double...doubles){
		Double result = 0.0;
		for(Double value : doubles){
			result += value;
		}
		return result; 
	}
	
	/*
	 * 串函数
	 */
	public static String subString(int start, int end, String string){
		return string.substring(start, end);
	}
	
	public static String concat(String string, String string1){
		return string.concat(string1);
	}
	
	public static Boolean contains(String sub, String string){
		return string.contains(sub);
	}
	
	public static String replace(String sub, String string){
		return string.replaceAll(sub, string);
	}
		
	public static String valueof(Object object){
		return String.valueOf(object);
	}
	
	public static char charAt(String str, int index){
		return str.charAt(index);
	}
	
//	public static corate( )
	public static boolean endsWith(String str, String suffix){
		return str.endsWith(suffix);
	}
	
	public static boolean equalsIgnoreCase(String str , String anotherString){
		return str.equalsIgnoreCase(anotherString);
	}
	
	public static int indexof(String str, String subString){
		return str.indexOf(subString);
	}
	public static boolean isempty(String str){
		return str.isEmpty();
	}
	public static int lastindexof(String str, String subString){
		return str.lastIndexOf(subString);
	}
	public static String left(String str, int endIndex){
		return str.substring(0, endIndex);
	}
	public static String leftStr(String str, int length, String subString){
		String tempStr = str;
		
		if (tempStr.length() < 1)
			subString = " ";
		
		while(tempStr.length() < length)
			tempStr += subString;
		
		tempStr = tempStr.substring(length);
		return tempStr;
	}
	public static int length(String str){
		return str.length();
	}
	public static String mid(String str, int beginIndex, int endIndex ){
		return str.substring(beginIndex, endIndex);
	}
	public static String right(String str, int index){
		return str.substring(index, str.length()-1);
	}
	public static String rightStr(String str, int length, String subString){
		String tempStr = str;
		
		if (tempStr.length() < 1)
			subString = " ";
		
		while(tempStr.length() < length)
			tempStr += subString;
		
		tempStr = tempStr.substring(length);
		return tempStr;
	}
	public static boolean startswith(String str, String prefix){
		return str.startsWith(prefix);
	}
	public static BigDecimal todecimal(String str){
		return BigDecimal.valueOf(Double.valueOf(str));
	}
	public static String toLowerCase(String str ){
		return str.toLowerCase();
	}
	public static String tostring(Object obj){
		return obj.toString();
	}
	public static String touppercase(String str ){
		return str.toUpperCase();
	}
	public static String trimzero(String str){
		return str.trim();
	}

	
	/*
	 * 日期函数
	 */
	public static Integer compareDate(Object date1, Object date2, Object field){
		if (FormulaUtils.isParamNull(date1) || FormulaUtils.isParamNull(date2) || FormulaUtils.isParamNull(field)) {
			throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("formulaparse",
					"UPPformulaparse-000077")/* @res "参数值为空（null）， 不能进行比较运算" */);
		}

		UFDateTime time1 = null;
		UFDateTime time2 = null;

		if (date1 instanceof String) {
			try {
				date1 = FormulaUtils.toDateOrDateTime(date1.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (date2 instanceof String) {
			try {
				date2 = FormulaUtils.toDateOrDateTime(date2.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (date1 instanceof UFDateTime) {
			time1 = (UFDateTime) date1;
		} else if (date1 instanceof UFDate) {
			time1 = new UFDateTime((UFDate) date1, null);
		} else if (date1 instanceof UFTime) {
			time1 = new UFDateTime(null, (UFTime) date1);
		}

		if (date2 instanceof UFDateTime) {
			time2 = (UFDateTime) date2;
		} else if (date2 instanceof UFDate) {
			time2 = new UFDateTime((UFDate) date2, null);
		} else if (date2 instanceof UFTime) {
			time2 = new UFDateTime(null, (UFTime) date2);
		}

		if (time1 == null || time2 == null) {
			throw new RuntimeException("Invalid parameter type");
		}

		if (field instanceof String) {
			if (field.toString().equals("Y")) {
				return Integer.valueOf(time1.getYear() - time2.getYear());
			} else if (field.toString().equals("M")) {
				int year = (time1.getYear() - time2.getYear()) * 12;
				int month = time1.getMonth() - time2.getMonth();
				return Integer.valueOf(year + month);
			} else if (field.toString().equals("D")) {
				return Integer.valueOf(UFDateTime.getDaysBetween(time2, time1));
			} else if (field.toString().equals("H")) {
				return Integer.valueOf(UFDateTime.getHoursBetween(time2, time1));
			} else if (field.toString().equals("m")) {
				return Integer.valueOf(UFDateTime.getMinutesBetween(time2, time1));
			} else if (field.toString().equals("S")) {
				return Integer.valueOf(UFDateTime.getSecondsBetween(time2, time1));
			} else {
				throw new RuntimeException("Invalid parameter format");
			}
		}

		throw new RuntimeException("Invalid parameter type");
	}
	public static Object date(){
		return UFDate.getDate(new java.util.Date());
	}
	
	private static void addOperation(Calendar calendar, Object paraField, int value){
		if (paraField instanceof String) {
			if (paraField.toString().equals("Y")) {
				calendar.add(Calendar.YEAR, value);
			} else if (paraField.toString().equals("M")) {
				calendar.add(Calendar.MONTH, value);
			} else if (paraField.toString().equals("D")) {
				calendar.add(Calendar.DAY_OF_MONTH, value);
			} else if (paraField.toString().equals("H")) {
				calendar.add(Calendar.HOUR_OF_DAY, value);
			} else if (paraField.toString().equals("m")) {
				calendar.add(Calendar.MINUTE, value);
			} else if (paraField.toString().equals("S")) {
				calendar.add(Calendar.SECOND, value);
			} else {
				throw new RuntimeException("Invalid parameter format");
			}
		}
	}

	public static Object dateAdd(Object param1, Object param2, Object param3){
		if (FormulaUtils.isParamNull(param1) || FormulaUtils.isParamNull(param2) || FormulaUtils.isParamNull(param3)) {
			throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("formulaparse",
					"UPPformulaparse-000084")/* @res "参数值为空（null）， 不能进行数值运算" */);
		}

		// param1 转化为时间
		if (param1 instanceof String) {
			try {
				param1 = FormulaUtils.toDateOrDateTime(param1.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		GregorianCalendar calendar = null;
		int value = Integer.parseInt(param2.toString());
		if (param1 instanceof UFDateTime) {
			UFDateTime time = (UFDateTime) param1;
			calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay(), time.getHour(), time
					.getMinute(), time.getSecond());
			addOperation(calendar, param3, value);
			return new UFDateTime(calendar.getTime());
		} else if (param1 instanceof UFDate) {
			UFDate time = (UFDate) param1;
			calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay());
			addOperation(calendar, param3, value);
			return UFDate.getDate(calendar.getTime());
		} else if (param1 instanceof UFTime) {
			UFDateTime time = new UFDateTime(null, (UFTime) param1);
			calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay(), time.getHour(), time
					.getMinute(), time.getSecond());
			addOperation(calendar, param3, value);
			return new UFTime(calendar.getTime());
		}
		throw new RuntimeException("Invalid parameter type");
	}
	
	public static Object formatDate(Object param1, Object param2){
		if (FormulaUtils.isParamNull(param1) || FormulaUtils.isParamNull(param2))
			throw new RuntimeException("For DateFormat(date,pattern),Parameters can't be null!");

		// param1 转化为时间
		if (param1 instanceof String) {
			try {
				param1 = FormulaUtils.toDateOrDateTime(param1.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (param1 instanceof UFDate) {
			UFDate time = (UFDate) param1;
			GregorianCalendar calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay());
			java.text.DateFormat formatter = new SimpleDateFormat(param2.toString());
			return formatter.format(calendar.getTime());
		} else if (param1 instanceof UFDateTime) {
			UFDateTime time = (UFDateTime) param1;
			GregorianCalendar calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay(), time
					.getHour(), time.getMinute(), time.getSecond());
			java.text.DateFormat formatter = new SimpleDateFormat(param2.toString());
			return formatter.format(calendar.getTime());
		} else if (param1 instanceof UFTime) {
			UFDateTime time = new UFDateTime(null, (UFTime) param1);
			GregorianCalendar calendar = new GregorianCalendar(time.getYear(), time.getMonth() - 1, time.getDay(), time
					.getHour(), time.getMinute(), time.getSecond());
			java.text.DateFormat formatter = new SimpleDateFormat(param2.toString());
			return formatter.format(calendar.getTime());
		}
		throw new RuntimeException("Invalid parameter type");
	}
	public static Object datetime(){
		return new UFDateTime(new Date());
	}
	public static Object dayof(Object datetime ){
		return datetime.toString().trim().substring(8);
		
	}
//	formataddress( )
//	loginbusidate()

//	mon()
//	monof( )
	public static Object time(){
		return new UFTime(new Date());
	}
	public static int year(){
		UFDate date = UFDate.getDate(new java.util.Date());
		return date.getYear();
	}
	public static Object yearof(Object date) {
		if (!isDateType(date))
			throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("formulaparse",
					"UPPformulaparse-000093"));
		return date.toString().trim().substring(0, 4);
	}
	public static Object todate(Object date) {
		if (FormulaUtils.isParamNull(date))
			// throw new ParseException("For toDate(date),Parameter can't be
			// null!");
			return null;

		if (date instanceof String) {
			try {
				return FormulaUtils.toDateOrDateTime(date.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		} else if (date instanceof UFDate) {
			return (UFDate) date;
		} else if (date instanceof UFDateTime) {
			return ((UFDateTime) date).getDate();
		} else if (date instanceof Date) {
			return UFDate.getDate((Date) date);
		} else {
			throw new RuntimeException("Invalid parameter type");
		}
	}

	public static Object todatetime(Object date) {
		if (FormulaUtils.isParamNull(date))
			throw new RuntimeException("For toDateTime(date),Parameter can't be null!");

		if (date instanceof String) {
			Object datetime = null;
			try {
				boolean bdate = date.toString().indexOf("-") > 0;
				boolean btime = date.toString().indexOf(":") > 0;
				if (bdate && btime)
					datetime = new UFDateTime(date.toString());
				else if (bdate)
					datetime = new UFDateTime(date.toString() + " " + new UFTime(new Date()).toString());
				else if (btime)
					datetime = new UFDateTime(new UFDate(new java.util.Date()).toString() + " " + date.toString());
			} catch (RuntimeException e) {
				throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("formulaparse",
						"UPPformulaparse-000166")/* @res "错误：时间格式不对！" */);
			}
			return datetime;
		} else {
			return new RuntimeException("Invalid parameter type");
		}
	}	
	public static Object toTime(Object date){
		if (FormulaUtils.isParamNull(date))
			throw new RuntimeException("For toTime(date),Parameter can't be null!");

		if (date instanceof String) {
			try {
				return new UFTime(date.toString());
			} catch (RuntimeException e) {
				throw new RuntimeException(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("formulaparse",
						"UPPformulaparse-000166")/* @res "错误：时间格式不对！" */);
			}
		} else if (date instanceof UFDateTime) {
			return (UFDateTime) date;
		} else if (date instanceof UFDate) {
			return new UFDateTime((UFDate) date, null);
		} else if (date instanceof Date) {
			return new UFDateTime(new UFDate((Date) date), null);
		} else {
			return new RuntimeException("Invalid parameter type");
		}
	}
	
    private static boolean isDateType(Object obj)
    {
        if (obj == null) return false;
        if (obj instanceof UFDate) return true;
        if (obj instanceof String)
        {
            String date = obj.toString().trim();
            if (obj == null || obj.toString().trim().length() != 10)
                return false;
            if (date.charAt(4) != '-' || date.charAt(7) != '-')
                return false;
            try
            {
                int year = Integer.parseInt(date.substring(0, 4));
                int mon = Integer.parseInt(date.substring(5, 7));
                if (mon < 1 || mon > 12)
                    return false;
                int day = Integer.parseInt(date.substring(8));
                if (day < 1 || day > 31)
                    return false;
                if ((mon == 4 || mon == 6 || mon == 9 || mon == 11) && day > 30)
                    return false;
                if (mon == 2)
                {
                    if (isLeapYear(year))
                    {
                        if (day > 29)
                            return false;
                    }
                    else
                    {
                        if (day > 28)
                            return false;
                    }
                }

            }
            catch (Exception ex)
            {
                return false;
            }
            return true;
        }
        return false;
    }
    private static boolean isLeapYear(int year)
    {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0))
            return true;
        else
            return false;

    }

    
	//数学
//	abs( )
//	acos( )
//	acosh( )
//	add( , )
//	angle( , )
//	asin( )
//	asinh( )
//	atan( )
//	atanh( )
//	cos( )
//	cosh( )
//	div( , )
//	exp( )
//	getresult()
//	int( )
//	ln( )
//	log( )
//	max( , )
//	min( , )
//	mod( , )
//	mul( , )
//	rand()
//	round( , )
//	sgn( )
//	sin( )
//	sinh( )
//	sqrt( )
//	sub( , )
//	sum()
//	tan( )
//	tanh( )
//	tonumber( )
//	zeroifnull( )

    public static Object iif(boolean condition, Object trueClause, Object falseClause){
    	if (condition)
    		return trueClause;
    	else
    		return falseClause;
    }
    
}
