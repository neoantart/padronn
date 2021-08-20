package pe.gob.reniec.padronn.logic.model;

import pe.gob.reniec.padronn.logic.web.validator.checks.CentroPobladoCheckValidorCheck;
import pe.gob.reniec.padronn.logic.web.validator.constraints.NameCharacters;
import pe.gob.reniec.padronn.logic.web.validator.constraints.UbigeoPobladoCheckValidorCheck;

import java.io.Serializable;

public class Ubigeo implements Serializable {

	String coUbigeoReniec;
	String coUbigeoInei;
	String deDepartamento;
	String deProvincia;
	String deDistrito;

    String deUbigeoInei;

    // campos extras
    String num; // total
    String num2; // sin dni
    String num3; // con dni

    //extras
    String nuSinDni;
    String nuConDni;
    String nuTotal;
    String nuCui;

    String id;
    String text;

    //reportes midis
    String nuPvl;
    String nuCuna;
    String nuJuntos;
    String nuQaliwarma;
    String nuNingun;
    String nuCunamasSCD;
    String nuCunamasSAF;

    // campos reportes
    String nuEdad0;
    String nuEdad1;
    String nuEdad2;
    String nuEdad3;
    String nuEdad4;
    String nuEdad5;

    // campos reportes
    String nuSis;
    String nuEssalud;
    String nuSanidad;
    String nuPrivado;
    String nuNinguno;


	public String getCoUbigeoReniec() {
		return coUbigeoReniec;
	}

	public void setCoUbigeoReniec(String coUbigeoReniec) {
		this.coUbigeoReniec = coUbigeoReniec;
	}

	public String getCoUbigeoInei() {
		return coUbigeoInei;
	}

	public void setCoUbigeoInei(String coUbigeoInei) {
		this.coUbigeoInei = coUbigeoInei;
	}

	public String getDeDepartamento() {
		return deDepartamento;
	}

	public void setDeDepartamento(String deDepartamento) {
		this.deDepartamento = deDepartamento;
	}

	public String getDeProvincia() {
		return deProvincia;
	}

	public void setDeProvincia(String deProvincia) {
		this.deProvincia = deProvincia;
	}

	public String getDeDistrito() {
		return deDistrito;
	}

	public void setDeDistrito(String deDistrito) {
		this.deDistrito = deDistrito;
	}

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDeUbigeoInei() {
        return deUbigeoInei;
    }

    public void setDeUbigeoInei(String deUbigeoInei) {
        this.deUbigeoInei = deUbigeoInei;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getNum3() {
        return num3;
    }

    public void setNum3(String num3) {
        this.num3 = num3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNuSinDni() {
        return nuSinDni;
    }

    public void setNuSinDni(String nuSinDni) {
        this.nuSinDni = nuSinDni;
    }

    public String getNuConDni() {
        return nuConDni;
    }

    public void setNuConDni(String nuConDni) {
        this.nuConDni = nuConDni;
    }

    public String getNuTotal() {
        return nuTotal;
    }

    public void setNuTotal(String nuTotal) {
        this.nuTotal = nuTotal;
    }

    public String getNuCui() {
        return nuCui;
    }

    public void setNuCui(String nuCui) {
        this.nuCui = nuCui;
    }

    public String getNuPvl() {
        return nuPvl;
    }

    public void setNuPvl(String nuPvl) {
        this.nuPvl = nuPvl;
    }

    public String getNuCuna() {
        return nuCuna;
    }

    public void setNuCuna(String nuCuna) {
        this.nuCuna = nuCuna;
    }

    public String getNuJuntos() {
        return nuJuntos;
    }

    public void setNuJuntos(String nuJuntos) {
        this.nuJuntos = nuJuntos;
    }

    public String getNuQaliwarma() {
        return nuQaliwarma;
    }

    public void setNuQaliwarma(String nuQaliwarma) {
        this.nuQaliwarma = nuQaliwarma;
    }

    public String getNuNingun() {
        return nuNingun;
    }

    public void setNuNingun(String nuNingun) {
        this.nuNingun = nuNingun;
    }

    public String getNuCunamasSCD() {
        return nuCunamasSCD;
    }

    public void setNuCunamasSCD(String nuCunamasSCD) {
        this.nuCunamasSCD = nuCunamasSCD;
    }

    public String getNuCunamasSAF() {
        return nuCunamasSAF;
    }

    public void setNuCunamasSAF(String nuCunamasSAF) {
        this.nuCunamasSAF = nuCunamasSAF;
    }

    public String getNuEdad0() {
        return nuEdad0;
    }

    public void setNuEdad0(String nuEdad0) {
        this.nuEdad0 = nuEdad0;
    }

    public String getNuEdad1() {
        return nuEdad1;
    }

    public void setNuEdad1(String nuEdad1) {
        this.nuEdad1 = nuEdad1;
    }

    public String getNuEdad2() {
        return nuEdad2;
    }

    public void setNuEdad2(String nuEdad2) {
        this.nuEdad2 = nuEdad2;
    }

    public String getNuEdad3() {
        return nuEdad3;
    }

    public void setNuEdad3(String nuEdad3) {
        this.nuEdad3 = nuEdad3;
    }

    public String getNuEdad4() {
        return nuEdad4;
    }

    public void setNuEdad4(String nuEdad4) {
        this.nuEdad4 = nuEdad4;
    }

    public String getNuEdad5() {
        return nuEdad5;
    }

    public void setNuEdad5(String nuEdad5) {
        this.nuEdad5 = nuEdad5;
    }

    public String getNuSis() {
        return nuSis;
    }

    public void setNuSis(String nuSis) {
        this.nuSis = nuSis;
    }

    public String getNuEssalud() {
        return nuEssalud;
    }

    public void setNuEssalud(String nuEssalud) {
        this.nuEssalud = nuEssalud;
    }

    public String getNuSanidad() {
        return nuSanidad;
    }

    public void setNuSanidad(String nuSanidad) {
        this.nuSanidad = nuSanidad;
    }

    public String getNuPrivado() {
        return nuPrivado;
    }

    public void setNuPrivado(String nuPrivado) {
        this.nuPrivado = nuPrivado;
    }

    public String getNuNinguno() {
        return nuNinguno;
    }

    public void setNuNinguno(String nuNinguno) {
        this.nuNinguno = nuNinguno;
    }

    @Override
    public String toString() {
        return "Ubigeo{" +
                "coUbigeoReniec='" + coUbigeoReniec + '\'' +
                ", coUbigeoInei='" + coUbigeoInei + '\'' +
                ", deDepartamento='" + deDepartamento + '\'' +
                ", deProvincia='" + deProvincia + '\'' +
                ", deDistrito='" + deDistrito + '\'' +
                ", deUbigeoInei='" + deUbigeoInei + '\'' +
                ", num='" + num + '\'' +
                ", num2='" + num2 + '\'' +
                ", num3='" + num3 + '\'' +
                '}';
    }
}
