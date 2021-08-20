/**
 *  @jronal
 */
registromanual = {
    formulario_do: {
        afterInit: function () {
            RegistroManual.inicializarFormulario();
            return true;
        }
    },
    guardar_do: {
        afterInit: function () {
            RegistroManual.inicializarFormulario();
            return true;
        }
    }
};


var RegistroManual = {
   /* getSegurosMenor : function(){
        var coPadronNominal =$('#coPadroNominal').val();
        console.log(coPadronNominal);
        $.ajax({
            url: 'registroManual/getSegurosMenor',
            data: {'coPadronNominal': coPadronNominal},
            datatype: 'json',
            method:'post',
            headers: {'X-Requested-With': 'XMLHttpRequest'},
        });
    },*/
    inicializarFormulario: function () {

        var coFuenteDatos = $('#coFuenteDatos').val();
        if(coFuenteDatos == 12){//telemonitoreo
           $('span#span-celular-required').show();
        } else{
            $('span#span-celular-required').hide();
        }

        $('#coFuenteDatos').on('change', function () {
            coFuenteDatos = $(this).val();
           if(coFuenteDatos == 12){ //telemonitoreo
               $('span#span-celular-required').show();
           }else{
               $('span#span-celular-required').hide();
           }
        });

        var beforeSelected = $('#coGraInstMadre').children('option:selected').val();

        $('#coGraInstMadre').each(function() {
            $(this).data('selected', $(this).val()); // back up value
        }).change(function() {
            var afterSelected = $(this).children("option:selected").val();
            if(parseInt(afterSelected)<parseInt(beforeSelected)){
                alert('El grado de instrucción no puede ser inferior al registrado.');
                $(this).val($(this).data('selected'));
            }
            else{
                $(this).data('selected', $(this).val());
            }

        });

        var previousValueTiVinculoJefe;
        $('#tiVinculoJefe').focus(function(e){
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();
            previousValueTiVinculoJefe = $(this).val();
        }).change(function (e) {
            e.preventDefault();
            e.stopPropagation();
            e.stopImmediatePropagation();

            if($('#tiVinculoJefe').val() != "") {
                if (confirm("¿Desea seleccionar otro tipo de vínculo de jefe de familiar?")) {

                    /*if($('#tiVinculoJefe').val() =="1") {//madre
                        $('#apPrimerJefe').val($('#apPrimerMadre').val());
                        $('#apSegundoJefe').val($('#apSegundoMadre').val());
                        $('#prenomJefe').val($('#prenomMadre').val());
                        $('#coDniJefeFam').val('');
                    } else {*/
                        $('#coDniJefeFam').val('');
                        $('#apPrimerJefe').val('');
                        $('#apSegundoJefe').val('');
                        $('#prenomJefe').val('');
                    /*}*/
                }
                else {
                    $(this).val(previousValueTiVinculoJefe)
                }
            }
            else{
                $('#coDniJefeFam').val('');
                $('#apPrimerJefe').val('');
                $('#apSegundoJefe').val('');
                $('#prenomJefe').val('');
            }
            $('#mensaje_busqueda_jefe_familia').html('');
            if ($('#tiVinculoJefe').val() == '') {
                $('#coDniJefeFam').parent().parent().parent().hide();
                $('#apPrimerJefe').parent().parent().hide();
                $('#apSegundoJefe').parent().parent().hide();
                $('#prenomJefe').parent().parent().hide();
            }
            else {
                $('#coDniJefeFam').parent().parent().parent().show();
                $('#apPrimerJefe').parent().parent().show();
                $('#apSegundoJefe').parent().parent().show();
                $('#prenomJefe').parent().parent().show();

                if ($('#tiVinculoJefe').val() == '5') { // OTRO
                    $('#coDniJefeFam').attr("readonly", "readonly");
                    $('#apPrimerJefe').removeAttr("readonly");
                    $('#apSegundoJefe').removeAttr("readonly");
                    $('#prenomJefe').removeAttr("readonly");

                    $('#coDniJefeFam-button').off('click');

                } else {
                    $('#coDniJefeFam-button').on('click', function (e) {
                        e.preventDefault();
                        fnBuscarJefeFamiliaPorDni(e);
                    });
                    $('#coDniJefeFam').removeAttr("readonly");
                    $('#apPrimerJefe').attr("readonly", "readonly");
                    $('#apSegundoJefe').attr("readonly", "readonly");
                    $('#prenomJefe').attr("readonly", "readonly");

                }
            }
        });

        if ($('#tiVinculoJefe').val() == '') {
            $('#coDniJefeFam').parent().parent().parent().hide();
            $('#apPrimerJefe').parent().parent().hide();
            $('#apSegundoJefe').parent().parent().hide();
            $('#prenomJefe').parent().parent().hide();
        }
        else {
            $('#coDniJefeFam').parent().parent().parent().show();
            $('#apPrimerJefe').parent().parent().show();
            $('#apSegundoJefe').parent().parent().show();
            $('#prenomJefe').parent().parent().show();

            if ($('#tiVinculoJefe').val() == '5') { // OTRO
                $('#coDniJefeFam').attr("readonly", "readonly");
                $('#apPrimerJefe').removeAttr("readonly");
                $('#apSegundoJefe').removeAttr("readonly");
                $('#prenomJefe').removeAttr("readonly");

                $('#coDniJefeFam-button').off('click');

            } else {
                $('#coDniJefeFam-button').on('click', function (e) {
                    e.preventDefault();
                    fnBuscarJefeFamiliaPorDni(e);
                });
                $('#coDniJefeFam').removeAttr("readonly");
                $('#apPrimerJefe').attr("readonly", "readonly");
                $('#apSegundoJefe').attr("readonly", "readonly");
                $('#prenomJefe').attr("readonly", "readonly");
            }
        }
        //>>

        var coUbigeoIneiEstSaludSelected = "";

        var $coEstSalud = $("#coEstSalud").select2({
            placeholder: "Buscar por nombre o codigo RENAES",
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
                            { text: "Por codigo RENAES", children: data.lista3},
                            { text: "En el Departamento", children: data.lista1},
                            { text: "Fuera del Departamento", children: data.lista2}
                        ]};
                }
            },
            formatResult: function (item) {
                if (item.text) {
                    return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                } else {
                    var cssStyle = '';
                    if(item.inFueraUbigeo == '1'){
                        cssStyle = 'background-color: #f2dede;color:#b94a48;border-color;#eed3d7;'
                    }
                    $res = $('<div class="title" style="' + cssStyle + '">').append(
                        '<strong>' + item.deEstSalud + '</strong> (Cod. RENAES ' + item.coEstSalud + ')</div><div class="body">' +
                        /*'<div><em>' + item.tiEstSalud + '</em></div>' +*/
                        '<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong>, ' + item.deDireccion + '</div>'
                    );
                    return $res;
                }
            },
            formatSelection: function (item) {
                //return item.deEstSalud + ' (' + item.coEstSalud + ', ' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + ')';
                return item.deEstSalud + ' (' + item.coEstSalud + ')';
            },
            id: function (object) {
                return object.coEstSalud;
            },
            initSelection: function (element, callback) {
                var codigo = $(element).val() + '_' +  $('#nuSecuenciaLocal').val();
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

        /* se modifico mensaje que solo muestre cuando co_est_salud esta fuera del departamento
            Autor: JMB
            fecha: 15-04-2015 */
        $coEstSalud.off('select2-selecting');
        $coEstSalud.on('select2-selecting', function(e) {
            if(!$('#msj_co_est_salud').is(':empty'))
                $('#msj_co_est_salud').html('');
            var coUbigeoRenaes = e.object.coUbigeoInei,
                coUbigeUsuario = e.object.coUbigeoUs;
            if(coUbigeoRenaes != undefined && coUbigeUsuario != undefined && coUbigeoRenaes.length>0 && coUbigeUsuario.length) {
                if(coUbigeoRenaes.substring(0, 2) != coUbigeUsuario.substring(0,2)) {
                    alert('El Establecimiento Salud que selecciono esta fuera del departamento.');
                    $('#msj_co_est_salud').html('El Establecimiento Salud que selecciono esta fuera del departamento.');
                }
            }
            $('#nuSecuenciaLocal').val(e.object.nuSecuenciaLocal);
        });

        /*$coEstSalud.on('change', function(e){
            if(!$('#msj_co_est_salud').is(':empty'))
                $('#msj_co_est_salud').html('');
        })*/

        var $nuCui = $('#nuCui');
        var $nuCnv = $('#nuCnv');
        var $coEstSaludNac = $('#coEstSaludNac');
        var $isPrecarga = $('#isPrecarga');


        if($nuCnv.val().trim().length=='0' || $nuCnv.val().trim()=="" || $nuCnv.val()==null) {
            $coEstSaludNac.select2({
                placeholder: "Buscar por nombre o codigo RENAES",
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
                                { text: "Por codigo RENAES", children: data.lista3},
                                { text: "En el Departamento", children: data.lista1},
                                { text: "Fuera del Departamento", children: data.lista2}
                            ]};
                    }
                },
                formatResult: function (item) {
                    if (item.text) {
                        return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                    } else {
                        var cssStyle = '';
                        if(item.inFueraUbigeo == '1'){
                            cssStyle = 'background-color: #f2dede;color:#b94a48;border-color;#eed3d7;'
                        }
                        $res = $('<div class="title" style="' + cssStyle + '">').append(
                            '<strong>' + item.deEstSalud + '</strong> (Cod. RENAES ' + item.coEstSalud + ')</div><div class="body">' +
                            /*'<div><em>' + item.tiEstSalud + '</em></div>' +*/
                            '<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong>, ' + item.deDireccion + '</div>'
                        );
                        return $res;
                    }
                },
                formatSelection: function (item) {
                    //return item.deEstSalud + ' (' + item.coEstSalud + ', ' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + ')';
                    return item.deEstSalud + ' (' + item.coEstSalud + ')';
                },
                id: function (object) {
                    return object.coEstSalud;
                },
                initSelection: function (element, callback) {
                    var codigo = $(element).val() + '_' + $('#nuSecuenciaLocalNac').val();
                    if (codigo.trim() !== "") {
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
            $coEstSaludNac.off('select2-selecting');
            $coEstSaludNac.on('select2-selecting', function(e) {
                $('#nuSecuenciaLocalNac').val(e.object.nuSecuenciaLocal);
            });
        }
        $coEstSaludNac.off('select2-selecting');
        $coEstSaludNac.on('select2-selecting', function(e) {
            if(!$('#msj_co_est_salud_nac').is(':empty'))
                $('#msj_co_est_salud_nac').html('');
            var coUbigeoRenaes = e.object.coUbigeoInei,
                coUbigeUsuario = e.object.coUbigeoUs;
            if(coUbigeoRenaes != undefined && coUbigeUsuario != undefined && coUbigeoRenaes.length>0 && coUbigeUsuario.length) {
                if(coUbigeoRenaes.substring(0, 2) != coUbigeUsuario.substring(0,2)) {
                    alert('El Establecimiento Salud que selecciono esta fuera del departamento.');
                    $('#msj_co_est_salud_nac').html('El Establecimiento Salud que selecciono esta fuera del departamento.');
                }
            }
            $('#nuSecuenciaLocalNac').val(e.object.nuSecuenciaLocal);
        });




        $coEstSaludAds = $('#coEstSaludAds');
        $isPrecarga = $('#isPrecarga').val().trim();

        /*if($coEstSaludAds.val().trim().length==0 && $isPrecarga.length==0)*/
        if($isPrecarga=="0" || $isPrecarga==""  || $coEstSaludAds.val() == null || $coEstSaludAds.val() == "" ) {
            $coEstSaludAds.select2({
                placeholder: "Buscar por nombre o codigo RENAES",
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
                        return {
                            results: [
                                {text: "Por codigo RENAES", children: data.lista3},
                                {text: "En el Departamento", children: data.lista1},
                                {text: "Fuera del Departamento", children: data.lista2}
                            ]
                        };
                    }
                },
                formatResult: function (item) {
                    if (item.text) {
                        return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                    } else {
                        var cssStyle = '';
                        if (item.inFueraUbigeo == '1') {
                            cssStyle = 'background-color: #f2dede;color:#b94a48;border-color;#eed3d7;'
                        }
                        $res = $('<div class="title" style="' + cssStyle + '">').append(
                            '<strong>' + item.deEstSalud + '</strong> (Cod. RENAES ' + item.coEstSalud + ')</div><div class="body">' +
                            /*'<div><em>' + item.tiEstSalud + '</em></div>' +*/
                            '<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong>, ' + item.deDireccion + '</div>'
                        );
                        return $res;
                    }
                },
                formatSelection: function (item) {
                    //return item.deEstSalud + ' (' + item.coEstSalud + ', ' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + ')';
                    return item.deEstSalud + ' (' + item.coEstSalud + ')';
                },
                id: function (object) {
                    return object.coEstSalud;
                },
                initSelection: function (element, callback) {
                    // console.log($(element).val()+$('#nuSecuenciaLocalAds').val());

                    var codigo = $(element).val() + '_' + $('#nuSecuenciaLocalAds').val();
                    if (codigo.trim() !== "") {
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
            $coEstSaludAds.off('select2-selecting');
            $coEstSaludAds.on('select2-selecting', function (e) {
                $('#nuSecuenciaLocalAds').val(e.object.nuSecuenciaLocal);
            });
        }
        $coEstSaludAds.off('select2-selecting');
        $coEstSaludAds.on('select2-selecting', function(e) {
            if(!$('#msj_co_est_salud_ads').is(':empty'))
                $('#msj_co_est_salud_ads').html('');
            var coUbigeoRenaes = e.object.coUbigeoInei,
                coUbigeUsuario = e.object.coUbigeoUs;
            if(coUbigeoRenaes != undefined && coUbigeUsuario != undefined && coUbigeoRenaes.length>0 && coUbigeUsuario.length) {
                if(coUbigeoRenaes.substring(0, 2) != coUbigeUsuario.substring(0,2)) {
                    alert('El Establecimiento Salud que selecciono esta fuera del departamento.');
                    $('#msj_co_est_salud_ads').html('El Establecimiento Salud que selecciono esta fuera del departamento.');
                }
            }
            $('#nuSecuenciaLocalAds').val(e.object.nuSecuenciaLocal);
        });



        /*---------------------------------------*/
        var $coInstEducativa = $("#coInstEducativa").select2({
            placeholder: "Buscar por nombre o codigo MODULAR",
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
                            { text: "Por codigo modular", children: data.lista3},
                            { text: "En el Departamento", children: data.lista1},
                            { text: "Fuera del Departamento", children: data.lista2}

                        ]};
                }
            },
            formatResult: function (item) {
                if (item.text) {
                    return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                } else {
                    var cssStyle = '';
                    if(item.inFueraUbigeo == '1'){
                        cssStyle = 'background-color: #f2dede;color:#b94a48;border-color;#eed3d7;'
                    }
                    $res = $('<div class="title" style="' + cssStyle + '">').append(
                        '<strong>' + item.noCentroEducativo + '</strong> - '+ item.deNivelEducativo +'(Cod. Modular ' + item.coModular + ')</div> ' +
                        '<div class="body">' +
                        '<div><em>' + item.diCentroEducativo + '</em></div>' +
                        '<div><strong>' + item.deDepartamento + ', ' + item.deProvincia + ', ' + item.deDistrito + '</strong></div>'
                    );
                    return $res;
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

        $coInstEducativa.off('select2-selecting');
        $coInstEducativa.on('select2-selecting', function(e) {
            if(!$('#msj_co_inst_educativa').is(':empty'))
                $('#msj_co_inst_educativa').html('');
            if(e.object.inFueraUbigeo == '1'){
                alert("La Institución Educativa que selecciono esta fuera de su juridicción!");
                $('#msj_co_inst_educativa').html('La Institución Educativa que selecciono esta fuera de su juridicción!');
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
                            { text: "En el Departamento", children: data.lista1},
                            { text: "Fuera del Departamento", children: data.lista2}
                        ]};
                }
            },
            formatResult: function (item) {
                if (item.text) {
                    return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                } else {
                    return "<div><strong>" + item.deDepartamento + ", " + item.deProvincia + ", " + item.deDistrito + "</strong></div>";
                }
            },
            formatSelection: function (item) {
                return '<strong>'+item.deDepartamento + ", " + item.deProvincia + ", " + item.deDistrito+'</strong>';
            },
            id: function (object) {
                return object.coUbigeoInei;
            },
            escapeMarkup: function (m) {
                return m;
            }, // we do not want to escape markup since we are displaying html in results
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
                if (item.text) {
                    return "<div>" + item.text + " (<em>" + item.children.length + " resultados</em>)</div>";
                } else {
                    return "<div><strong>" + item.noCentroPoblado + "</strong></div>";
                }
            },
            formatSelection: function (item) {
                return '<strong>'+item.noCentroPoblado +'</strong>';
            },
            id: function (object) {
                return object.coCentroPoblado;
            },
            escapeMarkup: function (m) {
                return m;
            }, // we do not want to escape markup since we are displaying html in results
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



        var nowTemp = new Date();
        var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);

        var feVisita = new  Date();
        function formatDatePrint(datePrint) {
            var dd = datePrint.getDate(),
                mm = datePrint.getMonth()+1,
                yyyy = datePrint.getFullYear();
            if(dd < 10) {
                dd = '0'+ dd;
            }
            if(mm < 10) {
                mm = '0' + mm;
            }
            return dd + '/' + mm + '/' + yyyy;
        }

        var menorDatePicker = $('#feNacMenor-date')
            .datepicker()
            .on('changeDate', function (ev) {
                if ($('#feVisita').val() !== '' && $('#feVisita').val() != null){
                    var feVisitaParts = $('#feVisita').val().split('/');
                    var feVisitaTemp = new Date(feVisitaParts[2], feVisitaParts[1] - 1, feVisitaParts[0]);
                    feVisita = new Date(feVisitaTemp.getFullYear(), feVisitaTemp.getMonth(), feVisitaTemp.getDate(), 0, 0, 0, 0);
                }

                if (ev.date.valueOf() <= now) {
                    if (ev.date.valueOf() > feVisita && $('#feVisita').val() !== ''){
                        menorDatePicker.hide();
                        var feVisitaPrint = formatDatePrint(feVisita);
                        if ($('#feNacMenor-date').data('date') !== feVisitaPrint){
                            if (document.getElementById('menorVisitOpt1').checked){
                                alert('La fecha de nacimiento no debe ser mayor a la fecha de visita');
                            } else {
                                alert('La fecha de nacimiento no debe ser mayor a la fecha de fuente de datos');
                            }
                        }
                        menorDatePicker.setValue(feVisita);
                        $('#feNacMenor').val('');
                    }else {
                        if (ev.viewMode == 'days') {
                            menorDatePicker.hide();
                            //$('#feNacMenor').ev.date.valueOf();
                            $('#feNacMenor').val($('#feNacMenor-date').data('date'));
                            //$('#feNacMenor').change();
                        }
                    }
                }
                else {
                    menorDatePicker.show();
                    //$('#feNacMenor').val('');
                }
            })
            .on('show', function (ev) {
                var left = menorDatePicker.picker.position().left - $('#feNacMenor').width();
                menorDatePicker.picker.css('left', left);
            })
            .data('datepicker');

        $('#feNacMenor').blur(function () {
            var existeFeNac = true;
            if($('#feNacMenor').val().trim().length < 10){ $('#feNacMenor').val('');}
            if ($('#feNacMenor').val()==='' || $('#feNacMenor').val()== null ) {existeFeNac = false}
            if (existeFeNac === true && document.getElementById('menorVisitOpt1').checked){
                var feNacParts = $('#feNacMenor').val().split('/');
                var feNacChangeTemp = new Date(feNacParts[2], feNacParts[1] - 1, feNacParts[0]),
                    feNacChange = new Date(feNacChangeTemp.getFullYear(), feNacChangeTemp.getMonth(), feNacChangeTemp.getDate(), 0, 0, 0, 0);

                var feVisitaParts = $('#feVisita').val().split('/');
                var feVisitaTemp = new Date(feVisitaParts[2], feVisitaParts[1] - 1, feVisitaParts[0]);
                feVisita = new Date(feVisitaTemp.getFullYear(), feVisitaTemp.getMonth(), feVisitaTemp.getDate(), 0, 0, 0, 0);

                if (feNacChange > feVisita && $('#feVisita').val() !== ''){
                    menorDatePicker.hide();
                    menorDatePicker.setValue(feVisita);
                    alert('La fecha de nacimiento no debe ser mayor a la fecha de visita / fuente de datos');
                    $('#feNacMenor').val('');
                }
            }
        });

        $("#nuDniMenor")
        /*.on('change', function(){
         var str = $(this).val();
         $(this).val(str.replace(/\s+/g,' '));
         })*/
            .inputmask('dni');
        $('#nuCui').inputmask('dni');

        $("#apPrimerMenor")
        /*.on('change', function(){
         var str = $(this).val();
         $(this).val(str.replace(/\s+/g,' '));
         })*/
            .inputmask('apellido');
        $("#apSegundoMenor")
        /*.on('change', function(){
         var str = $(this).val();
         $(this).val(str.replace(/\s+/g,' '));
         })*/
            .inputmask('apellido');
        $("#prenombreMenor")
        /*.on('change', function(){
         var str = $(this).val();
         $(this).val(str.replace(/\s+/g,' '));
         })*/
            .inputmask('nombre');

        $("#feNacMenor")
            .inputmask('dd/mm/yyyy')

        /*.keyup(function (e) {
            if ($.isNumeric($(this).val()[$(this).val().length - 1])) {
                var nowTemp = new Date()
                    , now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0)
                    , tmp = $(this).val().split('/')
                    , dateInput = new Date(parseInt(tmp[2]), parseInt(tmp[1]), parseInt(tmp[0]), 0, 0, 0, 0);

                if (dateInput > now) {
                    $(this).val('');
                }
            }
        });*/

        $("#deDireccion")
        /*.inputmask('Regex', { regex: "[a-zA-Z0-9._%-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,4}" })*/
            .inputmask('direccion')
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
        ;
        $("#coDniJefeFam")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('dni');
        $("#apPrimerJefe")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('apellido');
        $("#apSegundoJefe")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('apellido');
        $("#prenomJefe")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('nombre');
        $("#coDniMadre")
            .inputmask('dni');
        $("#apPrimerMadre")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('apellido');
        $("#apSegundoMadre")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('apellido');
        $("#prenomMadre")
            .on('change', function () {
                var str = $(this).val();
                $(this).val(str.replace(/\s+/g, ' '));
            })
            .inputmask('nombre');
        $('#coDniMadre')
            .keypress(function (event) {
                if (event.which == 13) {
                    $('#coDniMadre').blur();
                    event.preventDefault();
                }
            });
        $('#coDniJefeFam')
            .keypress(function (event) {
                if (event.which == 13) {
                    $('#coDniJefeFam').blur();
                    event.preventDefault();
                }
            });
        $('#padronNominal')
            .keypress(function (event) {
                if (event.which == 13) {
                    event.preventDefault();
                }
            });

        $('#deRefDir').inputmask('direccion');


    /*************************************************************************************
     * Logica de tipo de Seguros
    /*************************************************************************************/
    $isSusalud = $('#isSusalud').val();
    /*$isTipoSeguro = $('#isTipoSeguro').val();*/
    $seguroMenor = $('select#tiSeguroMenor');

    $seguroMenor.S2018({
        placeholder: 'Seleccionar uno o más tipos de seguro...',
        maximumSelectionLength: 8,
        allowClear: true
    });

    $isTipoSeguro = $('#isTipoSeguro').val();

    /*if($isSusalud == '1' && $isTipoSeguro=='1') {*/
    if($isSusalud == '1') {
        $seguroMenorAux = $('select#tiSeguroMenorAux');
        $seguroMenorAux.S2018({
            placeholder: 'Seleccionar uno o más tipos de seguro...',
            maximumSelectionLength: 8,
            allowClear: true
        });
        if (!!$seguroMenor.val()) {
            var nuSeguros = $seguroMenor.val().length;
            for (var i=nuSeguros-1 ; i>=0; i--) {
                var tiSeguro = $seguroMenor.next('.S2018').find('li')[i].title;
                $seguroMenorAux.next('.S2018').find('ul').prepend('<li class="S2018-selection__choice" title="'+ tiSeguro +'">'+tiSeguro +'</li>');
            }
            if (nuSeguros > 0) {
                $seguroMenorAux.next('.S2018').find('ul').prepend('<span class="S2018-selection__clear">×</span>')
            }
        }
        $seguroMenor.next('.S2018').hide();
        $seguroMenorAux.prop("disabled", true);
        $seguroMenorAux.next('.S2018').find('input').css('display', 'none');
        $('label[for=tiSeguroMenor]')
            .find('span')
            .remove();
    }

    $seguroMenor.on('S2018:select', function (e) {
        var data = e.params.data;
        if (data.selected && data.id == '0') {
            $seguroMenor.val(null).trigger('change');
            $seguroMenor.val('0').trigger('change');
        }
        if (data.selected && data.id != '0') {
            var i, currentSelection;
            var listSelected = $seguroMenor.S2018('data');

            if (listSelected.length > 1) {
                for (i = 0; i < listSelected.length; i++) {
                    currentSelection = listSelected[i];
                    if (currentSelection.id == '0') {
                        $seguroMenor.val(null).trigger('change');
                        $seguroMenor.val(data.id).trigger('change');
                    }
                }
            }
        }
    });
    /*************************************************************************************/

    /*************************************************************************************
     * Logica de tipo de Seguros
    /*************************************************************************************/
    $programaSocial = $('select#tiProSocial');

    $programaSocial.S2018({
        placeholder:'Seleccionar uno o más Programas Sociales...',
        maximumSelectionLength: 8,//without default option(NINGUNO)
        allowClear: true
    });

    $programaSocial.on('S2018:select', function (e) {
            var data = e.params.data;
            if(data.selected && data.id=='0'){ //ningun programa social
                $programaSocial.val(null).trigger('change');// se anula todo
                $programaSocial.val('0').trigger('change'); // Select the option with a value of '1'
            }
            if(data.selected && data.id!='0'){//cualquier opcion, menos ningun programa social
                var i, currentSelection;
                var listSelected = $programaSocial.S2018('data');

                if(listSelected.length>1){
                    for(i=0; i<listSelected.length; i++){
                        currentSelection=listSelected[i];
                        if(currentSelection.id=='0'){
                            $programaSocial.val(null).trigger('change');//limpiamos
                            $programaSocial.val(data.id).trigger('change'); // Select the option with a value of '1'
                        }
                    }
                }
            }
    });



    $("#imgFotoMenor")
        .on('change', function (e) {
            var $imgFotoMenor = $(this)
            extFile = $imgFotoMenor.val().split('.').pop().toLowerCase(),
                extenciones = ['jpeg', 'jpg', 'gif', 'png'];
            if ($.inArray(extFile, extenciones) == -1) {
                $imgFotoMenor.val('');
                $('.bootstrap-filestyle').find('input').val('');
            }
        })
        .filestyle({classInput: "input-medium", input: true, icon: true, buttonText: ""});


    $('.info-popover')
        .popover({trigger: 'hover'});

    var mostrarEjeVial=function(deAreaCcpp){
        if(deAreaCcpp == 'URBANA'){
            $('#eje_vial').removeClass('hide');
        } else {
            $('#eje_vial').addClass('hide');
            /*$('#deRefDirWrap').addClass('hide');*/
        }
        $('#deRefDirWrap').removeClass('hide');
    }
    $("#coUbigeoInei").on('select2-open', function(){
            $('.select2-input').inputmask('ubigeo');
    });

    $("#coUbigeoInei").on('select2-removed', function(){
        mostrarEjeVial('');
        $('#deAreaCcpp').val('');
        $("#coCentroPoblado").select2("val", "");
        $('#coVia').select2("val", "");
        $('#deDireccion').val('');
        //$('#coMenorEncontrado').val('');
    });

        // si busca nuevo distrito por defecto se toma el primer centro poblado que es el mismos distrito CO_CCPP_INEI=0001
    $("#coUbigeoInei").on('select2-selecting', function(e){
            $.ajax("dominio/getCentroPoblado.do", {
                data: {
                    coCentroPoblado: e.val + '0001'
                },
                dataType: "json"
            }).done(function (data) {
                if (data.coCentroPoblado && e.val.substring(0,$("#coUbigeoPad").val().length) === $("#coUbigeoPad").val()) {
                    // callback(data);
                    $('#deAreaCcpp').val(data.deAreaCcpp);
                    $("#coCentroPoblado").select2("data", data);
                    mostrarEjeVial(data.deAreaCcpp);
                    $('#deDireccion').val('');
                    $('#deRefDir').val('');
                    $('#coVia').select2("val", "");
                }
            });
    });

    $('#coCentroPoblado').on('select2-open', function(){
        $('.select2-input').inputmask('ubigeo');
    });

    // evento de seleccion de elemento
    $('#coCentroPoblado').on('select2-selecting', function(e) {
        var deAreaCcpp = e.object.deAreaCcpp;
        $('#deAreaCcpp').val(deAreaCcpp);
        $('#deDireccion').val('');
        $('#deRefDir').val('');
        $('#coVia').select2("val", "");
        mostrarEjeVial(deAreaCcpp);
    });

    // evento de eliminacion de seleccion
    $('#coCentroPoblado').on('select2-removed', function(e){
        $('#deAreaCcpp').val('');
        $('#coVia').select2("val", "");
        $('#deDireccion').val('');
        mostrarEjeVial('');
    });

    $('#coEstSalud').on('select2-open', function(){
        $('.select2-input').inputmask('select2-input');
    });

    $('#coEstSaludNac').on('select2-open', function(){
        $('.select2-input').inputmask('select2-input');
    });

    $('#coInstEducativa').on('select2-open', function(){
        $('.select2-input').inputmask('select2-input');
    });

    $('#coVia').select2({
            placeholder: "Buscar Eje Vial: JIRON, CALLE, AVENIDA, PASAJE, CARRETERA, OTROS",
            minimumInputLength: 2,
            allowClear: true,
            ajax: {
                url: "dominio/buscarEjeVial.do",
                dataType: 'json',
                type: 'post',
                quietMillis: 250,
                data: function (term, page) { // page is the one-based page number tracked by Select2
                    return {
                        parteDeVia: term, //search term
                        nuPagina: page, // page number
                        coCentroPoblado:$('#coCentroPoblado').val()
                    };
                },
                results: function (data, page) {
                    var more = (page * 30) < data.total_count; // whether or not there are more results available
                    // notice we return the value of more so Select2 knows if more results can be loaded
                    return {results: data.items, more: more};
                }
            },
            formatSelection: function (item) {
                return "<strong>" + item.deVia + "</strong>";
            },
            formatResult: function (item) {
                return '<strong>' + item.deVia + '</strong>';
            },
            id: function (object) {
                return object.coVia;
            },
            escapeMarkup: function (m) {
                return m;
            }, // we do not want to escape markup since we are displaying html in results
            initSelection: function (element, callback) {
                var codigo = $(element).val();
                if (codigo !== "") {
                    $.ajax("dominio/obtenerEjeVial.do", {
                        data: {
                            coVia: codigo
                        },
                        dataType: "json"
                    }).done(function (data) {
                        if (data.coVia) {
                            callback(data);
                        }
                    });
                }
            }
    });

    var fnBuscarJefeFamiliaPorDni = function (e) {

        var esDocumentada = $('#' + 'noDniMadre').is(":checked");
        var coDniMadre = $('#coDniMadre').val();
        var coDniJefeFam = $('#coDniJefeFam').val();
        var vinculoFamiliar = $('#tiVinculoJefe').val();


        if(esDocumentada === false && vinculoFamiliar=='1' && coDniMadre!='' && coDniMadre!=coDniJefeFam){
            $('#coDniMadre').css('background-color','#faffbd');
            $('#coDniJefeFam').css('background-color','#faffbd');

            $('#apPrimerJefe').val('');
            $('#apSegundoJefe').val('');
            $('#prenomJefe').val('');

            alert('El numero de DNI no coincide con el de la madre');
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        }
        else{
            $('#coDniMadre').css('background-color','#ffffff');
            $('#coDniJefeFam').css('background-color','#ffffff');
        }

        var $form = $('<form action="registromanual/buscarJefeFamiliaPorDni.do">')
            .append($('<input name="tiVinculoJefe">').val($('#tiVinculoJefe').val()))
            .append($('<input name="coDniJefeFam">').val($('#coDniJefeFam').val()));

        Util.enviarFormTarget($form, '#regman-form-message-jefe');
        /*$.ajax({
            type: "GET",
            url: 'registromanual/buscarJefeFamiliaPorDni.do',
            /!*dataType: 'text/html',*!/
            data: {
                tiVinculoJefe: $('#tiVinculoJefe').val(),
                coDniJefeFam: $('#coDniJefeFam').val()

            },
            success: function (response) {
                $('#regman-form-message-jefe').html(response);
            },
            error: function () {
            },
            beforeSend: function () {
                $.blockUI();
            },
            complete: function () {
                $.unblockUI();
            }
        });*/

    }

    //registromanual/buscarJefeFamiliaPorDni.do#regman-form-message-jefe
    $('#coDniJefeFam-button').on('click', function (e) {
        e.preventDefault();
        fnBuscarJefeFamiliaPorDni(e);
    });
    $('#coDniJefeFam-button').on('keypress', function (e) {
        e.preventDefault();
        if (e.keyCode == '13')
            fnBuscarJefeFamiliaPorDni(e);
    });
    $('#coDniJefeFam').on('keypress', function (e) {
        e.preventDefault();
        if (e.keyCode == '13')
            fnBuscarJefeFamiliaPorDni(e);
    });



    /*BUSQUEDA DE LA MADRE*/
    var fnBuscarMadrePorDni = function (e) {
        var esDocumentada = $('#' + 'noDniMadre').is(":checked");
        var coDniMadre = $('#coDniMadre').val();
        var coDniJefeFam = $('#coDniJefeFam').val();
        var vinculoFamiliar = $('#tiVinculoJefe').val();

        if(esDocumentada===false && vinculoFamiliar=='1' && coDniJefeFam!='' && coDniMadre!=coDniJefeFam){
            $('#coDniMadre').css('background-color','#faffbd');
            $('#coDniJefeFam').css('background-color','#faffbd');

            alert('El numero de DNI no coincide con el de la madre');
            e.preventDefault();
            e.stopImmediatePropagation();
            return false;
        }
        else{
            $('#coDniMadre').css('background-color','#ffffff');
            $('#coDniJefeFam').css('background-color','#ffffff');
        }

        var $form = $('<form action="registromanual/buscarMadreApoderadoPorDni.do">')
            .append($('<input name="coDniMadre">').val($('#coDniMadre').val()));

        Util.enviarFormTarget($form, '#regman-form-message-madre');
    }
    //registromanual/buscarMadreApoderadoPorDni.do#regman-form-message-madre
    $('#coDniMadre-button').on('click', function (e) {
        e.preventDefault();
        fnBuscarMadrePorDni(e);
    });
    $('#coDniMadre-button').on('keypress', function (e) {
        e.preventDefault();
        if (e.keyCode == '13')
            fnBuscarMadrePorDni(e);
    });
    $('#coDniMadre').on('keypress', function (e) {
        e.preventDefault();
        if (e.keyCode == '13')
            fnBuscarMadrePorDni(e);
    });

    var menorVisitOpt1 = document.getElementById("menorVisitOpt1");
    if (menorVisitOpt1.checked) {
        RegistroManual.checkMenorVisitado(menorVisitOpt1);
    } else {
        RegistroManual.checkMenorVisitado(document.getElementById("menorVisitOpt2"));
    }
    },
    seleccionarCoincidenciaMenor: function () {
        var codigoMenor = $('[name=codigoMenorCoincidente]:checked').val();
        if (codigoMenor && codigoMenor != '') {
            $('#regmen-fichaeleccion').submit();
        }
    },
    corregirInformacion: function () {
        $('#ficha-confirmacion').fadeOut(function () {
            $('#formulario-registro').fadeIn();
        });
    },
    mostrarFichaConfirmacion: function () {
        $('#formulario-registro').hide();
        $('#ficha-confirmacion').show();
        $('div.text-error').remove();
        $('p.text-error').remove();
        $('div.alert-error').remove();
        $('.badge').removeClass('badge-important').addClass('badge-info');
    },
    modificarEstadoCamposMadre: function (checkbox) {
        $("#coDniMadre").val("");
        $("#apPrimerMadre").val("");
        $("#apSegundoMadre").val("");
        $("#prenomMadre").val("");
        $("#coGraInstMadre").val("");
        $("#coLenMadre").val("");
        $("#nuCelMadre").val("");
        $("#diCorreoMadre").val("");

        var spanRequerido = $(document.createElement('span'))
            .addClass('text-error required')
            .text('*')
            .attr('title', 'requerido');

        if (checkbox.checked) {

            $('#mensaje_busqueda_madre').html('');
            $('#coDniMadre').prop('disabled', true);
            $('#apPrimerMadre').prop('readonly', false);
            $('#apSegundoMadre').prop('readonly', false);
            $('#prenomMadre').prop('readonly', false);
            $('#apPrimerMadre').focus();

            /* $('.label-grado-instruccion').html('');
             $('.label-lengua-habitual').html('');*/

            $('label[for=apPrimerMadre]')
                .find('span')
                .remove();
            $('label[for=apPrimerMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=apSegundoMadre]')
                .find('span')
                .remove();
            $('label[for=apSegundoMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=prenomMadre]')
                .find('span')
                .remove();
            $('label[for=prenomMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=coGraInstMadre]')
                .find('span')
                .remove();

            $('label[for=coGraInstMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=coLenMadre]')
                .find('span')
                .remove();
            $('label[for=coLenMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));
        } else {
            $('#coDniMadre').prop('disabled', false);
            $('#apPrimerMadre').prop('readonly', true);
            $('#apSegundoMadre').prop('readonly', true);
            $('#prenomMadre').prop('readonly', true);
            $('#coDniMadre').focus();


            $('label[for=apPrimerMadre]')
                .find('span')
                .remove();

            $('label[for=apSegundoMadre]')
                .find('span')
                .remove();

            $('label[for=prenomMadre]')
                .find('span')
                .remove();

            $('label[for=coGraInstMadre]')
                .find('span')
                .remove();

            $('label[for=coGraInstMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=coLenMadre]')
                .find('span')
                .remove();
            $('label[for=coLenMadre]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            /*$('.label-grado-instruccion').html(
             $(document.createElement('span'))
             .text('*')
             .addClass('text-error required')
             .attr({title: 'requerido'})
             );
             $('.label-lengua-habitual').html(
             $(document.createElement('span'))
             .text('*')
             .addClass('text-error required')
             .attr({title: 'requerido'})
             );*/
        }
    },
    modificarMenorVisitado: function (checkbox) {
        $("#feVisita").val("");
        $("#feUltimaTomaDatos").val("");
        $("#coMenorEncontrado").val("");
        if ($('#existenDatosForm').val() === 'false'){
            $("#coMenorEncontrado").val("1");
        }
        $('#coMenorEncontrado').removeAttr("disabled");
        $('#tab2Formulario').show();
        $('#tab3Formulario').show();
        $('#tab1-form-next').show();
        $('#visita-form-saveButton').hide();
        $('#feVisitaGroup').removeClass('error');
        $('#feUltimaTomaDatosGroup').removeClass('error');
        $('#feVisitaGroup div.text-error').text('');
        $('#feUltimaTomaDatosGroup div.text-error').text('');
        RegistroManual.checkMenorVisitado(checkbox);
    },
    checkMenorVisitado: function (checkbox) {
        function formatDatePrint(datePrint) {
            var dd = datePrint.getDate(),
                mm = datePrint.getMonth()+1,
                yyyy = datePrint.getFullYear();
            if(dd < 10) {
                dd = '0'+ dd;
            }
            if(mm < 10) {
                mm = '0' + mm;
            }
            return dd + '/' + mm + '/' + yyyy;
        }
        $('#mensajeFechaVisita').hide();
        if (checkbox.value === '1') {

            document.getElementById('section-menor-visitado').style.display = "block";
            document.getElementById('section-feVisita-before').style.display = "block";
            document.getElementById('section-feUltimaTomaDatos-before').style.display = "none";
            document.getElementById('section-fuente-datos').style.display = "none";
            $('#feVisita').removeAttr("readonly");
            $('#feVisitaButton').removeAttr("readonly");
            $('#feUltimaTomaDatos').attr("readonly", "readonly");
            $('#feUltimaTomaDatosButton').attr("readonly", "readonly");
            // $('#feVisita').focus();

            $('#feUltimaTomaDatos').val($('#feUltimaTomaDatosBefore').val());

            $('label[for=feVisita]')
                .find('span')
                .remove();
            $('label[for=feVisita]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=feUltimaTomaDatos]')
                .find('span')
                .remove();

            $('label[for=coMenorEncontrado]')
                .find('span')
                .remove();
            $('label[for=coMenorEncontrado]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            if (($('#inMenorVisitadoBefore').val()==='' || $('#inMenorVisitadoBefore').val()== null) && $('#existeError').val()==='false' &&
                ($('#feVisita').val() ==null || $('#feVisita').val()=== '') &&($('#feVisitaBefore').val()==null || $('#feVisitaBefore').val()==='') &&
                ($('#feUltimaTomaDatosBefore').val()==null || $('#feUltimaTomaDatosBefore').val()==='')){
                $('#feVisita').val(formatDatePrint(new Date()));
            }
            if (($('#inMenorVisitadoBefore').val()==='' || $('#inMenorVisitadoBefore').val()== null)&&($('#feVisitaBefore').val()==null || $('#feVisitaBefore').val()==='') &&
                ($('#feUltimaTomaDatosBefore').val()==null || $('#feUltimaTomaDatosBefore').val()==='')) {
                $('#mensajeFechaVisita').show();
            }
        } else {
            document.getElementById('section-menor-visitado').style.display = "none";
            document.getElementById('section-feVisita-before').style.display = "none";
            document.getElementById('section-feUltimaTomaDatos-before').style.display = "block";
            document.getElementById('section-fuente-datos').style.display = "block";
            $('#feVisita').attr("readonly", "readonly");
            $('#feVisitaButton').attr("readonly", "readonly");
            $('#feUltimaTomaDatos').removeAttr("readonly");
            $('#feUltimaTomaDatosButton').removeAttr("readonly");
            // $('#feVisita').focus();

            $('#feVisita').val($('#feVisitaBefore').val());
            if ($('#coUbigeoInei').val() === $('#coUbigeoPad').val() && $('#coUbigeoPad').val().length===6) {
                $('#coUbigeoInei').attr("readonly", "readonly");
            } else {
                $('#coUbigeoInei').removeAttr("readonly");
            }
            $('#coCentroPoblado').removeAttr("readonly");
            $('#coVia').removeAttr("readonly");
            $('#deDireccion').removeAttr("readonly");
            $('#deRefDir').removeAttr("readonly");

            $('label[for=feUltimaTomaDatos]')
                .find('span')
                .remove();
            $('label[for=feUltimaTomaDatos]').append(
                $(document.createElement('span'))
                    .addClass('text-error required')
                    .text('*')
                    .attr('title', 'requerido'));

            $('label[for=feVisita]')
                .find('span')
                .remove();

            $('label[for=coMenorEncontrado]')
                .find('span')
                .remove();

            /*if (($('#inMenorVisitadoBefore').val() === '0'||$('#inMenorVisitadoBefore').val() === ''||$('#inMenorVisitadoBefore').val() ==null) && $('#existeError').val() === 'false' &&
                ($('#feUltimaTomaDatos').val() ==null || $('#feUltimaTomaDatos').val()=== '') && ($('#feUltimaTomaDatosBefore').val()!=null && $('#feUltimaTomaDatosBefore').val()!=='')) {
                $('#feUltimaTomaDatos').val($('#feUltimaTomaDatosBefore').val());
            }*/
            // if ($('#inMenorVisitadoBefore').val()==='' || $('#inMenorVisitadoBefore').val()== null) {
                $('#mensajeFechaVisita').hide();
            // }
        }
    },
    validarMenorVisita: function () {
        var menorVisitOpt1 = document.getElementById("menorVisitOpt1");
        var existeError = $('#existeError').val();
        var coUbigeoInei = $('#coUbigeoInei');
        var coUbigeoPad = $('#coUbigeoPad');
        var coCentroPoblado = $('#coCentroPoblado');
        var deDireccion = $('#deDireccion');
        var coMenorEncontrado = $('#coMenorEncontrado');
        var classCoCentroPoblado = $('#fieldCentroPoblado')[0].className;
        var classDireccion = $('#fieldDireccion')[0].className;
        var tab1Formulario = $('#tab1Formulario');
        var tab2Formulario = $('#tab2Formulario');
        var tab3Formulario = $('#tab3Formulario');
        var buttonNextTab1 = $('#tab1-form-next');
        var buttonSaveVisita = $('#visita-form-saveButton');
        var lengthUbigeo;
        
        if (menorVisitOpt1.checked) {
            lengthUbigeo = coUbigeoPad.val().length;
            if (lengthUbigeo > coUbigeoInei.val().length) {lengthUbigeo = coUbigeoInei.val().length}
            if (existeError === 'false'){
                if (coUbigeoInei.val().substring(0,lengthUbigeo) === coUbigeoPad.val()) {
                    if (coUbigeoPad.val().length===6) coUbigeoInei.attr("readonly", "readonly");
                    if (coMenorEncontrado.val()=== '0') {
                        disabledAndEnableButtons(true);
                        disabledField();
                    } else {
                        disabledAndEnableButtons(false);
                        enabledField();
                    }
                } else {
                    /*if (coMenorEncontrado.val() === '1') {
                        disabledAndEnableButtons(true);
                        coUbigeoInei.attr("readonly", "readonly");
                        coMenorEncontrado.attr("disabled", "disabled");
                        buttonSaveVisita.hide();
                        disabledField();
                    } else {*/
                        disabledAndEnableButtons(false);
                        coUbigeoInei.removeAttr("readonly");
                        enabledField();
                    //}
                }
            } else {
                if (coMenorEncontrado.val()=== '0') {
                    disabledAndEnableButtons(true);
                    disabledField();
                    if ((classCoCentroPoblado.indexOf('error')> -1) || (classDireccion.indexOf('error')>-1)){
                        enabledField();
                    }
                } else {
                    disabledAndEnableButtons(false);
                    enabledField();
                }
                if (coUbigeoInei.val().substring(0, lengthUbigeo) === coUbigeoPad.val()) {
                    if (coUbigeoPad.val().length===6) coUbigeoInei.attr("readonly", "readonly");
                } else {
                    coUbigeoInei.removeAttr("readonly");
                    if (coMenorEncontrado.val()=== '0') {
                        disabledAndEnableButtons(false);
                        enabledField();}
                }
            }
        } else {
            lengthUbigeo = coUbigeoPad.val().length;
            if (lengthUbigeo > coUbigeoInei.val().length) {lengthUbigeo = coUbigeoInei.val().length}
            if (coUbigeoInei.val().substring(0,lengthUbigeo) === coUbigeoPad.val()) {
                if (coUbigeoPad.val().length===6) coUbigeoInei.attr("readonly", "readonly");
            } else {
                coUbigeoInei.removeAttr("readonly");
            }
            disabledAndEnableButtons(false);
            enabledField();
        }

        /*if (existeError ==='true' && coMenorEncontrado.val() === '0' && coUbigeoInei.val() === coUbigeoPad.val()){
            //Informacion adicional del niño
            $('#coEstSalud').val('');
            $('#tiSeguroMenor').val('');
            $('#tiProSocial').val('');

            if (document.getElementById('noDniMadre') !== null){
                if (document.getElementById('noDniMadre').checked) {
                    $('#apPrimerMadre').val('');
                    $('#apSegundoMadre').val('');
                    $('#prenomMadre').val('');
                } else {
                    $('#coDniMadre').val('');
                }
            }

            $('#coGraInstMadre').val('');
            $('#coLenMadre').val('');
        }*/

        coMenorEncontrado.on('change', function () {
            validarMenorEncontrado();
        });

        coUbigeoInei.on('change', function (e) {
            lengthUbigeo = coUbigeoPad.val().length;
            if (lengthUbigeo > coUbigeoInei.val().length) {lengthUbigeo = coUbigeoInei.val().length}
            //coMenorEncontrado.val('');
            if (coUbigeoInei.val().substring(0, lengthUbigeo) !== coUbigeoPad.val() && coUbigeoInei.val()!== '') {
                    alert('Por favor elija un ubigeo de su jurisdicción');
                    $('#coUbigeoInei').select2("val", "");
                    $('#eje_vial').addClass('hide');
                /*    $('#deRefDirWrap').addClass('hide');*/
                    $('#deAreaCcpp').val('');
                    $("#coCentroPoblado").select2("val", "");
                    $('#coVia').select2("val", "");
                    $('#deDireccion').val('');
            } /*else {
                if (document.getElementById('menorVisitOpt1').checked){
                    alert('A continuación elija la fecha de visita y menor encontrado');
                }
            }*/
            validarMenorEncontrado();
            enabledField();
        });

        function validarMenorEncontrado(){
            lengthUbigeo = coUbigeoPad.val().length;
            if (lengthUbigeo > coUbigeoInei.val().length) {lengthUbigeo = coUbigeoInei.val().length}
            
            if (coMenorEncontrado.val()=== '0') {
                disabledAndEnableButtons(true);
                if (coUbigeoInei.val().substring(0, lengthUbigeo) === coUbigeoPad.val()){
                    if (coUbigeoPad.val().length===6) $('#coUbigeoInei').attr("readonly", "readonly");
                    enabledField();
                    if (coCentroPoblado.val() !=null && coCentroPoblado.val()!=='' && deDireccion.val() !=null && deDireccion.val()!==''){
                        disabledField();
                    }
                } else {
                    coUbigeoInei.removeAttr("readonly");
                    enabledField();
                    disabledAndEnableButtons(false);
                }
            } else {
                disabledAndEnableButtons(false);
                enabledField();
                coUbigeoInei.removeAttr("readonly");
                if (coUbigeoInei.val() === coUbigeoPad.val()){
                    coUbigeoInei.attr("readonly", "readonly");
                }
            }
        }

        buttonSaveVisita.on('click', function () {
            /*//Informacion adicional del niño
            if ($('#coEstSalud').val()==null || $('#coEstSalud').val() ===''){$('#coEstSalud').val('11111111');}
            if ($('#tiSeguroMenor').val()==null || $('#tiSeguroMenor').val() ===''){$('#tiSeguroMenor').val('0');}
            if ($('#tiProSocial').val()==null || $('#tiProSocial').val() ===''){$('#tiProSocial').val('0');}

            //Informacion del apoderado
            if ($('#tiVinculoJefe').val()!=null && $('#tiVinculoJefe').val() !==''){
                if ($('#apPrimerJefe').val()==null || $('#apPrimerJefe').val()==='' ||
                    $('#apSegundoJefe').val()==null || $('#apSegundoJefe').val()==='' ||
                    $('#prenomJefe').val()==null || $('#prenomJefe').val()===''){
                    $('#tiVinculoJefe').val('');
                    $('#coDniJefeFam').val('');
                    $('#apPrimerJefe').val('');
                    $('#apSegundoJefe').val('');
                    $('#prenomJefe').val('');
                }
            }

            if (document.getElementById('noDniMadre') !== null){
                if (document.getElementById('noDniMadre').checked) {
                    if ($('#apPrimerMadre').val()==null || $('#apPrimerMadre').val() ===''){$('#apPrimerMadre').val($('#apSegundoMenor').val());}
                    if ($('#apSegundoMadre').val()==null || $('#apSegundoMadre').val() ===''){$('#apSegundoMadre').val('abc');}
                    if ($('#prenomMadre').val()==null || $('#prenomMadre').val() ===''){$('#prenomMadre').val('abc');}
                } else {
                    if ($('#coDniMadre').val()==null || $('#coDniMadre').val() ===''){$('#coDniMadre').val('12345678');}
                }
            }

            if ($('#coGraInstMadre').val()==null || $('#coGraInstMadre').val() ===''){$('#coGraInstMadre').val('37');}
            if ($('#coLenMadre').val()==null || $('#coLenMadre').val() ===''){$('#coLenMadre').val('1');}
            */
            $('#padronNominal').submit();
        });

        function disabledAndEnableButtons(enviar) {
            if (enviar){
                tab2Formulario.hide();
                tab3Formulario.hide();
                buttonNextTab1.hide();
                buttonSaveVisita.show();
            } else {
                tab2Formulario.show();
                tab3Formulario.show();
                buttonNextTab1.show();
                buttonSaveVisita.hide();
            }
        }

        function disabledField() {
            $('#coCentroPoblado').attr("readonly", "readonly");
            $('#coVia').attr("readonly", "readonly");
            $('#deDireccion').attr("readonly", "readonly");
            $('#deRefDir').attr("readonly", "readonly");
        }
        function enabledField() {
            $('#coCentroPoblado').removeAttr("readonly");
            $('#coVia').removeAttr("readonly");
            $('#deDireccion').removeAttr("readonly");
            $('#deRefDir').removeAttr("readonly");
        }
    },
    setDefaultCentroPoblado: function (coUbigeoUsuario) {
        if (!(coUbigeoUsuario == null || coUbigeoUsuario === '')){
            $('label[for=coUbigeoInei]')
                .find('span')
                .remove();
            $.ajax("dominio/getCentroPoblado.do", {
                data: {
                    coCentroPoblado: coUbigeoUsuario + '0001'
                },
                dataType: "json"
            }).done(function (data) {
                if (data.coCentroPoblado) {
                    // callback(data);
                    $('#deAreaCcpp').val(data.deAreaCcpp);
                    $("#coCentroPoblado").select2("data", data);
                    if(data.deAreaCcpp == 'URBANA'){
                        $('#eje_vial').removeClass('hide');
                    } else {
                        $('#eje_vial').addClass('hide');
                        /*$('#deRefDirWrap').addClass('hide');*/
                    }
                    $('#deRefDirWrap').removeClass('hide');
                    $('#deDireccion').val('');
                    $('#deRefDir').val('');
                    $('#coVia').select2("val", "");
                }
            });
        }
    }

}
