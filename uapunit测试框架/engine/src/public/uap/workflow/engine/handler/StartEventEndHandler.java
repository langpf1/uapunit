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
package uap.workflow.engine.handler;
import java.util.List;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.HistoricActivityInstanceEntity;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 */
public class StartEventEndHandler implements IInstanceListener {
	public void notify(IActivityInstance execution) throws Exception {
		String executionId = execution.getActInsPk();
		String activityId = ((ActivityInstanceEntity) execution).getActivity().getId();
		// interrupted executions might not have an activityId set.
		if (activityId == null) {
			return;
		}
		CommandContext commandContext = Context.getCommandContext();
		// search for the historic activity instance in the dbsqlsession cache
		DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
		List<HistoricActivityInstanceEntity> cachedHistoricActivityInstances = dbSqlSession.findInCache(HistoricActivityInstanceEntity.class);
		for (HistoricActivityInstanceEntity cachedHistoricActivityInstance : cachedHistoricActivityInstances) {
			if (executionId.equals(cachedHistoricActivityInstance.getExecutionId()) && (activityId.equals(cachedHistoricActivityInstance.getActivityId()))
					&& (cachedHistoricActivityInstance.getEndTime() == null)) {
				cachedHistoricActivityInstance.markEnded(null);
				return;
			}
		}
	}
}
