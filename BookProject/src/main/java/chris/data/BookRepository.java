package chris.data;

import chris.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
public interface BookRepository extends CrudRepository<Book, Long> {

}
