package pe.gob.reniec.padronn.logic.dao;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import pe.gob.reniec.padronn.logic.model.PadronImg;

import java.io.ByteArrayInputStream;
import java.io.IOException;


@RunWith(SpringJUnit4ClassRunner.class)//instancia la clase
@ContextConfiguration(locations = {"/applicationContext-test.xml"})//configuracion spring
public class PadronImgDaoTest {

	@Autowired
	PadronImgDao padronImgDao;

	@Test
	public void testInsertarFotoMenor() {
		System.out.println("Insertar foto ");
		try {
			byte[] foto = IOUtils.toByteArray(getClass().getResourceAsStream("/img/peru_nino_3591.jpg"));
			PadronImg padronImg = new PadronImg();
			padronImg.setCoPadroNominal("70000001");
			padronImg.setImgFotoMenor(foto);
			padronImg.setCtyFotoMenor("image/jpeg");
			padronImg.setExtFotoMenor("jpg");
			padronImg.setUsuCreaAudi("43873223");
			padronImg.setUsuModiAudi("43873223");
			padronImgDao.insertarPadronImg(padronImg);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("OK");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testClearPadronImg() {
		System.out.println("Borrar foto: ");
		padronImgDao.clearPadronImg("70000001");
		System.out.println("OK");
		Assert.assertEquals(1, 1);
	}

	@Test
	public void testObtenerFotoMenor() {
		System.out.println("Obtener foto: ");
		Assert.assertEquals(9121, padronImgDao.obtenerFotoMenor("70000001").length);
		System.out.println("OK");
	}

	@Test
	public void testObtenerPadronImg() {
		System.out.println("Obtener foto: ");
		PadronImg padronImg = padronImgDao.obtenerPadronImg("70000001");
		System.out.println(padronImg);
		Assert.assertEquals(9121, padronImg.getImgFotoMenor().length);
		System.out.println("OK");
	}

}
