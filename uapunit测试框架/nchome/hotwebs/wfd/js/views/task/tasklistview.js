define(function(){
     
    function render(parameters){
        var appUl = document.getElementById('datatable');
 
        var tasks = parameters.tasks;
       
        var html = '';
        for (var i = 0, len = tasks.length; i < len; i++){
            html += '<tr><td>' + tasks[i].organization + '</td><td>'+tasks[i].billNo+'</td><td>'+tasks[i].user+
            '</td><td>'+tasks[i].date+'</td><td>'+tasks[i].state+'</td><td>'+tasks[i].customerName+'</td><td>'+tasks[i].actions+'</td></tr>';
        }
         
        appUl.innerHTML = html;
    }
 
    return {
        render:render
    };
});

// Filename: views/
define([
  'jquery',
  'underscore',
  'backbone',
  // Pull in the Collection module from above,
  'models/task/taskModel',
  'collections/task/taskListCollection'
], function($, _, Backbone, TaskModel, TaskListCollection){
  var UserListView = Backbone.View.extend({
    
    el: $("#dashboard-statistics"),

    render: function(){
      console.log("render user view...");
      this.collection = new UserListCollection();
      this.collection.add([{ name: "SO销售订单",age:100},
        { name: "提供轮胎库存量",age:66},
        { name: "发动机销售数量",age:2},
        { name: "fanfree",age:28},
        { name: "Lesely",age:26},
        { name: "Dabao",age:87}
        ]);
      //this.collection.url="api/userlist";
      //this.collection.fetch();
      // Compile the template using Underscores micro-templating
      var compiledTemplate = _.template( UserListTemplate, { users: this.collection.models } );
      $("#dashboard-statistics").html( compiledTemplate ); 
    }
  });
  return UserListView;
});