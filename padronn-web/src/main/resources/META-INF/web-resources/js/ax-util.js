$(function() {
    $.widget( "ax.xComboBox", {
        options:{
            data:[],//[{id:1,nombre:'a'}]
            emptyValue:'',
            value:''
        },
        _create: function() {
            this._startAutoComplete();
            if(this.options.value!='')this.value(this.options.value);
        },
        _setDataSelect:function(){
            var self = this;
            self.options.data=[];
            $('option',this.element).each(function(){
                var $option = $(this),
                valor = $option.val();
                if(valor!="")
                    self.options.data.push({id:valor,nombre:$option.text()});
            });
        },
        value:function(value){
             if (value === undefined) {
                return this.element.val();
            // se pasa un valor, entonces actúa como método establecedor
            } else {
                for(var i in this.options.data){
                    if(this.options.data[i].id==value){
                        this.element.val(value);
                        this.$inputAutocomplete.val(this.options.data[i].nombre);
                    }
                }
            }
        },
        _startAutoComplete:function(){
            var $input,
            input=document.createElement('input'),
            self = this,
            elemento = this.element.hide(),
            $wrapper = this.wrapper = $( "<div>" )
                .addClass("input-append")
                .insertBefore(elemento);
            input.type = 'text';
            this.$inputAutocomplete = $input = $(input).addClass(this.element.attr('class'));
            if(this.element[0].nodeName=="SELECT")
                this._setDataSelect();
            $input.appendTo($wrapper)
//                .addClass('input-large')
                .attr({placeholder:'Seleccione una opción'})
                .autocomplete({
                    delay:0,
                    minLength:0,
                    source:function(request,response){
                        var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term),"i");
                        response($.map(self.options.data, function(n,i){
                            if(!request.term||matcher.test(n.nombre))
                                return {
                                    label:n.nombre.replace(
                                        new RegExp(
                                            "(?![^&;]+;)(?!<[^<>]*)("+
                                            $.ui.autocomplete.escapeRegex(request.term)+
                                            ")(?![^<>]*>)(?![^&;]+;)","gi"
                                        ),"<strong>$1</strong>"
                                    ),
                                    value:n.nombre,
                                    option:n
                                };
                        }))
                    },
                    select:function(event,ui){
                        $(elemento).val(ui.item.option.id);
                        self._trigger("axSelected", null, ui.item.option);
                    },
                    change:function(event,ui){
                        if ( !ui.item ) {
                            var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( $(this).val() ) + "$", "i" ),
                            valid = false;
                            for(var i in self.options.data){
                                if(self.options.data[i].nombre.match(matcher)){
                                    $(self.element).val(self.options.data[i].id);
                                    valid=true;
                                    break;
                                }
                            }
                            if ( !valid ) {
                                // remove invalid value, as it didn't match anything
                                $( this ).val( "" );
                                elemento.val( self.options.emptyValue );
                                $input.data( "autocomplete" ).term = "";
                                return false;
                            }
                        }
                    }
                });
                //.addClass( "ui-widget ui-widget-content ui-corner-left" );
            $input.data("autocomplete")._renderItem=function(ul,item){
                return $("<li>")
                    .data("item.autocomplete",item)
                    .append("<a>"+item.label+"</a>")
                    .appendTo(ul);
            };
            $( "<button>" )
                .appendTo( $wrapper )
                .addClass( "btn" )
                .css({paddingLeft:'1px',paddingRight:'1px'})
                .html('<span class="ui-icon ui-icon-triangle-1-s"></span>')
                .click(function(e) {
                    e.preventDefault();
                    // close if already visible
                    if ( $input.autocomplete( "widget" ).is( ":visible" ) ) {
                        $input.autocomplete( "close" );
                        return;
                    }

                    // work around a bug (likely same cause as #5265)
                    $( this ).blur();

                    // pass empty string as value to search for, displaying all results
                    $input.autocomplete( "search", "" );
                    $input.focus();
                });
        },
        destroy: function() {
            this.wrapper.remove();
            this.element.show();
            $.Widget.prototype.destroy.call( this );
        }
    });
});

