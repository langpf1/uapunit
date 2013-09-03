define([
  'underscore',
  'backbone',
  'common'
  ],function(_,Backbone,Commonon){
  	var HistoricActivity=Backbone.Model.extend({
  		defaults:{
  			pk_executer :null,
  			activity_name : null,
        begindate:null,
        finishdate:null,
        comment:null
  		},
  		initialize:function(){
  			console.log('Historic activity initialized');
  		}
  	});
  	return HistoricActivity;
  });