package delivery;
import java.util.*;
public class StrategyComparison {
 public static List<RouteDecision> compare(List<RouteStrategy> strategies,List<Route> routes,DeliveryRequest request){return strategies.stream().map(s->s.selectRoute(routes,request)).toList();}
}
