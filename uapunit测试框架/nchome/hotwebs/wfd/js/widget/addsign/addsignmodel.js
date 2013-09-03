define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Common){
  	var AddsignModel=Backbone.Model.extend({
      url:'../wfd/runtime/task/addsign/',
  		defaults:{
        taskID:null,
        participants:[],
        roles:[],
        parallel:true,
        addsignType:Common.WF.ADDSIGN_TYPE.BEFORE,
  			comment:'备注',
  			track:false,
        actioncode:Common.WF.OPERATION.ADDSIGN,
        cc:null
  		},
  		initialize:function(taskid){
       /* this.comment=comment;
        this.track=track;
        this.task=task;*/
  			console.log('Addsign initialized');
  		} 
  	});
  	return AddsignModel;
  });