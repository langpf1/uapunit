define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Common){
  	var ReassignModel=Backbone.Model.extend({
      //TODO:需修改
      url:'../wfd/runtime/task/reassign/',
  		defaults:{
        taskID:null,
        reassign : [],
  			comment:null,
  			track:false,
        actioncode:Common.WF.OPERATION.REASSIGN,
        cc:null
  		},
  		initialize:function(taskid){
  			console.log('reassign initialized');
  		}
  	});
  	return ReassignModel;
  });