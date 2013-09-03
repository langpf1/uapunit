define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var WfHistory=Backbone.Model.extend({
      url:'../wfd/runtime/history/',
  		defaults:{
  			historicActivities : [],
        imageUrl:null
  		},
  		initialize:function(){
  			console.log('WfHistory initialized');
  		}
  	});
  	return WfHistory;
  });