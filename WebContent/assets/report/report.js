$(function(){
    resizeReportFrame();

    //$(window).resize(function() {
    //    resizeReportFrame();
    //});
});

function resizeReportFrame(){
    var offset = $('#reportFrame').offset();
    if(offset != undefined) {
        var height = $(window).height() - offset.top - 30;
        $('#reportFrame').attr("height", height);
    }
}
