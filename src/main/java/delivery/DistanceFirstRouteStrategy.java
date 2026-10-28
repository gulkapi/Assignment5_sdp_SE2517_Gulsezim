package delivery;
import java.util.*;
public class DistanceFirstRouteStrategy implements RouteStrategy {
 public String getStrategyName() { return "DistanceFirst"; }
 public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
  List<Route> valid=RouteRules.eligible(routes,request);
  Route best=valid.stream().min(Comparator.comparingDouble((Route r)->r.distanceKm()).thenComparing(Route::id)).orElseThrow();
  double score=best.distanceKm();
  return new RouteDecision(best,getStrategyName(),score,"Shortest distance in kilometers; score="+String.format(java.util.Locale.US,"%.2f",score)+"; rejected="+(routes.size()-valid.size())+" routes by constraints");
 }
}
