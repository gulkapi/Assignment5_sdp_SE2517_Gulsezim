package delivery;
import java.util.*;
public class Main {
 public static List<Route> demoRoutes(){
  Location a=new Location("Warehouse",51.17,71.43),b=new Location("Customer",51.20,71.48);
  return List.of(new Route("R1",a,b,8,22,1200,2.5,true,8,100),new Route("R2",a,b,12,18,1800,3.5,true,2,0),new Route("R3",a,b,7,30,950,1.2,true,4,0),new Route("R4",a,b,9,26,1100,.8,true,3,50),new Route("R5",a,b,6,42,700,1.8,true,3,0),new Route("R6",a,b,15,16,2300,4.2,true,1,0),new Route("R7",a,b,5,20,500,.4,false,0,0),new Route("R8",a,b,10,24,1250,1.0,true,6,0));
 }
 public static void main(String[] args){List<Route> routes=demoRoutes();DeliveryRequest request=new DeliveryRequest(DeliveryPriority.STANDARD,3000,60,false);List<RouteStrategy> strategies=List.of(new FastestRouteStrategy(),new CheapestRouteStrategy(),new EcoFriendlyRouteStrategy(),new BalancedRouteStrategy(.4,.3,.1,.2),new DistanceFirstRouteStrategy());RoutePlanner planner=new RoutePlanner(strategies.get(0));for(RouteStrategy strategy:strategies){planner.setStrategy(strategy);System.out.println(planner.plan(routes,request));}StrategySelector selector=new StrategySelector();for(DeliveryPriority priority:DeliveryPriority.values()){DeliveryRequest q=new DeliveryRequest(priority,4000,60,false);planner.setStrategy(selector.select(q));System.out.println("Automatic "+priority+": "+planner.plan(routes,q));}System.out.println("Comparison: "+StrategyComparison.compare(strategies,routes,request));}
}
