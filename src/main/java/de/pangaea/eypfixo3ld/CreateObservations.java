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

package de.pangaea.eypfixo3ld;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import de.pangaea.eypfixo3ld.vocab.FIXO3;
import de.pangaea.eypfixo3ld.vocab.SOSA;
import de.pangaea.eypfixo3ld.vocab.SSN;
import de.pangaea.eypfixo3ld.vocab.Schema;
import de.pangaea.eypfixo3ld.vocab.Time;

/**
 * <p>
 * Title: CreateObservations
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

public class CreateObservations {

	RDFDatatype unitCodeDatatype = new CustomDatatype("http://w3id.org/lindt/custom_datatypes#ucum");

	String csvFile = "src/main/resources/config/e2-m3a-adcp-horizontal-current-speed-monthly.csv";
	DateTime skipBefore = new DateTime(2011, 5, 1, 0, 0, 0);
	Double skipDepth = Double.valueOf(253.0);
	Resource sensorId = ResourceFactory.createResource("http://fixo3.eu/vocab/0195d");
	Resource propertyId = ResourceFactory.createResource("http://esonetyellowpages.com/vocab/c6563");
	Resource featureId = ResourceFactory.createResource("http://esonetyellowpages.com/vocab/f9211");
	Resource stimulusId = ResourceFactory.createResource("http://esonetyellowpages.com/vocab/6fd42");
	Resource unitCodeId = ResourceFactory.createResource("http://qudt.org/vocab/unit#MeterPerSecond");
	String unitCodeSymbol = "m s^-1";
	String rdfFile = "src/main/resources/rdf/e2-m3a-adcp-observations.rdf";

	// String csvFile = "src/main/resources/config/pap-pco2.csv";
	// DateTime skipBefore = new DateTime(2010, 1, 1, 0, 0, 0);
	// Double skipDepth = Double.valueOf(0.0);
	// Resource sensorId =
	// ResourceFactory.createResource("http://fixo3.eu/vocab/45767");
	// Resource propertyId =
	// ResourceFactory.createResource("http://esonetyellowpages.com/vocab/2dfc4");
	// Resource featureId =
	// ResourceFactory.createResource("http://esonetyellowpages.com/vocab/541de");
	// Resource stimulusId =
	// ResourceFactory.createResource("http://esonetyellowpages.com/vocab/746b9");
	// Resource unitCodeId =
	// ResourceFactory.createResource("http://qudt.org/vocab/unit#PartsPerMillion");
	// String unitCodeSymbol = "ppm";
	// String rdfFile = "src/main/resources/rdf/pap-pco2-observations.rdf";

	private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

	private void run() throws IOException {
		BufferedReader br = null;
		String line;

		Model m = ModelFactory.createDefaultModel();

		Resource Observation = ResourceFactory.createResource(SOSA.Observation.toString());
		Resource QuantitativeValue = ResourceFactory.createResource(Schema.QuantitativeValue.toString());
		Resource Instant = ResourceFactory.createResource(Time.Instant.toString());

		Property isObservedBy = ResourceFactory.createProperty(SOSA.isObservedBy.toString());
		Property observedProperty = ResourceFactory.createProperty(SOSA.observedProperty.toString());
		Property hasFeatureOfInterest = ResourceFactory.createProperty(SOSA.hasFeatureOfInterest.toString());
		Property wasOriginatedBy = ResourceFactory.createProperty(SSN.wasOriginatedBy.toString());
		Property hasResult = ResourceFactory.createProperty(SOSA.hasResult.toString());
		Property hasSimpleResult = ResourceFactory.createProperty(SOSA.hasSimpleResult.toString());
		Property value = ResourceFactory.createProperty(Schema.value.toString());
		Property resultTime = ResourceFactory.createProperty(SOSA.resultTime.toString());
		Property unitCode = ResourceFactory.createProperty(Schema.unitCode.toString());
		Property inXSDDateTime = ResourceFactory.createProperty(Time.inXSDDateTime.toString());

		try {
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",");

				DateTime time = dtf.parseDateTime(row[0]);

				if (time.isBefore(skipBefore))
					continue;

				Double depth = Double.valueOf(row[1]);

				if (depth.equals(skipDepth))
					continue;

				Double resultValue = Double.valueOf(row[2]);
				String resultValueWithUnit = resultValue + " " + unitCodeSymbol;

				Resource observationId = ResourceFactory.createResource(FIXO3.ns + UUID.randomUUID().toString());
				Resource timeId = ResourceFactory.createResource(FIXO3.ns + UUID.randomUUID().toString());
				Resource resultId = ResourceFactory.createResource(FIXO3.ns + UUID.randomUUID().toString());

				m.add(observationId, RDF.type, Observation);
				m.add(observationId, isObservedBy, sensorId);
				m.add(observationId, observedProperty, propertyId);
				m.add(observationId, hasFeatureOfInterest, featureId);
				m.add(observationId, wasOriginatedBy, stimulusId);
				m.add(observationId, hasResult, resultId);
				m.add(observationId, hasSimpleResult,
						ResourceFactory.createTypedLiteral(resultValueWithUnit, unitCodeDatatype));
				m.add(resultId, RDF.type, QuantitativeValue);
				m.add(resultId, value,
						ResourceFactory.createTypedLiteral(resultValue.toString(), XSDDatatype.XSDdouble));
				m.add(resultId, unitCode, unitCodeId);
				m.add(observationId, resultTime, timeId);
				m.add(timeId, RDF.type, Instant);
				m.add(timeId, inXSDDateTime, ResourceFactory.createTypedLiteral(
						ISODateTimeFormat.dateTime().withOffsetParsed().print(time), XSDDatatype.XSDdateTime));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		m.write(new FileWriter(new File(rdfFile)));
	}

	public static void main(String[] args) throws IOException {
		CreateObservations app = new CreateObservations();
		app.run();
	}

	private class CustomDatatype extends BaseDatatype {

		public CustomDatatype(String uri) {
			super(uri);
		}

	}
}
