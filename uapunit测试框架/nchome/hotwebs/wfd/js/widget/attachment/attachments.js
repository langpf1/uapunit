define([ 
  'underscore',
  'backbone',
  'participant',
  'common'
  ],function(_,Backbone,Participant,Commonon){
    var Attachments=Backbone.Collection.extend({
      model:Attachment,
      url:'../wfd/runtime/participant/operator/',
      initialize: function(){

    },
      render:function(){

      }
    });
    return Attachments;
  });