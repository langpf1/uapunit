define([ 
  'underscore',
  'backbone',
  'role',
  'common'
  ],function(_,Backbone,Role,Commonon){
    var Roles=Backbone.Collection.extend({
      model:Role,
      url:'../wfd/runtime/participant/role/',
      initialize: function(){

    },
      render:function(){

      }
    });
    return Roles;
  });