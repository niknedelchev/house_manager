package com.housemanager.nn.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.housemanager.nn.model.Fee;

public class FeeService {

	// 10. Received fees are saved to a file
	public static void saveReceivedFeeToFile(Fee fee) {

		String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\receipts\\";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + "received_fee_" + fee.getId() + ".txt"))) {
			bw.write("Service company: " + fee.getEmployee().getServiceCompany().getName() + "\n");
			bw.write("Employee: " + fee.getEmployee().getName() + "\n");
			bw.write("Building: " + fee.getApartment().getBuilding().getId() + ", address: "
					+ fee.getApartment().getBuilding().getAddress() + "\n");
			bw.write("Apartment: " + fee.getApartment().getId() + ", floor: " + fee.getApartment().getFloor() + "\n");
			bw.write("Received amount: " + fee.getAmountReceived().toString() + "\n");
			bw.write("Date received: " + fee.getPaymentDate().toString());

		} catch (IOException ex) {
			System.err.println("thrown exception: " + ex.toString());
		}

	}
}