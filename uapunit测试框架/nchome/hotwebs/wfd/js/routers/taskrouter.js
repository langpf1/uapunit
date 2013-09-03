// Filename: router.js
define([
  'jquery',
  'underscore',
  'backbone',
  'task/taskview',
  'common'
], function($, _, Backbone,TaskView,Common){
  var TaskRouter = Backbone.Router.extend({
    routes: {     
      '': 'taskList',
      'error': 'defaultAction'
    },
    defaultAction:function(actions){
      // We have no matching route, lets just log what the URL was
      console.log('No route:', actions);

    },
    taskList: function(actions)
    {
      console.log('route:Get task list');
      var taskview = new TaskView();
      taskview.render();
    }
  });
  var initialize = function(){
      var task_router = new TaskRouter();
      Backbone.history.start();
    };
    return {
      initialize: initialize
    };
});