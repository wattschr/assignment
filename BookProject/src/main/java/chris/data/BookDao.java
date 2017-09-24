package chris.data;


import chris.domain.Book;

public interface BookDao {
    void create(Book b);
    void delete(Book b);
    void update(Book b);
}
