package de.pangaea.eypfixo3ld;
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

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import de.pangaea.eypfixo3ld.vocab.EYP;
import de.pangaea.eypfixo3ld.vocab.SSN;

import static de.pangaea.eypfixo3ld.vocab.EYP.AcousticDopplerCurrentProfiler;
import static de.pangaea.eypfixo3ld.vocab.EYP.HydroacousticCurrentMeter;
import static de.pangaea.eypfixo3ld.vocab.EYP.CellSize;
import static de.pangaea.eypfixo3ld.vocab.EYP.DopplerEffect;
import static de.pangaea.eypfixo3ld.vocab.EYP.Infrared;
import static de.pangaea.eypfixo3ld.vocab.EYP.WaterCurrent;
import static de.pangaea.eypfixo3ld.vocab.EYP.CarbonDioxide;
import static de.pangaea.eypfixo3ld.vocab.EYP.Frequency;
import static de.pangaea.eypfixo3ld.vocab.EYP.MeasuringRange;
import static de.pangaea.eypfixo3ld.vocab.EYP.OperatingDepth;
import static de.pangaea.eypfixo3ld.vocab.EYP.PartialPressureOfCO2Analyzer;
import static de.pangaea.eypfixo3ld.vocab.EYP.ProfilingRange;
import static de.pangaea.eypfixo3ld.vocab.EYP.TemperatureRange;
import static de.pangaea.eypfixo3ld.vocab.EYP.Speed;
import static de.pangaea.eypfixo3ld.vocab.EYP.PartialPressure;
import static de.pangaea.eypfixo3ld.vocab.SSNSystem.SystemProperty;
import static de.pangaea.eypfixo3ld.vocab.SOSA.FeatureOfInterest;
import static de.pangaea.eypfixo3ld.vocab.SOSA.ObservableProperty;
import static de.pangaea.eypfixo3ld.vocab.SSNSystem.SystemCapability;
import static de.pangaea.eypfixo3ld.vocab.SOSA.Sensor;
import static de.pangaea.eypfixo3ld.vocab.SSN.Stimulus;
import static de.pangaea.eypfixo3ld.vocab.SSN.detects;
import static de.pangaea.eypfixo3ld.vocab.SSNSystem.hasSystemCapability;
import static de.pangaea.eypfixo3ld.vocab.SSNSystem.hasSystemProperty;
import static de.pangaea.eypfixo3ld.vocab.SSN.isPropertyOf;
import static de.pangaea.eypfixo3ld.vocab.SSN.isProxyFor;
import static de.pangaea.eypfixo3ld.vocab.SOSA.observes;
import static de.pangaea.eypfixo3ld.vocab.Schema.PropertyValue;
import static de.pangaea.eypfixo3ld.vocab.Schema.maxValue;
import static de.pangaea.eypfixo3ld.vocab.Schema.minValue;
import static de.pangaea.eypfixo3ld.vocab.Schema.unitCode;
import static de.pangaea.eypfixo3ld.vocab.Schema.value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * <p>
 * Title: CreateEsonetYellowPages
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

public class CreateEsonetYellowPages {

	private final String eypDevicesFile = "src/main/resources/config/esonetyellowpages-devicetypes.json";
	private final String eypFile = "src/main/resources/rdf/esonetyellowpages.rdf";
	private final String eypInferredFile = "src/main/resources/rdf/esonetyellowpages-inferred.rdf";

	private OntologyManager m;

	private void run() throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		m = new OntologyManager(EYP.ns);

		m.addTitle("ESONET Yellow Pages Ontology");
		m.addVersion("1.0");
		m.addDate("2017-07-31");
		m.addCreator("Markus Stocker");
		m.addSeeAlso("http://www.esonetyellowpages.com/");
		m.addImport(SSN.ns);

		m.addSubClass(Frequency, SystemProperty);
		m.addLabel(Frequency, "Frequency");
		m.addSubClass(ProfilingRange, SystemProperty);
		m.addLabel(ProfilingRange, "Profiling Range");
		m.addSubClass(CellSize, SystemProperty);
		m.addLabel(CellSize, "Cell Size");
		m.addSubClass(OperatingDepth, SystemProperty);
		m.addLabel(OperatingDepth, "Operating Depth");
		m.addSubClass(TemperatureRange, SystemProperty);
		m.addLabel(TemperatureRange, "Temperature Range");
		m.addSubClass(MeasuringRange, SystemProperty);
		m.addLabel(MeasuringRange, "Measuring Range");

		m.addClass(WaterCurrent);
		m.addLabel(WaterCurrent, "Water Current");
		m.addSubClass(WaterCurrent, FeatureOfInterest);

