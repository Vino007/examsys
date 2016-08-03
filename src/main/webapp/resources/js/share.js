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
    courseSetCate = {'url': 'course/setCate', 'type': 'GET'},
    courseGetCourseQues = {'url': 'course/getCourseQues', 'type': 'GET'};

//role
var roleAll = {'url': 'role/all', 'type': 'GET'},
    roleFind = {'url': 'role/find', 'type': 'GET'},
    roleSearch = {'url': 'role/search', 'type': 'GET'},
    roleAdd = {'url': 'role/add', 'type': 'POST'},
    roleDelete = {'url': 'role/delete', 'type': 'POST'},
    roleUpdate = {'url': 'role/update', 'type': 'POST'},
    roleGetResourceTree = {'url': 'role/getResourceTree', 'type': 'GET'},
    roleBind = {'url': 'role/bind', 'type': 'POST'},
    roleGetAllCat = {'url': 'role/getAllCat', 'type': 'GET'},
    roleBindCats = {'url': 'role/bindCats', 'type': 'POST'};

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
    examBindMockQuestion = {'url': 'exam/bindMockQuestion', 'type': 'POST'},
    examFindUser = {'url': 'exam/findUser', 'type': 'GET'},
    examUpdateUserStatus = {'url': 'exam/updateUserStatus', 'type': 'POST'},
    examAddUser = {'url': 'exam/addUser', 'type': 'POST'},
    examRemoveUser = {'url': 'exam/removeUser', 'type': 'POST'},
    examFindQuestions = {'url': 'exam/findQuestions', 'type': 'GET'},
    examFindMockQuestions = {'url': 'exam/findMockQuestions', 'type': 'GET'},
    examSubmit = {'url': 'exam/submit', 'type': 'POST'},
    examPrepareBindQuestion = {'url': 'exam/prepareBindQuestion', 'type': 'GET'},
    examPrepareBindMockQuestion = {'url': 'exam/prepareBindMockQuestion', 'type': 'GET'},
    examAutoGenerate = {'url': 'exam/autoGenerate', 'type': 'POST'},
    examAutoGenerateMock = {'url': 'exam/autoGenerateMock', 'type': 'POST'};

//examresult
var
    examResultSearch = {'url': 'examresult/search', 'type': 'GET'},
    examResultAdd = {'url': 'examresult/add', 'type': 'POST'},
    examResultDelete = {'url': 'examresult/delete', 'type': 'POST'},
    examResultUpdate = {'url': 'examresult/update', 'type': 'POST'},
    examResultDownload = {'url': 'examresult/download', 'type': 'GET'},
    examResultDownloadSpecial = {'url': 'examresult/downloadspecial', 'type': 'GET'},
    examResultGetSituation = {'url': 'examresult/getsituation', 'type': 'GET'};

//logs
var
    logsAll = {'url': 'logs/all', 'type': 'GET'};
logsSearch = {'url': 'logs/search', 'type': 'GET'};

//resources
var resourcesTree = {'url': 'resource/resourceTree', 'type': 'GET'};


//url rewrite
/*
courseSearch = {'url': '../mock/courseSearch.json', 'type': 'GET'};
 courseGetAllCat = {'url': '../mock/coursePreSetCate.json', 'type': 'GET'};
 courseShowMsg = {'url': '../mock/courseShowMsg.json', 'type': 'GET'};
 courseAddMsg = {'url': '../mock/courseAddMsg.json', 'type': 'POST'};
 courseGetCourseQues = {'url': '../mock/courseGetCourseQues.json', 'type': 'GET'};
 coursecatShowCourses = {'url': '../mock/courseShowCourses.json', 'type': 'GET'};

 coursecatSearch = {'url': '../mock/coursecatSearch.json', 'type': 'GET'};
 courseGetAllCat = {'url': '../mock/coursePreSetCate.json', 'type': 'GET'};

 roleAll = {'url': '../mock/roleAll.json', 'type': 'GET'};
 roleSearch = {'url': '../mock/userSearch.json', 'type': 'GET'};
 roleUpdate = {'url': '../mock/userUpdate.json', 'type': 'POST'};
 roleAdd = {'url': '../mock/userAdd.json', 'type': 'POST'};
 roleDelete = {'url': '../mock/userDelete.json', 'type': 'POST'};
 roleBind = {'url': '../mock/roleBind.json', 'type': 'POST'};
 roleGetAllCat = {'url': '../mock/roleGetAllCat.json', 'type': 'GET'};
 roleBindCats = {'url': '../mock/roleBind.json', 'type': 'POST'};

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
 examFindUser = {'url': '../mock/examFindUser.json', 'type': 'GET'};
 examFindQuestions = {'url': '../mock/examFindQuestions.json', 'type': 'GET'};
 examFindMockQuestions = {'url': '../mock/examFindQuestions.json', 'type': 'GET'};
 examSubmit = {'url': '../mock/examSubmit.json', 'type': 'POST'};
 examBindQuestion = {'url': '../mock/examBindQuestion.json', 'type': 'POST'};
 examBindMockQuestion = {'url': '../mock/examBindQuestion.json', 'type': 'POST'};
 examPrepareBindQuestion = {'url': '../mock/examPrepareBindQuestion.json', 'type': 'GET'};
 examPrepareBindMockQuestion = {'url': '../mock/examPrepareBindQuestion.json', 'type': 'GET'};

 examResultSearch = {'url': '../mock/examResultSearch.json', 'type': 'GET'};
 examResultGetSituation = {'url': '../mock/examResultGetSituation.json', 'type': 'GET'};

 logsAll = {'url': '../mock/logsAll.json', 'type': 'GET'};

 roleGetResourceTree = {'url': '../mock/ztree.json', 'type': 'GET'};*/
