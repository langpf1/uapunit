//define(["i18n!wf-zh"]{
define({
	WF:{
		STATE:{
			DRAFT : 0, 
			INVALID: 1, 
			SUSPEND : 2,
			AFTERSIGN : 3
		},
		OPERATION:{
			APPROVE : 0,  //批准
			UNAPPROVE : 1, //不批准
			REJECT : 2, //驳回
			ADDSIGN : 3, //加签
			ASSIGN : 4, //指派
			REASSIGN : 5 //改派
		},
		TASK:{
			PREFIX_BUTTON:"t",
			PREFIX_DROPDOWN:"tm",
			PREFIX_SPLITFLAG:"_"
		},
		NOTIFY_TYPE:{
			MESSAGE:0,
			EMAIL:1
		},
		ADDSIGN_TYPE:{
			BEFORE:0,
			AFTER:1
		},
		Pagination:{
			TotalRecords : null,
			TotalPages : null,
			PageSize : 10,
			PageNumber : 1
		},
		HISTORY_KEY:{
			Activity_INS:"data-activity-instanceid"
		}
		
		
	}
});