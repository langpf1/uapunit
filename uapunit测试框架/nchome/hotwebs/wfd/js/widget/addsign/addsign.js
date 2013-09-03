define([
  'underscore',
  'backbone',
  'widget/addsign/addsignmodel',
  'widget/addsign/addsignview'
],function(_,Backbone,AddsignModel,AddsignView){
  return{
          Model : AddsignModel,
          View  : AddsignView
  };
})  