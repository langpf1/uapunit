// Filename: main.js
// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
  baseUrl: "./js",
  paths: {
    'jquery': 'libs/jquery/jquery-1.9.1',
    'jqueryUi': 'libs/jquery/jquery-ui/jquery-ui-1.9.2.custom.min',
    'underscore': 'libs/underscore/underscore',
    'backbone': 'libs/backbone/backbone',
    'bootstrap': 'libs/bootstrap/bootstrap.min',
    'bootstrap-editable':'libs/bootstrap-editable/bootstrap-editable',
    'bootstrap-tags':'libs/bootstrap-tags/bootstrap-tags',
    'select2':'libs/select2',
    'taskapp':'task/taskapp',
    'approve':'widget/approve/approve',
    'reject':'widget/reject/reject',
    'reassign':'widget/reassign/reassign',
    'addsign':'widget/addsign/addsign',
    'utis': 'utils',
    'participants':'common/participants',
    'participant':'common/participant',
    'role':'common/role',
    'roles':'common/roles',
    'common':'common/common'
  },
  shim: {
    'jquery' :{
      exports: '$'
    },
    'jqueryUi': {
      deps: ['jquery'],
      exports:'$'
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
    },
    'bootstrap-editable':{
      deps : ['jquery','bootstrap'],
      exports:'editable'
    },
    'select2':{
      deps : ['bootstrap-editable'],
      exports: 'select2'
    },
    'bootstrap-tags':{
      deps : ['bootstrap'],
      exports: 'tags'
    },
    'taskapp' :{
      exports :'taskApp'
    },
    'approve' :{
      deps:[
        'backbone'
      ],
      exports :'Approve'
    }
  }
});

require([

// Load our app module and pass it to our definition function
'taskapp','approve','bootstrap-editable','bootstrap-tags','select2'], function(TaskApp,Approve) {
  // The "app" dependency is passed in as "App"
  TaskApp.initialize(Approve);
});