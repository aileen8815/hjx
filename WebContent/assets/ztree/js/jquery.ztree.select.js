;
(function ($) {
    // 插件的定义
    $.fn.ztreeSelect = function (options) {
        debug(this);

        var opts = $.extend({}, $.fn.ztreeSelect.defaults, options);

        return this.each(function () {
            $this = $(this);
            var o = $.meta ? $.extend({}, opts, $this.data()) : opts;

            var zm = new ztreeMenu(o, $this);

            $this.click(function () {
                zm.showMenu();
                return false;
            });

            $this.css('background', '#fff');
        });
    };

    function debug($obj) {
        if (window.console && window.console.log)
            ;//window.console.log('ztreeSelect selection count: ' + $obj.size());
    };

    $.fn.ztreeSelect.defaults = {
        ztreeMenuContainer: 'ztreeMenuContainer',
        width: '240px',
        height: '360px',
        hiddenInput: '',
        url: '',
        zNodes: [
            {id: 1, pId: 0, name: "北京"},
        ]
    };

    function ztreeMenu(settings, obj){
        this.settings = settings;
        this.obj = obj;
        var that = this;

        // --- render ztree ---
        var ztreeSetting = {
            async: {
                enable: true,
                url: this.settings.url,
                autoParam: ["id"]
            },
            view: {
                dblClickExpand: false
            },
            callback: {
                beforeClick: function (treeId, treeNode) {
                    var check = (treeNode && !treeNode.isParent);
                    if (!check) alert("只能选择叶子节点...");
                    return check;
                },
                onClick: function(e, treeId, treeNode){
                    var zTree = $.fn.zTree.getZTreeObj(treeId),
                        nodes = zTree.getSelectedNodes(),
                        v = "",
                        d = "";
                    nodes.sort(function compare(a, b) {
                        return a.id - b.id;
                    });
                    for (var i = 0, l = nodes.length; i < l; i++) {
                        v += nodes[i].name + ",";
                        d += nodes[i].id + ",";
                    }
                    if (v.length > 0) v = v.substring(0, v.length - 1);
                    if (d.length > 0) d = d.substring(0, d.length - 1);

                    that.obj.attr("value", v);
                    $(that.settings.hiddenInput).val(d);
                    $("#ztree_wrap_" + that.obj.attr("id")).fadeOut("fast");
                }
            }
        };

        var ztreeHtml = '<div id="ztree_wrap_' + that.obj.attr("id") + '" class="ztreeMenuContainer">';
        ztreeHtml += '<ul id="ztree_ul_' + that.obj.attr("id") + '" class="ztree"></ul></div>';
        $('body').append(ztreeHtml);

        $ztreeMenu = $("#ztree_ul_" + that.obj.attr("id"));
        $ztreeMenu.width(that.settings.width).height(that.settings.height);

        $.fn.zTree.init($ztreeMenu, ztreeSetting);
        // --- ztree end ---

        return this;
    }

    ztreeMenu.prototype.showMenu = function() {
        var offset = this.obj.offset();
        $("#ztree_wrap_" + this.obj.attr("id")).css({left: offset.left + "px", top: offset.top + this.obj.outerHeight() + "px"}).slideDown("fast");

        var that = this;
        $("body").bind("mousedown", function(event){
            if (!(event.target.id == "#ztree_wrap_" + that.obj.attr("id") || $(event.target).parents("#ztree_wrap_" + that.obj.attr("id")).length > 0)) {
                $("#ztree_wrap_" + that.obj.attr("id")).fadeOut("fast");
            }
        });
    };
})
(jQuery);