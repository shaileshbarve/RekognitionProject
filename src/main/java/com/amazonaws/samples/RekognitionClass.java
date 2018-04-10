package com.amazonaws.samples;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.DetectTextResult;
import com.amazonaws.services.rekognition.model.Face;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.FaceMatch;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.ListFacesRequest;
import com.amazonaws.services.rekognition.model.ListFacesResult;
import com.amazonaws.services.rekognition.model.SearchFacesByImageRequest;
import com.amazonaws.services.rekognition.model.SearchFacesByImageResult;
import com.amazonaws.services.rekognition.model.SearchFacesRequest;
import com.amazonaws.services.rekognition.model.SearchFacesResult;
import com.amazonaws.services.rekognition.model.TextDetection;

//import com.amazonaws.services.rekognitionpreview.AmazonRekognitionPreview;
//import com.amazonaws.services.rekognitionpreview.AmazonRekognitionPreviewClientBuilder;
//import com.amazonaws.services.rekognitionpreview.model.DetectCustomLabelsRequest;
//import com.amazonaws.services.rekognitionpreview.model.DetectCustomLabelsResult;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * <p>
 * Fill in your AWS access credentials in the provided credentials file
 * template, and be sure to move the file to the default location
 * (C:\\Users\\sbarve\\.aws\\credentials) where the sample code will load the
 * credentials from.
 * <p>
 * <b>WARNING:</b> To avoid accidental leakage of your credentials, DO NOT keep
 * the credentials file in your source directory.
 *
 * http://aws.amazon.com/security-credentials
 */
public class RekognitionClass {

	public static String collectionName = "myphotos";
	public static AWSCredentials credentials;
	public static AmazonRekognition client;
//	public static AmazonRekognitionPreview clientPreview; 
	public static Connection conn1 = null;
	public static Webcam webcam = null;
	static JFrame webcamWindow = null;
	
//	static DetectMotionExample motionDetector = new DetectMotionExample();

	public static StreamProcessorClass stream = new StreamProcessorClass();
	
	public static void main(String[] args) throws IOException {

		InitializeAWSRekognitionClient();

	}

	public static void InitializeAWSRekognitionClient() {
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (/Usersuserid.aws/credentials), and is in a valid format.", e);
		}

