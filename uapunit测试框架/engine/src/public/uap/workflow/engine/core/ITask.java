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
package uap.workflow.engine.core;
import java.util.Date;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.task.TaskDefinition;
/**
 * Represents one task for a human user.
 * 
 * @author Joram Barrez
 */
public interface ITask extends VariableScope {
	int PRIORITY_MINIUM = 0;
	int PRIORITY_NORMAL = 50;
	int PRIORITY_MAXIMUM = 100;
	String getTaskPk();
	void setTaskPk(String taskPk);
	String getName();
	void setName(String name);
	String getDescription();
	void setDescription(String description);
	Date getDueDate();
	void setDueDate(Date dueDate);
	ITask getParentTask();
	TaskInstanceCreateType getCreateType();
	boolean isExe();
	String getBeforeaddsign_times();
	void setParentTask(ITask task);
	void setTaskDefinition(TaskDefinition taskDefinition);
	TaskDefinition getTaskDefinition();
	void setExecution(IActivityInstance execution);
	IActivityInstance getExecution();
	void setOwner(String owner);
	String getOwner();
	void setExecutionVariables(Map<String, Object> parameters);
	Map<String, VariableInstanceEntity> getVariableInstances();
	void next(boolean isPass, String opinion);
	void reject(String toActivityID);
	void callBack();
	void updateProgress(String finish, String opinion);
	void update(ITask task);
	TaskInstanceStatus getStatus();
	void setStatus(TaskInstanceStatus status);
	void delete();
	void delegate(List<String> userIds);
	void fireEvent(String taskEventName);
	String getCreater();
	String getAgenter();
	String getExecuter();
	Date getCreateTime();
	Date getFinishTime();
	void setFinishTime(Date finishTime);
	void asyn();
	void setPk_form_ins(String pk_form_ins);
	void setPk_form_ins_version(String pk_form_ins_version);
	String getPk_form_ins_version();
	String getPk_form_ins();
	IProcessDefinition getProcessDefinition();
	String getUserobject();
	void setUserobject(String userobject);
	String getOpenUIStyle();
	void setOpenUIStyle(String openUIStyle);
	String getOpenURI();
	void setOpenURI(String openURI);
	IProcessInstance getProcessInstance();
}
