/* global toastr, bootbox, CKEDITOR */

var modalBusy = new function() {
    
    this.open = function(){
        
        $('#modal-busy').modal({
        
            backdrop: 'static',
            keyboard: false
        });
    };
    
    this.close = function(){
        
        $('#modal-busy').modal('toggle');
    };
};

/* Show busy modal for all ajax requests */
$( document ).ajaxStart(function() {
    
    modalBusy.open();
  
}).ajaxStop(function() {
    
    modalBusy.close();
});

/* Refresh UI with notifications and messages even without user activity */
$(document).ready(function () {
 
    /* On page load */
    RefreshUI();
    
    /* Repeat every minute */
    var uiRefresh = window.setInterval(RefreshUI, 2*60*1000);
    
});


/* Search results highlighting */
$(document).ready(function () {
    
   if ($('#isSearchPage').length > 0) {
       
        var str = $('#searchQueryString').html().replace("+", " ");

        var words = str.split(" ");

        for (var i = 0; i < words.length; i++) {
            
            var regexUpper = new RegExp(words[i].charAt(0).toUpperCase() + words[i].slice(1), "g");
            var regexLower = new RegExp(words[i].toLowerCase(), "g");
            var highlightUpper = '<span style="background-color: yellow;">' + words[i].charAt(0).toUpperCase() + words[i].slice(1) + '</span>';
            var highlightLower = '<span style="background-color: yellow;">' + words[i].toLowerCase() + '</span>';
            
            $('.panel-body').children().each(function () {
                
                $(this).html( $(this).html().replace(regexUpper, highlightUpper) );
                $(this).html( $(this).html().replace(regexLower, highlightLower) );
            });
        }
   }
});


/* Update User Interface (UI) notifications */
function RefreshUI() {
       
//    console.log(new Date().toLocaleTimeString() + ': UI Updated!');
    
    /* Update User toolbar with messages */
    UpdateUserMessageAlerts();
    
    /* Update User toolbar with notifications */
    UpdateUserNotificationAlerts();
    
    /* Update User toolbar with tasks list */
    UpdateUserTasksAlerts();
    
}

/* Update User toolbar with messages */
function UpdateUserMessageAlerts() {
    
    $.ajax({
        
        global: false,
        url: '/api/messages/unread',
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            if (data.length > 0) {
                
                var msgCount = (data.length > 10) ? '10+' : data.length;

                $('#userMessageCount').html(msgCount);
                $('#userMessageSummaries').empty();
                
                $.each(data, function (key, index) {

                    var subject = index.subject;

                    var subjectSummary = (subject.length > 48) ? subject.substring(0, 45) + '...' : subject;
                    var date = new Date(index.sentDate);
                    var whenSent = timeSince(index.sentDate) + '<br/>'+ date.toISOString().substring(0, 19).replace("T", " ");

                    var message  =  '<li class="message-preview">' +
                                    '<a href="/messages/' + index.uuId + '">' +
                                    '<div class="media">' +
                                    '<span class="pull-left"><img class="media-object" src="/images/admin.png" alt="" /></span>' +
                                    '<div class="media-body">' +
                                    '<h5 class="media-heading">From:<strong> ' + index.sender.firstName + '</strong></h5>' +
                                    '<p class="small text-muted"><i class="fa fa-clock-o"></i> ' + whenSent + '</p>' +
                                    '<p>' + subjectSummary + '...</p>' +
                                    '</div>' +
                                    '</div>' +
                                    '</a>' +
                                    '</li>';

                    $('#userMessageSummaries').append(message);

                });

                $('#userMessageSummaries').append('<li class="message-footer"><a href="/messages">Read All New Messages</a></li>');
                $('#userMessages').show();
                 
            } else {
                
                $('#userMessages').hide();
            }
        },
        error: function() {
            
            return;
        }
    });
}

/* Update User toolbar with notifications */
function UpdateUserNotificationAlerts() {
    
    $.ajax({
        
        global: false,
        url: '/api/notifications/unread',
        type: "GET",
        dataType: "json",
        success: function (data) {
             
            if (data.length > 0) {
                
                var msgCount = (data.length > 10) ? '10+' : data.length;

                $('#userNotificationsCount').html(msgCount);
                $('#userNotificationDetails').empty();
                
                $.each(data, function (key, index) {

                    var name = index.name;
                    var alertClass = '';

                    if (index.notificationPriority === "HIGH") { alertClass = 'danger'; }
                    if (index.notificationPriority === "NORMAL") { alertClass = 'success'; }
                    if (index.notificationPriority === "MEDIUM") { alertClass = 'warning'; }

                    var simpleAlert  =  '<div class="notice notice-' + alertClass + '" >' +
                                        '<strong>' + name + '</strong> ' + index.notification + 
                                        '</div>';

                    $('#userNotificationDetails').append(simpleAlert);

                });

                $('#userNotificationDetails').append('<li class="divider"></li>');
                $('#userNotificationDetails').append('<li class="message-footer"><a href="/notifications">View all</a></li>');
                $('#userNotifications').show();
            
            } else {
                
                $('#userNotifications').hide();
            }
        },
        error: function() {
            
            return;
        }
    });
}


/* Update User toolbar with tasks */
function UpdateUserTasksAlerts() {
    
    $.ajax({
        
        global: false,
        url: '/api/tasks/all',
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            if (data.length > 0) {
                
                var taskCount = (data.length > 10) ? '10+' : data.length;
                
                $('#userTasksCount').html(taskCount);
                $('#userTasksDetails').empty();
                
                var age = timeSince(data[0].creationDate);
                var total = data.length;
                var complete = 0;
                var outstanding = 0;
                var taskDetails = '';
                 
                $.each(data, function (key, index) {
                    
                    if (index.completed === true) {
                        
                        complete++;
                    }
                    
                    if (index.completed === false) {
                        
                        outstanding++;
                        
                        var action = '';
                        if (index.taskAction === "CONFIRM_REQ") { action = 'Confirmation'; }
                        if (index.taskAction === "ACKNOWLEDGE_REQ") { action = 'Acknowlegement'; }
                            
                        taskDetails  =  taskDetails +
                                        '<li>' +
                                        '<a href="javascript:void(0);" onclick="javascript:BootboxPerformTask(\'' + index.id + '\');">' +
                                        '<div style="padding: 10px; border-top: 1px solid #c0c0c0;">' +
                                        '<span><strong>' + action +' Request</strong></span>' +
                                        '<span class="pull-right" style="font-size: 75%">' + index.taskItem.sysId + '</span>' +
                                        '<div style="margin-top: 5px;">Confirm requirement requirement with identifier: ' + index.taskItem.identifier + '</div>' +
                                        '</div>' +
                                        '</a>';
                                        '</li>';
                    }
                });
                
                var pecentageComplete = Math.round(complete / total * 100);
                    
                var task =  '<li>' +
                            '<a href="/tasks">' +
                            '<div style="padding: 10px; border-top: 6px solid #369;">' +
                            '<span><strong>Requirements Requests</strong></span>' +
                            '<span class="pull-right" style="font-size: 75%">' + age + '</span>' +
                            '<div style="margin-top: 5px;">You have ' + outstanding + ' confirmation requests.</div>' +
                            '<div style="font-size: 85%; margin-top: 10px;">Completion with: ' + pecentageComplete + '%</div>' +
                            '<div class="progress" style="margin-top: 4px;">' +
                            '<div class="progress-bar progress-bar-success" role="progressbar" style="width: ' + pecentageComplete + '%" arial-valuenow="' + pecentageComplete + '" arial-valuemin="0" arial-valuemax="100"></div>' +
                            '</div>' +
                            '</div>' +
                            '</a>';
                            '</li>';
                            
                task = task + taskDetails;

                $('#userTasksDetails').append(task);

                $('#userTasks').show();
                
            } else {
                
                $('#userTasks').hide();
            }
        },
        error: function() {
            
            return;
        }
    });
}


/* Update User toolbar with tasks */
function UpdateUserTasksAlertsOld() {
    
    $.ajax({
        
        global: false,
        url: '/api/tasks/active',
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            console.log(data);
            
            if (data.length > 0) {
                
                var taskCount = (data.length > 10) ? '10+' : data.length;
                
                $('#userTasksCount').html(taskCount);
                $('#userTasksDetails').empty();
                
                $.each(data, function (key, index) {
                    
                    var taskClass = 'progress-bar-primary';
                    var percentage = Math.round(index.taskProgress / index.initialSize * 100);
                    
                    if (percentage >= 75) {
                        taskClass = 'progress-bar-success';
                    } else if (percentage >= 50) {
                        taskClass = 'progress-bar-warning';
                    } else if (percentage >= 25) {
                        taskClass = 'progress-bar-info';
                    } else if (percentage < 25) {
                        taskClass = 'progress-bar-danger';
                    }
                            
                    var task =  '<li>' +
                                '<div style="padding: 10px; border-top: 1px solid #c0c0c0;">' +
                                '<span style="color: #369;"><strong>' + index.taskItem.identifier + '</strong></span>' +
                                '<span class="pull-right">' + percentage + '%</span>' +
                                '<div class="progress">' +
                                '<div class="progress-bar ' + taskClass + '" role="progressbar" style="width: ' + percentage + '%" arial-valuenow="' + percentage + '" arial-valuemin="0" arial-valuemax="100"></div>' +
                                '</div>' +
                                '</div>' +
                                '</li>';
                        
                    $('#userTasksDetails').append(task);
                     
                });
                
                $('#userTasks').show();
                
            } else {
                
                $('#userTasks').hide();
            }
        },
        error: function() {
            
            return;
        }
    });
}


// Retrieve projects list
$(document).ready(function () {

    var list = '';

    $.ajax({
        
        global: false,
        url: '/api/projects',
        type: "GET",
        dataType: "json",
        success: function (data) {

            $.each(data, function (key, index) {

                list = list
                        + '<li style="border-bottom: 1px solid #c0c0c0;">'
                        + '<a href="javascript:void(0);" onclick="return loadProjectTree(' + index.id + ');">'
                        + index.projectShortName
                        + '<i class="fa fa-fw fa-bar-chart pull-right" style="margin: 4px 0;"></i>'
                        + '</a>'
                        + '</li>';
            });

            $("#projectsList").append(list);

            $.ajax({

                global: false,
                url:'/api/projects/selected',
                type: "GET",
                dataType: "json",
                success: function (data) {

                    var project = data;
                    loadProjectTree(project.id);

                },
                error: function () {

                    // Do nothing
                }
            });
        },
        error: function () {
            
            checkLogin();
            $("#projectsList").append('<br/>No projects to list.');
        }
    });
});


//Retrieve project specification tree
function loadProjectTree(id) {
 
    var zTreeObj;
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: 0
            }
        },
        view: {
            dblClickExpand: true
        },
        callback: {

            onClick: zTreeOnClick
        }
    };

    $.ajax({
        
        global: false,
        url: '/api/projects/select/' + id,
        type: "GET",
        dataType: "json",
        success: function (data) {
          
            $('#projectModulesTitle').html(data.projectShortName);
            
            if (data) {
                
                var project = data;
                
                $.ajax({

                    global: false,
                    url: '/api/project/folders/' + id,
                    type: "GET",
                    dataType: "json",
                    success: function (data) {
                        
                        if (!$.trim(data)) {

                            $.fn.zTree.destroy("projectTree");
                            $('#projectTree').empty();
                            $("#projectTree").append('<li style="width: 200px; margin: 10px; color:red;">No documents or folders found</li>');

                        } else {

                            $("#projectsList").collapse('hide');
                            $("#more-less").removeClass('fa-minus-square').addClass('fa-plus-square');

                            $('#projectTree').hide();
                            $('#more-less-modules').addClass('fa-plus-square').removeClass('fa-minus-square');

                            $('#applicationMenu').hide();
                            $('#more-less-appmenu').addClass('fa-plus-square').removeClass('fa-minus-square');

                            zTreeObj = $.fn.zTree.init($("#projectTree"), setting, data);

                            if (project.id) {
                                
                                $.ajax({

                                    global: false,
                                    url: '/api/folders/selected',
                                    type: "GET",
                                    dataType: "json",
                                    success: function (data) {
                                        
                                        var folder = data;
                                        var currFolderId = folder.id;
                                        var treeObj = $.fn.zTree.getZTreeObj("projectTree");

                                        var currNode = treeObj.getNodeByParam("id", currFolderId, null);
                                        treeObj.expandNode(currNode, true, false, true);
                                    }
                                });
                            }
                        }
                    },
                    error: function () {

                        checkLogin();

                        $('#projectTree').empty();
                        $("#projectTree").append('<br/>Error!');
                        $("#projectTree").hide();
                        $('#more-less-modules').addClass('fa-plus-square').removeClass('fa-minus-square');
                    }
                });
            }
        }
    });
}


