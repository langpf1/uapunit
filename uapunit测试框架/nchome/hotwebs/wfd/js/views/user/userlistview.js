define(function(){
     
    function render(parameters){
        var appUl = document.getElementById('dashboard-statistics');
 
        var users = parameters.users;
       
        var html = '';
        for (var i = 0, len = users.length; i < len; i++){
            html += '<li><a href="#"><i class="icon-fire"></i><span class="yellow">400</span>' + users[i].name + '</a></li>';
        }
         
        appUl.innerHTML = html;
    }
 
    return {
        render:render
    };
});