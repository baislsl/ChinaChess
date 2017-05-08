package record;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-23.
 */
public class RecordReader {
    private Document doc = null;
    private ArrayList<Record> list = new ArrayList<>();
    private int listIndex = 0;


    public RecordReader(File file){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputStream in;
        try{
            in  = new FileInputStream(file);
            builder = factory.newDocumentBuilder();
            doc = builder.parse(in);
        }catch (Exception e){
            System.out.println("Build parse builder failed.\n" + e.getMessage());
        }
        if(doc != null){
            Element root = doc.getDocumentElement();
            NodeList xList = root.getElementsByTagName("x");
            NodeList yList = root.getElementsByTagName("y");

            for(int i=0;i<xList.getLength();i+=2){
                int fromX = Integer.parseInt(xList.item(i).getTextContent());
                int fromY = Integer.parseInt(yList.item(i).getTextContent());
                int toX = Integer.parseInt(xList.item(i + 1).getTextContent());
                int toY = Integer.parseInt(yList.item(i + 1).getTextContent());
                list.add(new Record(fromX, fromY,toX, toY));
            }

        }

    }

    public Record getNextStep(){
        if(doc == null) System.out.println("No load the file successfully.");
        if(listIndex == list.size()){
            System.out.println("Finished");
            return null;
        }else{
            return list.get(listIndex++);
        }
    }

    public void reset(){
        listIndex = 0;
    }

}