function checkLogin() {
    
    $.ajax({
        
        global: false,
        url: '/api/login/check',
        type: "GET",
        dataType: "json",
        success: function() {
            
            window.location.replace('/login');
        }
    });
}


function zTreeOnClick(event, treeId, treeNode, clickFlag) {

    var treeObj = $.fn.zTree.getZTreeObj("projectTree");

    $.ajax({

        global: false,
        url:'/api/projects/selected',
        type: "GET",
        dataType: "json",
        success: function (data) {

            var project = data;
            
            if (treeNode.isParent) {

                if (treeNode.pId === 0) {

                    window.location.replace('/projects/' + project.uuId);

                } else {

                    window.location.replace('/projects/' + project.uuId + '/' + treeNode.id);
                }

                treeObj.expandNode(treeNode, true, false, true);

            } else {

                if (treeNode.linkId !== null) {
                    
                    window.location.replace('/artifacts/' + treeNode.linkId);
                }
            }
        },
        error: function () {
            
            // Do nothing
        }
    });
}


$(document).ready(function(){
   
    $('button').tooltip();
    
    $(".top-tip").tooltip({
        placement: 'top',
        html: true
    });
    
    $(".top-bottom").tooltip({
        placement: 'bottom',
        html: true
    });
    
    $(".top-left").tooltip({
        placement: 'left',
        html: true
    });
    
    $(".top-right").tooltip({
        placement: 'right',
        html: true
    });
});


function ToggleProjectModulesTree() {
    
    var query = $('#projectTree');
    var isVisible = query.is(':visible');
    
    if (isVisible === true) {

        query.hide();
        $('#more-less-modules').addClass('fa-plus-square').removeClass('fa-minus-square');
    
    } else {

        query.show();
        $('#more-less-modules').addClass('fa-minus-square').removeClass('fa-plus-square');
    }
}


function ToggleApplicationMenu() {
    
    var query = $('#applicationMenu');
    var isVisible = query.is(':visible');
    
    if (isVisible === true) {

        query.hide();
        $('#more-less-appmenu').addClass('fa-plus-square').removeClass('fa-minus-square');
        
    } else {

        query.show();
        $('#more-less-appmenu').addClass('fa-minus-square').removeClass('fa-plus-square');
    }
}


function BootboxAlertTitle(title, message) {
    
    bootbox.alert({
        
        title: title,
        message: message,
        buttons: {
            ok: {
                label: "Close",
                className: "btn-success btn-fixed-width-100"
            }}
    });
}


function BootboxCreateFolder(projectId) {
    
    BootboxCreateSubfolder(0, projectId);
}

function BootboxCreateSubfolder(parentId, projectId) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/project/project-subfolder-create-form.html',
        success: function (data) {

            bootbox.confirm({

                message: data,
                title: "Create folder/subfolder",
                size: 'small',
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Create",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    var folderName = '';
                    
                    if (result) {

                        folderName = document.getElementById('subFolderName').value;
                        
                        var data = {};

                        data['parentId'] = parentId;
                        data['projectId'] =  projectId;
                        data['folderName'] =  folderName;
                        data['folderType'] =  'DOCUMENT';
                        
                        console.log(data);
                    
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/project/folder/create",
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function () {
                                
                                loadProjectTree(projectId);
                                showToastr('success', 'Folder ' + folderName + ' created!');
                            }
                        });
                    }
                }
            });
        }
    });
}


function BootboxAdminUserPwdReset(uuId, user) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/user/user-admin-password-reset.html',
        success: function (data) {

            bootbox.confirm({

                message: data,
                title: "Reset password",
                size: 'small',
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Change",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {

                    if (result) {

                        var data = {};

                        data['uuId'] = user;
                        data['password'] = document.getElementById('newPassword').value;
                        data['confirmPassword'] = document.getElementById('confirmNewPassword').value;
                        
                        $.ajax({
                            
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/admin/password/reset",
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function (data) {

                                showToastr('success', 'Password for ' + user +' changed!');
                            }
                        });
                    }
                }
            }); 
        }
    });
}


function BootboxAdminUserDetailsUpdate(uuId, user) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/user/user-admin-details-update.html',
        success: function (data) {
            
            var box = bootbox.confirm({

                message: data,
                title: "Update user details for " + user,
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Update",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    if (result) {
                        
                        var data = {};
                        
                        data['uuid'] = uuId;
                        data['firstName'] = document.getElementById('firstName').value;
                        data['lastName'] = document.getElementById('lastName').value;
                        data['mainRole'] = document.getElementById('userJobTitle').value;
                        data['phoneNumber'] = document.getElementById('userPhoneNumber').value;
                        data['mobileNumber'] = document.getElementById('userMobileNumber').value;
                        data['address'] = document.getElementById('userAddres').value;
                        data['country'] = $('#userCountry option:selected').html();
                        data['state'] = $('#userState option:selected').html();
                        data['city'] = $('#userCity option:selected').html();
                        data['postCode'] = '';
                        
                        $.ajax({
                            
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/user/update/" + uuId,
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function () {

                                showToastr('success', 'Profile for ' + user +' updated!');
                            }
                        });
                    }
                }
            });
            
            box.on("shown.bs.modal", function(e) {
                
                $.ajax({

                    type: "GET",
                    contentType: "application/json",
                    url: "/api/countries/list",
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {

                    var selectCountryOptions = '';
                    
                    BootboxAdminGetUserInfo(uuId);
                    
                    $.each(data, function (key, index) {

                        selectCountryOptions = selectCountryOptions + '<option value="' + index.id + '">' + index.countryName+ '</option>';

                    });
                    
                    $('#userCountry').empty();
                    $('#userCountry').append(selectCountryOptions);
                });
            });
        }
    });
    
}


function BootboxAdminGetUserInfo(uuId) {
    
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "/api/user/byuuid/" + uuId,
        dataType: "json",
        cache: false
    })
    .done(function (data) {

        if (data.firstName !== null) {
            
            $('#firstName').prop('value', data.firstName);
        }

        if (data.lastName !== null) {
            
            $('#lastName').prop('value', data.lastName);
        }

        if (data.mainRole !== null) {
            
            $('#userJobTitle').prop('value', data.mainRole);
        }
        
        if (data.phoneNumber !== null) {
            
            $('#userPhoneNumber').prop('value', data.phoneNumber);
        }

        if (data.mobileNumber !== null) {
            
            $('#userMobileNumber').prop('value', data.mobileNumber);
        }

        if (data.address !== null) {
            
            $('#userAddres').prop('value', data.address);
        }

        if (data.country !== null) {
            
            var selectedCountry = '';
            
            $("#userCountry > option").each(function() {
                
                if (this.text === data.country) {
                    
                    selectedCountry = this.value;
                }
            });
                                
            $('#userCountry').prop('value', selectedCountry);
        }
        
        if (data.state !== null) {
            
            OnUserUpdateCountryChange();
            
            var selectedState = '';
            
            $("#userState > option").each(function() {
                
                if (this.text === data.state) {
                    
                    selectedState = this.value;
                }
            });
                                
            $('#userState').prop('value', selectedState);
        }
        
        if (data.city !== null) {
            
            OnUserUpdateStateChange();
            
            var selectedCity = '';
            
            $("#userCity > option").each(function() {
                
                if (this.text === data.city) {
                    
                    selectedCity = this.value;
                }
            });
                                
            $('#userCity').prop('value', selectedCity);
        }
    });
}


function OnUserUpdateCountryChange(){
    
    var countryId = document.getElementById('userCountry').value;
    
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "/api/states/list/" + countryId,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var selectStateOptions = '';

        $.each(data, function (key, index) {

            selectStateOptions = selectStateOptions + '<option value="' + index.id + '">' + index.stateName+ '</option>';

        });

        $('#userState').empty();
        $('#userState').append(selectStateOptions);

        OnUserUpdateStateChange();
    });
}


function OnUserUpdateStateChange() {
    
    var stateId = document.getElementById('userState').value;
    
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "/api/cities/list/" + stateId,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
       
        var selectCityOptions = '';

        $.each(data, function (key, index) {

            selectCityOptions = selectCityOptions + '<option value="' + index.id + '">' + index.cityName+ '</option>';

        });

        $('#userCity').empty();
        $('#userCity').append(selectCityOptions);
        
    });
}


function BootboxAdminUserRolesManage(uuId, user) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/user/user-admin-roles-manage.html',
        success: function (data) {
            
            var box = bootbox.confirm({

                message: data,
                title: "Roles for " + user,
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Change",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    if (result) {
                        
                        var rolesListPara = '';
                        var rolesCount = $('#userRolesList option').length;
                        
                        var roles = [];
                        
                        for (i = 0; i < rolesCount; i++) {
                            
                            var role = {};
                            role['roleName'] = $('#userRolesList option').eq(i).val();
                            role['roleDisplayName'] = $('#userRolesList option').eq(i).html();
                            roles.push(role);
                            
                            rolesListPara = rolesListPara + '<p style="line-height: 90%">' + $('#userRolesList option').eq(i).html() + '</p>';
                        }
                        
                        $.ajax({

                            type: "POST",
                            contentType: "application/json",
                            url: "/api/roles/add/" + uuId,
                            data: JSON.stringify(roles),
                            dataType: "json",
                            timeout: 60000,
                            success: function (data) {
                              
                                var roleListElement  =  '<div id="userRolesTableCell" style="padding-top: 5px;">' +
                                                        '<span th:each="role : ${profile.userRoles}">' +
                                                        rolesListPara +
                                                        '</span>' +
                                                        '</div>';
                                                
                                
                                                
                                $('#userRolesTableCell').replaceWith(roleListElement);
                                
                                showToastr('success', 'Roles for ' + user + '] updated!');
                            }
                        });
                    }
                }
            });
            
            box.on("shown.bs.modal", function(e) { 
                
                $.ajax({

                    type: "GET",
                    contentType: "application/json",
                    url: "/api/user/roles/all",
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {
            
                    var selectCityOptions = '';
                    
                    $.each(data, function (key, index) {

                        selectCityOptions = selectCityOptions + '<option value="' + index.roleName + '">' + index.roleDisplayName + '</option>';

                    });
                    
                    $('#availableRolesList').append(selectCityOptions);
                    
                    $.ajax({

                        type: "GET",
                        contentType: "application/json",
                        url: "/api/user/roles/" + uuId,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) {

                        var selectCityOptions = '';

                        $.each(data, function (key, index) {

                            selectCityOptions = selectCityOptions + '<option value="' + index.roleName + '">' + index.roleDisplayName + '</option>';

                            // Remove option from main list
                            $('#availableRolesList option[value="' + index.roleName + '"]').remove();
                        });

                        $('#userRolesList').append(selectCityOptions);

                    });
                });
            });
            
            box.modal('show');
        }
    });
}


