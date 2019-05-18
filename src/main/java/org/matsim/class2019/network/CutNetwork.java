package org.matsim.class2019.network;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;


public class CutNetwork {
	public static void main(String[] args) {

		Path inputNetwork= Paths.get(args[0]);
		Path outputNetwork= Paths.get(args[1]);
		Network network = NetworkUtils.createNetwork();
			
		new MatsimNetworkReader(network).readFile(inputNetwork.toString());
		network.getLinks().get(Id.createLinkId("16578")).setCapacity(0);
		network.getLinks().get(Id.createLinkId("16572")).setCapacity(0);

		//network.getLinks().get(Id.createLinkId("16584"));
		
		new NetworkWriter(network).write(outputNetwork.toString());
	}
}
