define([
  'underscore',
  'backbone',
  'widget/approve/approvemodel',
  'widget/approve/approveview'
],function(_,Backbone,ApproveModel,ApproveView){
  return{
          Model : ApproveModel,
          View  : ApproveView
  };
})  