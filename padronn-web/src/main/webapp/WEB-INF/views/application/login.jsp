<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<%--<script type="text/javascript">
    var onloadCallback = function () {
        grecaptcha.render('html_element', {
            'sitekey': '6LdSjnwUAAAAALcp5lAKNYfdiHQHhLADPn2IX7kI'
        });
    };
</script>--%>

<div class="row-fluid">
    <div class="span6">
        <spring:eval expression="@applicationProps['application.resources.local.version']" var="localResourcesVersion"/>
        <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
        <c:set var="localResourcesURL" value="${ctx}/resources-${localResourcesVersion}" scope="request"/>
        <c:set var="req" value="${pageContext.request}"/>
        <!--<c:set var="baseURL" value="${req.scheme}://${req.serverName}:${req.serverPort}${req.contextPath}"/>-->
        <img src="${localResourcesURL}/img/collage.jpg" width="590" height="427"/>
    </div>
    <div class="span5 offset1">
        <div class="tab-pane active" id="login-form">

            <div class="form-container form-log-in" style="width:80%;">
                <form class="form-signin" id="form-signin" action="login.do#wrap" method="post" autocomplete="off">

                    <fieldset>
                        <h2 class="login-title">Iniciar Sesión</h2>
                        <!--<p class="lead"><br/>Iniciar Sesión</p>-->
                        <c:if test="${errorMessage!=null or not empty errorMessage}">
                            <p class="text-error">
                                    ${errorMessage}
                            </p>
                        </c:if>

                        <c:choose>
                            <c:when test="${not empty entidades}">
                                <div class="control-group">
                                    <label class="control-label" for="user">DNI:</label>
                                    <div class="controls">
                                        <input type="text" name="user" id="user" class="input-block-level"
                                               value="${login}" readonly="readonly" placeholder="DNI"/>
                                    </div>
                                </div>

                                <div class="control-group">
                                    <label class="control-label" for="password">Contraseña:</label>
                                    <div class="controls">
                                        <input type="password" name="password" id="password" class="input-block-level"
                                               value="${password}" readonly="readonly" autocomplete="off"/>
                                        <!--<input type="password" name="password" id="password" class="input-block-level" readonly="readonly" autocomplete="off"/>-->
                                    </div>
                                </div>

                                <div class="control-group error">
                                    <label class="control-label" for="entidad">Entidad</label>
                                    <div class="controls">
                                        <!--<select id="entidad" name="entidad" class="input-block-level">
                                            &lt;!&ndash;<option value="${valorPorDefecto}" selected="selected">${textoPorDefecto}</option>&ndash;&gt;
                                            <option value="0">Seleccione una entidad para logearse...</option>
                                            <c:forEach var="item" items="${entidades}" varStatus="loop">
                                                <option value="${item.coEntidad}">${item.deEntidad}</option>
                                            </c:forEach>
                                        </select>-->
                                        <util:simpleSelect selectorId="entidad" selectorName="entidad"
                                                           selectorClass="input-block-level"
                                                           textoPorDefecto="Seleccione una entidad para logearse..."
                                                           valorPorDefecto="0"
                                                           items="${entidades}" nombreVarItem="deEntidad"
                                                           nombreVarValue="coEntidad"/>
                                    </div>
                                </div>


                            </c:when>
                            <c:otherwise>
                                <div class="control-group">
                                    <label class="control-label" for="user">DNI:</label>
                                    <div class="controls">
                                        <input type="text" name="user" class="input-block-level"
                                               placeholder="Ingrese su DNI" value="${login}" autofocus="autofocus"
                                               onkeypress="if(event.keyCode==13){document.getElementById('password').focus();event.preventDefault();return false;}"/>
                                    </div>
                                </div>
                                <div class="control-group">
                                    <label class="control-label" for="passwor">Contraseña:</label>
                                    <div class="controls">
                                        <input type="password" name="password" id="passwor" class="input-block-level"
                                               placeholder="Ingrese su Contraseña"/>
                                    </div>
                                </div>

                                <div id="captcha-section">
                                    <jsp:include page="captcha.jspx"/>
                                </div>

                                <div class="control-group">
                                    <label class="control-label" for="deCaptcha">Código CAPTCHA</label>
                                    <div class="controls">
                                        <input type="text" name="deCaptcha" id="deCaptcha" class="input-block-level"
                                               placeholder="Ingrese el texto de la imagen"/>
                                    </div>
                                </div>
