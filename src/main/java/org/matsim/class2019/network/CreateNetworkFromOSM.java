package org.matsim.class2019.network;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.io.OsmNetworkReader;
import org.locationtech.jts.geom.Geometry;
import org.matsim.api.core.v01.Coord; 
import org.matsim.core.utils.geometry.geotools.MGC;
import org.opengis.feature.simple.SimpleFeature;



public class CreateNetworkFromOSM {

	private static Path inputFile = Paths.get("/home/valentin/MATSim/OSM/thueringen-latest.osm");
	private static String epsg = "EPSG:25832";
	

	private static Path filterShape = Paths.get("/home/valentin/MATSim/Erfurt-shape/erfurt.shp");
	
	public static void main(String[] args) {
		new CreateNetworkFromOSM().create();
	}
	
	public void create() {
		// create empty network
		Network network = NetworkUtils.createNetwork();
		
		CoordinateTransformation transformation = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, epsg);

				
		OsmNetworkReader reader = new OsmNetworkReader(network, transformation, true, true);
		reader.addOsmFilter(new NetworkFilter(filterShape));
	//	reader.addOsmFilter((coord, hierarchy) -> {
	//		return hierarchy <= 4; // 1 == highways
	//	});
		
		reader.parse(inputFile.toString());
		
		new NetworkCleaner().run(network); // no dead ends and islands not connected to the rest
		
		new NetworkWriter(network).write(Paths.get("/home/valentin/MATSim/OSM/erfurt-network.xml.gz").toString());
		
		
	}
	
	private static class NetworkFilter implements OsmNetworkReader.OsmFilter {

		private final Collection<Geometry> geometries = new ArrayList<>();

		NetworkFilter(Path shapeFile) {
			for (SimpleFeature feature : ShapeFileReader.getAllFeatures(shapeFile.toString())) {
				geometries.add((Geometry) feature.getDefaultGeometry());
			}
		}

		@Override
		public boolean coordInFilter(Coord coord, int hierarchyLevel) {
			// hierachy levels 1 - 3 are motorways and primary roads, as well as their trunks
			if (hierarchyLevel <= 4) return true;

			// if coord is within the supplied shape use every street above level of tracks and cycle ways
			return hierarchyLevel <= 8 && containsCoord(coord);
		}

		private boolean containsCoord(Coord coord) {
			return geometries.stream().anyMatch(geom -> geom.contains(MGC.coord2Point(coord)));
		}
	}
}
