package com.transformuk.hee.tis.reference.ditool;

import com.transformuk.hee.tis.reference.model.Site;
import com.transformuk.hee.tis.reference.model.Trust;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a data import tool used to generate SQL code. It is not part of the runtime code of the project.
 * This tool parses a sites and trusts CSV file obtained from exporting an excel sheet and spits out the SQL
 * code necessary to populate our database.
 * To use just run the main method of this class independently
 */
public class SitesAndTrusts {

	public static void main(String[] args) throws IOException {
		sitesAndTrustsCSVToSQL();
	}

	private static void sitesAndTrustsCSVToSQL() throws IOException {
		Reader in = new FileReader("ODS Data Trusts and Sites 2015.04.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		Set<Trust> trusts = new LinkedHashSet<>();
		List<Site> sites = new ArrayList<>();
		for (CSVRecord record : records) {
			String trustCode = record.get("Trust Code Ods");
			String trustName = record.get("Trust Name Ods");
			trusts.add(new Trust(trustCode, trustName));
			String siteCode = record.get("Site Code Ods");
			String siteName = record.get("Site Name Ods");
			String address = record.get("Address Line 1").concat(" ").
					concat(record.get("Address Line 2")).concat(" ").
					concat(record.get("Address Line 3")).concat(" ").replace("  "," ").
					concat(record.get("Address Line 4")).concat(" ").
					concat(record.get("Address Line 5")).replace("  "," ");
			String postCode = record.get("Postcode");
			sites.add(new Site(siteCode, trustCode, siteName, address, postCode));
		}
		spitTrustsSql(trusts);
		spitSitesSql(sites);
	}

	private static void spitTrustsSql(Set<Trust> trusts) {
		System.out.println("INSERT INTO Trust (code,name) VALUES");
		trusts.stream().forEach(t -> System.out.println(
				"('" + t.getCode() + "', '" + t.getName().replace("'", "''") + "'),"));
	}

	private static void spitSitesSql(List<Site> sites) throws IOException {
		//outputting to file because there are 25k rows in there
		Path path = Paths.get("sites.sql");
		Files.deleteIfExists(path);
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write("INSERT INTO Site (siteCode,trustCode,siteName,address,postCode) VALUES\n");
			sites.stream().forEach(s -> {
				try {
					writer.write(
							"('" + s.getSiteCode() + "', '" + s.getTrustCode() + "','" + s.getSiteName().replace("'", "''") +
									"','" + s.getAddress().replace("'", "''") + "','" + s.getPostCode() + "'),\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}
}
