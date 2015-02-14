package mx.devf.requestexamples.model;


public class Event {

    String title;
    String date;
    String description;

    public Event(String title, String date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
