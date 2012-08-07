/*
	Copyright 2008-2014 ITACA-TSB, http://www.tsb.upv.es
	Instituto Tecnologico de Aplicaciones de Comunicacion 
	Avanzadas - Grupo Tecnologias para la Salud y el 
	Bienestar (TSB)
	
	See the NOTICE file distributed with this work for additional 
	information regarding copyright ownership
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	  http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.universAAL.support.utils.service.top;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.ontology.phThing.OnOffActuator;
import org.universAAL.support.utils.service.mid.UtilActuator;

/**
 * This is an abstract class for those who want to use the typical services of
 * an actuator (get status, set on, set off) over an ontological service.
 * Classes extending this abstract class will be ServiceCallees which handle by
 * default these 3 services. Those considering using UtilActuator could take
 * advantage of this class if they want only to handle those 3 typical services
 * profiles and no more.
 * 
 * @author alfiva
 * 
 */
public abstract class UtilActuatorCallee extends ServiceCallee {

    /**
     * Namespace for auxiliary URIs used in this class.
     */
    private String calleeNamespace;

    /**
     * Default error response.
     */
    ServiceResponse errorResponse = new ServiceResponse(
	    CallStatus.serviceSpecificFailure);

    /**
     * Default constructor of the class. Takes the same parameters needed by a
     * UtilActuator profile method, in addition to the ModuleContext.
     * 
     * @param context
     *            The Module Context of uAAL
     * @param namespace
     *            The namespace of your server, ending with the character #
     * @param actuator
     *            The ontology instance of the actuator you are controlling. The
     *            more properties it has set, the better.
     */
    public UtilActuatorCallee(ModuleContext context, String namespace,
	    OnOffActuator actuator) {
	super(context, UtilActuator.getServiceProfiles(namespace, actuator));
	this.calleeNamespace = namespace;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.universAAL.middleware.service.ServiceCallee#handleCall(org.universAAL
     * .middleware.service.ServiceCall)
     */
    @Override
    public ServiceResponse handleCall(ServiceCall call) {
	if (call == null)
	    return null;

	String operation = call.getProcessURI();
	if (operation == null)
	    return null;

	if (operation.startsWith(calleeNamespace
		+ UtilActuator.SERVICE_GET_ON_OFF)) {
	    boolean result = executeGet();
	    ServiceResponse response = new ServiceResponse(CallStatus.succeeded);
	    response.addOutput(new ProcessOutput(calleeNamespace
		    + UtilActuator.OUT_GET_ON_OFF, Boolean.valueOf(result)));
	    return response;
	}

	if (operation.startsWith(calleeNamespace
		+ UtilActuator.SERVICE_TURN_OFF)) {
	    if (executeOff()) {
		return new ServiceResponse(CallStatus.succeeded);
	    } else {
		return errorResponse;
	    }

	}

	if (operation.startsWith(calleeNamespace
		+ UtilActuator.SERVICE_TURN_ON)) {
	    if (executeOn()) {
		return new ServiceResponse(CallStatus.succeeded);
	    } else {
		return errorResponse;
	    }
	}

	errorResponse
		.addOutput(new ProcessOutput(
			ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
			"The service requested has not been implemented in this simple editor callee"));
	return errorResponse;
    }

    /**
     * When a SET ON service request is received, this method is called
     * automatically.
     * 
     * @return <code>true</code> if the actuator could be set to ON
     */
    public abstract boolean executeOn();

    /**
     * When a SET OFF service request is received, this method is called
     * automatically.
     * 
     * @return <code>true</code> if the actuator could be set to OFF
     */
    public abstract boolean executeOff();

    /**
     * When a GET STATUS service request is received, this method is called
     * automatically.
     * 
     * @return The Boolean value representing the status property of the
     *         actuator.
     */
    public abstract boolean executeGet();

}
