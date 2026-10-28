package delivery;
import java.util.*;
public final class RouteRules {
 private RouteRules() {}
 public static double totalCost(Route r, DeliveryRequest q) { return r.cost() + r.distanceKm()*12 + r.extraFee() + (q.priority()==DeliveryPriority.URGENT ? 250 : 0); }
 public static int totalMinutes(Route r) { return r.estimatedMinutes()+r.trafficMinutes(); }
 public static List<Route> eligible(List<Route> routes, DeliveryRequest request) {
  Objects.requireNonNull(routes,"Routes required"); Objects.requireNonNull(request,"Request required");
  List<Route> valid=routes.stream().map(r->Objects.requireNonNull(r,"Null route")).filter(Route::available).filter(r->totalCost(r,request)<=request.maxBudget()).filter(r->totalMinutes(r)<=request.maxDeliveryMinutes()).toList();
  if(valid.isEmpty()) throw new NoSuitableRouteException("No route satisfies availability, budget and deadline constraints");
  return valid;
 }
 public static double normalized(double v,double min,double max) { return max==min?0:(v-min)/(max-min); }
}
