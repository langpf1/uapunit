// add by kongml
define([
  'jquery',
  'underscore',
  'backbone',
  'bill/customerview',
], function($, _, Backbone,CustomerView){
  var CustomerRouter = Backbone.Router.extend({
    routes: {     
      '': 'customerviewload',
      'error': 'defaultAction'
    },
    defaultAction:function(actions){
      // We have no matching route, lets just log what the URL was
      console.log('No route:', actions);

    },
    customerviewload: function(actions)
    {
      console.log('route:Get CustomerView');
      var customerview = new CustomerView();
      customerview.render();
    }
  });
  var initialize = function(){
      var customer_router = new CustomerRouter();
      Backbone.history.start();
    };
    return {
      initialize: initialize
    };
});