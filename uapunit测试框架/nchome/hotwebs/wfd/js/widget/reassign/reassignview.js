// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/reassign/reassignmodel',
  'text!../../../templates/reassign.tpl.html',
  'participants',
  'common'
], function($, _, Backbone, ReassignModel,ReassignTemplate,Participants,Common){
 var ReassignView = Backbone.View.extend({
    
    el: $("#modal-container"),
    events:{
      'click #reassign-ok':'reassignTask',
      'click #reassign-cancel':'reassignCanceled',
      'click #reassign-track' : 'trackClicked',
      'change #reassign-comment': 'commentChanged'
    },
    initialize:function(){
     /* console.log("taskpkid:"+task.pk_task);*/
      
    },
    commentChanged:function(){
      this.model.set('comment',$("#reassign-comment").val());
    },
    trackClicked:function(){
      if($("#reassign-track").prop('checked')){
        this.model.set('track',true);
      }
      else
      {
        this.model.set('track',false); 
      }      
            
    },
    reassignTask:function(){
      console.log("reassigned============================");
      this.model.save(); 
      $('#reassign').modal("hide");
      this.model.clear();
      this.undelegateEvents();  
    },
    reassignCanceled:function(){
      console.log('reassign canceled'); 
      $('#reassign').modal("hide");
      this.model.clear();
      this.undelegateEvents();  
    },
    render: function(taskid){
      console.log("render reassign view...");
      var reassignUser=new Participants();
      var participantSource=[];
      
      reassignUser.fetch({

        success:function(participantsData,response){
          participantsData.each(function(participant){
              var participantRow={id:participant.get("participantID"),text:participant.get("name")};
              participantSource.push(participantRow);
          });
        },
        error:function(participantsData, response){
            console.log(participantsData);
            console.log(response);
            alert('error');
        }
      });

      var reassignModel=new ReassignModel();
      reassignModel.set('taskID',taskid);
      reassignModel.set('track',true);
/*
      approveModel.task=task;*/
      this.model=reassignModel;
      var compiledTemplate = _.template( ReassignTemplate, { reassignModel: this.model } );
      $("#modal-container").html( compiledTemplate ); 

      //改派人员选择
      $('#reassign-user-tag').tags( {
        excludes : excludes,
        tagRemoved: function(tag) { console.log(tag) }
      } );
      $('#reassign-add-user').editable({
        inputclass: 'input-large',
        placement:'right',
        source: participantSource,
        display:function(value,sourceData){
          $(this).html("选择改派人:");
        }
      });
      var that=this;
      $('#reassign-add-user').on('save',function(e,params){
        var text;
        participantSource.map(function(participant){
          if(participant.id==params.newValue)
            text=participant.text;
        });        
        $('#reassign-user-tag').tags().addTag(text);
        var selectedUser=participantCol.where({participantID:params.newValue});
        that.model.get('reassign').push(selectedUser[0]);
      });
    }
  });
  return ReassignView;
});