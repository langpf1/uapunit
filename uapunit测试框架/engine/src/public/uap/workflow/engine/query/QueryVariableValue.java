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


import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.variable.ByteArrayType;
import uap.workflow.engine.variable.JPAEntityVariableType;
import uap.workflow.engine.variable.VariableType;
import uap.workflow.engine.variable.VariableTypes;


/**
 * Represents a variable value used in queries.
 * 
 * @author Frederik Heremans
 */
public class QueryVariableValue implements Serializable {
  private static final long serialVersionUID = 1L;
  private String name;
  private Object value;
  private QueryOperator operator;
  
  private VariableInstanceEntity variableInstanceEntity;
    
  public QueryVariableValue(String name, Object value, QueryOperator operator) {
    this.name = name;
    this.value = value;
    this.operator = operator;
  }
  
  public void initialize(VariableTypes types) {
    if(variableInstanceEntity == null) {
      VariableType type = types.findVariableType(value);
      if(type instanceof ByteArrayType) {
        throw new WorkflowException("Variables of type ByteArray cannot be used to query");
      } else if(type instanceof JPAEntityVariableType && operator != QueryOperator.EQUALS) {
        throw new WorkflowException("JPA entity variables can only be used in 'variableValueEquals'");
      } else {
        // Type implementation determines which fields are set on the entity
        variableInstanceEntity = VariableInstanceEntity.create(name, type, value);
      }
    }
  }
  
  public String getName() {
    return name;
  }
  
  public String getOperator() {
    if(operator != null) {
      return operator.toString();      
    }
    return QueryOperator.EQUALS.toString();
  }
  
  public String getTextValue() {
    if(variableInstanceEntity != null) {
      return variableInstanceEntity.getTextValue();
    }
    return null;
  }
  
  public Long getLongValue() {
    if(variableInstanceEntity != null) {
      return variableInstanceEntity.getLongValue();
    }
    return null;
  }
  
  public Double getDoubleValue() {
    if(variableInstanceEntity != null) {
      return variableInstanceEntity.getDoubleValue();
    }
    return null;
  }
  
  public String getTextValue2() {
    if(variableInstanceEntity != null) {
      return variableInstanceEntity.getTextValue2();
    }
    return null;
  }

  public String getType() {
    if(variableInstanceEntity != null) {
      return variableInstanceEntity.getType().getTypeName();
    }
    return null;
  }
}