package pe.gob.reniec.padronn.logic.web.view;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.gob.reniec.padronn.logic.model.Dominio;
import pe.gob.reniec.padronn.logic.model.PadronNominal;
import pe.gob.reniec.padronn.logic.util.BasicImagenCiudadano;
import pe.gob.reniec.padronn.logic.util.GetCurrentDateTime;
import pe.gob.reniec.padronn.logic.util.PadronProperties;
import pe.gob.reniec.padronn.logic.util.SimpleBase64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by jfloresh on 20/05/2014.
 */
public class FichaPadronView extends AbstractITextPdfView {

    Logger logger = Logger.getLogger(getClass());

    private static Paragraph titleParagraph;
    private static Paragraph subtitleParagraph;
    private static Paragraph subtitleCoPadronNominal;
    private static Paragraph subtitleFechaRegistro;
    private static Image logoImg;
    private static Image fotoImg;
    private static Image sinfotoImgM;
    private static Image sinfotoImgF;
    URL resourceFoto;

    static {
        Font titleFont;
        Font subtitleFont;
        titleFont = FontFactory.getFont("san serif", 13, Font.BOLD);
        subtitleFont = FontFactory.getFont("san serif", 12, Font.BOLD);
        URL resource = FichaPadronView.class.getResource("/META-INF/web-resources/img/logo-reniec.png");
        URL resourceSinFotoM = FichaPadronView.class.getResource("/META-INF/web-resources/img/sin_foto_masculino.jpg");
        URL resourceSinFotoF = FichaPadronView.class.getResource("/META-INF/web-resources/img/sin_foto_femenino.jpg");

        titleParagraph = new Paragraph("REGISTRO NACIONAL DE IDENTIFICACIÓN Y ESTADO CIVIL (RENIEC)", titleFont);
        titleParagraph.setSpacingAfter(10);
        titleParagraph.setSpacingBefore(10);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        subtitleParagraph = new Paragraph("SISTEMA DE PADRON NOMINAL", subtitleFont);
        subtitleParagraph.setSpacingAfter(20);
        subtitleParagraph.setAlignment(Element.ALIGN_CENTER);

        try {
            logoImg = Image.getInstance(resource);
            sinfotoImgM = Image.getInstance(resourceSinFotoM);
            sinfotoImgF = Image.getInstance(resourceSinFotoF);
        } catch (BadElementException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
    }

    /*@Override
    protected Document newDocument() {
        Document document = new Document(PageSize.A4, 72, 72, 72, 72);
        return document;
    }*/

    private void createHeader(Document document, PdfWriter writer, PadronNominal padronNominal) throws Exception {
        logoImg.scalePercent(60);
        document.add(logoImg);

        String fechaModiRegistro = "SIN DATO";
        try {

            if (padronNominal.getFeModiRegistro() != null) {
                fechaModiRegistro = padronNominal.getFeModiRegistro();
            }
            else{
                fechaModiRegistro = padronNominal.getFeCreaRegistro();
            }
        }catch(Exception e) {
            fechaModiRegistro = padronNominal.getFeCreaRegistro();
        }

        subtitleCoPadronNominal = new Paragraph("Codigo Padron: " + padronNominal.getCoPadronNominal(), FontFactory.getFont("san serif", 10, Font.BOLD));
        subtitleCoPadronNominal.setAlignment(Element.ALIGN_RIGHT);
        subtitleFechaRegistro = new Paragraph("Fecha de registro: " + fechaModiRegistro, FontFactory.getFont("san serif", 8, Font.BOLD));
        subtitleFechaRegistro.setSpacingAfter(3);
        subtitleFechaRegistro.setAlignment(Element.ALIGN_RIGHT);


        document.add(titleParagraph);
        document.add(subtitleParagraph);
        document.add(subtitleCoPadronNominal);
        document.add(subtitleFechaRegistro);

        PdfContentByte canvas = writer.getDirectContent();
        canvas.saveState();

        canvas.setColorStroke(BaseColor.BLACK);
        canvas.setColorFill(BaseColor.BLACK);
        int margin = 40;
//        System.out.println(document.getPageSize().getLeft() + " " + document.getPageSize().getTop());

//        canvas.rectangle(margin, margin, document.getPageSize().getWidth() - 2 * margin, document.getPageSize().getHeight() - 2 * margin);

        canvas.stroke();
        canvas.restoreState();
    }

    private PdfPCell getCell(String s) {
        s = (s != null && !s.isEmpty()) ? s : "SIN DATO";
        PdfPCell pdfPCell = new PdfPCell(new Phrase(s, FontFactory.getFont("san serif", 9, Font.NORMAL)));
        pdfPCell.setBorder(0);
        pdfPCell.setPaddingBottom(3f);
        pdfPCell.setPaddingTop(3f);
        return pdfPCell;
    }

    private PdfPCell getCellLabel(String s) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(s, FontFactory.getFont("san serif", 9, Font.BOLD)));
        pdfPCell.setBorder(0);
        pdfPCell.setPaddingBottom(3f);
        pdfPCell.setPaddingTop(3f);
        return pdfPCell;
    }

    private PdfPCell getCellSubTitle(String s) {
        PdfPCell pdfPCell = new PdfPCell(new Phrase(s, FontFactory.getFont("san serif", 11, Font.BOLD)));
        pdfPCell.setBorder(2);
        pdfPCell.setBorderColorBottom(BaseColor.BLACK);
        pdfPCell.setBorderColorLeft(BaseColor.WHITE);
        pdfPCell.setBorderColorRight(BaseColor.WHITE);
        pdfPCell.setBorderColorTop(BaseColor.WHITE);
        pdfPCell.setPaddingBottom(10f);
        pdfPCell.setPaddingTop(15f);
        pdfPCell.setColspan(2);
        return pdfPCell;
    }

    private PdfPTable createDatosMenor1(PadronNominal padronNominal) {

        PdfPTable tableInformation = new PdfPTable(new float[]{35, 70});
        tableInformation.getDefaultCell().setBorder(0);

        tableInformation.getDefaultCell().setPaddingBottom(3f);
        tableInformation.getDefaultCell().setPaddingTop(3f);

        tableInformation.addCell(getCellLabel("DNI/CUI:"));

        if (padronNominal.getNuDniMenor() != null && !padronNominal.getNuDniMenor().isEmpty()) {
            tableInformation.addCell(getCell(padronNominal.getNuDniMenor()));
        }
        else if(padronNominal.getNuCui()!=null && !padronNominal.getNuCui().isEmpty()){
            tableInformation.addCell(getCell(padronNominal.getNuCui()));
        }
        else {
            tableInformation.addCell(getCell("SIN DATO"));
        }
        tableInformation.addCell(getCellLabel("CNV"));
        if(padronNominal.getNuCnv()!=null && !padronNominal.getNuCnv().isEmpty())
            tableInformation.addCell(getCell(padronNominal.getNuCnv()));
        else
            tableInformation.addCell(getCell("SIN DATO"));

        tableInformation.addCell(getCellLabel("Ap. Paterno:"));
        tableInformation.addCell(getCell(padronNominal.getApPrimerMenor()));
        tableInformation.addCell(getCellLabel("Ap. Materno:"));
        tableInformation.addCell(getCell(padronNominal.getApSegundoMenor()));
        tableInformation.addCell(getCellLabel("Pre Nombres:"));
        tableInformation.addCell(getCell(padronNominal.getPrenombreMenor()));
        tableInformation.addCell(getCellLabel("Nacimiento:"));
        tableInformation.addCell(getCell(padronNominal.getFeNacMenor()));
        tableInformation.addCell(getCellLabel("Sexo:"));
        tableInformation.addCell(getCell(padronNominal.getDeGeneroMenor()));
        tableInformation.addCell(getCellLabel("Edad:"));
        tableInformation.addCell(getCell(padronNominal.getEdadEscrita()));

        /*PdfPCell cellInformation = new PdfPCell();
        cellInformation.addElement(tableInformation);
        cellInformation.setBorder(Rectangle.NO_BORDER);
        cellInformation.setBackgroundColor(new BaseColor(255,255,45));*/

/*        PdfPTable pdfPTable = new PdfPTable(new float[]{70, 30});


        pdfPTable.getDefaultCell().setBorder(0);
        fotoImg.scalePercent(50);
        PdfPCell pdfPCellFoto = new PdfPCell(fotoImg);
        pdfPCellFoto.setBorder(0);
        pdfPCellFoto.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCellFoto.setVerticalAlignment(Element.ALIGN_CENTER);

        pdfPTable.addCell(tableInformation);
        pdfPTable.addCell(pdfPCellFoto);*/

        return tableInformation;
    }


    private PdfPTable createDatosMenor2(PadronNominal padronNominal) {

        if(padronNominal.getNuDniMenor()!=null && padronNominal.getImagen()!=null){
            try {
                resourceFoto = new URL("http://sio2.reniec.gob.pe/imagen/imagen?tiFicha=E&nuDni=" + padronNominal.getNuDniMenor() + "&tiImg=0");
                fotoImg = Image.getInstance(padronNominal.getImagen());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (padronNominal.getCoPadronNominal() != null && padronNominal.getImagen() != null){
            try {
                fotoImg = Image.getInstance(padronNominal.getImagen());
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            fotoImg = null;
        }


        PdfPTable tableInformation = new PdfPTable(2);
        tableInformation.getDefaultCell().setBorder(0);
        /*tableInformation.getDefaultCell().setBorderColorTop(BaseColor.BLACK);
        tableInformation.getDefaultCell().setBorderColorLeft(BaseColor.WHITE);
        tableInformation.getDefaultCell().setBorderColorRight(BaseColor.WHITE);
        tableInformation.getDefaultCell().setPaddingBottom(3f);
        tableInformation.getDefaultCell().setPaddingTop(3f);*/
        tableInformation.addCell(getCellLabel("Ubigeo:"));
        tableInformation.addCell(getCell(" "));
        PdfPCell cellUbigeo = getCell(String.format("%s/%s/%s", padronNominal.getDeDepartamento(), padronNominal.getDeProvincia(), padronNominal.getDeDistrito()).toUpperCase());
        cellUbigeo.setColspan(2);
        tableInformation.addCell(cellUbigeo);

        tableInformation.addCell(getCellLabel("Centro Poblado:"));
        tableInformation.addCell(getCell(" "));

        PdfPCell cellCentroPoblado = getCell(padronNominal.getDeCentroPoblado());
        cellCentroPoblado.setColspan(2);
        tableInformation.addCell(cellCentroPoblado);

        tableInformation.addCell(getCellLabel("Area:"));
        tableInformation.addCell(getCell(" "));
        PdfPCell cellDeAreaCcpp = getCell(padronNominal.getDeAreaCcpp());
        cellDeAreaCcpp.setColspan(2);
        tableInformation.addCell(cellDeAreaCcpp);

        if (padronNominal.getDeAreaCcpp().equals("URBANA")) {
            tableInformation.addCell(getCellLabel("Eje Vial:"));
            tableInformation.addCell(getCell(" "));

            String deEje = (padronNominal.getDeVia()!=null && !padronNominal.getDeVia().trim().isEmpty()) ? padronNominal.getDeVia():"SIN DATO";
            logger.info("deEje="+deEje);
            tableInformation.addCell(getCell(deEje));
            tableInformation.addCell(getCell(" "));

        }

        tableInformation.addCell(getCellLabel("Descripción Dirección:"));
        tableInformation.addCell(" ");
        PdfPCell direccionCell = getCell(padronNominal.getDeDireccion());
        direccionCell.setColspan(2);
        tableInformation.addCell(direccionCell);

        if (padronNominal.getDeAreaCcpp().equals("URBANA")) {
            tableInformation.addCell(getCellLabel("Referencia de Direccion:"));
            tableInformation.addCell(" ");

            String deRefe = (padronNominal.getDeRefDir()!=null && !padronNominal.getDeRefDir().trim().isEmpty())?padronNominal.getDeRefDir():"SIN DATO";
            tableInformation.addCell(getCell(deRefe));
            tableInformation.addCell(getCell(" "));
        }

        PdfPTable pdfPTable = new PdfPTable(new float[]{70, 30});

        pdfPTable.getDefaultCell().setBorder(0);

        if (padronNominal.getNuDniMenor() != null && !padronNominal.getNuDniMenor().isEmpty()) {
            fotoImg.scalePercent(50);
            PdfPCell pdfPCellFoto;

            pdfPCellFoto = new PdfPCell(fotoImg);
            pdfPCellFoto.setBorder(0);
            pdfPCellFoto.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCellFoto.setVerticalAlignment(Element.ALIGN_CENTER);
            pdfPTable.addCell(tableInformation);
            pdfPTable.addCell(pdfPCellFoto);
        }/* else {
            if(padronNominal.getCoPadronNominal()!=null && !padronNominal.getCoPadronNominal().isEmpty()) {
                pdfPCellFoto = new PdfPCell(fotoImg);
                pdfPCellFoto.setBorder(0);
                pdfPCellFoto.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCellFoto.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(tableInformation);
                pdfPTable.addCell(pdfPCellFoto);
            }*/
            else {
                PdfPCell pdfPCellFoto;
//              sinfotoImgM.setscale
                if ("1".equals(padronNominal.getCoGeneroMenor())) {
                    sinfotoImgM.scalePercent(50);
                    pdfPCellFoto = new PdfPCell(sinfotoImgM);
                } else {
                    sinfotoImgF.scalePercent(50);
                    pdfPCellFoto = new PdfPCell(sinfotoImgF);
                }
                pdfPCellFoto.setBorder(0);
                pdfPCellFoto.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCellFoto.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(tableInformation);
                pdfPTable.addCell(pdfPCellFoto);
            }
      //  }

        return pdfPTable;
    }


    private PdfPTable createInfoAdic1(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{70, 70});
        tableInformation.getDefaultCell().setBorder(0);
        /*tableInformation.addCell(getCellLabel("Ultimo Establecimiento de salud de atención:"));*/
        tableInformation.addCell(getCellLabel("Establecimiento de salud de  nacimiento:"));
     //   tableInformation.addCell(getCell(""));
        /*PdfPCell cellEESS = getCell(padronNominal.getNoEstSalud());*/
        PdfPCell cellEESS;
        if(padronNominal.getDeEstSaludNac()!=null && !padronNominal.getDeEstSaludNac().isEmpty()){
            cellEESS = getCell(padronNominal.getDeEstSaludNac());
        }
        else{
            cellEESS = getCell("SIN DATO");
        }

        cellEESS.setColspan(2);
        tableInformation.addCell(cellEESS);

        tableInformation.addCell(getCellLabel("Codigo RENAES:"));
        tableInformation.addCell(getCell(padronNominal.getCoEstSaludNac()));

        tableInformation.addCell(getCellLabel("Tipos de seguro:"));
        String tiposSeguro = "";
        for (Dominio dominio : padronNominal.getPadronSeguroList()) {
            tiposSeguro += dominio.getDeDom() + "\n";
        }
        if("".equals(tiposSeguro)) tiposSeguro = "SIN DATO";
        tableInformation.addCell(getCell(tiposSeguro));

        /*tableInformation.addCell(getCell(padronNominal.getDeSeguroMenor()));*/

        return tableInformation;
    }

    private PdfPTable createInfoAdic2(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{50, 70});

        tableInformation.getDefaultCell().setBorder(0);

        tableInformation.addCell(getCellLabel("Institución Educativa:"));

        tableInformation.addCell(getCell(padronNominal.getNoInstEducativa()));

        tableInformation.addCell(getCellLabel("Codigo Modular:"));
        tableInformation.addCell(getCell(padronNominal.getCoModular()));

        tableInformation.addCell(getCellLabel("Programa Social:"));
        String programasSociales = "";
        for (Dominio dominio : padronNominal.getPadronProgramaList()) {
            programasSociales += dominio.getDeDom() + "\n";
        }
        if ("".equals(programasSociales)) programasSociales = "SIN DATO";
        tableInformation.addCell(getCell(programasSociales));
        return tableInformation;
    }


    private PdfPTable createDatosMadre1(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{70, 70});

        tableInformation.getDefaultCell().setBorder(0);
        tableInformation.addCell(getCellLabel("Ap. Paterno:"));
        tableInformation.addCell(getCell(padronNominal.getApPrimerMadre()));
        tableInformation.addCell(getCellLabel("Ap. Materno:"));
        tableInformation.addCell(getCell(padronNominal.getApSegundoMadre()));
        tableInformation.addCell(getCellLabel("Nombres:"));
        tableInformation.addCell(getCell(padronNominal.getPrenomMadre()));
        return tableInformation;
    }

    private PdfPTable createDatosMadre2(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{50, 70});
        tableInformation.getDefaultCell().setBorder(0);
        tableInformation.addCell(getCellLabel("DNI"));
        tableInformation.addCell(getCell(padronNominal.getCoDniMadre()));
        tableInformation.addCell(getCellLabel("Grado Instruccion:"));

        String deGraInstMadre = (padronNominal.getDeGraInstMadre()!=null && !padronNominal.getDeGraInstMadre().isEmpty())?padronNominal.getDeGraInstMadre():"SIN DATO";
        tableInformation.addCell(getCell(deGraInstMadre));

        tableInformation.addCell(getCellLabel("Lengua Habitual:"));
        String deLenMadre = (padronNominal.getDeLenMadre()!=null && !padronNominal.getDeLenMadre().isEmpty())?padronNominal.getDeLenMadre():"SIN DATO";

        tableInformation.addCell(getCell(deLenMadre));

        tableInformation.addCell(getCellLabel("Número Celular:"));
        String deCelMadre = (padronNominal.getNuCelMadre() !=null && !padronNominal.getNuCelMadre().isEmpty())?padronNominal.getNuCelMadre():"SIN DATO";

        tableInformation.addCell(getCell(deCelMadre));

        tableInformation.addCell(getCellLabel("Dirección Correo:"));
        String deDiCorreoMadre = (padronNominal.getDiCorreoMadre() !=null && !padronNominal.getDiCorreoMadre().isEmpty())?padronNominal.getDiCorreoMadre():"SIN DATO";

        tableInformation.addCell(getCell(deDiCorreoMadre));

        return tableInformation;
    }

    private PdfPTable createDatosJefe1(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{70, 70});
        tableInformation.getDefaultCell().setBorder(0);
        tableInformation.addCell(getCellLabel("Ap. Paterno"));
        tableInformation.addCell(getCell(padronNominal.getApPrimerJefe()));
        tableInformation.addCell(getCellLabel("Ap. Materno:"));
        tableInformation.addCell(getCell(padronNominal.getApSegundoJefe()));
        tableInformation.addCell(getCellLabel("Nombres:"));
        tableInformation.addCell(getCell(padronNominal.getPrenomJefe()));
        return tableInformation;
    }


    private PdfPTable createDatosJefe2(PadronNominal padronNominal) {
        //PdfPTable tableInformation = new PdfPTable(2);
        PdfPTable tableInformation = new PdfPTable(new float[]{50, 70});
        tableInformation.getDefaultCell().setBorder(0);
        tableInformation.addCell(getCellLabel("DNI:"));
        tableInformation.addCell(getCell(padronNominal.getCoDniJefeFam()));
        tableInformation.addCell(getCellLabel("Tipo de Vinculo:"));
        tableInformation.addCell(getCell(padronNominal.getDeVinculoJefe()));
        return tableInformation;
    }

    private String getFooterInfo(Map model) throws DocumentException {
        String usuario = (String) model.get("coUsuario");
        String fechaTransaccion = GetCurrentDateTime.getCurrentDateTime();
        return "Usuario: " + usuario + " Fecha: " + fechaTransaccion;
    }


    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imprimir = (String) model.get("imprimir");
        String download = (String) model.get("download");
        if (imprimir != null && !imprimir.isEmpty()) {
            writer.addJavaScript("this.print(true);", false);// para printing
//            writer.addJavaScript("this.closeDoc(true);");
//            writer.addJavaScript("this.onfocus=function(){ this.close();}",false);// para printing
        }
        logger.info("request.getContextPath():" + request.getContextPath());
        logger.info("request.getRequestURI():" + request.getRequestURI());
        logger.info("request.getProtocol():" + request.getProtocol());
        logger.info("request.getServerPort():" + request.getServerPort());
        logger.info("request.getRequestURL():" + request.getRequestURL());
        if (download != null && !download.isEmpty()) {
            String coPadronNominal = (String) model.get("coPadronNominal");
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", coPadronNominal + ".pdf");
            response.setHeader(headerKey, headerValue);
        }

        PadronNominal padronNominal = (PadronNominal) model.get("padronNominal");
        createHeader(document, writer, padronNominal);
        PdfPTable tableLayout = new PdfPTable(new float[]{40, 60});
        tableLayout.setWidthPercentage(100);
        tableLayout.getDefaultCell().setBorder(0);

        tableLayout.addCell(getCellSubTitle("1. Datos del Niño/a"));
        tableLayout.addCell(createDatosMenor1(padronNominal));
        tableLayout.addCell(createDatosMenor2(padronNominal));

        tableLayout.addCell(getCellSubTitle("2. Información Adicional"));
        tableLayout.addCell(createInfoAdic1(padronNominal));
        tableLayout.addCell(createInfoAdic2(padronNominal));
        tableLayout.addCell(getCellSubTitle("3. Datos de la Madre"));
        tableLayout.addCell(createDatosMadre1(padronNominal));
        tableLayout.addCell(createDatosMadre2(padronNominal));
        tableLayout.addCell(getCellSubTitle("4. Datos del jefe de familia"));
        tableLayout.addCell(createDatosJefe1(padronNominal));
        tableLayout.addCell(createDatosJefe2(padronNominal));
        document.add(tableLayout);
        writer.setPageEvent(new HeaderAndFooter(getFooterInfo(model)));
    }


    public class HeaderAndFooter extends PdfPageEventHelper {

        private String name = "";


        protected Phrase footer;
        protected Phrase header;

        /*
         * Font for header and footer part.
         */
        private Font headerFont = FontFactory.getFont("san serif", 9, Font.NORMAL);

        private Font footerFont = FontFactory.getFont("san serif", 9, Font.NORMAL);

        /*
         * constructor
         */
        public HeaderAndFooter(String name) {
            super();

            this.name = name;
            header = new Phrase("***** Header *****");
            footer = new Phrase(name, footerFont);
        }


        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfContentByte cb = writer.getDirectContent();

            //header content
            String headerContent = "Name: " + name;

            //header content
            String footerContent = headerContent;
        /*
         * Header
         */
            /*ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(headerContent, headerFont),
                    document.leftMargin() - 1, document.top() + 30, 0);*/

        /*
         * Foooter
         */
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, footer,
                    document.right() - 2, document.bottom() - 20, 0);

        }

    }

}
