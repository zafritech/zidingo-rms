/* global toastr, bootbox */

// Retrieve projects list
$(document).ready(function () {

    var list = '';
    var url = '/api/projects';

    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        success: function (data) {

            $.each(data, function (key, index) {

                list = list
                        + '<li style="border-bottom: 1px solid #c0c0c0;">'
                        + '<a href="javascript:void(0);" onclick="return loadProjectTree(' + index.id + ');">'
                        + index.projectShortName
                        + '<i class="fa fa-fw fa-table pull-right" style="margin: 4px 0;"></i>'
                        + '</a>'
                        + '</li>';
            });

            $("#projectsList").append(list);

            if (localStorage.getItem('currentProjectId')) {

                projectId = localStorage.getItem('currentProjectId');
                loadProjectTree(projectId);
            }

        },
        error: function () {
            
            checkLogin();
            $("#projectsList").append('<br/>No projects to list.');
        }
    });
});


//Retrieve project specification tree
function loadProjectTree(id) {

    localStorage.setItem('currentProjectId', id);

    var url = '/api/folders/' + id;
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
        
        url: url,
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
                
                $('#projectTree').show();
                $('#more-less-modules').addClass('fa-minus-square').removeClass('fa-plus-square');

                zTreeObj = $.fn.zTree.init($("#projectTree"), setting, data);

                if (localStorage.getItem('currentFolderId')) {

                    var treeObj = $.fn.zTree.getZTreeObj("projectTree");
                    var currFolderId = localStorage.getItem('currentFolderId');

                    currNode = treeObj.getNodeByParam("id", currFolderId, null);
                    treeObj.expandNode(currNode, true, false, true);
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


function checkLogin() {
    
    $.ajax({
        
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
    var projectId = localStorage.getItem('currentProjectId');

    if (treeNode.isParent) {

        if (treeNode.pId === 0) {

            window.location.replace('/projects/' + treeNode.linkId);
            
        } else {
            
            window.location.replace('/projects/' + projectId + '/' + treeNode.id);
        }

        localStorage.setItem('currentFolderId', treeNode.id);
        treeObj.expandNode(treeNode, true, false, true);

    } else {

        if (treeNode.linkId !== null) {

            window.location.replace('/artifacts/' + treeNode.linkId);
        }
    }
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

// Manage user roles script
(function () {

    $('#btnRoleToRight').click(function (e) {

        var selectedOpts = $('#availableRolesList option:selected');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $('#userRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
    });

    $('#btnRolesAllToRight').click(function (e) {
        var selectedOpts = $('#availableRolesList option');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $('#userRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
    });

    $('#btnRoleToLeft').click(function (e) {
        var selectedOpts = $('#userRolesList option:selected');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $('#availableRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
    });

    $('#btnRolesAllToLeft').click(function (e) {
        var selectedOpts = $('#userRolesList option');
        if (selectedOpts.length === 0) {
            alert("Nothing to move.");
            e.preventDefault();
        }

        $('#availableRolesList').append($(selectedOpts).clone());
        $(selectedOpts).remove();
        e.preventDefault();
    });
}(jQuery));


function itemCreateClassChange() {

    var itemClass = document.getElementById('createItemClass');

    if (itemClass.value === "REQUIREMENT") {

        document.getElementById('createIdentTemplate').disabled = false;
        document.getElementById('createItemType').disabled = false;
        document.getElementById('createIdentifier').disabled = false;

        $('#createRequirementRow').show();
        $('#createIdentifierContainer').show();

        $.ajax({
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

        type: "GET",
        contentType: "application/json",
        url: "/api/item/edit/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        itemDao = data;

        $.ajax({
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
                            
                            var data = {};

                            data['id'] = document.getElementById('editId').value;
                            data['artifactId'] = document.getElementById('artifactId').value;
                            data['sysId'] = document.getElementById('editSysId').value;
                            data['itemClass'] = document.getElementById('editItemClass').value;
                            data['itemType'] = document.getElementById('editItemType').value;
                            data['identifier'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('editIdentifier').value : '';
                            data['itemLevel'] = document.getElementById('editItemLevel').value;
                            data['itemValue'] = document.getElementById('editItemValue').value;

                            $.ajax({

                                type: "POST",
                                contentType: "application/json",
                                url: "/api/items/save",
                                data: JSON.stringify(data),
                                dataType: "json",
                                timeout: 60000,
                                success: function (data) {

                                    if (data.itemClass === "HEADER") {

                                        document.getElementById(data.sysId).className = 'row item-header-' + data.itemLevel;

                                    } else if (data.itemClass === "REQUIREMENT") {

                                        var reqElementId = document.getElementById('ident-' + data.sysId);

                                        // Not a requirement yet (converted from prose)
                                        if (!document.getElementById(reqElementId)) {

                                            var newItemElement = '<div id="' + data.sysId + '" class="row item-level-' + data.itemLevel + '">' +
                                                    '<div id="value-container-' + data.sysId + '" class="col-xs-9 requirement-text">' +
                                                    '<span id="value-' + data.sysId + '">' + data.itemValue + '</span>' +
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
                                                '<span id="value-' + data.sysId + '">' + data.itemValue + '</span>' +
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
                        console.log(indentTemplValue);

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
                        
                        url: '/api/link/incominglinks/' + itemDao.item.id,
                        type: "GET",
                        dataType: "json",
                        success: function (data) {

                            console.log(data);

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

        type: "GET",
        contentType: "application/json",
        url: url,
        dataType: "json",
        cache: false
    })
    .done(function (data) { 
        
        console.log(data);

        var refItem = data;
        
        $.ajax({
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

                            console.log(data);
                        
                            $.ajax({
                                
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



function BootboxCreateItem(id, pos) {
 
    var url = "/api/item/create/" + id; 
    var newSortIndex = 0;
    
    $.ajax({

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

                            console.log(data);
                            
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

        type: "GET",
        contentType: "application/json",
        url: "/api/item/" + id,
        dataType: "json",
        cache: false
    })
    .done(function (data) {
        
        var itemToDelete = data;

        $.ajax({
            type: "GET",
            url: '/modal/item/item-delete-form.html',
            success: function (data) {

                console.log(itemToDelete);
                
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
                                type: "GET",
                                contentType: "application/json",
                                url: "/api/item/delete/" + id,
                                timeout: 60000,
                                success: function (data) {
                                    
                                    $('#' + itemToDelete.sysId).remove();
                                    showToastr('success', 'Item '+ itemToDelete.sysId +' deleted!');
                                }
                            });
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
                        
                        console.log(data);
                
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

                console.log(data);

                $.ajax({
                                
                    type: "POST",
                    contentType: "application/json",
                    url: "/api/messages/message/new",
                    data: JSON.stringify(data),
                    dataType: "json",
                    timeout: 60000,
                    success: function (data) {

                        showToastr('success', 'Message sent!');

                    }
                });
            }
        }
    });
}


function SendNotification() {
    
    bootbox.alert("Send global notification.");
}