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

package uap.workflow.engine.db;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.variable.VariableType;
import uap.workflow.engine.variable.VariableTypes;


/**
 * @author Dave Syer
 */
public class IbatisVariableTypeHandler implements TypeHandler {

  protected VariableTypes variableTypes;

  public Object getResult(ResultSet rs, String columnName) throws SQLException {
    String typeName = rs.getString(columnName);
    VariableType type = getVariableTypes().getVariableType(typeName);
    if (type == null) {
      throw new WorkflowException("unknown variable type name " + typeName);
    }
    return type;
  }

  public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
    String typeName = cs.getString(columnIndex);
    VariableType type = getVariableTypes().getVariableType(typeName);
    if (type == null) {
      throw new WorkflowException("unknown variable type name " + typeName);
    }
    return type;
  }

  public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    String typeName = ((VariableType) parameter).getTypeName();
    ps.setString(i, typeName);
  }

  protected VariableTypes getVariableTypes() {
    if (variableTypes==null) {
      variableTypes = Context
        .getProcessEngineConfiguration()
        .getVariableTypes();
    }
    return variableTypes;
  }
}
