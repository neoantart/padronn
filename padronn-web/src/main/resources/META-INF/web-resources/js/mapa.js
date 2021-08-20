var gmarkers = [];
var geocoder;
var map;
var bounds;

function CargarApi(){
    //$.getScript("http://www.google.com/jsapi?key=ABQIAAAAsw1nr5h31iihZ5pnE9tEXRTPYjB1Ij1otfF0XFH1SW_fTjZCphQeZfrrkok-U-6jvK_OJFL5UzV0kw&callback=AbrirMapas");
    $.getScript("http://www.google.com/jsapi?key=AIzaSyB0LLsPobs4_1XAgY8p8vhyAERziU2U-Zw&callback=AbrirMapas");
//$.getScript("http://www.google.com/jsapi?key=ABQIAAAAsw1nr5h31iihZ5pnE9tEXRQpDmmpk8q921mRIPG4qgOvy0T09hTnEDk_ytNoo33v_WgKSkam20tR-g&callback=AbrirMapas");
}
function AbrirMapas(){
    google.load("maps", "2.x", {
        "callback" : MapaAbierto,
        "language" : "es"
    });
}

function MapaAbierto(){

    var mapOptions = {
        googleBarOptions : {
            style : "new",
            adsOptions: {
                client: "partner-google-maps-api",
                channel: "AdSense for Search channel",
                adsafe: "high",
                language: "es"
            }
        },
        zoom: 8
    }

    map = new GMap2($("#cmapa")[0],mapOptions);
    //map.addControl(new GLargeMapControl());
    //map.addControl(new GMapTypeControl());
    //map.addControl(new GOverviewMapControl());
    //map.enableScrollWheelZoom();

    geocoder = new GClientGeocoder();

    name = $("#name").val();
    address = $("#address").val();

    //alert(name);
    //alert(address);
    //address = "";
    name=name.replace(/^"/,"");
    name=name.replace(/"$/,"");
    address=address.replace(/^"/,"");
    address=address.replace(/"$/,"");

    geocoder.getLatLng(address,
        function(point){
            if(!point){

            }else{
                map.setUIToDefault();
                map.setCenter(point,13);
                marker = new GMarker(point, {
                    draggable: true
                });
                map.addOverlay(marker);
                info="<div style='font-size:12px;'><b>"+name+"</b></div>";
                marker.openInfoWindowHtml(info);
            }
        });
}

$(document).ready(function(){
    CargarApi();
});