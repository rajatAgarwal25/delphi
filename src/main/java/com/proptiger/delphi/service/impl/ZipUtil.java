package com.proptiger.delphi.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZipUtil {

    @Value("${zip.temp.folder}")
    private String zipFolder;

    private void createFolder(String outputFolder) {
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    String zip(String folderPath) {
        createFolder(zipFolder);
        List<String> fileList = new ArrayList<>();
        generateFileList(folderPath, fileList, new File(folderPath));
        String zipFile = zipFolder + File.separator + System.currentTimeMillis() + ".zip";
        zipIt(folderPath, fileList, zipFile);
        return zipFile;
    }

    void unzip(File f, String outputFolder) {
        byte[] buffer = new byte[1024];
        try {
            // create output directory is not exists
            createFolder(outputFolder);

            // get the zip file content
            ZipInputStream zis = new ZipInputStream(new FileInputStream(f.getAbsolutePath()));
            // get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());
                // create all non exists folders
                // else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();

            System.out.println("Done");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void zipIt(String folderPath, List<String> fileList, String outputZipFile) {
        byte[] buffer = new byte[1024];
        String source = new File(folderPath).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(outputZipFile);
            zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + outputZipFile);
            FileInputStream in = null;

            for (String file : fileList) {
                System.out.println("File Added : " + file);
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try {
                    in = new FileInputStream(folderPath + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                }
                finally {
                    in.close();
                }
            }

            zos.closeEntry();
            System.out.println("Folder successfully compressed");

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                zos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(String folderPath, List<String> fileList, File node) {
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(folderPath, node.toString()));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(folderPath, fileList, new File(node, filename));
            }
        }
    }

    private String generateZipEntry(String folderPath, String file) {
        return file.substring(folderPath.length() + 1, file.length());
    }
}
