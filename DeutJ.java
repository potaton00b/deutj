/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.spectraextension;
import java.io.File;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.WindowManager;
import ij.io.FileSaver;
import ij.plugin.PlugIn;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageConverter;
import java.awt.Window;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.List;
import ij.measure.Measurements;
import ij.measure.ResultsTable;
import ij.plugin.Duplicator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.util.Arrays;
import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import ij.Imagej


/**
 *
 * @author sunny
 */
public class SpectraExtension extends Application implements PlugIn{
    //public static boolean runUI = true;
    
    public static void main(String[] args) {
        boolean runUI = false;
        if (runUI){
            System.out.println("I get run");
            Application.launch(UI.class, args);
            launch(args);
            //System.out.println("i get here");
        }
        

        
        
        

        System.out.println("we got here at least");
        Class<?> clazz = SpectraExtension.class;
        String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
	String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
	System.setProperty("plugins.dir", pluginsDir);
        
        ImageJ ij = new ImageJ();
        System.out.println("and also here");
        IJ.runPlugIn(clazz.getName(), "");
        
        
        
    }
    
     //@Override
    public void run(String arg) {
        System.out.println("maybe even here??");
        //String fileDir = "C:\\Users\\sunny\\Downloads\\TestDataSetOfThresholding\\test";
        String fileDir = "C:\\Users\\sunny\\Documents\\SpectraGenetics\\20220808 JJ Exposure Test";
        //String fileDir = "C:\\Users\\sunny\\Documents\\SpectraGenetics\\20220804 NJ 12spot 48 wells sups\\20220804 NJ 48 well 12 spot t30";
        String prefix = "";
        String suffix = "_Overlay";
        
        boolean runExcel = false;
        //CREATE THE EXCEL
        if (runExcel){

            try {
                String excel = "C:\\Users\\sunny\\Documents\\masterFolder\\macroed excel v2.xlsm";
//String excel = ".\\masterFolder\\macroed excel v2.xlsm";
		String sheetName = "C";
		FileInputStream input = new FileInputStream(excel);
		XSSFWorkbook workbook = new XSSFWorkbook(input);
		CreationHelper createHelper = workbook.getCreationHelper();
		XSSFCellStyle hlinkstyle = workbook.createCellStyle();
                XSSFFont hlinkfont = workbook.createFont();
                hlinkfont.setUnderline(XSSFFont.U_SINGLE);
                hlinkfont.setColor(IndexedColors.BLUE.index);
                hlinkstyle.setFont(hlinkfont);
		
		for (int c = 1; c <= 12; c++) {
			
			sheetName = sheetName.substring(0, 1) + String.valueOf(c);
			XSSFSheet sheet = workbook.getSheet(sheetName);
			
			for (int w = 0; w < 8; w++) {
				for (int s = 1; s <= 12; s++) {
					XSSFHyperlink link = (XSSFHyperlink)createHelper.createHyperlink(HyperlinkType.FILE);
					int rowNum = 20 * w + s;
					XSSFRow row = sheet.getRow(rowNum);
					if (row == null) {
						row = sheet.createRow(rowNum);
					}
					XSSFCell cell = row.createCell(9);
					cell.setCellValue(s);
					String linkAddress = "Column%20" + String.valueOf(c) + "/" + Character.toString((char)65 + w) + String.valueOf(c) + "/" + "spot%20" + String.valueOf(s) + "/OrigImg%20of%20spot" + String.valueOf(s) + ".tif";
					link.setAddress(linkAddress);
					cell.setHyperlink(link);
					cell.setCellStyle(hlinkstyle);
				}
			}
		}
		
		
		FileOutputStream output = new FileOutputStream(excel);
		workbook.write(output);
		org.apache.commons.io.IOUtils.closeQuietly(output);
		
		System.out.println("The spreadsheet is filled!");
            } catch (Exception e){
                System.out.println("oops there was a problem with writing to excel");
            }
        }
       
        //IJ.wait(9999999);
            File file = new File(fileDir);
            String[] directories = file.list(new FileNameFilter(prefix));
            System.out.println(Arrays.toString(directories));
            //BRYANS CODE FOR MAKING THE FOLDERS
            
            
            ArrayList<String> fullDirect = new ArrayList<>();
            for (int i = 0; i < directories.length; i++){
                if (i ==0){
                    new File(fileDir + "\\masks").mkdirs();
                    new File(fileDir + "\\masks\\"+ directories[i]).mkdirs();
                } else {
                    new File(fileDir + "\\masks\\"+ directories[i]).mkdirs();
                }
                fullDirect.add(fileDir + "\\"+ directories[i]);
                System.out.println(fullDirect.get(i));
            }
            
                     
        
        
        /*File file = new File(fileDirFolder);
        String[] directories = file.list(new FileNameFilter(prefix));
        System.out.println(Arrays.toString(directories));*/
        
        String fileDirFolder = fileDir;
        List<String> fileList = new ArrayList<>();
        //getAllFiles(fileDir, fileList, ".tif", "");
        getAllFiles(fileDirFolder, fileList, ".tif", suffix);
        System.out.println("this is the file list: " + fileList);
        //IJ.wait(99999);
        
        int fileListSize = fileList.size();
        char startChar = 'A';
        char endChar = 'H';
        int startNum = 1;
        int endNum = 6;
        
        System.out.println("value is: " + (endChar - startChar + 1) + " times " + (endNum+1-startNum));
        int numWellsInSet = (endChar+1-startChar)* (endNum+1-startNum);
        String[] wellList = new String[numWellsInSet];
        int letterTot = endChar-startChar + 1;
        System.out.println("colNum " + letterTot);
        int numTot = endNum - startNum + 1;
        System.out.println("startNum" + numTot);
        for (int i = 0; i < letterTot; i++){
            for (int k = 0; k < numTot; k++){
                System.out.println("this is " + Character.toString((startChar + i)) + " wth num of " + (startNum + k) + " with index of " + (i*numTot + k));
                wellList[i*numTot + k] = Character.toString((startChar + i)) + (startNum+k);
                System.out.println(wellList[i*numTot + k]);
            }
            //System.out.println(wellList.toString());
        }
        
       // IJ.wait(999999);
        
        
        //*******HUGE FOR LOOP THAT CIRCULATES ENTIRE FOLDER TO FIND FILES WITH CERTAIN PREFIX****
        int tempFileList = fileList.size();//fileList.size();//change to fileList.size() in final program
        System.out.println("number of iterations" + tempFileList);
        for (int i = 0; i < tempFileList; i++){
            String imageDir = fileList.get(i);
            System.out.println("imagedir of index " + i + " is " + imageDir);
            //IJ.wait(9999999);
        
        

        ImagePlus imp = IJ.openImage(imageDir);
        System.out.println("the width is: " + imp.getWidth());
        System.out.println("the height is: " + imp.getHeight());
        int numAcross = 4;
        int numDown = 3;
        int widthPer = (int) Math.round(imp.getWidth()/((double)numAcross));
        int heightPer = (int) Math.round(imp.getHeight()/((double)numDown));
        System.out.println("each spot should have a dimension of: " + widthPer + " wide and a height of " + heightPer);
        imp.show();
        //IJ.wait(99999);
        
        //IJ.wait(99999);
        //IJ.run(imp, "8-bit", "");
        Duplicator duper = new Duplicator();
        imp.show();
        ImagePlus combinedImg = duper.run(imp);
        //IJ.run(combinedImg, "RGB Color", "");
        combinedImg.show();
        //ImageConverter imgconv
        //IJ.wait(999999);
        IJ.run(combinedImg, "RGB Color", "");
        combinedImg.show();
        //IJ.wait(999999);
        //.wait(90);
        boolean newDataSet = true;
        if (!newDataSet){
            IJ.run(combinedImg, "Enhance Contrast...", "saturated=0.25 normalize");
        }

        //combinedImg.show();
        //System.out.println("showed combined img");
       
        //CloseAllImgs();
        ImageStack imgStk =imp.getImageStack();
        imgStk.deleteLastSlice();
        imgStk.deleteLastSlice();
        imp = new ImagePlus("redOnly", imgStk);
        imp.show();
        
        ImagePlus redOnlyImp = duper.run(imp);
        redOnlyImp.show();
        
        //TESTTING IN PROGRESS
        //combinedImg.setTitle("SpotMap");
        FileSaver saveBigImg = new FileSaver(combinedImg);
        String bigImgSaveDir = "C:\\Users\\sunny\\Documents\\masterFolder\\Column "  + wellList[i].substring(1)  + "\\" + wellList[i];
        System.out.println("big img save dir is " + bigImgSaveDir);
        saveBigImg.saveAsPng(bigImgSaveDir + "\\SpotMap.png");
        
        //saveBigImg.save
        
        //IJ.wait(99999);
        
        
        //ImageConverter ic = new ImageConverter(imp);
        //ic.convertToGray8();
        //imp.updateAndDraw();
        //IJ.run(imp, "Enhance Contrast...", "saturated=0.2");
        //imp.show();
        //IJ.wait(99999);
        if (!newDataSet){
            IJ.run(redOnlyImp, "Enhance Contrast...", "saturated=0.35 normalize");
        }
        
        ImagePlus tempImp = WindowManager.getCurrentImage();
        tempImp.setTitle("this a tempImp");
        tempImp.show();
        //IJ.wait(99999);  
        
        
        boolean testing2 = true;
        int index;
        int numWells = 1; // change to directories.length in the actual final program
        if (testing2){
        for (int k = 0; k < numWells; k++){
                for (int y = 0; y < numDown; y++){
                    for (int x = 0; x < numAcross; x++){
                        System.out.println("we are on index.." + ((y)*numAcross + x+1));
                        System.out.println("x1: " + widthPer*x);
                        System.out.println("y1: " + heightPer*y);
                        System.out.println("x2: " + widthPer*(x+1));
                        System.out.println("y2: " + heightPer*(y+1));
                        System.out.println("index: " + (numAcross*y + x+1));
                        //System.out.println("******** end of iteration, x: " + x + " and y is " + y);
                        System.out.println(fileDir);

                        //ImagePlus thisImp = dupe.run(imp);
                        index = ((y)*numAcross + x+1);
                        System.out.println("went past duplicator and just before method for index " + index);
                        saveMasks(tempImp, widthPer*x, heightPer*y, widthPer, heightPer, fileDir, index, combinedImg, directories, i, wellList);
                    }
                }
            }
        }

        CloseAllImgs();
    }//end of for loop
        
    }
    


