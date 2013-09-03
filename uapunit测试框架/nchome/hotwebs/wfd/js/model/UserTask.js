/**
 * Class: UserTask
 */
function UserTask(id)
{
	Task.call(this, id);
	this.name = "User Task";
	this.priority = 0;
	this.formKey="";
	this.dueDate="";
	this.formProperties=null;//new FormProperty();
    this.makeBill = false;
	this.afterSign = false;
	this.sequence = false;
	this.taskListeners=null;//new TaskListener();
	this.participants=null;//ew DefaultParticipantDefinition();
	this.notices=null;//new DefaultNoticeDefinition();
	this.taskHandlings=null;//new DefaultTaskHandlingDefinition();
	this.openUIStyle="BisunessUI";
	this.openURI="";	
	this.ExtendProperties;
	this.taskType = 0;
	this.control=new UserTaskPolicyControl();
	this.label = "User Task";
	this.infoClass = "UserTaskInfo";
};

UserTask.prototype = new Task();
UserTask.prototype.constructor = UserTask;

UserTask.prototype.toXML = function (mxCodec) {
	var control = this.control;
	this.control=null;

	var participants = this.participants;
	this.participants=null;

	var notices = this.notices;
	this.notices=null;

	var taskHandlings = this.taskHandlings;
	this.taskHandlings=null;

	var formProperties = this.formProperties;
	this.formProperties=null;

	var taskListeners = this.taskListeners;
	this.taskListeners=null;
	
	var mxObjectCodec = mxCodecRegistry.getCodec(this.constructor);
	var node = mxObjectCodec.encode(mxCodec, this);
	var nodeex = mxCodec.document.createElement("extensionElements");
	node.appendChild(nodeex);
	
	var nodecontrol = mxCodec.document.createElement("nc:userTaskPolicyControl");
	nodeex.appendChild(nodecontrol);
	mxObjectCodec.encodeObject(mxCodec, control, nodecontrol);

	if(participants != null)
	{
		for (var i = 0; i < participants.length; i++)
		{
			var participant = participants[i];
			var nodeparticipantj = mxCodec.document.createElement("nc:participant");
			nodeex.appendChild(nodeparticipantj);
			mxObjectCodec.encodeObject(mxCodec, participant, nodeparticipantj);
			
			var nodeparticipantType = mxCodec.document.createElement("participantType");
			nodeparticipantj.appendChild(nodeparticipantType);
			nodeparticipantType.setAttribute("code", participant.participantType);
			
			var nodeparticipantFilterType = mxCodec.document.createElement("participantFilterType");
			nodeparticipantj.appendChild(nodeparticipantFilterType);
			nodeparticipantFilterType.setAttribute("code", participant.participantFilterType);
		}
	}
	
	this.subToXML(mxCodec, mxObjectCodec, notices, nodeex, "nc:notice")
	this.subToXML(mxCodec, mxObjectCodec, taskHandlings, nodeex, "nc:taskHandling")
	this.subToXML(mxCodec, mxObjectCodec, formProperties, nodeex, "nc:taskHandling")
	this.subToXML(mxCodec, mxObjectCodec, taskListeners, nodeex, "nc:taskListener")
	
	this.control = control;
	this.participants = participants;
	this.notices = notices;
	this.taskHandlings = taskHandlings;
	this.formProperties = formProperties;
	this.taskListeners = taskListeners;
	return node;
};

UserTask.prototype.subToXML = function (mxCodec, mxObjectCodec, subObjs, nodeex, nodeName) {
	if(subObjs != null)
	{
		for (var i = 0; i < subObjs.length; i++)
		{
			var subObj = subObjs[i];
			var nodesubObj = mxCodec.document.createElement(nodeName);
			nodeex.appendChild(nodesubObj);
			mxObjectCodec.encodeObject(mxCodec, subObj, nodesubObj);
		}
	}
}

/**
 * Class: UserTaskInfo
 */
