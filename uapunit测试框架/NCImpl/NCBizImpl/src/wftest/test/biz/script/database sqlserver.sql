---------------------------工作流模型
select * from sm_funcregister where class_name = 'nc.ui.pub.bizflow.BusitypeClientUI2';
//*
update sm_funcregister set class_name='uap.workflow.modeler.WorkflowsManager', fun_desc='工作流定义', fun_name='工作流定义' where funcode = '101603';
update SM_MENUITEMREG set menuitemname = '工作流定义' where funcode = '101603';
//*/  

/*
update sm_funcregister set class_name='nc.ui.pub.bizflow.BusitypeClientUI2', fun_desc='定义集团的业务流程档案', fun_name='业务流定义' where funcode = '101603';
update SM_MENUITEMREG set menuitemname = '业务流定义' where funcode = '101603';
//*/  
-------------------------------------------------请购单

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
update sm_funcregister set class_name='test.biz.praybill.client.PrayBillUI', fun_desc='请购单维护', fun_name='请购单维护' where funcode = '40042008';
update SM_MENUITEMREG set menuitemname = '请购单维护' where funcode = '40042008';
//*/

/*
update sm_funcregister set class_name='nc.ui.iufo.freereport.FreeReportFunclet', fun_desc='综合日报', fun_name='综合日报' where funcode = '40042008';
update SM_MENUITEMREG set menuitemname = '综合日报' where funcode = '40042008';
//*/
-------------------------------------- 采购订单


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
update sm_funcregister set class_name='test.biz.purchaseorder.client.PurchaseOrderUI', fun_desc='采购订单维护', fun_name='采购订单维护' where funcode = '40042002';
update SM_MENUITEMREG set menuitemname ='采购订单维护' where funcode = '40042002';
//*/

/*
update sm_funcregister set class_name='nc.ui.pubapp.uif2app.ToftPanelAdaptorEx', fun_desc='物资需求申请汇总表', fun_name='物资需求申请汇总表' where funcode = '40042002';
update SM_MENUITEMREG set menuitemname ='物资需求申请汇总表' where funcode = '40042002';
//*/
-------------------------------------- 采购到货单

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
update sm_funcregister set class_name='test.biz.rcvbill.client.RecevieBillUI', fun_desc='采购到货维护', fun_name='采购到货维护' where funcode = '40042013';
update SM_MENUITEMREG set menuitemname ='采购到货单维护' where funcode = '40042012';
//*/

/*
update sm_funcregister set class_name='nc.ui.pubapp.uif2app.ToftPanelAdaptorEx', fun_desc='暂估月统计', fun_name='超级报表' where funcode = '40042013';
update SM_MENUITEMREG set menuitemname ='暂估月统计' where funcode = '40042012';
//*/
-----------------------------------------