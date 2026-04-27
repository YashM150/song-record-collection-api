package song.record.collection.expectionhandler;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

/**
 * Base class for all GraphQL exceptions.
 * Implements GraphQLError so Spring GraphQL picks it up automatically.
 */
public class GraphQLException extends RuntimeException implements GraphQLError {

    private final ErrorClassification errorType;
    private final Map<String, Object> extensions;

    public GraphQLException(String message,
                            ErrorClassification errorType,
                            Map<String, Object> extensions) {
        super(message);
        this.errorType  = errorType;
        this.extensions = extensions;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return errorType;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }
}