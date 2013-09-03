define([ 
  'underscore',
  'backbone',
  'participant',
  'common'
  ],function(_,Backbone,Participant,Commonon){
    var Participants=Backbone.Collection.extend({
      model:Participant,
      url:'../wfd/runtime/participant/operator/',
      initialize: function(){

    },
      render:function(){

      }
    });
    return Participants;
  });