		m.addClass(CarbonDioxide);
		m.addLabel(CarbonDioxide, "Carbon Dioxide");
		m.addSubClass(CarbonDioxide, FeatureOfInterest);

		m.addClass(Speed);
		m.addLabel(Speed, "Speed");
		m.addSubClass(Speed, ObservableProperty);
		m.addObjectSome(Speed, isPropertyOf, WaterCurrent);

		m.addClass(PartialPressure);
		m.addLabel(PartialPressure, "Partial Pressure");
		m.addSubClass(PartialPressure, ObservableProperty);
		m.addObjectSome(PartialPressure, isPropertyOf, CarbonDioxide);

		m.addClass(DopplerEffect);
		m.addSubClass(DopplerEffect, Stimulus);
		m.addLabel(DopplerEffect, "Doppler Effect");
		m.addComment(DopplerEffect,
				"The Doppler effect (or the Doppler shift) is the change in frequency of a wave (or other periodic event) for an observer moving relative to its source.");
		m.addSource(DopplerEffect, IRI.create("https://en.wikipedia.org/wiki/Doppler_effect"));
		m.addObjectAll(DopplerEffect, isProxyFor, Speed);

		m.addClass(Infrared);
		m.addSubClass(Infrared, Stimulus);
		m.addLabel(Infrared, "Infrared");
		m.addComment(Infrared, "Infrared (IR) is an invisible radiant energy.");
		m.addSource(Infrared, IRI.create("https://en.wikipedia.org/wiki/Infrared"));
		m.addObjectAll(Infrared, isProxyFor, PartialPressure);

		m.addClass(AcousticDopplerCurrentProfiler);
		m.addLabel(AcousticDopplerCurrentProfiler, "Acoustic Doppler Current Profiler");
		m.addSource(AcousticDopplerCurrentProfiler,
				IRI.create("https://en.wikipedia.org/wiki/Acoustic_Doppler_current_profiler"));
		m.addComment(AcousticDopplerCurrentProfiler,
				"An acoustic Doppler current profiler (ADCP) is a hydroacoustic current meter similar to a sonar, attempting to measure water current velocities over a depth range using the Doppler effect of sound waves scattered back from particles within the water column.");
		m.addObjectAll(AcousticDopplerCurrentProfiler, detects, DopplerEffect);
		m.addObjectAll(AcousticDopplerCurrentProfiler, observes, Speed);
		m.addSubClass(AcousticDopplerCurrentProfiler, HydroacousticCurrentMeter);

		m.addClass(PartialPressureOfCO2Analyzer);
		m.addLabel(PartialPressureOfCO2Analyzer, "Partial Pressure of CO2 Analyzer");
		m.addObjectAll(PartialPressureOfCO2Analyzer, detects, Infrared);
		m.addObjectAll(PartialPressureOfCO2Analyzer, observes, PartialPressure);
		m.addSubClass(PartialPressureOfCO2Analyzer, Sensor);

		m.addClass(HydroacousticCurrentMeter);
		m.addLabel(HydroacousticCurrentMeter, "Hydroacoustic Current Meter");
		m.addSubClass(HydroacousticCurrentMeter, Sensor);

		JsonReader jr = Json.createReader(new FileReader(new File(eypDevicesFile)));
		JsonArray ja = jr.readArray();

		for (JsonObject jo : ja.getValuesAs(JsonObject.class)) {
			String name = jo.getString("name");
			String label = jo.getString("label");
			String comment = jo.getString("comment");
			String seeAlso = jo.getString("seeAlso");
			JsonArray equivalentClasses = jo.getJsonArray("equivalentClasses");
			JsonArray subClasses = jo.getJsonArray("subClasses");

			addDeviceType(name, label, comment, seeAlso, equivalentClasses, subClasses);
		}

