import java.io.*;
import java.net.URL;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {

        YouTubeConnect youTubeConnect = new YouTubeConnect();
        ArrayList<URL> urls = new ArrayList<>();
        File file = new File("C://Users//Админ//Documents//url.txt");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        ArrayList<String> description = youTubeConnect.getDescription(youTubeConnect.getVideosIds(youTubeConnect.getPlayListId("UCMCgOm8GZkHp8zJ6l7_hIuA"), "null"));
        for (int i=0; i<description.size(); i++){
            urls.addAll(youTubeConnect.extractUrls(description.get(i)));
        }
        for (int i=0; i<urls.size(); i++){
            fileWriter.write(String.valueOf(urls.get(i)));
            fileWriter.write("\n");
            fileWriter.flush();
        }
        LinkProcessing linkProcessing = new LinkProcessing();
        try {
            ArrayList<Link> linkArrayList = linkProcessing.getHashMapIRL("C://Users//Админ//Documents//url.txt");
            linkProcessing.getTitle(linkArrayList);
            for (int i=0; i<linkArrayList.size(); i++) {
                System.out.println(linkArrayList.get(i).toString());
            }
            File file0 = new File("C://Users//Админ//Documents//coolLinks.txt");
            file0.createNewFile();
            FileWriter fileWriter0 = new FileWriter(file0);
            File file1 = new File("C://Users//Админ//Documents//badLinks.txt");
            file1.createNewFile();
            FileWriter fileWriter1 = new FileWriter(file1);
            for (int i=0; i<linkArrayList.size(); i++){
                if ((linkArrayList.get(i).getTitle()==null) || (linkArrayList.get(i).getTitle()=="") || (linkArrayList.get(i).getTitle()=="null") ){
                    fileWriter1.write(linkArrayList.get(i).toString());
                    fileWriter1.write("\n");
                    fileWriter1.flush();
                } else {
                    fileWriter0.write(linkArrayList.get(i).toString());
                    fileWriter0.write("\n");
                    fileWriter0.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        System.out.println(urls.size());
//        System.out.println(urls);

    }

}

