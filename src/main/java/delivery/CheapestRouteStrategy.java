package delivery;
import java.util.*;
public class CheapestRouteStrategy implements RouteStrategy {
 public String getStrategyName() { return "Cheapest"; }
 public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
  List<Route> valid=RouteRules.eligible(routes,request);
  Route best=valid.stream().min(Comparator.comparingDouble((Route r)->RouteRules.totalCost(r,request)).thenComparing(Route::id)).orElseThrow();
  double score=RouteRules.totalCost(best,request);
  return new RouteDecision(best,getStrategyName(),score,"Total cost = base + distance * 12 + extra fee + urgent surcharge; score="+String.format(java.util.Locale.US,"%.2f",score)+"; rejected="+(routes.size()-valid.size())+" routes by constraints");
 }
}
