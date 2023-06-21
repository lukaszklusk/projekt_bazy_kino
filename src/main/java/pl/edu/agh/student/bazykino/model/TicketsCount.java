package pl.edu.agh.student.bazykino.model;

import java.util.Set;

public class TicketsCount {
    private String title;
    private long count;

    public TicketsCount(String title, long count) {
        this.title = title;
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
