import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageEntry implements Comparable<PageEntry> {

    private final String pdfName;
    private final int page;
    private final int count;

    @Override
    public int compareTo(PageEntry o) {
        return o.count - this.count;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfFileName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                "}";
    }

}
