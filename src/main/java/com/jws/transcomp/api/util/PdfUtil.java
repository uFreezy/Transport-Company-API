package com.jws.transcomp.api.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfUtil {

    private PdfUtil() {
    }

    private static String removeInvalidChar(String str) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (WinAnsiEncoding.INSTANCE.contains(str.charAt(i))) {
                b.append(str.charAt(i));
            }
        }
        return b.toString();
    }

    public static byte[] generatePdf(List<?> entries) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.setFont(PDType1Font.HELVETICA, 14);

        contentStream.beginText();
        //Setting the leading
        contentStream.setLeading(14.5f);

        //Setting the position for the line
        contentStream.newLineAtOffset(25, 725);

        entries.forEach(entry -> {
            try {
                contentStream.showText("----------");
                contentStream.newLine();
                List<String> lines = List.of(StringUtils.splitByWholeSeparatorPreserveAllTokens(removeInvalidChar(entry.toString()), ",", 100));
                lines.forEach(l -> {
                    try {
                        contentStream.showText(l);
                        contentStream.newLine();
                    } catch (IOException ignored) {
                    }
                });

                contentStream.newLine();

            } catch (IOException ignored) {
            }
        });

        contentStream.endText();
        contentStream.close();

        document.save(byteArrayOutputStream);
        document.close();
        return byteArrayOutputStream.toByteArray();
    }
}
