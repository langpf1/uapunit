/**
 * Class: Task
 */
function Task(id)
{
	Activity.call(this, id);
	this.taskType;
};

Task.prototype = new Activity();
Task.prototype.constructor = Task;

function BpmnTaskType(){};
BpmnTaskType.prototype.constructor = BpmnTaskType;
BpmnTaskType.UserTask = 0;
BpmnTaskType.ScriptTask = 1;
BpmnTaskType.ServiceTask = 2;
BpmnTaskType.MailTask = 3;
BpmnTaskType.ManualTask = 4;
BpmnTaskType.ReceiveTask = 5;
BpmnTaskType.BusinessRuleTask = 6;


/**
 * Class: TaskInfo
 */
function TaskInfo(id)
{
	ActivityInfo.call(this, id);

//	this.properties.push(new Property('taskType', '', true, 
//		EditorType.combobox, {data:[{value:0,text:'UserTask'}, {value:1,text:'ScriptTask'}, {value:2,text:'ServiceTask'}, {value:3,text:'MailTask'}, {value:4,text:'ManualTask'}, {value:5,text:'ReceiveTask'}, {value:6,text:'BusinessRuleTask'}]}));		
};

TaskInfo.prototype = new ActivityInfo();
TaskInfo.prototype.constructor = TaskInfo;
