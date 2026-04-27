package song.record.collection.expectionhandler;

import graphql.ErrorType;

import java.util.Map;

public class GraphQLConflictException extends GraphQLException {
    public GraphQLConflictException(String message) {
        super(message,
                ErrorType.DataFetchingException,
                Map.of(
                        "classification", "CONFLICT",
                        "status",         409
                ));
    }
}