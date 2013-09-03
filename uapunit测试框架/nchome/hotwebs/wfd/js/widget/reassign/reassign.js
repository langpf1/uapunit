define([
  'underscore',
  'backbone',
  'widget/reassign/reassignmodel',
  'widget/reassign/reassignview'
],function(_,Backbone,ReassignModel,ReassignView){
  return{
          Model : ReassignModel,
          View  : ReassignView
  };
})  