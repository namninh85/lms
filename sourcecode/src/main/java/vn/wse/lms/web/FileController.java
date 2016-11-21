package vn.wse.lms.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import vn.wse.lms.bean.FileMeta;
import vn.wse.lms.util.Constant;
import vn.wse.lms.util.Utils;

@Controller
@RequestMapping("/file")
public class FileController {

	private static Logger logger = LoggerFactory.getLogger(FileController.class);


	FileMeta fileMeta = null;
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public @ResponseBody LinkedList<FileMeta> upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		LinkedList<FileMeta> files = new LinkedList<FileMeta>();
		//1. build an iterator
		 Iterator<String> itr =  request.getFileNames();
		 MultipartFile mpf = null;

		 //2. get each file
		 while(itr.hasNext()){
			 
			 //2.1 get next MultipartFile
			 mpf = request.getFile(itr.next()); 
			// logger.info(mpf.getOriginalFilename() +" uploaded! "+files.size());
			 
			 String newFileName = Constant.PREFIX_FILE_TEMP+	Utils.newPrefixName() + mpf.getOriginalFilename();
			 //2.2 if files > 10 remove the first from the list
			 if(files.size() >= 10)
				 files.pop();
			 
			 //2.3 create new fileMeta
			 fileMeta = new FileMeta();
			 fileMeta.setFileName(newFileName);
			 fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
			 fileMeta.setFileType(mpf.getContentType());
			 
			 try {
				fileMeta.setBytes(mpf.getBytes());
				File file = new File(Utils.getFoldelTemp());
				if(!file.exists())
					file.mkdirs();
				// copy file to local disk (make sure the path "e.g. D:/temp/files" exists)
				FileCopyUtils.copy(mpf.getBytes(), new FileOutputStream( Utils.getFoldelTemp()+newFileName));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info(e.getMessage());
			}
			 String[] names = fileMeta.getFileName().split("_", 2);
			 
			 fileMeta.setFileName(names[names.length-1]);
			 //2.4 add to files
			 files.add(fileMeta);	 
		 }
		 
