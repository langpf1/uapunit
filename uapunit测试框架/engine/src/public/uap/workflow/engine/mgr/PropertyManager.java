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

package uap.workflow.engine.mgr;


import uap.workflow.engine.entity.PropertyEntity;
import uap.workflow.engine.persistence.AbstractManager;


/**
 * @author Tom Baeyens
 */
public class PropertyManager extends AbstractManager {

  public PropertyEntity findPropertyById(String propertyId) {
    return getDbSqlSession().selectById(PropertyEntity.class, propertyId);
  }

}
