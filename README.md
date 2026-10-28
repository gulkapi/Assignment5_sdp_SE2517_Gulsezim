# Smart Delivery & Route Optimization Engine

Java 17 / Maven / JUnit 5 implementation of the Strategy design pattern.

## Run

```bash
mvn clean test
mvn exec:java
```

## Architecture

`RoutePlanner` is the Context and depends only on `RouteStrategy`, the Strategy interface. Concrete strategies are `FastestRouteStrategy`, `CheapestRouteStrategy`, `EcoFriendlyRouteStrategy`, `BalancedRouteStrategy`, and `DistanceFirstRouteStrategy`. `StrategySelector` maps delivery priorities to strategy instances, while `RouteRules` filters routes against hard constraints. `RouteDecision` contains the selected route, numeric score and explanation.

## Why Strategy?

Instead of a growing conditional statement, each routing algorithm is encapsulated in a replaceable object. The same `RoutePlanner` changes algorithms via `setStrategy()` in `Main`. `Main` demonstrates four runtime switches and automatic selection. To add a strategy, implement `RouteStrategy` and register it with the selector if desired; `RoutePlanner` remains unchanged.

## Scoring

- Fastest: estimated minutes + traffic penalty.
- Cheapest: base cost + 12 per km + extra fee + 250 for urgent requests.
- EcoFriendly: 0.8 * emissions + 0.2 * distance.
- Balanced: 0.4 * normalized time + 0.3 * normalized cost + 0.1 * normalized distance + 0.2 * normalized emissions. Weights are configurable, nonnegative and sum to 1.
- DistanceFirst: distance in km.

Normalization uses (value - min)/(max - min) across eligible candidates, or zero if all values are equal. Lower scores win. Unavailable routes, over-budget routes and late routes are excluded before scoring. No valid candidates raise `NoSuitableRouteException`.

## Strategy vs Bridge

Strategy switches among interchangeable algorithms for one routing task. Bridge separates two independent abstraction hierarchies so both can vary independently.

## SOLID

SRP separates planning, selection, scoring and validation. OCP allows new algorithms without editing the planner. DIP makes the planner depend on `RouteStrategy` rather than a concrete implementation.

## Limitation

The demo uses synthetic static routes and estimated costs rather than real traffic, maps, live emissions or real-world road restrictions. Explanations summarize scoring and the count of excluded routes rather than listing every rejection individually.

## UML

- [Class diagram source](docs/class-diagram.puml)
- [Sequence diagram source](docs/sequence-diagram.puml)

Render `.puml` files with PlantUML or a compatible IDE extension.

## Example console output

```text
RouteDecision[route=Route[id=R6, ...], strategy=Fastest, score=17.0, explanation=Adjusted time = base time + traffic penalty; score=17.00; rejected=1 routes by constraints]
```

## Tests

Run `mvn clean test` for 24 JUnit 5 tests covering the strategies, runtime switching, automatic selection, budget/deadline restrictions, invalid weights, failure cases and comparison. Check the Maven output for actual test results.