		m.save(eypFile);
		m.saveInferred(eypInferredFile);
	}

	private void addDeviceType(String name, String label, String comment, String seeAlso, JsonArray equivalentClasses,
			JsonArray subClasses) {
		IRI deviceTypeIRI = IRI.create(name);

		m.addClass(deviceTypeIRI);
		m.addLabel(deviceTypeIRI, label);
		m.addComment(deviceTypeIRI, comment);
		m.addSeeAlso(deviceTypeIRI, seeAlso);

		// Default sub class, though implicit with curated sensing device type
		// hierarchy
		// m.addSubClass(deviceTypeIRI, SSN.SensingDevice);

		for (JsonObject equivalentClass : equivalentClasses.getValuesAs(JsonObject.class)) {
			if (equivalentClass.containsKey("type")) {
				m.addEquivalentClass(deviceTypeIRI, IRI.create(equivalentClass.getString("type")));
			}
		}

		for (JsonObject subClass : subClasses.getValuesAs(JsonObject.class)) {
			if (subClass.containsKey("type")) {
				m.addSubClass(deviceTypeIRI, IRI.create(subClass.getString("type")));
			} else if (subClass.containsKey("observes")) {
				JsonObject observesJson = subClass.getJsonObject("observes");
				String propertyLabel = observesJson.getString("label");
				String propertyType = observesJson.getString("type");
				JsonObject featureJson = observesJson.getJsonObject("isPropertyOf");
				String featureLabel = featureJson.getString("label");
				String featureType = featureJson.getString("type");

				IRI propertyIRI = IRI.create(EYP.ns.toString() + Utils.hashid(propertyLabel));
				IRI propertyTypeIRI = IRI.create(propertyType);
				IRI featureIRI = IRI.create(EYP.ns.toString() + Utils.hashid(featureLabel));
				IRI featureTypeIRI = IRI.create(featureType);

				m.addObjectValue(deviceTypeIRI, observes, propertyIRI);
				m.addIndividual(propertyIRI);
				m.addType(propertyIRI, propertyTypeIRI);
				m.addLabel(propertyIRI, propertyLabel);
				m.addIndividual(featureIRI);
				m.addType(featureIRI, featureTypeIRI);
				m.addLabel(featureIRI, featureLabel);
				m.addObjectAssertion(propertyIRI, isPropertyOf, featureIRI);
			} else if (subClass.containsKey("detects")) {
				JsonObject detectsJson = subClass.getJsonObject("detects");
				String stimulusLabel = detectsJson.getString("label");
				String stimulusType = detectsJson.getString("type");

				IRI stimulusIRI = IRI.create(EYP.ns.toString() + Utils.hashid(stimulusLabel));
				IRI stimulusTypeIRI = IRI.create(stimulusType);

				m.addObjectValue(deviceTypeIRI, detects, stimulusIRI);
				m.addIndividual(stimulusIRI);
				m.addType(stimulusIRI, stimulusTypeIRI);
				m.addLabel(stimulusIRI, stimulusLabel);
			} else if (subClass.containsKey("capability")) {
				JsonObject capabilityJson = subClass.getJsonObject("capability");
				String capabilityLabel = capabilityJson.getString("label");
				JsonArray propertiesJson = capabilityJson.getJsonArray("properties");

				IRI capabilityIRI = IRI.create(EYP.ns.toString() + Utils.hashid(capabilityLabel));

				m.addObjectValue(deviceTypeIRI, hasSystemCapability, capabilityIRI);
				m.addIndividual(capabilityIRI);
				m.addType(capabilityIRI, SystemCapability);
				m.addLabel(capabilityIRI, capabilityLabel);

				for (JsonObject propertyJson : propertiesJson.getValuesAs(JsonObject.class)) {
					String propertyLabel = propertyJson.getString("label");
					String propertyType = propertyJson.getString("type");
					String propertyUnitCode = propertyJson.getString("unitCode");

					IRI propertyIRI = IRI.create(EYP.ns.toString() + Utils.hashid(propertyLabel));
					IRI propertyTypeIRI = IRI.create(propertyType);

					m.addObjectAssertion(capabilityIRI, hasSystemProperty, propertyIRI);
					m.addIndividual(propertyIRI);
					m.addType(propertyIRI, SystemProperty);
					m.addType(propertyIRI, PropertyValue);
					m.addType(propertyIRI, propertyTypeIRI);
					m.addLabel(propertyIRI, propertyLabel);

					if (propertyJson.containsKey("value")) {
						m.addDataAssertion(propertyIRI, value, Float.valueOf(propertyJson.getString("value")));
					} else if (propertyJson.containsKey("minValue") && propertyJson.containsKey("maxValue")) {
						m.addType(propertyIRI, PropertyValue);
						m.addDataAssertion(propertyIRI, minValue, Float.valueOf(propertyJson.getString("minValue")));
						m.addDataAssertion(propertyIRI, maxValue, Float.valueOf(propertyJson.getString("maxValue")));
					} else {
						throw new RuntimeException(
								"Expected value or min/max value [propertyJson = " + propertyJson + "]");
					}

					m.addObjectAssertion(propertyIRI, unitCode, IRI.create(propertyUnitCode));
				}
			}
		}

	}

	public static void main(String[] args)
			throws OWLOntologyCreationException, OWLOntologyStorageException, FileNotFoundException {
		CreateEsonetYellowPages app = new CreateEsonetYellowPages();
		app.run();
	}

}
