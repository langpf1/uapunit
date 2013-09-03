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
import java.util.Map;


import uap.workflow.engine.cmd.CheckPassword;
import uap.workflow.engine.cmd.CreateGroupCmd;
import uap.workflow.engine.cmd.CreateGroupQueryCmd;
import uap.workflow.engine.cmd.CreateMembershipCmd;
import uap.workflow.engine.cmd.CreateUserCmd;
import uap.workflow.engine.cmd.CreateUserQueryCmd;
import uap.workflow.engine.cmd.DeleteGroupCmd;
import uap.workflow.engine.cmd.DeleteMembershipCmd;
import uap.workflow.engine.cmd.DeleteUserCmd;
import uap.workflow.engine.cmd.DeleteUserInfoCmd;
import uap.workflow.engine.cmd.GetUserAccountCmd;
import uap.workflow.engine.cmd.GetUserInfoCmd;
import uap.workflow.engine.cmd.GetUserInfoKeysCmd;
import uap.workflow.engine.cmd.GetUserPictureCmd;
import uap.workflow.engine.cmd.SaveGroupCmd;
import uap.workflow.engine.cmd.SaveUserCmd;
import uap.workflow.engine.cmd.SetUserInfoCmd;
import uap.workflow.engine.cmd.SetUserPictureCmd;
import uap.workflow.engine.entity.GroupEntity;
import uap.workflow.engine.entity.IdentityInfoEntity;
import uap.workflow.engine.identity.Account;
import uap.workflow.engine.identity.Authentication;
import uap.workflow.engine.identity.Group;
import uap.workflow.engine.identity.GroupQuery;
import uap.workflow.engine.identity.Picture;
import uap.workflow.engine.identity.User;
import uap.workflow.engine.identity.UserQuery;
import uap.workflow.engine.service.IdentityService;


/**
 * @author Tom Baeyens
 */
public class IdentityServiceImpl extends ServiceImpl implements IdentityService {
  
  public Group newGroup(String groupId) {
    return commandExecutor.execute(new CreateGroupCmd(groupId));
  }

  public User newUser(String userId) {
    return commandExecutor.execute(new CreateUserCmd(userId));
  }

  public void saveGroup(Group group) {
    commandExecutor.execute(new SaveGroupCmd((GroupEntity) group));
  }

  public void saveUser(User user) {
    commandExecutor.execute(new SaveUserCmd(user));
  }
  
  public UserQuery createUserQuery() {
    return commandExecutor.execute(new CreateUserQueryCmd());
  }
  
  public GroupQuery createGroupQuery() {
    return commandExecutor.execute(new CreateGroupQueryCmd());
  }

  public void createMembership(String userId, String groupId) {
    commandExecutor.execute(new CreateMembershipCmd(userId, groupId));
  }

  public void deleteGroup(String groupId) {
    commandExecutor.execute(new DeleteGroupCmd(groupId));
  }

  public void deleteMembership(String userId, String groupId) {
    commandExecutor.execute(new DeleteMembershipCmd(userId, groupId));
  }

  public boolean checkPassword(String userId, String password) {
    return commandExecutor.execute(new CheckPassword(userId, password));
  }

  public void deleteUser(String userId) {
    commandExecutor.execute(new DeleteUserCmd(userId));
  }

  public void setUserPicture(String userId, Picture picture) {
    commandExecutor.execute(new SetUserPictureCmd(userId, picture));
  }

  public Picture getUserPicture(String userId) {
    return commandExecutor.execute(new GetUserPictureCmd(userId));
  }

  public void setAuthenticatedUserId(String authenticatedUserId) {
    Authentication.setAuthenticatedUserId(authenticatedUserId);
  }

  public String getUserInfo(String userId, String key) {
    return commandExecutor.execute(new GetUserInfoCmd(userId, key));
  }

  public List<String> getUserInfoKeys(String userId) {
    return commandExecutor.execute(new GetUserInfoKeysCmd(userId, IdentityInfoEntity.TYPE_USERINFO));
  }

  public List<String> getUserAccountNames(String userId) {
    return commandExecutor.execute(new GetUserInfoKeysCmd(userId, IdentityInfoEntity.TYPE_USERACCOUNT));
  }

  public void setUserInfo(String userId, String key, String value) {
    commandExecutor.execute(new SetUserInfoCmd(userId, key, value));
  }

  public void deleteUserInfo(String userId, String key) {
    commandExecutor.execute(new DeleteUserInfoCmd(userId, key));
  }

  public void deleteUserAccount(String userId, String accountName) {
    commandExecutor.execute(new DeleteUserInfoCmd(userId, accountName));
  }

  public Account getUserAccount(String userId, String userPassword, String accountName) {
    return commandExecutor.execute(new GetUserAccountCmd(userId, userPassword, accountName));
  }

  public void setUserAccount(String userId, String userPassword, String accountName, String accountUsername, String accountPassword, Map<String, String> accountDetails) {
    commandExecutor.execute(new SetUserInfoCmd(userId, userPassword, accountName, accountUsername, accountPassword, accountDetails));
  }
}
