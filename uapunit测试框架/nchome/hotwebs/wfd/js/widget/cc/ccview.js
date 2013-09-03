// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/cc/ccmodel',
  'text!../../../templates/cc.tpl.html',
  'participants',
  'roles',
  'bootstrap-editable',
  'bootstrap-tags',
  'select2'
], function($, _, Backbone, CcModel,CcTemplate,Participants,Roles){
 var CcView = Backbone.View.extend({
    //基于delegate事件绑定机制，事件必须绑定到静态根元素上
    el: $("#modal-container"),
    events:{
      'click #cc-message' : 'messageClicked',
      'click #cc-email' : 'emailClicked'
    },
    initialize:function(){
     /* console.log("taskpkid:"+task.pk_task);*/
     
    },
    messageClicked:function(){
      if($("#cc-message").prop('checked')){
        this.model.set('notifytype',0);
      }
      else
      {
        this.model.set('notifytype',1); 
      }      
            
    },
    emailClicked:function(){
      if($("#cc-email").prop('checked')){
        this.model.set('notifytype',0);
      }
      else
      {
        this.model.set('notifytype',1); 
      }      
            
    },
    render: function(taskid){
      console.log("render cc view...");
      var ccmodel=new CcModel();
      ccmodel.set('roles',[]);
      ccmodel.set('participants',[]);
      this.model=ccmodel;
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

      roleCol=new Roles();
      var roleSource=[];

      roleCol.fetch({
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

      var compiledTemplate = _.template( CcTemplate, { ccmodel: this.model } );
      $("#cctab").html( compiledTemplate ); 

      //抄送人员选择
      $('#cc-user-tag').tags( {
        excludes : excludes,
        popoverData : ["What a wonderful day", "to make some stuff", "up so that I", "can show it works"],
        tagRemoved: function(tag) { console.log(tag) }
      } );

      //抄送角色选择
      $('#cc-role-tag').tags( {
        tagClass: 'btn-success',
        excludes : excludes,
        tagRemoved: function(tag) { console.log(tag) }
      } );

      //
      $('#cc-add-user').editable({
        inputclass: 'input-large',
        placement:'right',
        source: participantSource,
        display:function(value,sourceData){
          $(this).html("添加用户:");
        }
      });

      $('#cc-add-user').on('save',function(e,params){
        var text;

        participantSource.map(function(participant){
          if(participant.id==params.newValue)
            text=participant.text;
        });
        
        $('#cc-user-tag').tags().addTag(text);
        var selectedUser=participantCol.where({participantID:params.newValue});
        that.model.get('participants').push(selectedUser[0]);
      });

      $('#cc-add-role').editable({
        inputclass: 'input-large',
        placement:'right',
        source: roleSource,
        display:function(value,sourceData){
          $(this).html("添加角色:");
        }
      });

      var roleId=[];
      var that=this;
      $('#cc-add-role').on('save',function(e,params){
        var text;
        roleSource.map(function(role){
          if(role.id==params.newValue)
            text=role.text;
        });
        $('#cc-role-tag').tags().addTag(text);
        //TODO:应使用findWhere方法,但当前此方法出现异常，原因不明;
        var selectedRole=roleCol.where({roleID:params.newValue});
        that.model.get('roles').push(selectedRole[0]);
      });
    }
  });
  return CcView;
});