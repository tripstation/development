package com.floow.insurance.service;

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

@Service
public class DriverService {
	
	private List<Driver> driverList = new ArrayList<Driver>();
	private static final Logger logger = LoggerFactory.getLogger(DriverService.class);
	private static final String  DRIVER_FILE_NAME  =  "insurance.csv";
	
	public Driver saveDriver(Driver newDriver) {
		List<Driver> existingDrivers = getDrivers();
		Long sequenceId = generateNextSequenceId(existingDrivers);
		newDriver.setId(sequenceId);
		newDriver.setCreationDate(LocalDate.now());
		writeDriverToDisk(existingDrivers, newDriver);
		return newDriver;
	}
	
	public List<Driver> getDrivers() {
		return loadDriverFromDisk();
	}
	
	public LocalDate stringToDate(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate  date = LocalDate.parse(stringDate,formatter);
		return date;
	}
	
	private  List<Driver> loadDriverFromDisk() {
		
		try  (Stream<String> stream = Files.lines(Paths.get(DRIVER_FILE_NAME))) {
			driverList = stream.map(line -> {
				String [] driverArray = line.split(",");
				Driver driver = new Driver(Long.valueOf(driverArray[0]), driverArray[1], driverArray[2],stringToDate(driverArray[3]),stringToDate(driverArray[4]));
				return driver;
			}).collect(Collectors.toList());
		} catch (IOException e) {
			logger.error("error reading driver file",e);
		}
		return driverList;
	}
	
	private void writeDriverToDisk(List<Driver> drivers, Driver newDriver) {
		try (FileWriter fileWriter = new FileWriter(DRIVER_FILE_NAME,true)) {
		    fileWriter.append(newDriver.getId() + "," 
		    					+ newDriver.getFirstName() + ","
		    					+ newDriver.getLastName() + ","
		    					+ newDriver.getDateOfBirth() + ","
		    					+ newDriver.getCreationDate() + "\n"
		    );
		} catch (IOException e) {
			logger.error("error writing to file",e);
			e.printStackTrace();
		}
	}
	
	private long generateNextSequenceId(List<Driver> drivers) {
		List<Long> idList = drivers.stream()
				.map(d -> d.getId())
				.collect(Collectors.toList());
		long sequenceId = idList.isEmpty() ? 0: idList.get(idList.size() -1 );
		++ sequenceId;
		return sequenceId;
	}

	public List<Driver> GetAllDriversAfterGivenDate(String beforeDate) {
		List<Driver> driversByAge = getDrivers().stream()
				.filter(d -> d.getDateOfBirth().isAfter(stringToDate(beforeDate)))
				.collect(Collectors.toList());
		return driversByAge;
	}
}