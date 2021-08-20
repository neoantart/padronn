package pe.gob.reniec.padronn.logic.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.*;

import java.util.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public class PadronDaoTest {

	@Autowired
	PadronDao padronDao;

	Gson gson=new GsonBuilder().setPrettyPrinting().create();

	@Test
	public void testObtenerPorDniEnPersona() throws Exception {
		Persona persona=padronDao.obtenerPorDniEnPersona("90000017");
		System.out.println(gson.toJson(persona));
	}

	@Test
	public void testGuardarPadronNominal() throws Exception {
		PadronNominal padronNominal=new PadronNominal();
		padronNominal.setNuDniMenor("43942930");
		padronNominal.setApPrimerMenor("FLORES");
		padronNominal.setApSegundoMenor("BELIZARIO");
		padronNominal.setPrenombreMenor("VICTOR DINO");
		padronNominal.setFeNacMenor("25/09/1986");
		padronNominal.setCoGeneroMenor("1");
		padronNominal.setCoUbigeoInei("140101");
		padronNominal.setDeDireccion("AV TEODOMIRO GUTIERRES 578");
		padronNominal.setCoNivelPobreza("1");
		padronNominal.setCoEstSalud("00003251");
		padronNominal.setTiSeguroMenor(new ArrayList<String>(){{add("1");}});
		padronNominal.setNuAfiliacion("123456");
		padronNominal.setTiProSocial(new ArrayList<String>(){{add("1");}});
		padronNominal.setCoInstEducativa("0226274");
		padronNominal.setTiVinculoJefe("2");
		padronNominal.setCoDniJefeFam("04326311");
		padronNominal.setApPrimerJefe("FLORES");
		padronNominal.setApSegundoJefe("PARIZACA");
		padronNominal.setPrenomJefe("VICTOR");
		padronNominal.setTiVinculoMadre("1");
		padronNominal.setCoDniMadre("07745874");
		padronNominal.setApPrimerMadre("BELIZARIO");
		padronNominal.setApSegundoMadre("PACOMPIA");
		padronNominal.setPrenomMadre("CRISTINA");
		padronNominal.setCoGraInstMadre("30");
		padronNominal.setCoLenMadre("1");

		//Dar campos de auditoria
		padronNominal.setUsCreaRegistro("43942930");
		padronNominal.setUsModiRegistro("43942930");

		boolean exito=padronDao.guardarPadronNominal(padronNominal);

	}

	/*@Test
	public void testObtener() throws Exception {
		PadronNominal padronNominal=padronDao.obtener("43942930");
		System.out.println(gsonBeautiful.toJson(padronNominal));
	}*/

	@Test
	public void testEdad() {
		Date now = new Date();
		Date born = new GregorianCalendar(1986,7-1,28).getTime();

		GregorianCalendar bornCal = new GregorianCalendar();
		bornCal.setTime(born);
		GregorianCalendar nowCal = new GregorianCalendar();
		nowCal.setTime(now);

		System.out.println(nowCal);
		System.out.println(bornCal);

		nowCal.add(Calendar.YEAR, -bornCal.get(Calendar.YEAR));
		nowCal.add(Calendar.MONTH, -bornCal.get(Calendar.MONTH));
		nowCal.add(Calendar.DAY_OF_MONTH, -bornCal.get(Calendar.DAY_OF_MONTH) + 1);

		/*
		nowCal.add(Calendar.YEAR, -1);
		nowCal.add(Calendar.MONTH, -1);
		nowCal.add(Calendar.DAY_OF_MONTH, -1);
		/*/

		//System.out.println(nowCal.get(Calendar.YEAR)+" año(s), "+nowCal.get(Calendar.MONTH)+" mes(es) y " +nowCal.get(Calendar.DAY_OF_MONTH)+" día(s)");
		System.out.println(nowCal.get(Calendar.YEAR)+" año(s), "+(nowCal.get(Calendar.MONTH)+(nowCal.get(Calendar.DAY_OF_MONTH)>15?1:0))+" mes(es)");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testName() throws Exception {
		PadronNominal padronNominal=padronDao.obtenerPorCodigoPadronConDetalles("90000001");
		System.out.println(padronNominal.getDeInstEducativa());
	}

	@Test
	public void testInsertPadronNominalBaja() {
		System.out.println("TEST insertPadronNominalBaja");
		PadronNominalBaja padronNominalBaja = new PadronNominalBaja();
		padronNominalBaja.setCoMotivoBaja("1");
		padronNominalBaja.setCoPadronNominal("90000000");
		padronNominalBaja.setDeObservacion("");
		padronNominalBaja.setEsPadron("1");
		padronNominalBaja.setUsCreaAudi("43873223");
		padronNominalBaja.setUsModiAudi("43873223");
		padronDao.insertPadronNominalBaja(padronNominalBaja);
	}
}
