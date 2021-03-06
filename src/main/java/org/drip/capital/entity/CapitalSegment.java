
package org.drip.capital.entity;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2022 Lakshmi Krishnamurthy
 * Copyright (C) 2021 Lakshmi Krishnamurthy
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	graph builder/navigator, and computational support.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
 *  
 *  - DROP Product Core - https://lakshmidrip.github.io/DROP-Product-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Computational Core - https://lakshmidrip.github.io/DROP-Computational-Core/
 * 
 * 	DROP Product Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Loan Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 *  - Asset Liability Management Analytics
 * 	- Capital Estimation Analytics
 * 	- Exposure Analytics
 * 	- Margin Analytics
 * 	- XVA Analytics
 * 
 * 	DROP Computational Core implements libraries for the following:
 * 	- Algorithm Support
 * 	- Computation Support
 * 	- Function Analysis
 *  - Graph Algorithm
 *  - Model Validation
 * 	- Numerical Analysis
 * 	- Numerical Optimizer
 * 	- Spline Builder
 *  - Statistical Learning
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
 * <i>CapitalSegment</i> exposes the VaR and the Stress Functionality for a Capital Segment. The References
 * 	are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Bank for International Supervision(2005): Stress Testing at Major Financial Institutions: Survey
 * 				Results and Practice https://www.bis.org/publ/cgfs24.htm
 * 		</li>
 * 		<li>
 * 			Glasserman, P. (2004): <i>Monte Carlo Methods in Financial Engineering</i> <b>Springer</b>
 * 		</li>
 * 		<li>
 * 			Kupiec, P. H. (2000): Stress Tests and Risk Capital <i>Risk</i> <b>2 (4)</b> 27-39
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/CapitalAnalyticsLibrary.md">Capital Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/capital/README.md">Basel Market Risk and Operational Capital</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/capital/entity/README.md">Economic Risk Capital Estimation Nodes</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public abstract class CapitalSegment
	implements org.drip.capital.entity.CapitalSimulator
{
	private org.drip.capital.label.CapitalSegmentCoordinate _coordinate = null;

	protected CapitalSegment (
		final org.drip.capital.label.CapitalSegmentCoordinate coordinate)
		throws java.lang.Exception
	{
		if (null == (_coordinate = coordinate))
		{
			throw new java.lang.Exception (
				"CapitalSegment Constructor => Invalid Inputs"
			);
		}
	}

	/**
	 * Retrieve the Capital Segment Coordinate
	 * 
	 * @return Capital Segment Coordinate
	 */

	public org.drip.capital.label.CapitalSegmentCoordinate coordinate()
	{
		return _coordinate;
	}

	/**
	 * Retrieve the Array of Capital Units
	 * 
	 * @return Array of Capital Units
	 */

	public abstract org.drip.capital.entity.CapitalUnit[] capitalUnitArray();

	/**
	 * Generate the Grid of Capital Unit Path Realizations
	 * 
	 * @param simulationControl Simulation Settings
	 * @param simulationPnLControl PnL Settings
	 * 
	 * @return Grid of Capital Unit Path Realizations
	 */

	public org.drip.capital.simulation.PathPnLRealization[][] capitalUnitPathPnLRealizationGrid (
		final org.drip.capital.setting.SimulationControl simulationControl,
		final org.drip.capital.setting.SimulationPnLControl simulationPnLControl)
	{
		org.drip.capital.entity.CapitalUnit[] capitalUnitArray = capitalUnitArray();

		int capitalUnitCount = capitalUnitArray.length;
		org.drip.capital.simulation.PathPnLRealization[][] capitalUnitPathPnLRealizationGrid = new
			org.drip.capital.simulation.PathPnLRealization[capitalUnitCount][];

		for (int capitalUnitIndex = 0; capitalUnitIndex < capitalUnitCount; ++capitalUnitIndex)
		{
			if (null == (capitalUnitPathPnLRealizationGrid[capitalUnitIndex] =
				capitalUnitArray[capitalUnitIndex].pathPnLRealizationArray (
					simulationControl,
					simulationPnLControl
				)
			))
			{
				return null;
			}
		}

		return capitalUnitPathPnLRealizationGrid;
	}

	@Override public org.drip.capital.simulation.PathPnLRealization[] pathPnLRealizationArray (
		final org.drip.capital.setting.SimulationControl simulationControl,
		final org.drip.capital.setting.SimulationPnLControl simulationPnLControl)
	{
		int pathCount = simulationControl.pathCount();

		org.drip.capital.simulation.PathPnLRealization[][] capitalUnitPathPnLRealizationGrid =
			capitalUnitPathPnLRealizationGrid (
				simulationControl,
				simulationPnLControl
			);

		if (null == capitalUnitPathPnLRealizationGrid)
		{
			return null;
		}

		org.drip.capital.entity.CapitalUnit[] capitalUnitArray = capitalUnitArray();

		int capitalUnitCount = capitalUnitArray.length;
		org.drip.capital.simulation.PathPnLRealization[] pathPnLRealizationArray =
			new org.drip.capital.simulation.PathPnLRealization[pathCount];

		for (int pathIndex = 0;
			pathIndex < pathCount;
			++pathIndex)
		{
			org.drip.capital.simulation.PathPnLRealization[] singlePathPnLRealizationArray =
				new org.drip.capital.simulation.PathPnLRealization[capitalUnitCount];

			for (int capitalUnitIndex = 0;
				capitalUnitIndex < capitalUnitCount;
				++capitalUnitIndex)
			{
				singlePathPnLRealizationArray[capitalUnitIndex] =
					capitalUnitPathPnLRealizationGrid[capitalUnitIndex][pathIndex];
			}

			if (null == (pathPnLRealizationArray[pathIndex] =
					org.drip.capital.simulation.PathPnLRealization.Combine (
						singlePathPnLRealizationArray
					)
				)
			)
			{
				return null;
			}
		}

		return pathPnLRealizationArray;
	}

	@Override public org.drip.capital.simulation.CapitalSegmentPathEnsemble pathEnsemble (
		final org.drip.capital.setting.SimulationControl simulationControl,
		final org.drip.capital.setting.SimulationPnLControl simulationPnLControl)
	{
		org.drip.capital.simulation.PathPnLRealization[][] capitalUnitPathPnLRealizationGrid =
			capitalUnitPathPnLRealizationGrid (
				simulationControl,
				simulationPnLControl
			);

		if (null == capitalUnitPathPnLRealizationGrid)
		{
			return null;
		}

		int pathCount = simulationControl.pathCount();

		java.util.Map<java.lang.String, org.drip.capital.simulation.PathEnsemble> pathEnsembleMap = new
			org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.capital.simulation.PathEnsemble>();

		org.drip.capital.entity.CapitalUnit[] capitalUnitArray = capitalUnitArray();

		int capitalUnitCount = capitalUnitArray.length;
		org.drip.capital.simulation.CapitalSegmentPathEnsemble capitalSegmentPathEnsemble = null;
		org.drip.capital.simulation.PathPnLRealization[] pathPnLRealizationArray = new
			org.drip.capital.simulation.PathPnLRealization[pathCount];

		for (int capitalUnitIndex = 0;
			capitalUnitIndex < capitalUnitCount;
			++capitalUnitIndex)
		{
			org.drip.capital.simulation.CapitalUnitPathEnsemble capitalUnitPathEnsemble =
				new org.drip.capital.simulation.CapitalUnitPathEnsemble();

			for (org.drip.capital.simulation.PathPnLRealization pathPnLRealization :
				capitalUnitPathPnLRealizationGrid[capitalUnitIndex])
			{
				if (!capitalUnitPathEnsemble.addPathPnLRealization (pathPnLRealization))
				{
					return null;
				}
			}

			pathEnsembleMap.put (
				capitalUnitArray[capitalUnitIndex].coordinate().fullyQualifiedName(),
				capitalUnitPathEnsemble
			);
		}

		try
		{
			capitalSegmentPathEnsemble = new org.drip.capital.simulation.CapitalSegmentPathEnsemble (
				pathEnsembleMap
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();

			return null;
		}

		for (int pathIndex = 0;
			pathIndex < pathCount;
			++pathIndex)
		{
			org.drip.capital.simulation.PathPnLRealization[] singlePathPnLRealizationArray =
				new org.drip.capital.simulation.PathPnLRealization[capitalUnitCount];

			for (int capitalUnitIndex = 0;
				capitalUnitIndex < capitalUnitCount;
				++capitalUnitIndex)
			{
				singlePathPnLRealizationArray[capitalUnitIndex] =
					capitalUnitPathPnLRealizationGrid[capitalUnitIndex][pathIndex];
			}

			if (null == (pathPnLRealizationArray[pathIndex] =
					org.drip.capital.simulation.PathPnLRealization.Combine (
						singlePathPnLRealizationArray
					)
				)
			)
			{
				return null;
			}
		}

		for (org.drip.capital.simulation.PathPnLRealization pathPnLRealization : pathPnLRealizationArray)
		{
			if (!capitalSegmentPathEnsemble.addPathPnLRealization (
				pathPnLRealization
			))
			{
				return null;
			}
		}

		return capitalSegmentPathEnsemble;
	}

	/**
	 * Generate the Simulation Path Ensemble Constricted to the specified Path Ensemble Map
	 * 
	 * @param pathEnsembleMap The Path Ensemble Constriction Map
	 * 
	 * @return The Constricted Simulation Path Ensemble
	 */

	public org.drip.capital.simulation.CapitalSegmentPathEnsemble pathEnsemble (
		final java.util.Map<java.lang.String, org.drip.capital.simulation.PathEnsemble> pathEnsembleMap)
	{
		if (null == pathEnsembleMap || 0 == pathEnsembleMap.size())
		{
			return null;
		}

		java.util.Map<java.lang.String, org.drip.capital.simulation.PathEnsemble>
			constrictedPathEnsembleMap = new
				org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.capital.simulation.PathEnsemble>();

		org.drip.capital.entity.CapitalUnit[] capitalUnitArray = capitalUnitArray();

		int pathCount = -1;
		int capitalUnitCount = capitalUnitArray.length;
		org.drip.capital.simulation.PathPnLRealization[][] capitalUnitPathPnLRealizationGrid =
			new org.drip.capital.simulation.PathPnLRealization[capitalUnitCount][];
		org.drip.capital.simulation.CapitalSegmentPathEnsemble constrictedCapitalSegmentPathEnsemble =
			null;

		for (int capitalUnitIndex = 0;
			capitalUnitIndex < capitalUnitCount;
			++capitalUnitIndex)
		{
			java.lang.String capitalUnitCoordinateFQN =
				capitalUnitArray[capitalUnitIndex].coordinate().fullyQualifiedName();

			if (!pathEnsembleMap.containsKey (
				capitalUnitCoordinateFQN
			))
			{
				return null;
			}

			org.drip.capital.simulation.PathEnsemble pathEnsemble = pathEnsembleMap.get (
				capitalUnitCoordinateFQN
			);

			constrictedPathEnsembleMap.put (
				capitalUnitCoordinateFQN,
				pathEnsemble
			);

			capitalUnitPathPnLRealizationGrid[capitalUnitIndex] = (
				(org.drip.capital.simulation.CapitalUnitPathEnsemble) pathEnsemble
			).pathPnLRealizationArray();

			if (0 == capitalUnitIndex)
			{
				pathCount = pathEnsemble.count();
			}
		}

		try
		{
			constrictedCapitalSegmentPathEnsemble = new
				org.drip.capital.simulation.CapitalSegmentPathEnsemble (
					constrictedPathEnsembleMap
				);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();

			return null;
		}

		org.drip.capital.simulation.PathPnLRealization[] pathPnLRealizationArray =
			new org.drip.capital.simulation.PathPnLRealization[pathCount];

		for (int pathIndex = 0;
			pathIndex < pathCount;
			++pathIndex)
		{
			org.drip.capital.simulation.PathPnLRealization[] singlePathPnLRealizationArray =
				new org.drip.capital.simulation.PathPnLRealization[capitalUnitCount];

			for (int capitalUnitIndex = 0;
				capitalUnitIndex < capitalUnitCount;
				++capitalUnitIndex)
			{
				singlePathPnLRealizationArray[capitalUnitIndex] =
					capitalUnitPathPnLRealizationGrid[capitalUnitIndex][pathIndex];
			}

			if (null == (pathPnLRealizationArray[pathIndex] =
					org.drip.capital.simulation.PathPnLRealization.Combine (
						singlePathPnLRealizationArray
					)
				)
			)
			{
				return null;
			}
		}

		for (org.drip.capital.simulation.PathPnLRealization pathPnLRealization : pathPnLRealizationArray)
		{
			if (!constrictedCapitalSegmentPathEnsemble.addPathPnLRealization (
				pathPnLRealization
			))
			{
				return null;
			}
		}

		return constrictedCapitalSegmentPathEnsemble;
	}
}
