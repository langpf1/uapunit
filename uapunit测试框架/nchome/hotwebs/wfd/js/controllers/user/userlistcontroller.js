define(['views/user/userlistview'], function(ListView){
     
    function start(users){
        //var users = JSON.parse(localStorage.users);
        ListView.render({users:users});
    }
     
    return {
        start:start
    };
});