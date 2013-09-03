define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var Node=Backbone.Model.extend({
  		defaults:{
  			name : null,
  			nodeID : null
  		},
  		initialize:function(){
  			console.log('Node Model initialized');
  		}
  	});
  	return Node;
  });