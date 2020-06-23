package com.s4n.scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.Test;

import com.s4n.common.model.Delivery;
import com.s4n.common.model.Drone;
import com.s4n.common.model.Movement;
import com.s4n.scad.model.DeliveriesFile;
import com.s4n.scad.service.ScadService;
import com.s4n.scad.service.impl.DefaultScadServiceImpl;
import com.s4n.scad.utils.ServiceLocator;

public class AppTest {
	private ScadService scadService = ServiceLocator.getScadService(DefaultScadServiceImpl.SERVICE_NAME);
	
	@BeforeClass
	public static void initApp() {
		BlockingQueue<Drone> availableDronesQueue = ServiceLocator.getAvailableDronesQueue();
		IntStream.range(0, availableDronesQueue.remainingCapacity()).forEach(idx -> {
			try {
				Drone drone = new Drone();
				drone.setId(Long.valueOf(idx));
				drone.setName("#"+(idx+1));
				
				availableDronesQueue.put(drone);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	  );
	}
	
	
	
	//Scenario test document
	@Test
	public void testDroneShippedOk() throws InterruptedException, IOException {
		DeliveriesFile deliveriesFile = new DeliveriesFile();
		deliveriesFile.setName("in01.txt");
		
		List<Delivery> deliveries =  new ArrayList<Delivery>();
		deliveries.add(createDelivery(1L, "AAAAIAA"));
		deliveries.add(createDelivery(2L, "DDDAIAD"));
		deliveries.add(createDelivery(3L, "AAIADAD"));
		deliveriesFile.setDeliveries(deliveries);
		
		this.scadService.processDeliveriesFile(deliveriesFile);
	}
	
	//Scenario test document over 3 deliveries
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidDeliveriesPerDrone() throws InterruptedException, IOException {
		DeliveriesFile deliveriesFile = new DeliveriesFile();
		deliveriesFile.setName("in02.txt");
		
		List<Delivery> deliveries =  new ArrayList<Delivery>();
		deliveries.add(createDelivery(1L, "AAAAIAA"));
		deliveries.add(createDelivery(2L, "DDDAIAD"));
		deliveries.add(createDelivery(3L, "AAIADAD"));
		deliveries.add(createDelivery(4L, "AAIADAD"));
		deliveriesFile.setDeliveries(deliveries);
		
		this.scadService.processDeliveriesFile(deliveriesFile);
	}
	
	//Test severals files
	@Test
	public void testProcessSeveralFiles() throws IOException, InterruptedException {
		List<DeliveriesFile> deliveriesFileList = this.generateDeliveriesFileList(3, 15);
		
		for (DeliveriesFile deliveriesFile : deliveriesFileList) {
			this.scadService.processDeliveriesFile(deliveriesFile);
		}
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
			deliveriesFile.setDeliveries(this.generateDeliveries(3, i));
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
