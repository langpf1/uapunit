package uap.workflow.app.extend.action;

import java.util.Hashtable;

/**
 * 批动作处理脚本 的返回值
 * <li>用于控制是否进行动作驱动
 * 
 * @author 雷军 created on 2005-6-25
 */
public class BatchWorkflowRet implements IWorkflowBatch {
  private Hashtable m_noPassAndGoing = null;
  private Object m_userObj = null;

  /* (non-Javadoc)
   * @see nc.bs.pub.compiler.IWorkflowBatch#getNoPassAndGoing()
   */
  public Hashtable getNoPassAndGoing() {
    return m_noPassAndGoing;
  }

  /* (non-Javadoc)
   * @see nc.bs.pub.compiler.IWorkflowBatch#getUserObj()
   */
  public Object getUserObj() {
    return m_userObj;
  }

  public void setNoPassAndGoing(Hashtable passAndGoing) {
    this.m_noPassAndGoing = passAndGoing;
  }

  public void setUserObj(Object userObj) {
    this.m_userObj = userObj;
  }
}