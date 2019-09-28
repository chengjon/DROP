
package org.drip.portfolioconstruction.alm;

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
 * <i>ExpectedNonFinancialIncome</i> holds the Parameters required for estimating the Investor's
 * Non-Financial Income Profile.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AssetAllocationAnalyticsLibrary.md">Asset Allocation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction">Portfolio Construction</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction/alm">ALM</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExpectedNonFinancialIncome
{
	private double _incomeReplacementRate = java.lang.Double.NaN;

	/**
	 * ExpectedNonFinancialIncome Constructor
	 * 
	 * @param incomeReplacementRate The Pension Based Income Replacement Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public ExpectedNonFinancialIncome (
		final double incomeReplacementRate)
		throws java.lang.Exception
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (_incomeReplacementRate = incomeReplacementRate))
		{
			throw new java.lang.Exception ("ExpectedNonFinancialIncome Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Retirement Age Income Replacement Rate
	 * 
	 * @return The Retirement Age Income Replacement Rate
	 */

	public double incomeReplacementRate()
	{
		return _incomeReplacementRate;
	}

	/**
	 * Compute the Retirement Age Income Replacement Rate
	 * 
	 * @param age The Age whose Investment Phase is needed
	 * @param investorCliffSettings The Investor's Time Cliff Settings Instance
	 * 
	 * @return The Retirement Age Income Replacement Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Retirement Age Income Replacement Rate cannot be computed
	 */

	public double rate (
		final double age,
		final org.drip.portfolioconstruction.alm.InvestorCliffSettings investorCliffSettings)
		throws java.lang.Exception
	{
		if (null == investorCliffSettings)
		{
			throw new java.lang.Exception ("ExpectedNonFinancialIncome::rate => Invalid Inputs");
		}

		int agePhase = investorCliffSettings.phase (age);

		if (org.drip.portfolioconstruction.alm.InvestorCliffSettings.DATE_PHASE_BEFORE_RETIREMENT ==
			agePhase)
		{
			return 1.;
		}

		if (org.drip.portfolioconstruction.alm.InvestorCliffSettings.DATE_PHASE_AFTER_RETIREMENT == agePhase)
		{
			return _incomeReplacementRate;
		}

		return 0.;
	}
}