<%--                                <div id="html_element" style="margin-bottom:20px;"></div>--%>
                            </c:otherwise>
                        </c:choose>
                        <div class="controls controls-row row-fluid">
                            <c:choose>
                                <c:when test="${not empty entidades}">
                                    <div class="span6">
                                        <button class="btn btn-primary input-block-level" type="submit"><span
                                                class="icon-ok-sign icon-white"><!-- --></span>&#160;Continuar
                                        </button>
                                    </div>
                                    <div class="span6">
                                        <!--<button class="btn btn-danger input-block-level" onclick="$('form-signin>input').val(''); $('form-signin').submit();">Cancelar</button>-->
                                        <a href="" class="btn btn-danger input-block-level"><span
                                                class="icon-remove-sign icon-white"><!-- --></span>&#160;Cancelar</a>
                                        <!--<button class="btn btn-danger input-block-level" onclick="location.reload(true);">Cancelar</button>-->
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!--<div class="span6">-->
                                    <button class="btn btn-primary " type="submit">
                                        <span class="icon-ok-sign icon-white"><!-- --></span>&#160;Iniciar Sesión
                                    </button>
                                    <!--</div>-->
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </fieldset>
                </form>
                <%--<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit"
                        async defer>
                </script>--%>
                <!--<p><a href="recuperarContrasena.do">Recuperar contraseña</a></p>-->
            </div>
        </div>

        <div class="help-browser" style="display: none;">
            <p class="text-info">Instale Google Chrome, Mozilla Firefox, Opera. Dar "click" en cualquier de los iconos
                mostrados a continuación.</p>
            <!--<p class="text-info">Instale google chrome(u otro navegador moderno como FIREFOX, OPERA o SAFARI ), para el correcto funcionamiento del sistema.</p>-->
            <div>
                <a href="http://www.google.com/chrome/" target="_blank" class="noAjax"
                   style="display: block; margin-bottom: 10px;">
                    <!--<img src="${baseURL}/resources-${localResourcesVersion}/img/chrome_logo.png" style="width: 25px; height: 25px; margin-right: 15px;" />-->
                    <img src="${localResourcesURL}/img/chrome_logo.png"
                         style="width: 25px; height: 25px; margin-right: 15px;"/>
                    <span>Google Chrome</span>
                </a>
                <a href="http://www.mozilla.org/es-ES/firefox/new/‎" target="_blank" class="noAjax"
                   style="display: block;margin-bottom: 10px;">
                    <!--<img src="${baseURL}/resources-${localResourcesVersion}/img/firefox-logo.png" style="width: 25px; height: 25px; margin-right: 15px;" />-->
                    <img src="${localResourcesURL}/img/firefox-logo.png"
                         style="width: 25px; height: 25px; margin-right: 15px;"/>
                    <span>Mozilla Firefox</span>
                </a>
                <a href="http://www.opera.com/" target="_blank" class="noAjax"
                   style="display: block; margin-bottom: 10px;">
                    <!--<img src="${baseURL}/resources-${localResourcesVersion}/img/opera-logo.png" style="width: 25px; height: 25px; margin-right: 15px;" />-->
                    <img src="${localResourcesURL}/img/opera-logo.png"
                         style="width: 25px; height: 25px; margin-right: 15px;"/>
                    <span>Opera</span>
                </a>
            </div>
        </div>
        <br/>
        <!--<div class="help-java">
                <p class="text-info">Instale Java.</p>
                <a href="http://www.java.com/es/download/" class="noAjax" target="_blank">
                    <img src="${baseURL}/resources-${localResourcesVersion}/img/java-logo.png" style="width: 35px; height: 70px;" />
                </a>
            </div>-->
    </div>
</div>
<style type="text/css">
    #wrap {
        background: none;
    }

    #header {
        height: 96px;
        margin-bottom: 18px;
    }

    #header #login {
        background-image: url(${baseURL}/resources-${localResourcesVersion}/img/top_titulo_login.jpg)
    }
</style>

<script type="text/javascript">
    /*<![CDATA[*/
        $(function () {
            $('#header #logo').css('background-image', "${baseURL}/resources-${localResourcesVersion}/img/top_titulo_login.jpg");
            $("[name='user']").inputmask('dni');

            if ($.browser.msie && parseInt($.browser.version) <= 9) {
                $('.help-browser').show();
            }
        });
    /*]]>*/
</script>
