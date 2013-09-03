define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var Participant=Backbone.Model.extend({
  		defaults:{
  			name : null,
  			participantID : null
  		},
  		initialize:function(){
  			console.log('Participant Model initialized');
  		}
  	});
  	return Participant;
  });