package chris.grpc;

import chris.proto.Book;
import chris.proto.BookId;
import chris.proto.BookServiceGrpc;
import chris.proto.Status;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.MethodDescriptor;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.AbstractStub;

import javax.annotation.Nonnull;

import static io.grpc.stub.ClientCalls.blockingUnaryCall;

final class BookJsonStub extends AbstractStub<BookJsonStub> {

    static final MethodDescriptor<Book, Status> METHOD_UPDATE_BOOK =
            BookServiceGrpc.METHOD_UPDATE
                    .toBuilder(
                            ProtoUtils.jsonMarshaller(Book.getDefaultInstance()),
                            ProtoUtils.jsonMarshaller(Status.getDefaultInstance()))
                    .build();

    static final MethodDescriptor<BookId, Status> METHOD_DELETE_BOOK =
            BookServiceGrpc.METHOD_DELETE
                    .toBuilder(
                            ProtoUtils.jsonMarshaller(BookId.getDefaultInstance()),
                            ProtoUtils.jsonMarshaller(Status.getDefaultInstance()))
                    .build();

    BookJsonStub(Channel channel) {
        super(channel);
    }

    private BookJsonStub(Channel channel, CallOptions callOptions) {
        super(channel, callOptions);
    }

    @Nonnull
    @Override
    protected BookJsonStub build(Channel channel, CallOptions callOptions) {
        return new BookJsonStub(channel, callOptions);
    }

    Status save(Book request) {
        return blockingUnaryCall(
                getChannel(), METHOD_UPDATE_BOOK, getCallOptions(), request);
    }

    @SuppressWarnings("unused")
    Status delete(BookId request) {
        return blockingUnaryCall(
                getChannel(), METHOD_DELETE_BOOK, getCallOptions(), request);
    }
}
