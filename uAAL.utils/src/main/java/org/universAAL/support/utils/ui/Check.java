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
package org.universAAL.support.utils.ui;

import org.universAAL.middleware.container.utils.StringUtils;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.ui.rdf.Group;
import org.universAAL.middleware.ui.rdf.InputField;
import org.universAAL.middleware.ui.rdf.Label;

/**
 * Class representing a Boolean Input UI element. Boolean Inputs get a binary
 * value, whether it's a checkbox, switch or any other way, depending on the UI
 * renderer. The input is represented as a Boolean.
 * <p>
 * Example render:
 * <p>
 * 
 * <pre>
 * [X] Label
 * </pre>
 * 
 * @author alfiva
 * 
 */
public class Check extends InputControl {

	/**
	 * Initial value.
	 */
	private Boolean initialValue = Boolean.FALSE;

	/**
	 * Generic empty constructor. The Input will be generated with default
	 * values (false).
	 */
	public Check() {
	}

	/**
	 * Constructor with the reference of the input to be used in request and
	 * response. The reference is a property path, but in this constructor it is
	 * simplified as a single String (a single-property path). All other
	 * properties of the input are set to defaults (false). Use method
	 * setReference(String[] path) to set a path through several properties.
	 * 
	 * @param ref
	 *            The simple reference identifying the input. Set to null to
	 *            auto-generate.
	 */
	public Check(String ref) {
		setReference(ref);
	}

	/**
	 * Constructor with the reference of the input to be used in request and
	 * response. The reference is a property path, but in this constructor it is
	 * simplified as a single String (a single-property path). Use method
	 * setReference(String[] path) to set a path through several properties.
	 * 
	 * @param ref
	 *            The simple reference identifying the input. Set to null to
	 *            auto-generate.
	 * @param label
	 *            The label text that identifies the input to the user.
	 */
	public Check(String ref, String label) {
		setReference(ref);
		this.label = new Label(label, null);
	}

	/**
	 * Constructor with the reference of the input to be used in request and
	 * response. The reference is a property path, but in this constructor it is
	 * simplified as a single String (a single-property path). Use method
	 * setReference(String[] path) to set a path through several properties.
	 * 
	 * @param ref
	 *            The simple reference identifying the input. Set to null to
	 *            auto-generate.
	 * @param label
	 *            The label text that identifies the input to the user.
	 * @param initialValue
	 *            The value the boolean input field has by default. If it is not
	 *            changed by the user this will be the value of the input in the
	 *            response.
	 */
	public Check(String ref, String label, Boolean initialValue) {
		setReference(ref);
		this.label = new Label(label, null);
		this.initialValue = initialValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.universAAL.support.utils.ui.Control#create(org.universAAL.middleware.
	 * ui.rdf.Group)
	 */
	public String[] create(Group group) {
		if (ref == null) {
			setReference(MY_NAMESPACE + StringUtils.createUniqueID());
		}
		model = new InputField(group, label, ref, MergedRestriction.getAllValuesRestrictionWithCardinality(
				ref.getLastPathElement(), TypeMapper.getDatatypeURI(Boolean.class), 1, 1), initialValue);
		return ref.getThePath();
	}

	/**
	 * Get the initial value of the input by default.
	 * 
	 * @return The initial value.
	 */
	public Boolean getInitialValue() {
		return initialValue;
	}

	/**
	 * Set the initial value of the input by default.
	 * 
	 * @param initialValue
	 *            The initial value.
	 */
	public void setInitialValue(Boolean initialValue) {
		this.initialValue = initialValue;
	}

}
