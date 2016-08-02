/**
 * Created by chucc on 2016/7/27.
 */

/*url start*/
//course
var courseFind = {'url': 'course/find', 'type': 'GET'},
    courseSearch = {'url': 'course/search', 'type': 'GET'},
    courseAdd = {'url': 'course/add', 'type': 'POST'},
    courseDelete = {'url': 'course/delete', 'type': 'POST'},
    courseUpdate = {'url': 'course/update', 'type': 'POST'},
    courseAddMsg = {'url': 'course/addMsg', 'type': 'POST'},
    courseShowMsg = {'url': 'course/showMsg', 'type': 'GET'},
    courseGetAllCat = {'url': 'course/getAllCat', 'type': 'GET'},
    coursePreSetCate = {'url': 'course/preSetCate', 'type': 'GET'},
    courseSetCate = {'url': 'course/setCate', 'type': 'GET'},
    courseSetOffLine = {'url': 'course/setOffLine', 'type': 'POST'},
    courseSetOnLine = {'url': 'course/setOnLine', 'type': 'POST'},
    coursecatShowCourses = {'url': 'coursecat/showCourses', 'type': 'GET'};

//coursecat
var coursecatSearch = {'url': 'coursecat/search', 'type': 'GET'},
    coursecatAdd = {'url': 'coursecat/add', 'type': 'POST'},
    coursecatDelete = {'url': 'coursecat/delete', 'type': 'POST'},
    coursecatUpdate = {'url': 'coursecat/update', 'type': 'POST'},
    courseGetAllCat = {'url': 'course/getAllCat', 'type': 'GET'},
    coursePreSetCate = {'url': 'course/preSetCate', 'type': 'GET'},
    courseSetCate = {'url': 'course/setCate', 'type': 'GET'};

//role
var roleAll = {'url': 'role/all', 'type': 'GET'},
    roleFind = {'url': 'role/find', 'type': 'GET'},
    roleSearch = {'url': 'role/search', 'type': 'GET'},
    roleAdd = {'url': 'role/add', 'type': 'POST'},
    roleDelete = {'url': 'role/delete', 'type': 'POST'},
    roleUpdate = {'url': 'role/update', 'type': 'POST'},
    roleGetResourceTree = {'url': 'role/getResourceTree', 'type': 'GET'},
    roleBind = {'url': 'role/bind', 'type': 'POST'};

//user
var userAll = {'url': 'user/all', 'type': 'GET'},
    userFind = {'url': 'user/find', 'type': 'GET'},
    userSearch = {'url': 'user/search', 'type': 'GET'},
    userAdd = {'url': 'user/add', 'type': 'POST'},
    userDelete = {'url': 'user/delete', 'type': 'POST'},
    userUpdate = {'url': 'user/update', 'type': 'POST'},
    userGetAllRoles = {'url': 'user/getAllRoles', 'type': 'GET'},
    userGetAllDepts = {'url': 'user/getAllDepts', 'type': 'GET'},
    userGetRoles = {'url': 'user/getRoles', 'type': 'GET'},
    userBind = {'url': 'user/bind', 'type': 'POST'};

//question
var questionAll = {'url': 'question/all', 'type': 'GET'},
    questionFind = {'url': 'question/find', 'type': 'GET'},
    questionSearch = {'url': 'question/search', 'type': 'GET'},
    questionAdd = {'url': 'question/add', 'type': 'POST'},
    questionDelete = {'url': 'question/delete', 'type': 'POST'},
    questionUpdate = {'url': 'question/update', 'type': 'POST'},
    questionOffline = {'url': 'question/offline', 'type': 'POST'},
    questionOnline = {'url': 'question/online', 'type': 'POST'};

//department
var departmentSearch = {'url': 'department/search', 'type': 'GET'},
    departmentAdd = {'url': 'department/add', 'type': 'POST'},
    departmentDelete = {'url': 'department/delete', 'type': 'POST'},
    departmentUpdate = {'url': 'department/update', 'type': 'POST'},
    departmentShowUsers = {'url': 'department/showUsers', 'type': 'GET'};

//exam
var examAll = {'url': 'exam/all', 'type': 'GET'},
    examFind = {'url': 'exam/find', 'type': 'GET'},
    examSearch = {'url': 'exam/search', 'type': 'GET'},
    examAdd = {'url': 'exam/add', 'type': 'POST'},
    examDelete = {'url': 'exam/delete', 'type': 'POST'},
    examUpdate = {'url': 'exam/update', 'type': 'POST'},
    examBindQuestion = {'url': 'exam/bindQuestion', 'type': 'POST'},
    examFindUser = {'url': 'exam/findUser', 'type': 'POST'},
    examUpdateUserStatus = {'url': 'exam/updateUserStatus', 'type': 'POST'},
    examAddUser = {'url': 'exam/addUser', 'type': 'POST'},
    examRemoveUser = {'url': 'exam/RemoveUser', 'type': 'POST'},
    examFindQuestions = {'url': 'exam/findQuestions', 'type': 'GET'},
    examFindMockQuestions = {'url': 'exam/findMockQuestions', 'type': 'GET'};

//examresult
var
    examResultSearch = {'url': 'examresult/search', 'type': 'GET'},
    examResultAdd = {'url': 'examresult/add', 'type': 'POST'},
    examResultDelete = {'url': 'examresult/delete', 'type': 'POST'},
    examResultUpdate = {'url': 'examresult/update', 'type': 'POST'};

