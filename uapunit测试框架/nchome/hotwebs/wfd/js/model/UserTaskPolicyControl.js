function UserTaskPolicyControl(id)
{
	this.name = "UserTaskPolicyControl";
	this.infoClass = "UserTaskInfo";
		//基本信息部分--------------------------------------------------------------
	this.approve=false;		//审批，使用动作标记
	this.deliver=false;		//传阅，使用固定的内置逻辑
	this.undertake=false;	//承办，
	this.processClass="";	//处理类
	this.form="";			//表单
	//@XmlElement
	//public void modifyResources(){}//修改审批对话框资源信息
	
	//参与者，包括组织机构、角色、角色组、用户、用户组、回报关系、虚拟角色（主办人、协办人、发起人）、同其他活动节点参与者、自定义参与这类
	//权限控制
	this.canAddSign=false;	//允许加签
	this.canDelegate=false;	//允许改派
	this.canTransfer=false;	//可转发
	this.canDeliver=false;	//可传阅
	this.canAssign=false;	//由上一步指派
	this.opinionEditable=false;	//可编辑意见
	this.opinionNullable=false;//是否意见可空
	//同部门限定
	this.canHasten=false;	//允许催办
	this.canPrint=false;	//允许打印
	this.canRecycle=false;	//允许收回
	this.canPassthrough=false; //允许快速通道
	this.canUploadAttachment=false;	//允许附件上传
	this.canDownloadAttachment=false;//允许附件下载
	this.canDeleteAttachment=false;	//允许附件删除
	this.canModifyAttachment=false;	//允许附件修改
	this.canViewAttachment=false;	//允许附件查看
	//协办参与者
	this.collaborationParticipants= null;
	this.voucherPrivilege=new Object();
	//活动策略----------------------------------------------------------------
	//回退策略
	this.canReject=false; //禁止回退，允许回退
	this.rejectPolicy; //上一步，制单人，全部活动，指定活动
	this.activityRef;
	//消息提醒----------------------------------------------------------------使用消息提醒和timer节点解决
	//任务创建消息提醒
		//任务创建提醒--使用协同消息
		//制单人控制项--使用协同消息
	//任务完成消息提醒
		//任务完成提醒--使用协同消息
		//制单人控制项--使用协同消息
	//时间估算
		//时间单位
		//提醒时间
		//工作时间
	//超时消息提醒
		//超时提醒--使用协同消息
		//制单人控制项--使用协同控制
	//超时控制
		//超时动作：继续等待、超时终止、超时继续
};
UserTaskPolicyControl.prototype.constructor = UserTask;



