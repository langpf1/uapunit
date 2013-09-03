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
/**
 * @author Tom Baeyens
 */
public interface ITaskListener {
	String EVENTNAME_CREATE = "Create";
	String EVENTNAME_COMPLETE_BEFORE = "complete_before";
	String EVENTNAME_COMPLETE_AFTER = "complete_after";
	String EVENTNAME_REJECT_BEFORE = "reject_before";
	String EVENTNAME_REJECT_AFTER = "reject_after";
	String EVENTNAME_CALLBACK_BEFORE = "callback_before";
	String EVENTNAME_CALLBACK_AFTER = "callback_after";
	String EVENTNAME_TAKEBACK_BEFORE = "takeback_before";
	String EVENTNAME_TAKEBACK_AFTER = "takeback_after";
	String EVENTNAME_DELEGATE_BEFORE = "delegate_before";
	String EVENTNAME_DELEGATE_AFTER = "delegate_after";
	void notify(ITask task);
}
