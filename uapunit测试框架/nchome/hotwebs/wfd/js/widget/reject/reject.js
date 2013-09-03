define([
  'underscore',
  'backbone',
  'widget/reject/rejectmodel',
  'widget/reject/rejectview'
],function(_,Backbone,RejectModel,RejectView){
  return{
          Model : RejectModel,
          View  : RejectView
  };
})  