//logs
var
    logsAll = {'url': 'logs/all', 'type': 'GET'};


//url rewrite
courseSearch = {'url': '../mock/courseSearch.json', 'type': 'GET'};
courseGetAllCat = {'url': '../mock/coursePreSetCate.json', 'type': 'GET'};
courseShowMsg = {'url': '../mock/courseShowMsg.json', 'type': 'GET'};
courseAddMsg = {'url': '../mock/courseAddMsg.json', 'type': 'POST'};
coursecatShowCourses = {'url': '../mock/courseShowCourses.json', 'type': 'GET'};

coursecatSearch = {'url': '../mock/coursecatSearch.json', 'type': 'GET'};
courseGetAllCat = {'url': '../mock/coursePreSetCate.json', 'type': 'GET'};

roleAll = {'url': '../mock/roleAll.json', 'type': 'GET'};
roleSearch = {'url': '../mock/userSearch.json', 'type': 'GET'};
roleUpdate = {'url': '../mock/userUpdate.json', 'type': 'POST'};
roleAdd = {'url': '../mock/userAdd.json', 'type': 'POST'};
roleDelete = {'url': '../mock/userDelete.json', 'type': 'POST'};

userAll = {'url': '../mock/userpage.json', 'type': 'GET'};
userSearch = {'url': '../mock/userSearch.json', 'type': 'GET'};
userUpdate = {'url': '../mock/userUpdate.json', 'type': 'POST'};
userAdd = {'url': '../mock/userAdd.json', 'type': 'POST'};
userDelete = {'url': '../mock/userDelete.json', 'type': 'POST'};
userGetAllRoles = {'url': '../mock/userGetAllRoles.json', 'type': 'GET'};
userGetAllDepts = {'url': '../mock/userGetAllDepts.json', 'type': 'GET'};

questionAll = {'url': '../mock/questionAll.json', 'type': 'GET'};

departmentSearch = {'url': '../mock/departmentSearch.json', 'type': 'GET'};
departmentShowUsers = {'url': '../mock/departmentShowUsers.json', 'type': 'GET'};

examAll = {'url': '../mock/examAll.json', 'type': 'GET'};
examFindUser = {'url': '../mock/examFindUser.json', 'type': 'POST'};
examFindQuestions = {'url': '../mock/examFindQuestions.json', 'type': 'GET'};
examFindMockQuestions = {'url': '../mock/examFindQuestions.json', 'type': 'GET'};

examResultSearch = {'url': '../mock/examResultSearch.json', 'type': 'GET'};

logsAll = {'url': '../mock/logsAll.json', 'type': 'GET'};
/*url end*/

$(document).ready(function () {
    //checkbox select all
    $(document).on('change', '[name=check-all]', function () {
        if ($(this).prop('checked')) {
            $('[name=sub-checkbox]').prop('checked', true);
        } else {
            $('[name=sub-checkbox]').prop('checked', false);
        }
    });

    //sub checkbox
    $(document).on('change', '[name=sub-checkbox]', function () {
        var checkboxnum = $('[name=sub-checkbox]').length;
        var checked = $('[name=sub-checkbox]:checked').length;
        if (checkboxnum == checked) {
            $('[name=check-all]').prop('checked', true);
        } else {
            $('[name=check-all]').prop('checked', false);
        }
    });
});

function opADU(urlObj, submitData) {
    $.ajax({
        url: urlObj.url,
        async: true,
        type: urlObj.type,
        data: getFormJson(submitData)
    }).done(function (data) {
        alert(data.msg);
        if (data.success) {
            window.location.reload();
        }
    });
}

function deleteSingle(deleteUrl) {
    if (!confirm('确认删除?'))
        return false;
    var id = $('#edit-submit').attr('data-id');
    var deleteIds = [];
    deleteIds.push(id);
    var deleteObj = {
        name: 'deleteIds',
        value: deleteIds
    };
    var deleteObjArr = [];
    deleteObjArr.push(deleteObj);
    opADU(deleteUrl, deleteObjArr);
}

function deleteMultiple(deleteUrl) {
    if (!confirm('确认删除?'))
        return false;
    var deleteIds = [];
    $.each($('[name=sub-checkbox]:checked'), function (key, value) {
        deleteIds.push($(value).val());
    });
    var deleteObj = {
        name: 'deleteIds',
        value: deleteIds
    };
    var deleteObjArr = [];
    deleteObjArr.push(deleteObj);
    opADU(deleteUrl, deleteObjArr);
}

function init(initObj) {
    $.ajax({
        url: initObj.urlObj.url,
        async: true,
        type: initObj.urlObj.type,
        data: initObj.data
    }).done(function (data) {
        if (data.success) {
            // initObj.resetTable(data);
            //page
            var totalPages = data.data.page.totalPages;
            var visiblePages = totalPages < 10 ? totalPages : 10;
            $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: visiblePages,
                onPageClick: function (event, page) {
                    $.ajax({
                        url: initObj.urlObj.url,
                        async: true,
                        type: initObj.urlObj.type,
                        data: {"pageNumber": page}
                    }).done(function (data) {
                        if (data.success) {
                            initObj.resetTable(data);
                        }
                    });
                }
            });
        }
    });
}

function getFormJson(formArray) {
    var jsonobj = {};
    $.each(formArray, function (key, value) {
        jsonobj[this.name] = this.value;
    })
    return jsonobj;
}
