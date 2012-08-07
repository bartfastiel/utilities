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
package org.universAAL.support.utils.service.mid;

import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.rdf.PropertyPath;
import org.universAAL.middleware.service.ServiceRequest;
import org.universAAL.middleware.service.owl.Service;
import org.universAAL.middleware.service.owls.process.ProcessInput;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.phThing.OnOffSensor;
import org.universAAL.support.utils.service.*;
import org.universAAL.support.utils.service.low.Request;

/**
 * This is a helper class for those who want to use the typical services of an
 * on/off sensor (get status) over an ontological service. It provides methods
 * for automatically generating service profiles and service requests that can
 * be used in ServiceCallees and ServiceCallers. If they are both with the same
 * parameters in both sides, the services are guaranteed to match.
 * 
 * @author alfiva
 * 
 */
public class UtilSensor {
    public static final String SERVICE_GET_ON_OFF = "servSensorGet";
    public static final String OUT_GET_ON_OFF = "outputSensorGet";
    public static final String IN_DEVICE = "inputSensorAll";

    /**
     * Gives you the typical service profile of an on/off sensor service: Get
     * status. When handling requests in your Callee, you can use the references
     * to services and arguments URIs prepending <code>namespace</code> to
     * UtilSensor constants.
     * <p>
     * Example:
     * <p>
     * <code>
     * new SCallee(context, getServiceProfiles("http://ontology.universAAL.org/OvenServer.owl#", CarpetSensor.MY_URI, myCarpetSensor))
     * </code>
     * <p>
     * 
     * @param namespace
     *            The namespace of your server, ending with the character #. You
     *            can optionally add some prefix after the # if you use
     *            UtilSensor more than once in the same Callee.
     * @param ontologyURI
     *            The MY_URI of the class of DeviceService ontology you are
     *            going to implement. It MUST be a subclass of DeviceService.
     * @param sensor
     *            The ontology instance of the sensor you are controlling. The
     *            more properties it has set, the better.
     * @return An array with the 1 typical service profiles
     */
    public static ServiceProfile[] getServiceProfiles(String namespace,
	    String ontologyURI, OnOffSensor sensor) {

	ServiceProfile[] profiles = new ServiceProfile[1];

	PropertyPath ppath = new PropertyPath(null, true, new String[] {
		DeviceService.PROP_CONTROLS, OnOffSensor.PROP_MEASURED_VALUE });

	ProcessInput input = new ProcessInput(namespace + IN_DEVICE);
	input.setParameterType(sensor.getClassURI());
	input.setCardinality(1, 0);

	MergedRestriction r = MergedRestriction.getFixedValueRestriction(
		DeviceService.PROP_CONTROLS, sensor);

	Service getOnOff = (Service) OntologyManagement.getInstance()
		.getResource(ontologyURI, namespace + SERVICE_GET_ON_OFF);
	profiles[0] = getOnOff.getProfile();
	ProcessOutput output = new ProcessOutput(namespace + OUT_GET_ON_OFF);
	output.setCardinality(1, 1);
	profiles[0].addOutput(output);
	profiles[0].addSimpleOutputBinding(output, ppath.getThePath());
	profiles[0].addInput(input);
	profiles[0].getTheService().addInstanceLevelRestriction(r,
		new String[] { DeviceService.PROP_CONTROLS });

	return profiles;
    }

    /**
     * Gives you the typical service profile of an on/off sensor service: Get
     * status. When handling requests in your Callee, you can use the references
     * to services and arguments URIs prepending <code>namespace</code> to
     * UtilSensor constants.
     * 
     * @param namespace
     *            The namespace of your server, ending with the character #. You
     *            can optionally add some prefix after the # if you use
     *            UtilSensor more than once in the same Callee.
     * @param sensor
     *            The ontology instance of the sensor you are controlling. The
     *            more properties it has set, the better.
     * @return An array with the 1 typical service profiles
     */
    public static ServiceProfile[] getServiceProfiles(String namespace,
	    OnOffSensor sensor) {
	return getServiceProfiles(namespace, DeviceService.MY_URI, sensor);
    }

    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param ontologyURI
     *            The MY_URI of the class of Service ontology you want to call
     * @param argIn
     *            Value representing the input you want to pass as
     *            parameter. The sensor GET service will be called for this
     *            specific actuator.
     * @param argOut
     *            The returned value of the sensor GET service will be placed in
     *            the URI represented by this Output. Look for it there in
     *            the response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(String ontologyURI,
	    Variable argIn, Output argOut) {
	Request req = new Request((Service) OntologyManagement
		.getInstance().getResource(ontologyURI, null));
	req.put(Path.at(DeviceService.PROP_CONTROLS), argIn);
	req.put(Path.at(DeviceService.PROP_CONTROLS).to(
		OnOffSensor.PROP_MEASURED_VALUE), argOut);
	return req;
    }
    
    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param ontologyURI
     *            The MY_URI of the class of Service ontology you want to call
     * @param in
     *            Object representing the input you want to pass as
     *            parameter. The sensor GET service will be called for this
     *            specific actuator.
     * @param out
     *            The returned value of the sensor GET service will be placed in
     *            this URI. Look for it there in
     *            the response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(String ontologyURI,
	    Object in, String out) {
	Request req = new Request((Service) OntologyManagement
		.getInstance().getResource(ontologyURI, null));
	req.put(Path.at(DeviceService.PROP_CONTROLS), Arg.in(in));
	req.put(Path.at(DeviceService.PROP_CONTROLS).to(
		OnOffSensor.PROP_MEASURED_VALUE), Arg.out(out));
	return req;
    }

    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param ontologyURI
     *            The MY_URI of the class of Service ontology you want to call
     * @param sensor
     *            The ontology instance of the sensor you want to get the status
     *            from.
     * @param argOut
     *            The returned value of the sensor GET service will be placed in
     *            the URI represented by this Output. Look for it there in the
     *            response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(String ontologyURI,
	    OnOffSensor sensor, Output argOut) {
	return requestGetOnOff(ontologyURI, Arg.in(sensor), argOut);
    }
    
    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param ontologyURI
     *            The MY_URI of the class of Service ontology you want to call
     * @param sensor
     *            The ontology instance of the sensor you want to get the status
     *            from.
     * @param out
     *            The returned value of the sensor GET service will be placed in
     *            the URI represented by this String. Look for it there in the
     *            response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(String ontologyURI,
	    OnOffSensor sensor, String out) {
	return requestGetOnOff(ontologyURI, Arg.in(sensor), Arg.out(out));
    }

    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param sensor
     *            The ontology instance of the sensor you want to get the status
     *            from.
     * @param argOut
     *            The returned value of the sensor GET service will be placed in
     *            the URI represented by this Output. Look for it there in the
     *            response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(OnOffSensor actuator,
	    Output argOut) {
	return requestGetOnOff(DeviceService.MY_URI, actuator, argOut);
    }
    
    /**
     * Gives you the typical GET STATUS service request for sensor services. If
     * the editor service also used UtilSensor the match is guaranteed.
     * 
     * @param sensor
     *            The ontology instance of the sensor you want to get the status
     *            from.
     * @param out
     *            The returned value of the sensor GET service will be placed in
     *            the URI represented by this String. Look for it there in the
     *            response.
     * @return The ServiceRequest that will call the matching GET STATUS service
     *         of an sensor
     */
    public static ServiceRequest requestGetOnOff(OnOffSensor actuator,
	    String out) {
	return requestGetOnOff(DeviceService.MY_URI, actuator, out);
    }

}
