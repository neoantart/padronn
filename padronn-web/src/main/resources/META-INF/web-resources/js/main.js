// Solucion rapida para weblogic
var urlApp = window.location.href; //return the href URL of the current page
if (urlApp.charAt(urlApp.length - 1) != "/") {  //return the last character of a string (URI)
    window.location = urlApp + "/"; //assign a slash to the last character in the URI
}

function Abrir_ventana (pagina) {
    var opciones =("width = 500, height = 390, top = 325, left = 100, fullscreen=no, toolbar=no, location=no, directories=no, status=no, menubar=no, resizable=no");
    window.open(pagina, "", opciones);
}

//prevent backspace
$(document).on("keydown", function (e) {
    if ((e.which === 8 && !$(e.target).is("input, textarea") ) /*|| (e.which === 37 && $(e.target).is("input, textarea")) || (e.which === 39 && $(e.target).is("input, textarea"))*/) {
         e.preventDefault();
    }
});

$(function () {
    // funcion que bloquea la pagina, evitando que el usuario modifiquer formularios luego de enviar data
    var bloquearPagina = function () {
            $('#wrap').block({
                message: '<h4>Cargando</h4><div class="center-block loader"></div>',
                css: {backgroundColor: '#d9edf7', width:300},
                baseZ: 900011
            });
    },
    desbloquearPagina = function () {
            $('#wrap').unblock();
    };

    Aplicacion = new Application(
    {
            defaultContent: '#application-content',
            noAjaxClass: 'noAjax',
            statusCode: {
                401: function () {
                    document.location = '';
                }
            },
            onAfterSuccessRequest: function () {
                $("#errorButton").hide();
            },
            onAfterFailRequest: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status != 401) {
                    $('#debug-content').contents().find('html').html(jqXHR.responseText);
                    //$("#errorModal").modal('show');
                    $("#errorButton").show();
                }
            },
            onBeforeRequest: function (xhr, settings) {
//                $.blockUI();
                bloquearPagina();
            },
            onRequestComplete: function () {
//                $.unblockUI();
                desbloquearPagina();
            }
        }
    );
    $("#errorButton").hide();
    Aplicacion.InicializarAcciones();

    // Wire up the events as soon as the DOM tree is ready

    /**para hacer el menu fixed en top**/
    //if ($filter.size()>1) {

    /*$(window).scroll(function () {
     var $filter = $('#nav'),
     $filterSpacer = $('<div />', {
     "class": "filter-drop-spacer",
     "height": $filter.outerHeight()
     }),
     heightHeader = 137,
     heightMenu = 40;
     if ($filter.size()) {
     $(window).scroll(function () {
         if (!$filter.hasClass('fix') && $(window).scrollTop() > $filter.offset().top + (heightHeader - heightMenu)) {
             $filter.before($filterSpacer);
             $filter.addClass("fix");
         }
         else if ($filter.hasClass('fix') && $(window).scrollTop() - (heightHeader - heightMenu) < $filterSpacer.offset().top) {
         $filter.removeClass("fix");
         $filterSpacer.remove();
     }
     });
     }
     });    */
    //var versions = deployJava.getJREs();


    /* $(document).ready(function () {
     wireUpEvents();
     });*/

    /*    $(window).on('beforeunload', function(e){
     console.log('close aqui...')
     console.log(e);
     return "Desea salir?";
     });*/


//    $(document).ready(function() {
//    wireUpEvents();
//    });

/*    // timer session
    var interval = setInterval(function () {
        var timer = $('#timer-session').html();
        if (timer != undefined) {
            timer = timer.split(':');
            var minutes = parseInt(timer[0], 10);
            var seconds = parseInt(timer[1], 10);
            seconds -= 1;
            if (minutes < 0) return clearInterval(interval);
            if (minutes < 10 && minutes.length != 2) minutes = '0' + minutes;
            if (seconds < 0 && minutes != 0) {
                minutes -= 1;
                seconds = 59;
            }
            else if (seconds < 10 && length.seconds != 2) seconds = '0' + seconds;
            $('#timer-session').html(minutes + ':' + seconds);

            if (minutes == 0 && seconds == 0) {
                clearInterval(interval);
//                dont_confirm_leave = 1;
                wireUpEvents();
                window.location = window.location.href
//                dont_confirm_leave = 0;
            }
        }
    }, 1000);*/

    $('.foto-usuario').hide();
});

var mousePoint = {clientX: 0, clientY: 0};
if (is_chrome() || is_mozilla_firefox()) {
    document.onmouseout = mousefuera;
    document.onmousemove = movimientomouse;
}

