define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var Attachment=Backbone.Model.extend({
  		defaults:{
  			name : null,
        actionName : null,
        uploadUser : null,
  			uploadDate : null
  		},
  		initialize:function(){
  			console.log('Attachment Model initialized');
  		}
  	});
  	return Attachment;
  });