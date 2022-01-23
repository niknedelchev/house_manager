package com.housemanager.nn.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.housemanager.nn.HouseManagerApplication;
import com.housemanager.nn.model.Apartment;
import com.housemanager.nn.model.Building;
import com.housemanager.nn.model.Employee;
import com.housemanager.nn.model.Occupant;
import com.housemanager.nn.model.Owner;
import com.housemanager.nn.model.ServiceCompany;
import com.housemanager.nn.repository.ApartmentRepository;
import com.housemanager.nn.repository.BuildingRepository;
import com.housemanager.nn.repository.EmployeeRepository;
import com.housemanager.nn.repository.FeeRepository;
import com.housemanager.nn.repository.OccupantRepository;
import com.housemanager.nn.repository.OwnerRepository;
import com.housemanager.nn.repository.ServiceCompanyRepository;

@Service
public class Initializer {
	@Autowired
	FeeRepository feeRepository;
	@Autowired
	ApartmentRepository apartmentRepository;
	@Autowired
	BuildingRepository buildingRepository;
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	OccupantRepository occupantRepository;
	@Autowired
	OwnerRepository ownerRepository;
	@Autowired
	ServiceCompanyRepository serviceCompanyRepository;
	
	Logger logger = LoggerFactory.getLogger(HouseManagerApplication.class);
	
	@EventListener
	public void eventListener(ApplicationReadyEvent e) {
		logger.info("Logger info channel: House manager app starts");
		logger.info("Data initialiation starts.");

		List<ServiceCompany> companies = new ArrayList<>();
		companies.add(new ServiceCompany("Company A",null)); 
		companies.add(new ServiceCompany("Company B",null));
		companies.add(new ServiceCompany("Company C",null));
		companies.add(new ServiceCompany("Company D",null));
		
		
		serviceCompanyRepository.saveAllAndFlush(companies);
		
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee("Angel Angelov", serviceCompanyRepository.findById(1).get(),null,null));
		employees.add(new Employee("Blago Blagoev", serviceCompanyRepository.findById(1).get(),null,null));
		employees.add(new Employee("Ceco Cecev", serviceCompanyRepository.findById(1).get(),null,null));
		employees.add(new Employee("Dani Danailov", serviceCompanyRepository.findById(1).get(),null,null));
		employees.add(new Employee("Emil Emilov", serviceCompanyRepository.findById(2).get(),null,null));
		employees.add(new Employee("Fifi Fifov", serviceCompanyRepository.findById(2).get(),null,null));
		employees.add(new Employee("George Georgiev", serviceCompanyRepository.findById(2).get(),null,null));
		employees.add(new Employee("Hristo Hristov", serviceCompanyRepository.findById(2).get(),null,null));
		employees.add(new Employee("Ivan Iordanov", serviceCompanyRepository.findById(3).get(),null,null));
		employees.add(new Employee("Kiril Kirilov", serviceCompanyRepository.findById(3).get(),null,null));
		employees.add(new Employee("Momchil Momchilov", serviceCompanyRepository.findById(3).get(),null,null));
		employees.add(new Employee("Niki Nikolov", serviceCompanyRepository.findById(3).get(),null,null));
		employees.add(new Employee("Ogi Ognianov", serviceCompanyRepository.findById(4).get(),null,null));
		employees.add(new Employee("Petar Petrov", serviceCompanyRepository.findById(4).get(),null,null));
		employees.add(new Employee("Rumen Rumenov", serviceCompanyRepository.findById(4).get(),null,null));
		employees.add(new Employee("Sasho Sashev", serviceCompanyRepository.findById(4).get(),null,null));
		
		employeeRepository.saveAllAndFlush(employees);
		
