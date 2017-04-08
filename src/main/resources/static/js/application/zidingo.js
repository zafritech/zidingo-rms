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
                        + '<li>'
                        + '<a href="#" onclick="return loadProjectTree(' + index.id + ');">'
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

            $('#projectTree').empty();
            $("#projectTree").append('<br/>Error!');
        }
    });
}
;


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


$(document).ready(function () {

    $("#projectsList").on("hide.bs.collapse", function () {

        $("#more-less").toggleClass('fa-minus-square fa-plus-square');
    });

    $("#projectModules").on("show.bs.collapse", function () {

        $("#more-less-modules").toggleClass('fa-plus-square fa-minus-square');
    });
});


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


// Edit Item
$(document).ready(function () {

    $('#editItemModal').on('show.bs.modal', function (e) {
        var itemId = $(e.relatedTarget).data('item-id');
        $.ajax({
            url: '/api/item/' + itemId,
            type: "GET",
            dataType: "json",
            success: function (data) {

                removeOptions(document.getElementById('editItemClass'));
                removeOptions(document.getElementById('editItemLevel'));
                removeOptions(document.getElementById('editItemType'));
                removeOptions(document.getElementById('editIdentTemplate'));

                getItemClasses(data.itemClass, 'edit');
                getItemLevels(data.itemLevel, 'edit');
                getItemTypes(data.itemType.itemTypeName, 'edit');
                getIdentifierTemplates(data.artifact.id, 'edit');
                getMediaTypes(data.mediaType, 'edit');

                $(e.currentTarget).find('input[name="artifactId"]').val(data.artifact.id);
                $(e.currentTarget).find('input[name="editId"]').val(data.id);
                $(e.currentTarget).find('input[name="editSysId"]').val(data.sysId);
                $(e.currentTarget).find('input[name="editItemType"]').val(data.itemType.itemTypeName);
                $(e.currentTarget).find('input[name="editIdentifier"]').val(data.identifier);
                $(e.currentTarget).find('textarea[name="editItemValue"]').val(data.itemValue);
                $(e.currentTarget).find('input[name="editSortIndex"]').val(data.sortIndex);
                
                if (isEmpty(data.identifier)) {

                    document.getElementById('editItemType').disabled = true;
                    document.getElementById('editIdentifier').disabled = true;

                } else {

                    document.getElementById('editItemType').disabled = false;
                    document.getElementById('editIdentifier').disabled = false;
                }

                if (data.itemClass !== "REQUIREMENT") {

                    $('#editRequirementRow').hide();
                    $('#editIdentField').hide();

                } else {

                    $('#editRequirementRow').show();
                    $('#editIdentField').show();
                }

                $.ajax({
                    url: '/api/link/incominglinks/' + itemId,
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
            },
            error: function (data) {
                var selectItemClass = document.getElementById('editItemClass');
                var selectItemLevel = document.getElementById('editItemLevel');

                $(selectItemClass).empty();
                $(selectItemLevel).empty();

                getItemClasses(data.itemClass, 'edit');
                getItemLevels(data.itemLevel, 'edit');
                document.getElementById('editSysId').disabled = false;
            }
        });
    });


    $('#createItemModal').on('show.bs.modal', function (e) {

        var artifictId = document.getElementById('artifactId').value;
        var refItemId = $(e.relatedTarget).data('ref-item-id');
        var refAboveIdx = $(e.relatedTarget).data('ref-item-idx');
        var refBelowIdx = parseInt(refAboveIdx) + 1;
        var refItemSysId = $(e.relatedTarget).data('ref-item-sys-id');
        var refPosition = $(e.relatedTarget).data('ref-pos');

        removeOptions(document.getElementById('createItemClass'));
        removeOptions(document.getElementById('createItemLevel'));
        removeOptions(document.getElementById('createMediaType'));
        removeOptions(document.getElementById('createItemType'));
        removeOptions(document.getElementById('createIdentTemplate'));

        getItemClasses('PROSE', 'create');
        getItemLevels(1, 'create');
        getItemTypes('Functional', 'create');
        getIdentifierTemplates(artifictId, 'create');
        getMediaTypes('TEXT', 'create');

        $(e.currentTarget).find('input[name="artifactId"]').val(artifictId);
        $(e.currentTarget).find('input[name="createId"]').val(refItemId);

        if (refPosition === 'ABOVE') {

            $(e.currentTarget).find('input[name="createSortIndex"]').val(refAboveIdx);
            $(e.currentTarget).find('input[name="createInsertBefore"]').val(refItemSysId);

        } else if (refPosition === 'BELOW') {

            $(e.currentTarget).find('input[name="createSortIndex"]').val(refBelowIdx);
            $(e.currentTarget).find('input[name="createInsertAfter"]').val(refItemSysId);
        }

        document.getElementById('createIdentTemplate').disabled = true;
        document.getElementById('createItemType').disabled = true;
        document.getElementById('createIdentifier').disabled = true;

        $('#createRequirementRow').hide();
        $('#createIdentifierContainer').hide();
    });


    // Save Edited Item
    $('#createItemSaveBtn').click(function () {

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/items/nextsystemid/" + document.getElementById('artifactId').value,
            dataType: "text",
            timeout: 60000,
            success: function (responseText) {

                var data = {};

                data['artifactId'] = document.getElementById('artifactId').value;
                data['sysId'] = responseText;
                data['itemClass'] = document.getElementById('createItemClass').value;
                data['identifier'] = (data['itemClass'] === "REQUIREMENT") ? document.getElementById('createIdentifier').value : '';
                data['itemLevel'] = document.getElementById('createItemLevel').value;
                data['itemType'] = document.getElementById('createItemType').value;
                data['mediaType'] = document.getElementById('createMediaType').value;
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

                        var refElementId = '';
                        var newItemElement = '';

                        var refItemId = document.getElementById('createId').value;
                        var refSortIdx = document.getElementById('createSortIndex').value;

                        if (!isEmpty(document.getElementById('createInsertBefore').value)) {

                            refElementId = document.getElementById('createInsertBefore').value;

                        } else if (!isEmpty(document.getElementById('createInsertAfter').value)) {

                            refElementId = document.getElementById('createInsertAfter').value;
                        } else {

                            location.reload();
                            return true;
                        }

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

                        if (!isEmpty(document.getElementById('createInsertBefore').value)) {

                            $(newItemElement).insertBefore('#' + refElementId);

                        } else if (!isEmpty(document.getElementById('createInsertAfter').value)) {

                            $(newItemElement).insertAfter('#' + refElementId);
                        }

                        $(data.sysId).show();

                        $('#createItemModal').modal('hide');
                        showToastr('success', 'Item successfully created!');
                    },
                    error: function (e) {

                        $('#createItemModal').modal('hide');
                        showToastr('error', 'There was an error creating an item!');
                    }
                });
            },
            error: function () {

                showToastr('error', 'There was an error creating an item!');
            }
        });
    });


    // Save Edited Item
    $('#editItemSaveBtn').click(function () {

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

                $('#editItemModal').modal('hide');
                showToastr('success', '[' + data.sysId + '] modfied!');
            },
            error: function () {

                $('#editItemModal').modal('hide');
                showToastr('error', 'An error occured updating item [' + data.sysId + ']!');
            }
        });
    });


    $('#manageIdentFormatsModal').on('show.bs.modal', function () {

        var id = document.getElementById('artifactId').value;

        $.ajax({
            url: '/api/items/identifiers/' + id,
            type: "GET",
            dataType: "json",
            success: function (data) {

                var container = document.getElementById('format-container');
                var formats = '';

                $.each(data, function (key, index) {

                    formats = formats + '<div class="row">' +
                            '<div class="col-sm-10">' +
                            '<span>' + index.variableValue + '</span>' +
                            '<input type="hidden" name="formats[]" value="' + index.variableValue + '" />' +
                            '</div>' +
                            '<div class="col-sm-2">' +
                            '<button type="button" class="close pull-right">' +
                            '<span style="color:red;" aria-hidden="true">×</span>' +
                            '<span class="sr-only">Close</span>' +
                            '</button>' +
                            '</div>' +
                            '</div>';
                });

                container.innerHTML = formats;
            },
            error: function () {

            }
        });

    });


    function getItemTypes(selectedOption, action) {

        var url = '/api/items/itemtypes';

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (data) {

                var select = document.getElementById(action + 'ItemType');
                $.each(data, function (key, index) {
                    var opt = document.createElement('option');
                    opt.value = index.itemTypeName;
                    opt.innerHTML = index.itemTypeLongName;
                    select.appendChild(opt);
                });

                select.value = selectedOption;
            }
        });
    }


    function getItemClasses(selectedValue, action) {
        
        var url = '/api/items/classes';
        
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (data) {

                // Populate options
                var select = document.getElementById(action + 'ItemClass');

                $.each(data, function (key, index) {

                    var opt = document.createElement('option');

                    opt.value = index;
                    opt.innerHTML = index;
                    select.appendChild(opt);
                });

                select.value = selectedValue;
            }
        });
    }


    function getIdentifierTemplates(id, action) {

        var url = '/api/items/identifiers/' + id;

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (data) {

                // Populate options
                var select = document.getElementById(action + 'IdentTemplate');
                var input = document.getElementById(action + 'Identifier');

                $.each(data, function (key, index) {

                    var opt = document.createElement('option');

                    opt.value = index.variableValue;
                    opt.innerHTML = index.variableValue;
                    select.appendChild(opt);
                });

                if (!isEmpty(input.value)) {

                    select.value = input.value.substr(0, input.value.lastIndexOf("-"));
                }
            }
        });
    }


    function getMediaTypes(selectedOption, action) {

        var url = '/api/items/mediatypes';

        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: function (data) {

                // Populate dropdown control
                var select = document.getElementById(action + 'MediaType');

                $.each(data, function (key, index) {

                    var opt = document.createElement('option');

                    opt.value = index;
                    opt.innerHTML = index;
                    select.appendChild(opt);
                });

                select.value = selectedOption;
            }
        });
    }


    function removeOptions(selectbox)
    {
        var i;
        for (i = selectbox.options.length - 1; i >= 0; i--)
        {
            selectbox.remove(i);
        }
    }

    function getItemLevels(itemLevel, action) {

        var select = document.getElementById(action + 'ItemLevel');
        var levels = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];

        $(select).empty();

        $.each(levels, function (i, opt) {
            $(select).append($('<option></option>').val(opt).html('Level ' + opt));
        });

        $(select).val(itemLevel);
    }

    // Show loading spinner on import popup closing
    $('#importExcelModal').on('hidden.bs.modal', function () {

        document.getElementById('artifactEmpty').className = 'row hidden';
        document.getElementById('artifactLoading').className = 'row';
    });

    // Reload page on popup closing
    $('#createItemModal').on('hidden.bs.modal', function () {
//		location.reload();
    });

});


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


