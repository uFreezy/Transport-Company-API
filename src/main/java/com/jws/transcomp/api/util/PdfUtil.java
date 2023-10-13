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
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 14);
                contentStream.setLeading(14.5f);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 725);

                for (Object entry : entries) {
                    contentStream.showText("----------");
                    contentStream.newLine();

                    String[] lines = StringUtils.splitByWholeSeparatorPreserveAllTokens(
                            removeInvalidChar(entry.toString()), ",", 100);

                    for (String line : lines) {
                        contentStream.showText(line);
                        contentStream.newLine();
                    }

                    contentStream.newLine();
                }
            }

            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

}