		Building build1 = new Building("Sofia", 4,300,null,employeeRepository.findById(1).get(),300,0);
		Building build2 = new Building("Plovdiv", 4,200,null,employeeRepository.findById(2).get(),200,0);
		Building build3 = new Building("Varna", 4,250,null,employeeRepository.findById(5).get(),250,0);
		Building build4 = new Building("Burgas", 4,150,null,employeeRepository.findById(9).get(),150,0);
		Building build5 = new Building("Pazardzhik", 3,100,null,employeeRepository.findById(13).get(),100,0);
		
		buildingRepository.saveAndFlush(build1);
		buildingRepository.saveAndFlush(build2);
		buildingRepository.saveAndFlush(build3);
		buildingRepository.saveAndFlush(build4);
		buildingRepository.saveAndFlush(build5);
		
		//Building 1: 12 apartments in total
		List<Owner> ownersBuld1 = new ArrayList<>();
		
		for (int i=0; i<12; i++) {
			ownersBuld1.add(new Owner("Bulding 1, owner " + (i+1) , 31 + i, null));
		}
		
		ownerRepository.saveAllAndFlush(ownersBuld1);
		
		List<Apartment> aptsInBulding1 = new ArrayList<>();
		int totalAptAreaBuild1 =0;
		boolean hasPet = false;
		int floor = 1;
		
		for (int i= 0; i<12; i++) {
		
			int aptSize = 92;
			
			totalAptAreaBuild1+=aptSize;
			
			aptsInBulding1.add(new Apartment(floor,aptSize,ownersBuld1.get(i),null,hasPet,build1));
			
			if((i+1) %3 ==0)
				floor+=1;
	
			hasPet = !hasPet;
		}
		
		apartmentRepository.saveAllAndFlush(aptsInBulding1);
		
		build1.setApartmentsAreaSize(totalAptAreaBuild1);
		build1.setTotalAreaSize(build1.getCommonAreaSize()+totalAptAreaBuild1);
		buildingRepository.saveAndFlush(build1);
		
		List<Occupant> ocupantsBuild1 = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+1)+",occupant " + 1, 25+i,aptsInBulding1.get(i)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+1)+",occupant " + 2, 25+i,aptsInBulding1.get(i)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+1)+",occupant " + 3, 25+i,aptsInBulding1.get(i)));
			
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+4+1)+",occupant " + 1, 25+i+3,aptsInBulding1.get(i+4)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+4+1)+",occupant " + 2, 25+i+3,aptsInBulding1.get(i+4)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+4+1)+",occupant " + 3, 25+i+3,aptsInBulding1.get(i+4)));
			
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+8+1)+",occupant " + 1, 25+i+4,aptsInBulding1.get(i+8)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+8+1)+",occupant " + 2, 25+i+4,aptsInBulding1.get(i+8)));
			ocupantsBuild1.add(new Occupant("Build 1, apt "+(i+8+1)+",occupant " + 3, 25+i+4,aptsInBulding1.get(i+8)));
		}
		
		occupantRepository.saveAllAndFlush(ocupantsBuild1);
		//End Building 1
		
		//Building 2: 12 apartments in total
		List<Owner> ownersBuld2 = new ArrayList<>();
		
		for (int i=0; i<12; i++) {
			ownersBuld2.add(new Owner("Bulding 2, owner " + (i+1) , 28 + i, null));
		}
		
		ownerRepository.saveAllAndFlush(ownersBuld2);
		
		List<Apartment> aptsInBulding2 = new ArrayList<>();
		int totalAptAreaBuild2 =0;
		floor = 1;
		
		for (int i= 0; i<12; i++) {
			
			int aptSize = 86;
			
			totalAptAreaBuild2+=aptSize;
			
			aptsInBulding2.add(new Apartment(floor,aptSize,ownersBuld2.get(i),null,hasPet,build2));
		
			if(floor %3 ==0)
				floor+=1;
		
			hasPet = !hasPet;
		}
		
		apartmentRepository.saveAllAndFlush(aptsInBulding2);
		
		build2.setApartmentsAreaSize(totalAptAreaBuild2);
		build2.setTotalAreaSize(build2.getCommonAreaSize()+totalAptAreaBuild2);
		buildingRepository.saveAndFlush(build2);
		
		List<Occupant> ocupantsBuild2 = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+1)+",occupant " + 1, 26+i,aptsInBulding2.get(i)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+1)+",occupant " + 2, 26+i,aptsInBulding2.get(i)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+1)+",occupant " + 3, 26+i,aptsInBulding2.get(i)));
			
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+4+1)+",occupant " + 1, 27+i+3,aptsInBulding2.get(i+4)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+4+1)+",occupant " + 2, 27+i+3,aptsInBulding2.get(i+4)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+4+1)+",occupant " + 3, 27+i+3,aptsInBulding2.get(i+4)));
			
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+8+1)+",occupant " + 1, 31+i+4,aptsInBulding2.get(i+8)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+8+1)+",occupant " + 2, 31+i+4,aptsInBulding2.get(i+8)));
			ocupantsBuild2.add(new Occupant("Build 2, apt "+(i+8+1)+",occupant " + 3, 31+i+4,aptsInBulding2.get(i+8)));
		}
		
		occupantRepository.saveAllAndFlush(ocupantsBuild2);
		//End Building 2
