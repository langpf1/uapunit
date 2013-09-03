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

import java.util.List;


import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.identity.Group;
import uap.workflow.engine.identity.GroupQuery;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.interceptor.CommandExecutor;


/**
 * @author Joram Barrez
 */
public class GroupQueryImpl extends AbstractQuery<GroupQuery, Group> implements GroupQuery {
  
  private static final long serialVersionUID = 1L;
  protected String id;
  protected String name;
  protected String nameLike;
  protected String type;
  protected String userId;

  public GroupQueryImpl() {
  }

  public GroupQueryImpl(CommandContext commandContext) {
    super(commandContext);
  }

  public GroupQueryImpl(CommandExecutor commandExecutor) {
    super(commandExecutor);
  }

  public GroupQuery groupId(String id) {
    if (id == null) {
      throw new WorkflowException("Provided id is null");
    }
    this.id = id;
    return this;
  }
  
  public GroupQuery groupName(String name) {
    if (name == null) {
      throw new WorkflowException("Provided name is null");
    }
    this.name = name;
    return this;
  }
  
  public GroupQuery groupNameLike(String nameLike) {
    if (nameLike == null) {
      throw new WorkflowException("Provided nameLike is null");
    }
    this.nameLike = nameLike;
    return this;
  }
  
  public GroupQuery groupType(String type) {
    if (type == null) {
      throw new WorkflowException("Provided type is null");
    }
    this.type = type;
    return this;
  }
  
  public GroupQuery groupMember(String userId) {
    if (userId == null) {
      throw new WorkflowException("Provided userId is null");
    }
    this.userId = userId;
    return this;
  }

  //sorting ////////////////////////////////////////////////////////
  
  public GroupQuery orderByGroupId() {
    return orderBy(GroupQueryProperty.GROUP_ID);
  }
  
  public GroupQuery orderByGroupName() {
    return orderBy(GroupQueryProperty.NAME);
  }
  
  public GroupQuery orderByGroupType() {
    return orderBy(GroupQueryProperty.TYPE);
  }
  
  //results ////////////////////////////////////////////////////////
  
  public long executeCount(CommandContext commandContext) {
    checkQueryOk();
    return commandContext
      .getGroupManager()
      .findGroupCountByQueryCriteria(this);
  }
  
  public List<Group> executeList(CommandContext commandContext, Page page) {
    checkQueryOk();
    return commandContext
      .getGroupManager()
      .findGroupByQueryCriteria(this, page);
  }
  
  //getters ////////////////////////////////////////////////////////
  
  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public String getNameLike() {
    return nameLike;
  }
  public String getType() {
    return type;
  }
  public String getUserId() {
    return userId;
  }
  
}