		client = AmazonRekognitionClientBuilder.standard().withRegion(Regions.US_EAST_1)
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

	}

	public static void StopWebcam() throws FileNotFoundException, IOException {
		motionDetector.stop();
	}
	
	public static void StartWebcam() throws FileNotFoundException, IOException {
		
	    try {
	    	
//	    	motionDetector.run();
		
			// automatically open if webcam is closed
		    Webcam.setAutoOpenMode(true);

		    
		    // get default webcam and open it
			webcam = Webcam.getDefault();
			webcam.setViewSize(new Dimension(640, 480));
			webcam.open();

			//create web came panel window
			WebcamPanel panel = new WebcamPanel(webcam);
			panel.setFPSDisplayed(true);
			panel.setDisplayDebugInfo(true);
			panel.setImageSizeDisplayed(true);
			panel.setMirrored(true);

			webcamWindow = new JFrame("Test webcam panel");
			webcamWindow.add(panel);
			webcamWindow.setResizable(true);
			webcamWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			webcamWindow.pack();
			webcamWindow.setVisible(true);
			
		    
		}catch (InvalidParameterException ex) {
			ex.printStackTrace();
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}
	}
	
	public static void SnapPhoto() throws FileNotFoundException, IOException {
		
		// get image
		BufferedImage image = null;

		image = Webcam.getDefault().getImage();
		int random = new Random().nextInt();
	    
		String imagePath = "C:\\Users\\sbarve\\Pictures\\Camera Roll\\" + random + ".jpg";
//		String imagePath = "C:\\Users\\sbarve\\Pictures\\Camera Roll\\" + TestUI.textField_name.getText() + ".jpg";
	    // save image to PNG file
	    ImageIO.write(image, "PNG", new File(imagePath));

//	    webcam.close();
//	    webcamWindow.setVisible(false);

	    TestUI.textField4.setText(imagePath);
	    	    
//	    JOptionPane.showMessageDialog(
//                null,
//                "",
//                "Verify your Photo", JOptionPane.INFORMATION_MESSAGE,
//                new ImageIcon ( imagePath ));
	    
	}
	
	/*
	 * Detects faces within an image that is provided as input.
	 * 
	 * DetectFaces detects the 100 largest faces in the image. For each face
	 * detected, the operation returns face details including a bounding box of the
	 * face, a confidence value (that the bounding box contains a face), and a fixed
	 * set of attributes such as facial landmarks (for example, coordinates of eye
	 * and mouth), gender, presence of beard, sunglasses, etc.
	 */
	public static void DetectFacesfromFile() throws FileNotFoundException, IOException {

			 		
		String photo = "";
		TestUI.txtrThisIsA.removeAll();

		if (TestUI.textField.getText() != null)
			photo = TestUI.textField.getText();

		try {
			// Covert photo to bytes
			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			DetectFacesRequest request = new DetectFacesRequest().withImage(new Image().withBytes(imageBytes));

			DetectFacesResult response = client.detectFaces(request);
			List<FaceDetail> labels = response.getFaceDetails();

			StringBuilder resultString = new StringBuilder();

			System.out.println("Detected faces for " + photo);
			for (FaceDetail label : labels) {
				resultString.append("Confidence level=" + label.getConfidence().toString() + ";\nFace dimentions="
						+ label.getBoundingBox().toString() + ";\n\n Full response:\n" + label.toString() + "\n\n");

				TestUI.txtrThisIsA.setText(resultString.toString());

				if (labels.isEmpty()) {
					TestUI.txtrThisIsA.setText("NO faces found.");
					// System.out.println("NO faces found.");
				}

			}

		} catch (InvalidParameterException ex) {
			ex.printStackTrace();
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}
	/*
	 * For a given input image, first detects the largest face (only 1 image) in the image, and then searches the specified collection for 
	 * matching faces. The operation compares the features of the input face with faces in the specified collection.
	 */

	public static void SearchImagesinCollection() throws FileNotFoundException, IOException {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at
		 * (C:\\Users\\sbarve\\.aws\\credentials).
		 */

		try {

			String photo = TestUI.textField4.getText();
			TestUI.txtrThisIsA.setText("");

			ByteBuffer imageBytes = null;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Image not found! Please supply proper image.");
				return;
			}

			Image imageObj = new Image().withBytes(imageBytes);

			ObjectMapper objectMapper = new ObjectMapper();
			
			//Test DetectFaces  - ADDED FOR TESTING
			DetectFacesRequest request = new DetectFacesRequest().withImage(imageObj);
			DetectFacesResult response = client.detectFaces(request);
			//End - Test DetectFaces

			// Search collection for faces similar to the largest face in the image.
			SearchFacesByImageRequest searchFacesByImageRequest = new SearchFacesByImageRequest()
					.withCollectionId(collectionName).withImage(imageObj).withFaceMatchThreshold(0F).withMaxFaces(100);

			SearchFacesByImageResult searchFacesByImageResult = client.searchFacesByImage(searchFacesByImageRequest);

			List<FaceMatch> faceImageMatches = searchFacesByImageResult.getFaceMatches();
			StringBuilder resultString = new StringBuilder();

			if (faceImageMatches.isEmpty()) {
				System.out.println("No matches found for " + photo + " in collection : " + collectionName);
				resultString.append("No matches found for " + photo + " in collection : " + collectionName);
			} 

			String faceId = "";
			String userFound = "";
			for (FaceMatch face : faceImageMatches) {

				//searchFacesByImage returns matched and unmatched faces; so look for matches above 80%  to confirm
				//that faces belong to same user only which has similarity score above 80%
				if (face.getSimilarity() > 50){		
				
					resultString.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
					faceId = face.getFace().getFaceId();
					
					//Look up this faceId in DB
					userFound = SeachFaceIdinDB(faceId);
					
					if (userFound.length() > 0)
						JOptionPane.showMessageDialog(null, "User Found == " + userFound);
					
					//If more users found over 80% threshold, then something wrong with searchFacesByImage API
					//THIS SHOULD NEVER HAPPEN
					
				}

			}

			if (resultString.toString().length() > 0)
			{
				TestUI.txtrThisIsA.setText(resultString.toString());

			} else {
				TestUI.txtrThisIsA.setText("");
				JOptionPane.showMessageDialog(null, "No Match Found!");
			}

		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			JOptionPane.showMessageDialog(null, "Invalid image! Please supply proper image.");
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	//Detects faces from the image. Get the boundingbox and call the SearchFacebyImage API on the faces found.
	public static DetectFacesResult DetectFaces() throws FileNotFoundException, IOException {


		DetectFacesRequest request = new DetectFacesRequest().withImage(new Image().withBytes(bytes));
		DetectFacesResult response = client.detectFaces(request);
		
		return response;
		
	}
	
	/*
	 * Call this API to search all faces from the source image.
	 */
	public static void SearchAllfacesinCollection() throws FileNotFoundException, IOException {

		try {

			String photo = TestUI.textField4.getText();
			TestUI.txtrThisIsA.setText("");


			//Add all faces from source image to collection
			IndexFacesResult result = AddImageToCollection(photo);

			//An array of faces detected and added to the collection
			List<FaceRecord> faceImageMatches = result.getFaceRecords();
			
			String userFound = "";
			SearchFacesResult response = null;
			for (FaceRecord face : faceImageMatches) {

				//For each face, search for matching faces in the collection
				SearchFacesRequest request = new SearchFacesRequest().withCollectionId(collectionName).withFaceId(face.getFace().getFaceId()).withMaxFaces(10)
				        .withFaceMatchThreshold(80f);
				response = client.searchFaces(request);
				
				if (response != null)
				{
					List<FaceMatch> matches = response.getFaceMatches();
					for (FaceMatch faceResult : matches) {
						//Look up this faceId in Infor DB
						userFound = SeachFaceIdinDB(faceResult.getFace().getFaceId());
						
						if (userFound.length() > 0)
							JOptionPane.showMessageDialog(null, "User Found == " + userFound);
//						else
//							JOptionPane.showMessageDialog(null, "No Match Found for == " + faceResult.getFace().getFaceId());
					}
				}
				
			}
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			JOptionPane.showMessageDialog(null, "Invalid image! Please supply proper image.");
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public static void DeleteCollection() throws FileNotFoundException, IOException {

		try {
			DeleteCollectionRequest requestDel = new DeleteCollectionRequest().withCollectionId(collectionName);
			DeleteCollectionResult response2 = client.deleteCollection(requestDel);

			TestUI.txtrThisIsA.setText("Items deleted from collection successfully.");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void CreateCollection() throws FileNotFoundException, IOException {

		try {
			CreateCollectionRequest requestColn = new CreateCollectionRequest().withCollectionId(collectionName);
			CreateCollectionResult responseColn = client.createCollection(requestColn);

			TestUI.txtrThisIsA.setText("Collection created successfully.");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void ListCollection() throws FileNotFoundException, IOException {
		try {
			
//			motionDetector.run();
			
			// LIST COLLECTION
			ListFacesRequest requestFaces = new ListFacesRequest().withCollectionId(collectionName).withMaxResults(20);
			ListFacesResult responseFaces = client.listFaces(requestFaces);
	
			ObjectMapper objectMapper = new ObjectMapper();
	
			List<Face> faceImageMatches = responseFaces.getFaces();
			StringBuilder resultString = new StringBuilder();
	
			for (Face face : faceImageMatches) {
	
				resultString.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(face));
	
			}
			TestUI.txtrThisIsA.setText(resultString.toString());
		} catch(Exception e) {

			e.printStackTrace();
		}

	}

	public static void InsertInDB(String name, String userid, String faceId) throws FileNotFoundException, IOException {

		String dbURL1 = "jdbc:postgresql://localhost:5432/Image?user=postgres&password=admin";

		try {
			conn1 = DriverManager.getConnection(dbURL1);
			if (conn1 != null) {
				System.out.println("Connected to database #1");
			}
			
			
			Statement stmt = conn1.createStatement();
			String sql;
			sql = "SELECT * FROM \"Users\"";
			ResultSet rs = stmt.executeQuery(sql);
			
			
			String query = "INSERT INTO \"Users\" (\"UserId\", \"Name\", \"FaceId\") VALUES (?, ?, ?)";
		      PreparedStatement preparedStmt = conn1.prepareStatement(query);
		      preparedStmt.setInt(1, Integer.parseInt(userid));  //User id
		      preparedStmt.setString(2, name);  //Name
		      preparedStmt.setString(3, faceId);  //FaceId
		      
//		      preparedStmt.setString(2, "\'shailesh\'");  //Name
//		      preparedStmt.setString(3, "\'123113213123\'");  //FaceId
		      preparedStmt.executeUpdate();
		      
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn1 != null && !conn1.isClosed()) {
					conn1.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static String SeachFaceIdinDB(String faceId) throws FileNotFoundException, IOException {

		String dbURL1 = "jdbc:postgresql://localhost:5432/Image?user=postgres&password=admin";
		String userFound = "";
		
		try {
			conn1 = DriverManager.getConnection(dbURL1);
			if (conn1 != null) {
				System.out.println("Connected to database #1");
			}
			
			
			Statement stmt = conn1.createStatement();
			String sql;
			sql = "SELECT * FROM \"Users\" where \"FaceId\"=" + "\'" + faceId + "\'";
			ResultSet rs = stmt.executeQuery(sql);
			

			 while (rs.next()) {
			        userFound = rs.getString("Name");
			        userFound += ";";  //seperator for username;userid
			        userFound += rs.getInt("UserId");
			    }
		      
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn1 != null && !conn1.isClosed()) {
					conn1.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return userFound;
	}
		
	public static void AddImageToCollectionandDB() throws FileNotFoundException, IOException {

//		motionDetector.stop();
		
		try {
			String photo = TestUI.textField4.getText();

			Random rand = new Random();
			int randomNum = rand.nextInt(100);

			String imageId = "TestId" + randomNum;

			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			Image imageObj = new Image().withBytes(imageBytes);

			// Store images in collection (START)
			IndexFacesRequest request = new IndexFacesRequest().withCollectionId(collectionName).withImage(imageObj)
					.withExternalImageId(imageId).withDetectionAttributes(new ArrayList());
			IndexFacesResult responseIndex = client.indexFaces(request);
			// Store images in collection (END)

			TestUI.txtrThisIsA.setText(photo + " added to collection.");
			
						
			//Add Face Id to DB
			InsertInDB(TestUI.textField_name.getText(), TestUI.textField_Userid.getText(), responseIndex.getFaceRecords().get(0).getFace().getFaceId());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static IndexFacesResult AddImageToCollection(String photo) throws FileNotFoundException, IOException {

		IndexFacesResult responseIndex = null;
		
		try {

			Random rand = new Random();
			int randomNum = rand.nextInt(100);

			String imageId = "TestId" + randomNum;

			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			Image imageObj = new Image().withBytes(imageBytes);
			
			// Call Indexfaces to add all faces from input image to collection
			IndexFacesRequest request = new IndexFacesRequest().withCollectionId(collectionName).withImage(imageObj)
					.withExternalImageId(imageId).withDetectionAttributes(new ArrayList());
			responseIndex = client.indexFaces(request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseIndex;
	}

	
	public static void ReadImagefromS3() throws FileNotFoundException, IOException {
		/*
		 * The ProfileCredentialsProvider will return your [default] credential profile
		 * by reading from the credentials file located at
		 * (C:\\Users\\sbarve\\.aws\\credentials).
		 */
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\sbarve\\.aws\\credentials), and is in valid format.", e);
		}

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_2).build();

		String bucketName = "my-first-s3-bucket-" + UUID.randomUUID();
		String key = "MyObjectKey";

		System.out.println("===========================================");
		System.out.println("Getting Started with Amazon S3");
		System.out.println("===========================================\n");

		try {
			/*
			 * Create a new S3 bucket - Amazon S3 bucket names are globally unique, so once
			 * a bucket name has been taken by any user, you can't create another bucket
			 * with that same name.
			 *
			 * You can optionally specify a location for your bucket if you want to keep
			 * your data closer to your applications or users.
			 */
			System.out.println("Creating bucket " + bucketName + "\n");
			s3.createBucket(bucketName);

			/*
			 * List the buckets in your account
			 */
			System.out.println("Listing buckets");
			for (Bucket bucket : s3.listBuckets()) {
				System.out.println(" - " + bucket.getName());
			}
			System.out.println();

			/*
			 * Upload an object to your bucket - You can easily upload a file to S3, or
			 * upload directly an InputStream if you know the length of the data in the
			 * stream. You can also specify your own metadata when uploading to S3, which
			 * allows you set a variety of options like content-type and content-encoding,
			 * plus additional metadata specific to your applications.
			 */
			System.out.println("Uploading a new object to S3 from a file\n");
			s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));

			/*
			 * Download an object - When you download an object, you get all of the object's
			 * metadata and a stream from which to read the contents. It's important to read
			 * the contents of the stream as quickly as possibly since the data is streamed
			 * directly from Amazon S3 and your network connection will remain open until
			 * you read all the data or close the input stream.
			 *
			 * GetObjectRequest also supports several other options, including conditional
			 * downloading of objects based on modification times, ETags, and selectively
			 * downloading a range of an object.
			 */
			System.out.println("Downloading an object");
			S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
			System.out.println("Content-Type: " + object.getObjectMetadata().getContentType());
			displayTextInputStream(object.getObjectContent());

			/*
			 * List objects in your bucket by prefix - There are many options for listing
			 * the objects in your bucket. Keep in mind that buckets with many objects might
			 * truncate their results when listing their objects, so be sure to check if the
			 * returned object listing is truncated, and use the
			 * AmazonS3.listNextBatchOfObjects(...) operation to retrieve additional
			 * results.
			 */
			System.out.println("Listing objects");
			ObjectListing objectListing = s3
					.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("My"));
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				System.out.println(" - " + objectSummary.getKey() + "  " + "(size = " + objectSummary.getSize() + ")");
			}
			System.out.println();

			/*
			 * Delete an object - Unless versioning has been turned on for your bucket,
			 * there is no way to undelete an object, so use caution when deleting objects.
			 */
			System.out.println("Deleting an object\n");
			s3.deleteObject(bucketName, key);

			/*
			 * Delete a bucket - A bucket must be completely empty before it can be deleted,
			 * so remember to delete any objects from your buckets before you try to delete
			 * them.
			 */
			System.out.println("Deleting bucket " + bucketName + "\n");
			s3.deleteBucket(bucketName);
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
					+ "a serious internal problem while trying to communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	
	public static void StartStream() throws FileNotFoundException, IOException
	{
		try {
		//Test Video APIs  - START

		//Create the stream process and start it. Data would be available in Kinesis Data Stream.
		//For quick testing - create a lambda function, hook it up to KDS and use Cloudwatch logs to
		//look at he results.

		stream.createStreamProcessor();
		
		stream.startStreamProcessor();
		stream.describeStreamProcessor();
		
		//make sure to delete stream processor after 
//		stream.stopStreamProcessorSample();
//		stream.deleteStreamProcessorSample();
		
		}catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void DeleteStreamProcessor() throws FileNotFoundException, IOException
	{
		try {
			String streamName = TestUI.textStream.getText();
			stream.deleteStreamProcessorSample(streamName);
			
			System.out.println("Stream processor deleted == " + streamName);
			
//			stream.stopStreamProcessorSample();
//			stream.deleteStreamProcessorSample();
			
//			stream.deleteStreamProcessorSample("streamProcessorWebcam13");
//			stream.deleteStreamProcessorSample("streamProcessorWebcam12");
//			stream.deleteStreamProcessorSample("streamProcessorWebcam11");
//			stream.deleteStreamProcessorSample("streamProcessorWebcam10");
//			stream.deleteStreamProcessorSample("streamProcessorWebcam9");   //Deleted
//			stream.deleteStreamProcessorSample("streamProcessorWebcam8");	//Deleted
//			stream.deleteStreamProcessorSample("streamProcessorWebcam7");	//Doesnt exist
//			stream.deleteStreamProcessorSample("streamProcessorWebcam6");	//Deleted
//			stream.deleteStreamProcessorSample("streamProcessorWebcam5");	//Doesnt exist
//			stream.deleteStreamProcessorSample("streamProcessorWebcam4");	//Deleted
//			stream.deleteStreamProcessorSample("streamProcessorWebcam3");	//Deleted
//			stream.deleteStreamProcessorSample("streamProcessorWebcam2");	//Deleted
			
		}catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Detects instances of real-world entities within an image (JPEG or PNG)
	 * provided as input. This includes objects like flower, tree, and table; events
	 * like wedding, graduation, and birthday party; and concepts like landscape,
	 * evening, and nature.
	 */
	public static void AnalyzeImagefromFile() throws FileNotFoundException, IOException {

		try {

			String photo = TestUI.textField.getText();

			// Convert photo to bytes
			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			DetectLabelsRequest request = new DetectLabelsRequest().withImage(new Image().withBytes(imageBytes))
					.withMaxLabels(10).withMinConfidence(77F);

			// Detect face(s) or object(s) from the photo
			DetectLabelsResult result = client.detectLabels(request);
			List<Label> labels = result.getLabels();

//			clientPreview.detectCustomLabels(arg0);
								
			StringBuilder resultString = new StringBuilder();
			System.out.println("Detected labels for " + photo);
			for (Label label : labels) {
				resultString.append(label.getName() + ": " + label.getConfidence().toString() + "\n");
				System.out.println(label.getName() + ": " + label.getConfidence().toString());
			}

			TestUI.txtrThisIsA.setText(resultString.toString());
			
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Detects text in the input image and converts it into machine-readable text.
	 */
	public static void ReadTextfromFile() throws FileNotFoundException, IOException {

		// String photo = "C://Infor//Kairos//Photos//text2.jpg";

		try {

			String photo = TestUI.textField3.getText();

			// Convert photo to bytes
			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			DetectTextRequest request = new DetectTextRequest().withImage(new Image().withBytes(imageBytes));

			DetectTextResult result = client.detectText(request);
			List<TextDetection> textDetections = result.getTextDetections();

			StringBuilder resultString = new StringBuilder();

			System.out.println("Detected lines and words for " + photo);
			for (TextDetection text : textDetections) {

				resultString.append("Detected: " + text.getDetectedText() + "\n");
				resultString.append("Confidence: " + text.getConfidence().toString() + "\n");
				resultString.append("Id : " + text.getId() + "\n");
				resultString.append("Parent Id: " + text.getParentId() + "\n");
				resultString.append("Type: " + text.getType() + "\n");

			}

			TestUI.txtrThisIsA.setText(resultString.toString());

		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Compares a face in the source input image with each of the 100 largest faces
	 * detected in the target input image.
	 */
	public static void CompareImagesfromFile() throws FileNotFoundException, IOException {

		// String photo = "C://Infor//Kairos//Photos//shail3.jpg";
		// String targetPhoto = "C://Infor//Kairos//Photos//shail2.jpg";

		try {

			String photo = TestUI.textField.getText();
			String targetPhoto = TestUI.textField2.getText();

			// Convert photo to bytes
			ByteBuffer imageBytes;
			try (InputStream inputStream = new FileInputStream(new File(photo))) {
				imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
			}

			ByteBuffer targetImagebytes;
			try (InputStream inputStream2 = new FileInputStream(new File(targetPhoto))) {
				targetImagebytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream2));
			}

			CompareFacesRequest request = new CompareFacesRequest().withSourceImage(new Image().withBytes(imageBytes))
					.withTargetImage(new Image().withBytes(targetImagebytes));

			// Compare face in source with target image
			CompareFacesResult response = client.compareFaces(request);
			List<CompareFacesMatch> labels = response.getFaceMatches();

			StringBuilder resultString = new StringBuilder();
			System.out.println("Compare matches for " + photo + " ; " + targetPhoto);

			for (CompareFacesMatch label : labels) {

				resultString.append("Similarity Score=" + label.getSimilarity() + "; \nFace metadata = "
						+ label.getFace().toString() + "\n\n");
			}

			TestUI.txtrThisIsA.setText(resultString.toString());

			if (labels.isEmpty()) {
				System.out.println("NO Match found.");
				TestUI.txtrThisIsA.setText("NO Match found.");
			}

		} catch (InvalidParameterException ex) {
			ex.printStackTrace();
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Creates a temporary file with text data to demonstrate uploading a file to
	 * Amazon S3
	 *
	 * @return A newly created temporary file with text data.
	 *
	 * @throws IOException
	 */
	private static File createSampleFile() throws IOException {
		File file = File.createTempFile("aws-java-sdk-", ".txt");
		file.deleteOnExit();

		Writer writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write("abcdefghijklmnopqrstuvwxyz\n");
		writer.write("01234567890112345678901234\n");
		writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
		writer.write("01234567890112345678901234\n");
		writer.write("abcdefghijklmnopqrstuvwxyz\n");
		writer.close();

		return file;
	}

	/**
	 * Displays the contents of the specified input stream as text.
	 *
	 * @param input
	 *            The input stream to display as text.
	 *
	 * @throws IOException
	 */
	private static void displayTextInputStream(InputStream input) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;

			System.out.println("    " + line);
		}
		System.out.println();
	}

}