function BootboxAdminUserClaimsManage(uuId, user) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/user/user-admin-claims-manage.html',
        success: function (data) {
            
            var box = bootbox.confirm({

                message: data,
                title: "User claims for " + user,
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Update",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    if (result) {
                    
                        var data = {};
                    
                        data['userUuId'] = document.getElementById('userUuId').value;;
                        data['userClaimType'] = document.getElementById('userClaimType').value;;
                        data['userClaimValue'] = document.getElementById('userClaimTarget').value;

                        $.ajax({

                            type: "POST",
                            contentType: "application/json",
                            url: "/api/claims/new",
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function () {

                                location.reload('/admin/users/claims/' + uuId);
                            }
                        });
                    }
                }
            });
            
            box.on("shown.bs.modal", function(e) {
                
                $.ajax({
                    
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/claims/claimtypes",
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {
                    
                    $(e.currentTarget).find('input[name="userUuId"]').prop('value', uuId);
            
                    var selectClaimTypeOptions = '';
                    
                    $.each(data, function (key, index) {
                        
                        var claimsArray = index.split('_');
                        var claimsArray0 = claimsArray[0].charAt(0) + claimsArray[0].slice(1).toLowerCase();
                        var claimsArray1 = claimsArray[1].charAt(0) + claimsArray[1].slice(1).toLowerCase();
                        
                        selectClaimTypeOptions = selectClaimTypeOptions + '<option value="' + index + '">' + claimsArray0 + ' ' + claimsArray1 + '</option>';
                    });

                    $('#userClaimType').empty();
                    $('#userClaimType').append(selectClaimTypeOptions);
                    
                    OnUserClaimTypeChange();
                });
            });
            
            box.modal('show');
        }
    });
}



function AdminUserClaimDelete(id) {
    
    bootbox.confirm({
                    
        message: 'Do you really want to permanently delete the user claim?',
        title: "Delete user claim",
        size: 'small',
        buttons: {
            cancel: {
                label: "Cancel",
                className: "btn-danger btn-fixed-width-100"
            },
            confirm: {
                label: "Delete",
                className: "btn-success btn-fixed-width-100"
            }
        },
        callback: function (result) {

            if (result) {
                
                $.ajax({
                    
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/claims/delete/" + id,
                    dataType: "json",
                    cache: false
                })
                .done(function () {

                    $('#claim-id-' + id).hide();
                });
            }
        }
    });
}


function OnUserClaimTypeChange() {
    
    var claimType = $('#userClaimType').val();
    var claimsArray = claimType.split('_');
    var targetType = claimsArray[0];
    
    console.log(targetType);
    
    if (targetType === "PROJECT") {
        
        $.ajax({
                    
            type: "GET",
            contentType: "application/json",
            url: "/api/projects/list",
            dataType: "json",
            cache: false
        })
        .done(function (data) {
            
            var selectProjectsOptions = '';
                    
            $.each(data, function (key, index) {

                selectProjectsOptions = selectProjectsOptions + '<option value="' + index.id + '">' + index.projectShortName + '</option>';
            });

            $('#userClaimTarget').empty();
            $('#userClaimTarget').append(selectProjectsOptions);
            
        });
        
    } else if (targetType === "DOCUMENT") {
        
        $.ajax({
                    
            type: "GET",
            contentType: "application/json",
            url: "/api/artifacts/list",
            dataType: "json",
            cache: false
        })
        .done(function (data) {
            
            var selectDocumentOptions = '';
                    
            $.each(data, function (key, index) {

                selectDocumentOptions = selectDocumentOptions + '<option value="' + index.id + '">' + index.artifactLongName + '</option>';
            });

            $('#userClaimTarget').empty();
            $('#userClaimTarget').append(selectDocumentOptions);
            
        });
        
    } else if (targetType === "CATEGORY") {
        
        $.ajax({

            global: false,
            url:'/api/projects/selected',
            type: "GET",
            dataType: "json",
            success: function (data) {

                var project = data;
                
                $.ajax({

                    global: false,
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/categories/list/" + project.id,
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {

                    var selectDocumentOptions = '';

                    $.each(data, function (key, index) {

                        selectDocumentOptions = selectDocumentOptions + '<option value="' + index.id + '">' + index.categoryName + '</option>';
                    });

                    $('#userClaimTarget').empty();
                    $('#userClaimTarget').append(selectDocumentOptions);

                });
            }
        });
    }
}


function BootboxProjectDetailsEdit(id) {
    
    bootbox.alert('Not implemented: ' + id);
}


function BootboxProjectCategories(id) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/project/project-categories-create.html',
        success: function (data) {
            
            var box = bootbox.confirm({

                message: data,
                title: "Create project item category",
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Create",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    if (result) {
                        
                        var data = {};
                        
                        data['projectId'] = id;
                        data['categoryName'] = document.getElementById('categoryName').value;
                        data['categoryCode'] = document.getElementById('categoryCode').value;
                        data['categoryParentId'] = document.getElementById('categoryParent').value;
                        data['categoryLeadUuId'] = document.getElementById('categoryLead').value;
                        
                        console.log(data);
                        
                        $.ajax({
                            
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/project/category/create",
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function (data) {

                                location.replace('/projects/metadata/' + id);
                                showToastr('success', 'Password for ' + data.categoryName +' changed!');
                            }
                        });
                    }
                }
            });
            
            box.on("shown.bs.modal", function(e) {
                
                // Empty all SELECT controls
                $(e.currentTarget).find('select[name="categoryParent"]').empty();
                $(e.currentTarget).find('select[name="categoryLead"]').empty();
                $(e.currentTarget).find('select[id="categoryParent"]').append('<option value="">-- Select Parent Catgory --</option>');
                 
                $.ajax({
            
                    url: '/api/categories/list/' + id,
                    type: "GET",
                    dataType: "json",
                    success: function (data) {

                        // Populate dropdown controls
                        var selectCategoryOptions = '';

                        $.each(data, function (key, index) {

                            selectCategoryOptions = selectCategoryOptions + '<option value="' + index.id + '">' + index.categoryName + '</option>';
                        });

                        $(e.currentTarget).find('select[id="categoryParent"]').append(selectCategoryOptions);
                    }
                });
                
                $.ajax({
            
                    url: '/api/users/list',
                    type: "GET",
                    dataType: "json",
                    success: function (data) {

                        // Populate dropdown controls
                        var selectUserOptions = '';

                        $.each(data, function (key, index) {

                            selectUserOptions = selectUserOptions + '<option value="' + index.uuId + '">' + index.firstName + ' ' + index.lastName + '</option>';
                        });

                        $(e.currentTarget).find('select[id="categoryLead"]').append(selectUserOptions);
                    }
                });
                
                $(e.currentTarget).find('input[name="parentId"]').prop('value', id);
                 
            });
            
            box.modal('show');
        }
    });
    
}


// Manage user roles script
function AddRemoveRoles(btn) {

    if (btn === 'RoleToRight') {

        var selectedOpts = $('#availableRolesList option:selected');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
        }

        $('#userRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
    }

    if (btn === 'RolesAllToRight') {
        
        var selectedOpts = $('#availableRolesList option');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
        }

        $('#userRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
    }

    if (btn === 'RoleToLeft') {
        
        var selectedOpts = $('#userRolesList option:selected');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
        }

        $('#availableRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
    }

    if (btn === 'RolesAllToLeft') {
        
        var selectedOpts = $('#userRolesList option');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
        }

        $('#availableRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
    }
}


function BootboxAdminUserDelete(uuId, user) {
    
    bootbox.confirm({
                    
        message: 'Do you really want to permanently delete the user <b>' + user + '</b>?',
        title: "Delete user",
        size: 'small',
        buttons: {
            cancel: {
                label: "Cancel",
                className: "btn-danger btn-fixed-width-100"
            },
            confirm: {
                label: "Delete",
                className: "btn-success btn-fixed-width-100"
            }
        },
        callback: function (result) {

            if (result) {
                
                $.ajax({
                    
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/users/delete/" + uuId,
                    dataType: "json",
                    cache: false
                })
                .done(function () {

                    location.replace("/admin/users");
                });
            }
        }
    });
}


function BootboxUserChangePassword(uuId) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/user/user-password-change.html',
        success: function (data) {

            bootbox.confirm({

                message: data,
                title: "Change password",
                size: 'small',
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Change",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {

                    if (result) {

                        var data = {};

                        data['uuId'] = uuId;
                        data['currentPassword'] = document.getElementById('oldPassword').value;
                        data['newPassword'] = document.getElementById('newPassword').value;
                        data['newPasswordConfirm'] = document.getElementById('confirmNewPassword').value;
                        
                        $.ajax({
                            
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/user/password/change/" + uuId,
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function (data) {

                                showToastr('success', 'Password for ' + uuId +' changed!');
                            }
                        });
                    }
                }
            }); 
        }
    });
}


function BootboxUserLogout() { 
    
    bootbox.confirm({
                    
        message: 'Do you want to Logout?',
        title: "Logout",
        size: 'small',
        buttons: {
            cancel: {
                label: "Cancel",
                className: "btn-danger btn-fixed-width-100"
            },
            confirm: {
                label: "Logout",
                className: "btn-success btn-fixed-width-100"
            }
        },
        callback: function (result) {
            
            if (result) {
                
                location.replace('/logout');
            }
        }
    });
}


function itemCreateClassChange() {

    var itemClass = document.getElementById('createItemClass');

    if (itemClass.value === "REQUIREMENT") {

        document.getElementById('createIdentTemplate').disabled = false;
        document.getElementById('createItemType').disabled = false;
        document.getElementById('createIdentifier').disabled = false;

        $('#createRequirementRow').show();
        $('#createIdentifierContainer').show();

        $.ajax({
            
            global: false,
            type: "GET",
            url: "/api/items/nextidentifier",
            data: {id: document.getElementById('artifactId').value, template: document.getElementById('createIdentTemplate').value},
            dataType: "text",
            timeout: 60000,
            success: function (responseText) {

                document.getElementById('createIdentifier').value = responseText;
            }
        });

    } else {

        document.getElementById('createIdentTemplate').disabled = true;
        document.getElementById('createItemType').disabled = true;
        document.getElementById('createIdentifier').disabled = true;

        $('#createRequirementRow').hide();
        $('#createIdentifierContainer').hide();
    }
}


function itemCreateIdentTemplateChange() {

    $.ajax({
        
        global: false,
        type: "GET",
        url: "/api/items/nextidentifier",
        data: {id: document.getElementById('artifactId').value, template: document.getElementById('createIdentTemplate').value},
        dataType: "text",
        timeout: 60000,
        success: function (responseText) {

            document.getElementById('createIdentifier').value = responseText;
        }
    });
}


function moveItemUp(id) {

    $.ajax({
        
        type: "GET",
        contentType: "application/json",
        url: "/api/items/moveup/" + id,
        dataType: "json",
        timeout: 60000,
        success: function () {

            location.reload();
            showToastr('success', 'Item [' + id + '] moved up!');
        },
        error: function () {

            showToastr('error', 'Error: failed to move item [' + id + '] up!');
        }
    });
}


function addIdentFormatToMultipSelect() {

    var select = document.getElementById('formats');
    var opt = document.createElement('option');

    opt.value = document.getElementById('identFormatAdd').value;
    opt.innerHTML = document.getElementById('identFormatAdd').value;

    select.appendChild(opt);
}


function moveItemDown(id) {

    $.ajax({
        
        type: "GET",
        contentType: "application/json",
        url: "/api/items/movedown/" + id,
        dataType: "json",
        timeout: 60000,
        success: function () {

            location.reload();
            showToastr('success', 'Item [' + id + '] moved down!');
        },
        error: function () {

            showToastr('error', 'Error: failed to move item [' + id + '] down!');
        }
    });
}


