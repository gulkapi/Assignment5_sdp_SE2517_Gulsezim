package delivery;
public record RouteDecision(Route route, String strategy, double score, String explanation) {}
