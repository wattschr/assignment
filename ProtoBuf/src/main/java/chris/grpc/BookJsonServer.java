package chris.grpc;
/*
 * Copyright 2016, gRPC Authors All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import gappless.book.Book;
import gappless.book.BookServiceGrpc;
import gappless.book.Status;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

import static io.grpc.stub.ServerCalls.asyncUnaryCall;


@Component
class BookJsonServer {
    private static final Logger logger = Logger.getLogger(BookJsonServer.class.getName());

    private Server server;

    void start() throws IOException {
    /* The port on which the data should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new BookServerImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC data since JVM is shutting down");
            BookJsonServer.this.stop();
            System.err.println("*** data shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class BookServerImpl implements BindableService {

        private void save(Book req, StreamObserver<Status> responseObserver) {
            System.out.println("req = " + req);
            Status reply = Status.newBuilder().setSuccess(true).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        @Override
        public ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition
                    .builder(BookServiceGrpc.getServiceDescriptor().getName())
                    .addMethod(BookJsonClient.HelloJsonStub.METHOD_SAVE_BOOK,
                            asyncUnaryCall(this::save))
                    .build();
        }
    }

}
