package delivery;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class DeliveryTest {
 List<Route> routes=Main.demoRoutes();
 DeliveryRequest request=new DeliveryRequest(DeliveryPriority.STANDARD,4000,60,false);
 @Test void fastest(){assertEquals("R6",new FastestRouteStrategy().selectRoute(routes,request).route().id());}
 @Test void cheapest(){assertEquals("R5",new CheapestRouteStrategy().selectRoute(routes,request).route().id());}
 @Test void eco(){assertEquals("R3",new EcoFriendlyRouteStrategy().selectRoute(routes,request).route().id());}
 @Test void balanced(){assertNotNull(new BalancedRouteStrategy(.4,.3,.1,.2).selectRoute(routes,request).route());}
 @Test void fifthStrategy(){assertEquals("R5",new DistanceFirstRouteStrategy().selectRoute(routes,request).route().id());}
 @Test void runtimeSwitch(){RoutePlanner p=new RoutePlanner(new FastestRouteStrategy());String first=p.plan(routes,request).route().id();p.setStrategy(new CheapestRouteStrategy());assertNotEquals(first,p.plan(routes,request).route().id());}
 @Test void automaticUrgent(){assertInstanceOf(FastestRouteStrategy.class,new StrategySelector().select(new DeliveryRequest(DeliveryPriority.URGENT,4000,60,false)));}
 @Test void automaticEconomy(){assertInstanceOf(CheapestRouteStrategy.class,new StrategySelector().select(new DeliveryRequest(DeliveryPriority.ECONOMY,4000,60,false)));}
 @Test void automaticEco(){assertInstanceOf(EcoFriendlyRouteStrategy.class,new StrategySelector().select(new DeliveryRequest(DeliveryPriority.ECO,4000,60,false)));}
 @Test void automaticStandard(){assertInstanceOf(BalancedRouteStrategy.class,new StrategySelector().select(request));}
 @Test void ecoPreferred(){assertInstanceOf(EcoFriendlyRouteStrategy.class,new StrategySelector().select(new DeliveryRequest(DeliveryPriority.STANDARD,4000,60,true)));}
 @Test void budgetRestriction(){assertThrows(NoSuitableRouteException.class,()->new CheapestRouteStrategy().selectRoute(routes,new DeliveryRequest(DeliveryPriority.STANDARD,1,60,false)));}
 @Test void deadlineRestriction(){assertThrows(NoSuitableRouteException.class,()->new FastestRouteStrategy().selectRoute(routes,new DeliveryRequest(DeliveryPriority.STANDARD,4000,1,false)));}
 @Test void unavailableRejected(){assertNotEquals("R7",new DistanceFirstRouteStrategy().selectRoute(routes,request).route().id());}
 @Test void emptyRoutes(){assertThrows(NoSuitableRouteException.class,()->new FastestRouteStrategy().selectRoute(List.of(),request));}
 @Test void invalidWeights(){assertThrows(IllegalArgumentException.class,()->new BalancedRouteStrategy(.5,.5,.5,-.5));}
 @Test void nanWeights(){assertThrows(IllegalArgumentException.class,()->new BalancedRouteStrategy(Double.NaN,0,0,1));}
 @Test void compareStrategies(){var results=StrategyComparison.compare(List.of(new FastestRouteStrategy(),new CheapestRouteStrategy()),routes,request);assertEquals(2,results.size());assertNotEquals(results.get(0).route().id(),results.get(1).route().id());}
 @Test void boundaryBudget(){Route r=routes.get(4);double exact=RouteRules.totalCost(r,request);assertEquals("R5",new CheapestRouteStrategy().selectRoute(routes,new DeliveryRequest(DeliveryPriority.STANDARD,exact,60,false)).route().id());}
 @Test void negativeRoute(){assertThrows(IllegalArgumentException.class,()->new Route("bad",routes.get(0).origin(),routes.get(0).destination(),-1,10,10,1,true,0,0));}
 @Test void nullRequest(){assertThrows(NullPointerException.class,()->new FastestRouteStrategy().selectRoute(routes,null));}
 @Test void invalidRequest(){assertThrows(IllegalArgumentException.class,()->new DeliveryRequest(DeliveryPriority.STANDARD,-1,20,false));}
 @Test void configurableSelector(){StrategySelector s=new StrategySelector();s.register(DeliveryPriority.STANDARD,new DistanceFirstRouteStrategy());assertInstanceOf(DistanceFirstRouteStrategy.class,s.select(request));}
 @Test void explanation(){assertTrue(new FastestRouteStrategy().selectRoute(routes,request).explanation().contains("traffic penalty"));}
}
