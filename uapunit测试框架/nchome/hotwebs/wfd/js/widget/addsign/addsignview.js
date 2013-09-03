// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/addsign/addsignmodel',
  'text!../../../templates/addsign.tpl.html',
  'participants',
  'roles',
  'common'
], function($, _, Backbone, AddsignModel,AddsignTemplate,Participants,Roles,Common){
 var AddsignView = Backbone.View.extend({
    
    el: $("#modal-container"),
    events:{
      'click #addsign-ok':'addsignTask',
      'click #addsign-cancel':'addsignCanceled',
      'click #addsign-track' : 'trackClicked',
      'click input[name="addsign-type"]' : 'addsignTypeClicked',
      'change #addsign-comment': 'commentChanged'
    },
    initialize:function(){
     /* console.log("taskpkid:"+task.pk_task);*/
      
    },
    commentChanged:function(){
      this.model.set('comment',$("#addsign-comment").val());
    },
    trackClicked:function(){
      if($("#addsign-track").prop('checked')){
        this.model.set('track',true);
      }
      else
      {
        this.model.set('track',false); 
      }      
            
    },
    addsignTypeClicked:function(){
      if($("#addsign-before").prop('checked')){
        this.model.set('addsignType',Common.WF.ADDSIGN_TYPE.BEFORE);
      }
      else
      {
        this.model.set('addsignType',Common.WF.ADDSIGN_TYPE.AFTER); 
      }      
            
    },
    addsignTask:function(){
      console.log("addsigned");
      this.model.save();  
      $('#addsign').modal("hide");
      this.model.clear();
      this.undelegateEvents();  
    },
    addsignCanceled:function(){
      console.log('addsign canceled');
      $('#addsign').modal("hide");
      this.model.clear();
      this.undelegateEvents();  
    },
    render: function(taskid){
      console.log("render addsign view...");
      var addsignUsers=new Participants();
      var participantSource=[];
      
      addsignUsers.fetch({
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

      var addsignRoles=new Roles();
      var roleSource=[];

      addsignRoles.fetch({
        success:function(roleData,response){
         roleData.each(function(role){
              var roleRow={id:role.get("roleID"),text:role.get("name")};
              roleSource.push(roleRow);
          });
        },
        error:function(roleData, response){
            console.log(roleData);
            console.log(response);
            alert('error');
        }
      });

      var addsignModel=new AddsignModel();
      addsignModel.set('taskID',taskid);
      addsignModel.set('track',true);
      this.model=addsignModel;
      var compiledTemplate = _.template( AddsignTemplate, { addsignModel: this.model,Common:Common} );
      $("#modal-container").html( compiledTemplate ); 
      //抄送人员选择
      $('#addsign-user-tag').tags( {
        excludes : excludes,
        tagRemoved: function(tag) { console.log(tag) }
      } );

      //抄送角色选择
      $('#addsign-role-tag').tags( {
        tagClass: 'btn-success',
        excludes : excludes,
        tagRemoved: function(tag) { console.log(tag) }
      } );

      //
      $('#addsign-add-user').editable({
        inputclass: 'input-large',
        placement:'right',
        source: participantSource,
        display:function(value,sourceData){
          $(this).html("添加用户:");
        }
      });

      $('#addsign-add-user').on('save',function(e,params){
        var text;

        participantSource.map(function(participant){
          if(participant.id==params.newValue)
            text=participant.text;
        });
        
        $('#addsign-user-tag').tags().addTag(text);
        var selectedUser=participantCol.where({participantID:params.newValue});
        that.model.get('participants').push(selectedUser[0]);
      });

      $('#addsign-add-role').editable({
        inputclass: 'input-large',
        placement:'right',
        source: roleSource,
        display:function(value,sourceData){
          $(this).html("添加角色:");
        }
      });

      var roleId=[];
      var that=this;
      $('#addsign-add-role').on('save',function(e,params){
        var text;
        roleSource.map(function(role){
          if(role.id==params.newValue)
            text=role.text;
        });
        $('#addsign-role-tag').tags().addTag(text);
        //TODO:应使用findWhere方法,但当前此方法出现异常，原因不明;
        var selectedRole=roleCol.where({roleID:params.newValue});
        that.model.get('roles').push(selectedRole[0]);
      });
    }
  });
  return AddsignView;
});