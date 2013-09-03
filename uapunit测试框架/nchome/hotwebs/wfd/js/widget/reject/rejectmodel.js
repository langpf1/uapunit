define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Common){
  	var RejectModel=Backbone.Model.extend({
      url:'../wfd/runtime/task/reject/',
  		defaults:{
        taskID:null,
        rejectNodeID : null,
        pk_form_ins_version : null,
        pk_bizobject:null,
  			comment:'备注',
  			track:false,
        actioncode:Common.WF.OPERATION.REJECT,
        cc:null
  		},
  		initialize:function(){
  			console.log('reject initialized');
  		}
  	});
  	return RejectModel;
  });