        public static void saveMasks(ImagePlus imp, int x1, int y1, int widthPer, int heightPer, String fileDir, int index, ImagePlus redOnly, String[] directories, int dirIndex, String[] wellList){
            //Duplicator dupe = new Duplicator();
            //ImagePlus imp = dupe.run(impWillDupe);           
            //imp.show();           
            //System.out.println("showed imp image");
            IJ.run(imp, "Enhance Contrast...", "saturated=0.35 normalize");
            IJ.run(imp, "Subtract Background...", "rolling=25 separate");
            
            IJ.run(imp, "8-bit", "");
            imp.setRoi(x1,y1,widthPer, heightPer);
            redOnly.setRoi(x1,y1, widthPer, heightPer);
            System.out.println("set ROI");
            imp = imp.resize(widthPer, heightPer, "bilinear");
            
            
            IJ.run(redOnly, "Enhance Contrast...", "saturated=0.35 normalize");
            redOnly = redOnly.resize(widthPer, heightPer, "bilinear");
            System.out.println("resized image");
            IJ.run(redOnly, "RGB Color", "");
            
            
            System.out.println("converted origimg into rgb color");
            redOnly.show();
            //IJ.wait(99999);
            IJ.run(redOnly, "Set Scale...", "distance=1 known=1 unit=[]");
            redOnly.setTitle("OrigImg of spot" + index);
            FileSaver saverMachineRed = new FileSaver(redOnly);
            System.out.println("folder for direotries is: " + directories[dirIndex]);
            //IJ.wait(99999);
            String tempDir = fileDir + "\\masks\\"+ directories[dirIndex] +"\\spot " + index;
            String realDir = "C:\\Users\\sunny\\Documents\\masterFolder\\Column " + wellList[dirIndex].substring(1) + "\\" + wellList[dirIndex] + "\\" + "spot " + index;
            
            System.out.println("realDir is " + realDir);
            System.out.println("tempDir is " + tempDir);
            new File(tempDir).mkdirs();
            saverMachineRed.saveAsTiff(tempDir + "\\" + redOnly.getTitle() + ".tif");
            //saverMachineRed.saveAsTiff(realDir + "\\" + redOnly.getTitle() + ".tif");
            System.out.println("absolute path is: " + fileDir+"\\masks\\"+directories[dirIndex] +"\\"+ redOnly.getTitle() + ".tif");
            System.out.println("saved file of red only"); 
            //IJ.wait(999999);
            
            //imp.show();
            System.out.println("showed resized image"  + "");
            boolean testing = false;
            if (testing){
                IJ.run(imp, "Enhance Contrast...", "saturated=0.35 normalize");
                imp.show();
                IJ.wait(99999);
            }
            
           
            fiji.threshold.Auto_Threshold autoThreshNL = new fiji.threshold.Auto_Threshold();
            autoThreshNL.exec(imp, "Triangle", true, true, true, false, false, true);
            //imp.show();
            System.out.println("applied threshold");
            IJ.run(imp, "Convert to Mask", "");
            
            
            //imp.show();
            
            
            
            IJ.run(imp, "Despeckle", "");
            //imp.show();
            
            //COMMENT OUT TEMPORARILY 8/5/2022, RETURN LATER
            IJ.run(imp, "Analyze Particles...", "size=25-500 show=Masks");
            //COMMENT OUT TEMPORARILY 8/5/2022, RETURN LATER
            
            
            //imp.show();
            
            
            ImagePlus imp2 = WindowManager.getImage("Mask of Untitled");
            
            System.out.println("analyzed particles");
            //IJ.wait(99999);
            //IJ.wait(99999);
            //imp2.show();
            
            System.out.println("showed imp2");
            IJ.run(imp2, "Despeckle", "");
            System.out.println("showing despeckled");
            //imp2.show();
            //IJ.wait(99999);
            /*ij.plugin.filter.EDM ws = new ij.plugin.filter.EDM();
            ws.toWatershed(imp2.getProcessor());*/
            //Prefs.blackBackground = false;
            //IJ.run(imp2, "Erode", "");
            //imp2.show();
            //IJ.wait(99999);
            
            //delete if not working
            
           
            imp2.setTitle("mask for spot " + index);
            System.out.println("changed title");
            
            /*IJ.run(imp2, "Invert LUT", "");
            System.out.println("invert lut?");*/
            
            IJ.run(imp2, "Watershed", "");
            //IJ.run(imp2, "Dilate", "");
            
            //IJ.run(imp2, "Despeckle", "");
            imp2.show();
            //IJ.wait(99999);
            //imp2.show();
            
            FileSaver saverMachine = new FileSaver(imp2);
            saverMachine.saveAsTiff(tempDir + "\\" + imp2.getTitle() + ".tif");
            //saverMachine.saveAsTiff(realDir + "\\" + imp2.getTitle() + ".tif");
            /*
            BRYANS CODE
            
            /*
            */
            System.out.println("saved file");         
            //CloseAllImgs(); --> For some reason breaks the loop??? imagej is dumb
            System.out.println("closed all images");
            System.out.println("********made it to the other side*********");
            
        
    }
        
        
        
