package uap.workflow.wfdplugin.listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * �¼�����
 * @author dengjt
 *
 */
public class EventConf extends ExtendAttributeSupport implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 5332995004954913304L;
	//�¼�����״̬
	public static final int NORMAL_STATUS = 1;
	//�¼�����״̬
	public static final int ADD_STATUS = 2;
	//�¼�ɾ��״̬
	public static final int DEL_STATUS = 3;
	public static final String SUBMIT_PRE = "SP_";
	public static final String PARAM_DATASET_FIELD_ID = "dataset_field_id";
	private String name = null;
	private String script = null;
	private String methodName = null;
	private boolean onserver = true;
	private boolean async = true;
	private boolean nmc = true;
	private String id;
	private List<LfwParameter> paramList = null;
	private List<LfwParameter> extendParamList = null;
	//private EventSubmitRule submitRule = null;
	private String controllerClazz = null;
	private String jsEventClaszz = null;
	//�¼�״̬
	private int eventStatus = NORMAL_STATUS;
	//���ļ�·��
	private String classFilePath = null;
	//���ļ�����
	private String classFileName = null;
	public EventConf(){}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getJsEventClaszz() {
		return jsEventClaszz;
	}

	public void setJsEventClaszz(String jsEventClaszz) {
		this.jsEventClaszz = jsEventClaszz;
	}

	public EventConf(String name, LfwParameter param, String script) {
		this.name = name;
		this.paramList = new ArrayList<LfwParameter>();
		paramList.add(param);
		this.script = script;
	}
	
	public List<LfwParameter> getParamList() {
		if(this.paramList == null)
			this.paramList = new ArrayList<LfwParameter>();
		return this.paramList;
	}

	public void setParamList(List<LfwParameter> paramList) {
		this.paramList = paramList;
	}

	public void addParam(LfwParameter param) {
		LfwParameter p = getParam(param.getName());
		if(p == null)
			getParamList().add(param);
	}
	
	public LfwParameter getParam(String name) {
		for(int i = 0; i < getParamList().size(); i++)
		{
			if(getParamList().get(i).getName().equals(name))
				return getParamList().get(i);
		}
		return null;
	}


	public void setExtendParamList(List<LfwParameter> extendParamList) {
		this.extendParamList = extendParamList;
	}

	public void addExtendParam(LfwParameter extendParam) {
		extendParamList.add(extendParam);
	}
	
	public LfwParameter getExtendParam(String name) {
		for(int i = 0; i < getExtendParamList().size(); i++)
		{
			if(extendParamList.get(i).getName().equals(name))
				return extendParamList.get(i);
		}
		return null;
	}

	public List<LfwParameter> getExtendParamList() {
		if(this.extendParamList == null)
			this.extendParamList = new ArrayList<LfwParameter>();
		return extendParamList;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Object clone() {
		EventConf eventConf = (EventConf) super.clone();
		eventConf.paramList = new ArrayList<LfwParameter>();
		eventConf.extendParamList = new ArrayList<LfwParameter>();
		for(int i = 0, n =  getParamList().size(); i < n; i++)
			eventConf.paramList.add((LfwParameter) getParamList().get(i).clone());
		for(int i = 0, n = getExtendParamList().size(); i < n; i++)
			eventConf.extendParamList.add((LfwParameter) getExtendParamList().get(i).clone());
		//if(submitRule != null)
		//	eventConf.submitRule = (EventSubmitRule) submitRule.clone();
		
		return eventConf;
	}

	public boolean isOnserver() {
		return onserver;
	}

	public void setOnserver(boolean onserver) {
		this.onserver = onserver;
	}

	/*
	public EventSubmitRule getSubmitRule() {
		return submitRule;
	}

	public void setSubmitRule(EventSubmitRule submitRule) {
		this.submitRule = submitRule;
	}
*/
	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getControllerClazz() {
		return controllerClazz == null ? "" : controllerClazz;
	}

	public void setControllerClazz(String controllerClazz) {
		this.controllerClazz = controllerClazz;
	}

	public int getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(int eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getClassFilePath() {
		return classFilePath;
	}

	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	public String getClassFileName() {
		return classFileName;
	}

	public void setClassFileName(String classFileName) {
		this.classFileName = classFileName;
	}
	public boolean isNmc() {
		return nmc;
	}
	public void setNmc(boolean nmc) {
		this.nmc = nmc;
	}
}