var unloadUrl = 'close_window.do';
function cerrarventana(evt) {
    if(is_mozilla_firefox()) {
        jQuery.post(unloadUrl)
        //return "...";
    }
    var oEvent = (window.event) ? window.event : evt;
    var k = oEvent.keyCode || oEvent.which;
    if (!oEvent.clientY) {
        if (oEvent.altKey == true && k == 115) { // altf4
            jQuery.post(unloadUrl)
        } else if (is_chrome() && (mousePoint.clientX == 0) && (mousePoint.clientY == 0)) {
            jQuery.post(unloadUrl)
        } else if ((oEvent.explicitOriginalTarget.tagName == 'undefined') || (oEvent.explicitOriginalTarget.tagName == "HTML")) {
            jQuery.post(unloadUrl)
        }
    } else {
        if (oEvent.altKey == true && k == 115) { // altf4
            jQuery.post(unloadUrl)
        } else if ((oEvent.clientY < 0)) {
            jQuery.post(unloadUrl)
        }
    }
}

//window.onbeforeunload=cerrarventana;

function is_chrome() {
    return navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
}

function is_mozilla_firefox() {
    return navigator.userAgent.toLowerCase().indexOf('firefox') > -1;
}

function movimientomouse(event) {
    var evento = (window.event) ? window.event : event;
    mousePoint.clientX = evento.clientX;
    mousePoint.clientY = evento.clientY
}

function mousefuera() {
    mousePoint.clientX = 0;
    mousePoint.clientY = 0;
}

function destroySession() {
    $.ajax({
        url: 'close_window.do',
        type: 'get',
        dataType: 'html',
        success: function (response) {
        }
    });
    return "Seguro que quieres salir?";
//        window.onbeforeunload = goodbye;
}

// -------------------------------------------------------------------
function showDown(evt) {
    evt = (evt) ? evt : ((event) ? event : null);
    if (evt){
        if(evt.ctrlKey && (/*evt.keyCode == 86 ||*/ evt.keyCode == 118)){
            cancelKey(evt);
        }
        else if (evt.keyCode == 116) {
            // F5
            cancelKey(evt);
        }
        else if (evt.ctrlKey && (evt.keyCode == 78 || evt.keyCode == 82 || evt.keyCode == 84)) {
            // When ctrl is pressed with R or N or T
//            cancelKey(evt);
            return false;
        }
        else if (evt.ctrlKey && (evt.keyCode == 65 || evt.keyCode == 85)) {
            // When ctrl is pressed with U or A
            cancelKey(evt);
        }
        else if (evt.altKey && (evt.keyCode == 37 || evt.keyCode == 39)) {
            // stop Alt left or right cursor
            return false;
        }
    }
}

function cancelKey(evt) {
    if (evt.preventDefault) {
        evt.preventDefault();
        return false;
    }
    else {
        evt.keyCode = 0;
        evt.returnValue = false;
    }
}

document.onkeydown = showDown;

/*$(document).ready(function() {
    function disableBack() { window.history.forward() }

    window.onload = disableBack();
    window.onpageshow = function(evt) { if (evt.persisted) disableBack() }
});*/

/*// llamada para el timer session
$(document).ajaxSuccess(function (event, request, settings) {
    if (!(settings.url.indexOf('session_timer') != -1)) {
        $.ajax({
            url: 'session_timer.do',
            type: 'get',
            dataType: 'html',
            success: function (response) {
                $('#timer-session').html(response);
            }
        });
        return false;
    }
});*/

