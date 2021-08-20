/**
 * User: vflores
 * Date: 10/06/13
 * Time: 05:01 PM
 */
var CotejoRegObsEditForm = {
	inicializarFormulario: function () {
		$("#coEstSalud").select2({
			placeholder: "Buscar",
			minimumInputLength: 2,
			allowClear: true,
			ajax: {
				url: "dominio/buscarEstablecimientosSalud.do",
				dataType: 'json',
				data: function (term, page) {
					return {
						nombre: term
					};
				},
				results: function (data, page) {
					return {results: [
						{ text: "En el Departamento", children:data.lista1},
						{ text: "Fuera del Departamento", children:data.lista2}
					]};

				}
			},
			formatResult: function (item) {
				if(item.text){
					return "<div>"+item.text+" (<em>"+item.children.length+" resultados</em>)</div>";
				}else{
					return '<div class="title"><strong>' + item.deEstSalud + '</strong> (Cod. RENAES ' + item.coEstSalud + ')</div><div class="body">' +
						/*'<div><em>' + item.tiEstSalud + '</em></div>' +*/
						'<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong>, ' + item.deDireccion + '</div>' +
						'</div>';
				}
			},
			formatSelection: function (item) {
				return item.deEstSalud + ' (' + item.coEstSalud + ', ' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + ')';
			},
			id: function (object) {
				return object.coEstSalud;
			},
			initSelection: function (element, callback) {
				var codigo = $(element).val();
				if (codigo !== "") {
					$.ajax("dominio/buscarEstablecimientoSalud.do", {
						data: {
							codigo: codigo
						},
						dataType: "json"
					}).done(function (data) {
							if (data.coEstSalud && data.deEstSalud) {
								callback(data);
							}
						});
				}
			}
		});
		$("#coInstEducativa").select2({
			placeholder: "Buscar",
			minimumInputLength: 2,
			allowClear: true,
			ajax: {
				url: "dominio/buscarInstitucionesEducativas.do",
				dataType: 'json',
				data: function (term, page) {
					return {
						nombre: term
					};
				},
				results: function (data, page) {
					return {results: [
						{ text: "En el Departamento", children:data.lista1},
						{ text: "Fuera del Departamento", children:data.lista2}
					]};
				}
			},
			formatResult: function (item) {
				if(item.text){
					return "<div>"+item.text+" (<em>"+item.children.length+" resultados</em>)</div>";
				}else{
					return '<div class="title"><strong>' + item.noCentroEducativo + '</strong> (Cod. Modular ' + item.coModular + ')</div><div class="body">' +
						//'<div>' + item.deDireccion + '</div>' +
						'<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong>, ' + item.diCentroEducativo + '</div>' +
						'</div>';
				}
			},
			formatSelection: function (item) {
				return item.noCentroEducativo + ' (' + item.coModular + ', ' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + ')';
			},
			id: function (object) {
				return object.coCentroEducativo;
			},
			initSelection: function (element, callback) {
				var codigo = $(element).val();
				if (codigo !== "") {
					$.ajax("dominio/buscarInstitucionEducativa.do", {
						data: {
							codigo: codigo
						},
						dataType: "json"
					}).done(function (data) {
							if (data.coCentroEducativo && data.noCentroEducativo) {
								callback(data);
							}
						});
				}
			}
		});
		$("#coUbigeoInei").select2({
			placeholder: "Buscar",
			minimumInputLength: 2,
			allowClear: true,
			ajax: {
				url: "dominio/buscarUbigeos.do",
				dataType: 'json',
				data: function (term, page) {
					return {
						nombre: term
					};
				},
				results: function (data, page) {
					return {results: [
						{ text: "En el Departamento", children:data.lista1},
						{ text: "Fuera del Departamento", children:data.lista2}
					]};
				}
			},
			formatResult: function (item) {
				if(item.text){
					return "<div>"+item.text+" (<em>"+item.children.length+" resultados</em>)</div>";
				}else{
					return "<div>" + item.deDepartamento + ", " + item.deProvincia + ", " + item.deDistrito + "</div>";
				}
			},
			formatSelection: function (item) {
				return item.deDepartamento + ", " + item.deProvincia + ", " + item.deDistrito;
			},
			id: function (object) {
				return object.coUbigeoInei;
			},
			initSelection: function (element, callback) {
				var codigo = $(element).val();
				if (codigo !== "") {
					$.ajax("dominio/buscarUbigeo.do", {
						data: {
							codigo: codigo
						},
						dataType: "json"
					}).done(function (data) {
							if (data.coUbigeoInei) {
								callback(data);
							}
						});
				}
			}
		});

        $('#coCentroPoblado').select2({
            placeholder: "Buscar centro poblado",
            minimumInputLength: 2,
            allowClear: true,
            ajax: {
                url: "dominio/buscarCentroPoblado.do",
                dataType: 'json',
                data: function (term, page) {
                    return {
                        nombre: term,
                        coUbigeoInei: $('#coUbigeoInei').val()
                    };
                },
                results: function (data, page) {
                    return {results: [
                        { text: "Centros poblados", children: data}
                    ]};
                }
            },
            formatResult: function (item) {
                if(item.text) {
                    return "<div>"+item.text+" (<em>"+item.children.length+" resultados</em>)</div>";
                } else {
                    return "<div>" + item.noCentroPoblado + ", " + item.noCategoria  + "</div>";
                }
            },
            formatSelection: function (item) {
                return item.noCentroPoblado + ", " + item.noCategoria;
            },
            id: function (object) {
                return object.coCentroPoblado;
            },
            initSelection: function (element, callback) {
                var codigo = $(element).val();
                if (codigo !== "") {
                    $.ajax("dominio/getCentroPoblado.do", {
                        data: {
                            coCentroPoblado: codigo
                        },
                        dataType: "json"
                    }).done(function (data) {
                            if (data.coCentroPoblado) {
                                callback(data);
                            }
                        });
                }
            }
        });

		var menorDatePicker = $('#feNacMenor-date')
			.datepicker()
			.on('changeDate', function (ev) {
				if (ev.viewMode == 'days') {
					menorDatePicker.hide();
					$('#feNacMenor').change();
				}
			})
			.on('show', function(ev){
				var left=menorDatePicker.picker.position().left-$('#feNacMenor').width();
				menorDatePicker.picker.css('left', left);
			})
			.data('datepicker');
		$("#nuDniMenor").inputmask('dni');
		$("#apPrimerMenor").inputmask('apellido');
		$("#apSegundoMenor").inputmask('apellido');
		$("#prenombreMenor").inputmask('nombre');
		$("#feNacMenor").inputmask('dd/mm/yyyy');
		$("#deDireccion").inputmask('direccion');
		$("#coDniJefeFam").inputmask('dni');
		$("#apPrimerJefe").inputmask('apellido');
		$("#apSegundoJefe").inputmask('apellido');
		$("#prenomJefe").inputmask('nombre');
		$("#coDniMadre").inputmask('dni');
		$("#apPrimerMadre").inputmask('apellido');
		$("#apSegundoMadre").inputmask('apellido');
		$("#prenomMadre").inputmask('nombre');
		$('#coDniMadre').keypress(function (event) {
			if (event.which == 13) {
				$('#coDniMadre').blur();
				event.preventDefault();
			}
		});
		$('#coDniJefeFam').keypress(function (event) {
			if (event.which == 13) {
				$('#coDniJefeFam').blur();
				event.preventDefault();
			}
		});
		$('#padronNominal').keypress(function (event) {
			if (event.which == 13) {
				event.preventDefault();
			}
		});
		$('#tiProSocialList').selectpicker();
		//$("#imgFotoMenor").filestyle({classInput: "input-medium", input: true, icon: true, buttonText: ""});
		//$('.info-popover').popover({trigger: 'hover'});

        $('.select2-input').inputmask('direccion');


	},
	seleccionarCoincidenciaMenor: function () {
		var codigoMenor = $('[name=codigoMenorCoincidente]:checked').val();
		if (codigoMenor && codigoMenor != '') {
			$('#regmen-fichaeleccion').submit();
		}
	},
	corregirInformacion:function(){
		$('#ficha-confirmacion').fadeOut(function(){
			$('#formulario-registro').fadeIn();
		});
	},
	mostrarFichaConfirmacion:function(){
		$('#formulario-registro').hide();
		$('#ficha-confirmacion').show();
		$('div.text-error').remove();
		$('p.text-error').remove();
		$('div.alert-error').remove();
		$('.badge').removeClass('badge-important').addClass('badge-info');
	},
	verificarVinculoJefe:function(){
		//if($('#tiVinculoJefe').val()==''){
			$('#coDniJefeFam').val("");
			$('#apPrimerJefe').val("");
			$('#apSegundoJefe').val("");
			$('#prenomJefe').val("");
		//}
	},

	modificarEstadoCamposMadre:function(checkbox) {
		$("#coDniMadre").val("");
		$("#apPrimerMadre").val("");
		$("#apSegundoMadre").val("");
		$("#prenomMadre").val("");
		$("#coGraInstMadre").val("");
		$("#coLenMadre").val("");

		if(checkbox.checked) {
			$('#coDniMadre').prop('disabled', true);
			$('#apPrimerMadre').prop('readonly', false);
			$('#apSegundoMadre').prop('readonly', false);
			$('#prenomMadre').prop('readonly', false);
			$('#apPrimerMadre').focus();

		} else {
			$('#coDniMadre').prop('disabled', false);
			$('#apPrimerMadre').prop('readonly', true);
			$('#apSegundoMadre').prop('readonly', true);
			$('#prenomMadre').prop('readonly', true);
			$('#coDniMadre').focus();
		}
	}
}
