// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/approve/approvemodel',
  'text!../../../templates/approve.tpl.html',
  'widget/cc/ccview',
  'widget/assign/assignview',
  'common'
], function($, _, Backbone, ApproveModel,ApproveTemplate,CcView,AssignView,Common){
 var ApproveView = Backbone.View.extend({
    
    el: $("#modal-container"),
    events:{
      'click #approve-ok':'approveTask',
      'click #approve-cancel':'approveCanceled',
      'click #approve-track' : 'trackClicked',
      'change #approve-comment': 'commentChanged'
    },
    initialize:function(){
      console.log("Approve view initialize ==============");
      
    },
    commentChanged:function(){
      this.model.set('comment',$("#approve-comment").val());
    },
    trackClicked:function(){
      if($("#approve-track").prop('checked')){
        this.model.set('track',true);
      }
      else
      {
        this.model.set('track',false); 
      }      
            
    },
    approveTask:function(){
      console.log("approved ============================");
      this.model.save();  
      $('#approve').modal("hide");
      this.model.clear();
      /*view 多次render时，事件被多次绑定执行，
      http://stackoverflow.com/questions/10424160/why-is-the-click-event-triggered-several-times-in-my-backbone-app*/
      this.undelegateEvents();
    },
    approveCanceled:function(){
      console.log('approve canceled');
      this.model.set('actioncode',Common.WF.OPERATION.UNAPPROVE);
      this.model.save();  
      $('#approve').modal("hide");
      this.model.clear();
      this.undelegateEvents();
    },
    render: function(task){
      console.log("render approve view...");
      var approveModel=new ApproveModel();
      approveModel.set('taskID',task.get("taskID"));
      this.model=approveModel;
      var assignView;

      var compiledTemplate = _.template( ApproveTemplate, { approveModel: this.model } );

      $("#modal-container").html( compiledTemplate ); 

      if(_.indexOf(task.get('operations'),Common.WF.OPERATION.ASSIGN)!=-1)
      {
        console.log("render assign view");
        assignView=new AssignView();
        assignView.render();
        this.model.set('assign',assignView.model);
      }

      console.log("render ccview");
      var ccview=new CcView();
      ccview.render();
      this.model.set('cc',ccview.model);

      $('#approve').modal('show');
    }
  });
  return ApproveView;
});