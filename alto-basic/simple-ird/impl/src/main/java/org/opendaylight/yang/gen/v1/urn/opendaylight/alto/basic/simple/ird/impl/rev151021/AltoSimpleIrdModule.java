package org.opendaylight.yang.gen.v1.urn.opendaylight.alto.basic.simple.ird.impl.rev151021;

import org.opendaylight.alto.basic.impl.AltoSimpleIrdProvider;

public class AltoSimpleIrdModule extends org.opendaylight.yang.gen.v1.urn.opendaylight.alto.basic.simple.ird.impl.rev151021.AbstractAltoSimpleIrdModule {
    public AltoSimpleIrdModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public AltoSimpleIrdModule(org.opendaylight.controller.config.api.ModuleIdentifier identifier, org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, org.opendaylight.yang.gen.v1.urn.opendaylight.alto.basic.simple.ird.impl.rev151021.AltoSimpleIrdModule oldModule, java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    public void customValidation() {
        // add custom validation form module attributes here.
    }

    @Override
    public java.lang.AutoCloseable createInstance() {
        final AltoSimpleIrdProvider provider = new AltoSimpleIrdProvider();

        try {
            getBrokerDependency().registerProvider(provider);
            provider.setupRoute(getAltoNorthboundRouterDependency());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return provider;
    }

}
