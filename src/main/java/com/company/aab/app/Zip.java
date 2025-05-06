package com.company.aab.app;

import com.company.aab.entity.WithFile;
import io.jmix.core.FileStorageLocator;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {
    public static byte[] createZipArchive(List<? extends WithFile> reportOutputDocuments, FileStorageLocator fileStorageLocator) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(bos);
        Map<String, Integer> alreadyUsedNames = new HashMap();

        try {
            Iterator var13 = reportOutputDocuments.iterator();

            while(var13.hasNext()) {
                WithFile reportDocument = (WithFile)var13.next();
                String documentName = reportDocument.getFile().getFileName();
                if (alreadyUsedNames.containsKey(documentName)) {
                    int newCount = (Integer)alreadyUsedNames.get(documentName) + 1;
                    alreadyUsedNames.put(documentName,  newCount);
                    documentName = StringUtils.substringBeforeLast(documentName, ".") + newCount + "." + StringUtils.substringAfterLast(documentName, ".");
                    alreadyUsedNames.put(documentName, 1);
                } else {
                    alreadyUsedNames.put(documentName, 1);
                }

                InputStream bis = fileStorageLocator.getDefault().openStream(reportDocument.getFile());
                ZipEntry zipEntry = new ZipEntry(documentName);
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];

                int length;
                while((length = bis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }

                zipOut.closeEntry();
                bis.close();
            }

            zipOut.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException var12) {
            IOException e = var12;
            throw new RuntimeException("Error on building reports zip archive", e);
        }
    }
}
