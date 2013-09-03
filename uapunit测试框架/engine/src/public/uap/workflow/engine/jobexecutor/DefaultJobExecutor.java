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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * <p>
 * This is a simple implementation of the {@link JobExecutor} using self-managed
 * threads for performing background work.
 * </p>
 * 
 * <p>
 * This implementation uses a {@link ThreadPoolExecutor} backed by a queue to
 * which work is submitted.
 * </p>
 * 
 * <p>
 * <em>NOTE: use this class in environments in which self-management of threads 
 * is permitted. Consider using a different thread-management strategy in 
 * J(2)EE-Environments.</em>
 * </p>
 * 
 * @author Daniel Meyer
 */
public class DefaultJobExecutor extends JobExecutor {
	protected Thread jobAcquisitionThread;
	protected ThreadPoolExecutor threadPoolExecutor;
	protected void startExecutingJobs() {
		if (threadPoolExecutor == null) {
			int corePoolSize = 3;// 核心线程数
			int maxPoolSize = 10;// 线程池最大线程数
			threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(3));
			threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		}
		if (jobAcquisitionThread == null) {
			jobAcquisitionThread = new Thread(acquireJobsRunnable);
			jobAcquisitionThread.start();
		}
	}
	protected void stopExecutingJobs() {
		try {
			jobAcquisitionThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		threadPoolExecutor.shutdown();
		threadPoolExecutor = null;
		jobAcquisitionThread = null;
	}
	public void executeJobs(List<String> jobIds) {
		try {
			threadPoolExecutor.execute(new ExecuteJobsRunnable(this, jobIds));
		} catch (RejectedExecutionException e) {
			new ExecuteJobsRunnable(this, jobIds).run();
		}
	}
}
