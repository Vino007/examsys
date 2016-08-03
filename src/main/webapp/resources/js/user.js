/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var allRoles = [];
    var allDepts = [];

    var initObj = {
        urlObj: userAll,
        data: {},
        resetTable: resetTable
    };

    init(initObj);
    getAllRoles();
    getAllDepts();


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
        deleteSingle(userDelete);
    });

    $('#delete-multiple').click(function () {
        deleteMultiple(userDelete);
    });

    /*delete end*/


    /*edit start*/
    //edit modal
    $(document).on('click', '.edit', function () {
        var that = this;

        $.each($('.edit-form .form-group'), function (key, value) {
            $(value).children().eq(1).val($(that).parents().siblings('td').eq(key + 1).text());
        });


        //store id
        $('#edit-submit').attr('data-id', $(that).parents().siblings('td').eq(0).children().val());

        //role fill
        var roleIndex = $('#main-table tr:first th:contains("角色")').index();
        var roles = [];
        roles = $(that).parents().siblings('td').eq(roleIndex).text().split(',');
        $('.edit-form .form-inline input').prop('checked', false);
        $.each(roles, function (key, value) {
            $('.edit-form .form-inline label[data-value=' + value + '] input').prop('checked', true);
        });

        //dept fill
        var deptIndex = $('#main-table tr:first th:contains("部门")').index();
        $('.depts-options').val($(that).parents().siblings('td').eq(deptIndex).attr('data-id'));
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
        var search = $(this).siblings('input').val().split(',');
        var searchArgs = {
            'search_username': search[0] == undefined ? '' : search[0],
            'search_name': search[1] == undefined ? '' : search[1]
        };
        console.log(searchArgs);
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
                dataType: 'json'
            }).done(function (data) {
                $('#main-table tr + tr').remove();
                $.each(data.data.users, function (key, value) {
                    var roles = '';
                    if (value.roles != null) {
                        if (value.roles.length == 1) {
                            roles += value.roles[0].description;
                        } else {
                            $.each(value.roles, function (k, v) {
                                roles += v.description + ',';
                            });
                        }
                    }
                    var createTime = toDateStr(value.createTime);
                    var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.username + '</td><td>' + value.userAlias + '</td><td>' + roles + '</td><td data-id="' + value.department.id + '">' + value.department.deptName + '</td><td>' + value.email + '</td><td>' + createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
                    $('#main-table').append(tr);
                });
            });
        }
    })

    /*dept based search end*/

    // function opADU(urlObj, submitData) {
    //     $.ajax({
    //         url: urlObj.url,
    //         async: true,
    //         type: urlObj.type,
    //         data: submitData
    //     ,             dataType: 'json' }).done(function (data) {
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
    //     ,             dataType: 'json' }).done(function (data) {
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
    //                     ,             dataType: 'json' }).done(function (data) {
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
            type: userGetAllRoles.type,
            dataType: 'json'
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

    function getAllDepts() {
        $.ajax({
            url: userGetAllDepts.url,
            async: true,
            type: userGetAllDepts.type,
            dataType: 'json'
        }).done(function (data) {
            if (data.success) {
                $.each(data.data.availableDepts, function (key, value) {
                    var deptObj = {
                        deptName: value.deptName,
                        id: value.id
                    };
                    allDepts.push(deptObj);

                    //add roles to checkbox
                    var selectOption = '<option value="' + value.id + '">' + value.deptName + '</option>';
                    $('.dept-based-search').append(selectOption);
                    $('.depts-options').append(selectOption);
                });
            }
        });
    }

    function resetTable(data) {
        $('#main-table tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var roles = '';
            if (value.roles.length == 1) {
                roles += value.roles[0].description;
            } else {
                $.each(value.roles, function (k, v) {
                    roles += v.description + ',';
                });
            }
            var createTime = toDateStr(value.createTime);
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.username + '</td><td>' + value.userAlias + '</td><td>' + roles + '</td><td data-id="' + (value.department == null ? "" : value.department.id) + '">' + (value.department == null ? "" : value.department.deptName) + '</td><td>' + value.email + '</td><td>' + createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#main-table').append(tr);
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