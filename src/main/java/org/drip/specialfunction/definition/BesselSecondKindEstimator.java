
package org.drip.specialfunction.definition;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, and portfolio construction within and across fixed income, credit, commodity, equity,
 *  	FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three main modules:
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
 * 	- Statistical Learning Library
 * 	- Numerical Optimizer Library
 * 	- Machine Learning Library
 * 	- Spline Builder Library
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
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
 * <i>BesselSecondKindEstimator</i> exposes the Estimator for the Bessel Function of the Second Kind. The
 * References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Abramowitz, M., and I. A. Stegun (2007): <i>Handbook of Mathematics Functions</i> <b>Dover Book
 * 				on Mathematics</b>
 * 		</li>
 * 		<li>
 * 			Arfken, G. B., and H. J. Weber (2005): <i>Mathematical Methods for Physicists 6<sup>th</sup>
 * 				Edition</i> <b>Harcourt</b> San Diego
 * 		</li>
 * 		<li>
 * 			Temme N. M. (1996): <i>Special Functions: An Introduction to the Classical Functions of
 * 				Mathematical Physics 2<sup>nd</sup> Edition</i> <b>Wiley</b> New York
 * 		</li>
 * 		<li>
 * 			Watson, G. N. (1995): <i>A Treatise on the Theory of Bessel Functions</i> <b>Cambridge University
 * 				Press</b>
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Bessel Function https://en.wikipedia.org/wiki/Bessel_function
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/specialfunction/README.md">Special Function Project</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/specialfunction/ode/README.md">Special Function Ordinary Differential Equations</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class BesselSecondKindEstimator implements org.drip.function.definition.R2ToR1
{

	/**
	 * Construct the Alpha Zero Asymptotic Version of BesselSecondKindEstimator
	 * 
	 * @return Alpha Zero Asymptotic Version of BesselSecondKindEstimator
	 */

	public static final BesselSecondKindEstimator AlphaZeroAsymptote()
	{
		return new BesselSecondKindEstimator()
		{
			@Override public double bigY (
				final double alpha,
				final double z)
				throws java.lang.Exception
			{
				if (0. != alpha ||
					!org.drip.numerical.common.NumberUtil.IsValid (z))
				{
					throw new java.lang.Exception
						("BesselSecondKindEstimator::AlphaZeroAsymptote => Invalid Inputs");
				}

				return 2. / java.lang.Math.PI * (java.lang.Math.log (0.5 * z) +
					org.drip.specialfunction.gamma.Definitions.EULER_MASCHERONI);
			}
		};
	}

	/**
	 * Construct the Alpha Non-Negative Integer Asymptotic Version of BesselSecondKindEstimator
	 * 
	 * @param gammaEstimator Gamma Estimator
	 * 
	 * @return Alpha Non-Negative Integer Asymptotic Version of BesselSecondKindEstimator
	 */

	public static final BesselSecondKindEstimator AlphaNonNegativeIntegerAsymptote (
		final org.drip.function.definition.R1ToR1 gammaEstimator)
	{
		return null == gammaEstimator ? null : new BesselSecondKindEstimator()
		{
			@Override public double bigY (
				final double alpha,
				final double z)
				throws java.lang.Exception
			{
				if (!org.drip.numerical.common.NumberUtil.IsNonNegativeInteger (alpha) ||
					!org.drip.numerical.common.NumberUtil.IsValid (z))
				{
					throw new java.lang.Exception
						("BesselSecondKindEstimator::AlphaNonNegativeIntegerAsymptote => Invalid Inputs");
				}

				return -1. * gammaEstimator.evaluate (alpha) / java.lang.Math.PI * java.lang.Math.pow (
					2. / z,
					alpha
				) + java.lang.Math.pow (
					0.5 * z,
					alpha
				) / java.lang.Math.tan (java.lang.Math.PI * alpha) / gammaEstimator.evaluate (alpha + 1.);
			}
		};
	}

	/**
	 * Construct the Alpha Negative Integer Asymptotic Version of BesselSecondKindEstimator
	 * 
	 * @param gammaEstimator Gamma Estimator
	 * 
	 * @return Alpha Negative Integer Asymptotic Version of BesselSecondKindEstimator
	 */

	public static final BesselSecondKindEstimator AlphaNegativeIntegerAsymptote (
		final org.drip.function.definition.R1ToR1 gammaEstimator)
	{
		return null == gammaEstimator ? null : new BesselSecondKindEstimator()
		{
			@Override public double bigY (
				final double alpha,
				final double z)
				throws java.lang.Exception
			{
				if (!org.drip.numerical.common.NumberUtil.IsNegativeInteger (alpha) ||
					!org.drip.numerical.common.NumberUtil.IsValid (z))
				{
					throw new java.lang.Exception
						("BesselSecondKindEstimator::AlphaNegativeIntegerAsymptote => Invalid Inputs");
				}

				return (0 == alpha % 2 ? -1. : 1.) * gammaEstimator.evaluate (-1. * alpha) /
					java.lang.Math.PI * java.lang.Math.pow (
						0.5 * z,
						alpha
					);
			}
		};
	}

	/**
	 * Construct the High z Asymptotic Version of BesselSecondKindEstimator
	 * 
	 * @return High z Asymptotic Version of BesselSecondKindEstimator
	 */

	public static final BesselSecondKindEstimator HighZAsymptote()
	{
		return new BesselSecondKindEstimator()
		{
			@Override public double bigY (
				final double alpha,
				final double z)
				throws java.lang.Exception
			{
				if (!org.drip.numerical.common.NumberUtil.IsValid (alpha) ||
					!org.drip.numerical.common.NumberUtil.IsValid (z))
				{
					throw new java.lang.Exception
						("BesselSecondKindEstimator::HighZAsymptote => Invalid Inputs");
				}

				return java.lang.Math.sqrt (2. / java.lang.Math.PI / z) * java.lang.Math.sin (
					z - 0.5 * java.lang.Math.PI * alpha - 0.25 * java.lang.Math.PI
				);
			}
		};
	}

	/**
	 * Evaluate Bessel Function Second Kind Y given Alpha and z
	 * 
	 * @param alpha Alpha
	 * @param z Z
	 *  
	 * @return Bessel Function Second Kind Y Value
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public abstract double bigY (
		final double alpha,
		final double z)
		throws java.lang.Exception;

	@Override public double evaluate (
		final double alpha,
		final double z)
		throws java.lang.Exception
	{
		return bigY (
			alpha,
			z
		);
	}
}
