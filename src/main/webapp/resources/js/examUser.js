/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    //get queryString, examId
    var examId = getExamId();

    var initObj = {
        urlObj: examFindUser,
        data: {'examId': examId},
        resetTable: resetTable
    };

    init(initObj);

    //find all
    $('#find-all').click(function () {
        init(initObj);
    });

    //back to exam page
    $('#back-to-exam').click(function () {
        window.location.href = 'exam.html';
    });

    //operations
    $('#add-exam-user').click(function () {
        operateExamUser(examAddUser);
    });

    $('#exam-user-leave').click(function () {
        operateExamUser(examUpdateUserStatus, 0);
    });

    $('#user-leave-cancel').click(function () {
        operateExamUser(examUpdateUserStatus, 1);
    });

    $('#remove-exam-user').click(function () {
        operateExamUser(examRemoveUser);
    });

    /*delete start*/
    $('#delete-single').click(function () {
        deleteSingle(userDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(userDelete);
    });

    /*delete end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var search = [];
        search = $(this).siblings('input').val().split(',');
        var searchArgs = {
            'search_username': search[0] == undefined ? '' : search[0],
            'search_user': search[1] == undefined ? '' : search[1]
        };
        var initObj = {
            urlObj: userSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    /*dept based search start*/
    $('.dept-based-search').change(function () {
        var deptName = $('.dept-based-search option:selected').text();
        var id = $(this).val();
        if (id == 'all') {
            window.location.reload();
        } else {
            $.ajax({
                url: departmentShowUsers.url,
                type: departmentShowUsers.type,
                async: true,
                data: {'id': id},
                dataType:'json'
            }).done(function (data) {
                $('#main-table tr + tr').remove();
                $.each(data.data.users, function (key, value) {
                    var roles = '';
                    if (value.roles.length == 1) {
                        roles += value.roles[0].description;
                    } else {
                        $.each(value.roles, function (k, v) {
                            roles += v.description + ',';
                        });
                    }
                    var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.username + '</td><td>' + value.userAlias + '</td><td>' + roles + '</td><td data-id="' + value.department.id + '">' + value.department.deptName + '</td><td>' + value.email + '</td><td>' + value.createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
                    $('#main-table').append(tr);
                });
            });
        }
    })

    /*dept based search end*/

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.username + '</td><td>' + value.userAlias + '</td><td data-id="' + value.department.id + '">' + value.department.deptName + '</td><td>' + value.email + '</td></tr>';
            $('#main-table').append(tr);
        });
    }

    function operateExamUser(opUrl, status) {
        var userIds = [];
        $.each($('[name=sub-checkbox]:checked'), function (key, value) {
            userIds.push($(value).val());
        });
        var objArr = [
            {
                name: 'userIds',
                value: userIds
            }, {
                name: 'examId',
                value: examId
            }
        ];
        if (status != undefined) {
            objArr.push({
                name: status,
                value: status
            });
        }
        opADU(opUrl, objArr);
    }

    function getExamId() {
        var url = location.search;
        var str = url.substr(1);
        var strs = str.split('=');
        return strs[1];
    }

});