function BootboxEditItem(id) {
    
    var itemDao = '';
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/item/edit/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        itemDao = data;

        $.ajax({
                    
            global: false,        
            type: "GET",
            url: '/modal/item/item-edit-form.html',
            success: function (data) {

                var box = bootbox.confirm({
                    
                    message: data,
                    title: "Edit Item: [ " + itemDao.item.sysId + " ]",
                    buttons: {
                        cancel: {
                            label: "Cancel",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        confirm: {
                            label: "Save",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) {
                        
                        if (result) {
                            
                            var newItemValue = '';
                            var data = {};

                            data['id'] = document.getElementById('editId').value;
                            data['artifactId'] = document.getElementById('artifactId').value;
                            data['sysId'] = document.getElementById('editSysId').value;
                            data['itemClass'] = document.getElementById('editItemClass').value;
                            data['itemType'] = document.getElementById('editItemType').value;
                            data['identifier'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('editIdentifier').value : '';
                            data['itemLevel'] = document.getElementById('editItemLevel').value;
                            data['itemValue'] = document.getElementById('editItemValue').value;

                            newItemValue = data['itemValue'];
                            
                            $.ajax({

                                global: false,
                                type: "POST",
                                contentType: "application/json",
                                url: "/api/items/save",
                                data: JSON.stringify(data),
                                dataType: "json",
                                timeout: 60000,
                                success: function (data) {

                                    if (data.itemClass === "HEADER") {

                                        var oldValue = document.getElementById('value-' + data.sysId);
                                        var newValue = '<span id="value-' + data.sysId + '">' + newItemValue.replace(/\n/g, '<br/>') + '</span>';
                                        
                                        document.getElementById(data.sysId).className = 'row item-header-' + data.itemLevel;
                                        $(oldValue).replaceWith(newValue);

                                    } else if (data.itemClass === "REQUIREMENT") {

                                        var reqElementId = document.getElementById('ident-' + data.sysId);

                                        // Not a requirement yet (converted from prose)
                                        if (!document.getElementById(reqElementId)) {

                                            var newItemElement = '<div id="' + data.sysId + '" class="row item-level-' + data.itemLevel + '">' +
                                                    '<div id="value-container-' + data.sysId + '" class="col-xs-9 requirement-text">' +
                                                    '<span id="value-' + data.sysId + '">' + newItemValue.replace(/\n/g, '<br/>') + '</span>' +
                                                    '</div>' +
                                                    '<div class="col-xs-2 requirement-identifier" id="ident-container-' + data.sysId + '">' +
                                                    '<span id="ident-' + data.sysId + '">' + data.identifier + '</span>' +
                                                    '</div>' +
                                                    '<div class="btn-group pull-right normal-text" id="menu-container-' + data.sysId + '">' +
                                                    document.getElementById('menu-container-' + data.sysId).innerHTML +
                                                    '</div>' +
                                                    '</div>';

                                            var currentItemElement = document.getElementById(data.sysId);

                                            $(currentItemElement).replaceWith(newItemElement);
                                        }

                                    // Is PROSE or other itemClass
                                    } else {

                                        var newItemElement = '<div id="' + data.sysId + '" class="row item-level-' + data.itemLevel + ' ' + data.itemClass + '">' +
                                                '<div id="value-container-' + data.sysId + '" class="col-xs-11 ' + data.itemClass.toLowerCase() + '">' +
                                                '<span id="value-' + data.sysId + '">' + newItemValue.replace(/\n/g, '<br/>') + '</span>' +
                                                '</div>' +
                                                '<div class="btn-group pull-right normal-text" id="menu-container-' + data.sysId + '">' +
                                                document.getElementById('menu-container-' + data.sysId).innerHTML +
                                                '</div>' +
                                                '</div>';

                                        var currentItemElement = document.getElementById(data.sysId);

                                        $(currentItemElement).replaceWith(newItemElement);
                                    }

                                    showToastr('success', '[' + data.sysId + '] modfied!');
                                },
                                error: function () {

                                    $('#editItemModal').modal('hide');
                                    showToastr('error', 'An error occured updating item [' + data.sysId + ']!');
                                }
                            });
                        }
                    }
                });
                
                box.on("shown.bs.modal", function(e) { 
  
                    $(e.currentTarget).find('input[name="editArtifactId"]').prop('value', itemDao.item.artifact.id);
                    $(e.currentTarget).find('input[name="editId"]').prop('value', itemDao.item.id);
                    $(e.currentTarget).find('input[name="editSysId"]').prop('value', itemDao.item.sysId);
    
                    // Empty all SELECT controls
                    $(e.currentTarget).find('select[name="editItemLevel"]').empty();
                    $(e.currentTarget).find('select[name="editItemClass"]').empty();
                    $(e.currentTarget).find('select[name="editItemType"]').empty();
                    $(e.currentTarget).find('select[name="editIdentTemplate"]').empty();
                    $(e.currentTarget).find('select[name="editMediaType"]').empty();
                    
                    // Clear INPUT and TEXTAREA controls
                    $(e.currentTarget).find('input[name="editIdentifier"]').val('');
                    $(e.currentTarget).find('textarea[name="editItemValue"]').val('');
                    
                    // Populate Item Levels SELECT
                    $.each(itemDao.itemLevels, function (key, index) {

                        $(e.currentTarget).find('select[name="editItemLevel"]').append('<option value="' + index + '">Level ' + index + '</option>');
                    });
                    
                    // Populate Item Classes SELECT
                    $.each(itemDao.itemClasses, function (key, index) {

                        $(e.currentTarget).find('select[name="editItemClass"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Populate Requirement Types SELECT
                    $.each(itemDao.itemTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="editItemType"]').append('<option value="' + index.itemTypeName + '">' + index.itemTypeLongName + '</option>');
                    });
                    
                    // Populate Requirement Ident Templates SELECT
                    $.each(itemDao.identPrefices, function (key, index) {

                        $(e.currentTarget).find('select[name="editIdentTemplate"]').append('<option value="' + index.variableValue + '">' + index.variableValue + '</option>');
                    });
                    
                    // Populate Media Types SELECT
                    $.each(itemDao.mediaTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="editMediaType"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Selected values for dropdowns
                    $(e.currentTarget).find('select[name="editItemLevel"]').prop('value', itemDao.item.itemLevel);
                    $(e.currentTarget).find('select[name="editItemClass"]').prop('value', itemDao.item.itemClass);
                    $(e.currentTarget).find('select[name="editMediaType"]').prop('value', itemDao.item.mediaType);
                    $(e.currentTarget).find('textarea[name="editItemValue"]').val(itemDao.item.itemValue);

                    if (isEmpty(itemDao.item.identifier)) {

                        $(e.currentTarget).find('select[name="editItemType"]').prop('disabled', true);
                        $(e.currentTarget).find('select[name="editIdentTemplate"]').prop('disabled', true);
                        $(e.currentTarget).find('input[name="editIdentifier"]').prop('disabled', true);

                    } else {   // Is a requirement

                        var s = itemDao.item.identifier;
                        var indentTemplValue = s.substring(0, s.lastIndexOf('-'));

                        $(e.currentTarget).find('select[name="editItemType"]').prop('value', itemDao.item.itemType.itemTypeName);
                        $(e.currentTarget).find('select[name="editIdentTemplate"]').prop('value', indentTemplValue);
                        $(e.currentTarget).find('input[name="editIdentifier"]').val(itemDao.item.identifier);
                    
                        $(e.currentTarget).find('select[name="editItemType"]').prop('disabled', false);
                        $(e.currentTarget).find('select[name="editIdentTemplate"]').prop('disabled', false);
                        $(e.currentTarget).find('input[name="editIdentifier"]').prop('disabled', false);
                    }
                    
                    if (itemDao.item.itemClass !== "REQUIREMENT") {

                        $(e.currentTarget).find('div[id="editRequirementRow"]').hide();
                        $(e.currentTarget).find('div[id="editIdentField"]').hide();

                    } else {

                        $(e.currentTarget).find('div[id="editRequirementRow"]').show();
                        $(e.currentTarget).find('div[id="editIdentField"]').show();
                    }
                    
                    $.ajax({
                        
                        global: false,
                        url: '/api/link/incominglinks/' + itemDao.item.id,
                        type: "GET",
                        dataType: "json",
                        success: function (data) {

                            if (data > 0) {

                                var warningMsg  =   '<div id="linkedItemsWarning" style="padding: 8px 0; color: red; text-align: center; font-weight: bold;">' +
                                                    '<span>WARNING: This item has ' + data + ' incoming link(s).<br/>Modification of the item could impact the linking item(s).</span>' +
                                                    '</div>';

                                $('#linkedItemsWarning').remove();
                                $(warningMsg).insertBefore($('#itemDetalsGroup'));

                            } else {

                                $('#linkedItemsWarning').remove();
                            }
                        }
                    });
                });

                box.modal('show');
            }
        });
    });
}



function BootboxCreateFirstItem(id) {
    
    var url = '/api/item/createfirst/' + id;
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: url,
        dataType: "json",
        cache: false
    })
    .done(function (data) { 
        
        var refItem = data;
        
        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/item/item-create-form.html',
            success: function (data) {
                
                var box = bootbox.confirm({
                    
                    message: data,
                    title: "Create new Item",
                    buttons: {
                        cancel: {
                            label: "Cancel",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        confirm: {
                            label: "Save",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) { 
                        
                        if (result) {

                            var data = {};

                            data['artifactId'] = document.getElementById('createArtifactId').value;
                            data['itemClass'] = document.getElementById('createItemClass').value;
                            data['identifier'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('createIdentifier').value : '';
                            data['itemType'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('createItemType').value : '';
                            data['itemLevel'] = document.getElementById('createItemLevel').value;
                            data['itemValue'] = document.getElementById('createItemValue').value;
                            data['sortIndex'] = document.getElementById('createSortIndex').value;
                        
                            $.ajax({
                                
                                global: false,
                                type: "POST",
                                contentType: "application/json",
                                url: "/api/items/new",
                                data: JSON.stringify(data),
                                dataType: "json",
                                timeout: 60000,
                                success: function () {
                                   
                                    location.reload();
                                }
                            });
                        }
                    }
                });
                
                box.on("shown.bs.modal", function(e) {
                   
                    $(e.currentTarget).find('input[name="createArtifactId"]').prop('value', id);
                    $(e.currentTarget).find('input[name="createSortIndex"]').prop('value', 0);

                    // Empty all SELECT controls
                    $(e.currentTarget).find('select[name="createItemLevel"]').empty();
                    $(e.currentTarget).find('select[name="createItemClass"]').empty();
                    $(e.currentTarget).find('select[name="createItemType"]').empty();
                    $(e.currentTarget).find('select[name="createIdentTemplate"]').empty();
                    $(e.currentTarget).find('select[name="createMediaType"]').empty();
                    
                    // Clear INPUT and TEXTAREA controls
                    $(e.currentTarget).find('input[name="createIdentifier"]').val('');
                    $(e.currentTarget).find('textarea[name="createItemValue"]').val('');
                    
                    
                    // Populate Item Levels SELECT
                    $.each(refItem.itemLevels, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemLevel"]').append('<option value="' + index + '">Level ' + index + '</option>');
                    });
                    
                    // Populate Item Classes SELECT
                    $.each(refItem.itemClasses, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemClass"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Populate Requirement Types SELECT
                    $.each(refItem.itemTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemType"]').append('<option value="' + index.itemTypeName + '">' + index.itemTypeLongName + '</option>');
                    });
                    
                    // Populate Requirement Ident Templates SELECT
                    $.each(refItem.identPrefices, function (key, index) {

                        $(e.currentTarget).find('select[name="createIdentTemplate"]').append('<option value="' + index.variableValue + '">' + index.variableValue + '</option>');
                    });
                    
                    // Populate Media Types SELECT
                    $.each(refItem.mediaTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="createMediaType"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Default selected values for dropdowns
                    $(e.currentTarget).find('select[name="createItemLevel"]').prop('value', 1);
                    $(e.currentTarget).find('select[name="createItemClass"]').prop('value', "REQUIREMENT");
                    $(e.currentTarget).find('select[name="createItemType"]').prop('value', "Functional");
                    $(e.currentTarget).find('select[name="createMediaType"]').prop('value', "TEXT");
                          
                    itemCreateIdentTemplateChange();
                });
                
                box.modal('show');
            }
        });
    });
}


function BootboxViewItemDetails(id) {
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/item/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var item = data;

        console.log(item);
        
        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/item/item-details-view.html',
            success: function (data) {
                
                var box = bootbox.alert({
                    
                    message: data,
                    title: 'Item <strong>' + item.sysId + '</strong> details',
                    size: 'large',
                    buttons: {
                        ok: {
                            label: "Close",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) { 
                        
                        if (result) {
                            
                            
                        }
                    }
                });
                
                box.on("shown.bs.modal", function(e) {
                    
                    $(e.currentTarget).find('td[id="sysId"]').html(item.sysId);
                    $(e.currentTarget).find('td[id="artifactLongName"]').html(item.artifact.artifactLongName);
                    $(e.currentTarget).find('td[id="itemDetails"]').html(item.itemValue.replace(/\n/g, '<br/>'));
                    $(e.currentTarget).find('td[id="itemClass"]').html(item.itemClass);
                    
                    $.ajax({

                        global: false,
                        type: "GET",
                        contentType: "application/json",
                        url: "/api/itemcomments/" + id,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) {

                        var comments = '';

                        $.each(data, function (key, index) {

                            comments = comments + '<div class="row" style="padding: 0 0 10px 16px;">';
                            comments = comments + '<p><strong>' + index.author.firstName + ' ' + index.author.lastName + '</strong></p>';
                            comments = comments + '<span>' + index.comment.replace(/\n/g, '<br/>') + '</span>';
                            comments = comments + '</div>';
                        });

                        $(e.currentTarget).find('td[id="itemComments"]').html(comments);

                        if (comments === '') { $('#rowItemComments').remove(); }
                    });

                    if (item.itemClass === "REQUIREMENT") {
                        
                        $(e.currentTarget).find('td[id="itemIdentifier"]').html(item.identifier);
                        $(e.currentTarget).find('td[id="itemType"]').html(item.itemType.itemTypeLongName);
                        $(e.currentTarget).find('td[id="itemStatus"]').html(item.itemStatus);
                        $(e.currentTarget).find('td[id="itemCategory"]').html(item.itemCategory.categoryName);
                        $(e.currentTarget).find('td[id="itemLead"]').html(item.itemCategory.lead.firstName + ' ' + item.itemCategory.lead.lastName);
                        
                        $.ajax({

                            global: false,
                            type: "GET",
                            contentType: "application/json",
                            url: '/api/verification/details/' + id,
                            dataType: "json",
                            cache: false
                        })
                        .done(function (data) { 

                            var verification = data;
                            $(e.currentTarget).find('td[id="vvMethod"]').html(verification.method.methodName);
                            $(e.currentTarget).find('td[id="vvReferences"]').html(verification.vvReferences.replace(/\n/g, '<br/>'));         
                        }); 
                                                
                    } else {
                        
                        $('#rowItemIdentifier').remove();
                        $('#rowItemType').remove();
                        $('#rowItemStatus').remove();
                        $('#rowItemCategory').remove();
                        $('#rowItemLead').remove();
                        $('#rowVvMethod').remove();
                        $('#rowVvReferences').remove();
                    }
                });
                
                box.modal('show');
            }
        });
    });
}


