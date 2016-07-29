/**
 * Created by chucc on 2016/7/22.
 */
$(document).ready(function () {
    $.ajax({
        url: '../mock/course.json',
        async: true,
        type: 'GET'
    }).done(function (data) {
        if (data.success) {
            $.each(data.data, function (key, value) {
                var tr = '<tr><td>' + value.courseName + '</td><td>' + value.courseClassification + '</td><td>' + value.courseType + '</td><td>' + value.courseOutline + '</td><td>' + value.courseTarget + '</td><td>' + value.courseTeacher + '</td><td>' + value.courseMaterial + '</td><td><button class="btn btn-block btn-primary edit-course"data-toggle="modal"data-target="#edit-course">编辑</button></td></tr>';
                $('#course').append(tr);
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
    $(document).on('click', '.edit-course', function () {
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
            if (data.success) {
                alert('修改成功');
                window.location.reload();
            }
        });
    });

    $('#delete-course').click(function () {
        if (confirm('确认删除？')) {
            alert('删除成功');
            window.location.reload();
        }
    });

    function getFormJson(formArray) {
        var jsonobj = {};
        $.each(formArray, function (key, value) {
            jsonobj[this.name] = this.value;
        })
        return jsonobj;
    }
});