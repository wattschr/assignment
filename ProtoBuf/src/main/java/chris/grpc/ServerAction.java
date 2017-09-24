package chris.grpc;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Function;

class ServerAction<REQ, DOM, RES> implements BiConsumer<REQ, StreamObserver<RES>> {
    private static final Logger logger = LoggerFactory.getLogger(ServerAction.class);
    enum Action {
        UPDATE, DELETE, GET
    }
    private final Function<REQ, DOM> databaseAction;
    private final Function<DOM, RES> createResponse;
    private final Action action;

    ServerAction(Function<REQ, DOM> databaseAction,
                 Function<DOM, RES> createResponse,
                 Action action) {
        this.databaseAction = databaseAction;
        this.createResponse = createResponse;
        this.action = action;
    }

    @Override
    public void accept(REQ req, StreamObserver<RES> responseObserver) {
        logger.info("{} book {}", action, req);

        DOM domainResult = databaseAction.apply(req);

        logger.info("Result from DB {}", domainResult);

        constructReply(responseObserver, domainResult);
    }

    private void constructReply(final StreamObserver<RES> responseObserver, final DOM response) {
        RES apply = createResponse.apply(response);
        logger.info("Finishing {}  -  {}", action, response);
        if (apply != null) {
            responseObserver.onNext(apply);
        }
        responseObserver.onCompleted();
    }
}
