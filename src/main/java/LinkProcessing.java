import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkProcessing {



    public static ArrayList<Link> getHashMapIRL (String way) throws IOException {
        FileReader fileReader = new FileReader(way);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str = bufferedReader.readLine();
        HashMap<URL, Integer> hashMap = new HashMap<>();
        while (str != null) {
            URL url = new URL(str);
            if (hashMap.containsKey(url)){
                hashMap.put(url, hashMap.get(url)+1);
            } else {
                hashMap.put(url, 1);
            }

            //System.out.println(hashMap.get(url));
            str = bufferedReader.readLine();
        }
        HashMap<URL, Integer> insta = new HashMap<>();
        ArrayList<Link> arrayList = new ArrayList<>();
        hashMap.forEach((key, value) -> {
            if (String.valueOf(key).contains("instagram")) {
                insta.put(key, value);
            } else {
                arrayList.add(new Link(key, value));
            }
        });
//        hashMap.forEach((key,value) -> {
//            System.out.println(key + " - " + value);
//            arrayList.add(new Link(key, value));
//        });
        System.out.println(hashMap.size());
        System.out.println(arrayList.size());
        return arrayList;
    }
    public static void getTitle (ArrayList<Link> linkArrayList)  {
        BigThread bigThread = new BigThread(linkArrayList);
        Thread thread = new Thread(bigThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class BigThread implements Runnable {
        ArrayList<Link> linkArrayList;

        public BigThread(ArrayList<Link> linkArrayList) {
            this.linkArrayList = linkArrayList;
        }

        @Override
        public void run() {
            ArrayList<Thread> threads = new ArrayList<>();
            for (int i=0; i<linkArrayList.size(); i++){
                LittleThread littleThread = new LittleThread(linkArrayList.get(i));
                Thread thread = new Thread(littleThread);
                thread.start();
                threads.add(thread);
            }
            for(Thread myThread : threads){
                try {
                    myThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class LittleThread implements Runnable {
        private Link link;

        public LittleThread(Link link) {
            this.link = link;
        }

        @Override
        public void run() {
            String str = String.valueOf(link.getLink());
            Document doc = null;
            try {
                doc = Jsoup.connect(str)
                        .userAgent("Chrome/4.0.249.0 Safari/532.5")
                        .referrer("http://www.google.com")
                        .timeout(60000)
                        .followRedirects(true)
                        .get();
                link.setTitle(doc.title());
            } catch (IOException e) {

            }
        }
    }

}
