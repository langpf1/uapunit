define([ 
  'underscore',
  'backbone',
  'node',
  'common'
  ],function(_,Backbone,Node,Commonon){
    var Nodes=Backbone.Collection.extend({
      model:Node,
      initialize: function(){

    },
      render:function(){

      }
    });
    return Nodes;
  });