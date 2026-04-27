package song.record.collection.expectionhandler;

import graphql.ErrorType;

import java.util.Map;

public class GraphQLBadRequestException extends GraphQLException {
    public GraphQLBadRequestException(String message) {
        super(message,
                ErrorType.ValidationError,
                Map.of(
                        "classification", "BAD_REQUEST",
                        "status",         400
                ));
    }
}
