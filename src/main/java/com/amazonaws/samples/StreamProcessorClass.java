package com.amazonaws.samples;

import java.io.IOException;

import com.amazonaws.services.applicationdiscovery.model.InvalidParameterException;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.CreateStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.CreateStreamProcessorResult;
import com.amazonaws.services.rekognition.model.DeleteStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.DeleteStreamProcessorResult;
import com.amazonaws.services.rekognition.model.DescribeStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.DescribeStreamProcessorResult;
import com.amazonaws.services.rekognition.model.FaceSearchSettings;
import com.amazonaws.services.rekognition.model.KinesisDataStream;
import com.amazonaws.services.rekognition.model.KinesisVideoStream;
import com.amazonaws.services.rekognition.model.ListStreamProcessorsRequest;
import com.amazonaws.services.rekognition.model.ListStreamProcessorsResult;
import com.amazonaws.services.rekognition.model.StartStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.StartStreamProcessorResult;
import com.amazonaws.services.rekognition.model.StopStreamProcessorRequest;
import com.amazonaws.services.rekognition.model.StopStreamProcessorResult;
import com.amazonaws.services.rekognition.model.StreamProcessor;
import com.amazonaws.services.rekognition.model.StreamProcessorInput;
import com.amazonaws.services.rekognition.model.StreamProcessorOutput;
import com.amazonaws.services.rekognition.model.StreamProcessorSettings;

public class StreamProcessorClass {

//    private String streamProcessorName = "streamProcessorWebcam14";
    private String streamProcessorName = "streamProcessor6";
    private String kinesisVideoStreamArn = "arn:aws:kinesisvideo:us-east-1:957852833411:stream/VideoStream6/1522443218702";
   
    private String kinesisDataStreamArn = "arn:aws:kinesis:us-east-1:957852833411:stream/AmazonRekognition6";
//    private String kinesisDataStreamArn = "arn:aws:kinesis:us-east-1:957852833411:stream/AmazonRekognitionData4";
    private String roleArn = "arn:aws:iam::957852833411:role/roleWithKinesisPermission";

    private String collectionId = "myphotos";
    private float matchThreshold = 0;

//    private AmazonRekognitionClient rekognitionClient;

    
	public static void main(String[] args) throws IOException {

		//ini variables here
		
	}

    public void createStreamProcessor() {
    	try
    	{
    		//input - kinesis video stream
            KinesisVideoStream kinesisVideoStream = new KinesisVideoStream().withArn(kinesisVideoStreamArn);
            StreamProcessorInput streamProcessorInput =
                    new StreamProcessorInput().withKinesisVideoStream(kinesisVideoStream);
            
            //output - kinesis data stream
            KinesisDataStream kinesisDataStream = new KinesisDataStream().withArn(kinesisDataStreamArn);
            StreamProcessorOutput streamProcessorOutput =
                    new StreamProcessorOutput().withKinesisDataStream(kinesisDataStream);
            
            //face collection for comparision
            FaceSearchSettings faceSearchSettings =
                    new FaceSearchSettings().withCollectionId(collectionId).withFaceMatchThreshold(matchThreshold);
            StreamProcessorSettings streamProcessorSettings =
                    new StreamProcessorSettings().withFaceSearch(faceSearchSettings);


            //Create stream processor
			CreateStreamProcessorResult createStreamProcessorResult = S3Sample.client.createStreamProcessor(
                    new CreateStreamProcessorRequest()
                    	.withInput(streamProcessorInput)
                    	.withOutput(streamProcessorOutput)
                        .withSettings(streamProcessorSettings)
                        .withRoleArn(roleArn)
                        .withName(streamProcessorName));
            
            System.out.println("StreamProcessorArn - " + createStreamProcessorResult.getStreamProcessorArn());    	
            
    	} catch (InvalidParameterException ex) {
			ex.printStackTrace();
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
		}

    }

    public void startStreamProcessor() {
        StartStreamProcessorResult startStreamProcessorResult =
                S3Sample.client.startStreamProcessor(new StartStreamProcessorRequest().withName(streamProcessorName));
    }

    public void stopStreamProcessor() {
        StopStreamProcessorResult stopStreamProcessorResult =
                S3Sample.client.stopStreamProcessor(new StopStreamProcessorRequest().withName(streamProcessorName));
    }

    public void deleteStreamProcessor() {
        DeleteStreamProcessorResult deleteStreamProcessorResult = S3Sample.client
                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamProcessorName));
    }
    
    public void deleteStreamProcessorSample(String streamName) {
        DeleteStreamProcessorResult deleteStreamProcessorResult = S3Sample.client
                .deleteStreamProcessor(new DeleteStreamProcessorRequest().withName(streamName));
    }

    public void describeStreamProcessor() {
        DescribeStreamProcessorResult describeStreamProcessorResult = S3Sample.client
                .describeStreamProcessor(new DescribeStreamProcessorRequest().withName(streamProcessorName));
        System.out.println("Arn - " + describeStreamProcessorResult.getStreamProcessorArn());
        System.out.println("Input kinesisVideo stream - "
                + describeStreamProcessorResult.getInput().getKinesisVideoStream().getArn());
        System.out.println("Output kinesisData stream - "
                + describeStreamProcessorResult.getOutput().getKinesisDataStream().getArn());
        
        System.out.println("getOutput() - "
                + describeStreamProcessorResult.getOutput());
        System.out.println("getOutput().getKinesisDataStream() - "
                + describeStreamProcessorResult.getOutput().getKinesisDataStream());
        
       
        System.out.println("RoleArn - " + describeStreamProcessorResult.getRoleArn());
        System.out.println(
                "CollectionId - " + describeStreamProcessorResult.getSettings().getFaceSearch().getCollectionId());
        System.out.println("Status - " + describeStreamProcessorResult.getStatus());
        System.out.println("Status message - " + describeStreamProcessorResult.getStatusMessage());
        System.out.println("Creation timestamp - " + describeStreamProcessorResult.getCreationTimestamp());
        System.out.println("Last update timestamp - " + describeStreamProcessorResult.getLastUpdateTimestamp());
    }

    public void listStreamProcessorSample() {
        ListStreamProcessorsResult listStreamProcessorsResult =
                S3Sample.client.listStreamProcessors(new ListStreamProcessorsRequest().withMaxResults(100));
        for (StreamProcessor streamProcessor : listStreamProcessorsResult.getStreamProcessors()) {
            System.out.println("StreamProcessor name - " + streamProcessor.getName());
            System.out.println("Status - " + streamProcessor.getStatus());
        }
    }
}