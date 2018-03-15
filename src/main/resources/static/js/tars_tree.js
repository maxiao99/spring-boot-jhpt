/*
 * Tencent is pleased to support the open source community by making Tars available.
 *
 * Copyright (C) 2016 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

var tarsTree = {};
tarsTree.activeTab = 0;
var TAB_INDEX = 'tab_seed_';
var CONTAINER_INDEX = 'tab_container_';
var AHREF_INDEX = 'tab_ahref_';
var IFRAME_INDEX = 'tab_iframe_';

var htmlMap = {
    "device_info/device_info": "",
    "pay/customer_list": "",
    "pay/record_list": "",
    "device_info/to_activate_list": "",
    "device_info/all_list": "",
    "device_info/config_list": "",
    "device_info/msg_list": "",
    "device_info/real_time": "",
    "user/user_list": "user_manage.html",
}

$(function () {
    $.ajax({
        type: 'POST',
        url: '/user/get_menu',
        dateType: 'json',
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify({
            userName: "admin",
            userPassword: "admin",
        })
    }).done(function (data) {
        if (data.status == 0) {
            var treeId = location.hash.split('treeId=');

            data.data[0].open = true;
            if (treeId.length && treeId[0]) {
                showTree(data.data, $('#tree-container'), 'radio', location.hash.split('treeId=')[1]);
            } else {
                showTree(data.data, $('#tree-container'), 'radio');
                var treeObj = $.fn.zTree.getZTreeObj("filetree");

                var nodes = treeObj.getNodes();
                if (nodes.length > 0) {
                    treeObj.selectNode(nodes[1]);
                }
            }
        } else {
            $('#tree-container').html('<div class="alert alert-danger" role="alert"><strong>业务树加载出错!</strong> ' + data.err_msg + '</div>');
        }
    });
});


tarsTree.zTreeClick = function (e, treeId, treeNode) {
    if (treeNode.url == 'undefined' || treeNode.url == '') {
        return;
    }
    var url = htmlMap[treeNode.url];
    if (url == 'undefined' || url == '') {
        return;
    }
    var tabs = $('#myTab').find('li');
    var currentIndex = tabs.size();

    var open_tab_id = TAB_INDEX + currentIndex;
    var next_ahref_id = AHREF_INDEX + currentIndex;

    var li_tab = '<li role="presentation" id="' + open_tab_id + '"><a id="' + next_ahref_id + '" href="#tab_container_' + currentIndex + '" role="tab" data-active="' + currentIndex + '" data-toggle="tab">' + treeNode.text;
    $(li_tab + '<i class="glyphicon glyphicon-remove small" tabclose="' + open_tab_id + '" style="position: absolute;right:4px;top: 4px;" onclick="closeTab(this)"></i></a></li>').appendTo('#myTab');

    $('#' + open_tab_id).prev().removeClass('active');
    $("#" + open_tab_id).addClass("active");

    $('#myTab a').click(function (e) {
        e.preventDefault();
        tarsTree.activeTab = $(this).attr('data-active');
        if (tarsTree.treeId) {
            $(this).tab('show');
            $activeTab = $('.tab-content').find('.active');
            $activeTab.find('.frame').attr('src', $activeTab.attr('data-url'));
        }
        location.hash = '#tabUrl=' + $activeTab.attr('data-url');
    });

    var open_iframe_id = IFRAME_INDEX + currentIndex;
    var height = $('#home-frame').attr('style');
    var open_container_id = CONTAINER_INDEX + currentIndex;

    var tabpanel = '<div role="tabpanel" class="tab-pane" id="' + open_container_id + '" data-url="' + url + '">' +
        '<iframe id="' + open_iframe_id + '" class="frame" width="100%" frameborder="0" style="' + height + '"></iframe></div>';
    $(tabpanel).appendTo('#myTabContent');

    $('#' + open_container_id).prev().removeClass('active');
    $("#" + open_container_id).addClass("active");

    $('#' + next_ahref_id).tab('show');
    location.hash = 'tabUrl=' + url;   //url

    $('#' + open_iframe_id).attr('src', '/' + url);
};

function closeTab(item) {
    var val = $(item).attr('tabclose');
    var closeTab = $("#" + val);
    // 前一个 tab active
    var prevTab = closeTab.prev();
    prevTab.addClass("active");

    // tab 移除
    closeTab.remove();

    var containerId = CONTAINER_INDEX + val.substring(9);
    var closeContainer = $("#" + containerId);
    // 前一个 container active
    var prevContainer = closeContainer.prev();
    prevContainer.addClass("active");

    // li 移除
    closeContainer.remove();

    var preva = prevTab.find('a');
    tarsTree.activeTab = preva.attr('data-active');
    preva.tab('show');
    $activeTab = $('.tab-content').find('.active');
    $activeTab.find('.frame').attr('src', $activeTab.attr('data-url'));
    location.hash = '#tabUrl=' + $activeTab.attr('data-url');
}

tarsTree.setting = {
    data: {
        key: {
            name: "text",
            url: "url1",
        },
        simpleData: {
            enable: false,
            idKey: "id",
            pIdKey: "pid",
            rootPId: '0'
        }
    },
    async: {
        enable: false
    },
    check: {
        //enable: true
        //chkStyle: "radio"
    },
    view: {
        selectedMulti: false,
        dblClickExpand: false,
        showIcon: false
    },
    callback: {
        onClick: tarsTree.zTreeClick
    }
};

/** 弹出框的点击事件
 *  @param data {Array} 业务树数据
 *  @param treeContainer {Element} 业务树容器
 *  @param checkType {String} 选择类型
 *  @param selectedData {Array} 已选数据
 *  @param callback {Function} 回调函数
 */
function showTree(data, treeContainer, checkType, selectedData, callback) {
    var $treeObj;
    tarsTree.setting.check.enable = (checkType == 'check');
    tarsTree.treeContainer = treeContainer;
    tarsTree.checkType = checkType;
    tarsTree.callback = callback || function () {
    };
    $treeObj = treeContainer.find("#filetree");
    if (data.length) {
        $.fn.zTree.init($treeObj, tarsTree.setting, data);
        tarsTree.zTreeObj = $.fn.zTree.getZTreeObj('filetree');
        if (selectedData) {
            if (checkType != 'check') {
                var node = tarsTree.zTreeObj.getNodeByParam('id', selectedData, null);
                tarsTree.zTreeObj.selectNode(node);
            } else {
                var dataArr = selectedData.split(','),
                    checkedNodes = [];
                $.each(dataArr, function (index, n) {
                    checkedNodes.push(tarsTree.zTreeObj.getNodeByParam('id', n, null));
                });
                $.each(checkedNodes, function (index, n) {
                    tarsTree.zTreeObj.checkNode(n, true, false);
                    tarsTree.zTreeObj.selectNode(n);
                });
            }
        }
    }
}

