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
package uap.workflow.engine.entity;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.db.PersistentObject;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.jobexecutor.Job;
import uap.workflow.engine.jobexecutor.JobHandler;
import uap.workflow.engine.service.WfmServiceFacility;
/**
 * Stub of the common parts of a Job. You will normally work with a subclass of
 * JobEntity, such as {@link TimerEntity} or {@link MessageEntity}.
 * 
 * @author Tom Baeyens
 * @author Nick Burch
 * @author Dave Syer
 * @author Frederik Heremans
 */
public abstract class JobEntity implements Serializable, Job, PersistentObject {
	private static final long serialVersionUID = 1L;
	public static final boolean DEFAULT_EXCLUSIVE = true;
	protected boolean isExclusive = DEFAULT_EXCLUSIVE;
	public static final int DEFAULT_RETRIES = 3;
	private static final int MAX_EXCEPTION_MESSAGE_LENGTH = 255;
	protected String id;
	protected Date duedate;
	protected String lockOwner = null;
	protected Date lockExpirationTime = null;
	protected String executionId = null;
	protected String processInstanceId = null;
	protected int retries = DEFAULT_RETRIES;
	protected String jobHandlerType = null;
	protected String jobHandlerConfiguration = null;
	protected ByteArrayEntity exceptionByteArray;
	protected String exceptionByteArrayId;
	protected String exceptionMessage;
	protected int revision;
	public void execute(CommandContext commandContext) {
		// job的执行的真正的执行是利用jobhandelr去处理事情
		ActivityInstanceEntity execution = commandContext.getExecutionManager().getActInsByActInsPk(executionId);
		Map<String, JobHandler> jobHandlers = Context.getProcessEngineConfiguration().getJobHandlers();
		JobHandler jobHandler = jobHandlers.get(jobHandlerType);
		jobHandler.execute(jobHandlerConfiguration, execution, commandContext);
	}
	public void delete() {
		WfmServiceFacility.getJobBill().delete(id);
	}
	public void setExecution(IActivityInstance execution) {
		executionId = execution.getActInsPk();
		processInstanceId = execution.getProcessInstance().getProInsPk();
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public int getRetries() {
		return retries;
	}
	public void setRetries(int retries) {
		this.retries = retries;
	}
	public String getExceptionStacktrace() {
		String exception = null;
		ByteArrayEntity byteArray = getExceptionByteArray();
		if (byteArray != null) {
			try {
				exception = new String(byteArray.getBytes(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new WorkflowException("UTF-8 is not a supported encoding");
			}
		}
		return exception;
	}
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String claimedBy) {
		this.lockOwner = claimedBy;
	}
	public Date getLockExpirationTime() {
		return lockExpirationTime;
	}
	public void setLockExpirationTime(Date claimedUntil) {
		this.lockExpirationTime = claimedUntil;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public boolean isExclusive() {
		return isExclusive;
	}
	public void setExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getDuedate() {
		return duedate;
	}
	public void setDuedate(Date duedate) {
		this.duedate = duedate;
	}
	public void setExceptionStacktrace(String exception) {
		byte[] exceptionBytes = null;
		if (exception == null) {
			exceptionBytes = null;
		} else {
			try {
				exceptionBytes = exception.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new WorkflowException("UTF-8 is not a supported encoding");
			}
		}
		ByteArrayEntity byteArray = getExceptionByteArray();
		if (byteArray == null) {
			byteArray = new ByteArrayEntity("job.exceptionByteArray", exceptionBytes);
			Context.getCommandContext().getDbSqlSession().insert(byteArray);
			exceptionByteArrayId = byteArray.getId();
			exceptionByteArray = byteArray;
		} else {
			byteArray.setBytes(exceptionBytes);
		}
	}
	public String getJobHandlerType() {
		return jobHandlerType;
	}
	public void setJobHandlerType(String jobHandlerType) {
		this.jobHandlerType = jobHandlerType;
	}
	public String getJobHandlerConfiguration() {
		return jobHandlerConfiguration;
	}
	public void setJobHandlerConfiguration(String jobHandlerConfiguration) {
		this.jobHandlerConfiguration = jobHandlerConfiguration;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		if (exceptionMessage != null && exceptionMessage.length() > MAX_EXCEPTION_MESSAGE_LENGTH) {
			this.exceptionMessage = exceptionMessage.substring(0, MAX_EXCEPTION_MESSAGE_LENGTH);
		} else {
			this.exceptionMessage = exceptionMessage;
		}
	}
	public String getExceptionByteArrayId() {
		return exceptionByteArrayId;
	}
	private ByteArrayEntity getExceptionByteArray() {
		if ((exceptionByteArray == null) && (exceptionByteArrayId != null)) {
			exceptionByteArray = Context.getCommandContext().getDbSqlSession().selectById(ByteArrayEntity.class, exceptionByteArrayId);
		}
		return exceptionByteArray;
	}
	public Object getPersistentState() {
		Map<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("lockOwner", lockOwner);
		persistentState.put("lockExpirationTime", lockExpirationTime);
		persistentState.put("retries", retries);
		persistentState.put("exceptionMessage", exceptionMessage);
		if (exceptionByteArrayId != null) {
			persistentState.put("exceptionByteArrayId", exceptionByteArrayId);
		}
		return persistentState;
	}
	public int getRevisionNext() {
		return revision + 1;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
}
