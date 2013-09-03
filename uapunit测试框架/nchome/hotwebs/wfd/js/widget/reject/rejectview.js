// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/reject/rejectmodel',
  'text!../../../templates/reject.tpl.html',
  'bootstrap-editable',
  'bootstrap-tags',
  'select2'
], function($, _, Backbone, RejectModel,RejectTemplate){
 var RejectView = Backbone.View.extend({
    
    el: $("#modal-container"),
    events:{
      'click #reject-ok':'rejectTask',
      'click #reject-cancel':'rejectCanceled',
      'click #reject-track' : 'trackClicked',
      'change #reject-comment': 'commentChanged',
      'select #reject-node-selected': 'selectChanged'
    },

    initialize:function(){
   
    },
    commentChanged:function(){
      this.model.set('comment',$("#reject-comment").val());
    },
    selectChanged:function(){
      this.model.set('rejectNodeID',$("#reject-node-selected").val());
    },
    trackClicked:function(){
      if($("#reject-track").prop('checked')){
        this.model.set('track',true);
      }
      else
      {
        this.model.set('track',false); 
      }      
            
    },
    rejectTask:function(){
      console.log("rejected");
      var selectNode=$("#reject-node-selected");
      this.model.set('rejectNodeID',selectNode.val());
      this.model.save();  
      $('#reject').modal("hide");
      this.model.clear();
      this.undelegateEvents();  
    },
    rejectCanceled:function(){
      console.log('reject canceled');
      $('#reject').modal("hide");
      this.model.clear();
      this.undelegateEvents();
    },
    render: function(task){
      console.log("render reject view...");
      var rejectModel=new RejectModel();
      rejectModel.set('taskID',task.get('taskID'));
      rejectModel.set('pk_form_ins_version',task.get('pk_form_ins_version'));
      rejectModel.set('pk_bizobject',task.get('pk_bizobject'));
      var rejectNodes=task.get('rejectNodes');
      this.model=rejectModel;
      var compiledTemplate = _.template( RejectTemplate, { rejectModel: this.model,rejectNodes:rejectNodes} );
      $("#modal-container").html( compiledTemplate ); 

    }
  });
  return RejectView;
});