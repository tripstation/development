package com.floow.insurance.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.floow.insurance.model.Driver;

//"D:\\development\\csv\\insurance.csv"
@Service
public class DriverService {
	
	private List<Driver> driverList = new ArrayList<Driver>();
	private static final Logger logger = LoggerFactory.getLogger(DriverService.class);
	private static final String driverFileName =  "D:\\development\\csv\\insurance2.csv";
//	private static final String driverFileName =  "insurance3.csv";
	
	public Driver saveDriver(Driver newDriver) {
		logger.debug("new driver passed in = " + newDriver);
		List<Driver> existingDrivers = getDrivers();
		logger.debug("existing drivers = " + existingDrivers);
		Long sequenceId = generateNextSequenceId(existingDrivers);
		logger.debug("sequence in service = " + sequenceId);
		newDriver.setId(sequenceId);
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate creationDate = LocalDate.now();
		newDriver.setCreationDate(creationDate);
		writeDriverToDisk(existingDrivers, newDriver);
		
		return newDriver;
	}
	public List<Driver> getDrivers() {
		return loadDriverFromDisk();
	}
	
	public  List<Driver> loadDriverFromDisk() {
		logger.debug("loadDriverFromDisk()");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		/////
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(driverFileName).getFile());
		////
		try  (Stream<String> stream = Files.lines(Paths.get(driverFileName))) {
			driverList = stream.map(line -> {
				String [] driverArray = line.split(",");
				LocalDate  dateOfBirth = LocalDate.parse(driverArray[3], formatter);
				LocalDate  creationDate = LocalDate.parse(driverArray[4], formatter);
				Driver driver = new Driver(Long.valueOf(driverArray[0]), driverArray[1], driverArray[2],dateOfBirth,creationDate);
				return driver;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			logger.debug("error reading driver file");
			System.out.println(e.fillInStackTrace());
		}
		return driverList;
	}
	
	public void writeDriverToDisk(List<Driver> drivers, Driver newDriver) {
		
		try (FileWriter fileWriter = new FileWriter(driverFileName,true)) {
			
		    fileWriter.append(newDriver.getId() + "," 
		    					+ newDriver.getFirstName() + ","
		    					+ newDriver.getLastName() + ","
		    					+ newDriver.getDateOfBirth() + ","
		    					+ newDriver.getCreationDate() + "\n"
		    );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long generateNextSequenceId(List<Driver> drivers) {
		List<Long> idList = drivers.stream()
				.map(d -> d.getId())
				.collect(Collectors.toList());
		System.out.println("sequence from file = " + idList);
		long sequenceId = idList.isEmpty() ? 0: idList.get(idList.size() -1 );
		System.out.println("sequence id = " + sequenceId);
		++ sequenceId;
		System.out.println("next sequence id = " + sequenceId);
		return sequenceId;
	}

	public List<Driver> GetAllDriversAfterGivenDate(String date, List<Driver> drivers) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate beforeDate = LocalDate.parse(date,formatter);
		
		List<Driver> driversByAge = drivers.stream()
				.filter(d -> d.getDateOfBirth().isAfter(beforeDate))
				.collect(Collectors.toList());
		return driversByAge;
	}
}
