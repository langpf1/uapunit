define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var CcModel=Backbone.Model.extend({
  		defaults:{
  			participants : [],
  			roles : [],
  			notifytype : [Commonon.WF.NOTIFY_TYPE.MESSAGE,Commonon.WF.NOTIFY_TYPE.EMAIL]
  		},
  		initialize:function(){
  			console.log('ccModel initialized');
  		}
  	});
  	return CcModel;
  });