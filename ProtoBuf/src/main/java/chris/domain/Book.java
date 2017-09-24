package chris.domain;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {
    //Assume this is an id specific to the book, e.g. ISBN
    //Rather than our internal generated id.
    @Id
    private long id;
    @Nonnull
    @Column(nullable = false)
    private String title;
    private int numberOfPages;

    public Book(long id, @Nonnull String title, int numberOfPages) {
        this.id = id;
        this.title = title;
        this.numberOfPages = numberOfPages;
    }

    @SuppressWarnings("unused")
    private Book() {
        title = "";
        //For JPA
    }

    long getId() {
        return id;
    }

    @Nonnull
    String getTitle() {
        return title;
    }

    int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id == book.id;
    }


    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}
