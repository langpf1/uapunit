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
package uap.workflow.engine.pvm.runtime;
import java.util.logging.Logger;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ITransition;
/**
 * 
 * @author Tom Baeyens
 * @author Daniel Meyer
 */
public class OutgoingExecution {
	private static Logger log = Logger.getLogger(OutgoingExecution.class.getName());
	protected IActivityInstance outgoingExecution;
	protected ITransition outgoingTransition;
	protected boolean isNew;
	public OutgoingExecution(IActivityInstance outgoingExecution, ITransition outgoingTransition, boolean isNew) {
		this.outgoingExecution = outgoingExecution;
		this.outgoingTransition = outgoingTransition;
		this.isNew = isNew;
	}
	public void take() {
		if (!outgoingExecution.isDeleteRoot()) {
			outgoingExecution.take(outgoingTransition);
		} else {
			log.fine("Not taking transition '" + outgoingTransition + "', outgoing execution has ended.");
		}
	}
}