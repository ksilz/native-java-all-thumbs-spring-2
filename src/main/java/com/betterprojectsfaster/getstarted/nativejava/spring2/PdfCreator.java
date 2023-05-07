package com.betterprojectsfaster.getstarted.nativejava.spring2;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PdfCreator {

  public static final int NUMBER_OF_RUNS = 5;

  void createPdfs() throws DocumentException {
    try {
      System.out.println(
              "This program will convert all JPG, PNG, and GIF pictures in the current directory into PDF.");
      System.out.println();
      System.out.println(
              "JVM: "
                      + SystemUtils.JAVA_VM_VENDOR
                      + " "
                      + SystemUtils.JAVA_VM_NAME
                      + " "
                      + SystemUtils.JAVA_VM_VERSION);
      System.out.println();

      var pid = ProcessHandle.current().pid();
      System.out.println("My process ID: " + pid);
      waitForInput("Press ENTER to START");

      var inputDir = new File(".");
      var files = inputDir.listFiles();

      if (files != null && files.length > 0) {
        List<File> pictureFiles =
                Arrays.stream(files)
                        .filter(
                                f ->
                                        f.isFile()
                                                && (f.getName().endsWith(".jpg")
                                                || f.getName().endsWith(".gif")
                                                || f.getName().endsWith(".png")))
                        .toList();

        if (pictureFiles.size() > 0) {
          var currentDir = inputDir.getCanonicalPath();
          var outputDir = new File(currentDir + File.separator + "pdf");
          var goOn = true;

          if (outputDir.exists() == false) {
            goOn = outputDir.mkdir();

            if (goOn == false) {
              System.err.println("ERROR: Could not create 'pdf' directory!");
            }
          }

          if (goOn) {
            System.out.println(
                    "Creating PDFs for " + pictureFiles.size() + " pictures in the 'pdf' directory...");
            var start = System.currentTimeMillis();

            for (var i = 1; i <= NUMBER_OF_RUNS; i++) {
              System.out.println();
              System.out.println("Pass " + i + "/" + NUMBER_OF_RUNS);
              var counter = 1;

              for (var aFile : pictureFiles) {
                System.out.print("\r  File " + counter);
                var document = new Document(PageSize.LETTER);
                var baseName = FilenameUtils.getBaseName(aFile.getName());
                var pdfFile = new File(outputDir, baseName + ".pdf");
                var pdfOutputStream = new FileOutputStream(pdfFile);
                var writer = PdfWriter.getInstance(document, pdfOutputStream);

                writer.open();
                document.open();

                var imagePath = aFile.toPath().toAbsolutePath().toString();
                var image = Image.getInstance(imagePath);

                image.setAbsolutePosition(0, 0);
                image.scaleToFit(PageSize.LETTER);
                document.add(image);

                document.close();
                writer.close();
                counter++;
              }
            }

            var stop = System.currentTimeMillis();
            var duration = (stop - start) / 1000f;

            var formatter = new DecimalFormat("###,###.##");
            var formattedDuration = formatter.format(duration);
            System.out.println();
            System.out.println();
            System.out.println("Done creating PDFs in " + formattedDuration + " seconds. ");
            System.out.println();
            waitForInput("Press ENTER for garbage collection");
            System.out.println("Now sleeping for 10 seconds, hoping for garbage collection.");
            System.gc();

            try {
              Thread.sleep(10_000);
            } catch (InterruptedException e) {
              // ignore
            }

            System.out.println("Woke up from sleep. ");
          }
        } else {
          System.err.println("ERROR: No picture files found in current directory!");
        }

      } else {
        System.err.println("ERROR: No files found in current directory");
      }

      waitForInput("Press ENTER to STOP");
    } catch (IOException e) {
      System.err.println("ERROR: Fatal error calculating thumbnails!");
      e.printStackTrace();
      System.exit(1);
    }
  }

  void waitForInput(String message) {
    System.out.println();
    System.out.println(message);
    new Scanner(System.in).nextLine();
  }
}
