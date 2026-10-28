package delivery;
import java.util.*;
public class EcoFriendlyRouteStrategy implements RouteStrategy {
 public String getStrategyName() { return "EcoFriendly"; }
 public RouteDecision selectRoute(List<Route> routes, DeliveryRequest request) {
  List<Route> valid=RouteRules.eligible(routes,request);
  Route best=valid.stream().min(Comparator.comparingDouble((Route r)->r.carbonEmission()*0.8 + r.distanceKm()*0.2).thenComparing(Route::id)).orElseThrow();
  double score=best.carbonEmission()*0.8 + best.distanceKm()*0.2;
  return new RouteDecision(best,getStrategyName(),score,"Eco score = emissions * 0.8 + distance * 0.2; score="+String.format(java.util.Locale.US,"%.2f",score)+"; rejected="+(routes.size()-valid.size())+" routes by constraints");
 }
}
