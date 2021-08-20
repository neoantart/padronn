/**
 * basado en: http://gabrieleromanato.name/jquery-easy-table-pagination/
 * Quick&Dirty
 * pagina la tabla con clase "js-paginate", y pone el paginador en el div con clase "js-pager".
 * por defecto en líneas de 10. sin embargo podría modificarse.
 *
 * absolutamente amarrado a bootstrap (usa su paginador)
 * sin embargo cumple su objetivo.
 *
 * @author lmiguelmh
 * @since 040413 1735
 +*
 */

function paginar() {
	$('table.js-paginate').each(function () {
		var currentPage = 0;
		var numPerPage = 10;
		var $table = $(this);
		$table.bind('repaginate', function () {
			$table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
		});
		$table.trigger('repaginate');

		var numRows = $table.find('tbody tr').length;
		var numPages = Math.ceil(numRows / numPerPage);
		var $pager = $('<div class="pagination" style="margin-top: 0px;"></div>');
		var $ul = $('<ul></ul>');
		for (var page = 0; page < numPages; page++) {
			//var $li = $('<li></li>');
			if (page == 0) {
				$('<li class="disabled"></li>').html('<a href="#" style="color: #333" onclick="return false;"><strong>&nbsp;Paginación:&nbsp;</strong></a>').appendTo($ul);
			}
			$('<li class="page-number"></li>').html('<a href="#">' + ((page + 1) < 10 ? "0" + (page + 1) : (page + 1)) + '</a>').bind(
					'click',
					{newPage: page},
					function (event) {
						currentPage = event.data['newPage'];
						$table.trigger('repaginate');
						$(this).addClass('active').siblings().removeClass('active');
						event.preventDefault();
					}
			).appendTo($ul);
			//$li.appendTo($ul);
		}
		$ul.appendTo($pager).find('li.page-number:first').addClass('active');
		$ul.find('li.page-number:first').before('<li class="laquo"><a href="#">&laquo;</a></li>');
		$ul.find('li.page-number:last').after('<li class="raquo"><a href="#">&raquo;</a></li>');
		if (numPages > 1) {
			$('div.js-pager').html($pager);
		}

		var currentPageli = 0;
		var numPerPageli = 10;
		var numRowsli = $ul.find('li.page-number').length;
		var numPagesli = Math.ceil(numRowsli / numPerPageli);
		$ul.bind('repaginateli', function () {
			if (currentPageli == 0) {
				$ul.find('li.laquo').addClass('disabled');
			} else {
				$ul.find('li.laquo').removeClass('disabled');
			}
			if (currentPageli >= (numPagesli - 1)) {
				$ul.find('li.raquo').addClass('disabled');
			} else {
				$ul.find('li.raquo').removeClass('disabled');
			}
			$ul.find('li.page-number').hide().slice(currentPageli * numPerPageli, (currentPageli + 1) * numPerPageli).show();
		});
		$ul.trigger('repaginateli');

		$ul.find('li.laquo').bind('click', function (event) {
			if (currentPageli > 0) {
				currentPageli = currentPageli - 1;
				$ul.trigger('repaginateli');
			}
			event.preventDefault();
		});
		$ul.find('li.raquo').bind('click', function (event) {
			if (currentPageli < (numPagesli - 1)) {
				currentPageli = currentPageli + 1;
				$ul.trigger('repaginateli');
			}
			event.preventDefault();
		});

	});
}
