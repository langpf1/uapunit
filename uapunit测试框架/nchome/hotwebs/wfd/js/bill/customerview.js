// add by kongml
define([
  'jquery',
  'underscore',
  'backbone',
  'bill/customer',
  'text!../../bill/customertemplate.html'], function($, _, Backbone, Customer, CustomerTemplate) {
  var CustomerView = Backbone.View.extend({

    el: $("#modal-container"),
    events: {
      'click #btn_appr': 'onappr',
      'click #btn_save': 'onsave'
    },
    onsave: function() {
      this.model.setActionCode("create");
      this.model.save();
      getbill(this.model.get('billcode'));
      alert('保存成功！');
    },
    onappr: function() {
      this.model.setActionCode("approve");
      $.ajax({
        url: '../wfd/runtime/wfform/approve/',
        type: 'POST',
        data: this.model,
        dataType: 'json',
        error: function() {
          alert('JSON 数据传输错误');
        },
        success: function(data) {
          alert('提交成功！');
        }
      });
    },
    render: function() {
      this.model = new Customer();
      var compiledTemplate = _.template(CustomerTemplate, {
        model: this.model
      });
      $("#modal-container").html(compiledTemplate);
    },
    getbill: function(billcode) {
      //前台向后台传输的是 js对象
      this.model.setActionCode("query");
      $.ajax({
        url: '../wfd/runtime/wfform/bill/',
        type: 'POST',
        data: this.model,
        dataType: 'json',
        error: function() {
          alert('JSON 数据传输错误');
        },
        success: function(data) {
          alert(data);
        }
      });
    }
  });
  return CustomerView;
});