function UserTaskInfo(id)
{
	TaskInfo.call(this, id);
	this.properties.push(new Property('ExtendProperties', 'General', false, 
	    EditorType.text, '','ExtendProperties','ExtendPropertiesHints'));
   	this.properties.push(new Property('formKey', 'Form', false, 
		EditorType.text, '','formKey','formKeyHints'));
	this.properties.push(new Property('formProperties', 'Form', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/formProperties.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'formProperties','formPropertiesHints'));
	this.properties.push(new Property('dueDate', 'Main Config', false, 
		EditorType.datebox, '','dueDate','dueDateHints'));
	this.properties.push(new Property('priority', 'Main Config', false, 
		EditorType.numberbox, '','priority','priorityHints'));
	this.properties.push(new Property('makeBill', 'Main Config', false, 
		EditorType.checkbox, {on:true,off:false},'makeBill','makeBillHints'));		
	this.properties.push(new Property('taskListeners', 'Listeners', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/taskListeners.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'taskListeners','taskListenersHints'));
	this.properties.push(new Property('participants', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/Participants.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'participants','participantsHints'));
	this.properties.push(new Property('notices', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/notices.html',features:'dialogHeight:400px;dialogWidth:650px;center:yes'},'notices','noticesHints'));
	this.properties.push(new Property('taskHandlings', 'Main Config', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/taskHandlings.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'taskHandlings','taskHandlingsHints'));
	this.properties.push(new Property('openUIStyle', 'Main Config', false, 
	    EditorType.combobox, {data:
	    [{value:'BisunessUI',text:mxResources.get('BisunessUI')}, {value:'ApproveUI',text:mxResources.get('ApproveUI')}, {value:'DefinedUI',text:mxResources.get('DefinedUI')}, {value:'CustomURI',text:mxResources.get('CustomURI')}]},'openUIStyle','openUIStyleHints'));
	this.properties.push(new Property('openURI', 'Main Config', false, 
		EditorType.text, '','openURI','openURIHints'));
    this.properties.push(new Property('control.approve', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.approve','control.approveHints'));
	this.properties.push(new Property('control.deliver', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.deliver','control.deliverHints'));
	this.properties.push(new Property('control.undertake', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.undertake','control.undertakeHints'));
	this.properties.push(new Property('control.processClass', 'Policy Control', false, 
		getEditorType('control.processClass'),getOptions('control.processClass') ,'control.processClass','control.processClassHints'));
	this.properties.push(new Property('control.canAddSign', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canAddSign','control.canAddSignHints'));
	this.properties.push(new Property('control.canDelegate', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDelegate','control.canDelegateHints'));
	this.properties.push(new Property('control.canTransfer', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canTransfer','control.canTransferHints'));		
	this.properties.push(new Property('control.canDeliver', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDeliver','control.canDeliverHints'));
	this.properties.push(new Property('control.canAssign', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canAssign','control.canAssignHints'));
	this.properties.push(new Property('control.opinionEditable', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.opinionEditable','control.opinionEditableHints'));
	this.properties.push(new Property('control.opinionNullable', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.opinionNullable','control.opinionNullableHints'));
	this.properties.push(new Property('control.canHasten', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canHasten','control.canHastenHints'));
	this.properties.push(new Property('control.canPrint', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canPrint','control.canPrintHints'));
	this.properties.push(new Property('control.canRecycle', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canRecycle','control.canRecycleHints'));
	this.properties.push(new Property('control.canPassthrough', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canPassthrough','control.canPassthroughHints'));
	this.properties.push(new Property('control.canUploadAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canUploadAttachment','control.canUploadAttachmentHints'));
	this.properties.push(new Property('control.canDownloadAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDownloadAttachment','control.canDownloadAttachmentHints'));
	this.properties.push(new Property('control.canDeleteAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canDeleteAttachment','control.canDeleteAttachmentHints'));
	this.properties.push(new Property('control.canModifyAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canModifyAttachment','control.canModifyAttachmentHints'));
	this.properties.push(new Property('control.canViewAttachment', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canViewAttachment','control.canViewAttachmentHints'));
	this.properties.push(new Property('control.collaborationParticipants', 'Policy Control', false, 
		EditorType.refbox, {url:'./dialog/PropertyDialog/Participants.html',features:'dialogHeight:400px;dialogWidth:600px;center:yes'},'control.collaborationParticipants','control.collaborationParticipantsHints'));
	this.properties.push(new Property('control.canReject', 'Policy Control', false, 
		EditorType.checkbox, {on:true,off:false},'control.canReject','control.canRejectHints'));
	this.properties.push(new Property('control.rejectPolicy', 'Policy Control', true, 
		EditorType.combobox, {data:[{value:0,text:mxResources.get('LastStep')}, {value:1,text:mxResources.get('BillMaker')}, {value:2,text:mxResources.get('AllActivity')},{value:3,text:mxResources.get('SpecifiedActivity')}]},'control.rejectPolicy','control.rejectPolicyHints'));		
	this.properties.push(new Property('control.activityRef', 'Policy Control', false, 
		EditorType.text, '','control.activityRef','control.activityRefHints'));
	
	function getEditorType(name){
		if(name === "control.processClass" && mxClient.IS_SWTBROWSER){
			return EditorType.refbox;
		}
		return EditorType.text;
	};

	function getOptions(name){
		if(name === "control.processClass" && mxClient.IS_SWTBROWSER){
			return {id:'UserTask.control.processClass'};
		}
		return "";
	};
};

UserTaskInfo.prototype = new TaskInfo();
UserTaskInfo.prototype.constructor = UserTaskInfo;
