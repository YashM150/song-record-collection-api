package song.record.collection.expectionhandler.exceptions;

import graphql.ErrorType;
import song.record.collection.expectionhandler.GraphQLException;

import java.util.Map;

// ── 404 Not Found ─────────────────────────────────────────
public class GraphQLNotFoundException extends GraphQLException {
    public GraphQLNotFoundException(String message) {
        super(message,
                ErrorType.DataFetchingException,
                Map.of(
                        "classification", "NOT_FOUND",
                        "status",         404
                ));
    }
}