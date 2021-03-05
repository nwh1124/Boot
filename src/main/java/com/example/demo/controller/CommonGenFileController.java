package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.example.demo.dto.GenFile;
import com.example.demo.dto.ResultData;
import com.example.demo.service.GenFileService;

@Controller
public class CommonGenFileController extends BaseController{
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;
	
	@Autowired
	private GenFileService genFileService;
	
	@RequestMapping("/common/genFile/doUpload")
	@ResponseBody
	public ResultData doUpload(@RequestParam Map<String, Object> param, MultipartRequest multipartRequest) {
		return genFileService.saveFiles(param, multipartRequest);
	}
	
	@GetMapping("/common/genFile/doDownload")
	public ResponseEntity<Resource> downloadFile(int id, HttpServletRequest req) throws IOException{
		
		GenFile genFile = genFileService.getGenFile(id);
		String filePath = genFile.getFilePath(genFileDirPath);
		Path path = Paths.get(filePath);
		
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		String contentType = null;
		try {
			contentType = req.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {

		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + genFile.getOriginFileName() + "\"")
				.body(resource);
	}
	
}
