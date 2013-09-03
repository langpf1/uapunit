define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var ApproveModel=Backbone.Model.extend({
      url:'../wfd/runtime/task/approve/',
  		defaults:{
  			comment:'批准',
  			track:false,
        actioncode:Commonon.WF.OPERATION.APPROVE,
        cc:null,
        taskID:null,
        assign:[]
  		},
  		initialize:function(taskid){
       /* this.comment=comment;
        this.track=track;
        this.task=task;*/
  			console.log('approve initialized');
  		}
  	});
  	return ApproveModel;
  });