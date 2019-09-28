
package org.drip.sample.idzorek;

import org.drip.numerical.common.FormatUtil;
import org.drip.portfolioconstruction.allocator.ForwardReverseOptimizationOutput;
import org.drip.portfolioconstruction.asset.*;
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
 * <i>ExpectedExcessReturnsWeights</i> reconciles the Expected Returns and the corresponding Weights for
 * different Input Asset Distributions using the Black-Litterman Model Process. The References are:
 *  
 * <br><br>
 *  <ul>
 *  	<li>
 *  		He. G., and R. Litterman (1999): The Intuition behind the Black-Litterman Model Portfolios,
 *  			Goldman Sachs Asset Management
 *  	</li>
 *  	<li>
 *  		Idzorek, T. (2005): A Step-by-Step Guide to the Black-Litterman Model: Incorporating User
 *  			Specified Confidence Levels, Ibbotson Associates, Chicago
 *  	</li>
 *  </ul>
 *  
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AssetAllocationAnalyticsLibrary.md">Asset Allocation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/idzorek/README.md">Idzorek (2005) User Confidence Setting</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExpectedExcessReturnsWeights {

	private static final void ForwardOptimizationWeights (
		final ForwardReverseOptimizationOutput from,
		final double[] adblWeightReconciler,
		final int iLeft,
		final int iRight,
		final String strHeader)
	{
		Portfolio fromPortfolio = from.optimalPortfolio();

		String[] astrAssetID = fromPortfolio.assetIDArray();

		double[] adblWeight = fromPortfolio.weightArray();

		AssetComponent acMaxWeight = fromPortfolio.highestWeightAsset();

		AssetComponent acMinWeight = fromPortfolio.lowestWeightAsset();

		System.out.println ("\t|------------------------------------------------------||");

		System.out.println (strHeader);

		System.out.println ("\t|------------------------------------------------------||");

		System.out.println ("\t|                  ID               =>  CALC  | VERIFY ||");

		System.out.println ("\t|------------------------------------------------------||");

		for (int i = 0; i < adblWeight.length; ++i)
			System.out.println (
				"\t| [" + astrAssetID[i] + "] => " +
				FormatUtil.FormatDouble (adblWeight[i], iLeft, iRight, 100.) + "% | " +
				FormatUtil.FormatDouble (adblWeightReconciler[i], iLeft, iRight, 100.) + "% ||"
			);

		System.out.println ("\t|------------------------------------------------------||");

		System.out.println ("\t| HIGH : " + acMaxWeight.id() + " => "+ FormatUtil.FormatDouble (acMaxWeight.amount(), iLeft, iRight, 100.) + "%     ||");

		System.out.println ("\t| LOW  : " + acMinWeight.id() + " => "+ FormatUtil.FormatDouble (acMinWeight.amount(), iLeft, iRight, 100.) + "%     ||");

		System.out.println ("\t|------------------------------------------------------||\n");
	}

	public static final void main (
		final String[] astArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double dblRiskAversion = 3.07;

		String[] astrAssetID = new String[] {
			"US BONDS                       ",
			"INTERNATIONAL BONDS            ",
			"US LARGE GROWTH                ",
			"US LARGE VALUE                 ",
			"US SMALL GROWTH                ",
			"US SMALL VALUE                 ",
			"INTERNATIONAL DEVELOPED EQUITY ",
			"INTERNATIONAL EMERGING EQUITY  "
		};

		double[] adblAssetEquilibriumWeight = new double[] {
			0.1934,
			0.2613,
			0.1209,
			0.1209,
			0.0134,
			0.0134,
			0.2418,
			0.0349
		};

		double[][] aadblAssetExcessReturnsCovariance = new double[][] {
			{ 0.001005,  0.001328, -0.000579, -0.000675,  0.000121,  0.000128, -0.000445, -0.000437},
			{ 0.001328,  0.007277, -0.001307, -0.000610, -0.002237, -0.000989,  0.001442, -0.001535},
			{-0.000579, -0.001307,  0.059582,  0.027588,  0.063497,  0.023036,  0.032967,  0.048039},
			{-0.000675, -0.000610,  0.027588,  0.029609,  0.026572,  0.021465,  0.020697,  0.029854},
			{ 0.000121, -0.002237,  0.063497,  0.026572,  0.102488,  0.042744,  0.039943,  0.065994},
			{ 0.000128, -0.000989,  0.023036,  0.021465,  0.042744,  0.032056,  0.019881,  0.032235},
			{-0.000445,  0.001442,  0.032967,  0.020697,  0.039943,  0.019881,  0.028355,  0.035064},
			{-0.000437, -0.001535,  0.048039,  0.029854,  0.065994,  0.032235,  0.035064,  0.079958}
		};

		double[] adblAssetSpaceHistoricalReturns = new double[] {
			 0.0315,
			 0.0175,
			-0.0639,
			-0.0286,
			-0.0675,
			-0.0054,
			-0.0675,
			-0.0526
		};

		double[] adblAssetSpaceCAPMReturns = new double[] {
			0.0008,
			0.0067,
			0.0641,
			0.0408,
			0.0743,
			0.0370,
			0.0480,
			0.0660
		};

		double[] adblAssetSpaceGSMIReturns = new double[] {
			 0.0002,
			 0.0018,
			 0.0557,
			 0.0339,
			 0.0659,
			 0.0316,
			 0.0392,
			 0.0560
		};

		double[] adblHistoricalPortfolioWeightReconciler = new double[] {
			 11.4432,
			 -1.0459,
			  0.5459,
			 -0.0529,
			 -0.6052,
			  0.8147,
			 -1.0436,
			  0.1459
		};

		double[] adblCAPMGSMIPortfolioWeightReconciler = new double[] {
			  0.2133,
			  0.0519,
			  0.1080,
			  0.1082,
			  0.0373,
			 -0.0049,
			  0.1710,
			  0.0214
		};

		ForwardReverseOptimizationOutput fromPrior = ForwardReverseOptimizationOutput.Reverse (
			Portfolio.Standard (
				astrAssetID,
				adblAssetEquilibriumWeight
			),
			aadblAssetExcessReturnsCovariance,
			dblRiskAversion
		);

		double[] adblImpliedEquilibriumExcessReturns = fromPrior.expectedAssetExcessReturnsArray();

		System.out.println ("\n\t|---------------------------------------------------------------------||");

		System.out.println ("\t|               STARTING RETURNS SOURCES RECONCILIATION               ||");

		System.out.println ("\t|---------------------------------------------------------------------||");

		System.out.println ("\t|                ID                 =>  HIST  | GSMI  | CAPM  | IMPL  ||");

		System.out.println ("\t|---------------------------------------------------------------------||");

		for (int i = 0; i < adblImpliedEquilibriumExcessReturns.length; ++i)
			System.out.println (
				"\t| [" + astrAssetID[i] + "] => " +
				FormatUtil.FormatDouble (adblAssetSpaceHistoricalReturns[i], 1, 2, 100.) + "% |" +
				FormatUtil.FormatDouble (adblAssetSpaceGSMIReturns[i], 1, 2, 100.) + "% |" +
				FormatUtil.FormatDouble (adblAssetSpaceCAPMReturns[i], 1, 2, 100.) + "% |" +
				FormatUtil.FormatDouble (dblRiskAversion * adblImpliedEquilibriumExcessReturns[i], 1, 2, 100.) + "% ||"
			);

		System.out.println ("\t|---------------------------------------------------------------------||\n");

		ForwardOptimizationWeights (
			ForwardReverseOptimizationOutput.Forward (
				astrAssetID,
				adblAssetSpaceHistoricalReturns,
				aadblAssetExcessReturnsCovariance,
				dblRiskAversion
			),
			adblHistoricalPortfolioWeightReconciler,
			4,
			0,
			"\t|             HISTORICAL WEIGHTS RECONCILER            ||"
		);

		ForwardOptimizationWeights (
			ForwardReverseOptimizationOutput.Forward (
				astrAssetID,
				adblAssetSpaceGSMIReturns,
				aadblAssetExcessReturnsCovariance,
				dblRiskAversion
			),
			adblCAPMGSMIPortfolioWeightReconciler,
			2,
			1,
			"\t|              CAPM GSMI WEIGHTS RECONCILER            ||"
		);

		ForwardOptimizationWeights (
			ForwardReverseOptimizationOutput.Forward (
				astrAssetID,
				adblAssetSpaceCAPMReturns,
				aadblAssetExcessReturnsCovariance,
				dblRiskAversion
			),
			adblAssetEquilibriumWeight,
			2,
			1,
			"\t|             EQUILIBRIUM WEIGHTS RECONCILER           ||"
		);

		EnvManager.TerminateEnv();
	}
}
