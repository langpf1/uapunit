define([
  'underscore',
  'backbone'
  ],function(_,Backbone){
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