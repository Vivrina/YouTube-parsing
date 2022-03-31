import java.net.URL;

public class Link {
    private URL link;
    private String title;
    private Integer count;

    public Link(URL link, Integer count) {
        this.link = link;
        this.title = "null";
        this.count = count;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(URL link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Ссылка: " + link + '\n'+
                "Заголовок сайта: " + title + '\n'+
                "Встречается: " + count +
                " раз" + '\n';
    }
}
