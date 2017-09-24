package chris.grpc;

import chris.data.BookRepository;
import chris.domain.Translator;
import chris.proto.Book;
import chris.proto.BookServiceGrpc;
import chris.proto.BookId;
import chris.proto.Status;
import io.grpc.BindableService;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import static io.grpc.stub.ServerCalls.asyncUnaryCall;

@Component
class BookServerImpl implements BindableService {
    private static final Logger logger = LoggerFactory.getLogger(BookServerImpl.class);
    private final ServerAction<Book, Boolean, Status> save;
    private final ServerAction<BookId, Boolean, Status> delete;
    private final ServerAction<BookId, chris.domain.Book, Book> get;
    @Autowired
    private BookRepository repository;

    public BookServerImpl() {
        save = new ServerAction<>(
                req -> exceptionSafeRun(() -> repository.save(Translator.createDomain(req))),
                this::statusResponse,
                ServerAction.Action.UPDATE);

        delete = new ServerAction<>(
                req -> exceptionSafeRun(() -> repository.delete(req.getId())),
                this::statusResponse,
                ServerAction.Action.DELETE);

        get = new ServerAction<>(
                req -> repository.findOne(req.getId()),
                Translator::createProto,
                ServerAction.Action.GET);

    }

    private void save(Book req, StreamObserver<Status> responseObserver) {
        save.accept(req, responseObserver);
    }

    private void delete(BookId req, StreamObserver<Status> responseObserver) {
        delete.accept(req, responseObserver);
    }

    private void get(BookId req, StreamObserver<Book> responseObserver) {
        get.accept(req, responseObserver);
    }

    private Status statusResponse(Boolean dom) {
        return Status.newBuilder().setSuccess(dom).build();
    }


    private boolean exceptionSafeRun(final Runnable r) {
        try {
            r.run();
            return true;
        } catch (Exception e) {
            logger.error("Error",e);
            return false;
        }
    }

    @Override
    public ServerServiceDefinition bindService() {
        return ServerServiceDefinition
                .builder(BookServiceGrpc.getServiceDescriptor().getName())
                .addMethod(BookJsonStub.METHOD_UPDATE_BOOK, asyncUnaryCall(this::save))
                .addMethod(BookJsonStub.METHOD_DELETE_BOOK, asyncUnaryCall(this::delete))
                .addMethod(BookJsonStub.METHOD_GET_BOOK, asyncUnaryCall(this::get))
                .build();
    }
}
