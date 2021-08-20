package pe.gob.reniec.padronn.logic.util;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import java.awt.*;
import java.awt.image.BufferedImage;


import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageUtils {

	private List<String> getImageTextLines(FontMetrics fm, String phrase, int width) {
		List<String> result = new ArrayList<String>();

		String[] words = phrase.trim().split("\\s+");
		//*System.out.println(Arrays.toString(words));
		String tempText = "";
		String currentText = "";
		for (String word : words) {
			tempText += (currentText.isEmpty() ? "" : " ") + word;
			if (width < fm.stringWidth(tempText)) {
				if (currentText.isEmpty()) {
					//es una plabra que se desborda, dibujar tempText
					result.add(tempText);
					//*System.out.println(tempText);
					currentText = "";
					tempText = "";
				} else {
					//dibujar currentText
					result.add(currentText);
					//*System.out.println(currentText);
					currentText = word;
					tempText = word;
				}
			} else {
				currentText = tempText;
			}
		}
		if (!currentText.isEmpty()) {
			result.add(currentText);
			//*System.out.println(currentText);
		}
		return result;
	}

	public byte[] entramarFotoAlt(byte[] imageInByte, String usuario, String institucion, String organizacion, String fecha) throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage bufferedImage = ImageIO.read(in);
		Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
		//g2d.drawImage(photo.getImage(), 0, 0, null);
		int iAncho = bufferedImage.getWidth();
		int iAlto = bufferedImage.getHeight();


		//Create an alpha composite of 50%
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f);
		g2d.setComposite(alpha);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Empresa
		g2d.setPaint(Color.BLACK);
		g2d.setFont(new Font("SansSerif", Font.BOLD, 15));
		FontMetrics fm = g2d.getFontMetrics();
		List<String> lines = getImageTextLines(fm, organizacion, iAncho);
		float yText=25;
		float heightText=fm.getAscent();
		for (String text : lines) {
			float xText = (iAncho - fm.stringWidth(text)) / 2;
			g2d.drawString(text, xText, yText);
			yText+=heightText;
		}

		// Fecha - Hora
		g2d.rotate(Math.PI / 2.0);
		g2d.setPaint(Color.BLACK);
		g2d.setFont(new Font("SansSerif ", Font.BOLD, 15));
		g2d.drawString(fecha, (iAlto-fm.stringWidth(fecha))*2/3, - iAncho + 15);
		g2d.drawString(institucion, (iAlto-fm.stringWidth(institucion))*2/3, -7);

		g2d.rotate(-1 * Math.PI);
		// Logo
		g2d.rotate(Math.PI * 0.80);
//		g2d.setPaint(Color.LIGHT_GRAY);
		g2d.setPaint(Color.RED);
		g2d.setFont(new Font("SansSerif ", Font.BOLD, 30));
		//g2d.drawString(institucion, 40, 20);
		g2d.drawString(usuario, 75, 5);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", baos);
		baos.flush();
		byte[] returnImage = baos.toByteArray();
		baos.close();
		return returnImage;
	}

	public PlanarImage convertGrayScale(RenderedImage img) {
		double[][] matrix = {{0.3D, 0.59D, 0.11D, 0D}};
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		pb.add(matrix);
		PlanarImage result = JAI.create("BandCombine", pb, null);
		return result;
	}

}
