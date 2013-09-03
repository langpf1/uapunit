/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uap.workflow.engine.mgr;

import java.util.Collections;
import java.util.Date;
import java.util.List;


import uap.workflow.engine.cfg.ProcessEngineConfigurationImpl;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.entity.HistoricTaskInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.history.HistoricTaskInstance;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.persistence.AbstractHistoricManager;
import uap.workflow.engine.query.HistoricTaskInstanceQueryImpl;
import uap.workflow.engine.query.Page;


/**
 * @author Tom Baeyens
 */
public class HistoricTaskInstanceManager extends AbstractHistoricManager {

  @SuppressWarnings("unchecked")
  public void deleteHistoricTaskInstancesByProcessInstanceId(String processInstanceId) {
    if (historyLevel>=ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      List<String> taskInstanceIds = (List<String>) getDbSqlSession().selectList("selectHistoricTaskInstanceIdsByProcessInstanceId", processInstanceId);
      for (String taskInstanceId: taskInstanceIds) {
        deleteHistoricTaskInstanceById(taskInstanceId);
      }
    }
  }

  public long findHistoricTaskInstanceCountByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
    if (historyLevel>ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      return (Long) getDbSqlSession().selectOne("selectHistoricTaskInstanceCountByQueryCriteria", historicTaskInstanceQuery);
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  public List<HistoricTaskInstance> findHistoricTaskInstancesByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery, Page page) {
    if (historyLevel>ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      return getDbSqlSession().selectList("selectHistoricTaskInstancesByQueryCriteria", historicTaskInstanceQuery, page);
    }
    return Collections.EMPTY_LIST;
  }
  
  public HistoricTaskInstanceEntity findHistoricTaskInstanceById(String taskId) {
    if (taskId == null) {
      throw new WorkflowException("Invalid historic task id : null");
    }
    if (historyLevel>ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      return (HistoricTaskInstanceEntity) getDbSqlSession().selectOne("selectHistoricTaskInstance", taskId);
    }
    return null;
  }
  
  public void deleteHistoricTaskInstanceById(String taskId) {
    if (historyLevel>ProcessEngineConfigurationImpl.HISTORYLEVEL_NONE) {
      HistoricTaskInstanceEntity historicTaskInstance = findHistoricTaskInstanceById(taskId);
      if(historicTaskInstance!=null) {
        CommandContext commandContext = Context.getCommandContext();
        
        commandContext
          .getHistoricDetailManager()
          .deleteHistoricDetailsByTaskId(taskId);
          
        commandContext
          .getCommentManager()
          .deleteCommentsByTaskId(taskId);
        
        commandContext
          .getAttachmentManager()
          .deleteAttachmentsByTaskId(taskId);
      
        getDbSqlSession().delete(HistoricTaskInstanceEntity.class, taskId);
      }
    }
  }

  public void markTaskInstanceEnded(String taskId, String deleteReason) {
    if (historyLevel>=ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.markEnded(deleteReason);
      }
    }
  }

  public void setTaskAssignee(String taskId, String assignee) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setAssignee(assignee);
      }
    }
  }

  public void setTaskOwner(String taskId, String owner) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, taskId);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setOwner(owner);
      }
    }
  }

  public void setTaskName(String id, String taskName) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, id);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setName(taskName);
      }
    }
  }

  public void setTaskDescription(String id, String description) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, id);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setDescription(description);
      }
    }
  }

  public void setTaskDueDate(String id, Date dueDate) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, id);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setDueDate(dueDate);
      }
    }
  }

  public void setTaskPriority(String id, int priority) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, id);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setPriority(priority);
      }
    }
  }

  public void setTaskParentTaskId(String id, String parentTaskId) {
    if (historyLevel >= ProcessEngineConfigurationImpl.HISTORYLEVEL_AUDIT) {
      HistoricTaskInstanceEntity historicTaskInstance = getDbSqlSession().selectById(HistoricTaskInstanceEntity.class, id);
      if (historicTaskInstance!=null) {
        historicTaskInstance.setParentTaskId(parentTaskId);
      }
    }
  }
}
