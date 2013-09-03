// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'wf-console/workflow',
  'wf-console/workflowlist',
  'text!../../templates/workflowtemplate.html',
  'common'
], function($, _, Backbone, WorkFlow, WorkFlowList,WorkFlowTemplate,Common){
  var WorkFlowView = Backbone.View.extend({
    
    el: $("#process-container"),
    events:{
      'click #process-first': 'firstPage',
      'click #process-previous': 'previous',
      'click #process-next': 'next',
      'click #process-end': 'goEnd'
    },
    firstPage: function(){
      Common.WF.Pagination.PageNumber=1;
      this.processfetch(Common.WF.Pagination);
    },
    previous: function() {
      if(Common.WF.Pagination.PageNumber>1)
      {
        Common.WF.Pagination.PageNumber--;
        this.processfetch(Common.WF.Pagination);
      }
    },
    next: function() {
      if(Math.ceil(Common.WF.Pagination.TotalRecords/Common.WF.Pagination.PageSize)>Common.WF.Pagination.PageNumber)
      {
        Common.WF.Pagination.PageNumber++;
        this.processfetch(Common.WF.Pagination);
      }
    },
    goEnd: function() {
        Common.WF.Pagination.PageNumber=Math.ceil(Common.WF.Pagination.TotalRecords/Common.WF.Pagination.PageSize);
        this.processfetch(Common.WF.Pagination);
    },
    processfetch:function(page){
      this.collection.fetch({data:{pagenation:page},
          success:function(wfListData,response){
            Common.WF.Pagination.TotalRecords=response[response.length-1];
            wfListData.remove(wfListData.models[wfListData.models.length-1]);
            var compiledTemplate = _.template( WorkFlowTemplate, { workflowlist: wfListData.models} );
            $("#process-container").html( compiledTemplate ); 
          },
          error:function(wfListData, response){
              console.log(wfListData);
              console.log(response);
              alert('error');
          }
      });
    },
    render: function(){
      $("#logout").click(function(){
        var date=new Date();
        date.setTime(date.getTime()-10000);
        document.cookie="name=a;expires="+date.toGMTString();
        $("#user-name").text(document.cookie);
        window.location.href="login.html";
      });
    $(document).ready(function(){
      //editables
      $("#user-name").text(document.cookie.substring(5,document.cookie.length));
      
      });
      console.log("render workflow list view...");
      this.collection = new WorkFlowList();
      this.processfetch(Common.WF.Pagination);
    }
  });
  return WorkFlowView;
});