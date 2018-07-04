package com.buildbyte.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.buildbyte.dto.AppliedJobDetailsDTO;
import com.buildbyte.dto.JobDTO;
import com.buildbyte.dto.JobDetailsDTO;
import com.buildbyte.dto.UserRequestDTO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class JobsDAO {
	private final SimpleDateFormat sdf  = new SimpleDateFormat("MM/dd/yyyy");
	private final MongoCollection<Document> jobsCollection;

    public JobsDAO(final MongoDatabase mongoDatabase) {
    	jobsCollection = mongoDatabase.getCollection("jobs");
    }
    
    public JobDTO getJobDetails() {
    	JobDTO jobDTO = new JobDTO();
    	List<JobDetailsDTO> jobDetailsList = new ArrayList<JobDetailsDTO>();
    	jobDTO.setJobDetails(jobDetailsList);
    	try{
	    	List<Document> jobsList = jobsCollection.find().into(new ArrayList<Document>());
	    	
	    	for(Document doc : jobsList){
	    		JobDetailsDTO jobDetailsDTO = new JobDetailsDTO();
	    		jobDetailsDTO.setJobId(doc.getString("_id"));
	    		jobDetailsDTO.setTitle(doc.getString("title"));
	    		jobDetailsDTO.setDesc(doc.getString("desc"));
	    		jobDetailsDTO.setJobLocation(doc.getString("jobLocation"));
	    		jobDetailsDTO.setPostingDate(sdf.format(doc.getDate("postingDate")));
	    		jobDetailsDTO.setApplyBefore(sdf.format(doc.getDate("applyBefore")));
	    		jobDetailsDTO.setHiringManager(doc.getString("hiringManager"));
	    		jobDetailsDTO.setHmEmail(doc.getString("hmEmail"));
	    		jobDetailsList.add(jobDetailsDTO);
	    	}
	    	jobDTO.setStatus("SUCCESS");
    	}catch(Exception e){
    		e.printStackTrace();
    		jobDTO.setStatus("System error occured. Please contact Administrator");
    	}
    	return jobDTO;
    }
    
    public JobDTO getAppliedJobsDetails(UserRequestDTO req) {
    	JobDTO jobDTO = new JobDTO();
    	List<AppliedJobDetailsDTO> appliedJobDetailsList = new ArrayList<AppliedJobDetailsDTO>();
    	jobDTO.setAppliedJobs(appliedJobDetailsList);
    	try{
    		String userName = req.getUserName();
        	if(userName == null || "".equals(userName.trim())){
        		jobDTO.setStatus("User " + userName + " is not registered or doesn't exist");
        		return jobDTO;
        	}
    		Bson filter = new Document("appliedJobs.userName",userName);
	    	List<Document> appliedJobsList = jobsCollection.find(filter).into(new ArrayList<Document>());
	    	
	    	for(Document doc : appliedJobsList){
	    		AppliedJobDetailsDTO appliedJobDetailsDTO = new AppliedJobDetailsDTO();
	    		appliedJobDetailsDTO.setUserName(doc.getString("userName"));
	    		appliedJobDetailsDTO.setUserEmail(doc.getString("userEmail"));
	    		
	    		appliedJobDetailsList.add(appliedJobDetailsDTO);
	    	}
	    	jobDTO.setStatus("SUCCESS");
    	}catch(Exception e){
    		e.printStackTrace();
    		jobDTO.setStatus("System error occured. Please contact Administrator");
    	}
    	return jobDTO;
    }

}