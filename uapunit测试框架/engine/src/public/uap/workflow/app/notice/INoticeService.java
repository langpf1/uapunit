package uap.workflow.app.notice;

/** 
   通知运行时的对外服务接口，具体业务从此接口实现自己的具体服务类
   需要在配置文件中配置INoticeService的具体实现类是哪个   
 * @author 
 */

public interface INoticeService{

	/** 
	    通知发送信息
	 */
	public void sendNotice(NoticeContext noticeContext);
}