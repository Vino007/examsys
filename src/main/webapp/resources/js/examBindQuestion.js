/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    //get queryString
    var queryString = getQueryStringObject();
    var examId = queryString.examId;
    var courseId = queryString.courseId;
    var isMock = queryString.isMock == 'true' ? true : false;

    var initObj = {
        urlObj: courseGetCourseQues,
        data: {'id': courseId},
        resetTable: resetTable
    };

    $.ajax({
        url: initObj.urlObj.url,
        async: true,
        type: initObj.urlObj.type,
        data: initObj.data,
        dataType: 'json'
    }).done(function (data) {
        if (data.success) {
            resetTable(data);
            checkBind();
        }
    });

    function checkBind() {
        $.ajax({
            url: isMock ? examPrepareBindMockQuestion.url : examPrepareBindQuestion.url,
            type: isMock ? examPrepareBindMockQuestion.type : examPrepareBindQuestion.type,
            async: true,
            data: {'examId': examId},
            dataType: 'json'
        }).done(function (data) {
            if (data.success) {
                $.each($('[name=sub-checkbox]'), function (key, value) {
                    if (jQuery.inArray(parseInt($(value).val()), data.data.selectIds) != -1) {
                        $(value).prop('checked', true);
                    }
                });
            }
        });
    }

    //bind
    $('#bind-question').click(function () {
        var questionIds = getCheckedIds();
        var submitData = [
            {
                name: 'questionIds',
                value: questionIds
            },
            {
                name: 'examId',
                value: examId
            }
        ];
        opADU(isMock ? examBindMockQuestion : examBindQuestion, submitData);
    });

    //auto generate
    $('#auto-generate').click(function () {
        var submitData = [
            {
                name: 'examId',
                value: examId
            }
        ];
        opADU(isMock ? examAutoGenerateMock : examAutoGenerate, submitData);
    });

    //back to exam page
    $('#back-to-exam').click(function () {
        window.location.href = 'exam.html';
    });

    /*search start*/
    //search
    $('#search').click(function () {
        var content = $(this).siblings('input').val();
        var type = $(this).siblings('.type-based-search').val();
        var isOnline = $(this).siblings('.is-online').val();
        var searchArgs = {
            'search_content': content,
            'search_type': type,
            'search_isOnline': isOnline
        };
        var initObj = {
            urlObj: questionSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.questions, function (key, value) {
            var img;
            if (value.contentImageUrl == null) {
                img = '';
            } else {
                img = '<img src="' + value.contentImageUrl + '">';
            }
            var tr = '<tr><td><input type="checkbox"name="sub-checkbox"value="' + value.id + '"></td><td>' + value.content + '</td><td>' + img + '</td><td>' + value.choices + '</td><td>' + value.answer + '</td><td>' + (value.isOnline ? "是" : "否") + '</td><td>' + (value.courseId == null ? "" : value.courseId) + '</td></tr>';
            $('#main-table').append(tr);
        });
    }

    function getQueryStringObject() {
        var queryStringObj = {};
        var url = location.search;
        var str = url.substr(1);
        var strs = str.split('&');
        $.each(strs, function (key, value) {
            queryStringObj[this.split('=')[0]] = this.split('=')[1];
        });
        return queryStringObj;
    }

    // function getExamId() {
    //     var url = location.search;
    //     var str = url.substr(1);
    //     var strs = (str.split('&'))[0].split('=');
    //     return strs[1];
    // }
    //
    // function getCourseId() {
    //     var url = location.search;
    //     var str = url.substr(1);
    //     var strs = (str.split('&'))[1].split('=');
    //     return strs[1];
    // }

});