/*url end*/

/*permission control start*/
function permissionControl() {
    var btnPermission = (JSON.parse(localStorage.getItem('data'))).data.buttonPermissions;
    var existPermission = [];
    $.each($('[data-permission]'), function (key, value) {
        existPermission.push($(value).attr('data-permission'));
    });
    $.each(btnPermission, function (key, value) {
        if (jQuery.inArray(value.permission, existPermission) != -1) {
            $('[data-permission="' + value.permission + '"]').removeAttr('data-permission');
        }
    });
    //if table
    $.each($('[data-permission]'), function (key, value) {
        if ($(value).get(0).tagName == 'TH') {
            var index = $(value).index();
            // $('#main-table tr').find(':eq(' + index + ')').remove();
            // $('#main-table tr th:nth-child(' + index + ')').remove();
            $('#main-table tr td:nth-child(' + (index + 1) + ')').remove();
            $('#main-table tr th:nth-child(' + (index + 1) + ')').remove();
        } else {
            $(value).remove();
        }
    });
}

/*permission control end*/

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
    // console.log(getFormJson(submitData));
    $.ajax({
        url: urlObj.url,
        async: true,
        type: urlObj.type,
        data: getFormJson(submitData)
        , dataType: 'json'
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
    var deleteIds = getCheckedIds();
    var deleteObj = {
        name: 'deleteIds',
        value: deleteIds
    };
    var deleteObjArr = [];
    deleteObjArr.push(deleteObj);
    opADU(deleteUrl, deleteObjArr);
}

function getCheckedIds() {
    var checkedIds = [];
    $.each($('[name=sub-checkbox]:checked'), function (key, value) {
        checkedIds.push($(value).val());
    });
    return checkedIds;
}

function init(initObj) {
    $.ajax({
        url: initObj.urlObj.url,
        async: true,
        type: initObj.urlObj.type,
        data: initObj.data,
        dataType: 'json'
    }).done(function (data) {
        if (data.success) {
            // initObj.resetTable(data);
            //page
            var totalPages = data.data.page.totalPages;
            var visiblePages = totalPages < 10 ? totalPages : 10;
            $('#pagination').remove();
            $('.container .row').append('<ul id="pagination" class="pagination"></ul>');
            $('#pagination').twbsPagination({
                totalPages: totalPages,
                visiblePages: visiblePages,
                onPageClick: function (event, page) {
                    initObj.data['pageNumber'] = page;
                    $.ajax({
                        url: initObj.urlObj.url,
                        async: true,
                        type: initObj.urlObj.type,
                        data: initObj.data,
                        dataType: 'json'
                    }).done(function (data) {
                        if (data.success) {
                            initObj.resetTable(data);
                            permissionControl();
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

function toDateStr(timestamp) {
    var time = new Date(timestamp);
    var y = time.getFullYear(),
        M = (time.getMonth() + 1) >= 10 ? (time.getMonth() + 1) : '0' + (time.getMonth() + 1),
        d = time.getDate() >= 10 ? time.getDate() : '0' + time.getDate(),
        h = time.getHours() >= 10 ? time.getHours() : '0' + time.getHours(),
        m = time.getMinutes() >= 10 ? time.getMinutes() : '0' + time.getMinutes(),
        s = time.getSeconds() >= 10 ? time.getSeconds() : '0' + time.getSeconds();
    return y + '-' + M + '-' + d + ' ' + h + ':' + m + ':' + s;
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
