package pe.gob.reniec.padronn.logic.util;

import org.springframework.stereotype.Component;

/**
 * Created by jfloresh on 02/06/2014.
 */
@Component
public class Paginador {
    Integer currentPageLi=0;
    Integer nuPagina=0;
    Integer numPerPageLi=0;
/*
    public Integer currentPageLi() {
//        Math.floor(($currentPage - 1) / numPerPageli);
        return (int) Math.floor((nuPagina - 1) / numPerPageLi);
    }*/

    public Integer getCurrentPageLi() {
        return (int) Math.floor((nuPagina - 1) / numPerPageLi);
    }

    public void setCurrentPageLi(Integer currentPageLi) {

        this.currentPageLi = currentPageLi;
    }

    public Integer getNuPagina() {
        return nuPagina;
    }

    public void setNuPagina(Integer nuPagina) {
        this.nuPagina = nuPagina;
    }

    public Integer getNumPerPageLi() {
        return numPerPageLi;
    }

    public void setNumPerPageLi(Integer numPerPageLi) {
        this.numPerPageLi = numPerPageLi;
    }
}
