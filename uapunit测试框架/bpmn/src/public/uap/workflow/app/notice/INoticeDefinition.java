package uap.workflow.app.notice;

import java.util.List;
import java.util.Map;

/**
 * 通知接口 
 * 通知设计期时的接口
 * @author 
 */

public interface INoticeDefinition{
	  //通知的接收者
	  public List<ReceiverVO> getReceivers();
	  public void setReceivers(List<ReceiverVO> receivers);
	  
	  //通知的内容模板
	  public String getContentTemplate();
	  public void setContentTemplate(String contentTemplate);

	  //通知的类型
	  public INoticeType getNoticeType();
	  public void setNoticeType(INoticeType noticeType);
	  
	  //通知的时机
	  public NoticeTimeTypeEnum getNoticeTime();
	  public void setNoticeTime(NoticeTimeTypeEnum noticeTime);

	  //通知的发送条件
	  public String getCondition();
	  public void setCondition(String condition);

	  //发送的通知是否需要回执
	  public boolean getHasReceipt();
	  public void setHasReceipt(boolean hasReceipt);

	  //取通知的所有扩展属性
	  public Map<String,Object> getProperties();
	
	  //取通知的扩展属性
	  public Object getProperty(String key);
	
	  ///设置通知的扩展属性
	  public void setProperty(String key, Object value);
}