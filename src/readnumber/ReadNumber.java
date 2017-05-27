/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readnumber;

/**
 *
 * @author GuruCoder
 */
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.Videoio;
import org.opencv.videoio.VideoCapture;


public class ReadNumber {
    
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    private JFrame frame;
    private JLabel imageLabel;
    private JLabel message;
    private JLabel caunttext;
    private JLabel inform;
    private JLabel textVideo;
    private JLabel textImage;
    private JButton Buttonl;
    private JButton Button2;
    private JLabel imageView;
    Mat newImage;
    Mat imagefase;
    Mat webcamMatImage;
    ImageProcessor imageProcessor;
    CascadeClassifier faceDetector;
    String CascadeFile = "data/haarcascade_frontalface_alt.xml";
    String filePath = "images/camera.jpg";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ReadNumber app = new ReadNumber();
        app.initGUI();
        app.runMainLoop(args);
    }
    
    /**
     * initGUI
     */
     private void initGUI() {        
        frame = new JFrame("CauntPerson");  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                
        frame.setSize(666,400);
        Font f=new Font("Arial",Font.BOLD,14);  // Определение шрифта
        frame.setFont(f);
        newImage = Imgcodecs.imread(filePath);
        imageView = new JLabel();
        frame.add(imageView);
        
        Buttonl = new JButton("Faces analysis");
        Buttonl.setFont(f);
        frame.add(Buttonl);
        Button2 = new JButton("Read number");
        frame.add(Button2);        
        initListeners();
        
        message = new JLabel();
        message.setFont(f);
        frame.add(message);
        inform = new JLabel();
        inform.setFont(f);
        frame.add(inform);         
        caunttext = new JLabel();
        caunttext.setFont(f);
        frame.add(caunttext);        
        textVideo = new JLabel("Video");
        textVideo.setFont(f);
        frame.add(textVideo);
        textImage = new JLabel("Image");
        textImage.setFont(f);
        frame.add(textImage);        
        
        imageLabel = new JLabel();
        frame.add(imageLabel);
             
        frame.setVisible(true);         
     }
     
    private void initListeners() 
       {
         // saveframe 
         Buttonl.addActionListener((ActionEvent e) -> {
             detectFaces();
         });
       }

    /**
     * 
     * @param args runMainLoop - VideoCapture
     */    
     
      private void runMainLoop(String[] args) {
        imageProcessor = new ImageProcessor();
        webcamMatImage = new Mat();  
        Image tempImage;  
        VideoCapture capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH,320);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,240);
        
        // Create a face detector from the cascade file                
        faceDetector = new CascadeClassifier(CascadeFile);
        
        if( capture.isOpened()){  
        while (true){  
        capture.read(webcamMatImage);
        if( !webcamMatImage.empty() ){        
          // Output video to form (JLabel)
          //imageLabel.setBounds(0, 61, 320, 240);
          tempImage= imageProcessor.toBufferedImage(webcamMatImage);
          ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
          imageLabel.setIcon(imageIcon);
          Buttonl.setBounds(10, 0, 140, 30);
          //Button2.setBounds(140, 0, 120, 30);
          message.setBounds(10, 30, 200, 30);
          inform.setBounds(10, 315, 200, 30);
          caunttext.setBounds(100, 315, 200, 30);
          textVideo.setBounds(10, 295, 120, 30);

          //frame.pack();  //this will resize the window to fit the image
              }  
            else{
          System.out.println(" -- Frame not captured -- Break!");
          break;  
        }
      }
      }
      else{
        System.out.println("Couldn't open capture.");
      }
          
      }
      
    /**
     * detectFaces
     */
    private void detectFaces() 
       {
          // Save video to image           
          Imgcodecs.imwrite(filePath, webcamMatImage); 
          
          // Detect faces in the image
          imagefase = Imgcodecs.imread(filePath);
          MatOfRect faceDetections = new MatOfRect();          
          faceDetector.detectMultiScale(imagefase, faceDetections);
          
          inform.setText("Information:");
          String cauntfase = "total face: " 
                  + faceDetections.toArray().length;
          caunttext.setText(cauntfase);
          
          // Draw a bounding box around each face
          for (Rect rect : faceDetections.toArray()) {
           Imgproc.rectangle(imagefase, new Point(rect.x, rect.y), 
                   new Point(rect.x + rect.width, rect.y + rect.height), 
                   new Scalar(0, 255, 0));    
          }

          // Save video to image
          Imgcodecs.imwrite(filePath, imagefase);
          message.setText("Detect faces ...");  

          // Output image to form (JLabel)
          imageView.setBounds(330, 61, 320, 240);            
          newImage = Imgcodecs.imread(filePath);                        
          Image loadedImage =  imageProcessor.toBufferedImage(newImage);
          ImageIcon imgIcon = new ImageIcon(loadedImage, "img");
          imageView.setIcon(imgIcon);
          textImage.setBounds(340, 295, 120, 30);        
       }
    
    private Image toBufferedImage(Mat newImage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
