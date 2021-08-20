/**
 * basado en: http://gabrieleromanato.name/jquery-easy-table-pagination/
 * Quick&Dirty
 * @author lmiguelmh >>> mejorado por jronal
 * @since 040413 1735
 */
function ul_paginar($currentPage, $nuPaginas, $urlAntesNuPagina, $urlDespuesNuPagina, $numPerPageli) {
	var numPerPageli = $numPerPageli;
	var currentPageli = Math.floor(($currentPage-1)/numPerPageli);
	var numPagesli = $nuPaginas;//Math.ceil(numRowsli / numPerPageli);

    var fnRenderPaginador = function() {
//        <ul class="ul-paginate"></ul>
        var $ul = $(document.createElement('ul')).addClass('ul-paginate');
//        $ul.html('');
        var liDetaPag = $(document.createElement('li'))
            .addClass('disabled')
            .append('<a href="#" style="color: #333" onclick="return false;"><strong> Página ' +
                    $currentPage + ' de ' + $nuPaginas + ': </strong></a>')
                .appendTo($ul)

            ,lilaquo = $(document.createElement('li'))
                .on('click', function(event){
                    event.stopPropagation();
                    event.preventDefault();
                    if (currentPageli > 0) {
                        currentPageli = currentPageli - 1;
                        fnRenderPaginador();
                    }
                    if (currentPageli == 0) {
                        $(this).addClass('disabled');
                    } else {
                        $(this).removeClass('disabled');
                    }
                })
                .append('<a href="#">«</a>')
                .appendTo($ul)

        for(var nuPaginaVar=numPerPageli*(currentPageli)+1; nuPaginaVar <= numPerPageli*(currentPageli+1) && nuPaginaVar<=numPagesli; nuPaginaVar++) {
            var url = $urlAntesNuPagina + nuPaginaVar + $urlDespuesNuPagina
                ,liPageNumber = $(document.createElement('li'))
                    .addClass('page-number')
                    .attr({'id':'a_'+nuPaginaVar})
                    .append(
                    $(document.createElement('a'))
                        .attr({'href': url})
                        .text(nuPaginaVar)
//                        .addClass('a_pagina')
                        .on('click', function(e){
                            e.preventDefault();
                            e.stopPropagation();
//                            Aplicacion.InicializarAcciones($(this));
                        })
                    )
                    .on('click', function(e){
//                        e.preventDefault();
                        e.stopPropagation();
                    })

            if(nuPaginaVar == $currentPage){
                liPageNumber
                    .addClass('active')
                    .attr({'onClickAction': 'return false;'})
                    .find('a')
                    .attr('href','#')
                    .on('click', function(e){
                        e.preventDefault();
                        e.stopPropagation();
                    })
            }
            liPageNumber.appendTo($ul);
//            $('#a_'+nuPaginaVar).off('click');
        }

//        if($currentPage!=1)


        var raquo = $(document.createElement('li'))
            .on('click', function(event){
                event.preventDefault();
                event.stopPropagation();
                if (currentPageli <= (Math.floor(numPagesli/numPerPageli))-1) {
                    currentPageli = currentPageli + 1;
                    fnRenderPaginador();
                }
                if (currentPageli >= (Math.floor(numPagesli/numPerPageli))-1) {
                    $(this).addClass('disabled');
                } else {
                    $(this).removeClass('disabled');
                }
            })
            .append('<a href="#">»</a>')
            .appendTo($ul);

        $('.pagination').html($ul);
        $('.page-number').off('click');
        Aplicacion.InicializarAcciones('.page-number');
        //Aplicacion.InicializarAcciones($ul);
    }
    fnRenderPaginador();

//    console.log('#a_'+nuPaginaVar);
}
