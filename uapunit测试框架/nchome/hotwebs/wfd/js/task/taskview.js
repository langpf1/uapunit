// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  // Pull in the Collection module from above,
  'task/task',
  'task/tasklist',
  'text!../../templates/todo.tpl.html',
  'approve',
  'reject',
  'reassign',
  'addsign',
  'widget/cc/ccview',
  'widget/assign/assignview',
  'task/complatedtaskview',
  'widget/history/historyview',
  'common'
], function($, _, Backbone, Task, TaskList,TodoTemplate,Approve,Reject,Reassign,Addsign,CcView,AssignView,ComplatedTaskView,Historyview,Common){
  var TaskView = Backbone.View.extend({
    
    el: $("#todo-container"),
    events:{
      'click #handle-click-btn':'showApproveDialog',
      'click #approveclick-btn':'showApproveDialog',
      'click #rejectclick-btn':'showRejectDialog',
      'click #reassignmentclick-btn':'showReassignmentDialog',
      'click #addsignclick-btn':'showAddsignDialog',
      'click #wf-history':'showHistoryDialog',
      'click #todo-first': 'firstPage',
      'click #todo-end': 'goEnd',
      'click .page': 'goPage'
    },

    firstPage: function(){
      Common.WF.Pagination.PageNumber=1;
      this.taskfetch(Common.WF.Pagination);
    },
    goEnd: function() {
        Common.WF.Pagination.PageNumber=Math.ceil(Common.WF.Pagination.TotalRecords/Common.WF.Pagination.PageSize);
        this.taskfetch(Common.WF.Pagination);
    },
    goPage:function(e) {
        Common.WF.Pagination.PageNumber=e.target.id;
        this.taskfetch(Common.WF.Pagination);
    },
    showApproveDialog:function(e){
      var taskid=e.target.attributes["task-id"].value;
      var task=this.collection.where({taskID:taskid})[0];
      var approveView = new Approve.View();
      approveView.render(task);
    },
    showRejectDialog:function(e){
      console.log("Show reject dialog")
      var taskid=e.target.attributes["task-id"].value;
      var task=this.collection.where({taskID:taskid})[0];
      var rejectView = new Reject.View();
      console.log("render reject view");
      rejectView.render(task);
      console.log("render ccview");
      var ccview=new CcView();
      ccview.render();
      rejectView.model.set('cc',ccview.model);
      $('#reject').modal('show');
    },
    showReassignmentDialog:function(e){
      console.log("Show reassign dialog")
      var taskid=e.target.attributes["task-id"].value;
      var task=this.collection.where({taskID:taskid})[0];
     /* var task=_.findWhere(this.collection,{pk_task:"0001AA100000000252P3"});*/

      /*var testCol=[{test:"abc",ID:"0001AA100000000252P3"}];

      var task=_.findWhere(testCol,{test:"abc"});
      console.log(task.ID);*/
      var reassignView = new Reassign.View();
      console.log("render reassign view");
      reassignView.render(taskid);
      console.log("render ccview");
      var ccview=new CcView();
      ccview.render();
      reassignView.model.set('cc',ccview.model);
      $('#reassign').modal('show');
    },
    showAddsignDialog:function(e){
      console.log("Show addsign dialog")
      var taskid=e.target.attributes["task-id"].value;
      var task=this.collection.where({taskID:taskid})[0];
     /* var task=_.findWhere(this.collection,{pk_task:"0001AA100000000252P3"});*/

      /*var testCol=[{test:"abc",ID:"0001AA100000000252P3"}];

      var task=_.findWhere(testCol,{test:"abc"});
      console.log(task.ID);*/
      var addsignView = new Addsign.View();
      console.log("render addsign view");
      addsignView.render(taskid);
      console.log("render ccview");
      var ccview=new CcView();
      ccview.render();
      addsignView.model.set('cc',ccview.model);
      $('#addsign').modal('show');
    },
    showHistoryDialog:function(e){
     
      var activityInstanceID=e.target.attributes["data-activity-instanceid"].value;
      console.log("Show history dialog"+activityInstanceID);
      var histroyView = new Historyview();

      console.log("render history view");
      histroyView.render(activityInstanceID);
    },
    taskfetch:function(page){
      this.collection.fetch({data:{pagenation:page},
        beforeSend:function(){
          var compiledTemplate = '<ul class="bokeh"><li></li><li></li><li></li><li></li></ul>';
          $("#todo-container").html( compiledTemplate ); 
        },                
        success:function(taskListData,response){          
          Common.WF.Pagination.TotalRecords=response[response.length-1];
          taskListData.remove(taskListData.models[taskListData.models.length-1]);
          var compiledTemplate = _.template( TodoTemplate, { tasklist: taskListData.models,_Common:Common } );
          $("#todo-container").html( compiledTemplate ); 
        },
        error:function(taskListData, response){
            console.log(taskListData);
            console.log(response);
            alert('error');
        }
      });
      switch(Common.WF.Pagination.PageNumber)
      {
        case 1:
          $("#todo-first-page").className="disabled";
          break;
        case 2:
          $("#todo-second-page").className="disabled";
          break;
        case 3:
          $("#todo-first-page").className="disabled";
          break;
        case 4:
          $("#todo-first-page").className="disabled";
          break;
        case 5:
          $("#todo-first-page").className="disabled";
          break;
      }
    },
    render: function(){
      console.log("render task list view...");
      this.collection = new TaskList();
      this.taskfetch(Common.WF.Pagination);
      var complatedtaskview = new ComplatedTaskView();
      complatedtaskview.render();
    }
  });
  return TaskView;
});