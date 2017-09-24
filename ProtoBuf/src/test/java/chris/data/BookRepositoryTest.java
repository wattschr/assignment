package chris.data;

import chris.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest(classes = {JpaConfig.class})
@EntityScan(basePackages = "chris.domain")
public class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    public void saveUpdateAndDeleteBook() throws Exception {
        Book book = new Book(1L, "title", 10);
        repository.save(book);

        Book savedBook = repository.findOne(1L);
        assertEquals(book.toString(), savedBook.toString());

        Book bookUpdate = new Book(1L, "title2", 10);
        repository.save(bookUpdate);

        Book savedUpdatedBook = repository.findOne(1L);
        assertEquals(bookUpdate.toString(), savedUpdatedBook.toString());

        repository.delete(1L);

        assertEquals(0, repository.count());
    }
}