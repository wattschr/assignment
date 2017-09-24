package chris.domain;

import javax.annotation.Nonnull;

public class Translator {
    private Translator() {
        //Util
    }

    @Nonnull
    public static Book createDomain (chris.proto.Book proto) {
        return new Book(proto.getId(), proto.getTitle(), proto.getNrOfPages());
    }

    @Nonnull
    public static chris.proto.Book createProto(Book b) {
        return chris.proto.Book.newBuilder()
                .setId(b.getId())
                .setTitle(b.getTitle())
                .setNrOfPages(b.getNumberOfPages())
                .build();
    }
}
