/**
 * Created by chucc on 2016/7/25.
 */
$(document).ready(function () {
    var allRoles = [];
    //url arguments
    var roleAll = {'url': 'role/all', 'type': 'GET'},
        roleFind = {'url': 'role/find', 'type': 'GET'},
        roleSearch = {'url': 'role/search', 'type': 'GET'},
        roleAdd = {'url': 'role/add', 'type': 'POST'},
        roleDelete = {'url': 'role/delete', 'type': 'POST'},
        roleUpdate = {'url': 'role/update', 'type': 'POST'},
        roleGetResourceTree = {'url': 'role/getResourceTree', 'type': 'GET'},
        roleBind = {'url': 'role/bind', 'type': 'POST'};

    // roleAll = {'url': '../mock/roleAll.json', 'type': 'GET'};
    // roleSearch = {'url': '../mock/userSearch.json', 'type': 'GET'};
    // roleUpdate = {'url': '../mock/userUpdate.json', 'type': 'POST'};
    // roleAdd = {'url': '../mock/userAdd.json', 'type': 'POST'};
    // roleDelete = {'url': '../mock/userDelete.json', 'type': 'POST'};

    var initObj = {
        urlObj: roleAll,
        data: {},
        resetTable: resetTable
    };
    init(initObj);


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
        opADU(roleAdd, submitData);
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
        opADU(roleDelete, deleteObj);
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
        opADU(roleDelete, deleteObj);
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

        opADU(roleUpdate, submitData);
    });

    /*edit end*/

    /*search start*/
    //search user
    $('#search').click(function () {
        var search = $(this).siblings('input').val();
        var searchArgs = {
            'search_name': search
        };
        var initObj = {
            urlObj: roleSearch,
            data: searchArgs,
            resetTable: resetTable
        };
        init(initObj);
    });

    /*search end*/

    function resetTable(data) {
        $('#user tr + tr').remove();
        $.each(data.data.page.content, function (key, value) {
            var tr = '<tr><td><input type="checkbox" name="sub-checkbox" value="' + value.id + '"></td><td>' + value.name + '</td><td>' + value.description + '</td><td>' + value.createTime + '</td><td>' + value.creatorName + '</td><td><button class="btn btn-primary form-control edit"data-toggle="modal"data-target="#edit">编辑</button></td></tr>';
            $('#role').append(tr);
        });
    }
});