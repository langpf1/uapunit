//add by kongml
define([
  'jquery',
  'underscore',
  'backbone',
  'bill/customerrouter'
  ], function($,_,backbone,CustomerRouter) {
      var initialize = function() {

        CustomerRouter.initialize();
      };
   
    return {
      initialize: initialize
    };
})