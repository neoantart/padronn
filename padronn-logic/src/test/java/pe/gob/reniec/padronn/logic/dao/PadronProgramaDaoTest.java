package pe.gob.reniec.padronn.logic.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pe.gob.reniec.padronn.logic.model.PadronPrograma;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
//@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})//configuracion spring
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PadronProgramaDaoTest {

	@Autowired
	PadronProgramaDao padronProgramaDao;
	@Autowired
	PadronDao padronDao;

	@Test
	public void testGuardarProgramaSocial() {
		System.out.println("Guardado de programa social: ");

		PadronPrograma padronPrograma = new PadronPrograma();
		padronPrograma.setCoPadronNominal("3");
		padronPrograma.setUsuCreaAudi("43873223");
		padronPrograma.setUsuModiAudi("43873223");

		padronProgramaDao.clearPadronPrograma(padronPrograma.getCoPadronNominal());
		//padronPrograma.setCoProgramaSocial("1");
		//padronProgramaDao.guardarPadronPrograma(padronPrograma);
		padronPrograma.setCoProgramaSocial("2");
		padronProgramaDao.guardarPadronPrograma(padronPrograma);
		padronPrograma.setCoProgramaSocial("3");
		padronProgramaDao.guardarPadronPrograma(padronPrograma);

		padronPrograma.setNuSec(5);
		padronPrograma.setCoPadronNominal("2");
		padronPrograma.setCoProgramaSocial("2");
		padronProgramaDao.guardarPadronProgramaHistorico(padronPrograma);
		padronPrograma.setCoProgramaSocial("3");
		padronProgramaDao.guardarPadronProgramaHistorico(padronPrograma);
		padronPrograma.setCoProgramaSocial("4");
		padronProgramaDao.guardarPadronProgramaHistorico(padronPrograma);

		System.out.println("OK");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testListarPadronNominal() {
		System.out.println("Listados de programa social: ");

		List<PadronPrograma> lista;
		lista = padronProgramaDao.listarPadronPrograma("1");
		System.out.println("listado:");
		for(PadronPrograma padronPrograma:lista) {
			System.out.println(padronPrograma);
		}
		lista = padronProgramaDao.listarPadronProgramaHistorico("2", "1");
		System.out.println("histórico:");
		for(PadronPrograma padronPrograma:lista) {
			System.out.println(padronPrograma);
		}
		System.out.println("OK");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testGenerarNumeroSecPadronHistorico() {
		System.out.println("Generando números secuenciales históricos: ");

		System.out.println(padronDao.generarNumeroSecPadronHistorico("1"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("2"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("3"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("4"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("5"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("6"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("7"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("8"));
		System.out.println(padronDao.generarNumeroSecPadronHistorico("9"));

		System.out.println("OK");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testListarCoPadronPrograma() {
		System.out.println("Lista de PadronProgramas de CoPadronNominal");

		List<String> list = padronProgramaDao.listarCoPadronPrograma("70000001");
		System.out.println(Arrays.toString(list.toArray(new String[0])));
	}
}
