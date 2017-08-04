/*
 * Copyright (c) PANGAEA - Data Publisher for Earth & Environmental Science
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.pangaea.eypfixo3ld.vocab;

import org.semanticweb.owlapi.model.IRI;

/**
 * <p>
 * Title: SOSA
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Project:
 * </p>
 * <p>
 * Copyright: PANGAEA
 * </p>
 */

public class SOSA {

	public static final IRI ns = IRI.create("http://www.w3.org/ns/sosa/");

	public static final IRI Observation = c("Observation");
	public static final IRI Platform = c("Platform");
	public static final IRI FeatureOfInterest = c("FeatureOfInterest");
	public static final IRI Sensor = c("Sensor");
	public static final IRI ObservableProperty = c("ObservableProperty");
	public static final IRI Result  = c("Result");

	public static final IRI observes = c("observes");
	public static final IRI isObservedBy = c("isObservedBy");
	public static final IRI observedProperty = c("observedProperty");
	public static final IRI hosts = c("hosts");
	public static final IRI hasFeatureOfInterest = c("hasFeatureOfInterest");
	
	public static final IRI hasResult = c("hasResult");
	public static final IRI hasSimpleResult = c("hasSimpleResult");
	public static final IRI resultTime = c("resultTime");

	private static IRI c(String suffix) {
		return IRI.create(ns.toString(), suffix);
	}
	
}