function itemEditClassChange() {

    var itemClass = document.getElementById('editItemClass');
    if (itemClass.value === "REQUIREMENT") {

        document.getElementById('editIdentifier').disabled = false;
        document.getElementById('editItemType').disabled = false;
        $('#editRequirementRow').show();
        $('#editIdentField').show();

    } else {

        document.getElementById('editIdentifier').disabled = true;
        document.getElementById('editItemType').disabled = true;
        $('#editRequirementRow').hide();
        $('#editIdentField').hide();
    }
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

                var box = bootbox.dialog({
                    message: data,
                    size: 'large',
                    title: "Links for Item: [" + ident + "]",
                    buttons: {
                        cancel: {
                            
                            label: "Close",
                            className: "btn-danger btn-fixed-width-100"
                        },
                        success: {
                            
                            label: "Print",
                            className: "btn-success btn-fixed-width-100"
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
                        var linkClass = '';
                        var linkIdent = '';
                        var linkValue = '';
                        
                        $.each(data, function (key, index) {
                           
                           if (index.srcItem.id === id) {
                               
                               linkClass = '<i class="fa fa-fw fa-caret-square-o-right pull-right" style="color: #2ECC71;">';
                               linkIdent = index.dstArtifact.identifier + '<br/>' + index.dstItem.identifier;
                               linkValue = index.dstItem.itemValue;
                               
                           } else {
                               
                               linkClass = '<i class="fa fa-fw fa-caret-square-o-left pull-right" style="color: red;">';
                               linkIdent = index.srcArtifact.identifier + '<br/>' + index.srcItem.identifier;
                               linkValue = index.srcItem.itemValue;
                           }
                            
                           links = links +  '<div class="row" style="margin-top: 5px; border-top: 1px solid #E3E3E3; padding: 4px;">' +
                                            '<div class="col-sm-3" style="border-right: 1px solid #E3E3E3;">' + linkIdent + '</div>' +
                                            '<div class="col-sm-8">' + linkValue + '</div>' +
                                            '<div class="col-sm-1">' + linkClass + '</i></div>' +
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