function BootboxPerformTask(id) {
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/tasks/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var task = data;
        var taskAction = task.taskAction.replace("_REQ", "");
        
        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/item/item-task-perform-form.html',
            success: function (data) {
                
                var box = bootbox.alert({
                    
                    message: data,
                    title: 'Performing Task: #<strong>' + id + '</strong>',
                    size: 'large',
                    buttons: {
                        ok: {
                            label: "Cancel",
                            className: "btn-danger btn-fixed-width-100"
                        }
                    },
                    callback: function (result) { 
                        
                        if (result) {
                            
                            /* Do nothing */
                        }
                    }
                });
                
                box.on("shown.bs.modal", function(e) {
                    
                    if (taskAction === "CONFIRM") {
                        
                        $('#acknowledgeButton').remove();
                        $(e.currentTarget).find('button[id="confirmButtom"]').attr('onclick', 'javascript:BootboxPerformTaskAction(\'' + task.id + '\', \'CONFIRMATION\')');
                        
                    } else {
                        
                        $('#confirmButtom').remove();
                        $(e.currentTarget).find('button[id="acknowledgeButton"]').attr('onclick', 'javascript:BootboxPerformTaskAction(\'' + task.id + '\', \'ACKNOWLEDGEMENT\')');
                    }
                        
                    $(e.currentTarget).find('button[id="commentButton"]').attr('onclick', 'javascript:BootboxPerformTaskComment(\'' + task.id + '\')');
                   
                    $(e.currentTarget).find('span[id="itemIdentifier"]').html(task.taskItem.identifier);
                    $(e.currentTarget).find('td[id="sysId"]').html(task.taskItem.sysId);
                    $(e.currentTarget).find('td[id="taskAction"]').html(taskAction);
                    $(e.currentTarget).find('td[id="itemDetails"]').html(task.taskItem.itemValue.replace(/\n/g, '<br/>'));
                    $(e.currentTarget).find('td[id="itemStatus"]').html(task.taskItem.itemStatus);
                    
                    /* Load V&V method and references */
                    $.ajax({

                        global: false,
                        type: "GET",
                        contentType: "application/json",
                        url: '/api/verification/details/' + task.taskItem.id,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) { 

                        var verification = data;
                        $(e.currentTarget).find('td[id="vvMethod"]').html(verification.method.methodName);
                        $(e.currentTarget).find('td[id="vvReferences"]').html(verification.vvReferences.replace(/\n/g, '<br/>'));         
                    }); 
                    
                    /* Load comments */
                    $.ajax({

                        global: false,
                        type: "GET",
                        contentType: "application/json",
                        url: "/api/itemcomments/" + task.taskItem.id,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) {

                        var comments = '';

                        $.each(data, function (key, index) {

                            comments = comments + '<div class="row" style="padding: 0 0 10px 16px;">';
                            comments = comments + '<p><strong>' + index.author.firstName + ' ' + index.author.lastName + '</strong></p>';
                            comments = comments + '<span>' + index.comment.replace(/\n/g, '<br/>') + '</span>';
                            comments = comments + '</div>';
                        });

                        $(e.currentTarget).find('td[id="itemComments"]').html(comments);

                        if (comments === '') { $('#rowItemComments').remove(); }
                    });
                });
                
                box.modal('show');
            }
        });
    });
}


function BootboxCreateItem(id, pos) {
 
    var url = "/api/item/create/" + id; 
    var newSortIndex = 0;
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: url,
        dataType: "json",
        cache: false
    })
    .done(function (data) { 
    
        var refItem = data;

        if (pos === "ABOVE") { newSortIndex = refItem.item.sortIndex; } 
        else { newSortIndex = refItem.item.sortIndex + 1; }

        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/item/item-create-form.html',
            success: function (data) {
                
                var box = bootbox.confirm({
                    
                    message: data,
                    title: "Create new Item",
                    buttons: {
                        cancel: {
                            label: "Cancel",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        confirm: {
                            label: "Save",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) { 
                        
                        if (result) {
                            
                            var data = {};
                            
                            data['artifactId'] = document.getElementById('createArtifactId').value;
                            data['itemClass'] = document.getElementById('createItemClass').value;
                            data['identifier'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('createIdentifier').value : '';
                            data['itemType'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('createItemType').value : '';
                            data['itemLevel'] = document.getElementById('createItemLevel').value;
                            data['itemValue'] = document.getElementById('createItemValue').value;
                            data['sortIndex'] = document.getElementById('createSortIndex').value;
                            
                            $.ajax({
                                
                                type: "POST",
                                contentType: "application/json",
                                url: "/api/items/new",
                                data: JSON.stringify(data),
                                dataType: "json",
                                timeout: 60000,
                                success: function (data) {

                                    var refItemId = id;
                                    var refSortIdx = refItem.item.sortIndex;
                                    var refElementId = refItem.item.sysId;

                                    // First item in the document (no other items have been created)
                                    if (refElementId === null) {

                                        location.reload();
                                        return true;
                                    }
                                    
                                    var newItemElement = '';
                                    
                                    var regex1 = new RegExp(refItemId, "g");
                                    var regex2 = new RegExp(refElementId, "g");
                                    var regex3 = new RegExp(refSortIdx, "g");

                                    var menuElement = $(document.getElementById('menu-container-' + refElementId)).clone().html();

                                    menuElement = menuElement.replace(regex1, data.id);
                                    menuElement = menuElement.replace(regex2, data.sysId);

                                    var menuElementHTML = menuElement.replace(regex3, data.sortIndex);
                                    
                                    if (data['itemClass'] === "REQUIREMENT") {

                                        newItemElement = '<div id="' + data.sysId + '" class="row item-level-' + data.itemLevel + '">' +
                                                '<div id="value-container-' + data.sysId + '" class="col-xs-9 requirement-text">' +
                                                '<span id="value-' + data.sysId + '">' + data.itemValue + '</span>' +
                                                '</div>' +
                                                '<div class="col-xs-2 requirement-identifier" id="ident-container-' + data.sysId + '">' +
                                                '<span id="ident-' + data.sysId + '">' + data.identifier + '</span>' +
                                                '</div>' +
                                                '<div class="btn-group pull-right normal-text" id="menu-container-' + data.sysId + '">' +
                                                menuElementHTML +
                                                '</div>';

                                    } else {

                                        var itemClass = (data.itemClass === "HEADER") ? 'item-header-' + data.itemLevel : 'item-level-' + data.itemLevel;

                                        newItemElement = '<div id="' + data.sysId + '" class="row ' + itemClass + '">' +
                                                '<div id="value-container-' + data.sysId + '" class="col-xs-11">' +
                                                '<span id="value-' + data.sysId + '">' + data.itemValue + '</span>' +
                                                '</div>' +
                                                '<div class="btn-group pull-right normal-text" id="menu-container-' + data.sysId + '">' +
                                                menuElementHTML +
                                                '</div>';
                                    }
                                    
                                    if (pos === "ABOVE") {

                                        $(newItemElement).insertBefore('#' + refElementId);

                                    } else if (pos === "BELOW") {

                                        $(newItemElement).insertAfter('#' + refElementId);
                                    }

                                    $(data.sysId).show();

                                    showToastr('success', 'Item successfully created!');
                                }
                            });
                        }
                    }
                });
                
                box.on("shown.bs.modal", function(e) {
                   
                    $(e.currentTarget).find('input[name="createArtifactId"]').prop('value', refItem.item.artifact.id);
                    $(e.currentTarget).find('input[name="createSortIndex"]').prop('value', newSortIndex);

                    // Empty all SELECT controls
                    $(e.currentTarget).find('select[name="createItemLevel"]').empty();
                    $(e.currentTarget).find('select[name="createItemClass"]').empty();
                    $(e.currentTarget).find('select[name="createItemType"]').empty();
                    $(e.currentTarget).find('select[name="createIdentTemplate"]').empty();
                    $(e.currentTarget).find('select[name="createMediaType"]').empty();
                    
                    // Clear INPUT and TEXTAREA controls
                    $(e.currentTarget).find('input[name="createIdentifier"]').val('');
                    $(e.currentTarget).find('textarea[name="createItemValue"]').val('');
                    
                    // Populate Item Levels SELECT
                    $.each(refItem.itemLevels, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemLevel"]').append('<option value="' + index + '">Level ' + index + '</option>');
                    });
                    
                    // Populate Item Classes SELECT
                    $.each(refItem.itemClasses, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemClass"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Populate Requirement Types SELECT
                    $.each(refItem.itemTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="createItemType"]').append('<option value="' + index.itemTypeName + '">' + index.itemTypeLongName + '</option>');
                    });
                    
                    // Populate Requirement Ident Templates SELECT
                    $.each(refItem.identPrefices, function (key, index) {

                        $(e.currentTarget).find('select[name="createIdentTemplate"]').append('<option value="' + index.variableValue + '">' + index.variableValue + '</option>');
                    });
                    
                    // Populate Media Types SELECT
                    $.each(refItem.mediaTypes, function (key, index) {

                        $(e.currentTarget).find('select[name="createMediaType"]').append('<option value="' + index + '">' + index + '</option>');
                    });
                    
                    // Default selected values for dropdowns
                    $(e.currentTarget).find('select[name="createItemLevel"]').prop('value', 1);
                    $(e.currentTarget).find('select[name="createItemClass"]').prop('value', "REQUIREMENT");
                    $(e.currentTarget).find('select[name="createItemType"]').prop('value', "Functional");
                    $(e.currentTarget).find('select[name="createMediaType"]').prop('value', "TEXT");
                          
                    itemCreateIdentTemplateChange();
                });
                
                box.modal('show');
            }
        });
    });
}



