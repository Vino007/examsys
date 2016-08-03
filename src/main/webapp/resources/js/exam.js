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
        opADU(examAdd, submitData);
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

    //submit edit
    $('#edit-submit').click(function () {
        var submitData = $('.edit-form').serializeArray();
        var id = $(this).attr('data-id');
        var idObj = {
            name: 'id',
            value: id
        };
        submitData.push(idObj);

        opADU(examUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search
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

    /*user mgr start*/
    $(document).on('click', '.user', function () {
        var examId = $(this).parents().siblings('td').eq(0).children().val();
        window.location.href = 'examUser.html?id=' + examId;
    });

    /*user mgr end*/

    /*bind question start*/
    //type choose
    $(document).on('click', '.bind', function () {
        var examId = $(this).parents().siblings('td').eq(0).children().val();
        var courseId = $(this).parents().siblings('td').eq(1).attr('data-id');
        $('#bind-mock-btn').attr('href', 'examBindQuestion.html?examId=' + examId + '&courseId=' + courseId + '&isMock=true');
        $('#bind-formal-btn').attr('href', 'examBindQuestion.html?examId=' + examId + '&courseId=' + courseId + '&isMock=false');
    });

    /*bind question end*/

    //start exam type choose
    $(document).on('click', '.start-exam', function () {
        var examId = $(this).attr('data-id');
        $('#mock-exam-btn').attr('href', 'startExam.html?examId=' + examId + '&isMock=true');
        $('#formal-exam-btn').attr('href', 'startExam.html?examId=' + examId + '&isMock=false');
    });

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        var startTime, endTime;
        $.each(data.data.page.content, function (key, value) {
            startTime = toDateStr(value.startTime);
            endTime = toDateStr(value.endTime);
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td data-id="' + (value.course.id != null ? value.course.id : value.exam.course.id) + '">' + (value.course.courseName != null ? value.course.courseName : value.exam.course.courseName) + '</td><td data-id="' + (value.courseCategory == null ? '' : value.courseCategory.id) + '">' + (value.courseCategory == null ? "未分类" : value.courseCategory.coursecatName) + '</td><td>' + startTime + '</td><td>' + endTime + '</td><td>' + value.passLine + '</td><td>' + value.questionNumber + '</td><td><button class="btn btn-primary form-control start-exam" data-id="' + value.id + '"data-toggle="modal"data-target="#start-exam">开始考试</button></td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td><td><button class="btn btn-primary form-control bind"data-toggle="modal"data-target="#bind-question">绑定考题</button></td><td><button class="btn btn-primary form-control user">参考人员</button></td></tr>';
            $('#main-table').append(tr);
        });
    }

});