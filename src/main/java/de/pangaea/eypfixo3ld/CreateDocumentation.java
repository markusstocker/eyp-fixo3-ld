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

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import de.pangaea.eypfixo3ld.vocab.EYP;
import de.pangaea.eypfixo3ld.vocab.FIXO3;
import de.pangaea.eypfixo3ld.vocab.GeoSPARQL;
import de.pangaea.eypfixo3ld.vocab.SOSA;
import de.pangaea.eypfixo3ld.vocab.SSN;
import de.pangaea.eypfixo3ld.vocab.SSNSystem;
import de.pangaea.eypfixo3ld.vocab.Schema;

/**
 * <p>
 * Title:
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

public class CreateDocumentation {

	private final String base = "file:///home/ms/workspace-sensdatatran/eyp-fixo3-ld/src/main/resources/rdf/";

	private final IRI schemaIRI = IRI.create(base + "schema.rdf");
	// private final IRI geosparqlIRI = IRI.create(base +
	// "geosparql_vocab_all.rdf");
	private final IRI sosaIRI = IRI.create(base + "sosa.rdf");
	private final IRI ssnIRI = IRI.create(base + "ssn.rdf");
	private final IRI ssnSystemIRI = IRI.create(base + "ssn-system.rdf");
	private final IRI eypIRI = IRI.create(base + "esonetyellowpages.rdf");
	private final IRI fixo3IRI = IRI.create(base + "fixo3.rdf");
	// private final IRI unitIRI = IRI.create(base + "unit.rdf"); // Includes
	// all units in documentation
	private final IRI mergedIRI = IRI.create("http://fixo3.eu/vocab/doc/");

	private final String fixo3DocFile = "src/main/resources/rdf/fixo3-doc.rdf";

	private void run() throws OWLOntologyCreationException, OWLOntologyStorageException {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		man.addIRIMapper(new SimpleIRIMapper(Schema.ns, schemaIRI));
		// man.addIRIMapper(new SimpleIRIMapper(GeoSPARQL.ns, geosparqlIRI));
		man.addIRIMapper(new SimpleIRIMapper(SOSA.ns, sosaIRI));
		man.addIRIMapper(new SimpleIRIMapper(SSN.ns, ssnIRI));
		man.addIRIMapper(new SimpleIRIMapper(SSNSystem.ns, ssnSystemIRI));
		man.addIRIMapper(new SimpleIRIMapper(EYP.ns, eypIRI));
		man.addIRIMapper(new SimpleIRIMapper(FIXO3.ns, fixo3IRI));
		// man.addIRIMapper(new
		// SimpleIRIMapper(IRI.create("http://qudt.org/vocab/unit#"), unitIRI));

		man.loadOntology(schemaIRI);
		// man.loadOntology(geosparqlIRI);
		man.loadOntology(sosaIRI);
		man.loadOntology(ssnIRI);
		man.loadOntology(ssnSystemIRI);
		man.loadOntology(eypIRI);
		man.loadOntology(fixo3IRI);
		// man.loadOntology(unitIRI);

		OWLOntologyMerger merger = new OWLOntologyMerger(man);
		OWLOntology ret = merger.createMergedOntology(man, mergedIRI);

		man.saveOntology(ret, new FileDocumentTarget(new File(fixo3DocFile)));
	}

	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		CreateDocumentation app = new CreateDocumentation();
		app.run();
	}

}
