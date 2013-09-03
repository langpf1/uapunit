define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var Role=Backbone.Model.extend({
  		defaults:{
  			name : null,
  			roleID : null
  		},
  		initialize:function(){
  			console.log('Role Model initialized');
  		}
  	});
  	return Role;
  });