package VisionProject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.AsyncAnnotateFileRequest;
import com.google.cloud.vision.v1.AsyncAnnotateFileResponse;
import com.google.cloud.vision.v1.AsyncBatchAnnotateFilesResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.FaceAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.GcsDestination;
import com.google.cloud.vision.v1.GcsSource;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.InputConfig;
import com.google.cloud.vision.v1.OperationMetadata;
import com.google.cloud.vision.v1.OutputConfig;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.JsonFormat;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class VisionProjectMain {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try 
		{
			//VisionProjectMain.detectText("D:\\Work\\EclipseWorkspace\\VisionProject\\Data\\sea_shell.JPG");
			//VisionProjectMain.detectText("D:\\Work\\EclipseWorkspace\\VisionProject\\Data\\ThreeCans.JPG");
			int[][] i = VisionProjectMain.getPeopleCoordinates("D:\\Work\\EclipseWorkspace\\VisionProject\\Data\\WomanAndCat.JPG");
			System.out.println(i[0][0]);
			System.out.println(i[0][1]);
			System.out.println(i[0][2]);
			System.out.println(i[0][3]);
			VisionProjectMain.findPeopleInImage("D:\\Work\\EclipseWorkspace\\VisionProject\\Data\\WomanAndCat.JPG");
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void detectText(String filePath) throws Exception, IOException 
	{
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) 
        {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) 
            {
                if (res.hasError()) 
                {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) 
                {
                    System.out.print("Text: \n" + (String) annotation.getDescription());
                    System.out.print("Position : \n" + annotation.getBoundingPoly());
                }
            }
        }
    }
	
	public static void findPeopleInImage(String filePath) throws FileNotFoundException, IOException
	{
		List<AnnotateImageRequest> requests = new ArrayList<>();

		  ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		  Image img = Image.newBuilder().setContent(imgBytes).build();
		  Feature feat = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
		  AnnotateImageRequest request =
		      AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		  requests.add(request);

		  try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) 
		  {
		    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
		    List<AnnotateImageResponse> responses = response.getResponsesList();

		    for (AnnotateImageResponse res : responses) 
		    {
		      if (res.hasError()) 
		      {
		        System.out.printf("Error: %s\n", res.getError().getMessage());
		        return;
		      }
		      
		      // For full list of available annotations, see http://g.co/cloud/vision/docs
		      int numberOfFaces=0;
		      for (FaceAnnotation annotation : res.getFaceAnnotationsList()) 
		      {
		    	numberOfFaces++;
		        System.out.println(
		            "anger: "  + annotation.getAngerLikelihood() +
		            "\njoy: " + annotation.getJoyLikelihood() +
		            "\nsurprise:" + annotation.getSurpriseLikelihood()+
		            "\nposition: " + annotation.getBoundingPoly());
		      }
		      System.out.println("\nNumber of Faces: " + numberOfFaces);
		    }
		  }
	}
	
	public static int[][] getPeopleCoordinates(String filePath) throws FileNotFoundException, IOException
	{
		int[][] coordinates = new int[4][8];

		List<AnnotateImageRequest> requests = new ArrayList<>();

		  ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

		  Image img = Image.newBuilder().setContent(imgBytes).build();
		  Feature feat = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
		  AnnotateImageRequest request =
		      AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		  requests.add(request);

		  try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) 
		  {
		    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
		    List<AnnotateImageResponse> responses = response.getResponsesList();

		    for (AnnotateImageResponse res : responses) 
		    {
		      if (res.hasError()) 
		      {
		        System.out.printf("Error: %s\n", res.getError().getMessage());
		      }
		      
		      // For full list of available annotations, see http://g.co/cloud/vision/docs
		      int numberOfFaces=0;
		      for (FaceAnnotation annotation : res.getFaceAnnotationsList()) 
		      {
		    	if (numberOfFaces<8) 
		    	{
					numberOfFaces++;
					coordinates[numberOfFaces-1][0] = annotation.getBoundingPoly().getVertices(0).getX();
					coordinates[numberOfFaces-1][1] = annotation.getBoundingPoly().getVertices(0).getY();
					coordinates[numberOfFaces-1][2] = annotation.getBoundingPoly().getVertices(2).getX();
					coordinates[numberOfFaces-1][3] = annotation.getBoundingPoly().getVertices(2).getY();
				}
		      }
		    }
		  }
		
		return coordinates;
	}
}
