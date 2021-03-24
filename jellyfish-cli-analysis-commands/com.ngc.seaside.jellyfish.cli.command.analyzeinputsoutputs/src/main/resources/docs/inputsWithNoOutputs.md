## Avoid components that have inputs but no outputs
Models of components that receive inputs but do not produce outputs can result in components that cannot be tested or
verified.  Minimally, a component should produce some type of output that indicates it has received some input.  This
allows the component to be tested as well as inspected.  This applies to both components that use pub/sub messaging an
well as components that use request/response to exchange messages.

