/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: examResultSearch,
        data: {},
        resetTable: resetTable
    };
    init(initObj);


    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(examResultAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(examResultDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(examResultDelete);
    });

    /*delete end*/


    /*edit start*/
    //edit modal
    $(document).on('click', '.edit', function () {
        var that = this;

        $.each($('.edit-form .form-group'), function (key, value) {
            $(value).children().eq(1).val($(that).parents().siblings('td').eq(key + 1).text());
        });

        $('#edit-submit').attr('data-id', $(that).parents().siblings('td').eq(0).children().val());
    });

    //submit edit user
    $('#edit-submit').click(function () {
        var submitData = $('.edit-form').serializeArray();
        var id = $(this).attr('data-id');
        var idObj = {
            name: 'id',
            value: id
        };
        submitData.push(idObj);

        opADU(examResultUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search
    $('#search').click(function () {
        var searchArgs = {};
        $.each($('.conditional-search input'), function (key, value) {
            searchArgs[$(this).attr('name')] = $(this).val();
        });
        var initObj = {
            urlObj: examResultSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    //situation
    $('#result-situation').click(function () {
        var courseId = $('#situation-course-id').val();
        $.ajax({
            url: examResultGetSituation.url,
            type: examResultGetSituation.type,
            dataType: 'json',
            data: {'courseId': courseId}
        }).done(function (data) {
            if (data.success) {
                $.each(data.data, function (key, value) {
                    var time = toDateStr(value.time);
                    var tr = '<tr><td>' + value.examId + '</td><td>' + value.passCount + '</td><td>' + value.unpassCount + '</td><td>' + value.total + '</td><td>' + value.passPercent + '</td><td>' + time + '</td></tr>';
                    $('#situation-table').append(tr);
                });
            }
        });
    });

    //export result
    $('#export-result').click(function () {
        var ids = getCheckedIds();
        var url = examResultDownloadSpecial;
        $.ajax({
            url: url.url,
            type: url.type,
            data: {'ids': ids}
        }).done(function () {
            window.open('/download');
        });
    });

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var roles = '';
            if (value.user.roles.length == 1) {
                roles += value.user.roles[0].description;
            } else {
                $.each(value.user.roles, function (k, v) {
                    roles += v.description + ',';
                });
            }
            var createTime = toDateStr(value.exam.createTime);
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.user.username + '</td><td>' + value.user.department.deptName + '</td><td>' + roles + '</td><td>' + value.exam.course.courseName + '</td><td>' + value.exam.course.courseCategory.coursecatName + '</td><td>' + value.score + '</td><td>' + createTime + '</td><td>' + (value.isPass == 1 ? "是" : "否") + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#main-table').append(tr);
        });
    }
});