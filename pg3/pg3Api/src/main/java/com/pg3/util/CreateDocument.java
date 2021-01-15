package com.pg3.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Calendar;

public class CreateDocument {
    Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Font.BOLD, new BaseColor(63,114,175));
    Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.BOLD, new BaseColor(0,0,0));
    Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 11, Font.NORMAL, new BaseColor(0,0,0));
    Font signatureFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, new BaseColor(0,0,0));

    Document document;
    ByteArrayOutputStream stream;
    PdfWriter writer;

    private void createDocument() throws Exception{
        document = new Document();

        stream = new ByteArrayOutputStream();
        writer = PdfWriter.getInstance(document, stream);
    }

    private void createHeader() throws Exception{
        Paragraph title = new Paragraph("FAST PGIII", titleFont);
        title.setSpacingAfter(70);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
    }

    private void init() throws Exception{
        createDocument();
        document.open();

        createHeader();
    }

    private String finish(){
        document.close();

        return Base64.getEncoder().encodeToString(stream.toByteArray());
    }

    public String createReceiverDocument(String subTitle, String content, String receiverName, String deliveryManName ) throws Exception{
        init();

        Paragraph _subTitle = new Paragraph(subTitle, subTitleFont);
        _subTitle.setAlignment(Element.ALIGN_CENTER);
        _subTitle.setSpacingAfter(25);
        document.add(_subTitle);

        Paragraph _content = new Paragraph(content, contentFont);
        _content.setAlignment(Element.ALIGN_CENTER);
        _content.setSpacingAfter(110);
        document.add(_content);

        // Create canvas
        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(signatureFont.getColor());
        canvas.setFontAndSize(signatureFont.getBaseFont(), signatureFont.getSize());

        // Date
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,"Documento recebido na cidade de", 180, 570, 0 );
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,"no dia", 350, 570, 0 );
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,"do mês de", 420, 570, 0 );
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER,"de", 520, 570, 0 );
        canvas.showTextAligned(PdfContentByte.ALIGN_CENTER, Integer.toString(Calendar.getInstance().get(Calendar.YEAR)), 540, 570, 0 );

        canvas.moveTo(252, 570);
        canvas.lineTo(335, 570);

        canvas.moveTo(365, 570);
        canvas.lineTo(395, 570);

        canvas.moveTo(445, 570);
        canvas.lineTo(512, 570);


        // Signature
        Paragraph signature = new Paragraph("ASSINATURAS E OBSERVAÇÕES", subTitleFont);
        signature.setAlignment(Element.ALIGN_CENTER);
        signature.setSpacingAfter(25);
        document.add(signature);

        // Signature
        Paragraph receiverSignature = new Paragraph(receiverName + ":", signatureFont);
        receiverSignature.setAlignment(Element.ALIGN_LEFT);
        receiverSignature.setSpacingAfter(200);
        document.add(receiverSignature);

        // Signature
        Paragraph deliveryManSignature = new Paragraph(deliveryManName + ":", signatureFont);
        deliveryManSignature.setAlignment(Element.ALIGN_LEFT);
        document.add(deliveryManSignature);

        canvas.closePathStroke();

        return finish();
    }
}
