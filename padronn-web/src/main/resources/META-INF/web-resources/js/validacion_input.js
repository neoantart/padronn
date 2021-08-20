/**
 * Created by jfloresh on 05/07/2016.
 */

var sets = {
    ACTA_NACIMIENTO: "0123456789",
    CARNET_EXTRANJERIA: "6789012345ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-",
    DESCRIPCION: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ.0123456789-' :/(),"+'"',
    DIRECCION: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÈÌÒÙÂÊÎÔÛÃÕ.0123456789':,ÁÉÍÓÚÄËÏÖÜÀ ",
    DI_EXTRANJERO: "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-",
    DNI: "0123456789",
    TELEFONO: " 0123456789[;*-+()]",
    EDAD: "0123456789",
    ESTADO_CIVIL: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz",
    EXPEDIENTE: "abcdefghijklmnñopqrstuvwxyz0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-",
    EMAIL: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789_@.-abcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ",
    NOMBRE: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ-' .0123456789",
    ENTIDAD: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÈÌÒÙÂÊÎÔÛÃÕb 'ÁÉÍÓÚÄËÏÖÜÀ",
    CARGO: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÈÌÒÙÂÊÎÔÛÃÕb 'ÁÉÍÓÚÄËÏÖÜÀ",
    OBSERVACION: "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÈÌÒÙÂÊÎÔÛÃÕ.0123456789-':\\/,ÁÉÍÓÚÄËÏÖÜÀ abcdefghijklmnñopqrstuvwxyzáéíóúäëïöüàèìòùâêîôûãõ",
    PASAPORTE: "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ-",
    SEXO: "BACDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz"
};



/**
 * Funcion que permite definir los caracteres permitidos en un campo de texto
 * @param event
 * @param permitidos
 * @returns {Boolean}
 */
function fnRestringirCaracteres(event) {
    //console.log('fnRestringirCaracteres');
    //recibir parametros
    var permitidos = event.data.perm;
    var nombreFuncion = event.data.func;

    //definir teclas especiales permitidas
    /*   0 = (codigo which correspondiente a las teclas: tab, end, home, arrow left, arrow right, delete)
     *  13 = intro			(incluida)
     *   8 = backspace		(incluida)
     *  32 = space			(incluida)******
     *   9 = tab		  	(incluida con which = 0)
     *  35 = end			(incluida con which = 0, el codigo 35 coincide con caracter #)
     *  36 = home			(incluida con which = 0, el codigo 36 coincide con caracter $)
     *  37 = arrow left		(incluida con which = 0, el codigo 37 coincide con caracter %)
     *  39 = arrow right	(incluida con which = 0, el codigo 39 coincide con caracter ')
     *  46 = delete			(incluida con which = 0, el codigo 46 coincide con caracter .)
     */
    var teclas_especiales = [0, 8, 13];

    // obtener tecla pulsada
    var evento = event || window.event;
    //var codigoCaracter = evento.charCode || evento.keyCode;
    var codigoCaracter = evento.which;
    //alert("codigoCaracter: " + codigoCaracter);
    var caracter = String.fromCharCode(codigoCaracter);

    // verificar teclas especiales
    var tecla_especial = false;
    for (var i in teclas_especiales) {
        if (codigoCaracter == teclas_especiales[i]) {
            tecla_especial = true;
            break;
        }
    }

    // verificar si la tecla pulsada se encuentra en los caracteres permitidos
    //console.log(permitidos)
    caracter = caracter.toUpperCase();
    if (permitidos.indexOf(caracter) != -1 || tecla_especial) {

        if (codigoCaracter == 13) {

            //detener la accion
            event.preventDefault();
            //ejecutar funcion
            if (nombreFuncion != null) {
                eval(nombreFuncion);
            }

        } else {

            //escribir caracter
            return true;

        }

    } else {

        //detener la accion
        event.preventDefault();

    }

}

