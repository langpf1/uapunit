define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'js/task/task',
  'js/task/tasklist',
  'js/task/taskview'
  ], function($,_,backbone,bootstrap,task,tasklist,taskview) {
  var initialize = function() {
  	tasklist=new TaskList();
  	taskview=new TaskView;
  };
  return {
    initialize: initialize
  };
})