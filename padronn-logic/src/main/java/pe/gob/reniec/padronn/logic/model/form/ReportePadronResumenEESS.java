package pe.gob.reniec.padronn.logic.model.form;

import org.hibernate.validator.constraints.NotEmpty;
import pe.gob.reniec.padronn.logic.web.validator.constraints.CoFrecuenciaAtencion;
import pe.gob.reniec.padronn.logic.web.validator.constraints.TipoEstablecimientoEmpty;

@CoFrecuenciaAtencion
public class ReportePadronResumenEESS {

        @NotEmpty(message = "Ubigeo es requerido")
        private String coUbigeo;

        private String deUbigeo;

        @NotEmpty(message = "Ingresar el nombre del establecimiento de salud")
        private String coEstSalud;

        private String nuSecuenciaLocal;

        private String deEstSalud;

        private String coFrecAtencion;

        @TipoEstablecimientoEmpty
        private Integer tiEstablecimiento;

        private Integer nuPagina;

        private Integer nuEdad;

        private Integer esPadron;

        public String getCoUbigeo() {
            return coUbigeo;
        }

        public void setCoUbigeo(String coUbigeo) {
            this.coUbigeo = coUbigeo;
        }

        public String getDeUbigeo() {
            return deUbigeo;
        }

        public void setDeUbigeo(String deUbigeo) {
            this.deUbigeo = deUbigeo;
        }

        public String getCoEstSalud() {
            return coEstSalud;
        }

        public void setCoEstSalud(String coEstSalud) {
            this.coEstSalud = coEstSalud;
        }

        public String getNuSecuenciaLocal() {
            return nuSecuenciaLocal;
        }

        public void setNuSecuenciaLocal(String nuSecuenciaLocal) {
            this.nuSecuenciaLocal = nuSecuenciaLocal;
        }

        public String getDeEstSalud() {
            return deEstSalud;
        }

        public void setDeEstSalud(String deEstSalud) {
            this.deEstSalud = deEstSalud;
        }

        public Integer getTiEstablecimiento() {
            return tiEstablecimiento;
        }

        public void setTiEstablecimiento(Integer tiEstablecimiento) {
            this.tiEstablecimiento = tiEstablecimiento;
        }

        public Integer getNuPagina() {
            return nuPagina;
        }

        public void setNuPagina(Integer nuPagina) {
            this.nuPagina = nuPagina;
        }

        public Integer getNuEdad() {
            return nuEdad;
        }

        public void setNuEdad(Integer nuEdad) {
            this.nuEdad = nuEdad;
        }

        public String getCoFrecAtencion() {
            return coFrecAtencion;
        }

        public void setCoFrecAtencion(String coFrecAtencion) {
            this.coFrecAtencion = coFrecAtencion;
        }

        public Integer getEsPadron() {
            return esPadron;
        }

        public void setEsPadron(Integer esPadron) {
            this.esPadron = esPadron;
        }
}
