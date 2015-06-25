/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package ${package}.client;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.HasPresenter;
import org.jboss.as.console.client.core.MainLayoutPresenter;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.v3.behaviour.CrudOperationDelegate;
import org.jboss.as.console.client.v3.dmr.AddressTemplate;
import org.jboss.as.console.client.v3.dmr.Operation;
import org.jboss.as.console.spi.RequiredResources;
import org.jboss.as.console.spi.SearchIndex;
import org.jboss.as.console.spi.SubsystemExtension;
import org.jboss.dmr.client.ModelNode;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.dispatch.impl.DMRAction;
import org.jboss.dmr.client.dispatch.impl.DMRResponse;
import ${package}.client.i18n.I18n;
import org.useware.kernel.gui.behaviour.StatementContext;

import javax.inject.Inject;

import java.util.Map;

import static org.jboss.dmr.client.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.dmr.client.ModelDescriptionConstants.RESULT;

public class ExtensionPresenter
        extends Presenter<ExtensionPresenter.MyView, ExtensionPresenter.MyProxy> {

    public final static String ROOT_RESOURCE = "{selected.profile}/subsystem=${subsystem}";
    public final static AddressTemplate ROOT_RESOURCE_ADDRESS = AddressTemplate.of(ROOT_RESOURCE);


    @ProxyCodeSplit
    @NameToken("${nameToken}")
    @RequiredResources(resources = ROOT_RESOURCE)
    @SearchIndex(keywords = {"${subsystem}"})
    @SubsystemExtension(name = "${extensionName}", group = "Extensions", key = "${subsystem}")
    public interface MyProxy extends ProxyPlace<ExtensionPresenter> {}


    public interface MyView extends View, HasPresenter<ExtensionPresenter> {

        void update(ModelNode data);
    }


    // ------------------------------------------------------ presenter lifecycle

    private final StatementContext statementContext;
    private final DispatchAsync dispatcher;
    private final CrudOperationDelegate operationDelegate;
    private final I18n i18n;

    @Inject
    public ExtensionPresenter(final EventBus eventBus,
            final MyView view,
            final MyProxy proxy,
            final StatementContext statementContext,
            final DispatchAsync dispatcher,
            final I18n i18n) {
        super(eventBus, view, proxy, MainLayoutPresenter.TYPE_MainContent);
        this.statementContext = statementContext;
        this.dispatcher = dispatcher;
        this.i18n = i18n;
        this.operationDelegate = new CrudOperationDelegate(statementContext, dispatcher);
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        loadSubsystem();
    }


    // ------------------------------------------------------ subsystem methods

    private void loadSubsystem() {
        Operation operation = new Operation.Builder(READ_RESOURCE_OPERATION,
                ROOT_RESOURCE_ADDRESS.resolve(statementContext)).build();
        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(final DMRResponse response) {
                ModelNode body = response.get();
                if (body.isFailure()) {
                    Console.error(i18n.extensionConstants().load_failed(), body.getFailureDescription());
                } else {
                    getView().update(body.get(RESULT));
                }
            }
        });
    }

    public void onSaveResource(final Map<String, Object> changedValues) {
        operationDelegate.onSaveResource(ROOT_RESOURCE_ADDRESS, null, changedValues,
                new CrudOperationDelegate.Callback() {
                    @Override
                    public void onFailure(final AddressTemplate addressTemplate, final String name, final Throwable t) {
                        Console.error(i18n.consoleMessages().modificationFailed("subsystem '${subsystem}'"),
                                t.getMessage());
                    }

                    @Override
                    public void onSuccess(final AddressTemplate addressTemplate, final String name) {
                        Console.info(i18n.consoleMessages().modified("subsystem '${subsystem}'"));
                        loadSubsystem();
                    }
                });
    }
}
