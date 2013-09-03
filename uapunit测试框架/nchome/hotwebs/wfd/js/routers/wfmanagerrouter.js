// Filename: router.js
define([
  'jquery',
  'underscore',
  'backbone',
  'wf-console/workflowview'
], function($, _, Backbone,WorkflowView){
  var WfmanagerRouter = Backbone.Router.extend({
    routes: {     
      '': 'processList',
      'error': 'defaultAction'
    },
    defaultAction:function(actions){
      // We have no matching route, lets just log what the URL was
      console.log('No route:', actions);

    },
    processList: function(actions)
    {
      console.log('route:Get process list');
      var workflowview = new WorkflowView();
      workflowview.render();
    }
  });
  var initialize = function(){
      var manager_router = new WfmanagerRouter();
      Backbone.history.start();
    };
    return {
      initialize: initialize
    };
});