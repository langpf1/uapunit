// Filename: task
define([
  'underscore',
  'backbone',
  'utils'
], function(_, Backbone){
  var WorkFlow = Backbone.Model.extend({
    defaults: {
      pk_prodef:null,
      ts:null,
      status:null,
      prodef_id:null,
      pk_bizobject:null,
      prodef_version:null,
      prodef_name:null  
    },
    initialize: function(){
        // this.bind("change:name", function(){
        //     var name = this.get("name"); 
        //     console.log('Name updated to ' + name)
        // });
      console.log('WorkFlow model has been initialized');
      this.bind("error",function(model,error){
        console.log(error);
      })
    }
  });
  // Return the model for the module
  return WorkFlow;
});