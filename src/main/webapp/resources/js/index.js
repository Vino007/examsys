/**
 * Created by chucc on 2016/7/21.
 */
$(document).ready(function () {
    $.ajax({
        url:'mock/login.json',
        type:'GET',
        async:false
    }).done(function (data) {
        
    });
    
    //user.json
    var usr = sessionStorage.getItem('username');
    $('.usr-info span').eq(1).text(usr);

    //get left-nav items
    var sessionData = JSON.parse(sessionStorage.getItem('data'));
    $.each(sessionData.data.menuPermissions, function (key, value) {
        var li = '<li><a href="' + value.url + '" target="body-content">' + value.name + '</a></li>';
        $('.left-nav-list').append(li);
    });

    //set default iframe
    $('.left-nav-list li:first').addClass('active');
    $('iframe[name=body-content]').attr('src', $('.left-nav-list li').children('a:first').attr('href'));

    //left-nav click event
    $('.left-nav-list li').click(function () {
        $('.left-nav-list li').removeClass('active');
        $(this).addClass('active');
    });
});