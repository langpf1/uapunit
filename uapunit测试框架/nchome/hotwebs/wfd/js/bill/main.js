// Filename: main.js
// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
  baseUrl: "./js",
  paths: {
    'jquery': 'libs/jquery/jquery-1.9.1',
    'underscore': 'libs/underscore/underscore',
    'backbone': 'libs/backbone/backbone',
    'bootstrap': 'libs/bootstrap/bootstrap.min',
    'billapp':'bill/billapp',
  },
  shim: {
    'jquery' :{
      exports: '$'
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
      deps : ['jquery'],
      exports:'bootstrap'
    }
  }
});

require(['billapp','jquery','underscore','backbone','bootstrap'], function(BillApp,$) {
	BillApp.initialize();
  // The "app" dependency is passed in as "App"
});