$(function() {
    $.widget( "ax.xAutoComplete", {
        options:{
            url:''
        },
        _create: function() {
            this._startAutoComplete();
        },
        _startAutoComplete:function(){
            var $input,
            input = document.createElement('input'),
            self = this,
            elemento = this.element.hide().val('');
            input.type='text';
            this.$input = $input = $(input).insertAfter(elemento).addClass($(self.element).attr('class'));
            
            $input.attr({placeholder:'Digite 3 letras'})
            .autocomplete({
                minLength:3,
                source:function(request,response){
                    Util.ajax(self.options.url, {termino:request.term}, function(data){
                        var termino = $.ui.autocomplete.escapeRegex(request.term);
                        response( $.map( data, function( item ) {
                            return {
                                label: item.nombre.replace(
                                    new RegExp(
                                            "(?![^&;]+;)(?!<[^<>]*)("+
                                            termino+
                                            ")(?![^<>]*>)(?![^&;]+;)","gi"
                                        ),"<strong>$1</strong>"
                                ),
                                value: item.nombre,
                                option: item
                            }
                        }));
                    });
                },
                select:function(event,ui){
                    $(elemento).val(ui.item.option.id);
                },
                change:function(event,ui){
                    if ( !ui.item ) {
                        $( this ).val( "" );
                        elemento.val("");
                        $input.data( "autocomplete" ).term = "";
                        return false;
                    }
                }
                
            });
            
            $input.data("autocomplete")._renderItem=function(ul,item){
                return $("<li>")
                    .data("item.autocomplete",item)
                    .append("<a>"+((item.option.codigo!=undefined)?'<i>('+item.option.codigo+')</i> ':'')+item.label+"</a>")
                    .appendTo(ul);
            };
        },
        destroy: function() {
            this.$input.remove();
            this.element.show();
            $.Widget.prototype.destroy.call( this );
        }
    });
});
$(function() {
    $.widget( "ax.xForm", {
        options:{
            dataExtra:''
            ,inicio:function(){}
            ,continuar:function(d,el){}
        },
        _create: function() {
            var self  = this;
            this.enviando = false;
            this.element.submit(function(e){
                e.preventDefault();
                if(!self.enviando){
                    self._startFormSubmit();
                }
            });
        },
        envioManual:function(){
            if(!this.enviando){
                this._startFormSubmit();
            }
        },
        _startFormSubmit:function(){
            this.enviando = true;
            var self = this;
            var $form = this.element;
            Util.mensajes.showGlobo({titulo:'Enviando Datos',tipo:'success',tiempo:2000});
            Util.removeErrors($form);
            this.options.inicio();
            Util.ajax({
                url:$form.attr('action')
                ,data:$form.serialize()+self.options.dataExtra
                ,fnSuccess:function(data){
                    self.options.continuar(data,self.element);
                    self.enviando = false;
                }
                ,fnError:function(errores){
                    if(errores!=undefined)
                        Util.showErrorsByName(errores, $form);
                    self.enviando = false;
                }
                ,error:function(){
                    Util.mensajes.showGlobo({titulo:"Ocurrio un <b>error<b> no esperado, intentelo denuevo",tipo:'error'});
                    self.enviando = false;
                }
            })
        },
        destroy: function() {
            $.Widget.prototype.destroy.call( this );
        }
    });
});
$(function() {
    $.widget( "ax.xDatePicker", {
        options:{
            placeholder:'Haga click para modificar'
        },
        _create: function() {
            this._startDatePicker();
        },
        _startDatePicker:function(){
            var $input,
            input = document.createElement('input'),
            self = this,
            elemento = this.element.hide().val('');
            input.type='text';
            this.$input = $input = $(input).insertAfter(elemento).addClass(elemento.attr('class'));
            $input
                .css({cursor:'pointer'})
                .attr({placeholder:self.options.placeholder,readonly:true})
                .datepicker( {
                    changeMonth: true,
                    changeYear: true,
                    dateFormat:"dd 'de' MM 'del' yy",
                    onSelect: function(value, inst) {
                        elemento.val(Util.convertFchToDB(value));
                    }
                } );
        },
        setDate:function(date){
            this.$input.datepicker('setDate',date);
            this.element.val(Util.convertFchToDB(this.$input.val()));
        },
        destroy: function() {
            this.$input.remove();
            this.element.show();
            $.Widget.prototype.destroy.call( this );
        }
    });
});
$(function() {
    $.widget( "ax.xNavSpia", {
        options:{
            distance: 40
        },
        _create: function() {
            var self = this,
            processScroll,
            $win = $(window)
                , $nav = this.element
                , $divguia = this.guia = $('<div>').height(0).insertAfter(this.element)
                , elementoAltura = parseInt(this.element.height())
                , altura = elementoAltura +this.options.distance
                , navTop = this.element.length && this.element.offset().top - this.options.distance
                , isFixed = 0;
            processScroll=function() {
                navTop = $divguia.offset().top - altura;
                var scrollTop = $win.scrollTop();
                if (scrollTop >= navTop && !isFixed) {
                    $divguia.height(elementoAltura);
                    isFixed = 1;
                    $nav.addClass('subnav-fixed');
                    self._trigger( "arriba", null, {} );
                } else if (scrollTop <= navTop && isFixed) {
                    $divguia.height(0);
                    isFixed = 0;
                    $nav.removeClass('subnav-fixed');
                    self._trigger( "abajo", null, {} );
                }
            }
            processScroll()
            setTimeout(function() {
                $win.on('scroll', processScroll);
            },1);
        },
        destroy: function() {
            $(window).off('scroll');
            $.Widget.prototype.destroy.call( this );
        }
    });
});

$(function() {
    $.widget( "ax.xPagination", {
        options:{
            maxFilas:4
            ,paginasVisibles:10
            ,paginaActual:1
            ,totalFilas:10
        },
        _create: function() {
            var self  = this
            ,$ul = this.$ul = $('<ul>')
            ,paginasTotal = Math.ceil(parseInt(this.options.totalFilas)/this.options.maxFilas)
            ,inicio = 1
            ,fin = paginasTotal
            ,posiblei = this.options.paginaActual-parseInt(this.options.paginasVisibles/2);
            inicio = (posiblei >= 1)?posiblei:1;
            fin = (inicio+this.options.paginasVisibles-1<=paginasTotal)?inicio+this.options.paginasVisibles-1:paginasTotal;
            $ul.append(
                $('<li>').append($('<a>').attr({href:'#',title:'Pagina 1'}).data('paginacion',1).text('Inicio'))
            );
            for(var i = inicio;i<=fin;i++){
                var $li;
                $ul.append(
                    $li = $('<li>').append(
                        $('<a>').attr({href:'#',title:'Pagina '+i}).data('paginacion',i).text(i)
                    )
                );
                if(i==this.options.paginaActual){$li.addClass('active');}
            }
            $ul.append(
                $('<li>').append($('<a>').attr({href:'#',title:'Pagina '+paginasTotal}).data('paginacion',paginasTotal).text('Fin'))
            ).appendTo(this.element);
            $('a',$ul).click(function(e){
                e.preventDefault();
                var numero = parseInt($(this).data('paginacion'));
                if(numero == self.options.paginaActual)return;
                self._trigger("cambiado", null, {pagina: numero});
            });
            
        },
        destroy: function() {
            this.$ul.remove();
            $.Widget.prototype.destroy.call( this );
        }
    });
});