define([
  'jquery',
  'underscore',
  'backbone',
  'wf-console/workflow',
  'text!../../templates/workflowtemplate.html'
], function($, _, Backbone, WorkFlow,TaskTemplate){
  var WorkFlowList = Backbone.Collection.extend({
    model: WorkFlow,
    url:'../wfd/runtime/console/processdefinitions/',
    initialize: function(){

    }
  });
  return WorkFlowList;
});