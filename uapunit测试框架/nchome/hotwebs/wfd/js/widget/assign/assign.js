define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var Assign=Backbone.Model.extend({
  		defaults:{
  			participants : [],
  			parallel : true,
        track:false,
        comment:'备注'
  		},
  		initialize:function(){
  			console.log('Assign initialized');
  		}
  	});
  	return Assign;
  });