Util={
    url_base:"",
    dataType:"json",
    ordinal:["PRIMERA","SEGUNDA","TERCERA","CUARTA","QUINTA","SEXTA","S&Eacute;PTIMA","OCTAVA","NOVENA","D&Eacute;CIMA","UND&Eacute;CIMA","DUOD&Eacute;CIMA"],
    mes:['','Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre']
};
Util.getMesIndice=function(mes){
    for(var i in this.mes)if(mes==this.mes[i])return i;
    return null;
};
Util.convertFchToDB=function(f){
    var fecha=f.split(" ");
    fecha[2]=Util.getMesIndice(fecha[2]);
    return fecha[4]+"-"+fecha[2]+"-"+fecha[0];
};
Util.convertFchToHoja=function(f){
    if(f==null)return null;
    var fecha=f.split("-");
    if(fecha[0]=="0000")return null;
    fecha=fecha[2]+" de "+Util.mes[parseInt(fecha[1],10)]+" del "+fecha[0];
    return fecha;
};
Util.convertParrafoToHtml = function(des){
    if(des == null || des == '')return '';
    var out = "";
    if(!(des instanceof Array)){
        des = des.split('\n');
    }

    for(var i in des){
        out += '<p>'+des[i]+'</p>';
    }
    return out;
};
Util.getHTMLInputDate = function(configuracion){
    var config,$inputDate,$div,$divControls;
    config = {
        fecha: '',
        label: 'fecha',
        nombre: 'fecha',
        tiulo: 'Click para modificar'
    };
    $.extend(config, configuracion);
    $inputDate = $(document.createElement('input'));
    $div = $(document.createElement('div'));
    $divControls = $(document.createElement('div'));
    $inputDate.attr({type:'text',name:config.nombre,placeholder:config.titulo,readonly:true}).datepicker( {changeMonth: true,changeYear: true,dateFormat:"dd 'de' MM 'del' yy"} );
    $inputDate.datepicker( "setDate" ,Util.convertFchToHoja(config.fecha));
    $divControls.addClass('controls').append($inputDate);
    $div.addClass('control-group').append('<label class="control-label">'+config.label+'</label>').append($divControls);
    return $div;
}
Util.getHTMLFormElement=function(configuracion){
    var config,$div,$divControls;
    config = {
        tipo:'input',
        type:'text',
        label: 'Elemento',
        nombre: 'input',
        rows:5,
        valor:'',
        opciones:[],
        titulo: '',
        clase:''
    };
    $.extend(config, configuracion);
    $divControls = $(document.createElement('div')).addClass('controls');
    switch(config.tipo){
        case 'textarea':
            var texto = ((config.valor instanceof Array)?config.valor.join('\n'):config.valor);
            $divControls
                .append($(document.createElement('textarea'))
                    .addClass(config.clase)
                    .attr({rows:config.rows,name:config.nombre,placeholder:config.titulo})
                    .val(texto));break;
        case 'select':
            var $select;
            $divControls.append(
                $select = $('<select>').addClass(config.clase).attr({name:config.nombre})
            );
            $.each(config.opciones, function() {
                if(config.campos != undefined)
                    $select.append( $('<option>').val(this[config.campos[0]]).text(this[config.campos[1]]));
                else
                    $select.append( $('<option>').val(this[0]).text(this[1]));
            });

            $select.val(config.valor);
            break;
        case 'inputxdate':
            $divControls.append($(document.createElement('input')).addClass(config.clase).attr({type:'text',name:config.nombre,placeholder:config.titulo}).val(config.valor));
            $('input',$divControls).xDatePicker();
            break;
        case 'input':
        default :
            $divControls.append($(document.createElement('input')).addClass(config.clase).attr({type:config.type,name:config.nombre,placeholder:config.titulo}).val(config.valor));
            break;
    }
    $div = $(document.createElement('div')).addClass('control-group').append('<label class="control-label">'+config.label+'</label>').append($divControls);
    return $div;
};
Util.getHtmlSelectRemote=function(label,name,url,datos,retornar){
    Util.ajax(url, datos, function(data){
        retornar(Util.getHtmlSelect(label, name, data),data);
    });
};
Util.getHtmlSelect = function(label,name,data){
    var html = '<div class="control-group">';
    html += '<label class="control-label">'+label+'</label>';
    html += '<div class="controls">';
    html += '<select type="text" name="'+name+'" class="input-large">';
    for(var i in data){
        html += '<option value="'+data[i].id+'" class="s_'+i+'">'+data[i].nombre+'</option>';
    }
    html += '</select>';
    html += '</div></div>';
    return html;
}
Util.ajaxWithObj = function(configuracion){
    var config = {
        type:"POST",
        dataType:'json',
        success: function(recepcion){
            if(recepcion.success){
                if(recepcion.mensaje!=undefined && recepcion.mensaje!=""){
                    Util.mensajes.showGlobo({
                        titulo:recepcion.mensaje,
                        tipo:'success'
                    });
                }
                if(configuracion.fnSuccess != undefined)
                    configuracion.fnSuccess(recepcion.data);
            }else{
                if(recepcion.mensaje!=undefined && recepcion.mensaje!=""){
                    Util.mensajes.showGlobo({
                        titulo:recepcion.mensaje,
                        tipo:'error'
                    });
                    if(configuracion.fnError != undefined)
                        configuracion.fnError(recepcion.error);
                }
            }
        },
        error:function(){
            Util.mensajes.showGlobo({
                titulo:"Ocurrio un <b>error<b> no esperado, no se pudo realizar la operación",
                tipo:'error'
            });
        }
    }
    $.extend(config,configuracion);
    $.ajax(config);
}
Util.ajax = function(url,datos,exito,error,showMensaje){
    if(typeof(url)=='object') {
        Util.ajaxWithObj(url);
        return;
    }
    showMensaje = (showMensaje!=undefined)?showMensaje:true;
    $.ajax({
        type:"POST",
        url: Util.url_base+url, //url
        dataType:Util.dataType,//json
        data:datos,//empty
        success: function(recepcion){
            if(recepcion.success){
                if(showMensaje&&recepcion.mensaje!=undefined && recepcion.mensaje!=""){
                    Util.mensajes.showGlobo({titulo:recepcion.mensaje,tipo:'success'});
                }
                exito(recepcion.data);
            }else{//TODO:three different errors
                if(showMensaje&&recepcion.mensaje!=undefined && recepcion.mensaje!=""){
                    Util.mensajes.showGlobo({titulo:recepcion.mensaje,tipo:'error'});
                    if(error!=undefined)error(recepcion.errors);
                }
            }
        },
        error:function(){
            if(showMensaje)
                Util.mensajes.showGlobo({titulo:"Ocurrio un Error, no se pudo realizar la operación",tipo:'error'});
            if(error!=undefined)error();
        }
    });
}
Util.showErrorsByName=function(errores,$form){
    if(errores!=undefined){
        for(var i in errores){
            var $element = $("input[name="+i+"]",$form);
            if($element.length==0)$element = $("textarea[name="+i+"]",$form);;

            $element.parent().parent().addClass('error');
            $element.after('<span class="help-inline">'+errores[i]+'</span>');
        }
    }
}
Util.removeErrors = function ($form){
    $('.help-inline',$form).remove();
    $('.error',$form).removeClass('error');
}
Util.enviarForm=function($form,configuracion){
    var config = {
        continuar:function(d){},
        data:[]
    }
    $.extend(config,configuracion);
    Util.mensajes.showGlobo({titulo:'Enviando Datos',tipo:'info',tiempo:2000});
    Util.removeErrors($form);
    Util.ajax($form.attr('action'), ((config.data.length==0)?$form.serialize():config.data), function(data){
        config.continuar(data);
    },function(errores){
        Util.showErrorsByName(errores, $form);
    });
}
Util.mensajes={
    contador:0,
    $container:'msj_container',
    init:function(){
        var $div=$(document.createElement('div')).attr('id', this.$container);
        $('body').append($div);
        this.$container = $div;
    },
    show:function(msj,tipo){
        var idMsj='msj_'+(this.contador++);
        var html = '<div id="'+idMsj+'" class="alert alert-'+tipo+'"> <a class="close"  data-dismiss="alert">×</a><p>'+msj+'</p></div>';
        $("#content").prepend(html);
        $("#"+idMsj).alert();
        return idMsj;
    },
    showGlobo:function(configuracion){
        var config,icono,$div;
        config = {conTiempo:true,tiempo:5000,titulo:'Alerta!!',texto:'',tipo:'info'};
        $.extend(config, configuracion);
        switch(config.tipo){
            case 'info':icono='info-sign';break;
            case 'success':icono='ok-sign';break;
            case 'error':icono='warning-sign';break;
            default:icono = 'info-sign';break;
        }
        $div=$(document.createElement('div')).
            addClass('alert alert-'+config.tipo).
            html('<a class="close" data-dismiss="alert">×</a><h4 class="alert-heading"><i class="icon-'+icono+'"></i> '+config.titulo+'</h4><p>'+config.texto+'</p>');
        this.$container.append($div);
        if(config.conTiempo) setTimeout(function(){$div.alert('close');},config.tiempo);
    }
}
Util.getHTMLUIAlert=function(configuracion){
    var config,$div;
    config={
        texto:''
    };
    $.extend(config, configuracion);
    $div = $(document.createElement('div')).addClass('ui-widget');
    $div.append($(document.createElement('div')).
        addClass('ui-state-highlight ui-corner-all').
        append('<p><span class="ui-icon ui-icon-info" style="float: left; margin-right: 3px;"></span>'+config.texto+'</p>')
    );
    return $div;
}
Util.ventanas={
    contador:0,
    getNueva:function(configuracion){
        this.container='u_wnd_'+Util.ventanas.contador++;
        var config,win = this;
        config = {
            title:'Ventana',
            width:300,
            minHeight:100,
            height:'auto',
            dialogClass:'',
            position:'center',
            modal:true,
            autoOpen: true,
            buttons:{},
            zIndex:'1000',
            beforeClose:function(event,ui){},
            open:function(event,ui){},
            close:function(event,ui){
                $("#"+win.container).dialog( "destroy" );
                $("#"+win.container).remove();
                config.fnCerrar();
            },


            showClose:true,
            fnCerrar:function(){}

        };
        $.extend(config, configuracion);

        if(config.showClose){
            $.extend(config.buttons,{'Cerrar':function(){
                $("#"+win.container).dialog("close");
            }});
        }

        this.$container = $("<div id=\""+win.container+"\"></div>")
        $("body").append(this.$container);
        this.$container.dialog(config);
        this.close=function(){
            win.$container.dialog("close");
        }
    }
}
Util.vConfirmacion = function(configuracion){
    var win,config;
    config = {
        title: 'Confirmación',
        texto: '¿Esta Seguro?',
        showClose:false,
        buttons:[{text:'Continuar',click:function(){
            if(configuracion.continuar!=undefined){configuracion.continuar();}
            win.close();
        }},{text:'Cancelar',click:function(){
            if(configuracion.cancelar!=undefined){configuracion.cancelar();}
            win.close();
        }}]
    };
    $.extend(config, configuracion);
    win = new Util.ventanas.getNueva(config);
    $('#'+win.container).html(config.texto);
    return win;
}
Util.vSort = function(configuracion){
    var win,config,$ulsortable,$contenedor;
    config = {
        url:'validate_sort_generic',
        title: 'Ordenar',
        tip:'Para <b>Ordenar</b> haga click sobre el elemento a ordenar, sin soltar el click muevalo hacia arriba o abajo de su lista, luego de ubicarlo soltar el click y Guardar el orden',
        campos:{indice:'id',nombre:'nombre'},
        datos:[],
        continuar:function(){},
        item:'',
        parametros:'',
        showClose:false,
        buttons:{'Guardar Orden':function(){
            var ordenado = [],count=0;
            $('li',$ulsortable).each(function(index,dom){
                var dataOrdenado = $(this).data('data');
                dataOrdenado.orden = count++;
                ordenado.push(dataOrdenado);
            });
            Util.ajax(config.url, $ulsortable.sortable("serialize")+'&item='+config.item+((config.parametros=='')?'':'&'+config.parametros), function(data){
                configuracion.continuar(ordenado,data);
            });
            win.close();
        },'Cancelar':function(){
            if(configuracion.cancelar!=undefined){configuracion.cancelar();}
            win.close();
        }}
    };
    $.extend(config, configuracion);
    win = new Util.ventanas.getNueva(config);
    $contenedor = $('#'+win.container);
    $ulsortable = $(document.createElement('ul')).addClass('unstyled');
    for(var i in config.datos){
        var $li = $(document.createElement('li')).addClass('ui-state-default').attr({id:'usort_'+config.datos[i][config.campos.indice]}).data('data',config.datos[i]);
        $li.append('<span class="ui-icon ui-icon-arrowthick-2-n-s"></span>'+config.datos[i][config.campos.nombre]+'</li>');
        $ulsortable.append($li);
    }
    $ulsortable.sortable();
    $ulsortable.disableSelection();
    $contenedor.append(Util.getHTMLUIAlert({texto:config.tip}),$ulsortable);
    return win;
}
Util.vChange=function(configuracion){
    var config,$ul,$contenedor,esto=this;
    config = {
        url_del:'validate_del_generic',
        url_set:'validate_edit_generic',
        titleModificar:'Modificar',
        tip:'Para <b>Eliminar</b> un Item haga click <span class="ui-icon ui-icon-trash" style="display: inline-block;vertical-align: bottom;"></span> y para <b>modificarlo</b> <span class="ui-icon ui-icon-tag" style="display: inline-block;vertical-align: bottom;"></span>',
        title: 'Modificar',
        width:400,
        campos:{indice:'id',nombre:'nombre'},
        isInput:false,
        datos:[],
        item:'',
        parametros:'',
        showClose:false,
        continuarEdit:function(d){},
        continuarDel:function(d){}
    };
    $.extend(config, configuracion);
    this.win = new Util.ventanas.getNueva(config);
    $contenedor = $('#'+this.win.container);
    $ul = $(document.createElement('ul')).addClass('unstyled');
    this.setData = function(data){
        $ul.html('');
        config.datos = data;
        for(var i in data){
            var $btn_del = $(document.createElement('button')).text('Eliminar').button({icons: {primary: "ui-icon-trash"},text: false}).data('data',data[i]);
            var $btn_edit = $(document.createElement('button')).text('Modificar').button({icons: {primary: "ui-icon-tag"},text: false}).data('data',data[i]);
            var $li = $(document.createElement('li')).append($btn_del,$btn_edit,data[i][config.campos.nombre]);
            $btn_del.on('click',function(e){
                e.preventDefault();
                var data = $(this).data('data');
                Util.vConfirmacion({texto:'¿Esta seguro de eliminar este item?',continuar:function(){
                    var ordenado = [];
                    for(var i in config.datos){
                        if(config.datos[i].id != data.id){
                            ordenado.push(config.datos[i]);
                        }
                    }
                    Util.ajax(config.url_del, 'id='+data.id+'&item='+config.item+((config.parametros=='')?'':'&'+config.parametros), function(d){
                        config.continuarDel(d,ordenado);
                    });
                }});

            });
            $btn_edit.on('click',function(e){
                e.preventDefault();
                var data = $(this).data('data');
                var winEdit = Util.vEdit({
                    url:config.url_set,
                    title:config.titleModificar,
                    isInput:config.isInput,
                    valor:data[config.campos.nombre],
                    parametros:'id='+data.id+'&item='+config.item+((config.parametros=='')?'':'&'+config.parametros),
                    continuar:function(d){
                        config.continuarEdit(d);
                        data[config.campos.nombre] = d.valor;
                        esto.setData(config.datos);
                        winEdit.close();
                    }});
            });
            $ul.append($li);
        }
    }
    this.setData(config.datos);
    $contenedor.append(Util.getHTMLUIAlert({texto:config.tip}),$ul);
};
//Util.vEdit = function(configuracion){
//    var win,config,$form,$contenedor,fnGuardarDatos;
//    var $elemento,$divElem;
//    config = {
//        url:'',
//        title: 'Editar',
//        position:'top',
//        campos:{label:'Descripción',nombre:'descripcion'},
//        valor:'',
//        isInput:false,
//        parametros:'',
//        continuar:function(d){},
//        showClose:true,
//        buttons:{'Guardar':function(){
//                fnGuardarDatos();
//        }}
//    };
//    fnGuardarDatos = function(){
//        Util.removeErrors($form);
//        Util.ajax(config.url,$form.serialize()+((config.parametros=='')?'':'&'+config.parametros), function(data){
//            config.continuar(data,$form.find(('input, textarea')).val());
//        },function(errores){
//                Util.showErrorsByName(errores, $form);
//        });
//    };
//    $.extend(config, configuracion);
//    win = new Util.ventanas.getNueva(config);
//    $contenedor = $('#'+win.container);
//    $form = $(document.createElement('form')).attr({id:win.container+'_frm',method:'post',action:config.url});
//    $form.submit(function(e){
//        e.preventDefault();
//    });
//    if(config.isInput)
//    {
//        $divElem = Util.getHTMLFormElement({label:config.campos.label,nombre:config.campos.nombre,valor:config.valor});
//        $form.append($divElem);
//        $elemento = $('input',$divElem);
//        $elemento.keypress(function (e) {
//            if(e.which==13){fnGuardarDatos();return false;}
//            return true;
//        });
//    }
//    else
//    {
//        var texto = '';
//        if(config.valor instanceof Array){
//            for(var i in config.valor){
//                texto += ((texto=='')?config.valor[i]:'\n'+config.valor[i]);
//            }
//        }else{
//            texto = config.valor;
//        }
//        $divElem = Util.getHTMLFormElement({tipo:'textarea',label:config.campos.label,nombre:config.campos.nombre,valor:texto,rows:7});
//        $form.append($divElem);
//        $elemento = $('textarea',$divElem);
//    }
//    $form.find(('input, textarea')).css({width:'95%'})
//    $contenedor.append($form);
//    $elemento.focus();
//    return win;
//};
Util.vEdit = function(configuracion){
    var win,config,$form,$contenedor,fnGuardarDatos;
    var $elemento,$divElem
    config = {
        url:'',
        title: 'Editar',
        ayuda:'',
        multiple:false,
        width:570,
        position:['center',80],
        campos:{
            label:'Descripción',
            nombre:'descripcion'
        },
        valor:'',
        isInput:false,
        parametros:'',
        continuar:function(d){},
        showClose:true,
        buttons:{
            'Guardar':function(){
                if(config.url==''){
                    config.continuar($elemento.val());
                    if(!config.multiple)
                        win.close();
                    else{
                        $elemento.val('');
                        $elemento.focus();
                    }
                }else{

                    $form.xForm('envioManual');
                }
            }
        }
    };
    $.extend(config, configuracion);
    win = new Util.ventanas.getNueva(config);
    $contenedor = $('#'+win.container);
    $form = $(document.createElement('form')).attr({
        id:win.container+'_frm',
        method:'post',
        action:config.url
    });
    $form.xForm({
        dataExtra:((config.parametros=='')?'':'&'+config.parametros)
        ,continuar:function(data){
            config.continuar(data,$elemento.val());
            if(!config.multiple)
                win.close();
            else{
                $elemento.val('');
                $elemento.focus();
            }
        }
    });
    if(config.ayuda!=''){
        $contenedor.append(
            $('<div>')
                .addClass('alert')
                .html('<button type="button" class="close" data-dismiss="alert">&times;</button>'+config.ayuda)
        );
    }
    if(config.isInput)
    {
        $divElem = Util.getHTMLFormElement({
            clase:'input-xxlarge',
            label:config.campos.label,
            nombre:config.campos.nombre,
            valor:config.valor
        });
        $form.append($divElem);
        $elemento = $('input',$divElem);
        $elemento.keypress(function (e) {
            if(e.which==13){
                $form.xForm('envioManual');
                return false;
            }
            return true;
        });
    }
    else
    {
        $divElem = Util.getHTMLFormElement({
            clase:'input-xxlarge',
            tipo:'textarea',
            label:config.campos.label,
            nombre:config.campos.nombre,
            valor:config.valor,
            rows:9
        });
        $form.append($divElem);
        $elemento = $('textarea',$divElem);
    }
    $contenedor.append($form);
    $elemento.focus();
    return win;
};


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

Util.enviarFormTarget= function($form, target) {
    $form.ajaxSubmit({
        target: target,
        beforeSubmit:function() {
            bloquearPagina();
        },
        success:function(response) {
            desbloquearPagina();
            Aplicacion.InicializarAcciones(target);
        }
    });
};


Util.load = function (url, datos, target) {
    bloquearPagina();
    $.ajax({
        url: Util.url_base + url,
        data: datos,
        cache:false,
        success: function (response) {
            desbloquearPagina();
            if(target != undefined && target.length>0) {
                $(target).html(response);
                Aplicacion.InicializarAcciones(target);
            }
        },
        error: function (response) {
            desbloquearPagina();
            $('#debug-content').contents().find('html').html(response);
            $("#errorModal").modal('show');
            $("#errorButton").show();
        }
    });
};

$(function(){
    Util.mensajes.init();
});