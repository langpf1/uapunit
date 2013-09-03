function DefaultNoticeDefinition(id)
{
	this.receivers =null;
	this.contentTemplate="";
	this.noticeType=null;
	this.noticeTime=null;
	this.condition="";
	this.hasReceipt=false;
	this.properties=null;
};
DefaultNoticeDefinition.prototype.constructor = DefaultNoticeDefinition;