package chris.grpc;

import gappless.book.Book;
import gappless.book.BookServiceGrpc;
import gappless.book.Status;
import io.grpc.*;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.AbstractStub;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.grpc.stub.ClientCalls.blockingUnaryCall;

public class BookJsonClient {

    private static final Logger logger = Logger.getLogger(BookJsonClient.class.getName());

    private final ManagedChannel channel;
    private final HelloJsonStub blockingStub;

    /** Construct client connecting to HelloWorld data at {@code host:port}. */
    private BookJsonClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        blockingStub = new HelloJsonStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to data. */
    private void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        Book request = Book.newBuilder().setTitle(name).build();
        Status response;
        try {
            response = blockingStub.save(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getSuccess());
    }

    /**
     * Greet data. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception {
        BookJsonClient client = new BookJsonClient("localhost", 50051);
        try {
      /* Access a service running on the local machine on port 50051 */
            String user = "world";
            if (args.length > 0) {
                user = args[0]; /* Use the arg as the name to greet if provided */
            }
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }


    static final class HelloJsonStub extends AbstractStub<HelloJsonStub> {

        static final MethodDescriptor<Book, Status> METHOD_SAVE_BOOK =
                BookServiceGrpc.METHOD_SAVE_BOOK
                        .toBuilder(
                                ProtoUtils.jsonMarshaller(Book.getDefaultInstance()),
                                ProtoUtils.jsonMarshaller(Status.getDefaultInstance()))
                        .build();

        HelloJsonStub(Channel channel) {
            super(channel);
        }

        HelloJsonStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected HelloJsonStub build(Channel channel, CallOptions callOptions) {
            return new HelloJsonStub(channel, callOptions);
        }

        Status save(Book request) {
            return blockingUnaryCall(
                    getChannel(), METHOD_SAVE_BOOK, getCallOptions(), request);
        }
    }
}
