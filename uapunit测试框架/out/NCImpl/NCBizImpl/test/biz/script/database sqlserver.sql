---------------------------������ģ��
select * from sm_funcregister where class_name = 'nc.ui.pub.bizflow.BusitypeClientUI2';
//*
update sm_funcregister set class_name='uap.workflow.modeler.WorkflowsManager', fun_desc='����������', fun_name='����������' where funcode = '101603';
update SM_MENUITEMREG set menuitemname = '����������' where funcode = '101603';
//*/  

/*
update sm_funcregister set class_name='nc.ui.pub.bizflow.BusitypeClientUI2', fun_desc='���弯�ŵ�ҵ�����̵���', fun_name='ҵ��������' where funcode = '101603';
update SM_MENUITEMREG set menuitemname = 'ҵ��������' where funcode = '101603';
//*/  
-------------------------------------------------�빺��

create table PrayBillMaster(
	ID char(20) not null, 
	supplier nvarchar(50), 
	sum decimal,
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_PrayBillMaster_ID PRIMARY KEY (ID) 

);
create table PrayBillDetail(
	ID char(20) not null, 
	parentID char(20) not null, 
	materiel nvarchar(50), 
	price numeric(28,8), 
	quanlity numeric(28,8),
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_PrayBillDetail_ID PRIMARY KEY (ID) 
);

//*
update sm_funcregister set class_name='test.biz.praybill.client.PrayBillUI', fun_desc='�빺��ά��', fun_name='�빺��ά��' where funcode = '40042008';
update SM_MENUITEMREG set menuitemname = '�빺��ά��' where funcode = '40042008';
//*/

/*
update sm_funcregister set class_name='nc.ui.iufo.freereport.FreeReportFunclet', fun_desc='�ۺ��ձ�', fun_name='�ۺ��ձ�' where funcode = '40042008';
update SM_MENUITEMREG set menuitemname = '�ۺ��ձ�' where funcode = '40042008';
//*/
-------------------------------------- �ɹ�����


create table pomaster(
	ID char(20) not null, 
	supplier nvarchar(50), 
	sum decimal,
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_pomaster_ID PRIMARY KEY (ID) 

);
create table podetail(
	ID char(20) not null, 
	parentID char(20) not null, 
	materiel nvarchar(50), 
	price numeric(28,8), 
	quanlity numeric(28,8),
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_podetail_ID PRIMARY KEY (ID) 
);

//*
update sm_funcregister set class_name='test.biz.purchaseorder.client.PurchaseOrderUI', fun_desc='�ɹ�����ά��', fun_name='�ɹ�����ά��' where funcode = '40042002';
update SM_MENUITEMREG set menuitemname ='�ɹ�����ά��' where funcode = '40042002';
//*/

/*
update sm_funcregister set class_name='nc.ui.pubapp.uif2app.ToftPanelAdaptorEx', fun_desc='��������������ܱ�', fun_name='��������������ܱ�' where funcode = '40042002';
update SM_MENUITEMREG set menuitemname ='��������������ܱ�' where funcode = '40042002';
//*/
-------------------------------------- �ɹ�������

create table rcvbillmaster(
	ID char(20) not null, 
	supplier nvarchar(50), 
	sum decimal,
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_rcvbillmaster_ID PRIMARY KEY (ID) 

);
create table rcvbilldetail(
	ID char(20) not null, 
	parentID char(20) not null, 
	materiel nvarchar(50), 
	price numeric(28,8), 
	quanlity numeric(28,8),
	DR numeric(10) DEFAULT 0,
        TS CHAR(19) DEFAULT cast(getdate() as datetime), 
        CONSTRAINT PK_rcvbilldetail_ID PRIMARY KEY (ID) 
);

//*
update sm_funcregister set class_name='test.biz.rcvbill.client.RecevieBillUI', fun_desc='�ɹ�����ά��', fun_name='�ɹ�����ά��' where funcode = '40042013';
update SM_MENUITEMREG set menuitemname ='�ɹ�������ά��' where funcode = '40042012';
//*/

/*
update sm_funcregister set class_name='nc.ui.pubapp.uif2app.ToftPanelAdaptorEx', fun_desc='�ݹ���ͳ��', fun_name='��������' where funcode = '40042013';
update SM_MENUITEMREG set menuitemname ='�ݹ���ͳ��' where funcode = '40042012';
//*/
-----------------------------------------