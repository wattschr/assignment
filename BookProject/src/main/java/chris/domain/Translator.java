package chris.domain;

import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Translator {
    private Translator() {
        //Util
    }

    @Nonnull
    public static Book createDomain (chris.proto.Book proto) {
        return new Book(proto.getId(), proto.getTitle(), proto.getNrOfPages());
    }

    @Contract("null -> null")
    @Nullable
    public static chris.proto.Book createProto(@Nullable Book b) {
        if (b == null) {
            return null;
        }
        return chris.proto.Book.newBuilder()
                .setId(b.getId())
                .setTitle(b.getTitle())
                .setNrOfPages(b.getNumberOfPages())
                .build();
    }
}
