package vn.wse.lms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.drive.DriveFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ParentReference;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import vn.wse.lms.web.FileController;

public class GoogleSpreadSheetUtil {

	private static final String APPLICATION_NAME = "lms-application";
	private static final String ACCOUNT_P12_ID = "lms-serviceaccount@wse-lms.iam.gserviceaccount.com";
	private static final File P12FILE = new File(Utils.getRootPath() + "key.p12");

	private static final List<String> SCOPES = Arrays.asList("https://docs.google.com/feeds",
			"https://spreadsheets.google.com/feeds","https://www.googleapis.com/auth/drive","https://www.googleapis.com/auth/drive.file", "https://www.googleapis.com/auth/drive.metadata","https://www.googleapis.com/auth/drive.file", "https://www.googleapis.com/auth/drive.file");

	private static final URL SPREADSHEET_FEED_URL;
//1KLjTUfa7XENDWF26brHqm7PkeMmPdoBaN-Fym56p9MA
	static {
		try {
			SPREADSHEET_FEED_URL = new URL(
					"https://spreadsheets.google.com/feeds/worksheets/1fCR_kadkzezwflYaRCRHBTWomzySSpKFSsHcPCp5dcw/public/basic");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public static SpreadsheetService getSpreadsheetService() throws Exception {
		System.out.println("service in");

		SpreadsheetService service = new SpreadsheetService(APPLICATION_NAME);
		service.setProtocolVersion(SpreadsheetService.Versions.V3);

		Credential credential = authorize();
		service.setOAuth2Credentials(credential);

		// debug dump
		System.out.println("Schema: " + service.getSchema().toString());
		System.out.println("Protocol: " + service.getProtocolVersion().getVersionString());
		System.out.println("ServiceVersion: " + service.getServiceVersion());

		System.out.println("service out");

		return service;
	}
	
	public static Drive getDriveService() throws Exception {
		System.out.println("service in");

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = new JacksonFactory();
	
		Credential credential = authorize();
		Drive service = new Drive.Builder(httpTransport, jsonFactory, null).setApplicationName("DRIVE_TEST").setHttpRequestInitializer(credential).build();
		return service;
	}

	private static Credential authorize() throws Exception {
		System.out.println("authorize in");

		HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		if (!P12FILE.exists())
			throw new Exception("ko co file");
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
				.setJsonFactory(jsonFactory).setServiceAccountId(ACCOUNT_P12_ID)
				.setServiceAccountPrivateKeyFromP12File(P12FILE).setServiceAccountScopes(SCOPES).build();

		boolean ret = credential.refreshToken();
		// debug dump
		System.out.println("refreshToken:" + ret);

		// debug dump
		if (credential != null) {
			System.out.println("AccessToken:" + credential.getAccessToken());
		}

		System.out.println("authorize out");

		return credential;
	}

	public static List<WorksheetEntry> findAllWorksheetEntry(SpreadsheetService service) throws Exception {
		if (service == null)
			service = getSpreadsheetService();

		WorksheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, WorksheetFeed.class);
		List<WorksheetEntry> worksheetEntries = feed.getEntries();

		// Iterate through all of the spreadsheets returned
		for (WorksheetEntry worksheet : worksheetEntries) {
			// Print the title of this worksheetEntrys to the screen
			System.out.println(worksheet.getTitle().getPlainText());
			int rowCount = worksheet.getRowCount();
			int colCount = worksheet.getColCount();
			// Print the fetched information to the screen for this worksheet.
			System.out.println("- rows:" + rowCount + " cols: " + colCount);
			
		}

		return worksheetEntries;
	}

	public static void parseCell(WorksheetEntry worksheetEntry, SpreadsheetService service)
			throws IOException, ServiceException {

		// Fetch column 4, and every row after row 1.
		/*
		 * URL cellFeedUrl = new URI(worksheet.getCellFeedUrl().toString() +
		 * "?min-row=2&min-col=4&max-col=4").toURL();
		 */
		
		// Fetch the cell feed of the worksheet.
		URL cellFeedUrl = worksheetEntry.getCellFeedUrl();
		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

		// Iterate through each cell, printing its value.
		for (CellEntry cell : cellFeed.getEntries()) {
			// Print the cell's address in A1 notation
			System.out.print(cell.getTitle().getPlainText() + "\t");
			// Print the cell's address in R1C1 notation
			System.out.print(cell.getId().substring(cell.getId().lastIndexOf('/') + 1) + "\t");
			// Print the cell's formula or text value
			System.out.print(cell.getCell().getInputValue() + "\t");
			// Print the cell's calculated value if the cell's value is numeric
			// Prints empty string if cell's value is not numeric
			System.out.print(cell.getCell().getNumericValue() + "\t");
			// Print the cell's displayed value (useful if the cell has a
			// formula)
			System.out.println(cell.getCell().getValue() + "\t");
		}
	}

	public static void parseRow(WorksheetEntry worksheetEntry, SpreadsheetService service)
			throws IOException, ServiceException {

		// Fetch the list feed of the worksheet.
		URL listFeedUrl = worksheetEntry.getListFeedUrl();
		ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);

		// Iterate through each row, printing its cell values.
		for (ListEntry row : listFeed.getEntries()) {
			// Print the first column's cell value
			System.out.print(row.getTitle().getPlainText() + "\t");
			// Iterate over the remaining columns, and print each cell value
			for (String tag : row.getCustomElements().getTags()) {
				System.out.print(row.getCustomElements().getValue(tag) + "\t");
			}
			
		}
	}
	
	
	public static com.google.api.services.drive.model.File insertFile(Drive service, String title, String description,
		      String parentId, String mimeType, InputStream instream) throws Exception {
		    // File's metadata.
			com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
		    body.setTitle(title);
		    body.setDescription(description);
		    body.setMimeType(mimeType);

		    // Set the parent folder.
		    if (parentId != null && parentId.length() > 0) {
		      body.setParents(
		          Arrays.asList(new ParentReference().setId(parentId)));
		    }

			final java.io.File fileContent = java.io.File.createTempFile("temp", ".tmp");
			FileUtils.copyInputStreamToFile(instream, fileContent);

		    // File's content.
		    if(fileContent == null)
		    	throw new Exception("File not found!!");
		    	
		    FileContent mediaContent = new FileContent(mimeType, fileContent);
		    try {
		    com.google.api.services.drive.model.File file = service.files().insert(body, mediaContent).execute();

		      // Uncomment the following line to print the File ID.
		       System.out.println("File ID: %s" + file.getId());
		    
		      return file;
		    } catch (IOException e) {
		      System.out.println("An error occured: " + e);
		      return null;
		    }
	}
	
