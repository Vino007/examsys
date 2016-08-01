/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var allCates = [];
    //url arguments
    var courseFind = {'url': 'course/find', 'type': 'GET'},
        courseSearch = {'url': 'course/search', 'type': 'GET'},
        courseAdd = {'url': 'course/add', 'type': 'POST'},
        courseDelete = {'url': 'course/delete', 'type': 'POST'},
        courseUpdate = {'url': 'course/update', 'type': 'POST'},
        courseAddMsg = {'url': 'course/addMsg', 'type': 'POST'},
        courseShowMsg = {'url': 'course/showMsg', 'type': 'GET'},
        coursePreSetCate = {'url': 'course/preSetCate', 'type': 'GET'},
        courseSetCate = {'url': 'course/setCate', 'type': 'GET'},
        courseSetOffLine = {'url': 'course/setOffLine', 'type': 'POST'},
        courseSetOnLine = {'url': 'course/setOnLine', 'type': 'POST'};

    // courseSearch = {'url': '../mock/courseSearch.json', 'type': 'GET'};
    // coursePreSetCate = {'url': '../mock/coursePreSetCate.json', 'type': 'GET'};

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
        var rolesArray = [];
        $.each($('.add-form .form-inline input:checked'), function (key, value) {
            rolesArray.push($(value).val());
        });
        var rolesObj = {
            name: 'roleIds',
            value: rolesArray
        };
        submitData.push(rolesObj);
        opADU(courseAdd, submitData);
    });

    /*add end*/

    /*delete start*/
    $('#delete-single').click(function () {
        if (!confirm('确认删除?'))
            return false;
        var id = $('#edit-submit').attr('data-id');
        var deleteIds = [];
        deleteIds.push(id);
        var deleteObj = {
            name: "deleteIds",
            value: deleteIds
        };
        opADU(courseDelete, deleteObj);
    });

    $('#delete-multiple').click(function () {
        if (!confirm('确认删除?'))
            return false;
        var deleteIds = [];
        $.each($('[name=sub-checkbox]:checked'), function (key, value) {
            deleteIds.push($(value).val());
        });
        var deleteObj = {
            name: "deleteIds",
            value: deleteIds
        };
        opADU(courseDelete, deleteObj);
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

    function resetTable(data) {
        $('#user tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.courseName + '</td><td>' + (value.courseCategory == null ? "未分类" : value.courseCategory.description) + '</td><td>' + value.courseType + '</td><td>' + value.outline + '</td><td>' + value.objectives + '</td><td>' + value.teacher + '</td><td>' + value.onlineData + '</td><td>' + (value.online ? "是" : "否") + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#course').append(tr);
        });
    }

    function getAllCates() {
        $.ajax({
            url: coursePreSetCate.url,
            async: true,
            type: coursePreSetCate.type
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
                    $('.cates-checkbox').append(checkboxOption);
                });
            }
        });
    }
});