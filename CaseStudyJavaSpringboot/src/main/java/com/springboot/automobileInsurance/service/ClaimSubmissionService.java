package com.springboot.automobileInsurance.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.automobileInsurance.dto.ClaimUpdateDTO;
import com.springboot.automobileInsurance.exception.InvalidIDException;
import com.springboot.automobileInsurance.model.ClaimTable;
import com.springboot.automobileInsurance.repository.ClaimSubmissionRepository;

@Service
public class ClaimSubmissionService {
	
	@Autowired
	ClaimSubmissionRepository claimSubmissionRepository;

//	public ClaimTable submitClaim(ClaimTable claimTable,MultipartFile file) throws IOException {
//		
//		List<String> allowedExtensions = Arrays.asList("png","jpg","jpeg","gif","svg"); 
//		String originalFileName = file.getOriginalFilename(); 
//		System.out.println(originalFileName);
//		String extension= originalFileName.split("\\.")[1];
//		/*Check weather extension is allowed or not */
//		if( !(allowedExtensions.contains(extension))) {
//			throw new RuntimeException("Image Type Invalid");
//		}
//		
//		
//		String uploadPath= "C:\\Users\\karthick\\Desktop\\CaseStudyReact\\public\\images";
//		
//		/*Create directory *///Check if directory is present else create it
//		Files.createDirectories(Paths.get(uploadPath));
//		/*Define full path with folder and image name */
//		Path path = Paths.get(uploadPath + "\\" +originalFileName); 
//		/*Copy the image into uploads path */
//		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//		/*Save this path in Db */
//		claimTable.setImageUrl(path.toString());
//		
//		return claimSubmissionRepository.save(claimTable);
//	}
	
	public ClaimTable submitClaim(ClaimTable claimTable)
	{
		return claimSubmissionRepository.save(claimTable);
	}

	public ClaimTable uploadImage(int cId,MultipartFile file) throws IOException {
		
		
		
		Optional<ClaimTable> optional=claimSubmissionRepository.findById(cId);
		
		ClaimTable claimTable=optional.get();		
		List<String> allowedExtensions = Arrays.asList("png","jpg","jpeg","gif","svg"); 
		String originalFileName = file.getOriginalFilename(); 
		System.out.println(originalFileName);
		String extension= originalFileName.split("\\.")[1];
		/*Check weather extension is allowed or not */
		if( !(allowedExtensions.contains(extension))) {
			throw new RuntimeException("Image Type Invalid");
		}
		
		
		String uploadPath= "C:\\Users\\karthick\\Desktop\\CaseStudy\\Backend\\CaseStudyReact\\public\\images";
		
		/*Create directory *///Check if directory is present else create it
		Files.createDirectories(Paths.get(uploadPath));
		/*Define full path with folder and image name */
		Path path = Paths.get(uploadPath + "\\" +originalFileName); 
		/*Copy the image into uploads path */
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		/*Save this path in Db */
		claimTable.setImageUrl(path.toString());
		
		return claimSubmissionRepository.save(claimTable);
		
	}

	public List<ClaimTable> getAllClaims(int cId) {
		return claimSubmissionRepository.findByCustomerId(cId);
	}
	

	public ResponseEntity<String> updateClaim(int id, ClaimUpdateDTO claimUpdateDTO) {
		Optional<ClaimTable> optional = claimSubmissionRepository.findById(id);

		if (optional.isEmpty()) {
			throw new InvalidIDException("Invalid Claim Id" + id);
		}

		ClaimTable claimTable = optional.get();
		claimTable.setStatus(claimUpdateDTO.getStatus());
		claimTable.setFeedback(claimUpdateDTO.getFeedback());
		claimTable.setApprovedAmount(claimUpdateDTO.getApprovedAmount());

		claimSubmissionRepository.save(claimTable);
		return ResponseEntity.ok("Claim updated successfully.");
	}
	
	
}