	public static HttpEntity geHttpEntityFileFromDrive(String exportUri,Google google) throws IOException, ClientProtocolException {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(exportUri);
		httpget.setHeader("Authorization", "Bearer " + google.getAccessToken());
		HttpResponse resp = client.execute(httpget);
		HttpEntity entity = resp.getEntity();
		return entity;
	}
	
	public static String dowloadFile(Drive driveService, DriveFile file, Google google) throws Exception{
		
		String exportUri = file.getExportLinks() != null ? file.getExportLinks().get(convertMineTypeSpreadsheet(file.getMimeType())): null;
		
		if(exportUri == null)
			exportUri = "https://docs.google.com/uc?export=download&id="+file.getId();
		
		if (exportUri != null) {
			
			HttpEntity entity = geHttpEntityFileFromDrive(exportUri, google);
			
			if (entity != null) {
				InputStream instream = entity.getContent();
				
				File folder = new File(Utils.getFolderCourse());
				if(!folder.exists())
					folder.mkdirs();
				
				String newFileName =Utils.newPrefixName()+file.getTitle() ;
				
				String extentsion = FileController.getExtension(file.getMimeType());				
				if(!newFileName.contains(extentsion))
					newFileName += extentsion;
				
				File targetFile = new File(Utils.getFolderCourse()+newFileName);
				 
				FileUtils.copyInputStreamToFile(instream, targetFile);
							
				return newFileName;
			} else {
				throw new Exception("Response entity is null!");
			}
		} else {
			throw new Exception("No Excel export found!");
		}
	}
	
	private static String convertMineTypeSpreadsheet(String mineType){
		if("application/vnd.google-apps.spreadsheet".contains(mineType)){
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		}
		
		return mineType;
	}
	
	public void insertCell(){
		/*
		 * URL cellFeedUrl= worksheet.getCellFeedUrl (); CellFeed cellFeed=
		 * service.getFeed (cellFeedUrl, CellFeed.class);
		 * 
		 * CellEntry cellEntry= new CellEntry (1, 1, "abbba");
		 * cellFeed.insert (cellEntry);
		 */
	}

}
