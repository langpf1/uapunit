// Filename: task
define([
  'underscore',
  'backbone',
  'utils'
], function(_, Backbone){
  var Task = Backbone.Model.extend({
    defaults: {
      taskID:null,
      operations:[0,1,2,3,4,5],
      rejectNodes:[],
      //enmu
      create_type: 2,
      ts: {
          "utcTime": 1365562747000
      },
      startdate: null,
      begindate: null,
      //是否通过
      ispass: {
          "value": false
      },
      process_def_name: null,
      //任务拥有者
      pk_owner: null,
      pk_process_instance: null,
      pk_creater: null,
      pk_parent: null,
      pk_task: null,
      pk_form_ins:null,
      pk_form_ins_version: null,
      pk_bizobject: null,
      pk_activity_instance: null,
      pk_group: null,
      pk_process_def: null,
      process_def_id: null,
      form_no: null,
      //是否已执行  
      isexec: {
          "value": false
      },
      //任务状态
      state_task: 5,
      //??
      handlepiece: 0,
      //活动类型：usertask subprocesstask...
      action_type: 0,
      activity_id: "_3",
      activity_name: "User Task",
      
      //??
      finish_type: -1,
      //no used
      dr: 0,
      
      //no used
      m_isDirty: false,
      //no used
      openUIStyle: "BisunessUI",
      //??      
      status: 0
    },
    initialize: function(){
        // this.bind("change:name", function(){
        //     var name = this.get("name"); 
        //     console.log('Name updated to ' + name)
        // });
      console.log('task model has been initialized');
      this.bind("error",function(model,error){
        console.log(error);
      })
    },
    validate: function(attribs){      
      if(attribs.billNo==null){
          return "error billNo null.";
      }
    }
  });
  // Return the model for the module
  return Task;
});