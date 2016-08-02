/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var initObj = {
        urlObj: examAll,
        data: {},
        resetTable: resetTable
    };
    init(initObj);

    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(courseAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(examDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(examDelete);
    });

    /*delete end*/


    /*edit start*/
    //edit modal
    $(document).on('click', '.edit', function () {
        var that = this;

        //set default value
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

        opADU(courseUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var search = $(this).siblings('input').val();
        var onlineOpt = ($('.is-online').length && $('.is-online').val() != 'true') ? ($('.is-online').val() ? false : '') : true;
        var searchArgs = {
            'search_courseName': search,
            'search_isOnline': onlineOpt
        };
        var initObj = {
            urlObj: examSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    /*cate based search start*/
    $('.cate-based-search').change(function () {
        var cateName = $('.cate-based-search option:selected').text();
        var id = $(this).val();
        if (id == 'all') {
            window.location.reload();
        } else {
            $.ajax({
                url: coursecatShowCourses.url,
                type: coursecatShowCourses.type,
                async: true,
                data: {'id': id},
                dataType:'json'
            }).done(function (data) {
                console.log(data)
                $('#main-table tr + tr').remove();
                $.each(data.data.courses, function (key, value) {
                    var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.courseName + '</td><td data-id="' + id + '">' + cateName + '</td><td>' + value.courseType + '</td><td>' + value.outline + '</td><td>' + value.objectives + '</td><td>' + value.teacher + '</td><td>' + value.onlineData + '</td><td>' + (value.online ? "是" : "否") + '</td><td><button class="btn btn-primary form-control comment"data-toggle="modal"data-target="#comment">评论</button></td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
                    $('#main-table').append(tr);
                });
            });
        }
    })

    /*cate based search end*/

    /*user mgr start*/
    $(document).on('click', '.user', function () {
        var examId = $(this).parents().siblings('td').eq(0).children().val();
        window.location.href = 'examUser.html?id=' + examId;
    });

    /*user mgr end*/

    //start exam type choose
    $(document).on('click', '.start-exam', function () {
        var examId = $(this).attr('data-id');
        $('#mock-exam-btn').attr('href', 'startExam.html?examId=' + examId + '&isMock=true');
        $('#formal-exam-btn').attr('href', 'startExam.html?examId=' + examId + '&isMock=false');
    });

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.course.id + '</td><td>' + value.course.courseName + '</td><td data-id="' + (value.courseCategory == null ? '' : value.courseCategory.id) + '">' + (value.courseCategory == null ? "未分类" : value.courseCategory.coursecatName) + '</td><td>' + value.startTime + '</td><td>' + value.endTime + '</td><td>' + value.passLine + '</td><td>' + value.questionNumber + '</td><td><button class="btn btn-primary form-control start-exam" data-id="' + value.id + '"data-toggle="modal"data-target="#start-exam">开始考试</button></td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td><td><button class="btn btn-primary form-control bind"data-toggle="modal"data-target="#bind">绑定考题</button></td><td><button class="btn btn-primary form-control user">参考人员</button></td></tr>';
            $('#main-table').append(tr);
        });
    }

});