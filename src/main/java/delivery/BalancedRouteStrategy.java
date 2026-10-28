package delivery;
import java.util.*;
import java.util.function.ToDoubleFunction;
public class BalancedRouteStrategy implements RouteStrategy {
 private final double tw,cw,dw,ew;
 public BalancedRouteStrategy(double tw,double cw,double dw,double ew) {
  if(!Double.isFinite(tw)||!Double.isFinite(cw)||!Double.isFinite(dw)||!Double.isFinite(ew)||tw<0||cw<0||dw<0||ew<0||Math.abs(tw+cw+dw+ew-1)>1e-9) throw new IllegalArgumentException("Weights must be nonnegative and sum to 1");
  this.tw=tw;this.cw=cw;this.dw=dw;this.ew=ew;
 }
 public String getStrategyName(){return "Balanced";}
 private double normalize(Route r,List<Route> routes,ToDoubleFunction<Route> metric){double min=routes.stream().mapToDouble(metric).min().orElseThrow();double max=routes.stream().mapToDouble(metric).max().orElseThrow();return RouteRules.normalized(metric.applyAsDouble(r),min,max);}
 private double score(Route r,List<Route> routes,DeliveryRequest q){return tw*normalize(r,routes,RouteRules::totalMinutes)+cw*normalize(r,routes,x->RouteRules.totalCost(x,q))+dw*normalize(r,routes,Route::distanceKm)+ew*normalize(r,routes,Route::carbonEmission);}
 public RouteDecision selectRoute(List<Route> routes,DeliveryRequest request){List<Route> valid=RouteRules.eligible(routes,request);Route best=valid.stream().min(Comparator.comparingDouble((Route r)->score(r,valid,request)).thenComparing(Route::id)).orElseThrow();double score=score(best,valid,request);return new RouteDecision(best,getStrategyName(),score,"Normalized time/cost/distance/emissions weighted "+tw+"/"+cw+"/"+dw+"/"+ew+"; score="+String.format(Locale.US,"%.3f",score)+"; rejected="+(routes.size()-valid.size())+" routes by constraints");}
}
