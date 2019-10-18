
package org.drip.sample.assetallocationexcel;

import org.drip.function.rdtor1descent.LineStepEvolutionControl;
import org.drip.function.rdtor1solver.InteriorPointBarrierControl;
import org.drip.measure.statistics.MultivariateMoments;
import org.drip.numerical.common.FormatUtil;
import org.drip.portfolioconstruction.allocator.*;
import org.drip.portfolioconstruction.asset.*;
import org.drip.portfolioconstruction.params.AssetUniverseStatisticalProperties;
import org.drip.service.env.EnvManager;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
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
 * <i>CMVMonthlyReconciler07</i> demonstrates the Execution and Reconciliation of the Dual Constrained Mean
 * Variance against an XL-based Monthly Series Implementation for Portfolio Design Returns #7.
 *  
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AssetAllocationAnalyticsLibrary.md">Asset Allocation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/assetallocationexcel/README.md">Asset Allocation Excel</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class CMVMonthlyReconciler07
{

	public static final void main (
		final String[] argumentArray)
		throws Exception
	{
		EnvManager.InitEnv (
			"",
			true
		);

		String[] assetIDArray = new String[]
		{
			"TOK",
			"EWJ",
			"HYG",
			"LQD",
			"EMD",
			"GSG",
			"BWX"
		};
		double[] assetHoldingsLowerBoundArray = new double[]
		{
			0.00,
			0.00,
			0.00,
			0.00,
			0.00,
			0.00,
			0.00
		};
		double[] assetHoldingsUpperBoundArray = new double[]
		{
			0.30,
			0.30,
			0.30,
			0.50,
			0.30,
			0.30,
			0.50
		};
		double[] expectedAssetReturnsArray = new double[]
		{
			0.009190,
			0.007930,
			0.007050,
			0.003134,
			0.006840,
			0.007980,
			0.003727
		};
		double portfolioDesignReturn = 0.005586;
		double portfolioRiskExcel = 0.028166;
		double[][] assetReturnsCovarianceMatrix = new double[][]
		{
			{0.002733, 0.002083, 0.001593, 0.000488, 0.001172, 0.002312, 0.000710},
			{0.002083, 0.002768, 0.001302, 0.000457, 0.001105, 0.001647, 0.000563},
			{0.001593, 0.001302, 0.001463, 0.000639, 0.001050, 0.001110, 0.000519},
			{0.000488, 0.000457, 0.000639, 0.000608, 0.000663, 0.000042, 0.000370},
			{0.001172, 0.001105, 0.001050, 0.000663, 0.001389, 0.000825, 0.000661},
			{0.002312, 0.001647, 0.001110, 0.000042, 0.000825, 0.005211, 0.000749},
			{0.000710, 0.000563, 0.000519, 0.000370, 0.000661, 0.000749, 0.000703}
		};
		double[] assetWeightsReconcilerArray = new double[]
		{
			0.059817,
			0.059468,
			0.181286,
			0.213394,
			0.186720,
			0.052937,
			0.246378
		};

		AssetComponent[] assetComponentReconcilerArray =
			new AssetComponent[assetWeightsReconcilerArray.length];

		for (int assetIndex = 0;
			assetIndex < assetWeightsReconcilerArray.length;
			++assetIndex)
		{
			assetComponentReconcilerArray[assetIndex] = new AssetComponent (
				assetIDArray[assetIndex],
				assetWeightsReconcilerArray[assetIndex]
			);
		}

		AssetUniverseStatisticalProperties assetUniverseStatisticalProperties =
			AssetUniverseStatisticalProperties.FromMultivariateMetrics (
				MultivariateMoments.Standard (
					assetIDArray,
					expectedAssetReturnsArray,
					assetReturnsCovarianceMatrix
				)
			);

		double[][] covarianceMatrix = assetUniverseStatisticalProperties.covariance (
			assetIDArray
		);

		System.out.println ("\n\n\t|------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                  CROSS ASSET COVARIANCE MATRIX                                 ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------||");

		String header = "\t|     |";

		for (int assetIndex = 0;
			assetIndex < assetIDArray.length;
			++assetIndex)
		{
			header += "    " + assetIDArray[assetIndex] + "     |";
		}

		System.out.println (header + "|");

		System.out.println ("\t|------------------------------------------------------------------------------------------------||");

		for (int assetIndexI = 0;
			assetIndexI < assetIDArray.length;
			++assetIndexI)
		{
			String dump = "\t| " + assetIDArray[assetIndexI] + " ";

			for (int assetIndexJ = 0;
				assetIndexJ < assetIDArray.length;
				++assetIndexJ)
			{
				dump += "|" + FormatUtil.FormatDouble (
					covarianceMatrix[assetIndexI][assetIndexJ], 1, 8, 1.
				) + " ";
			}

			System.out.println (dump + "||");
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------||\n\n");

		System.out.println ("\t|-------------------||");

		System.out.println ("\t|   ASSET BOUNDS    ||");

		System.out.println ("\t|-------------------||");

		for (int assetIndex = 0;
			assetIndex < assetIDArray.length;
			++assetIndex)
		{
			System.out.println (
				"\t| " + assetIDArray[assetIndex] + " | " +
				FormatUtil.FormatDouble (assetHoldingsLowerBoundArray[assetIndex], 2, 0, 100.) + "% | " +
				FormatUtil.FormatDouble (assetHoldingsUpperBoundArray[assetIndex], 2, 0, 100.) + "% ||"
			);
		}

		System.out.println ("\t|-------------------||\n\n");

		InteriorPointBarrierControl interiorPointBarrierControl = InteriorPointBarrierControl.Standard();

		System.out.println ("\t|--------------------------------------------||");

		System.out.println ("\t|  INTERIOR POINT METHOD BARRIER PARAMETERS  ||");

		System.out.println ("\t|--------------------------------------------||");

		System.out.println (
			"\t|    Barrier Decay Velocity        : " + 1. / interiorPointBarrierControl.decayVelocity()
		);

		System.out.println (
			"\t|    Barrier Decay Steps           : " + interiorPointBarrierControl.decayStepCount()
		);

		System.out.println (
			"\t|    Initial Barrier Strength      : " + interiorPointBarrierControl.initialStrength()
		);

		System.out.println (
			"\t|    Barrier Convergence Tolerance : " + interiorPointBarrierControl.relativeTolerance()
		);

		System.out.println ("\t|--------------------------------------------||\n\n");

		BoundedPortfolioConstructionParameters boundedPortfolioConstructionParameters =
			new BoundedPortfolioConstructionParameters (
				assetIDArray,
				CustomRiskUtilitySettings.VarianceMinimizer(),
				new EqualityConstraintSettings (
					EqualityConstraintSettings.FULLY_INVESTED_CONSTRAINT |
						EqualityConstraintSettings.RETURNS_CONSTRAINT,
					portfolioDesignReturn
				)
			);

		for (int assetIndex = 0;
			assetIndex < assetIDArray.length;
			++assetIndex)
		{
			boundedPortfolioConstructionParameters.addBound (
				assetIDArray[assetIndex],
				assetHoldingsLowerBoundArray[assetIndex],
				assetHoldingsUpperBoundArray[assetIndex]
			);
		}

		OptimizationOutput optimizationOutput = new ConstrainedMeanVarianceOptimizer (
			interiorPointBarrierControl,
			LineStepEvolutionControl.NocedalWrightStrongWolfe (
				false
			)
		).allocate (
			boundedPortfolioConstructionParameters,
			assetUniverseStatisticalProperties
		);

		AssetComponent[] optimalAssetComponentArray =
			optimizationOutput.optimalPortfolio().assetComponentArray();

		System.out.println ("\t|------------------------------||");

		System.out.println ("\t|    OPTIMAL  ASSET  WEIGHTS   ||");

		System.out.println ("\t|------------------------------||");

		System.out.println ("\t| ASSET |   DROP   |   EXCEL   ||");

		System.out.println ("\t|------------------------------||");

		for (int assetIndex = 0;
			assetIndex < optimalAssetComponentArray.length;
			++assetIndex)
		{
			System.out.println (
				"\t|  " + optimalAssetComponentArray[assetIndex].id() + "  |" +
				FormatUtil.FormatDouble (
					optimalAssetComponentArray[assetIndex].amount(), 2, 4, 100.
				) + "% | " +
				FormatUtil.FormatDouble (
					assetComponentReconcilerArray[assetIndex].amount(), 2, 4, 100.
				) + "% ||"
			);
		}

		System.out.println ("\t|------------------------------||\n\n");

		System.out.println ("\t|-------------------------------------------------------------||");

		System.out.println (
			"\t| Optimal Portfolio Normalize                     : " + FormatUtil.FormatDouble (
				optimizationOutput.optimalPortfolio().notional(), 1, 4, 1.
			) + "   ||"
		);

		System.out.println (
			"\t| Optimal Portfolio Input Return                  : " + FormatUtil.FormatDouble (
				portfolioDesignReturn, 1, 4, 100.
			) + "%  ||"
		);

		System.out.println (
			"\t| Optimal Portfolio Expected Return               : " + FormatUtil.FormatDouble (
				optimizationOutput.optimalMetrics().excessReturnsMean(), 1, 4, 100.
			) + "%  ||"
		);

		System.out.println (
			"\t| Optimal Portfolio Standard Deviation            : " + FormatUtil.FormatDouble (
				optimizationOutput.optimalMetrics().excessReturnsStandardDeviation(), 1, 4, 100.
			) + "%  ||"
		);

		System.out.println (
			"\t| Excel Portfolio Standard Deviation (Calculated) : " + FormatUtil.FormatDouble (
				Math.sqrt (
					new Portfolio (
						assetComponentReconcilerArray
					).variance (
						assetUniverseStatisticalProperties
					)
				), 1, 4, 100.
			) + "%  ||"
		);

		System.out.println (
			"\t| Excel Portfolio Standard Deviation (Input)      : " + FormatUtil.FormatDouble (
				portfolioRiskExcel, 1, 4, 100.
			) + "%  ||"
		);

		System.out.println ("\t|-------------------------------------------------------------||\n");

		EnvManager.TerminateEnv();
	}
}
