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
    private static final Logger logger = LoggerFactory.getLogger(BookServerImpl.class.getName());

    @Autowired
    private BookRepository repository;

    private void save(Book req, StreamObserver<Status> responseObserver) {
        logger.info("Saving {}", req);

        final boolean status = exceptionSafeRun(() -> repository.save(Translator.createDomain(req)));

        logger.info("Finishing save  - successful {}", status);
        constructReply(responseObserver, status);
    }

    private void delete(BookId req, StreamObserver<Status> responseObserver) {
        logger.info("Deleting book {}", req);

        final boolean status = exceptionSafeRun(() -> repository.delete(req.getId()));

        logger.info("Finishing delete  - successful {}", status);
        constructReply(responseObserver, status);
    }

    private void constructReply(final StreamObserver<Status> responseObserver, final boolean status) {
        final Status reply = Status.newBuilder().setSuccess(status).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
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
                .build();
    }
}
