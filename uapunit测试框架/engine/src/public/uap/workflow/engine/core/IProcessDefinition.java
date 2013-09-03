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
import java.util.List;
import java.util.Map;

import uap.workflow.engine.form.StartFormHandler;
import uap.workflow.engine.task.TaskDefinition;
/**
 * @author Tom Baeyens
 */
public interface IProcessDefinition extends IScope {
	/**
	 * ���̶���汾
	 * 
	 */
	int getVersion();
	void setVersion(int version);
	/**
	 * ���̶�����Ϣ����
	 * 
	 */
	String getResourceName();
	void setResourceName(String resourceStr);
	/**
	 * ���̶���λͼ����
	 * 
	 */
	String getDiagramName();
	void setDiagramName(String diagramName);
	/**
	 * ���̶�����Ϣ
	 * 
	 * @param str
	 */
	byte[] getResourceBytes();
	void setResourceBytes(byte[] bytes);
	/**
	 * ���̶���λͼ��Ϣ
	 * 
	 */
	void setDiagramBytes(byte[] bytes);
	byte[] getDiagramBytes();
	/**
	 * ���̶�������
	 * 
	 */
	void setName(String name);
	String getName();
	/**
	 * 
	 * @return
	 */
	String getDeploymentId();
	void setDeploymentId(String deploymentId);
	/**
	 * id
	 * 
	 * @return
	 */
	String getProDefId();
	String setProDefId(String proDefId);
	/**
	 * pk
	 * 
	 * @param proDefPk
	 */
	void setProDefPk(String proDefPk);
	String getProDefPk();
	/**
	 * ����
	 * 
	 * @param category
	 */
	void setCategory(String category);
	String getCategory();
	/**
	 * desc
	 * 
	 * @return
	 */
	String getDescription();
	void setDescription(String desc);
	/**
	 * ������
	 * 
	 * @param taskDefinitions
	 */
	void setTaskDefinitions(Map<String, TaskDefinition> taskDefinitions);
	Map<String, TaskDefinition> getTaskDefinitions();
	boolean isPublic();
	void setPublic(boolean isPublic);
	/**
	 * 
	 * @return
	 */
	StartFormHandler getStartFormHandler();
	void setStartFormHandler(StartFormHandler startFormHandler);
	/**
	 * 
	 * @return
	 */
	IActivity getInitial();
	void setInitial(IActivity initial);
	/**
	 * 
	 * @return
	 */
	int getSuspensionState();
	void setSuspensionState(int suspensionState);
	/**
	 * 
	 * @return
	 */
	IProcessInstance createProcessInstance();
	IProcessInstance createProcessInstance(IActivity startActivity);
	/**
	 * 
	 * @param startActivity
	 * @return
	 */
	List<IActivity> getInitialActivityStack(IActivity startActivity);
	boolean isSuspended();
	void setStartFormKey(boolean hasStartFormKey);
	void setPk_group(String pk_group);
	String getPk_group();
	String getPk_bizobject();
	void setPk_bizobject(String pk_bizobject);
	String getPk_biztrans();
	void setPk_biztrans(String pk_biztrans);
	int getValidity();
	void setValidity(int validity);

}
