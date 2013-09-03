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
package uap.workflow.engine.cmd;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


import uap.workflow.engine.bridge.JobBridge;
import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.jobexecutor.AcquiredJobs;
import uap.workflow.engine.jobexecutor.JobExecutor;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.util.ClockUtil;
import uap.workflow.engine.vos.JobVO;
/**
 * @author Nick Burch
 * @author Daniel Meyer
 */
public class AcquireJobsCmd implements Command<AcquiredJobs> {
	private static final long serialVersionUID = 1L;
	private final JobExecutor jobExecutor;
	public AcquireJobsCmd(JobExecutor jobExecutor) {
		this.jobExecutor = jobExecutor;
	}
	// 这个用来收集jobentity
	public AcquiredJobs execute(CommandContext commandContext) {
		String lockOwner = jobExecutor.getLockOwner();
		AcquiredJobs acquiredJobs = new AcquiredJobs();
		int jobsInThisAcquisition = 0;
		// 抓取需要处理的作业
		JobVO[] vos = WfmServiceFacility.getJobQry().selectNextJobsToExecute();
		if (vos == null) {
			return acquiredJobs;
		}
		for (int i = 0; i < vos.length; i++) {
			JobEntity job = new JobBridge().convertM2T(vos[i]);
			List<String> jobIds = new ArrayList<String>();
			if (job == null) {
				return acquiredJobs;
			}
			if (acquiredJobs.contains(job.getId())) {
				return acquiredJobs;
			}
			// 如果这个是作业是独占的作业，那么需要找到这个流程实列所有的作业，
			if (job.isExclusive()) {
				List<JobEntity> exclusiveJobs = commandContext.getJobManager().findExclusiveJobsToExecute(job.getProcessInstanceId());
				for (JobEntity exclusiveJob : exclusiveJobs) {
					// 给当前流程实列的其他作业添加过期时间
					lockJob(exclusiveJob, lockOwner, jobExecutor.getLockTimeInMillis());
					jobIds.add(exclusiveJob.getId());
				}
			} else {
				lockJob(job, lockOwner, jobExecutor.getLockTimeInMillis());
				jobIds.add(job.getId());
			}
			acquiredJobs.addJobIdBatch(jobIds);
			// 作业执行器每次处理的最大作业数是三，如果查出来的作业数大约三的话，就不再往作业执行器里面继续添加作业，防止内存泄露
			jobsInThisAcquisition += jobIds.size();// 这个保证是加出来的作业数
			// 说明已经超过最大作业数，不需要继续添加
			if (jobsInThisAcquisition >= jobExecutor.getMaxJobsPerAcquisition()) {
				break;
			}
		}
		return acquiredJobs;
	}
	protected void lockJob(JobEntity job, String lockOwner, int lockTimeInMillis) {
		job.setLockOwner(lockOwner);
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(ClockUtil.getCurrentTime());
		gregorianCalendar.add(Calendar.MILLISECOND, lockTimeInMillis);
		job.setLockExpirationTime(gregorianCalendar.getTime());
		WfmServiceFacility.getJobBill().asyn(new JobBridge().convertT2M(job));
	}
}
