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
import java.util.List;
import java.util.UUID;

import uap.workflow.engine.interceptor.CommandExecutor;
/**
 * <p>
 * Interface to the work management component of activiti.
 * </p>
 * 
 * <p>
 * This component is responsible for performing all background work ({@link Job
 * Jobs}) scheduled by activiti.
 * </p>
 * 
 * <p>
 * You should generally only have one of these per Activiti instance (process
 * engine) in a JVM. In clustered situations, you can have multiple of these
 * running against the same queue + pending job list.
 * </p>
 * 
 * @author Daniel Meyer
 */
public abstract class JobExecutor {
	protected String name = "JobExecutor[" + getClass().getName() + "]";
	protected CommandExecutor commandExecutor;// 命令执行器负责执行指令
	protected AcquireJobsRunnable acquireJobsRunnable;// 这个线程主要用来扫描jobentity;
	protected boolean isAutoActivate = true;// 这个参数用来标识是否需要启动作业执行器
	protected boolean isActive = false;// 这个用来表识作业执行器是否激活
	protected int maxJobsPerAcquisition = 3;
	protected int waitTimeInMillis = 5 * 1000;//5秒钟
	protected int lockTimeInMillis = 5 * 60 * 1000;// 5分钟
	protected String lockOwner = UUID.randomUUID().toString();
	public void start() { // 这个主要是作业执行器 的启动，需要初始化作业执行器的一些初始化参数
		if (isActive) {
			return;
		}
		ensureInitialization();
		startExecutingJobs();
		isActive = true;
	}
	public synchronized void shutdown() {
		if (!isActive) {
			return;
		}
		acquireJobsRunnable.stop();
		stopExecutingJobs();
		ensureCleanup();
		isActive = false;
	}
	protected void ensureInitialization() {
		acquireJobsRunnable = new AcquireJobsRunnable(this);
	}
	protected void ensureCleanup() {
		acquireJobsRunnable = null;
	}
	public void jobWasAdded() {
		if (isActive) {
			acquireJobsRunnable.jobWasAdded();
		}
	}
	protected abstract void startExecutingJobs();
	protected abstract void stopExecutingJobs();
	protected abstract void executeJobs(List<String> jobIds);
	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	public int getWaitTimeInMillis() {
		return waitTimeInMillis;
	}
	public void setWaitTimeInMillis(int waitTimeInMillis) {
		this.waitTimeInMillis = waitTimeInMillis;
	}
	public int getLockTimeInMillis() {
		return lockTimeInMillis;
	}
	public void setLockTimeInMillis(int lockTimeInMillis) {
		this.lockTimeInMillis = lockTimeInMillis;
	}
	public String getLockOwner() {
		return lockOwner;
	}
	public void setLockOwner(String lockOwner) {
		this.lockOwner = lockOwner;
	}
	public boolean isAutoActivate() {
		return isAutoActivate;
	}
	public void setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}
	public void setAutoActivate(boolean isAutoActivate) {
		this.isAutoActivate = isAutoActivate;
	}
	public int getMaxJobsPerAcquisition() {
		return maxJobsPerAcquisition;
	}
	public void setMaxJobsPerAcquisition(int maxJobsPerAcquisition) {
		this.maxJobsPerAcquisition = maxJobsPerAcquisition;
	}
	public String getName() {
		return name;
	}
	public boolean isActive() {
		return isActive;
	}
}
