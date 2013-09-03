require.config({
  baseUrl: "js",
  paths: {
    'jquery': 'libs/jquery/jquery-1.9.1',
    'jqueryUi': 'libs/jquery/jquery-ui/jquery-ui-1.9.2.custom.min',
    'underscore': 'libs/underscore/underscore',
    'backbone': 'libs/backbone/backbone',
    'bootbox': 'libs/bootbox/bootbox.min',
    'bootstrap': 'libs/bootstrap/bootstrap.min',
    'wfmanagerrouter':'routers/wfmanagerrouter',
    'utis': 'utils',
    'common':'common/common'
  },
  shim: {
    'jquery' :{
      exports: '$'
    },
    'jqueryUi': {
      deps: ['jquery']
    },
    'underscore': {
      exports: "_"
    },
    'backbone': {
      //These script dependencies should be loaded before loading
      //backbone.js
      deps: ['jquery', 'underscore'],
      //Once loaded, use the global 'Backbone' as the
      //module value.
      exports: 'Backbone'
    },
    'bootstrap':{
      deps : ['jquery']
    },
    'bootbox' :{
      deps : ['bootstrap'],
      exports : 'bootbox'
    }
  }
});

require([

'wfmanagerrouter','bootstrap'], function(WfManagerRouter) {
  // The "app" dependency is passed in as "App"
  WfManagerRouter.initialize();

});