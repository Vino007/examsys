/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    $.ajax({
        url: '../mock/user.json',
        async: true,
        type: 'GET'
    }).done(function (data) {
        if (data.success) {
            $.each(data.data, function (key, value) {
                var tr = '<tr><td>' + value.username + '</td><td>' + value.role + '</td><td>' + value.createTime + '</td><td>' + value.creator + '</td><td><button class="btn btn-block btn-primary edit-user"data-toggle="modal"data-target="#edit-user">编辑</button></td></tr>';
                $('#user').append(tr);
            })
        }
    });

    //page
    $('#pagination').twbsPagination({
        totalPages: 10,
        visiblePages: 5,
        onPageClick: function (event, page) {

        }
    });

    //edit course modal
    $(document).on('click', '.edit-user', function () {
        var that = this;

        $.each($('.edit-form .form-group'), function (key, value) {
            $(value).children().eq(1).val($(that).parents().siblings('td').eq(key).text());
        });
    });

    $('#edit-submit').click(function () {
        var submitData = $('.edit-form').serializeArray();
        var usrObj = {
            name: 'username',
            value: sessionStorage.getItem('username')
        };
        submitData.push(usrObj);

        $.ajax({
            url: '',
            async: true,
            type: 'POST',
            data: getFormJson(submitData)
        }).done(function (data) {
            if(data.success){
                alert('修改成功');
                window.location.reload();
            }
        });
    });

    function getFormJson(formArray) {
        var jsonobj = {};
        $.each(formArray, function (key, value) {
            jsonobj[this.name] = this.value;
        })
        return jsonobj;
    }
});