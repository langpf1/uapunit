// Filename: main.js
// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
  baseUrl: "js",
  paths: {
    'jquery': 'libs/jquery/jquery-1.8.3',
    'jqueryUi': 'libs/jquery/jquery-ui/jquery-ui-1.9.2.custom.min',
    'underscore': 'libs/underscore/underscore',
    'backbone': 'libs/backbone/backbone',
    'bootbox': 'libs/bootbox/bootbox.min',
    'bootstrap': 'libs/bootstrap/bootstrap.min',
    'fullcalendar': 'libs/fullcalendar/fullcalendar.min',
    'highcharts': 'libs/chart/highcharts',
    'utis': 'utils'
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
    },
    'highcharts': {
      deps : ['jquery'],
      exports: 'Highcharts'
    },
    'fullcalendar': {
      deps: ['jquery', 'jqueryUi'],
      exports: 'fullCalendar'
    },
    'app' :{
      deps : ['highcharts'],
      exports :'App'
    }
  }
});

require([

// Load our app module and pass it to our definition function
'app','models/user/user','controllers/user/userlistcontroller' ], function(App,User,UserListController) {
  // The "app" dependency is passed in as "App"
  App.initialize();
  var users = [new User('Barney'),
                 new User('Cartman'),
                 new User('Sheldon')];
  UserListController.start(users); 

});