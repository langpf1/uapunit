// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/assign/assign',
  'text!../../../templates/assign.tpl.html',
  'participants',
  'bootstrap-editable',
  'bootstrap-tags',
  'select2'
], function($, _, Backbone, AssignModel,AssignTemplate,Participants){
 var AssignView = Backbone.View.extend({
    //基于delegate事件绑定机制，事件必须绑定到静态根元素上
    el: $("#modal-container"),
    events:{
      'click #assign-track' : 'trackClicked',
      'click #assign-parallel' : 'parallelClicked',
      'change #assign-comment': 'commentChanged'
    },
    initialize:function(){
     /* console.log("taskpkid:"+task.pk_task);*/
     
    },
    commentChanged:function(){
      this.model.set('comment',$("#assign-comment").val());
    },
    parallelClicked:function(){
      if($("#assign-parallel").prop('checked')){
        this.model.set('parallel',true);
      }
      else
      {
        this.model.set('parallel',false); 
      }      
            
    },
    trackClicked:function(){
      if($("#assign-track").prop('checked')){
        this.model.set('track',true);
      }
      else
      {
        this.model.set('track',false); 
      }      
            
    },
    render: function(taskid){
      console.log("render assign view...");
      var assignModel=new AssignModel();
      assignModel.set('participants',[]);
      this.model=assignModel;
      participantCol=new Participants();
      var participantSource=[];
      
      participantCol.fetch({

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

      var compiledTemplate = _.template( AssignTemplate, { assignModel: this.model } );
      $("#assign-tab").html( compiledTemplate ); 

      //抄送人员选择
      $('#assign-user-tag').tags( {
        excludes : excludes,
        tagRemoved: function(tag) { console.log(tag) }
      } );

      //
      $('#assign-add-user').editable({
        inputclass: 'input-large',
        placement:'right',
        source: participantSource,
        display:function(value,sourceData){
          $(this).html("添加用户:");
        }
      });
      var that=this;
      $('#assign-add-user').on('save',function(e,params){
        var text;

        participantSource.map(function(participant){
          if(participant.id==params.newValue)
            text=participant.text;
        });
        $('#assign-user-tag').tags().addTag(text);
        var selectedUser=participantCol.where({participantID:params.newValue});
        that.model.get('participants').push(selectedUser[0]);
      });
    }
  });
  return AssignView;
});