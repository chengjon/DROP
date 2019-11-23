
package org.drip.specialfunction.incompletegamma;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2020 Lakshmi Krishnamurthy
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting analytics/risk, transaction cost analytics,
 *  	asset liability management analytics, capital, exposure, and margin analytics, valuation adjustment
 *  	analytics, and portfolio construction analytics within and across fixed income, credit, commodity,
 *  	equity, FX, and structured products. It also includes auxiliary libraries for algorithm support,
 *  	numerical analysis, numerical optimization, spline builder, model validation, statistical learning,
 *  	and computational support.
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
 * <i>LowerSFixed</i> implements the Lower Incomplete Gamma Function using Power Series for a Fixed s. The
 * References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Geddes, K. O., M. L. Glasser, R. A. Moore, and T. C. Scott (1990): Evaluation of Classes of
 * 				Definite Integrals involving Elementary Functions via Differentiation of Special Functions
 * 				<i>Applicable Algebra in Engineering, Communications, and </i> <b>1 (2)</b> 149-165
 * 		</li>
 * 		<li>
 * 			Gradshteyn, I. S., I. M. Ryzhik, Y. V. Geronimus, M. Y. Tseytlin, and A. Jeffrey (2015):
 * 				<i>Tables of Integrals, Series, and Products</i> <b>Academic Press</b>
 * 		</li>
 * 		<li>
 * 			Mathar, R. J. (2010): Numerical Evaluation of the Oscillatory Integral over
 *				e<sup>iπx</sup> x<sup>(1/x)</sup> between 1 and ∞
 *				https://arxiv.org/pdf/0912.3844.pdf <b>arXiV</b>
 * 		</li>
 * 		<li>
 * 			National Institute of Standards and Technology (2019): Incomplete Gamma and Related Functions
 * 				https://dlmf.nist.gov/8
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Incomplete Gamma Function
 * 				https://en.wikipedia.org/wiki/Incomplete_gamma_function
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ComputationalCore.md">Computational Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FunctionAnalysisLibrary.md">Function Analysis Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/specialfunction/README.md">Special Function Implementation Analysis</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/specialfunction/gammaincomplete/README.md">Upper/Lower Incomplete Gamma Functions</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class LowerSFixed extends org.drip.numerical.estimation.R1ToR1Estimator
{
	private org.drip.specialfunction.incompletegamma.LowerSFixedSeries _lowerSFixedSeries = null;

	/**
	 * Construct the Weierstrass Lower S Fixed Series Incomplete Gamma Estimator
	 * 
	 * @param s Incomplete Gamma s
	 * @param termCount Count of the Number of Terms
	 * 
	 * @return The Weierstrass Lower S Fixed Series Incomplete Gamma Estimator
	 */

	public static final LowerSFixed WeierstrassLimit (
		final double s,
		final int termCount)
	{
		try
		{
			return new LowerSFixed (
				org.drip.specialfunction.incompletegamma.LowerSFixedSeries.WeierstrassLimit (
					s,
					termCount
				),
				null
			)
			{
				@Override public double nonDimensional (
					final double z)
					throws java.lang.Exception
				{
					return weierstrassLimit (z) * java.lang.Math.exp (-1. * z);
				}
			};
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct the NIST (2019) Lower S Fixed Series Incomplete Gamma Estimator
	 * 
	 * @param s Incomplete Gamma s
	 * @param termCount Count of the Number of Terms
	 * 
	 * @return The NIST (2019) Lower S Fixed Series Incomplete Gamma Estimator
	 */

	public static final LowerSFixed NIST2019 (
		final double s,
		final int termCount)
	{
		try
		{
			return new LowerSFixed (
				org.drip.specialfunction.incompletegamma.LowerSFixedSeries.NIST2019 (
					s,
					termCount
				),
				null
			)
			{
				@Override public double nonDimensional (
					final double z)
					throws java.lang.Exception
				{
					return weierstrassLimit (z);
				}
			};
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * LowerSFixed Constructor
	 * 
	 * @param lowerSFixedSeries R<sup>1</sup> To R<sup>1</sup> Lower S Fixed Limit Series
	 * @param dc Differential Control
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public LowerSFixed (
		final org.drip.specialfunction.incompletegamma.LowerSFixedSeries lowerSFixedSeries,
		final org.drip.numerical.differentiation.DerivativeControl dc)
		throws java.lang.Exception
	{
		super (dc);

		_lowerSFixedSeries = lowerSFixedSeries;
	}

	@Override public org.drip.numerical.estimation.R1Estimate seriesEstimateNative (
		final double x)
	{
		return null == _lowerSFixedSeries ? seriesEstimate (
			x,
			null,
			null
		) : seriesEstimate (
			x,
			_lowerSFixedSeries.termWeightMap(),
			_lowerSFixedSeries
		);
	}

	/**
	 * Compute the Limiting Weierstrass Sum
	 * 
	 * @param z Z
	 * 
	 * @return The Limiting Weierstrass Sum
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double weierstrassLimit (
		final double z)
		throws java.lang.Exception
	{
		return _lowerSFixedSeries.evaluate (z);
	}

	/**
	 * Compute the Non-dimensional Incomplete Gamma (Weierstrass Gamma Star)
	 * 
	 * @param z Z
	 * 
	 * @return The Non-dimensional Incomplete Gamma (Weierstrass Gamma Star)
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public abstract double nonDimensional (
		final double z)
		throws java.lang.Exception;

	@Override public double evaluate (
		final double z)
		throws java.lang.Exception
	{
		return java.lang.Math.exp (
			_lowerSFixedSeries.s() * java.lang.Math.log (z) + _lowerSFixedSeries.logGammaS()
		) * nonDimensional (z);
	}
}
