/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var allCates = [];

    var initObj = {
        urlObj: courseSearch,
        data: {'search_isOnline': true},
        resetTable: resetTable
    };
    init(initObj);
    getAllCates();


    /*add start*/
    $('.add-form').submit(function (e) {
        e.preventDefault();
        var submitData = $('.add-form').serializeArray();
        opADU(courseAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(courseDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(courseDelete);
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

        //cate fill
        var cateIndex = $('#main-table tr:first th:contains("课程类别")').index();
        $('.cates-options').val($(that).parents().siblings('td').eq(cateIndex).attr('data-id'));

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
            urlObj: courseSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    /*comment start*/
    $(document).on('click', '.comment', function () {
        var id = $(this).parents().siblings('td').eq(0).children().val();
        //store course id
        $('#comment-submit').attr('data-id', id);

        //get comment and show
        $.ajax({
            url: courseShowMsg.url,
            type: courseShowMsg.type,
            async: true,
            data: {'id': id}
        }).done(function (data) {
            $('.show-comment').html('');
            if (data.success) {
                $.each(data.data.messages, function (key, value) {
                    var li = '<li class="list-group-item"><span class="comment-name">' + value.creatorName + '</span><span class="pull-right comment-time">' + value.createTime + '</span><p>' + value.messContent + '</p></li>';
                    $('.show-comment').append(li);
                });
            } else {
                alert(data.msg);
            }
        });
    });

    $('.comment-form').submit(function (e) {
        e.preventDefault();
        var id = $('#comment-submit').attr('data-id');
        var msg = $('.comment-form textarea').val();
        var commentObj = {
            'courseId': id,
            'messContent': msg
        };
        //submit comment
        $.ajax({
            url: courseAddMsg.url,
            type: courseAddMsg.type,
            data: commentObj
        }).done(function (data) {
            if (data.success) {
                alert(data.msg);
                $('#comment').modal('toggle');
                $('.comment-form textarea').val('');
            } else {
                alert(data.msg);
            }
        });
    });

    /*comment end*/

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
                data: {'id': id}
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

    //cate-mgr
    $('#cate-mgr').click(function () {
        window.location.href = 'coursecat.html';
    });

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.id + '</td><td>' + value.courseName + '</td><td data-id="' + (value.courseCategory == null ? '' : value.courseCategory.id) + '">' + (value.courseCategory == null ? "未分类" : value.courseCategory.description) + '</td><td>' + value.courseType + '</td><td>' + value.outline + '</td><td>' + value.objectives + '</td><td>' + value.teacher + '</td><td>' + value.onlineData + '</td><td>' + (value.online ? "是" : "否") + '</td><td><button class="btn btn-primary form-control comment"data-toggle="modal"data-target="#comment">评论</button></td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#main-table').append(tr);
        });
    }

    function getAllCates() {
        $.ajax({
            url: courseGetAllCat.url,
            async: true,
            type: courseGetAllCat.type
        }).done(function (data) {
            if (data.success) {
                $.each(data.data.availableCategories, function (key, value) {
                    var roleObj = {
                        description: value.description,
                        id: value.id
                    };
                    allCates.push(roleObj);

                    //add roles to checkbox
                    var checkboxOption = '<option value="' + value.id + '">' + value.description + '</option>';
                    $('.cate-based-search').append(checkboxOption);
                    $('.cates-options').append(checkboxOption);
                });
            }
        });
    }
});