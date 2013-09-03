
select * from act_id_user
select * from act_id_group

select * from act_ru_task
select * from act_ru_identitylink;
select * from ACT_RU_VARIABLE
select * from act_hi_procinst
select * from act_hi_taskinst

select * from act_re_procdef;
select * from act_hi_procinst;
select * from act_hi_taskinst;
select * from act_hi_procinst;
select * from act_ru_execution;
select * from act_ru_task;
select * from act_ru_variable
delete from act_hi_taskinst;
delete from act_hi_actinst;
delete from act_hi_procinst;
delete from act_ru_variable;
delete from act_ru_task;
delete from act_ru_execution;
delete from act_hi_procinst;
select * from act_re_deployment
delete from act_re_deployment
select * from act_ge_bytearray
delete from act_ge_bytearray


select *
  from wf_job
 where (type = 'timer')
   and (duedate is not null)
   and (duedate <= '2012-06-26 10:40:34')
   and (lockowner is null or lockexpirationtime <= '2012-06-26 10:40:34')
   and (retries >= 0)
 order by duedate


delete from wf_prodef;
select * from wf_prodef order by ts;
--0001AA10000000006YRT
update wf_prodef set pk_prodef='0001AA10000000006YRT';
select * From wf_proins order by ts;
select * from wf_actins order by begindate;
select * from wf_assignment;
select * from wf_variable where pk_variable='0001AA1000000000BT5O';
select * from wf_task order by begindate ;
delete from wf_task where pk_task='0001AA1000000000AAFP';
delete from wf_proins;
delete from wf_actins;
delete from wf_task;
delete from wf_assignment;
delete from wf_variable;
select * from sm_user;
select * from sm_role;
select * from wf_beforeaddsign;
delete from wf_beforeaddsign;
select * from wf_beforeaddsignuser;
delete from wf_beforeaddsignuser;
select * from sm_user;
select * from wf_subscription;
delete from wf_subscription where pk_execution='0001AA10000000006DYN';
select * from wf_job;
update wf_job set duedate='2012-06-26 10:59:00';
delete from wf_job;

--流程定义表
drop table wf_prodef;
CREATE TABLE wf_prodef 
(
  pk_prodef char(20) not null, 
  pk_prodefgroup char(20) not null,

	prodef_id varchar2(50), 
	prodef_desc varchar2(500), 
	prodef_name varchar2(50), 
	prodef_version varchar2(50),
	processxml blob, 
  diagramimg blob,
  ispublic char(1),
  serverclass varchar2(50), 
  pk_group char(20) default '~', 
  pk_org char(20) default '~', 
  dr number(10,0) default 0, 
	ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
  validity int,
  pk_bizobject char(20),
  pk_biztrans char(20),
	constraint pk_wf_prodef primary key (pk_prodef)
);

--流程实列表
drop table wf_proins;
 CREATE TABLE wf_proins 
(
  pk_prodef char(20) not null, 
  pk_group char(20) default '~', 
  pk_org char(20) default '~',
  pk_proins char(20) default '~', 
	pk_parent char(20) default '~',
	pk_super char(20) default '~',
  pk_starttask char(20), 
	pk_starter char(20), 
	pk_form_ins char(20),
  pk_form_ins_version char(20),
  startdate char(19),
  enddate   char(19),
  duedate  char(19),
  title varchar2(50),
  state_proins int,  
  dr number(10,0) default 0, 
	ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	 constraint wf_pk_proins primary key (pk_proins)
);
--活动实列表
drop table wf_actins;
 CREATE TABLE wf_actins 
