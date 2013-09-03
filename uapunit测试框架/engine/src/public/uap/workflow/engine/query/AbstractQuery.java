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
package uap.workflow.engine.query;
import java.io.Serializable;
import java.util.List;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.interceptor.CommandExecutor;
/**
 * Abstract superclass for all query types.
 * 
 * @author Joram Barrez
 */
public abstract class AbstractQuery<T extends Query<?, ?>, U> implements Command<Object>, Query<T, U>, Serializable {
	private static final long serialVersionUID = 1L;
	public static final String SORTORDER_ASC = "asc";
	public static final String SORTORDER_DESC = "desc";
	private static enum ResultType {
		LIST, LIST_PAGE, SINGLE_RESULT, COUNT
	}
	protected transient CommandExecutor commandExecutor;
	protected transient CommandContext commandContext;
	protected String orderBy;
	protected int firstResult;
	protected int maxResults;
	protected ResultType resultType;
	protected QueryProperty orderProperty;
	protected AbstractQuery() {}
	protected AbstractQuery(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
	}
	public AbstractQuery(CommandContext commandContext) {
		this.commandContext = commandContext;
	}
	public AbstractQuery<T, U> setCommandExecutor(CommandExecutor commandExecutor) {
		this.commandExecutor = commandExecutor;
		return this;
	}
	@SuppressWarnings("unchecked")
	public T orderBy(QueryProperty property) {
		this.orderProperty = property;
		return (T) this;
	}
	public T asc() {
		return direction(Direction.ASCENDING);
	}
	public T desc() {
		return direction(Direction.DESCENDING);
	}
	@SuppressWarnings("unchecked")
	public T direction(Direction direction) {
		if (orderProperty == null) {
			throw new WorkflowException("You should call any of the orderBy methods first before specifying a direction");
		}
		addOrder(orderProperty.getName(), direction.getName());
		orderProperty = null;
		return (T) this;
	}
	protected void checkQueryOk() {
		if (orderProperty != null) {
			throw new WorkflowException("Invalid query: call asc() or desc() after using orderByXX()");
		}
	}
	@SuppressWarnings("unchecked")
	public U singleResult() {
		this.resultType = ResultType.SINGLE_RESULT;
		if (commandExecutor != null) {
			return (U) commandExecutor.execute(this);
		}
		return executeSingleResult(Context.getCommandContext());
	}
	@SuppressWarnings("unchecked")
	public List<U> list() {
		this.resultType = ResultType.LIST;
		if (commandExecutor != null) {
			return (List<U>) commandExecutor.execute(this);
		}
		return executeList(Context.getCommandContext(), null);
	}
	@SuppressWarnings("unchecked")
	public List<U> listPage(int firstResult, int maxResults) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.resultType = ResultType.LIST_PAGE;
		if (commandExecutor != null) {
			return (List<U>) commandExecutor.execute(this);
		}
		return executeList(Context.getCommandContext(), new Page(firstResult, maxResults));
	}
	public long count() {
		this.resultType = ResultType.COUNT;
		if (commandExecutor != null) {
			return (Long) commandExecutor.execute(this);
		}
		return executeCount(Context.getCommandContext());
	}
	public Object execute(CommandContext commandContext) {
		if (resultType == ResultType.LIST) {
			return executeList(commandContext, null);
		} else if (resultType == ResultType.SINGLE_RESULT) {
			return executeSingleResult(commandContext);
		} else if (resultType == ResultType.LIST_PAGE) {
			return executeList(commandContext, new Page(firstResult, maxResults));
		} else {
			return executeCount(commandContext);
		}
	}
	public abstract long executeCount(CommandContext commandContext);
	/**
	 * Executes the actual query to retrieve the list of results.
	 * 
	 * @param page
	 *            used if the results must be paged. If null, no paging will be
	 *            applied.
	 */
	public abstract List<U> executeList(CommandContext commandContext, Page page);
	public U executeSingleResult(CommandContext commandContext) {
		List<U> results = executeList(commandContext, null);
		if (results.size() == 1) {
			return results.get(0);
		} else if (results.size() > 1) {
			throw new WorkflowException("Query return " + results.size() + " results instead of max 1");
		}
		return null;
	}
	protected void addOrder(String column, String sortOrder) {
		if (orderBy == null) {
			orderBy = "";
		} else {
			orderBy = orderBy + ", ";
		}
		orderBy = orderBy + column + " " + sortOrder;
	}
	public String getOrderBy() {
		return orderBy;
	}
}