		// result will be like this
		// [{"fileName":"app_engine-85x77.png","fileSize":"8 Kb","fileType":"image/png"},...]
		return files;
 
	}
	
	@RequestMapping(value = "/temp/get/{value}", method = RequestMethod.GET)
	 public void tempGet(HttpServletResponse response,@PathVariable String value){
		 //FileMeta getFile = files.get(Integer.parseInt(value));
		File file = null;
		 try {		
			 	file = new File(Utils.getFoldelTemp()+Constant.PREFIX_FILE_TEMP+value);
			 	if(file != null){			 		
			 		response.setContentType(new MimetypesFileTypeMap().getContentType(file));
			 		response.setHeader("Content-disposition", "attachment; filename=\""+file.getName().replace("@@@", "")+"\"");
			 		FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
			 	}
		 }catch (IOException e) {
			 logger.info(e.getMessage());
		 }
	 }
	
	@RequestMapping(value = "/image/get/{value}", method = RequestMethod.GET)
	 public void imageGet(HttpServletResponse response,@PathVariable String value){
		 //FileMeta getFile = files.get(Integer.parseInt(value));
		File file = null;
		 try {		
			 	file = new File(Utils.getFolderImg()+ value);
			 	if(file != null){			 		
			 		response.setContentType(new MimetypesFileTypeMap().getContentType(file));
			 		response.setHeader("Content-disposition", "attachment; filename=\""+file.getName()+"\"");
			 		FileCopyUtils.copy(FileUtils.readFileToByteArray(file), response.getOutputStream());
			 	}
		 }catch (IOException e) {
			 logger.info(e.getMessage());
		 }
	 }
	
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam(required = true) String fileName,
			@RequestParam(value="folder" ,required = false) String folderParam,
			@RequestParam(required = false) String downloadViewID, HttpServletRequest request,
			HttpServletResponse response) {
		String userAgent = request.getHeader("user-agent");
		try {
			String domain = "";
			String path = "";
			String folder= Utils.getFolderImg();
			
			if(StringUtils.isNotBlank(folderParam) && folderParam.equalsIgnoreCase("course"))
				folder = Utils.getFolderCourse();
			
			File fileTempCheck = new File(domain + folder + fileName);
			try {
				if ( !fileTempCheck.exists() && fileName.startsWith("@@@")) {
					path = Utils.getFoldelTemp() + fileName;
				} else {
					path = folder + fileName;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			File fileTemp = new File(domain + path);

			FileInputStream fileInputStream = new FileInputStream(fileTemp);
			byte[] arr = new byte[1024];
			int numRead = -1;

			response.addHeader("Content-Length", Long.toString(fileTemp.length()));
			if (StringUtils.isNotEmpty(request.getParameter("isDownload")) && request.getParameter("isDownload").equalsIgnoreCase("download")) {
				response.setContentType("application/octet-stream");
			} else {
				response.setContentType(getContentType(fileName));
			}
		
			response.addHeader("Content-Disposition", "inline; filename=\"" + fileTemp.getName() + "\"");
			while ((numRead = fileInputStream.read(arr)) != -1) {
				response.getOutputStream().write(arr, 0, numRead);
			}
			fileInputStream.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public String getContentType(String fileName) {
		String contentType = "";
		String extension = "";
		if (fileName.lastIndexOf('.') != -1) { // file co extent
			extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length()).toLowerCase();
		}

		if (extension.equals("pdf"))
			contentType = "application/pdf";
		else if (extension.equals("txt"))
			contentType = "text/plain";
		else if (extension.equals("exe"))
			contentType = "application/octet-stream";
		else if (extension.equals("zip"))
			contentType = "application/zip";
		else if (extension.equals("doc"))
			contentType = "application/msword";
		else if (extension.equals("xls"))
			contentType = "application/vnd.ms-excel";
		else if (extension.equals("xlsx"))
			contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		else if (extension.equals("ppt"))
			contentType = "application/vnd.ms-powerpoint";
		else if (extension.equals("gif"))
			contentType = "image/gif";
		else if (extension.equals("png"))
			contentType = "image/png";
		else if (extension.equals("jpeg"))
			contentType = "image/jpeg";
		else if (extension.equals("jpg"))
			contentType = "image/jpeg";
		else if (extension.equals("mp3"))
			contentType = "audio/mpeg";
		else if (extension.equals("wav"))
			contentType = "audio/x-wav";
		else if (extension.equals("mpeg"))
			contentType = "video/mpeg";
		else if (extension.equals("mpg"))
			contentType = "video/mpeg";
		else if (extension.equals("mpe"))
			contentType = "video/mpeg";
		else if (extension.equals("mov"))
			contentType = "video/quicktime";
		else if (extension.equals("avi"))
			contentType = "video/x-msvideo";
		else if (extension.equals("flv"))
			contentType = "video/flv";
		else
			contentType = "application/octet-stream";

		return contentType;
	}
	
	
	public static String getExtension(String mineType) {
		String extension = "";
	
		if (mineType.equals("application/pdf"))
			extension = "pdf";
		else if (mineType.equals("text/plain"))
			extension = "txt";
		else if (mineType.equals("application/octet-stream"))
			extension = "exe";
		else if (mineType.equals("application/zip"))
			extension = "zip";
		else if (mineType.equals("application/x-rar"))
			extension = "rar";
		else if (mineType.equals("application/msword"))
			extension = "doc";
		else if (mineType.equals("application/vnd.google-apps.form"))
			extension = "google_form";
		else if (mineType.equals("application/vnd.ms-excel"))
			extension = "xls";
		else if (mineType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || mineType.equals("application/vnd.google-apps.spreadsheet"))
			extension = "xlsx";
		else if (mineType.equals("application/vnd.ms-powerpoint"))
			extension = "ppt";
		else if (mineType.equals("image/gif"))
			extension = "gif";
		else if (mineType.equals("image/png"))
			extension = "png";
		else if (mineType.equals("image/jpeg"))
			extension = "jpeg";
		else if (mineType.equals("image/jpeg"))
			extension = "jpg";
		else if (mineType.equals("audio/mpeg"))
			extension = "mp3";
		else if (mineType.equals("audio/x-wav"))
			extension = "wav";
		else if (mineType.equals("video/mpeg"))
			extension = "mpeg";
		else if (mineType.equals("video/mpeg"))
			extension = "mpg";
		else if (mineType.equals("video/mpeg"))
			extension = "mpe";
		else if (mineType.equals("video/quicktime"))
			extension = "mov";
		else if (mineType.equals("video/x-msvideo"))
			extension = "avi";
		else if (mineType.equals("video/flv"))
			extension = "flv";
		else
			extension = "application/octet-stream";

		return "."+extension;
	}
}