function fu_validaCaracter(pCadenaTex,pCampo){
    var pCarValidoNombres = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ-' .0123456789";
    var pCarValidoDireccion = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ.0123456789-' :/(),"+'"';
    var pCarValidoEmail = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ0123456789_@.-abcdefghijklmnñopqrstuvwxyzäëïöüáéíóúàèìòùâêîôûãõ";
    var pCarValidoTelefono = " 0123456789[;*-()]";
    var p1CarValidoNombres = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ"; //PARA EL PRIMER CARACTER
    var p1CarValidoDireccion = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZÄËÏÖÜÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÃÕ.0123456789"; //PARA EL PRIMER CARACTER
    var pCaracter = "";
    var inicio = "OK";
    if (pCampo=="1") {pCaracter = pCarValidoNombres;}
    if (pCampo=="2") {pCaracter = pCarValidoDireccion;}
    if (pCampo=="3") {pCaracter = pCarValidoEmail;}
    if (pCampo=="4") {pCaracter = pCarValidoTelefono;}
    if (pCampo=="5") {pCaracter = p1CarValidoNombres;}
    if (pCampo=="6") {pCaracter = p1CarValidoDireccion;}
    for (var i=0; i<=pCadenaTex.length-1; i++) {
        var car = pCadenaTex.substring(i,i+1);
        var pos = pCaracter.indexOf((car));
        if (pos==-1) {
            return car;
            break;
        }
    }
    return inicio;
}

function fu_validaCaracterGrupo(pCadenaText) {
    var inicio = "OK";
    var pCarGrupo = "  |AAA|BBB|CCC|DDD|EEE|FFF|GGG|HH|III|JJ|KK|LLL|MMM|NNN|OOO|PPP|QQ|RRR|SSS|TTT|UUU|VV|WW|XX|YY|ZZZ|ÄÄ|ËË|ÑÑ|ÖÖ|ÜÜ|";
    var result = new Array();
    var p = new Array();
    p = pCarGrupo.split("|");
    for (var i=0; i<p.length -1; i++) {
        result = pCadenaText.match((p[i]));
        if (result!=null) {
            return result;
            break;
        }
    }
    return inicio;
}

function validaCaracteres(pCadenaTex,pCampo) {
    var rpta = fu_validaCaracter(pCadenaTex,pCampo);
    if (rpta!="OK") {
        rpta = 'contiene caracteres no permitidos: '+rpta;
        return rpta;
    }
    if (pCampo=="1") {
        if (pCadenaTex.substr(0,1)==" ") {
            rpta = 'No puede tener como primer caracter: Un espacio en blanco';
            return rpta;
        } else {
            rpta = fu_validaCaracter(pCadenaTex.substr(0,1),"5");
            if (rpta!="OK") {
                rpta = 'No puede tener como primer caracter: '+rpta;
                return rpta;
            }
        }
    }
    if (pCampo=="2") {
        if (pCadenaTex.substr(0,1)==" ") {
            rpta = 'No puede tener como primer caracter: Un espacio en blanco';
            return rpta;
        } else {
            rpta = fu_validaCaracter(pCadenaTex.substr(0,1),"6");
            if (rpta!="OK") {
                rpta = 'No puede tener como primer caracter: '+rpta;
                return rpta;
            }
        }
    }
    if (pCadenaTex.substr(pCadenaTex.length-1,1)==" ") {
        rpta = 'No puede tener como último caracter: Un espacio en blanco';
        return rpta;
    }
    rpta = fu_validaCaracterGrupo(pCadenaTex);
    if (rpta=="  ") {
        rpta = 'No puede tener más de un espacio en blanco entre los caracteres';
    } else if (rpta!="OK") {
        rpta = 'contiene grupo de caracteres no permitidos: '+rpta;
    }
    return rpta;
}

/**
 * Funcion que evita pegar Crtl + v(V) en un campo de texto
 */
function fnOmitirCtrlV(event) {

    //detectar cuando se presione Ctrl + v(V)
    if (event.ctrlKey && (event.which == 86 || event.which == 118)) {
        //detener la accion
        event.preventDefault();
    }

}