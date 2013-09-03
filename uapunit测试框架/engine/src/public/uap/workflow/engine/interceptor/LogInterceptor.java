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
package uap.workflow.engine.interceptor;
import java.util.logging.Logger;

import uap.workflow.engine.util.ClassNameUtil;
/**
 * @author Tom Baeyens
 */
public class LogInterceptor extends CommandInterceptor {
	private static Logger log = Logger.getLogger(LogInterceptor.class.getName());
	// 这个拦截器主要用来记录执行日志，并唤醒下一个拦截器
	public <T> T execute(Command<T> command) {
		log.fine("                                                                                                    ");
		log.fine("--- starting " + ClassNameUtil.getClassNameWithoutPackage(command) + " --------------------------------------------------------");
		try {
			return next.execute(command);
		} finally {
			log.fine("--- " + ClassNameUtil.getClassNameWithoutPackage(command) + " finished --------------------------------------------------------");
			log.fine("                                                                                                    ");
		}
	}
}
