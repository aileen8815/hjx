/*---LEFT BAR ACCORDION----*/
$(function () {
    $('#nav-accordion').dcAccordion({
        eventType: 'click',
        autoClose: true,
        saveState: true,
        disableLink: true,
        speed: 'slow',
        showCount: false,
        autoExpand: true,
//        cookie: 'dcjq-accordion-1',
        classExpand: 'dcjq-current-parent'
    });
});

var Script = function () {

//    sidebar dropdown menu auto scrolling

    jQuery('#sidebar .sub-menu > a').click(function () {
        var o = ($(this).offset());
        diff = 250 - o.top;
        if (diff > 0)
            $("#sidebar").scrollTo("-=" + Math.abs(diff), 500);
        else
            $("#sidebar").scrollTo("+=" + Math.abs(diff), 500);
    });

//    sidebar toggle

    $(function () {
        function responsiveView() {
            var screenLimit = 1300;
            var wSize = $(window).width();
            if (wSize <= screenLimit) {
                $('#container').addClass('sidebar-close');
                $('#sidebar > ul').hide();
                $('#main-content').css({
                    'margin-left': '0px'
                });
                $('#sidebar').css({
                    'margin-left': '-240px'
                });
            } else {
                $('#container').removeClass('sidebar-close');
                $('#sidebar > ul').show();
                $('#main-content').css({
                    'margin-left': '240px'
                });
                $('#sidebar').css({
                    'margin-left': '0'
                });
            }

            $('.easyui-datagrid').datagrid('resize');
            $('.easyui-treegrid').treegrid('resize');
        }

        $(window).on('load', responsiveView);
        $(window).on('resize', responsiveView);
    });

    $('.fa-bars').click(function () {
        if ($('#sidebar > ul').is(":visible") === true) {
            $('#main-content').css({
                'margin-left': '0px'
            });
            $('#sidebar').css({
                'margin-left': '-240px'
            });
            $('#sidebar > ul').hide();
            $("#container").addClass("sidebar-closed");
        } else {
            $('#main-content').css({
                'margin-left': '240px'
            });
            $('#sidebar').css({
                'margin-left': '0'
            });
            $('#sidebar > ul').show();
            $("#container").removeClass("sidebar-closed");
        }
        $('.easyui-datagrid').datagrid('resize');
        $('.easyui-treegrid').treegrid('resize');
    });

// custom scrollbar
    $("#sidebar").niceScroll({styler: "fb", cursorcolor: "#777", cursorwidth: '3', cursorborderradius: '10px', background: '#fff', spacebarenabled: false, cursorborder: ''});

    $("html").niceScroll({styler: "fb", cursorcolor: "#777", cursorwidth: '6', cursorborderradius: '10px', background: '#fff', spacebarenabled: false, cursorborder: '', zindex: '1000'});

// widget tools

    jQuery('.panel .tools .fa-chevron-down').click(function () {
        var el = jQuery(this).parents(".panel").children(".panel-body");
        if (jQuery(this).hasClass("fa-chevron-down")) {
            jQuery(this).removeClass("fa-chevron-down").addClass("fa-chevron-up");
            el.slideUp(200);
        } else {
            jQuery(this).removeClass("fa-chevron-up").addClass("fa-chevron-down");
            el.slideDown(200);
        }
    });

    jQuery('.panel .tools .fa-times').click(function () {
        jQuery(this).parents(".panel").parent().remove();
    });


//    tool tips

    $('.tooltips').tooltip();

//    popovers

    $('.popovers').popover();


// custom bar chart

    if ($(".custom-bar-chart")) {
        $(".bar").each(function () {
            var i = $(this).find(".value").html();
            $(this).find(".value").html("");
            $(this).find(".value").animate({
                height: i
            }, 2000)
        })
    }


}();

function StringBuffer() {
    this.__strings__ = new Array();
}
StringBuffer.prototype.append = function (str) {
    this.__strings__.push(str);
};
StringBuffer.prototype.toString = function () {
    return this.__strings__.join("");
};

