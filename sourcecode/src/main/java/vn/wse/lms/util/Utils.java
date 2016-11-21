package vn.wse.lms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.FileList;


@Component
public class Utils {
	private static Logger logger = LoggerFactory.getLogger(Utils.class);
	
    @Autowired ServletContext context;
	
	public static String foldelTemp;
	public static String folderImg;
	public static String folderCourse;
	
	private static String conextPath;
	private static String rootPath;
	
	@PostConstruct
	public void init(){
		conextPath =context.getContextPath();
		rootPath = context.getRealPath("/");
		foldelTemp = rootPath + "temp/files/";
		folderImg = rootPath + "image/files/";
		folderCourse = rootPath + "course/files/";
		
	}
	
	public static String getFoldelTemp() {
		return foldelTemp;
	}
	public static String getFolderImg() {
		return folderImg;
	}
	public static String getFolderCourse() {
		return folderCourse;
	}

	public static void setFolderCourse(String folderCourse) {
		Utils.folderCourse = folderCourse;
	}

	public static String getConextPath() {
		return conextPath;
	}
	public static String getRootPath() {
		return rootPath;
	}
	
	public static String getCountQuery(String query) {
		String countQuery = "select count(*) as count ";
		countQuery += query.substring(query.indexOf("FROM"));
		return countQuery;
	}
	
	public static void move(String fileNeedToMove, String folderFromName, String folderToName) {
		try {
			File folderFrom = new File(folderFromName);
			if (!folderFrom.exists())
				folderFrom.mkdirs();
			
			File folderTo = new File(folderToName);
			if (!folderTo.exists())
				folderTo.mkdirs();
			
			File file = new File(folderFromName + fileNeedToMove);
			if(file.exists()){				
				String[] names = null;	
				if(fileNeedToMove.contains(Constant.PREFIX_FILE_TEMP))
					names = fileNeedToMove.split("_", 2);
				else
					names[0] = fileNeedToMove;
				
				FileCopyUtils.copy(FileUtils.readFileToByteArray(file), new FileOutputStream(folderToName + names[names.length-1]));
			}
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	public static boolean isFileExits(String path, String fileName) {
		if(StringUtils.isNotBlank(path) && StringUtils.isNotBlank(fileName)){
			File file = new File(path+fileName);
			return file.exists();
		}
		return false;
	}
	
	public boolean isFileExitsInDrive(String fileId){
		Drive driveService;
		try {
			driveService = GoogleSpreadSheetUtil.getDriveService();
			
			List<com.google.api.services.drive.model.File> fileInDrive = new ArrayList<>();
			 String pageToken = null;
			 String query = "'"+Constant.FOLDER_ID_GOOGLE_DRIVE_SHARE+"' in parents";
			 FileList result;
			 do {
			     result = driveService.files().list().setQ(query).execute();
			     fileInDrive.addAll(result.getItems());
			     pageToken = result.getNextPageToken();
			 } while (pageToken != null);
			
			 for (com.google.api.services.drive.model.File file : fileInDrive) {
				 if(file.getId().equals(fileId)){
					logger.info("File "+ fileId +" exits");
					
					return true;
		        }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}
	
	
	public static String newPrefixName(){
		return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) +"_";
	}
	
}
