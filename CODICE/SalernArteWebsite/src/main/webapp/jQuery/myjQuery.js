

$(document).ready(function () {
    $(".container2").click(function () {
        $("#menuIntegrate").toggleClass("change");
        $(".items3").toggle();
    });
    $(".hidemenu").click(function(){
        $(".showmenu").slideToggle();
    });
    window.addEventListener("resize", function () {
        xx();
    });
    xx();
});


var xx = function () {
    console.log(window.innerWidth);
    if (window.innerWidth >= 1152) {
        $(".items3").css("display", "block");
    }else {
        $(".togglecerca").hide();
        var op=$(".bar2").css('opacity');
        if(op=='0'){
            $("#menuIntegrate").toggleClass("change");
        }
        $(".items3").hide();
    }
}





