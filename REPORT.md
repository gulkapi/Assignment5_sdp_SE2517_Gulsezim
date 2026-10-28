# Assignment 5 Report — Smart Delivery & Route Optimization Engine

## Problem and Objective

Different deliveries need different optimization goals. Strategy encapsulates route selection algorithms and lets the application replace them without changing the planner.

## Design

The Context is `RoutePlanner`, which delegates to the `RouteStrategy` interface. Five concrete strategies implement alternative scoring rules. `StrategySelector` manages an extensible priority-to-strategy map. `RouteRules` enforces availability, cost and deadline restrictions before ranking candidates. Eight synthetic routes provide varied time, cost, emissions, distance and traffic conditions.

## Mathematical Rules

Let `t = estimatedMinutes + trafficMinutes` and `c = baseCost + 12 * distanceKm + extraFee + urgentSurcharge`, where the urgent surcharge is 250 and otherwise 0.

Fastest minimizes `t`. Cheapest minimizes `c`. EcoFriendly minimizes `0.8 * carbonEmission + 0.2 * distanceKm`. Balanced minimizes `0.4 * norm(t) + 0.3 * norm(c) + 0.1 * norm(distanceKm) + 0.2 * norm(carbonEmission)`. Configurable weights must be finite, nonnegative and total 1. DistanceFirst minimizes distance. All scores are minimized only among eligible routes.

## Switching and Selection

The same `RoutePlanner` instance changes strategies in `Main` using `setStrategy()`. `StrategySelector` maps URGENT to Fastest, ECONOMY to Cheapest, ECO to EcoFriendly, and STANDARD to Balanced; a STANDARD request with `ecoPreferred=true` selects EcoFriendly.

## Failure Handling

Empty or infeasible candidate sets throw `NoSuitableRouteException`. Invalid domain values and invalid weights throw `IllegalArgumentException`. Null route collections and requests are rejected. Routes failing hard constraints are excluded regardless of their score.

## Comparison and Explainability

`StrategyComparison` executes all supplied strategies for the same request. Each `RouteDecision` records the route, strategy, score and a concise scoring explanation with the number of rejected routes.

## Extensibility and SOLID

DistanceFirst is an added fifth algorithm. The planner required no modification to support it. SRP, OCP and DIP are reflected in the separate planner, selector, validation and strategy types.

## UML Diagrams

See `docs/class-diagram.puml` and `docs/sequence-diagram.puml`.

## Testing

The suite contains 24 JUnit 5 tests. Execute `mvn clean test` and attach a screenshot of the actual Maven summary in Moodle. Do not claim successful execution unless the command passes in your environment.

## Limitations

The model is deterministic and synthetic; real route optimization would integrate traffic, road network routing and dynamic pricing. Balanced normalization is request-specific and changes when the eligible candidate set changes. Explanation output is concise and does not enumerate every excluded route.
