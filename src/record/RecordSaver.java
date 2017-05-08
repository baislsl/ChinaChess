package record;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by baislsl on 17-2-23.
 */
public class RecordSaver {
    private Path path;
    private ArrayList<Record> list;

    public RecordSaver(String path){
        new File(path);
        this.path = Paths.get(path);
        this.list = new ArrayList<>();
    }

    public void addRecord(Record record){
        list.add(record);
    }

    public Record popLastRecord(){
        int lastIndex= list.size() - 1;
        Record result = list.get(lastIndex);
        list.remove(lastIndex);
        return result;
    }

    public void deleteLastRecord(){
        int lastIndex= list.size() - 1;
        list.remove(lastIndex);
    }

    public boolean isEmpty(){
        return list.isEmpty();
    }

    public void save(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try{
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        }catch (Exception e){
            System.out.println("Build builder failed.\n" + e.getMessage());
        }

        if(doc != null){
            Element content = doc.createElement("content");
            for(Record record : list){
                Element child = doc.createElement("step");
                child.setAttribute("chess", record.moveChess.getName());
                child.setAttribute("class", record.moveChess.id.toString());

                Element from = doc.createElement("from");
                Element to = doc.createElement("to");

                Element fromX = doc.createElement("x");
                fromX.setTextContent(Integer.toString(record.from.x));
                Element fromY = doc.createElement("y");
                fromY.setTextContent(Integer.toString(record.from.y));
                from.appendChild(fromX);
                from.appendChild(fromY);

                Element toX = doc.createElement("x");
                toX.setTextContent(Integer.toString(record.to.x));
                Element toY = doc.createElement("y");
                toY.setTextContent(Integer.toString(record.to.y));
                to.appendChild(toX);
                to.appendChild(toY);

                child.appendChild(from);
                child.appendChild(to);

                content.appendChild(child);
            }
            doc.appendChild(content);

            // save the file
            DOMImplementation impl = doc.getImplementation();
            DOMImplementationLS implLS = (DOMImplementationLS)impl.getFeature("LS", "3.0");
            LSSerializer ser = implLS.createLSSerializer();
            ser.getDomConfig().setParameter("format-pretty-print", true);
            LSOutput out = implLS.createLSOutput();
            out.setEncoding("UTF-8");
            try{
                out.setByteStream(Files.newOutputStream(path));
                ser.write(doc, out);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }


        }

    }

    public void save(String path){
        this.path = Paths.get(path);
        new File(path);
        this.save();
    }


}