function BootboxDeleteItem(id) {
 
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/item/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var itemToDelete = data;

        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/item/item-delete-form.html',
            success: function (data) {

                var box = bootbox.confirm({
                    
                    message: data,
                    title: "Delete Item [ " + itemToDelete.sysId + " ]",
                    buttons: {
                        cancel: {
                            label: "Cancel",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        confirm: {
                            label: "Delete",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) {
                        
                        if (result) {
                            
                           $.ajax({
                               
                                global: false,
                                type: "GET",
                                contentType: "application/json",
                                url: "/api/item/delete/" + id,
                                timeout: 60000,
                                success: function () {
                                    
                                    $('#' + itemToDelete.sysId).remove();
                                    showToastr('success', 'Item '+ itemToDelete.sysId +' deleted!');
                                }
                            });
                        }
                    }
                }); 
                
                box.on("shown.bs.modal", function(e) {
                    
                    $.ajax({

                        global: false,
                        type: "GET",
                        contentType: "application/json",
                        url: "/api/link/links/" + id,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) {

                        var cantDelete = false;
                       
                        $.each(data, function (key, index) {
                            
                            if (parseInt(index.dstItem.id) === parseInt(id)) {
                                
                                cantDelete = true;
                            }
                        });

                        $(e.currentTarget).find('input[name="itemId"]').prop('value', id);
                        $(e.currentTarget).find('input[name="cantDelete"]').prop('value', cantDelete);
                        $(e.currentTarget).find('#itemValue').text(itemToDelete.itemValue);
                        
                        if (cantDelete) {
                            
                            var warningMsg  =   '<div id="linkedItemsWarning" style="padding: 8px 0; color: red; text-align: center;">' +
                                                '<span><strong>WARNING:</strong> This item has incoming link(s).<br/>' +
                                                'Deletion is <strong>NOT ALLOWED</strong> before unlinking the linking item(s).</span>' +
                                                '</div>';

                            $('#linkedItemsWarning').remove();
                            $("[data-bb-handler=confirm]").remove();
                            $(warningMsg).insertAfter($('#itemValueGroup'));
                        }
                    });
                    
                });
                
                box.modal('show');
            }
        });
    });
}


function BootboxPerformTaskAction(id, action) {
    
    var labelText = '';
    var confirm = false;
    
    if (action === 'CONFIRMATION') {
        
        labelText = 'Confirm';
        confirm = true;
        
    } else if (action === 'ACKNOWLEDGEMENT') {
        
        labelText = 'Acknowledge';
        confirm = false;
    }
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/tasks/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var task = data;

        bootbox.confirm({
            
            message: task.taskItem.itemValue,
            title: labelText + " requirement: " + task.taskItem.identifier + "?",
            buttons: {
                cancel: {
                    label: "Cancel",
                    className: "btn-danger btn-sm btn-fixed-width-120"
                },
                confirm: {
                    label: labelText,
                    className: "btn-success btn-sm btn-fixed-width-120"
                }
            },
            callback: function (result) {

                if (result) {
                    
                    if (confirm === true) {
                        
                        $.ajax({

                            global: false,
                            type: "GET",
                            contentType: "application/json",
                            url: "/api/tasks/confirm/" + id,
                            dataType: "json",
                            cache: false
                        })
                        .done(function (data) {
                            
                            $('#task-id-' + data.id).remove();
                            showToastr('success', "Task ID#: " + data.id + " confirmed.");
                        });
                        
                    } else {
                       
                        $.ajax({

                            global: false,
                            type: "GET",
                            contentType: "application/json",
                            url: "/api/tasks/acknowledge/" + id,
                            dataType: "json",
                            cache: false
                        })
                        .done(function (data) {
                            
                            $('#task-id-' + data.id).remove();
                            showToastr('success', "Task ID#: " + data.id + " acknowledged.");
                        });
                    }
                }
            }
        });
    });
}


function BootboxPerformTaskComment(id) {
    
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/tasks/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var date = new Date().toISOString().substring(0, 10) + ': ';
        var task = data;
        
        var msg =   '<div class="row">' +
                    '<div class="col-md-12" style="padding: 15px;">' +
                    '<span>' + task.taskItem.itemValue + '</span>' +
                    '</div>' +
                    '<div class="col-md-12">' +
                    '<form action="/api/itemcomments/save" method="post">' +
                    '<div class="row" style="margin-top: 10px;">' +
                    '<div class="col-md-12">' +
                    '<div class="form-group">' +
                    '<input type="hidden" name="itemId" id="itemId" value="' + task.taskItem.id + '" />' +
                    '<label for="commentValue">Comment:</label>' +
                    '<textarea class="form-control" rows="8" cols="50" id="commentValue" name="commentValue">' + date + '</textarea>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</form>' +
                    '</div>' +
                    '</div>';

        bootbox.confirm({

            message: msg,
            title: "Add Comment to Task#: " + task.id,
            buttons: {
                cancel: {
                    label: 'Cancel',
                    className: 'btn-danger btn-fixed-width-100'
                },
                confirm: {
                    label: "Save",
                    className: "btn-success btn-fixed-width-100"}
            },
            callback: function (comment) {

                if (comment) {

                    var data = {};
                    data['itemId'] = task.taskItem.id;
                    data['comment'] = $("#commentValue").val();

                    $.ajax({

                        global: false,
                        type: "POST",
                        contentType: "application/json",
                        url: "/api/tasks/comment/" + id,
                        data: JSON.stringify(data),
                        dataType: "json",
                        timeout: 60000,
                        success: function (data) {
                            
                            $('#task-id-' + id).remove();
                            showToastr('success', "Comment for Task#: " + id + " created.");
                        }
                    });
                }
            }
        });
    });
}


function itemCreateIdentTemplateChange() {
    
    var itemClass = document.getElementById('createItemClass');
    
    if (itemClass.value === "REQUIREMENT") {

        document.getElementById('createIdentifier').disabled = false;
        document.getElementById('createItemType').disabled = false;
        document.getElementById('createIdentTemplate').disabled = false;
        
        $('#createRequirementRow').show();
        $('#createIdentField').show();
        
        itemCreateIdentTemplateChange();

    } else {

        document.getElementById('createIdentifier').disabled = true;
        document.getElementById('createItemType').disabled = true;
        document.getElementById('createIdentTemplate').disabled = true;
        
        $('#createRequirementRow').hide();
        $('#createIdentField').hide();
    }
}



function itemEditClassChange() {

    var itemClass = document.getElementById('editItemClass');
    
    if (itemClass.value === "REQUIREMENT") {

        document.getElementById('editIdentifier').disabled = false;
        document.getElementById('editItemType').disabled = false;
        document.getElementById('editIdentTemplate').disabled = false;
        
        $('#editRequirementRow').show();
        $('#editIdentField').show();
        
        itemEditIdentTemplateChange();

    } else {

        document.getElementById('editIdentifier').disabled = true;
        document.getElementById('editItemType').disabled = true;
        document.getElementById('editIdentTemplate').disabled = true;
        
        $('#editRequirementRow').hide();
        $('#editIdentField').hide();
    }
}



function itemCreateIdentTemplateChange() {
    
    var data = {
        
        id: document.getElementById('createArtifactId').value, 
        template: document.getElementById('createIdentTemplate').value
    };
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: "/api/items/nextidentifier",
        data: data,
        dataType: "text",
        timeout: 60000,
        success: function (responseText) {

            document.getElementById('createIdentifier').value = responseText;
        }
    });
}


function itemEditIdentTemplateChange() {
    
    var str = document.getElementById('editIdentifier').value;

    if (isEmpty(str)) {

        $.ajax({
            
            global: false,
            type: "GET",
            url: "/api/items/nextidentifier",
            data: {id: document.getElementById('artifactId').value, template: document.getElementById('editIdentTemplate').value},
            dataType: "text",
            timeout: 60000,
            success: function (responseText) {

                document.getElementById('editIdentifier').value = responseText;
            }
        });

    } else {

        var identTemplate = document.getElementById('editIdentTemplate').value;
        var editIdentifierNumericValue = str.substr(str.lastIndexOf("-") + 1);

        document.getElementById('editIdentifier').value = identTemplate + '-' + editIdentifierNumericValue;
    }
}

function isEmpty(str) {

    return (!str || 0 === str.length);
}


function BootboxDisplayComments(id) {

    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/itemcomments/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {

        var comments = '';
        
        $.each(data, function (key, index) {

            var date = new Date(index.creationDate);
            
            comments = comments + '<div class="row" style="margin: 10px 0; border-top: 1px solid #c0c0c0; padding: 8px 0;">';
            
            // Author column
            comments = comments + '<div class="col-sm-3">';
            comments = comments + '<span><b>' + index.author.firstName + '</b></span><br/>';
            comments = comments + '<span>' + date.toISOString().substring(0, 10) + '</span>';
            comments = comments + '</div>';
            
            // Comment text column
            comments = comments + '<div class="col-sm-9" style="border-left: 1px solid #c0c0c0;">';
            comments = comments + '<span>' + index.comment + '</span>';
            comments = comments + '</div>';
            
            comments = comments + '</div>';
        });
                
        var msg =   '<div class="row">' +
                    '<div class="col-sm-12" style="padding: 16px;">' +
                    '<span style="font-weight: bold;">' + data[0].item.itemValue + '</span>' +
                    '</div>' +
                    '<div class="col-sm-12">' +
                    '<form action="/api/itemcomments/save" method="post">' +
                    '<div class="row" style="margin-top: 10px;">' +
                    '<div class="col-sm-12">' +
                    '<div class="form-group">' +
                    '<input type="hidden" name="itemId" id="itemId" value="' + data[0].item.id + '" />' +
                    '<label for="commentValue">Comments:</label>' +
                    comments +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</form>' +
                    '</div>' +
                    '</div>';

        bootbox.confirm({

            message: msg,
            size: 'large',
            title: data[0].item.sysId + " Comments",
            buttons: {
                cancel: {
                    label: 'Close',
                    className: 'btn-danger btn-fixed-width-100'
                },
                confirm: {
                    label: "Comment",
                    className: "btn-success btn-fixed-width-100"}
            },
            callback: function (comment) {

                if (comment) {

                    BootboxAddComment(data[0].item.id);
                }
            }
        });
    });
}


