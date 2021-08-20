/**
 * Created with IntelliJ IDEA.
 * User: lmamani
 * Date: 13/08/13
 * Time: 06:39 PM
 * To change this template use File | Settings | File Templates.
 */
function Application(b) {
	/*Valores por defecto*/
	this.defaults = {
		defaultContent: "body",
		noAjaxClass: "noAjax",
		statusCode: {},
		onAfterSuccessRequest: function () {},
		onAfterFailRequest: function () {},
		onBeforeRequest: function () {
			$(this.defaultContent).fadeTo(1, 0.5)
		},
		onRequestComplete: function () {
			$(this.defaultContent).fadeTo("fast", 1)
		}
	};
	this.defaultContent = b.defaultContent ? b.defaultContent : this.defaults.defaultContent;
	this.noAjaxClass = b.noAjaxClass ? b.noAjaxClass : this.defaults.noAjaxClass;
	this.statusCode = b.statusCode ? b.statusCode : this.defaults.statusCode;
	this.onAfterSuccessRequest = b.onAfterSuccessRequest ? b.onAfterSuccessRequest : this.defaults.onAfterSuccessRequest;
	this.onAfterFailRequest = b.onAfterFailRequest ? b.onAfterFailRequest : this.defaults.onAfterFailRequest;
	this.onBeforeRequest = b.onBeforeRequest ? b.onBeforeRequest : this.defaults.onBeforeRequest;
	this.onRequestComplete = b.onRequestComplete ? b.onRequestComplete : this.defaults.onRequestComplete
}
Application.prototype.PeticionExitosa = function (b, a, d, e) {
	var c;
	try {
		var f = $(b);
		if (1 == f.length) {
			var g = f.first().attr("id");
			(clientElement = document.getElementById(g)) && (c = "#" + g)
		}
	} catch (h) {}
	c ? (this._target = c, $(this._target).html($(b).html())) : (this._target || (this._target = this.defaultContent), $(this._target).html(b));
	this.applicationObject.InicializarAcciones(this._target);
	this.applicationObject.onAfterSuccessRequest(b, a, d, e)
};
Application.prototype.PeticionFallida = function (b, a, d) {
	this.applicationObject.onAfterFailRequest(b, a, d)
};
Application.prototype.BeforeRequest = function (b, a) {
	this.applicationObject.onBeforeRequest(b, a)
};
Application.prototype.RequestComplete = function () {
	this.applicationObject.onRequestComplete()
};
Application.prototype.InicializarAcciones = function (b) {
	b = b ? b + " " : "";
	$(b + "form:not(." + this.noAjaxClass + ")").submit({
		applicationObject: this
	}, function (a) {
		try {
			$(this).attr("target") || (requestParams = a.data.applicationObject.getRequestComponents($(this).attr("action")), requestParams.requestUrl && (target = requestParams.requestTarget || a.data.applicationObject.defaultContent, $(this).ajaxSubmit({
				applicationObject: a.data.applicationObject,
				_target: target,
				data: requestParams.requestParams,
				success: a.data.applicationObject.PeticionExitosa,
				error: a.data.applicationObject.PeticionFallida,
				beforeSend: a.data.applicationObject.BeforeRequest,
				complete: a.data.applicationObject.RequestComplete,
				statusCode: a.data.applicationObject.statusCode,
				cache: !1
			}), a.preventDefault()))
		} catch (b) {
			a.preventDefault()
		}
	});
	$(b + "a[href]:not(." + this.noAjaxClass + ")").click({
		applicationObject: this
	}, function (a) {
		$(this).attr("target") || (requestParams = a.data.applicationObject.getRequestComponents($(this).attr("href")), requestParams.requestUrl && (target = requestParams.requestTarget ||
				a.data.applicationObject.defaultContent,
        $.ajax({
			applicationObject: a.data.applicationObject,
			type: "get",
			url: requestParams.requestUrl,
			data: requestParams.requestParams,
			_target: target,
			success: a.data.applicationObject.PeticionExitosa,
			error: a.data.applicationObject.PeticionFallida,
			beforeSend: a.data.applicationObject.BeforeRequest,
			complete: a.data.applicationObject.RequestComplete,
			statusCode: a.data.applicationObject.statusCode,
			cache: !1
		}), a.preventDefault()))
	});
	$(b + "input[type=text][href], " + b + "select[href]").change({
				applicationObject: this
			},
			function (a) {
				requestParams = a.data.applicationObject.getRequestComponents($(this).attr("href"));
				requestParams.requestUrl && (target = requestParams.requestTarget || a.data.applicationObject.defaultContent, name = $(this).attr("name"), ownParam = "", "undefined" != name && "" != name && (ownParam = "&" + name + "=" + $(this).val()), $.ajax({
					applicationObject: a.data.applicationObject,
					type: "get",
					url: requestParams.requestUrl,
					data: requestParams.requestParams + ownParam,
					_target: target,
					success: a.data.applicationObject.PeticionExitosa,
					error: a.data.applicationObject.PeticionFallida,
					beforeSend: a.data.applicationObject.BeforeRequest,
					complete: a.data.applicationObject.RequestComplete,
					statusCode: a.data.applicationObject.statusCode,
					cache: !1
				}), a.preventDefault())
			});
	$(b + "button[type=button][href], " + b + "input[type=button][href]").click({
		applicationObject: this
	}, function (a) {
		requestParams = a.data.applicationObject.getRequestComponents($(this).attr("href"));
		requestParams.requestUrl && (target = requestParams.requestTarget || a.data.applicationObject.defaultContent, name = $(this).attr("name"), ownParam =
				"", "undefined" != name && "" != name && (ownParam = "&" + name + "=" + $(this).val()), $.ajax({
			applicationObject: a.data.applicationObject,
			type: "get",
			url: requestParams.requestUrl,
			data: requestParams.requestParams + ownParam,
			_target: target,
			success: a.data.applicationObject.PeticionExitosa,
			error: a.data.applicationObject.PeticionFallida,
			beforeSend: a.data.applicationObject.BeforeRequest,
			complete: a.data.applicationObject.RequestComplete,
			statusCode: a.data.applicationObject.statusCode,
			cache: !1
		}), a.preventDefault())
	})
};
Application.prototype.getRequestComponents = function (b) {
	var a = {
		requestUrl: "",
		requestParams: "",
		requestTarget: ""
	};
	if (!b || "?" === b || "#" === b || "#?" === b || "?#" === b) return a;
	var d = document.createElement("a");
	d.href = b;
	var e = b.indexOf("?"),
			c = b.indexOf("#");
	firstSymbol = 0 <= e && 0 <= c ? e < c ? e : c : e > c ? e : c;
	a.requestUrl = 0 <= firstSymbol ? b.substr(0, firstSymbol) : b;
	0 == a.requestUrl.indexOf("javascript:") && (a.requestUrl = "");
	if (1 < d.search.length) {
		var b = d.search.substr(1).split("&"),
				f;
		for (f in b) tmpItem = b[f].split("="), tmpItem[0] &&
				(finalValue = tmpItem[1] ? tmpItem[1] : $("#" + tmpItem[0]).val() || "", a.requestParams += tmpItem[0] + "=" + finalValue + "&");
		0 < a.requestParams.length && (a.requestParams = a.requestParams.substr(0, a.requestParams.length - 1))
	}
	a.requestTarget = d.hash;
	return a
};
