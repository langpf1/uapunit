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
package uap.workflow.engine.jobexecutor;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import uap.workflow.engine.cmd.AcquireJobsCmd;
import uap.workflow.engine.entity.TimerEntity;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.util.ClockUtil;
/**
 * 
 * @author Daniel Meyer
 */
public class AcquireJobsRunnable implements Runnable {
	protected final JobExecutor jobExecutor;
	protected volatile boolean isInterrupted = false;
	protected volatile boolean isJobAdded = false;
	protected final Object MONITOR = new Object();
	protected final AtomicBoolean isWaiting = new AtomicBoolean(false);
	public AcquireJobsRunnable(JobExecutor jobExecutor) {
		this.jobExecutor = jobExecutor;
	}
	public synchronized void run() {
		final CommandExecutor commandExecutor = jobExecutor.getCommandExecutor();
		long millisToWait = 0;
		while (!isInterrupted) {
			try {
				// 获取jobentity
				AcquiredJobs acquiredJobs = commandExecutor.execute(new AcquireJobsCmd(jobExecutor));
				for (List<String> jobIds : acquiredJobs.getJobIdBatches()) {
					jobExecutor.executeJobs(jobIds); // 批处理执行任务
				}
				{// 计算最小等待时间
					millisToWait = jobExecutor.getWaitTimeInMillis();// 5秒
					int batchSize = acquiredJobs.getJobIdBatches().size();
					int maxAcqusiz = jobExecutor.getMaxJobsPerAcquisition();
					if (batchSize < maxAcqusiz) {
						isJobAdded = false;
						long cntDate = ClockUtil.getCurrentTime().getTime();
						Date duedate = new Date(cntDate + millisToWait);
						List<TimerEntity> nextTimers = commandExecutor.execute(new GetUnlockedTimersByDuedateCmd(duedate));
						if (!nextTimers.isEmpty()) {
							long dueDate = nextTimers.get(0).getDuedate().getTime();
							long millisTillNextTimer = dueDate - cntDate;// 下一个job执行的时间
							if (millisTillNextTimer < millisToWait) {
								millisToWait = millisTillNextTimer;
							}
						}
					} else {
						millisToWait = 0;
					}
				}
			} catch (Exception e) {
				if (millisToWait == 0) {
					millisToWait = jobExecutor.getWaitTimeInMillis();
				} else {
					long maxWait = 60 * 1000;// 6秒中
					millisToWait = maxWait;
				}
			}
			// 根据等待时间，进行等待延时，如果不需要等直接进入下一轮计算
			if ((millisToWait > 0) && (!isJobAdded)) {
				try {
					synchronized (MONITOR) {
						if (!isInterrupted) {
							isWaiting.set(true);
							MONITOR.wait(60 * 1000);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					isWaiting.set(false);
				}
			}
		}
	}
	public void stop() {
		synchronized (MONITOR) {
			isInterrupted = true;
			if (isWaiting.compareAndSet(true, false)) {
				MONITOR.notifyAll();
			}
		}
	}
	public void jobWasAdded() {
		isJobAdded = true;
		if (isWaiting.compareAndSet(true, false)) {
			synchronized (MONITOR) {
				MONITOR.notifyAll();
			}
		}
	}
}