        public static void Threshold(String imageDir){
        ImagePlus imp = IJ.openImage(imageDir);
        
        System.out.println("the width is: " + imp.getWidth());
        System.out.println("the height is: " + imp.getHeight());
        double numAcross = 4;
        double numDown = 3;
        System.out.println("each spot should have a dimension of: " + imp.getWidth()/numAcross + "wide and a height of " + imp.getHeight()/numDown);
        imp.show();
        imp.setTitle("test1");
        IJ.selectWindow("test1");
        imp.show();
        IJ.selectWindow("test1");
        mpicbg.ij.clahe.PlugIn ELC = new mpicbg.ij.clahe.PlugIn();
        ELC.run(imp);
        imp.show();
        IJ.run(imp, "8-bit", "");
        imp.show();
        fiji.threshold.Auto_Threshold autoThreshNL = new fiji.threshold.Auto_Threshold();
        autoThreshNL.exec(imp, "Otsu", true, true, true, false, false, true);
        imp.show();
    }
    
    private static void getAllFiles(String path, List<String> fileList, String extension, String subStr) {
        File[] allFiles = new File(path).listFiles();
        
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isFile() && file.getName().endsWith(extension) && file.getName().contains(subStr) && !file.getName().startsWith("._")) {
                    fileList.add(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath(), fileList, extension, subStr);
                }
            }
        }
    }
    


    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("start is run");
        
        Parent root2 = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        stage.setTitle("hello word");
        stage.setScene(new Scene(root2, 300, 275));
        stage.show();
        
        boolean testing = false;
        if (testing){
            File file = new File("sample.fxml");
            System.out.println(file.getAbsolutePath());
            //URL url = new URL(file.getAbsolutePath());
            FXMLLoader root = new FXMLLoader(SpectraExtension.class.getResource("sample.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
             AnchorPane pane = root.load();

            Scene scene = new Scene(pane);
            System.out. println("it got here");
            stage.setScene(scene);
            stage.setTitle("firsts window");
            stage.show();
        }
        
    }
    
    public static void CloseAllImgs(){
        String[] titles=WindowManager.getImageTitles();
        if (titles.length!=0){
            for (String s:titles){
                ImagePlus im=WindowManager.getImage(s);
                im.changes = false; 
                im.close();
                
            }
        }
            String[] non_img_titles=WindowManager.getNonImageTitles();
            if (non_img_titles.length!=0){
            for (String s:non_img_titles){
                if (!"Log".equals(s)){
                    Window w=WindowManager.getWindow(s);
                    w.dispose();
                }
            }
            }
    }

 

   
}


