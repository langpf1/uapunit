// Filename: task/
define([
  'jquery',
  'underscore',
  'backbone',
  'widget/history/history',
  'widget/history/historicactivity',
  'text!../../../templates/history.tpl.html'
], function($, _, Backbone, WfHistory,HistoricActivity,HistoryTemplate){
 var HistoryView = Backbone.View.extend({
    //基于delegate事件绑定机制，事件必须绑定到静态根元素上
    el: $("#modal-container"),
    events:{
      'click #history-close':'historyClosed'
      
    },
    initialize:function(){
     /* console.log("taskpkid:"+task.pk_task);*/
     
    },
    historyClosed:function(){
      $('#history').modal("hide");
      this.model.clear();
    },
    historyfetch:function(){

    },
    render: function(activityInstanceID){
      console.log("render history view...");
      var historyModel=new WfHistory();
      this.model=historyModel;
      //现象：由于model.fetch方法callback方法,需要手动触发，具体表现为：需点击两次才能显示数据，
      //解决方式：由于第一次调用时，只绑定但不执行fetch方法，所以将show方法放到fetch方法体中
      this.model.fetch({data:{activityInsID:activityInstanceID},
        success:function(historyData,response){
          var compiledTemplate = _.template( HistoryTemplate, { historyModel: historyData } );
          $("#modal-container").html( compiledTemplate ); 
          $('#history').modal('show');
        },
        error:function(historyData, response){
            console.log(historyData);
            console.log(response);
            alert('error');
        }
      });
    }
  });
  return HistoryView;
});