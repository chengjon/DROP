
package org.drip.numerical.integration;

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
 * <i>Quadrature</i> contains the Array of Quadrature Abscissa and their corresponding Weights. The
 * References are:
 * 
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Briol, F. X., C. J. Oates, M. Girolami, and M. A. Osborne (2015): <i>Frank-Wolfe Bayesian
 * 				Quadrature: Probabilistic Integration with Theoretical Guarantees</i> <b>arXiv</b>
 * 		</li>
 * 		<li>
 * 			Forsythe, G. E., M. A. Malcolm, and C. B. Moler (1977): <i>Computer Methods for Mathematical
 * 				Computation</i> <b>Prentice Hall</b> Englewood Cliffs NJ
 * 		</li>
 * 		<li>
 * 			Leader, J. J. (2004): <i>Numerical Analysis and Scientific Computation</i> <b>Addison Wesley</b>
 * 		</li>
 * 		<li>
 * 			Stoer, J., and R. Bulirsch (1980): <i>Introduction to Numerical Analysis</i>
 * 				<b>Springer-Verlag</b> New York
 * 		</li>
 * 		<li>
 * 			Wikipedia (2019): Numerical Integration https://en.wikipedia.org/wiki/Numerical_integration
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical/README.md">Numerical Analysis</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/numerical/integration/README.md">R<sup>1</sup> R<sup>d</sup> Numerical Integration Schemes</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class Quadrature
{
	private double[] _weightArray = null;
	private double[] _abscissaArray = null;

	/**
	 * Quadrature Constructor
	 * 
	 * @param abscissaArray Array of the Abscissa
	 * @param weightArray Array of the Weights
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public Quadrature (
		final double[] abscissaArray,
		final double[] weightArray)
		throws java.lang.Exception
	{
		if (null == (_abscissaArray = abscissaArray) ||
			null == (_weightArray = weightArray))
		{
			throw new java.lang.Exception ("Quadrature Constructor => Invalid Inputs");
		}

		int size = _abscissaArray.length;

		if (0 == size || size != _weightArray.length ||
			!org.drip.numerical.common.NumberUtil.IsValid (_abscissaArray) ||
			!org.drip.numerical.common.NumberUtil.IsValid (_weightArray))
		{
			throw new java.lang.Exception ("Quadrature Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Array of the Abscissa
	 * 
	 * @return Array of the Abscissa
	 */

	public double[] abscissaArray()
	{
		return _abscissaArray;
	}

	/**
	 * Retrieve the Array of Weights
	 * 
	 * @return Array of Weights
	 */

	public double[] weightArray()
	{
		return _weightArray;
	}

	/**
	 * Integrate the Specified Integrand over the Nodes
	 * 
	 * @param r1ToR1Integrand The R<sup>1</sup> To R<sup>1</sup> Integrand
	 * 
	 * @return The Integrand Quadrature
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double integrate (
		final org.drip.function.definition.R1ToR1 r1ToR1Integrand)
		throws java.lang.Exception
	{
		if (null == r1ToR1Integrand)
		{
			throw new java.lang.Exception ("Quadrature::integrate => Invalid Inputs");
		}

		double quadrature = 0.;
		int nodeCount = _abscissaArray.length;

		for (int nodeIndex = 0; nodeIndex < nodeCount; ++nodeIndex)
		{
			quadrature = quadrature + _weightArray[nodeIndex] * r1ToR1Integrand.evaluate
				(_abscissaArray[nodeIndex]);
		}

		return quadrature;
	}
}
