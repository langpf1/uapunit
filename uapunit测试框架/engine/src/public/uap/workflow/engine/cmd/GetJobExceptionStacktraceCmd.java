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

package uap.workflow.engine.cmd;

import java.io.Serializable;


import uap.workflow.engine.entity.JobEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;


/**
 * @author Frederik Heremans
 */
public class GetJobExceptionStacktraceCmd implements Command<String>, Serializable{

  private static final long serialVersionUID = 1L;
  private String jobId;
    
  public GetJobExceptionStacktraceCmd(String jobId) {
    this.jobId = jobId;
  }


  public String execute(CommandContext commandContext) {
    if(jobId == null) {
      throw new WorkflowException("jobId is null");
    }
    
    JobEntity job = commandContext
      .getJobManager()
      .findJobById(jobId);
    
    if(job == null) {
      throw new WorkflowException("No job found with id " + jobId);
    }
    
    return job.getExceptionStacktrace();
  }

  
}
