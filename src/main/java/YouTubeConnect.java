import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeConnect {
    private static final Pattern urlPattern = Pattern.compile(
            "(http|ftp|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    private YouTube youtube;

    public YouTubeConnect() {
        this.youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("APP_ID").build();
    }
    public String getPlayListId(String channelId) throws IOException {
        String apiKey = "";
        YouTube.Channels.List request = youtube.channels().list("contentDetails").setId(channelId).setKey(apiKey);
        ChannelListResponse response = request.execute();
        //response.getItems().indexOf("uploads");
        Channel u = response.getItems().get(0);
        System.out.println(u.getContentDetails().getRelatedPlaylists().getUploads());
        return u.getContentDetails().getRelatedPlaylists().getUploads();
    }
    public ArrayList<String> getDescription(ArrayList<String> videoIds) throws IOException {
        ArrayList<String> descriptions = new ArrayList<>();
        String apiKey = "";
        YouTube.Videos.List request = youtube.videos().list("snippet").setId(videoIds.get(0)).setKey(apiKey);
        VideoListResponse response = request.execute();
        descriptions.add(response.getItems().get(0).getSnippet().getDescription());
        extractUrls(descriptions.get(0));
        for (int i=1; i<videoIds.size(); i++) {
            request = youtube.videos().list("snippet").setId(videoIds.get(i)).setKey(apiKey);
            response = request.execute();
            descriptions.add(response.getItems().get(0).getSnippet().getDescription());
            extractUrls(descriptions.get(i));
        }

        //System.out.println(descriptions);
        return descriptions;
    }
    ArrayList<URL> extractUrls(String description){
        //String urlPattern = "/http\\:\\/\\/[\\w\\-\\.\\/]+/";
        ArrayList<URL> urls = new ArrayList<>();
        Matcher urlMatcher = urlPattern.matcher(description);
        while (urlMatcher.find()){
            try {
                String link = description.substring(urlMatcher.start(0), urlMatcher.end(0));
                URL url = new URL(link);
                urls.add(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public ArrayList<String> getVideosIds(String playlistId, String pageToken) throws IOException {
        String apiKey = ""; // you can get it from https://console.cloud.google.com/apis/credentials
        ArrayList<String> videoIds = new ArrayList<>();
        YouTube.PlaylistItems.List request = youtube.playlistItems()
                .list("contentDetails")
                .setKey(apiKey)
                .setPlaylistId(playlistId)
                .setMaxResults(50L);
        if(pageToken != null && !pageToken.equals("null")){
            request.setPageToken(pageToken);
        }
        PlaylistItemListResponse response = request.execute();
        response.getItems().stream().forEach(item -> videoIds.add(item.getContentDetails().getVideoId()));
        String nextPageToken = response.getNextPageToken();
        if(nextPageToken != null){
            videoIds.addAll(getVideosIds(playlistId, nextPageToken));
        }
        return videoIds;
    }
}
