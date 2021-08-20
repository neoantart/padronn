var prevMessage = null;

function runAuthenticationApplet(appletPath, appletFileName, serviceUrl, width, height) {
    document.getElementById('appletSpace').height = height + 'px';
    document.getElementById('appletMessage').innerHTML = 'Conectando al servidor de autenticacion...';

    var attributes = {
        code : 'pe.gob.reniec.dnie.authentication.AppletDnie.class',
        archive :appletFileName,
        codebase:appletPath + '/',
        width : width,
        height : height
    };

    var parameters = {
        AppletService : serviceUrl + '/eid-applet-test/applet-service-autenticacion-dnie',
        BackgroundColor : '#EEEEEE',
        MessageCallbackEx : 'messageCallbackEx',
        Language: 'es',
        HideDetailsButton: 'true',
        AuthenticationDoneCallback: 'userAuthenticated'
    };
    var intercepted = '';
    var got = document.write;
    document.write = function(arg) {
        intercepted += arg;
    }
    deployJava.runApplet(attributes, parameters, '1.6');
    document.write = got;
    document.getElementById('appletSpace').innerHTML = intercepted;
    document.getElementById('readDNI').disabled = true;
}

function messageCallbackEx(status, messageId, message) {
    if (status == 'ERROR') {
        document.getElementById('appletMessage').className = 'text-error';
        document.getElementById('readDNI').disabled = false;
        document.getElementById('appletSpace').innerHTML = '';
    } else {
        document.getElementById('appletMessage').className = 'text-info';
    }
    if (messageId == 'INSERT_CARD_QUESTION') {
        document.getElementById('readDNI').disabled = false;
        document.getElementById('appletSpace').innerHTML = '';
    }
    if (prevMessage == 'PIN_INCORRECT') {
        prevMessage = messageId;
        if (messageId == 'DETECTING_CARD' || messageId == 'PIN_INCORRECT')
            return;
    }
    if (messageId == 'REMOVE_CARD' || prevMessage == 'PIN_INCORRECT') {
        document.getElementById('appletMessage').innerHTML =
            document.getElementById('appletMessage').innerHTML + '' + message + '';
    }
    else {
        document.getElementById('appletMessage').innerHTML = '' + message + '';
        if (message == 'Error de Seguridad')
            document.getElementById('appletMessage').innerHTML = document.getElementById('appletMessage').innerHTML + 'Verifique la Consola de Java.';
    }
}

function userAuthenticated(token) {
    document.getElementById('token').value = token;
    document.getElementById('btnSubmit').click();
}
