define([
  'jquery',
  'underscore',
  'backbone',
  'routers/taskrouter'
  ], function($,_,backbone,TaskRouter) {
      var initialize = function(Approve) {

        TaskRouter.initialize();
      };
    $(function() { 
    // 
      whenAddingTag = function (tag) {
        console.log(tag);
        // maybe fetch some content for the tag popover (can be HTML)
      };
      excludes = function (tag) {
        // return false if this tagger does *not* exclude
        // -> returns true if tagger should exclude this tag
        // --> this will exclude anything with !
        return (tag.indexOf("!") != -1);
      }
    });
    $("#logout").click(function(){
      var date=new Date();
      date.setTime(date.getTime()-10000);
      document.cookie="name=a;expires="+date.toGMTString();
      $("#user-name").text(document.cookie);
      window.location.href="login.html";
      });
    $(document).ready(function(){
      //editables
      $("#user-name").text(document.cookie.substring(5,document.cookie.length));
      
      });
    return {
      initialize: initialize
    };
})