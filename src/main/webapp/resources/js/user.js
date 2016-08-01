/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var allRoles = [];
    //url arguments
    var userAll = {'url': 'exam/user/all', 'type': 'GET'},
        userFind = {'url': 'user/find', 'type': 'GET'},
        userSearch = {'url': 'user/search', 'type': 'GET'},
        userAdd = {'url': 'user/add', 'type': 'POST'},
        userDelete = {'url': 'user/delete', 'type': 'POST'},
        userUpdate = {'url': 'user/update', 'type': 'POST'},
        userGetAllRoles = {'url': 'user/getAllRoles', 'type': 'GET'},
        userGetRoles = {'url': 'user/getRoles', 'type': 'GET'},
        userBind = {'url': 'user/bind', 'type': 'POST'};

    // userAll = {'url': '../mock/userpage.json', 'type': 'GET'};
    // userSearch = {'url': '../mock/userSearch.json', 'type': 'GET'};
    // userUpdate = {'url': '../mock/userUpdate.json', 'type': 'POST'};
    // userAdd = {'url': '../mock/userAdd.json', 'type': 'POST'};
    // userDelete = {'url': '../mock/userDelete.json', 'type': 'POST'};
    // userGetAllRoles = {'url': '../mock/userGetAllRoles.json', 'type': 'GET'};

    var initObj = {
        urlObj: userAll,
        data: {},
        resetTable: resetTable
    };

    init(initObj);
    getAllRoles();


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
        opADU(userAdd, submitData);
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
        opADU(userDelete, deleteObj);
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
        opADU(userDelete, deleteObj);
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
        var roleIndex = $('#user tr:first th:contains("角色")').index();
        var roles = [];
        roles = $(that).parents().siblings('td').eq(roleIndex).text().split(',');
        $('.edit-form .form-inline input').prop('checked', false);
        $.each(roles, function (key, value) {
            $('.edit-form .form-inline label[data-value=' + value + '] input').prop('checked', true);
        });
    });

    //submit edit user
    $('#edit-submit').click(function () {
        var submitData = $('.edit-form').serializeArray();
        var rolesArray = [];
        var id = $(this).attr('data-id');
        $.each($('.edit-form .form-inline input:checked'), function (key, value) {
            rolesArray.push($(value).val());
        });
        var idObj = {
            name: 'id',
            value: id
        };
        var rolesObj = {
            name: 'roleIds',
            value: rolesArray
        };
        submitData.push(idObj);
        submitData.push(rolesObj);

        opADU(userUpdate, submitData);
    });

    /*edit end*/

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

    // function opADU(urlObj, submitData) {
    //     $.ajax({
    //         url: urlObj.url,
    //         async: true,
    //         type: urlObj.type,
    //         data: getFormJson(submitData)
    //     }).done(function (data) {
    //         alert(data.msg);
    //         if (data.success) {
    //             window.location.reload();
    //         }
    //     });
    // }

    // function init(urlObj, data) {
    //     $.ajax({
    //         url: urlObj.url,
    //         async: true,
    //         type: urlObj.type,
    //         data: data
    //     }).done(function (data) {
    //         if (data.success) {
    //             resetTable(data);
    //             //page
    //             var totalPages = data.data.page.totalPages;
    //             var visiblePages = totalPages < 10 ? totalPages : 10;
    //             $('#pagination').twbsPagination({
    //                 totalPages: totalPages,
    //                 visiblePages: visiblePages,
    //                 onPageClick: function (event, page) {
    //                     $.ajax({
    //                         url: userAll.url,
    //                         async: true,
    //                         type: userAll.type,
    //                         data: {"pageNumber": page}
    //                     }).done(function (data) {
    //                         if (data.success) {
    //                             resetTable(data);
    //                         }
    //                     });
    //                 }
    //             });
    //         }
    //     });
    // }

    function getAllRoles() {
        $.ajax({
            url: userGetAllRoles.url,
            async: true,
            type: userGetAllRoles.type
        }).done(function (data) {
            if (data.success) {
                $.each(data.data.roles, function (key, value) {
                    var roleObj = {
                        description: value.description,
                        id: value.id
                    };
                    allRoles.push(roleObj);

                    //add roles to checkbox
                    var checkboxOption = '<label class="control-label" data-value="' + value.description + '"><input type="checkbox" class="form-control" value="' + value.id + '">' + value.description + '</label>';
                    $('.roles-checkbox').append(checkboxOption);
                });
            }
        });
    }

    function resetTable(data) {
        $('#user tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var roles = '';
            if (value.roles.length == 1) {
                roles += value.roles[0].description;
            } else {
                $.each(value.roles, function (k, v) {
                    roles += v.description + ',';
                });
            }
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.username + '</td><td>' + value.userAlias + '</td><td>' + roles + '</td><td>' + value.email + '</td><td>' + value.createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#user').append(tr);
        });
    }

    // function getFormJson(formArray) {
    //     var jsonobj = {};
    //     $.each(formArray, function (key, value) {
    //         jsonobj[this.name] = this.value;
    //     })
    //     return jsonobj;
    // }
});