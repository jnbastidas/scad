package com.s4n.scan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.s4n.model.DeliveriesFile;
import com.s4n.model.Delivery;
import com.s4n.model.Drone;
import com.s4n.model.Movement;
import com.s4n.scad.service.DroneService;
import com.s4n.scad.service.impl.DefaultDroneServiceImpl;
import com.s4n.utils.ProjectProperties;
import com.s4n.utils.ServiceLocator;

public class AppTest {
	private static Integer MAX_DRONES = 20;
	private BlockingQueue<Drone> availableDrones = new LinkedBlockingQueue<>(MAX_DRONES);
	
	private ProjectProperties projectProperties = ProjectProperties.getInstance();
	private DroneService droneService = ServiceLocator.getDroneService(DefaultDroneServiceImpl.SERVICE_NAME);
	
	@Before
	public void initApp() {
		this.createDronesQueue();
	}
	
	@Test
	public void testDroneShippedOk() throws InterruptedException, IOException {
		DeliveriesFile deliveriesFile = new DeliveriesFile();
		deliveriesFile.setName("in01.txt");
		
		List<Delivery> deliveries =  new ArrayList<Delivery>();
		deliveries.add(createDelivery(1L, "AAAAIAA"));
		deliveries.add(createDelivery(2L, "DDDAIAD"));
		deliveries.add(createDelivery(3L, "AAIADAD"));
		deliveriesFile.setDeliveries(deliveries);
		
		String actual = this.processDeliveriesFile(deliveriesFile);
		
		Assert.assertEquals("Drone shipped Correctly.", actual);
	}
	
	@Test
	public void testInvalidDeliveriesPerDrone() throws InterruptedException, IOException {
		Integer maxDeliveriesPerDrone = this.projectProperties.getMaxDeliveriesPerDrone();
		
		DeliveriesFile deliveriesFile = new DeliveriesFile();
		deliveriesFile.setName("in02.txt");
		
		List<Delivery> deliveries =  new ArrayList<Delivery>();
		deliveries.add(createDelivery(1L, "AAAAIAA"));
		deliveries.add(createDelivery(2L, "DDDAIAD"));
		deliveries.add(createDelivery(3L, "AAIADAD"));
		deliveries.add(createDelivery(4L, "AAIADAD"));
		deliveriesFile.setDeliveries(deliveries);
		
		String actual = this.processDeliveriesFile(deliveriesFile);
		
		String expected = "Cannot be over "+maxDeliveriesPerDrone+" deliveries per drone.";
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testProcessSeveralFiles() throws InterruptedException {
		List<DeliveriesFile> deliveriesFileList = this.generateDeliveriesFileList(3, 15);
		
		deliveriesFileList.forEach(deliveriesFile -> {
			String actual = this.processDeliveriesFile(deliveriesFile);;
			Assert.assertEquals("Drone shipped Correctly.", actual);
		});
	}
	
	public String processDeliveriesFile(DeliveriesFile deliveriesFile) {
		String fileOutName = deliveriesFile.getName().replace("in", "out");
		
		if (!this.createFile(fileOutName)) {
			return "Error creating txt file.";
		}
		
		Drone drone = null;
		try {
			drone = this.availableDrones.take();
		} catch (InterruptedException e) {
			return e.getMessage();
		}
		
		drone.setDeliveries(deliveriesFile.getDeliveries());
		
		try {
			this.droneService.startDeliveries(drone, fileOutName);
		} catch (RuntimeException e) {
			drone.setDeliveries(null);
			this.availableDrones.add(drone);
			return e.getMessage();
		}
		
		return "Drone shipped Correctly.";
	}
	
	private boolean createFile(String fileOutName) {
		try {
			File myObj = new File("src/main/resources/" + fileOutName);
			return myObj.createNewFile();
		} catch (IOException e) {
			return false;
		}
	}
	
	private void createDronesQueue() {
		IntStream.range(0, MAX_DRONES).forEach(idx -> {
			try {
				Drone drone = new Drone();
				drone.setId(Long.valueOf(idx));
				drone.setName("Drone"+(idx+1));
				
				this.availableDrones.put(drone);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	  );
	}
	
	private static Delivery createDelivery(Long id, String path) {
		Delivery delivery = new Delivery();
		delivery.setId(id);
		
		Movement[] movements = new Movement[path.length()];
		char[] pathChars = path.toCharArray();
		for (int i = 0; i < pathChars.length; i++) {
			Movement movement = Movement.valueOf(String.valueOf(pathChars[i]));
			movements[i] = movement;
		}
		
		delivery.setPath(movements);
		
		return delivery;
	}
	
	private List<DeliveriesFile> generateDeliveriesFileList(Integer from, Integer to) {
		List<DeliveriesFile> deliveriesFileList = new ArrayList<DeliveriesFile>();
		
		for (int i = from; i <= to; i++) {
			DeliveriesFile deliveriesFile = new DeliveriesFile();
			deliveriesFile.setId(Long.valueOf(i));
			deliveriesFile.setName("in"+String.format("%02d" , i)+".txt");
			deliveriesFile.setDeliveries(this.generateDeliveries(this.projectProperties.getMaxDeliveriesPerDrone(), i));
			deliveriesFileList.add(deliveriesFile);
		}
		
		return deliveriesFileList;
	}
	
	private List<Delivery> generateDeliveries(Integer totalDeliveries, Integer fileId) {
		List<Delivery> deliveries =  new ArrayList<Delivery>(totalDeliveries);
		
		for (int i = 0; i < totalDeliveries; i++) {
			deliveries.add(createDelivery( Long.valueOf(fileId + "" + i) , randomAlpha(7)));
		}
		
		return deliveries;
	}
	
	private static String randomAlpha(int count) {
		String posiblesCharacteres = "ADI";
		
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int)(Math.random()*posiblesCharacteres.length());
			builder.append(posiblesCharacteres.charAt(character));
		}
		
		return builder.toString();
	}
}