//		
//		
//		//Building 3: 12 apartments in total
		List<Owner> ownersBuld3 = new ArrayList<>();
		
		for (int i=0; i<12; i++) {
			ownersBuld3.add(new Owner("Bulding 3, owner " + (i+1) , 27 + i, null));
		}
		
		ownerRepository.saveAllAndFlush(ownersBuld3);
		
		List<Apartment> aptsInBulding3 = new ArrayList<>();
		int totalAptAreaBuild3 =0;
		floor = 1;
		
		for (int i= 0; i<12; i++) {
			
			int aptSize = 82;
			
			totalAptAreaBuild3+=aptSize;
			
			aptsInBulding3.add(new Apartment(floor,aptSize,ownersBuld3.get(i),null,hasPet,build3));
			
			if(floor %3 ==0)
				floor+=1;
			
			hasPet = !hasPet;
		}
		
		apartmentRepository.saveAllAndFlush(aptsInBulding3);
		
		build3.setApartmentsAreaSize(totalAptAreaBuild3);
		build3.setTotalAreaSize(build3.getCommonAreaSize()+totalAptAreaBuild3);
		buildingRepository.saveAndFlush(build3);
		
		List<Occupant> ocupantsBuild3 = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+1)+",occupant " + 1, 28+i,aptsInBulding3.get(i)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+1)+",occupant " + 2, 28+i,aptsInBulding3.get(i)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+1)+",occupant " + 3, 29+i,aptsInBulding3.get(i)));
			
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+4+1)+",occupant " + 1, 26+i+3,aptsInBulding3.get(i+4)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+4+1)+",occupant " + 2, 27+i+3,aptsInBulding3.get(i+4)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+4+1)+",occupant " + 3, 31+i+3,aptsInBulding3.get(i+4)));
			
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+8+1)+",occupant " + 1, 38+i+4,aptsInBulding3.get(i+8)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+8+1)+",occupant " + 2, 32+i+4,aptsInBulding3.get(i+8)));
			ocupantsBuild3.add(new Occupant("Build 3, apt "+(i+8+1)+",occupant " + 3, 31+i+4,aptsInBulding3.get(i+8)));
		}
		
		occupantRepository.saveAllAndFlush(ocupantsBuild3);
		//End Building 3

		//Building 4: 12 apartments in total
		List<Owner> ownersBuld4 = new ArrayList<>();
		
		for (int i=0; i<12; i++) {
			ownersBuld4.add(new Owner("Bulding 4, owner " + (i+1) , 31 + i, null));
		}
		
		ownerRepository.saveAllAndFlush(ownersBuld4);
		
		List<Apartment> aptsInBulding4 = new ArrayList<>();
		int totalAptAreaBuild4 =0;
		floor = 1;
			
		for (int i= 0; i<12; i++) {
			
			int aptSize = 78;
			
			totalAptAreaBuild4+=aptSize;
			
			aptsInBulding4.add(new Apartment(floor,aptSize,ownersBuld4.get(i),null,hasPet,build4));
			
			if(floor %3 ==0)
				floor+=1;
			
			hasPet = !hasPet;
		}
		
		apartmentRepository.saveAllAndFlush(aptsInBulding4);
		
		build4.setApartmentsAreaSize(totalAptAreaBuild4);
		build4.setTotalAreaSize(build4.getCommonAreaSize()+totalAptAreaBuild4);
		buildingRepository.saveAndFlush(build4);
		
		List<Occupant> ocupantsBuild4 = new ArrayList<>();
		
		for (int i=0; i<4; i++) {
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+1)+",occupant " + 1, 28+i,aptsInBulding4.get(i)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+1)+",occupant " + 2, 28+i,aptsInBulding4.get(i)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+1)+",occupant " + 3, 29+i,aptsInBulding4.get(i)));
			
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+4+1)+",occupant " + 1, 26+i+3,aptsInBulding4.get(i+4)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+4+1)+",occupant " + 2, 27+i+3,aptsInBulding4.get(i+4)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+4+1)+",occupant " + 3, 31+i+3,aptsInBulding4.get(i+4)));
			
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+8+1)+",occupant " + 1, 38+i+4,aptsInBulding4.get(i+8)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+8+1)+",occupant " + 2, 32+i+4,aptsInBulding4.get(i+8)));
			ocupantsBuild4.add(new Occupant("Build 4, apt "+(i+8+1)+",occupant " + 3, 31+i+4,aptsInBulding4.get(i+8)));
		}
		
		occupantRepository.saveAllAndFlush(ocupantsBuild4);
		//End Building 4
	
		//Building 5: 9 apartments in total
		List<Owner> ownersBuld5 = new ArrayList<>();
		
		for (int i=0; i<9; i++) {
			ownersBuld5.add(new Owner("Bulding 4, owner " + (i+1) , 37 + i, null));
		}
		
		ownerRepository.saveAllAndFlush(ownersBuld5);
		
		List<Apartment> aptsInBulding5 = new ArrayList<>();
		int totalAptAreaBuild5 =0;
		floor = 1;
		
		for (int i= 0; i<9; i++) {
			
			int aptSize = 63;
			
			totalAptAreaBuild5+=aptSize;
			
			aptsInBulding5.add(new Apartment(floor,aptSize,ownersBuld5.get(i),null,hasPet,build5));
			
			if(floor %3 ==0)
				floor+=1;

			hasPet = !hasPet;
		}
		
		apartmentRepository.saveAllAndFlush(aptsInBulding5);
		
		build5.setApartmentsAreaSize(totalAptAreaBuild5);
		build5.setTotalAreaSize(build4.getCommonAreaSize()+totalAptAreaBuild5);
		buildingRepository.saveAndFlush(build5);
		
		List<Occupant> ocupantsBuild5 = new ArrayList<>();
		
		for (int i=0; i<3; i++) {
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+1)+",occupant " + 1, 32+i,aptsInBulding5.get(i)));
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+1)+",occupant " + 2, 34+i,aptsInBulding5.get(i)));
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+1)+",occupant " + 3, 29+i,aptsInBulding5.get(i)));
			
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+4+1)+",occupant " + 1, 42+i+3,aptsInBulding5.get(i+4)));
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+4+1)+",occupant " + 2, 35+i+3,aptsInBulding5.get(i+4)));
			ocupantsBuild5.add(new Occupant("Build 5, apt "+(i+4+1)+",occupant " + 3, 31+i+3,aptsInBulding5.get(i+4)));
			
		}
		
		occupantRepository.saveAllAndFlush(ocupantsBuild4);
		//End Building 5
		
		logger.info("Data initialization ends.");

	}
	

}
