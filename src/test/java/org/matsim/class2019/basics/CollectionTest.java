package org.matsim.class2019.basics;

import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Force;

import org.junit.Test;

public class CollectionTest {

		@Test
		public void listExample() {
			List<Rectangle> myList = new ArrayList<>();
			myList.add(new Rectangle(10, 20));
			myList.add(new Rectangle(10, 200));
			myList.add(new Rectangle(100, 20));
			myList.add(new Rectangle(100, 200));
			myList.add(new Rectangle(1, 2));
			myList.add(new Rectangle(1, 20));
		
		
			for(Rectangle rect : myList) {
				System.out.println("The area is " + rect.calculateArea());
			}
		}
}
