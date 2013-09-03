define([
  '../libs/underscore/underscore',
  '../libs/backbone/backbone'
  ],function(_,Backbone){
  	var User=Backbone.Model.extend({
      url:'../wfd/runtime/task/todo/',
  		defaults:{
  			name : null,
  			password : null
  		},
  		initialize:function(){
  			console.log('User Model initialized');
  		}
  	});
  	return User;
  });