package delivery;
import java.util.*;
public class FastestRouteStrategy implements RouteStrategy {
 public String getStrategyName() { return "Fastest"; }
 public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
  List<Route> valid=RouteRules.eligible(routes,request);
  Route best=valid.stream().min(Comparator.comparingDouble((Route r)->RouteRules.totalMinutes(r)).thenComparing(Route::id)).orElseThrow();
  double score=RouteRules.totalMinutes(best);
  return new RouteDecision(best,getStrategyName(),score,"Adjusted time = base time + traffic penalty; score="+String.format(java.util.Locale.US,"%.2f",score)+"; rejected="+(routes.size()-valid.size())+" routes by constraints");
 }
}
