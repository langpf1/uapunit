// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  // Pull in the Collection module from above,
  'task/tasklist',
  'text!../../templates/complated.tpl.html',
  'widget/history/historyview',
  'common'
], function($, _, Backbone,TaskList,ComplatedTemplate,Historyview,Common){
  var ComplatedTaskView = Backbone.View.extend({
    
    el: $("#complated-container"),
    events:{
     'click #wf-complated-history':'showComplateHistoryDialog',
     'click #completed-first': 'firstPage',
     'click #completed-end': 'goEnd',
     'click .page': 'goPage'
    },
    firstPage: function(){
      Common.WF.Pagination.PageNumber=1;
      this.completefetch(Common.WF.Pagination);
    },
    goEnd: function() {
        Common.WF.Pagination.PageNumber=Math.ceil(Common.WF.Pagination.TotalRecords/Common.WF.Pagination.PageSize);
        this.completefetch(Common.WF.Pagination);
    },
    goPage:function(e) {
        Common.WF.Pagination.PageNumber=e.target.id;
        this.completefetch(Common.WF.Pagination);
    },
    showComplateHistoryDialog:function(e){
      console.log("Show complated history dialog")
     
      var activityInstanceID=e.target.attributes["data-activity-instanceid"].value;
      console.log("Show completed history dialog:      "+activityInstanceID);
      var histroyView = new Historyview();

      console.log("render history view");
      histroyView.render(activityInstanceID);
    },
    completefetch:function(page){
      this.collection.fetch({
          url:'../wfd/runtime/task/complete/',data:{pagenation:page},
          beforeSend:function(){
            var compiledTemplate = '<ul class="bokeh"><li></li><li></li><li></li><li></li></ul>';
            $("#complated-container").html( compiledTemplate ); 
          },   
          success:function(taskListData,response){
            Common.WF.Pagination.TotalRecords=response[response.length-1];
            taskListData.remove(taskListData.models[taskListData.models.length-1]);
            var compiledTemplate = _.template(ComplatedTemplate, { tasklist: taskListData.models,_Common:Common } );
            $("#complated-container").html(compiledTemplate); 
          },
          error:function(taskListData, response){
              console.log(taskListData);
              console.log(response);
              alert('error');
          }
      });
    },
    render: function(){
      console.log("render complated task list view...");
      this.collection = new TaskList();
      this.completefetch(Common.WF.Pagination);      
    }
  });
  return ComplatedTaskView;
});