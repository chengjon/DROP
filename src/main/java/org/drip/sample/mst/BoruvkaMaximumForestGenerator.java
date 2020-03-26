
package org.drip.sample.mst;

import java.util.Map;

import org.drip.graph.core.BidirectionalEdge;
import org.drip.graph.core.Graph;
import org.drip.graph.core.Tree;
import org.drip.graph.mstgreedy.BoruvkaGenerator;
import org.drip.service.env.EnvManager;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, valuation adjustment, and portfolio construction within and across fixed income,
 *  	credit, commodity, equity, FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Analytics Core - https://lakshmidrip.github.io/DROP-Analytics-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Numerical Core - https://lakshmidrip.github.io/DROP-Numerical-Core/
 * 
 * 	DROP Analytics Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Asset Backed Analytics
 * 	- XVA Analytics
 * 	- Exposure and Margin Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Numerical Core implements libraries for the following:
 * 	- Statistical Learning
 * 	- Numerical Optimizer
 * 	- Spline Builder
 * 	- Algorithm Support
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>BoruvkaMaximumForestGenerator</i> illustrates the Execution of the Boruvka Algorithm for the Generation
 * 	of the Maximum Spanning Forest. The References are:
 *  
 * <br><br>
 *  <ul>
 *  	<li>
 *  		Bader, D. A., and G. Cong (2006): Fast Shared Memory Algorithms for computing the Minimum
 *  			Spanning Forests of Sparse Graphs <i>Journal of Parallel and Distributed Computing</i>
 *  			<b>66 (11)</b> 1366-1378
 *  	</li>
 *  	<li>
 *  		Chazelle, B. (2000): A Minimum Spanning Tree ALgorithm with Inverse-Ackerman Type Complexity
 *  			<i> Journal of the Association for Computing Machinery</i> <b>47 (6)</b> 1028-1047
 *  	</li>
 *  	<li>
 *  		Karger, D. R., P. N. Klein, and R. E. Tarjan (1995): A Randomized Linear-Time Algorithm to find
 *  			Minimum Spanning Trees <i> Journal of the Association for Computing Machinery</i> <b>42
 *  			(2)</b> 321-328
 *  	</li>
 *  	<li>
 *  		Mares, M. (2004): Two Linear-Time Algorithms for MSTon Minor Closed Graph Classes <i>Activium
 *  			Mathematicum</i> <b>40 (3)</b> 315-320
 *  	</li>
 *  	<li>
 *  		Wikipedia (2019): Boruvka's Algorithm https://en.wikipedia.org/wiki/Bor%C5%AFvka's_algorithm
 *  	</li>
 *  </ul>
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/GraphAlgorithmLibrary.md">Graph Algorithm Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">DROP API Construction and Usage</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/mst/README.md">Minimum Spanning Tree and Forest Algorithms</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class BoruvkaMaximumForestGenerator
{

	public static final void main (
		final String[] argumentArray)
		throws Exception
	{
		EnvManager.InitEnv (
			""
		);

		String[] vertexArray = new String[]
		{
			"delhi     ",
			"bombay    ",
			"madras    ",
			"calcutta  ",
			"bangalore ",
			"hyderabad ",
			"cochin    ",
			"pune      ",
			"ahmedabad ",
			"jaipur    "
		};

		Graph graph = new Graph();

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[0], // Delhi
				vertexArray[1], // Bombay
				1388.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[0], // Delhi
				vertexArray[2], // Madras
				2191.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[1], // Bombay
				vertexArray[2], // Madras
				1279.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[0], // Delhi
				vertexArray[3], // Calcutta
				1341.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[1], // Bombay
				vertexArray[3], // Calcutta
				1968.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[2], // Madras
				vertexArray[3], // Calcutta
				1663.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[2], // Madras
				vertexArray[4], // Bangalore
				361.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[2], // Madras
				vertexArray[5], // Hyderabad
				784.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[2], // Madras
				vertexArray[6], // Cochin
				697.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[1], // Bombay
				vertexArray[7], // Pune
				192.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[1], // Bombay
				vertexArray[8], // Ahmedabad
				492.
			)
		);

		graph.addBidirectionalEdge (
			new BidirectionalEdge (
				vertexArray[0], // Delhi
				vertexArray[9], // Jaipur
				308.
			)
		);

		BoruvkaGenerator boruvka = new BoruvkaGenerator (
			graph,
			true
		);

		Map<String, Tree> maximumSpanningForest = boruvka.optimalSpanningForest().treeMap();

		for (Tree maximumSpanningTree : maximumSpanningForest.values())
		{
			System.out.println (
				"\t|-----------------------------------------------------------------------------------|"
			);

			System.out.println (
				"\t|                        BORUVKA MAXIMUM SPANNING TREE PATH                         |"
			);

			System.out.println (
				"\t|-----------------------------------------------------------------------------------|"
			);

			for (BidirectionalEdge edge : maximumSpanningTree.edgeMap().values())
			{
				System.out.println (
					"\t| " + edge
				);
			}

			System.out.println (
				"\t|-----------------------------------------------------------------------------------|"
			);

			System.out.println (
				"\t| Maximum Bottleneck Edge => " + maximumSpanningTree.maximumBottleneckEdge()
			);

			System.out.println (
				"\t| Minimum Bottleneck Edge => " + maximumSpanningTree.minimumBottleneckEdge()
			);

			System.out.println (
				"\t| Total MST Length        => " + maximumSpanningTree.length()
			);

			System.out.println (
				"\t|-----------------------------------------------------------------------------------|"
			);
		}

		EnvManager.TerminateEnv();
	}
}
