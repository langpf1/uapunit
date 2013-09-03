/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.history;
import java.util.Date;

import uap.workflow.engine.service.IdentityService;
/**
 * A single execution of a whole process definition that is stored permanently.
 * 
 * @author Christian Stettler
 */
public interface HistoricProcessInstance {
	String getId();
	/** The user provided unique reference to this process instance. */
	String getBusinessKey();
	/** The process definition reference. */
	String getProcessDefinitionId();
	/** The time the process was started. */
	Date getStartTime();
	/** The time the process was ended. */
	Date getEndTime();
	/**
	 * The difference between {@link #getEndTime()} and {@link #getStartTime()}
	 * .
	 */
	Long getDurationInMillis();
	/** Reference to the activity in which this process instance ended. */
	String getEndActivityId();
	/**
	 * The authenticated user that started this process instance.
	 * 
	 * @see IdentityService#setAuthenticatedUserId(String)
	 */
	String getStartUserId();
	/** The start activity. */
	String getStartActivityId();
	/** Obtains the reason for the process instance's deletion. */
	String getDeleteReason();
}