(
  pk_actins char(20) not null, 
  pk_proins char(20) default '~', 
  begindate char(19),
  state_actins int,  
  pk_parent char(20) default '~', 
  pk_super char(20) default '~',
	port_id varchar(50), 
  pport_id varchar(50), 
  pk_prodef char(20),
  prodef_id varchar(100),
	isexe char(1), 
	ispass char(1),
  isreject char(1),
  dr number(10,0) default 0, 
	ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	 constraint wf_pk_actins primary key (pk_actins)
);
--任务表
drop table wf_task;
CREATE TABLE wf_task 
(
  pk_task char(20) not null, 
  pk_parent char(20) default '~', 
  pk_process_instance char(20) default '~',
  pk_process_def char(20) default '~', 
  process_def_id varchar2(100), 
  process_def_name char(20), 
  pk_activity_instance char(20),
  pk_owner char(20),
  pk_creater   char(20),
  pk_executer  char(20),
  pk_agenter char(20),
  pk_super char(20),
  isexec     char(1),
  ispass    char(1),
  activity_id     varchar2(100),
  activity_name   varchar2(100),
  state_task    int,  
  create_type   int,
  finish   char(20),
  finish_type   int,
  action_type   int,
  startdate char(19),
  begindate   char(19),  
  signdate   char(19),
  finishdate     char(19),
  dutedate    char(19),
  standingdate varchar2(50), 
  handlepiece   varchar2(50),
  pk_ownerdept char(20),
  opinion   varchar2(50),
  scratch varchar2(50),  
  priority   char(2),
  pk_group   char(20),
  pk_org     char(20),
  pk_form_ins  char(20),
  pk_form_ins_version char(20),
  form_no    varchar(100),
  pk_bizobject varchar(100),
  pk_biztrans varchar(100),
  actiontype varchar(100),
  title         varchar2(50),
  beforeaddsign_times varchar(5),
  pk_myvisa    char(20),
  userobject   varchar2(256),
  openUIStyle  varchar2(50),
  openURI varchar(256),
  ext0 varchar2(50),
  ext1 varchar2(50),
  ext2 varchar2(50),  
  ext3 varchar2(50),
  ext4 varchar2(50),
  ext5 varchar2(50),  
  ext6 varchar2(50),
  ext7 varchar2(50),
  ext8 varchar2(50),   
  ext9 varchar2(20),
  dr number(10,0) default 0,   
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	 constraint wf_pk_task primary key (pk_task)
)
--指派信息表
drop table wf_assignment;
 CREATE TABLE wf_assignment 
(
  pk_assignment char(20) not null, 
  pk_proins char(20) default '~', 
	activity_id varchar(50), 
  pk_user   char(20), 
  order_str varchar(20),
  sequence char(1),
  dr number(10,0) default 0, 
	ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	constraint wf_pk_assignment primary key (pk_assignment)
);
--变量信息表
drop table wf_variable;
 CREATE TABLE wf_variable 
(
  pk_variable char(20) not null,
  code varchar2(100) ,
  pk_process_instance char(20) default '~', 
	pk_activity_instance char(20), 
  pk_task   char(20), 
  longvalue varchar(100),
  doublevalue varchar(100),
  textvalue varchar(100),
  textvalue2 varchar2(100),
  objectvalue blob, 
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	constraint wf_pk_variable primary key (pk_variable)
);
--前加签信息表
drop table wf_beforeaddsign;
CREATE TABLE wf_beforeaddsign (
  pk_beforeaddsign char(20) not null,
  pk_task char(20) ,
  times varchar(20) default '~', 
  logic   varchar(20), 
  scratch varchar(500),
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
	constraint wf_pk_beforeaddsign primary key (pk_beforeaddsign)
);

--前加签用户表
drop table wf_beforeaddsignuser;
create table wf_beforeaddsignuser(
  pk_beforeaddsignuser char(20) not null,
  pk_beforeaddsign char(20) not null,
  pk_user char(20)  null,
  pk_dept char(20)  null,
  isusered char(1) null,
  order_str varchar(20)  null,
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
  constraint wf_pk_beforeaddsignuser primary key (pk_beforeaddsignuser)
)

--订阅信息表
drop table wf_subscription;
create table wf_subscription(
  pk_subscription char(20) not null,
  pk_execution char(20) not null,
  pk_processInstance char(20) not null,
  eventtype varchar(50)  null,
  eventname varchar(50)  null,
  activity_id varchar(50) null,
  configuration varchar(50)  null,
  created char(19) null,
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
  constraint wf_pk_subscription primary key (pk_subscription)
)


--job调度表
drop table wf_job;
create table wf_job(
  pk_job char(20) not null,
  pk_execution char(20) not null,
  pk_processInstance char(20) not null,
  type varchar(50)  null,
  lockowner varchar(50)  null,
  jobhandlertype varchar(50) null,
  jobhandlerconfiguration varchar(200)  null,
  duedate char(19) null,
  lockexpirationtime char(19) null,
  retries int,
  isexclusive char(1) null,
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
  constraint pk_wf_job primary key (pk_job)
)


drop table wf_prodefgroup
create table wf_prodefgroup(
  pk_prodefgroup char(20) not null,
  pk_parentgroup char(20) not null,
  code varchar2(50) not null,
  dr number(10,0) default 0, 
  ts char(19) default to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'), 
  name varchar2(50) null
  constraint pk_wf_prodefgroup primary key (pk_prodefgroup)
)