//--------------------------------------------------------------
/*var public_enteros = "0123456789";
var altf4 = false;
var isIntroLastKey = false;
var mousePoint = {clientX: 0, clientY: 0};
if (is_chrome()) {
    document.onmouseout = mousefuera;
    document.onmousemove = movimientomouse;
}
function lTrim(a) {
    while (a.charAt(0) == " ")a = a.substr(1, a.length - 1);
    return a
}
function rTrim(a) {
    while (a.charAt(a.length - 1) == " ")a = a.substr(0, a.length - 1);
    return a
}
function allTrim(a) {
    return rTrim(lTrim(a))
}
function tamanoVentanaNavegador() {
    width = 0;
    height = 0;
    if (typeof(window.innerWidth) == 'number') {
        width = window.innerWidth;
        height = window.innerHeight
    } else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
        width = document.documentElement.clientWidth;
        height = document.documentElement.clientHeight
    } else if (document.body && (document.body.clientWidth || document.body.clientHeight)) {
        width = document.body.clientWidth;
        height = document.body.clientHeight
    }
    return{ancho: width, alto: height}
}
function fechaActual() {
    var a = new Date();
    var b = a.getYear();
    var c = (b < 1000) ? b + 1900 : b;
    a.setHours(0);
    var d = a.getUTCDate();
    var e = (d < 10) ? "0" + d : d;
    var f = a.getMonth() + 1;
    var g = (f < 10) ? "0" + f : f;
    var h = e + "/" + g + "/" + c;
    return h
}
function isValidDate(fecha) {
    var retval = true;
    var pattern = /([0-9]{2})\/([0-9]{2})\/([0-9]{4})/;
    var res = pattern.exec(fecha);
    var fecha_regreso_aux = '';
    var errores = 0;
    if (!res) {
        retval = false
    } else {
        var dia = res[1];
        var mes = res[2];
        var ano = res[3];
        if (dia < 1 || dia > 31) {
            retval = false
        }
        if (mes < 1 || mes > 12) {
            retval = false
        }
        if (ano < 2005 || ano > 3000) {
            retval = false
        }
    }
    return retval
}
function redondear(valor) {
    return Math.round(valor * Math.pow(10, 2)) / Math.pow(10, 2)
}
function replicate(str, len) {
    var x = '';
    if (str) {
        var y = len - str.length;
        for (i = 0; i < y; i++) {
            x += "0"
        }
    }
    return x + str
}
function nombreMes(codigo) {
    switch (codigo) {
        case"01":
            return"ENERO";
        case"02":
            return"FEBRERO";
        case"03":
            return"MARZO";
        case"04":
            return"ABRIL";
        case"05":
            return"MAYO";
        case"06":
            return"JUNIO";
        case"07":
            return"JULIO";
        case"08":
            return"AGOSTO";
        case"09":
            return"SETIEMBRE";
        case"10":
            return"OCTUBRE";
        case"11":
            return"NOVIEMBRE";
        case"12":
            return"DICIEMBRE"
    }
}
function detectVersion() {
    version = parseInt(navigator.appVersion);
    return version
}
function detectOS() {
    if (navigator.userAgent.indexOf('Win') == -1) {
        OS = 'Macintosh'
    } else {
        OS = 'Windows'
    }
    return OS
}
function detectBrowser() {
    if (navigator.appName.indexOf('Netscape') == -1) {
        browser = 'IE'
    } else {
        browser = 'Netscape'
    }
    return browser
}
function showPopupWindow(a, w, h) {
    var A = (screen.availHeight / 2) - (h / 2);
    var L = (screen.availWidth / 2) - (w / 2);
    open(a, "", "scrollbars=yes, resizable=yes, status=0, toolbar=0, location=0, menubar=0, titlebar=false,width=" + w + ",height=" + h + ",top=" + A + ",left=" + L)
}
function cerrarventana(evt) {
    var oEvent = (window.event) ? window.event : evt;
    if (!oEvent.clientY) {
        if (altf4 == true) {
            jQuery.post(unloadUrl)
        } else if (is_chrome() && (mousePoint.clientX == 0) && (mousePoint.clientY == 0) && !isIntroLastKey) {
            jQuery.post(unloadUrl)
        } else if ((oEvent.explicitOriginalTarget.tagName == 'undefined') || (oEvent.explicitOriginalTarget.tagName == "HTML")) {
            jQuery.post(unloadUrl)
        }
    } else {
        if (altf4 == true) {
            jQuery.post(unloadUrl)
        } else if ((oEvent.clientY < 0) && (!isIntroLastKey)) {
            jQuery.post(unloadUrl)
        }
    }
}
function presionaTecla(event) {
    var oEvent = (window.event) ? window.event : event;
    var k = oEvent.keyCode || oEvent.which;
    if (oEvent.altKey == true && k == 115) {
        altf4 = true
    }
    if (k == 13) {
        isIntroLastKey = true;
        setTimeout("isIntroLastKey=false", 1000);
    } else {
        isIntroLastKey = false
    }
}
function is_chrome() {
    return navigator.userAgent.toLowerCase().indexOf('chrome') > -1;
}
function movimientomouse(event) {
    var evento = (window.event) ? window.event : event;
    mousePoint.clientX = evento.clientX;
    mousePoint.clientY = evento.clientY
}
function mousefuera() {
    mousePoint.clientX = 0;
    mousePoint.clientY = 0;
}
var KeyboardClass = function (pEv, pOkeys) {
    this.anykeys = ((typeof(pOkeys) == 'string') && (pOkeys.length > 0)) ? pOkeys : ' ./|-()*"0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ';
    this.espKeysList = (typeof(public_esp_keys) != 'undefined') ? public_esp_keys : [8, 13, 27, 37, 39, 46];
    this.objEvent = (window.event) ? window.event : pEv;
    this.currentObj = this.objEvent.target || this.objEvent.srcElement;
    this.k = this.objEvent.keyCode || this.objEvent.which;
    this.character = String.fromCharCode(this.k);
    this.isSpecialKey = function () {
        for (var i in this.espKeysList) {
            if (this.k == this.espKeysList[i]) {
                return true
            }
        }
        return false
    };
    this.isIntro = function () {
        if ((this.k == 13) || (this.k == 9) || (this.k == 0)) {
            return true
        }
        return false
    };
    this.isHelp = function () {
        if (this.k == 113) {
            return true
        }
        return false
    };
    this.isValidKey = this.anykeys.indexOf(this.character) != -1 || this.isSpecialKey()
}
function eventEvaluate(evt) {
    oEvent = (evt) ? evt : ((event) ? event : null);
}*/


