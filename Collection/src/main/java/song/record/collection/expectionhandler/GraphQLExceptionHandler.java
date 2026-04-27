package song.record.collection.expectionhandler;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;

/**
 * Catches ALL exceptions thrown during GraphQL data fetching
 * and converts them to proper GraphQL error responses.
 */
@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex,
                                                DataFetchingEnvironment env) {
        // ── Already a GraphQLException — return as-is ─────────
        if (ex instanceof GraphQLException graphQLEx) {
            return graphQLEx;
        }

        // ── ResourceNotFoundException → 404 ──────────────────
        if (ex instanceof ResourceNotFoundException) {
            return GraphQLError.newError()
                    .errorType(ErrorType.NOT_FOUND)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .extensions(java.util.Map.of(
                            "classification", "NOT_FOUND",
                            "status",         404
                    ))
                    .build();
        }

        // ── DuplicateResourceException → 409 ─────────────────
        if (ex instanceof DuplicateResourceException) {
            return GraphQLError.newError()
                    .errorType(ErrorType.FORBIDDEN)
                    .message(ex.getMessage())
                    .path(env.getExecutionStepInfo().getPath())
                    .location(env.getField().getSourceLocation())
                    .extensions(java.util.Map.of(
                            "classification", "CONFLICT",
                            "status",         409
                    ))
                    .build();
        }

        // ── Any other exception → 500 ─────────────────────────
        return GraphQLError.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("An unexpected error occurred: " + ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .extensions(java.util.Map.of(
                        "classification", "INTERNAL_ERROR",
                        "status",         500
                ))
                .build();
    }
}