function BootboxAddComment(id) {
 
    $.ajax({

        global: false,
        type: "GET",
        contentType: "application/json",
        url: "/api/item/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {

        var msg =   '<div class="row">' +
                    '<div class="col-sm-12" style="padding: 15px;">' +
                    '<span style="font-weight: bold;">' + data.itemValue + '</span>' +
                    '</div>' +
                    '<div class="col-sm-12">' +
                    '<form action="/api/itemcomments/save" method="post">' +
                    '<div class="row" style="margin-top: 10px;">' +
                    '<div class="col-sm-12">' +
                    '<div class="form-group">' +
                    '<input type="hidden" name="itemId" id="itemId" value="' + data.id + '" />' +
                    '<label for="commentValue">New comment:</label>' +
                    '<textarea class="form-control" rows="5" cols="50" id="commentValue" name="commentValue"></textarea>' +
                    '</div>' +
                    '</div>' +
                    '</div>' +
                    '</form>' +
                    '</div>' +
                    '</div>';
            
        var item = data.sysId;
        var itemId = data.id;

        bootbox.confirm({

            message: msg,
            title: "Add Comment to Item: [" + data.sysId + "]",
            buttons: {
                cancel: {
                    label: 'Cancel',
                    className: 'btn-danger btn-fixed-width-100'
                },
                confirm: {
                    label: "Save",
                    className: "btn-success btn-fixed-width-100"}
            },
            callback: function (comment) {

                if (comment) {

                    var data = {};
                    data['itemId'] = $("#itemId").val();
                    data['comment'] = $("#commentValue").val();

                    $.ajax({
                        
                        type: "POST",
                        contentType: "application/json",
                        url: "/api/comments/new",
                        data: JSON.stringify(data),
                        dataType: "json",
                        timeout: 60000,
                        success: function (data) {
                            
                            if (!$('#comment-icon-' + item).length > 0) {
                                    
                                var elem = '<li class="dropdown item-toolbar-li" id="comment-icon-' + item + '">' +
                                           '<a href="javascript:void(0);" onclick="javascript:BootboxItemComments(\'' + itemId + '\');">' +
                                           '<i class="fa fa-comments icon-comment"></i>' +
                                           '</a>' +
                                           '</li>';

                                if ($('#link-icon-' + item).length > 0) {

                                    $(elem).insertBefore($('#link-icon-' + item));

                                } else {

                                    $(elem).insertBefore($('#menu-icon-' + item));
                                }
                            }

                            BootboxDisplayComments(itemId);
                            showToastr('success', "Comment with ID: " + data + " created for: [" + item + "]");
                        }
                    });
                }
            }
        });
    });
}


function BootboxCreateLink(id, ident, projectId) {
    
    $.ajax({
        
        global: false,
        type: "GET",
        url: '/modal/link/link-create-form.html',
        success: function (data) { 
                       
            var box = bootbox.confirm({
                message: data,
                title: "Create new Link for Item: [" + ident + "]",
                buttons: {
                    cancel: {
                        label: "Cancel",
                        className: "btn-danger btn-fixed-width-100"
                    },
                    confirm: {
                        label: "Save",
                        className: "btn-success btn-fixed-width-100"
                    }
                },
                callback: function (result) {
                    
                    if (result) {
                        
                        var data = {};
        
                        data['srcArtifactId'] = $("#artifactId").val();
                        data['dstArtifactId'] = $("#dstArtifactId").val();
                        data['linkTypeId'] = $("#linkTypeId").val();
                        data['srcItemId'] = id;
                        data['dstItemId'] = $("#dstItemId").val();
                        
                        $.ajax({
                            
                            type: "POST",
                            contentType: "application/json",
                            url: "/api/link/new",
                            data: JSON.stringify(data),
                            dataType: "json",
                            timeout: 60000,
                            success: function (data) {
                                                              
                                var srcIdent = data.srcItem.id;
                                var srcIdent = data.srcItem.identifier;
                                var dstIdent = data.dstItem.identifier;
                                
                                if (!$('#link-icon-' + data.srcItem.sysId).length > 0) {
                                    
                                    var elem = '<li class="dropdown item-toolbar-li" id="link-icon-' + data.srcItem.sysId + '">' +
                                               '<a href="javascript:void(0);" onclick="javascript:BootboxDisplayLinks(\'' + data.srcItem.id + '\');">' +
                                               '<i class="fa fa-link icon-link"></i>' +
                                               '</a>' +
                                               '</li>';
                                       
                                    $(elem).insertBefore($('#menu-icon-' + data.srcItem.sysId));
                                }
   
                                BootboxDisplayLinks(id);
                                showToastr('success', "Link " + srcIdent + " <---> " + dstIdent + " created.");
                            }
                        });
                    }
                }
            });
            
            box.on("shown.bs.modal", function(e) { 
                
                loadLinkArtifacts(projectId);
                loadLinkTypes();
            
            });
            
            box.modal('show');
        }
    });
}


function loadLinkArtifacts(id) {
    
    $.ajax({
        
        url: '/api/link/artifacts/' + id,
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            // Populate dropdown control
            var select = document.getElementById('dstArtifactId');
            $(select).empty();
            
            $.each(data, function (key, index) {
                
                var opt = document.createElement('option');

                opt.value = index.id;
                opt.innerHTML = index.artifactName;
                select.appendChild(opt);
            });
        }
    });
}


function loadLinkTypes() {
    
    $.ajax({
        
        url: '/api/link/linktypes',
        type: "GET",
        dataType: "json",
        success: function (data) {

            // Populate dropdown control
            var select = document.getElementById('linkTypeId');
            var selectedId = 0;
            
            $(select).empty();
            
            $.each(data, function (key, index) {
                
                var opt = document.createElement('option');
                opt.value = index.id;
                opt.innerHTML = index.linkTypeName;
                select.appendChild(opt);
                
                if (index.defaultType === true) {
                    
                    selectedId = index.id;
                }
            });

            select.value = selectedId;
        }
    });
}


function onItemCreateLinkSrcDestChange() {
    
    $.ajax({
        
        url: '/api/artifact/requirements/' + document.getElementById('dstArtifactId').value,
        type: "GET",
        dataType: "json",
        success: function (data) {
       
            // Populate dropdown control
            var select = document.getElementById('dstItemId');
            $(select).empty();
            
            $.each(data, function (key, index) {
                
                var opt = document.createElement('option');

                opt.value = index.id;
                opt.innerHTML = index.identifier +  " - " + index.itemValue.substring(0, 60) + " ...";
                select.appendChild(opt);
            });
        }
    });
}


function BootboxDisplayLinks(id) {
    
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "/api/item/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {

        var id = data.id;
        var ident = data.identifier;
        var value = data.identifier + ": " + data.itemValue;

        $.ajax({
            
            global: false,
            type: "GET",
            url: '/modal/link/link-display-form.html',
            success: function (data) { 

                var box = bootbox.confirm({
                    message: data,
                    size: 'large',
                    title: "Links for Item: [" + ident + "]",
                    buttons: {
                        cancel: {
                            
                            label: "Close",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        confirm: {
                            
                            label: "Manage",
                            className: "btn-success btn-fixed-width-100"
                        }
                    },
                    callback: function (result) {
                    
                        if (result) {
                            
                            window.location.href = '/items/links/' +id;
                        }
                    }
                });

                box.on("shown.bs.modal", function(e) { 
                    
                    $.ajax({

                        type: "GET",
                        contentType: "application/json",
                        url: "/api/link/links/" + id,
                        dataType: "json",
                        cache: false
                    })
                    .done(function (data) {
                
                        var links = '';
                        var linkIdentClass = 'link-display-ident';
                        var linkValueClass = 'link-display-value';
                        var linkOldValueClass = 'link-display-value';
                        var linkNewValueClass = 'link-display-value-new';
                        var linkIconClass = '';
                        var linkIdent = '';
                        var linkValue = '';
                        var linkValuesElement = '';
                        
                        $.each(data, function (key, index) {
                           
                           if (index.srcItem.id === id) {

                                linkValue = index.dstItem.itemValue;
                                
                                if (index.dstItemChanged === true) {

                                    linkIdentClass = 'link-display-ident-changed';
                                    linkOldValueClass = 'link-display-value-changed';
                                    
                                    var oldValue = (index.dstHistoryValue !== null) ? index.dstHistoryValue.replace('/\n/g', '<br/>') : '';
                                    
                                    linkValuesElement =  '<div class="' + linkOldValueClass + '">' + oldValue + '</div>' +
                                                         '<div class="' + linkNewValueClass + '">' + linkValue.replace(/\n/g, '<br/>') + '</div>';
                                                 
                                } else {
                                    
                                    linkValueClass = 'link-display-value';
                                    linkValuesElement =  '<div class="' + linkValueClass + '">' + linkValue + '</div>';
                                }
                               
                               linkIconClass = '<i class="fa fa-fw fa-caret-square-o-right pull-right" style="color: #2ECC71;">';
                               linkIdent = index.dstArtifact.identifier + '<br/>' + index.dstItem.identifier;
                               
                           } else {
                               
                               linkIconClass = '<i class="fa fa-fw fa-caret-square-o-left pull-right" style="color: red;">';
                               linkIdent = index.srcArtifact.identifier + '<br/>' + index.srcItem.identifier;
                               linkValue = index.srcItem.itemValue;
                               
                               linkValuesElement =  '<div>' + linkValue + '</div>';
                           }
                            
                           links = links +  '<div class="row link-display-container">' +
                                            '<div class="col-sm-3 ' + linkIdentClass + '">' + linkIdent + '</div>' +
                                            '<div class="col-sm-8">' + linkValuesElement + '</div>' +
                                            '<div class="col-sm-1">' + linkIconClass + '</i></div>' +
                                            '</div>';
                        });
                        
                        $(e.currentTarget).find('#itemValue').text(value);
                        $(links).insertAfter('#itemLinks');
                        
                    });
                });

                box.modal('show');
            }
        });
    });
}


function ArtifactBasicMetadata(id) {
    
    $.ajax({
        
        url: '/api/artifact/' + id,
        type: "GET",
        dataType: "json",
        success: function (data) {

            var projectId = data.artifactProject.id;
            var projectName = data.artifactProject.projectName;
            var folderId = data.artifactFolder.id;
            var longName = data.artifactLongName;
            var name = data.artifactName;
            var identifier = data.identifier;
            var artifactTypeId = data.artifactType.id;
            var artifactDescription = data.artifactDescription;

            $.ajax({
                
                global: false,
                type: 'GET',
                url: '/modal/artifact/artifact-meta-form.html',
                success: function(data) {

                    var box = bootbox.dialog({
                        message: data,
                        title: "Document Basic Information",
                        buttons: {
                            cancel: {
                                label: "Cancel",
                                className: "btn-danger btn-fixed-width-100"
                            },
                            success: {
                                label: "Save",
                                className: "btn-success btn-fixed-width-100"
                            }
                        }
                    });

                    box.on("shown.bs.modal", function(e) {

                        $(e.currentTarget).find('input[name="artifactId"]').val(id);
                        $(e.currentTarget).find('input[name="projectId"]').val(projectId);
                        $(e.currentTarget).find('input[name="artifactProjectId"]').val(projectName);
                        $(e.currentTarget).find('input[name="identifier"]').val(identifier);
                        $(e.currentTarget).find('input[name="artifactLongName"]').val(longName);
                        $(e.currentTarget).find('input[name="artifactName"]').val(name);
                        $(e.currentTarget).find('textarea[name="artifactDescription"]').val(artifactDescription);

                        getArtifactTypes(artifactTypeId);
                        getFoldersByProject(projectId, folderId);
                    });

                    box.modal('show');
                }
            });
        }
    });
}


function ArtifactStatusLoad(selectedId) {
    
    $.ajax({
        
        url: '/api/artifacts/statuslist',
        type: "GET",
        dataType: "json",
        success: function (data) {

            // Populate dropdown control
            var select = document.getElementById('artifactStatusSelect');
            $(select).empty();
            
            $.each(data, function (key, index) {
                
                var opt = document.createElement('option');

                opt.value = index;
                opt.innerHTML = index;
                select.appendChild(opt);
            });

            select.value = selectedId;
        }
    });
}


function getArtifactTypes(selectedId) {

    var url = '/api/artifact/types';

    $.ajax({
        
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            // Populate dropdown control
            var select = document.getElementById('artifactTypeId');
            $(select).empty();
            
            $.each(data, function (key, index) {

                var opt = document.createElement('option');

                opt.value = index.id;
                opt.innerHTML = index.artifactTypeLongName;
                select.appendChild(opt);
            });

            select.value = selectedId;
        }
    });
}


