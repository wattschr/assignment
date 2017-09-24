package chris.grpc;

import chris.proto.Book;
import chris.proto.Status;
import io.grpc.*;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.grpc.stub.ClientCalls.blockingUnaryCall;

public class BookJsonClient {

    private static final Logger logger = Logger.getLogger(BookJsonClient.class.getName());

    private final ManagedChannel channel;
    private final BookJsonStub blockingStub;

    /** Construct client connecting to HelloWorld data at {@code host:port}. */
    private BookJsonClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
        blockingStub = new BookJsonStub(channel);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to data. */
    private void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        Book request = Book.newBuilder().setId(1L).setTitle(name).setNrOfPages(100).build();
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


}
