package audioLibrary.music;

import java.util.Objects;

public class Song {
    private static int idCounter = 1;
    private final Integer id;
    private final String name;
    private final String author;
    private final Integer year;

    public Song(String name, String author, Integer year) {
        this.id = idCounter++;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Integer getId(){
        return this.id;
    }
    public String getName(){
        return name;
    }
    public String getAuthor(){
        return author;
    }
    public Integer getYear(){
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return name.equals(song.name) && author.equals(song.author);
    }

    public int hashCode() {
        return Objects.hash(name, author);
    }

    public String toString() {
        return this.author + " - " + this.name + " (" + this.year + ") [ID: " + this.id + "]";
    }
}
