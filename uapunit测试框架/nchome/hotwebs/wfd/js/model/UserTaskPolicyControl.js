function UserTaskPolicyControl(id)
{
	this.name = "UserTaskPolicyControl";
	this.infoClass = "UserTaskInfo";
		//������Ϣ����--------------------------------------------------------------
	this.approve=false;		//������ʹ�ö������
	this.deliver=false;		//���ģ�ʹ�ù̶��������߼�
	this.undertake=false;	//�а죬
	this.processClass="";	//������
	this.form="";			//��
	//@XmlElement
	//public void modifyResources(){}//�޸������Ի�����Դ��Ϣ
	
	//�����ߣ�������֯��������ɫ����ɫ�顢�û����û��顢�ر���ϵ�������ɫ�������ˡ�Э���ˡ������ˣ���ͬ������ڵ�����ߡ��Զ����������
	//Ȩ�޿���
	this.canAddSign=false;	//�����ǩ
	this.canDelegate=false;	//�������
	this.canTransfer=false;	//��ת��
	this.canDeliver=false;	//�ɴ���
	this.canAssign=false;	//����һ��ָ��
	this.opinionEditable=false;	//�ɱ༭���
	this.opinionNullable=false;//�Ƿ�����ɿ�
	//ͬ�����޶�
	this.canHasten=false;	//����߰�
	this.canPrint=false;	//�����ӡ
	this.canRecycle=false;	//�����ջ�
	this.canPassthrough=false; //�������ͨ��
	this.canUploadAttachment=false;	//�������ϴ�
	this.canDownloadAttachment=false;//����������
	this.canDeleteAttachment=false;	//������ɾ��
	this.canModifyAttachment=false;	//�������޸�
	this.canViewAttachment=false;	//�������鿴
	//Э�������
	this.collaborationParticipants= null;
	this.voucherPrivilege=new Object();
	//�����----------------------------------------------------------------
	//���˲���
	this.canReject=false; //��ֹ���ˣ��������
	this.rejectPolicy; //��һ�����Ƶ��ˣ�ȫ�����ָ���
	this.activityRef;
	//��Ϣ����----------------------------------------------------------------ʹ����Ϣ���Ѻ�timer�ڵ���
	//���񴴽���Ϣ����
		//���񴴽�����--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ��Ϣ
	//���������Ϣ����
		//�����������--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ��Ϣ
	//ʱ�����
		//ʱ�䵥λ
		//����ʱ��
		//����ʱ��
	//��ʱ��Ϣ����
		//��ʱ����--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ����
	//��ʱ����
		//��ʱ�����������ȴ�����ʱ��ֹ����ʱ����
};
UserTaskPolicyControl.prototype.constructor = UserTask;



