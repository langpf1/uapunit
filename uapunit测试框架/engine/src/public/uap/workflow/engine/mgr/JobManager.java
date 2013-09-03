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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import uap.workflow.engine.bridge.JobBridge;
import uap.workflow.engine.cfg.TransactionListener;
import uap.workflow.engine.cfg.TransactionState;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.entity.MessageEntity;
import uap.workflow.engine.entity.TimerEntity;
import uap.workflow.engine.jobexecutor.ExclusiveJobAddedNotification;
import uap.workflow.engine.jobexecutor.Job;
import uap.workflow.engine.jobexecutor.JobExecutor;
import uap.workflow.engine.jobexecutor.JobExecutorContext;
import uap.workflow.engine.jobexecutor.MessageAddedNotification;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.JobQueryImpl;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.util.ClockUtil;
import uap.workflow.engine.vos.JobVO;
/**
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class JobManager extends AbstractManager {
	// 这个是对messageentity的处理
	public void send(MessageEntity message) {
		JobVO jobVo = new JobBridge().convertT2M(message);
		WfmServiceFacility.getJobBill().asyn(jobVo);
		hintJobExecutor(message);
	}
	// 这个是对时间调度上的处理
	public void schedule(TimerEntity timer) {
		JobVO jobVo = new JobBridge().convertT2M(timer);
		WfmServiceFacility.getJobBill().asyn(jobVo);
		JobExecutor jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor();
		int waitTimeInMillis = jobExecutor.getWaitTimeInMillis();
		// 如果这个作业的到期时间小于当前时间+等待时间，说明这个作业需要马上加如作业执行器中
		if (timer.getDuedate().getTime() < (ClockUtil.getCurrentTime().getTime() + waitTimeInMillis)) {
			hintJobExecutor(timer);
		}
	}
	protected void hintJobExecutor(JobEntity job) {
		JobExecutor jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor();
		JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
		TransactionListener transactionListener = null;
		if (job.isExclusive() && jobExecutorContext != null && jobExecutorContext.isExecutingExclusiveJob()) {
			Date currentTime = ClockUtil.getCurrentTime();
			job.setLockExpirationTime(new Date(currentTime.getTime() + jobExecutor.getLockTimeInMillis()));
			job.setLockOwner(jobExecutor.getLockOwner());
			transactionListener = new ExclusiveJobAddedNotification(job.getId());
		} else {
			transactionListener = new MessageAddedNotification(jobExecutor);
		}
		Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.COMMITTED, transactionListener);
	}
	// 取消时间调度
	public void cancelTimers(ActivityInstanceEntity execution) {
		List<TimerEntity> timers = Context.getCommandContext().getJobManager().findTimersByExecutionId(execution.getActInsPk());
		for (TimerEntity timer : timers) {
			timer.delete();
		}
	}
	public JobEntity findJobById(String jobId) {
		JobVO jobVo = WfmServiceFacility.getJobQry().getJobByPk(jobId);
		if (jobVo == null) {
			return null;
		}
		return new JobBridge().convertM2T(jobVo);
	}
	public List<JobEntity> findNextJobsToExecute() {
		JobVO[] jobVos = WfmServiceFacility.getJobQry().selectNextJobsToExecute();
		List<JobEntity> list = new ArrayList<JobEntity>();
		if (jobVos == null || jobVos.length == 0) {
			return list;
		}
		for (int i = 0; i < jobVos.length; i++) {
			list.add(new JobBridge().convertM2T(jobVos[i]));
		}
		return list;
	}
	public List<JobEntity> findExclusiveJobsToExecute(String processInstanceId) {
		JobVO[] jobVos = WfmServiceFacility.getJobQry().findExclusiveJobsToExecute(processInstanceId);
		List<JobEntity> list = new ArrayList<JobEntity>();
		if (jobVos == null || jobVos.length == 0) {
			return list;
		}
		for (int i = 0; i < jobVos.length; i++) {
			list.add(new JobBridge().convertM2T(jobVos[i]));
		}
		return list;
	}
	public List<TimerEntity> findUnlockedTimersByDuedate(Date duedate) {
		JobVO[] jobVos = WfmServiceFacility.getJobQry().findUnlockedTimersByDuedate(duedate);
		List<TimerEntity> list = new ArrayList<TimerEntity>();
		if (jobVos == null || jobVos.length == 0) {
			return list;
		}
		for (int i = 0; i < jobVos.length; i++) {
			list.add((TimerEntity) new JobBridge().convertM2T(jobVos[i]));
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	public List<TimerEntity> findTimersByExecutionId(String executionId) {
		return getDbSqlSession().selectList("selectTimersByExecutionId", executionId);
	}
	@SuppressWarnings("unchecked")
	public List<Job> findJobsByQueryCriteria(JobQueryImpl jobQuery, Page page) {
		final String query = "selectJobByQueryCriteria";
		return getDbSqlSession().selectList(query, jobQuery, page);
	}
	@SuppressWarnings("unchecked")
	public List<Job> findJobsByConfiguration(String jobHandlerType, String jobHandlerConfiguration) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("handlerType", jobHandlerType);
		params.put("handlerConfiguration", jobHandlerConfiguration);
		return getDbSqlSession().selectList("selectJobsByConfiguration", params);
	}
	public long findJobCountByQueryCriteria(JobQueryImpl jobQuery) {
		return (Long) getDbSqlSession().selectOne("selectJobCountByQueryCriteria", jobQuery);
	}
}
