API bundles for JellyFish representation of the System Descriptor.  These bundles do not have a dependency on the XText
based DSL or any other DSL.  You can find implementations of some of the APIs at
https://github.ms.northgrum.com/CEACIDE/jellyfish-systemdescriptor-ext.

# com.ngc.seaside.systemdescriptor.model.api
Contains the model interfaces used by the system descriptor.

# com.ngc.seaside.systemdescriptor.service.api
Contains the API for the service based interfaces for interacting with the system descriptor.

# com.ngc.seaside.systemdescriptor.model.impl.basic
Contains a basic POJO based implementation of the model API.  This implementation is useful in tests and other areas
where you don't want to create a dependency on the XTest based DSL.

Testing change and branch protection