function getFoldersByProject(projId, selectedId) {
    
    var url = '/api/projectfolders/' + projId;
    
    $.ajax({
        
        global: false,
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {
            
            // Populate dropdown control
            var select = document.getElementById('artifactFolderId');
            $(select).empty();
            
            $.each(data, function (key, index) {

                if (index.parent !== null) {      // Skip project root folder
                    
                    var opt = document.createElement('option');

                    opt.value = index.id;
                    opt.innerHTML = index.folderName;
                    select.appendChild(opt);
                }
            });

            select.value = selectedId;
        }
    });
}



function showToastr(msgType, message) {

    toastr.options = {
        "closeButton": false,
        "debug": false,
        "newestOnTop": false,
        "progressBar": false,
        "positionClass": "toast-bottom-left",
        "preventDuplicates": false,
        "onclick": null,
        "showDuration": "300",
        "hideDuration": "1000",
        "timeOut": "5000",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    toastr[msgType](message);
}


$(function () {

    $('.spinner .btn:first-of-type').on('click', function () {

        var btn = $(this);
        var input = btn.closest('.spinner').find('input');

        if (input.attr('max') === undefined || parseInt(input.val()) < input.attr('max')) {

            input.val(parseInt(input.val(), 10) + 1);

        } else {

            btn.next("disabled", true);
        }
    });

    $('.spinner .btn:last-of-type').on('click', function () {

        var btn = $(this);
        var input = btn.closest('.spinner').find('input');

        if (input.attr('min') === undefined || parseInt(input.val()) > input.attr('min')) {

            input.val(parseInt(input.val(), 10) - 1);

        } else {

            btn.prev("disabled", true);
        }
    });
});


function BootboxLinkDeleteConfirm(id) {
    
    $.ajax({
        
        global: false,
        url: '/api/link/' + id,
        type: "GET",
        dataType: "json",
        success: function (data) {
           
            var message  =  '<div style="margin-bottom: 10px; text-align:center; color: crimson;">Do you really want to delete the following link?</div>' +
                            '<div style="font-weight: bold; text-align:center;">[ ' + data.srcItem.identifier + ' ] ----> [ ' + data.dstItem.identifier + ' ]</div>';
            
            var itemSysId = data.dstItem.sysId;
            
            bootbox.confirm({
                
                title: 'Confirm Link Deletion',
                message: message,
                buttons: {
                    confirm: {
                        
                        label: 'Confirm',
                        className: 'btn-success btn-fixed-width-100'
                    },
                    cancel: {
                        
                        label: 'Cancel',
                        className: 'btn-danger btn-fixed-width-100'
                    }
                },
                callback: function(result) {
                    
                    if (result) {
                        
                        $.ajax({

                            type: "GET",
                            contentType: "application/json",
                            url: "/api/link/delete/" + id,
                            dataType: "json",
                            cache: false
                        })
                        .done(function () {
                            
                            $('#remote-link-' + itemSysId).remove();
                            showToastr('success', 'Link to item ' + itemSysId + ' deleted.');
                        });
                    }
                }
            });
        }
    });
    
}



function BootboxAdminImportItems() {
        
    bootbox.confirm({
                
        title: "Import Requirements",
        message: "Import new requirements?",
        size: 'small',
        buttons: {
            confirm: {

                label: 'Import',
                className: 'btn-success btn-fixed-width-100'
            },
            cancel: {

                label: 'Cancel',
                className: 'btn-danger btn-fixed-width-100'
            }
        },
        callback: function(result) {
              
            if (result) {

                $.ajax({

                    type: "GET",
                    contentType: "application/json",
                    url: "/api/requirements/update",
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {

                    showToastr('success', data + ' new requirements added.');
                });
            }
        }
    });
}


function BootboxAdminRefreshComments() {
        
    bootbox.confirm({
                
        title: "Confirm Comments Refresh",
        message: "Do you want to refresh all comments?",
        size: 'small',
        buttons: {
            confirm: {

                label: 'Refresh',
                className: 'btn-success btn-fixed-width-100'
            },
            cancel: {

                label: 'Cancel',
                className: 'btn-danger btn-fixed-width-100'
            }
        },
        callback: function(result) {

            if (result) {

                $.ajax({

                    type: "GET",
                    contentType: "application/json",
                    url: "/api/itemcomments/refresh",
                    dataType: "json",
                    cache: false
                })
                .done(function (data) {

                    showToastr('success', data + ' comments refreshed.');
                });
            }
        }
    });
}


function BootboxAdminModalTest() {
    
    modalBusy.open();
    
    setTimeout(function(){
        
        modalBusy.close();
        showToastr('success', 'Comments refreshed.');
    }, 
    5000);
    
}


function BootboxAlertSmall(title, msg) {
    
    bootbox.alert({
        
        title: '<span style="font-weight: bold; color: crimson;"><i class="fa fa-warning"></i> '+title+'</span>',
        message: msg,
        size: 'small',
        buttons: {
            ok: {
                
                label: 'OK',
                className: 'btn-primary btn-fixed-width-100'
            }
        }
    });
}


function FileUploadLoadding() {
    
    $('#artifactEmpty').addClass('hidden');
    $('#artifactLoading').removeClass('hidden');
}


function CreateUserTask() {
    
    var task =  '<form id="sendMessage" action="/messages/send" method="post">' +
                '<input type="hidden" name="artifactId" id="artifactId" value="" />' +
                '<div class="row">' +
                '<div class="form-group col-sm-4">' +
                '<label for="assignedTo">Assign to</label>' +
                '<select class="form-control" id="assignedTo" name="assignedTo"></select>' +
                '</div>' +
                '<div class="form-group col-sm-4">' +
                '<label for="taskUnits">Units of measure</label>' +
                '<select class="form-control" id="taskUnits" name="taskUnits"></select>' +
                '</div>' +
                '<div class="form-group col-sm-4">' +
                '<label for="initialSize">Task size</label>' +
                '<input type="text" class="form-control" id="initialSize" name="initialSize" placeholder="Size value" />' +
                '</div>' +
                '</div>' +
                '<div class="form-group">' +
                '<label for="taskName">Task name</label>' +
                '<input type="text" class="form-control file-loading" id="taskName" name="taskName" placeholder="Task name" />' +
                '</div>' +
                '<div class="form-group">' +
                '<label for="taskDetails">Task details</label>' +
                '<textarea class="form-control"  rows="5" cols="50" id="taskDetails" name="taskDetails"></textarea>' +
                '</div>' +
                '</form>';
        
    var box  =  bootbox.confirm({
        
                    title: "Create user task",
                    message: task,
                    buttons: {
                        confirm: {
                            label: 'Create',
                            className: 'btn-success btn-fixed-width-100'
                        },
                        cancel: {
                            label: 'Cancel',
                            className: 'btn-danger btn-fixed-width-100'
                        }
                    },
                    callback: function (result) {

                        if (result) {

                            var data = {};
        
                            data['assignedTo'] = $("#assignedTo").val();
                            data['taskUnits'] = $("#taskUnits").val();
                            data['initialSize'] = $("#initialSize").val();
                            data['taskName'] = $("#taskName").val();
                            data['taskDetails'] = $("#taskDetails").val();
                            
                            $.ajax({
                                
                                type: "POST",
                                contentType: "application/json",
                                url: "/api/tasks/new",
                                data: JSON.stringify(data),
                                dataType: "json",
                                timeout: 60000,
                                success: function (data) {
                                    
                                }
                            });
                        }
                    }
                });
                
    box.on("shown.bs.modal", function(e) {
        
        $.ajax({
            
            url: '/api/users/list',
            type: "GET",
            dataType: "json",
            success: function (data) {

                // Populate dropdown controls
                var selectUserOptions = '';
                var selectUnitsOptions  =   '<option value="COUNT">Count</option>' + 
                                            '<option value="HOURS">Hours</option>' + 
                                            '<option value="DAYS">Days</option>' + 
                                            '<option value="WEEKS">Weeks</option>' + 
                                            '<option value="MONTHS">Months</option>'; 
                var defaultUser = '';

                $.each(data, function (key, index) {

                    selectUserOptions = selectUserOptions + '<option value="' + index.uuId + '">' + index.firstName+ '</option>';
                    
                    if (index.firstName === 'Admin') {
                        
                        defaultUser = index.uuId;
                    }
                });

                $(e.currentTarget).find('select[id="assignedTo"]').append(selectUserOptions);
                $(e.currentTarget).find('select[id="taskUnits"]').append(selectUnitsOptions);
                
                $(e.currentTarget).find('select[name="assignedTo"]').prop('value', defaultUser);
                $(e.currentTarget).find('select[name="taskUnits"]').prop('value', "COUNT");
            }
        });
        
    });
    
    box.modal('show');
}

function SendMessage() {
    
    var message  =  '<form id="sendMessage" action="/messages/send" method="post">' +
                    '<input type="hidden" name="artifactId" id="artifactId" value="" />' +
                    '<div class="form-group">' +
                    '<label for="messageSubject">Subject</label>' +
                    '<input type="text" class="form-control file-loading" id="messageSubject" name="messageSubject" placeholder="Message Subject" />' +
                    '</div>' +
                    '<div class="form-group">' +
                    '<label for="messageBody">Message Details</label>' +
                    '<textarea class="form-control"  rows="5" cols="50" id="messageBody" name="messageBody"></textarea>' +
                    '</div>' +
                    '</form>';

    
    bootbox.confirm({
        
        title: "Send global message",
        message: message,
        buttons: {
            confirm: {
                label: 'Send',
                className: 'btn-success btn-fixed-width-100'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger btn-fixed-width-100'
            }
        },
        callback: function (result) {
            
            if (result) {
                
                var data = {};
                            
                data['subject'] = document.getElementById('messageSubject').value;
                data['message'] = document.getElementById('messageBody').value;

                $.ajax({
                                
                    type: "POST",
                    contentType: "application/json",
                    url: "/api/messages/message/new",
                    data: JSON.stringify(data),
                    dataType: "json",
                    timeout: 60000,
                    success: function () {

                        showToastr('success', 'Message sent!');
                    }
                });
            }
        }
    });
}


function SendNotification() {
    
    var message  =  '<form id="sendNotification" action="/notifications/send" method="post">' +
                    '<input type="hidden" name="artifactId" id="artifactId" value="" />' +
                    '<div class="form-group">' +
                    '<label for="message">Notification Details</label>' +
                    '<textarea class="form-control"  rows="5" cols="50" id="message" name="message"></textarea>' +
                    '</div>' +
                    '</form>';
            
    bootbox.confirm({
        
        title: "Send global notification",
        message: message,
        buttons: {
            confirm: {
                label: 'Send',
                className: 'btn-success btn-fixed-width-100'
            },
            cancel: {
                label: 'Cancel',
                className: 'btn-danger btn-fixed-width-100'
            }
        },
        callback: function (result) {
            
            if (result) {
                
                var data = {};
                            
                data['name'] = "General Alert";
                data['priority'] = "NORMAL";
                data['message'] = document.getElementById('message').value;
                
                $.ajax({
                                
                    type: "POST",
                    contentType: "application/json",
                    url: "/api/messages/notification/new",
                    data: JSON.stringify(data),
                    dataType: "json",
                    timeout: 60000,
                    success: function () {

                        showToastr('success', 'Message sent!');
                    }
                });
            }
        }
    });
}


function timeSince(date) {

    var seconds = Math.floor((new Date() - date) / 1000);

    var interval = Math.floor(seconds / 31536000);
    if (interval > 1) {
        
        return interval + " years ago";
    }
    
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
        
        return interval + " months ago";
    }
    
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
        
        return interval + " days ago";
    }
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
        
        return interval + " hours ago";
    }
    
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
        
        return interval + " minutes ago";
    }
    
    return Math.floor(seconds) + " seconds ago";
}